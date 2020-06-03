package com.fiskaly.sdk.results;

public class ResultVersion {
  public final Client client;
  public final SMAERS smaers;

  public ResultVersion(final Client client, final SMAERS smaers) {
    this.client = client;
    this.smaers = smaers;
  }

  public static class Client {
    public final String version;

    public Client(final String version) {
      this.version = version;
    }

    @Override
    public String toString() {
      return "Client{" + "version='" + version + '\'' + '}';
    }
  }

  public static class SMAERS {
    public final String version;

    public SMAERS(final String version) {
      this.version = version;
    }

    @Override
    public String toString() {
      return "SMAERS{" + "version='" + version + '\'' + '}';
    }
  }

  @Override
  public String toString() {
    return "ResultVersion{" + "client=" + client + ", \"smaers\"=" + smaers + '}';
  }
}
