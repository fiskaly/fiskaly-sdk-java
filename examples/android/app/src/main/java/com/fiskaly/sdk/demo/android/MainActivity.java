package com.fiskaly.sdk.demo.android;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import com.fiskaly.sdk.FiskalyHttpClient;
import com.fiskaly.sdk.FiskalyHttpResponse;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    final TextView versionView = findViewById(R.id.text);
    try {
      final String apiKey = "..."; // TODO: insert you apiKey here
      final String apiSecret = "..."; // TODO: insert your apiSecret here
      final FiskalyHttpClient client =
          new FiskalyHttpClient(apiKey, apiSecret, "https://kassensichv.io/api/v1");
      final FiskalyHttpResponse response = client.request("GET", "/tss");
      versionView.setText(response.toString());
    } catch (Exception e) {
      e.printStackTrace();
      versionView.setText(e.getMessage());
    }
  }
}
