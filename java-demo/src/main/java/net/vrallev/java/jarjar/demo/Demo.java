package net.vrallev.java.jarjar.demo;

import java.io.IOException;

import ext.com.squareup.okhttp.OkHttpClient;
import ext.com.squareup.okhttp.Request;

/**
 * @author rwondratschek
 */
public class Demo {

    public static void main(String... args) throws IOException {
        Request request = new Request.Builder()
                .get()
                .url(" https://api.github.com/users/vRallev")
                .build();

        final String message = new OkHttpClient()
                .newCall(request)
                .execute()
                .message();

        System.out.println("Message: " + message);
    }
}
