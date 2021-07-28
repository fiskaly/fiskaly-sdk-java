package com.fiskaly.sdk.factories;

import com.fiskaly.sdk.FiskalyClientException;
import com.fiskaly.sdk.FiskalyHttpException;
import com.fiskaly.sdk.jsonrpc.JsonRpcResponse;
import com.fiskaly.sdk.results.ErrorData;
import com.fiskaly.sdk.results.FiskalyApiError;
import com.google.gson.Gson;
import java.io.IOException;
import net.iharder.Base64;
import java.util.*;

public abstract class ExceptionFactory {
  private static final Gson GSON = GsonFactory.createGson();

  private ExceptionFactory() {}

  public static <T> FiskalyHttpException buildHttpException(final JsonRpcResponse<T> response)
      throws IOException {
    final ErrorData errorData = GSON.fromJson(GSON.toJson(response.error.data), ErrorData.class);
    String requestId = null;
    //request id is in x-request-id header in v1, request-id in v2.
    List<?> requestIds = errorData.response.headers.get("x-request-id");
    if (requestIds == null) {
    	requestIds = errorData.response.headers.get("request-id");
    }
    if (requestIds != null) {
    	requestId = (String)requestIds.get(0);
    }
    final String decodedBody = new String(Base64.decode(errorData.response.body), "UTF-8");
    final FiskalyApiError errorBody = GSON.fromJson(decodedBody, FiskalyApiError.class);

    return new FiskalyHttpException(
        errorBody.statusCode, errorBody.error, errorBody.message, errorBody.code, requestId);
  }

  public static <T> FiskalyClientException buildClientException(final JsonRpcResponse<T> response) {
    return new FiskalyClientException(
        response.error.code, response.error.message, GSON.toJson(response.error.data));
  }
}
