package com.fiskaly.sdk.demo.jre;

import com.fiskaly.sdk.*;

public class Main {
  public static void main(String[] args) throws Exception {
    final String apiKey = System.getenv("FISKALY_API_KEY");
    final String apiSecret = System.getenv("FISKALY_API_SECRET");
    final FiskalyHttpClient client =
        new FiskalyHttpClient(apiKey, apiSecret,
        "https://kassensichv.fiskaly.dev/api/v2");
    final FiskalyHttpResponse response = client.request("GET", "/tss");
    System.out.println(response);
  }
}
