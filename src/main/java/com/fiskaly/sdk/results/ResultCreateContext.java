package com.fiskaly.sdk.results;

public class ResultCreateContext {
  public final String context;

  public ResultCreateContext(final String context) {
    this.context = context;
  }

  @Override
  public String toString() {
    return "ResultCreateContext{" + "context='" + context + '\'' + '}';
  }
}
