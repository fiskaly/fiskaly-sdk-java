package com.fiskaly.sdk.demo.android;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import com.fiskaly.sdk.client.ClientLibrary;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    TextView versionView = findViewById(R.id.text);
    try {
      final String res =
          ClientLibrary.invoke("{\"jsonrpc\":\"2.0\",\"id\":1,\"method\":\"version\"}");
      versionView.setText(res);
    } catch (Exception e) {
      e.printStackTrace();
      versionView.setText(e.getMessage());
    }
  }
}
