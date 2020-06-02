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

  public ParamConfig.Config config(
          final int debug_level,
          final String debug_file,
          final int client_timeout,
          final int smaers_timeout
  ) throws FiskalyHttpException, FiskalyHttpTimeoutException, FiskalyClientException, IOException {
    final ParamConfig params =
            new ParamConfig(
                    this.context, new ParamConfig.Config(debug_level, debug_file, client_timeout, smaers_timeout));

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
      final String destination_file)
      throws IOException, FiskalyHttpTimeoutException, FiskalyClientException,
          FiskalyHttpException {
    final ParamRequest params =
        new ParamRequest(
            this.context, new ParamRequest.Request(method, path, body, query, headers, destination_file));

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
          final String destination_file)
          throws IOException, FiskalyHttpException, FiskalyClientException,
          FiskalyHttpTimeoutException {
    return this.request(method, path, body, query, null, destination_file);
  }

  public FiskalyHttpResponse request(
      final String method, final String path, final byte[] body, final Map<String, ?> query)
      throws IOException, FiskalyHttpException, FiskalyClientException,
          FiskalyHttpTimeoutException {
    return this.request(method, path, body, query, null, null);
  }

  public FiskalyHttpResponse request(
          final String method,
          final String path,
          final byte[] body,
          final String destination_file)
          throws IOException, FiskalyHttpException, FiskalyClientException,
          FiskalyHttpTimeoutException {
    return this.request(method, path, body, null, null, destination_file);
  }

  public FiskalyHttpResponse request(final String method, final String path, final byte[] body)
      throws IOException, FiskalyHttpException, FiskalyClientException,
          FiskalyHttpTimeoutException {
    return this.request(method, path, body, null, null, null);
  }

  public FiskalyHttpResponse request(final String method, final String path, final String destination_file)
          throws IOException, FiskalyHttpException, FiskalyClientException,
          FiskalyHttpTimeoutException {
    return this.request(method, path, null, null, null, destination_file);
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
