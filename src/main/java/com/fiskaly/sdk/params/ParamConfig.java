package com.fiskaly.sdk.params;

public class ParamConfig {

  public final String context;
  public final Config config;

  public ParamConfig(String context, Config config) {

    if (context == null || context.isEmpty()) {
      throw new IllegalArgumentException("Missing or empty \"context\" parameter");
    }

    if (config == null) {
      throw new IllegalArgumentException("Missing \"config\" parameter");
    }

    this.context = context;
    this.config = config;
  }

  public static class Config {

    public final int debugLevel;
    public final String debugFile;
    public final int clientTimeout;
    public final int smaersTimeout;
    public final String httpProxy;

    public Config(int debugLevel, String debugFile, int clientTimeout, int smaersTimeout, String httpProxy) {

      this.debugLevel = debugLevel;
      this.debugFile = debugFile;
      this.clientTimeout = clientTimeout;
      this.smaersTimeout = smaersTimeout;
      this.httpProxy = httpProxy;
    }
  }
}
