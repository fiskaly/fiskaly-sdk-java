package com.fiskaly.sdk.jsonrpc;

import static org.junit.Assert.*;

import com.fiskaly.sdk.factories.GsonFactory;
import com.google.gson.Gson;
import java.util.Map;
import org.junit.Test;

public class JsonRpcRequestTest {
  private static final Gson GSON = GsonFactory.createGson();

  @Test
  public void missingMethod() {
    try {
      new JsonRpcRequest(null);
      fail("Expected an IllegalArgumentException to be thrown");
    } catch (IllegalArgumentException e) {
      assertEquals("Missing or empty \"method\" parameter", e.getMessage());
    }
  }

  @Test
  public void basicRequest() {
    final JsonRpcRequest r = new JsonRpcRequest("foo");
    assertNotNull(r);
    assertNotNull(r.method);
    assertNotNull(r.id);
    assertNotNull(r.jsonrpc);
    assertEquals("2.0", r.jsonrpc);
    final String json = GSON.toJson(r);
    assertNotNull(json);
    final Map map = GSON.fromJson(json, Map.class);
    assertEquals("2.0", map.get("jsonrpc"));
    assertNotNull(map.get("id"));
    assertNotNull(map.get("method"));
    assertNull(map.get("params"));
  }
}
