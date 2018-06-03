package com.erel.chillsounds.service;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class LocalResponder implements Interceptor{

    private Context context;

    LocalResponder(Context context){
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        HttpUrl url = chain.request().url();
        AssetManager assetManager = context.getAssets();

        try {
            InputStream stream = assetManager.open(url.encodedPath().replace("/", "") + ".json");
            return new Response.Builder()
                    .code(200)
                    .request(chain.request())
                    .message("")
                    .protocol(Protocol.HTTP_1_1)
                    .body(ResponseBody.create(MediaType.parse("application/json"), IOUtils.toByteArray(stream)))
                    .addHeader("content-type", "application/json")
                    .build();
        }catch (IOException ignored){
            return new Response.Builder()
                    .code(404)
                    .request(chain.request())
                    .message("")
                    .protocol(Protocol.HTTP_1_1)
                    .body(ResponseBody.create(MediaType.parse("application/json"), "Not Found".getBytes()))
                    .addHeader("content-type", "application/json")
                    .build();
        }
    }
}
