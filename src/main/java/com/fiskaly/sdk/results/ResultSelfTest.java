package com.fiskaly.sdk.results;

public class ResultSelfTest {
  public final String proxy;
  public final String backend;
  public final String smaers;

  public ResultSelfTest(final String proxy, final String backend, final String smaers) {
    this.proxy = proxy;
    this.backend = backend;
    this.smaers = smaers;
  }

  @Override
  public String toString() {
    return "ResultSelfTest{"
        + "\"proxy\"="
        + proxy
        + ", \"backend\"="
        + backend
        + ", \"smaers\"="
        + smaers
        + '}';
  }
}
