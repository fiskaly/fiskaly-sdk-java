package com.fiskaly.sdk.params;

import java.net.URI;

public class ParamCreateContext {
  public final String apiKey;
  public final String apiSecret;
  public final URI baseUrl;
  public final String email;
  public final String password;
  public final String organizationId;
  public final String environment;

  public ParamCreateContext(
      final String apiKey,
      final String apiSecret,
      final URI baseUrl,
      final String email,
      final String password,
      final String organizationId,
      final String environment) {
    if (email == null || email.isEmpty()) {
      if (apiKey == null || apiKey.isEmpty()) {
        throw new IllegalArgumentException("Missing or empty \"apiKey\" parameter");
      }
      this.apiKey = apiKey;

      if (apiSecret == null || apiSecret.isEmpty()) {
        throw new IllegalArgumentException("Missing or empty \"apiSecret\" parameter");
      }
      this.apiSecret = apiSecret;
    } else if (password == null || password.isEmpty()) {
      throw new IllegalArgumentException(
          "\"password\" must be provided in combination with \"email\"");
    } else {
      this.apiKey = apiKey == null ? "" : apiKey;
      this.apiSecret = apiSecret == null ? "" : apiSecret;
    }

    if (baseUrl == null || baseUrl.toString().isEmpty()) {
      throw new IllegalArgumentException("Missing or empty \"baseUrl\" parameter");
    }
    this.baseUrl = baseUrl;

    this.email = email == null ? "" : email;
    this.password = password == null ? "" : password;
    this.organizationId = organizationId == null ? "" : organizationId;
    this.environment = environment == null ? "" : environment;
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
        + ", email='"
        + email
        + '\''
        + ", password='"
        + password
        + '\''
        + ", organizationId='"
        + organizationId
        + '\''
        + ", environment='"
        + environment
        + '\''
        + ", baseUrl="
        + baseUrl
        + '}';
  }
}
