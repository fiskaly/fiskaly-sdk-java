package com.fiskaly.sdk.client;

import static org.junit.Assert.*;

import com.fiskaly.sdk.jsonrpc.JsonRpcError;
import com.fiskaly.sdk.jsonrpc.JsonRpcRequest;
import com.fiskaly.sdk.jsonrpc.JsonRpcResponse;
import com.fiskaly.sdk.results.ResultVersion;
import java.util.Map;
import org.junit.Test;

public class ClientLibraryTest {
  @Test
  public void invokeEcho() {
    final String version = "1.2.3";
    final ResultVersion params = new ResultVersion(new ResultVersion.Client(version), new ResultVersion.SMAERS(version));
    final JsonRpcRequest req = new JsonRpcRequest("echo", params);
    final JsonRpcResponse<ResultVersion> res = ClientLibrary.invoke(req, ResultVersion.class);
    assertNotNull(res);
    assertNotNull(res.result);
    assertNotNull(res.result.client);
    assertNotNull(res.result.client.version);
    assertEquals(version, res.result.client.version);
  }

  @Test
  public void invokeVersionAsString() {
    final String res =
        ClientLibrary.invoke("{\"jsonrpc\":\"2.0\",\"id\":1,\"method\":\"version\"}");
    assertNotNull(res);
    assertTrue(res.contains("version"));
  }

  @Test
  public void invokeVersion() {
    final JsonRpcRequest req = new JsonRpcRequest("version");
    final JsonRpcResponse<ResultVersion> res = ClientLibrary.invoke(req, ResultVersion.class);
    assertNotNull(res);
    assertNotNull(res.id);
    assertEquals(res.id, req.id);
    assertNull(res.error);
    assertNotNull(res.result);
    assertNotNull(res.result.client);
    assertNotNull(res.result.client.version);
  }

  @Test
  public void invokeInvalidMethod() {
    final JsonRpcRequest req = new JsonRpcRequest("some-invalid-method");
    final JsonRpcResponse<Map<?, ?>> res = ClientLibrary.invoke(req, Map.class);
    assertNotNull(res);
    assertNotNull(res.id);
    assertEquals(res.id, req.id);
    assertNull(res.result);
    final JsonRpcError error = res.error;
    assertNotNull(error);
    assertEquals(-32601, error.code);
    assertNotNull(error.message);
    assertEquals("Method not found", error.message);
  }
}
