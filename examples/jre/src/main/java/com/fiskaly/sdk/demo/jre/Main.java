package com.fiskaly.sdk.demo.jre;

import com.fiskaly.sdk.*;
import java.util.UUID;

import com.fiskaly.sdk.factories.GsonFactory;
import com.google.gson.Gson;
import net.iharder.Base64;

public class Main {
  static String tssUUID = "";
  static String adminPuk = "";
  static FiskalyHttpClient client = null;
  private static final Gson GSON = GsonFactory.createGson();

  public static void main(String[] args) throws Exception {
    final String apiKey = System.getenv("FISKALY_API_KEY");
    final String apiSecret = System.getenv("FISKALY_API_SECRET");
    client =
        new FiskalyHttpClient(apiKey, apiSecret, "https://kassensichv.fiskaly.dev/api/v2", "https://kassensichv-middleware.fiskaly.dev");
    final FiskalyHttpResponse response = client.request("GET", "/tss");
    System.out.println("List TSS response:");
    System.out.println(response);
    createTSS();
    personalizeTSS();
  }

  public static void createTSS() throws Exception {
    UUID uuid = UUID.randomUUID();
    tssUUID = uuid.toString();
    final FiskalyHttpResponse response = client.request("PUT", "/tss/" + tssUUID, "{}".getBytes());
    System.out.println("Create TSS response:");
    System.out.println(response);
    final String decodedBody = new String(response.body);
    final CreateTSSResponse body = GSON.fromJson(decodedBody, CreateTSSResponse.class);
    System.out.println("admin puk = "+body.admin_puk);
    adminPuk = body.admin_puk;
  }

  public static void setTSSState(String state) throws Exception {
    final String body = "{ \"state\" : \""+state+"\" }";
    System.out.println("Setting TSS state to "+state);
    System.out.println(body);
    final FiskalyHttpResponse response = client.request("PATCH", "/tss/" + tssUUID, body.getBytes());
    System.out.println("Set TSS state response:");
    System.out.println(response);
  }

  public static void personalizeTSS() throws Exception {
      setTSSState("UNINITIALIZED");
  }
}

class CreateTSSResponse {
  public final String admin_puk;

  public CreateTSSResponse(final String admin_puk) {
    this.admin_puk = admin_puk;
  }

  @Override
  public String toString() {
    return "Response{"
        + "admin_puk="
        + admin_puk
        + '}';
  }
}
