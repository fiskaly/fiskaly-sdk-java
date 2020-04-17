package com.fiskaly.sdk.client;

import com.fiskaly.sdk.factories.GsonFactory;
import com.fiskaly.sdk.jsonrpc.JsonRpcRequest;
import com.fiskaly.sdk.jsonrpc.JsonRpcResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.jna.Native;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;
import java.lang.reflect.Type;
import java.util.Map;

public abstract class ClientLibrary {
  private static final String LIB_PREFIX = "com.fiskaly.client";
  private static final String UTF_8 = "UTF8";
  private static final String PROP_LIB_PATH = "jna.library.path";
  private static final Gson GSON = GsonFactory.createGson();

  public static <T> JsonRpcResponse<T> invoke(
      final JsonRpcRequest request, final Class<?> resultClass) {
    final String requestString = GSON.toJson(request);
    final String invocationResponse = invoke(requestString);

    final Type responseType =
        TypeToken.getParameterized(JsonRpcResponse.class, resultClass).getType();

    return GSON.fromJson(invocationResponse, responseType);
  }

  public static JsonRpcResponse invoke(final JsonRpcRequest req) {
    return invoke(req, Map.class);
  }

  public static String invoke(final String req) {
    final Pointer resPtr = _fiskaly_client_invoke(req);
    final String resStr = resPtr.getString(0, UTF_8);

    _fiskaly_client_free(resPtr);

    return resStr;
  }

  private static String getLibName() {
    if (Platform.isAndroid()) {
      return LIB_PREFIX + "-" + ClientLibraryVersion.VERSION;
    }

    String os;
    String extension;

    if (Platform.isLinux()) {
      os = "linux";
      extension = ".so";
    } else if (Platform.isWindows()) {
      os = "windows";
      extension = ".dll";
    } else if (Platform.isMac()) {
      os = "darwin";
      extension = ".dylib";
    } else {
      throw new IllegalStateException("OS type not supported: " + Platform.getOSType());
    }

    String arch = Platform.ARCH;

    if (arch.equals("x86")) {
      arch = "386";
    } else if (arch.equals("x86-64")) {
      arch = "amd64";
    }

    return LIB_PREFIX + "-" + os + "-" + arch + "-" + ClientLibraryVersion.VERSION + extension;
  }

  private static void setLibSearchPath() {
    if (Platform.isAndroid()) {
      return; // don't ever try to fix the library search path on android
    }

    if (System.getProperty(PROP_LIB_PATH) == null) {
      System.setProperty(PROP_LIB_PATH, "."); // look in current directory
    }
  }

  private static native Pointer _fiskaly_client_invoke(final String req);

  private static native void _fiskaly_client_free(final Pointer res);

  static {
    final String libName = getLibName();
    setLibSearchPath();
    Native.setProtected(true);
    Native.register(ClientLibrary.class, libName);
  }
}
