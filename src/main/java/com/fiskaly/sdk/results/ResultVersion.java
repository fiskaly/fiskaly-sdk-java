package com.fiskaly.sdk.results;

public class ResultVersion {
  public final Client client;

  public ResultVersion(final Client client) {
    this.client = client;
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

  @Override
  public String toString() {
    return "ResultVersion{" + "client=" + client + '}';
  }
}
