package com.fiskaly.sdk;

import com.fiskaly.sdk.client.ClientErrors;
import com.fiskaly.sdk.client.ClientLibrary;
import com.fiskaly.sdk.factories.ExceptionFactory;
import com.fiskaly.sdk.jsonrpc.JsonRpcRequest;
import com.fiskaly.sdk.jsonrpc.JsonRpcResponse;
import com.fiskaly.sdk.params.ParamCreateContext;
import com.fiskaly.sdk.params.ParamRequest;
import com.fiskaly.sdk.results.ResultCreateContext;
import com.fiskaly.sdk.results.ResultRequest;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

public class FiskalyHttpClient {
  private String context;

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

  public FiskalyHttpResponse request(
      final String method,
      final String path,
      final byte[] body,
      final Map<String, ?> query,
      final Map<String, String> headers)
      throws IOException, FiskalyHttpTimeoutException, FiskalyClientException,
          FiskalyHttpException {
    final ParamRequest params =
        new ParamRequest(
            this.context, new ParamRequest.Request(method, path, body, query, headers));

    final JsonRpcRequest request = new JsonRpcRequest("request", params);
    final JsonRpcResponse<ResultRequest> response = doInvoke(request, ResultRequest.class);

    this.context = response.result.context;

    return new FiskalyHttpResponse(response.result.response);
  }

  public FiskalyHttpResponse request(
      final String method, final String path, final byte[] body, final Map<String, ?> query)
      throws IOException, FiskalyHttpException, FiskalyClientException,
          FiskalyHttpTimeoutException {
    return this.request(method, path, body, query, null);
  }

  public FiskalyHttpResponse request(final String method, final String path, final byte[] body)
      throws IOException, FiskalyHttpException, FiskalyClientException,
          FiskalyHttpTimeoutException {
    return this.request(method, path, body, null, null);
  }

  public FiskalyHttpResponse request(final String method, final String path)
      throws IOException, FiskalyHttpException, FiskalyClientException,
          FiskalyHttpTimeoutException {
    return this.request(method, path, null, null, null);
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
