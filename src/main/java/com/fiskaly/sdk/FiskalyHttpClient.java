package com.fiskaly.sdk;

import com.fiskaly.sdk.client.ClientErrors;
import com.fiskaly.sdk.client.ClientLibrary;
import com.fiskaly.sdk.factories.ExceptionFactory;
import com.fiskaly.sdk.jsonrpc.JsonRpcRequest;
import com.fiskaly.sdk.jsonrpc.JsonRpcResponse;
import com.fiskaly.sdk.params.ParamConfig;
import com.fiskaly.sdk.params.ParamCreateContext;
import com.fiskaly.sdk.params.ParamRequest;
import com.fiskaly.sdk.results.ResultConfig;
import com.fiskaly.sdk.results.ResultCreateContext;
import com.fiskaly.sdk.results.ResultRequest;
import com.fiskaly.sdk.results.ResultVersion;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

public class FiskalyHttpClient {
  private transient String context;

  public FiskalyHttpClient(final String apiKey, final String apiSecret, final URI baseUrl)
      throws IOException, FiskalyHttpTimeoutException, FiskalyClientException,
          FiskalyHttpException {
    final ParamCreateContext params = new ParamCreateContext(apiKey, apiSecret, baseUrl);
    final JsonRpcRequest request = new JsonRpcRequest("create-context", params);
    final JsonRpcResponse<ResultCreateContext> response =
        doInvoke(request, ResultCreateContext.class);
    this.context = response.result.context;
  }

  public FiskalyHttpClient(final String apiKey, final String apiSecret, final String baseUrl)
      throws IOException, URISyntaxException, FiskalyHttpException, FiskalyClientException,
          FiskalyHttpTimeoutException {
    this(apiKey, apiSecret, new URI(baseUrl));
  }

  public ResultVersion version()
      throws FiskalyHttpException, FiskalyHttpTimeoutException, FiskalyClientException,
          IOException {

    final JsonRpcRequest request = new JsonRpcRequest("version", null);
    final JsonRpcResponse<ResultVersion> response = doInvoke(request, ResultVersion.class);

    return response.result;
  }

  public Object echo(Object object)
      throws FiskalyHttpException, FiskalyHttpTimeoutException, FiskalyClientException,
          IOException {

    final JsonRpcRequest request = new JsonRpcRequest("echo", object);
    final JsonRpcResponse<Object> response = doInvoke(request, Object.class);

    return response.result;
  }

  public ParamConfig.Config config(
      final int debugLevel,
      final String debugFile,
      final int clientTimeout,
      final int smaersTimeout)
      throws FiskalyHttpException, FiskalyHttpTimeoutException, FiskalyClientException,
          IOException {
    final ParamConfig params =
        new ParamConfig(
            this.context,
            new ParamConfig.Config(debugLevel, debugFile, clientTimeout, smaersTimeout));

    final JsonRpcRequest request = new JsonRpcRequest("config", params);
    final JsonRpcResponse<ResultConfig> response = doInvoke(request, ResultConfig.class);

    this.context = response.result.context;

    return response.result.config;
  }

  public FiskalyHttpResponse request(
      final String method,
      final String path,
      final byte[] body,
      final Map<String, ?> query,
      final Map<String, String> headers,
      final String destinationFile)
      throws IOException, FiskalyHttpTimeoutException, FiskalyClientException,
          FiskalyHttpException {
    final ParamRequest params =
        new ParamRequest(
            this.context,
            new ParamRequest.Request(method, path, body, query, headers, destinationFile));

    final JsonRpcRequest request = new JsonRpcRequest("request", params);
    final JsonRpcResponse<ResultRequest> response = doInvoke(request, ResultRequest.class);

    this.context = response.result.context;

    return new FiskalyHttpResponse(response.result.response);
  }

  public FiskalyHttpResponse request(
      final String method,
      final String path,
      final byte[] body,
      final Map<String, ?> query,
      final Map<String, String> headers)
      throws IOException, FiskalyHttpTimeoutException, FiskalyClientException,
          FiskalyHttpException {
    return this.request(method, path, body, query, headers, null);
  }

  public FiskalyHttpResponse request(
      final String method,
      final String path,
      final byte[] body,
      final Map<String, ?> query,
      final String destinationFile)
      throws IOException, FiskalyHttpException, FiskalyClientException,
          FiskalyHttpTimeoutException {
    return this.request(method, path, body, query, null, destinationFile);
  }

  public FiskalyHttpResponse request(
      final String method, final String path, final byte[] body, final Map<String, ?> query)
      throws IOException, FiskalyHttpException, FiskalyClientException,
          FiskalyHttpTimeoutException {
    return this.request(method, path, body, query, null, null);
  }

  public FiskalyHttpResponse request(
      final String method, final String path, final byte[] body, final String destinationFile)
      throws IOException, FiskalyHttpException, FiskalyClientException,
          FiskalyHttpTimeoutException {
    return this.request(method, path, body, null, null, destinationFile);
  }

  public FiskalyHttpResponse request(final String method, final String path, final byte[] body)
      throws IOException, FiskalyHttpException, FiskalyClientException,
          FiskalyHttpTimeoutException {
    return this.request(method, path, body, null, null, null);
  }

  public FiskalyHttpResponse request(
      final String method, final String path, final String destinationFile)
      throws IOException, FiskalyHttpException, FiskalyClientException,
          FiskalyHttpTimeoutException {
    return this.request(method, path, null, null, null, destinationFile);
  }

  public FiskalyHttpResponse request(final String method, final String path)
      throws IOException, FiskalyHttpException, FiskalyClientException,
          FiskalyHttpTimeoutException {
    return this.request(method, path, null, null, null, null);
  }

  private static <T> JsonRpcResponse<T> doInvoke(
      final JsonRpcRequest request, final Class<?> resultClass)
      throws IOException, FiskalyHttpException, FiskalyHttpTimeoutException,
          FiskalyClientException {

    final JsonRpcResponse<T> response = ClientLibrary.invoke(request, resultClass);

    if (response.error != null) {
      if (response.error.code == ClientErrors.HTTP_ERROR) {
        throw ExceptionFactory.buildHttpException(response);
      } else if (response.error.code == ClientErrors.HTTP_TIMEOUT_ERROR) {
        throw new FiskalyHttpTimeoutException(response.error.message);
      }

      throw ExceptionFactory.buildClientException(response);
    }

    return response;
  }
}
