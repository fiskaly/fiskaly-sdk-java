package com.fiskaly.sdk.demo.jre;

import com.fiskaly.sdk.*;

import java.io.IOException;
import java.util.Collections;
import java.util.UUID;

import com.fiskaly.sdk.factories.GsonFactory;
import com.google.gson.Gson;

public class Main {
  static String tssUUID = "";
  static String clientUUID = "";
  static String adminPUK = "";
  static FiskalyHttpClient client = null;
  static String adminPIN = "0123456789";
  static String transactionUUID = "";
  static int transactionRevision = 0;
  private static final Gson GSON = GsonFactory.createGson();

  public static void main(String[] args) throws Exception {
    final String apiKey = System.getenv("FISKALY_API_KEY");
    final String apiSecret = System.getenv("FISKALY_API_SECRET");
    client =
            new FiskalyHttpClient(apiKey, apiSecret, "https://kassensichv.io/api/v1");
    listTSS();
    createTSSV1();
    createClient();
    createTransactionV1();
    finishTransactionV1();

      //now do something similar on v2
    final String apiKeyv2 = System.getenv("FISKALY_API_KEY_V2");
    final String apiSecretv2 = System.getenv("FISKALY_API_SECRET_V2");
    //set these to use an existing TSS instead of creating a new one
    final String existingTSS = System.getenv("FISKALY_TSS_UUID_V2");
    //set either a PIN (if the TSS already has one set) or a PUK (if the TSS doesn't have a PIN set)
    final String existingTSSAdminPIN = System.getenv("FISKALY_TSS_ADMIN_PIN");
    final String existingTSSPUK = System.getenv("FISKALY_TSS_PUK");
    client =
        new FiskalyHttpClient(apiKeyv2, apiSecretv2, "https://kassensichv.fiskaly.dev/api/v2", "https://kassensichv-middleware.fiskaly.dev");
    listTSS();
    if (existingTSS == null) {
      createTSS();
    } else {
      tssUUID = existingTSS;
      adminPUK = existingTSSPUK;
    }
    if (existingTSSAdminPIN == null) {
      personalizeTSS();
      changeAdminPIN();
    } else {
      adminPIN = existingTSSAdminPIN;
    }
    authenticateAdmin();
    initializeTSS();
    createClient();
    updateClient();
    createTransaction();
    updateTransaction();
    finishTransaction();
    //don't disable a TSS we didn't create
    if (existingTSS == null) {
      disableTSS();
    }
  }

  private static void listTSS() throws IOException, FiskalyHttpException, FiskalyClientException, FiskalyHttpTimeoutException {
    final FiskalyHttpResponse response = client.request("GET", "/tss");
    System.out.println("List TSS response:");
    System.out.println(response);
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
    adminPUK = body.admin_puk;
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

  public static void changeAdminPIN() throws Exception {
    final String body = "{ \"admin_puk\" : \""+ adminPUK +"\", \"new_admin_pin\" : \""+adminPIN+"\" }";
    System.out.println("Setting admin PIN to "+adminPIN);
    System.out.println(body);
    final FiskalyHttpResponse response = client.request("PATCH", "/tss/" + tssUUID + "/admin", body.getBytes());
    System.out.println("Set admin PIN response:");
    System.out.println(response);
  }

  public static void authenticateAdmin() throws Exception {
    final String body = "{ \"admin_pin\" : \""+adminPIN+"\" }";
    System.out.println("Authenticating admin");
    System.out.println(body);
    final FiskalyHttpResponse response = client.request("POST", "/tss/" + tssUUID + "/admin/auth", body.getBytes());
    System.out.println("Authenticate admin response:");
    System.out.println(response);
  }

  public static void initializeTSS() throws Exception {
    setTSSState("INITIALIZED");
  }

  public static void createClient() throws Exception {
    UUID uuid = UUID.randomUUID();
    clientUUID = uuid.toString();
    final String body = "{ \"serial_number\": \"JRE Test Client Serial\"}";
    System.out.println("Creating client");
    System.out.println(body);
    final FiskalyHttpResponse response = client.request("PUT", "/tss/" + tssUUID + "/client/" + clientUUID, body.getBytes());
    System.out.println("Create Client response:");
    System.out.println(response);
  }

  public static void updateClient() throws Exception {
    final String body = "{ \"state\": \"REGISTERED\", \"metadata\": {\"custom_field\": \"custom_value\"}}";
    System.out.println("Updating client");
    System.out.println(body);
    final FiskalyHttpResponse response = client.request("PATCH", "/tss/" + tssUUID + "/client/" + clientUUID, body.getBytes());
    System.out.println("Update Client response:");
    System.out.println(response);
  }

  public static FiskalyHttpResponse transactionRequest(String body) throws Exception {
    transactionRevision++; //transaction revision number starts at 1
    return client.request("PUT", "/tss/" + tssUUID + "/tx/" + transactionUUID, body.getBytes(), Collections.singletonMap("tx_revision", transactionRevision));
  }

  public static void createTransaction() throws Exception {
    UUID uuid = UUID.randomUUID();
    transactionUUID = uuid.toString();
    final String body = "{\"state\": \"ACTIVE\",\n" +
            "            \"client_id\": \" + clientUUID\"\n" +
            "        }";
    System.out.println("Creating transaction");
    System.out.println(body);
    transactionRevision = 0;
    final FiskalyHttpResponse response = transactionRequest(body);
    System.out.println("Create Transaction response:");
    System.out.println(response);
  }

  public static void updateTransaction() throws Exception {
    final String body = "{\n" +
            "            \"schema\": {\n" +
            "                \"standard_v1\": {\n" +
            "                    \"receipt\": {\n" +
            "                        \"receipt_type\": \"RECEIPT\",\n" +
            "                        \"amounts_per_vat_rate\": {\n" +
            "                            [\n" +
            "                                \"vat_rate\": \"NORMAL\",\n" +
            "                                \"amount\": \"21.42\"\n" +
            "                            ]\n" +
            "                        },\n" +
            "                        \"amounts_per_payment_type\": {\n" +
            "                            [\n" +
            "                                \"payment_type\": \"NON_CASH\",\n" +
            "                                \"amount\": \"21.42\"\n" +
            "                            ]\n" +
            "                        }\n" +
            "                    }\n" +
            "                }\n" +
            "            },\n" +
            "            \"state\": \"ACTIVE\",\n" +
            "            \"client_id\": clientUUID\n" +
            "        }";
    System.out.println("Updating transaction");
    System.out.println(body);
    final FiskalyHttpResponse response = transactionRequest(body);
    System.out.println("Update Transaction response:");
    System.out.println(response);
  }

  public static void finishTransaction() throws Exception {
    final String body = "{\n" +
            "            \"schema\": {\n" +
            "                \"standard_v1\": {\n" +
            "                    \"receipt\": {\n" +
            "                        \"receipt_type\": \"RECEIPT\",\n" +
            "                        \"amounts_per_vat_rate\": {\n" +
            "                            [\n" +
            "                                \"vat_rate\": \"NORMAL\",\n" +
            "                                \"amount\": \"21.42\"\n" +
            "                            ]\n" +
            "                        },\n" +
            "                        \"amounts_per_payment_type\": {\n" +
            "                            [\n" +
            "                                \"payment_type\": \"NON_CASH\",\n" +
            "                                \"amount\": \"21.42\"\n" +
            "                            ]\n" +
            "                        }\n" +
            "                    }\n" +
            "                }\n" +
            "            },\n" +
            "            \"state\": \"FINISHED\",\n" +
            "            \"client_id\": clientUUID\n" +
            "        }";
    System.out.println("Finishing transaction");
    System.out.println(body);
    final FiskalyHttpResponse response = transactionRequest(body);
    System.out.println("Finish Transaction response:");
    System.out.println(response);
  }

  public static void disableTSS() throws Exception {
    setTSSState("DISABLED");
  }

  ///V1 versions
  public static void createTSSV1() throws Exception {
    UUID uuid = UUID.randomUUID();
    tssUUID = uuid.toString();
    final String body = "{\n" +
            "            \"description\": \"JRE Test TSS\",\n" +
            "            \"state\": \"INITIALIZED\"\n" +
            "        }";
    final FiskalyHttpResponse response = client.request("PUT", "/tss/" + tssUUID, body.getBytes());
    System.out.println("Create TSS response:");
    System.out.println(response);
  }

  public static void createTransactionV1() throws Exception {
    UUID uuid = UUID.randomUUID();
    transactionUUID = uuid.toString();
    final String body = "{\"state\": \"ACTIVE\",\n" +
            "            \"client_id\": \"" + clientUUID +"\"\n" +
            "        }";
    System.out.println("Creating V1 transaction");
    System.out.println(body);
    transactionRevision = 0;
    final FiskalyHttpResponse response = client.request("PUT", "/tss/" + tssUUID + "/tx/" + transactionUUID, body.getBytes());

    System.out.println("Create V1 Transaction response:");
    System.out.println(response);
  }

  public static void finishTransactionV1() throws Exception {
    final String body = "{\n" +
            "            \"state\": \"FINISHED\",\n" +
            "            \"client_id\": \""+clientUUID+"\",\n" +
            "            \"schema\": {\n" +
            "                \"standard_v1\": {\n" +
            "                    \"receipt\": {\n" +
            "                        \"receipt_type\": \"RECEIPT\",\n" +
            "                        \"amounts_per_vat_rate\": [\n" +
            "                            {\"vat_rate\": \"19\", \"amount\": \"14.28\"}\n" +
            "                        ],\n" +
            "                        \"amounts_per_payment_type\": [\n" +
            "                            {\"payment_type\": \"NON_CASH\", \"amount\": \"14.28\"}\n" +
            "                        ]\n" +
            "                    }\n" +
            "                }\n" +
            "            }\n" +
            "        }";
    System.out.println("Finishing transaction");
    System.out.println(body);
    final FiskalyHttpResponse response = client.request("PUT", "/tss/" + tssUUID + "/tx/" + transactionUUID, body.getBytes(), Collections.singletonMap("last_revision", 1));
    System.out.println("Finish Transaction response:");
    System.out.println(response);
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
