package com.fiskaly.sdk.results;

public class ResultRequest {
  public final Response response;
  public final String context;

  public ResultRequest(final Response response, final String context) {
    this.response = response;
    this.context = context;
  }

  @Override
  public String toString() {
    return "ResultRequest{" + "response=" + response + '}';
  }
}
