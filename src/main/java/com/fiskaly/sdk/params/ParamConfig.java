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

    public final int debug_level;
    public final String debug_file;
    public final int client_timeout;
    public final int smaers_timeout;

    public Config(int debug_level, String debug_file, int client_timeout, int smaers_timeout) {

      this.debug_level = debug_level;
      this.debug_file = debug_file;
      this.client_timeout = client_timeout;
      this.smaers_timeout = smaers_timeout;
    }
  }
}
