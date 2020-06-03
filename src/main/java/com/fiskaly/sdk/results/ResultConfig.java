package com.fiskaly.sdk.results;

import com.fiskaly.sdk.params.ParamConfig;

public class ResultConfig {

  public final ParamConfig.Config config;
  public final String context;

  public ResultConfig(final ParamConfig.Config config, final String context) {
    this.config = config;
    this.context = context;
  }

  @Override
  public String toString() {
    return "ResultConfig{" + "config=" + config + '}';
  }
}
