package net.vrallev.android.jarjar.demo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

import ext.com.squareup.okhttp.OkHttpClient;
import ext.com.squareup.okhttp.Request;

/**
 * @author rwondratschek
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Thread() {
            @Override
            public void run() {
                Request request = new Request.Builder()
                        .get()
                        .url(" https://api.github.com/users/vRallev")
                        .build();

                try {
                    final String message = new OkHttpClient()
                            .newCall(request)
                            .execute()
                            .message();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "Message: " + message, Toast.LENGTH_SHORT).show();
                        }
                    });

                } catch (IOException e) {
                    Log.e("Demo", e.getMessage(), e);
                }
            }
        }.start();
    }

}
