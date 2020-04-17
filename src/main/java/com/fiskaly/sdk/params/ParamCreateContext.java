package com.fiskaly.sdk.params;

import java.net.URI;

public class ParamCreateContext {
  public final String apiKey;
  public final String apiSecret;
  public final URI baseUrl;

  public ParamCreateContext(final String apiKey, final String apiSecret, final URI baseUrl) {
    if (apiKey == null || apiKey.isEmpty()) {
      throw new IllegalArgumentException("Missing or empty \"apiKey\" parameter");
    }
    this.apiKey = apiKey;

    if (apiSecret == null || apiSecret.isEmpty()) {
      throw new IllegalArgumentException("Missing or empty \"apiSecret\" parameter");
    }
    this.apiSecret = apiSecret;

    if (baseUrl == null || baseUrl.toString().isEmpty()) {
      throw new IllegalArgumentException("Missing or empty \"baseUrl\" parameter");
    }
    this.baseUrl = baseUrl;
  }

  @Override
  public String toString() {
    return "ParamCreateContext{"
        + "apiKey='"
        + apiKey
        + '\''
        + ", apiSecret='"
        + apiSecret
        + '\''
        + ", baseUrl="
        + baseUrl
        + '}';
  }
}
