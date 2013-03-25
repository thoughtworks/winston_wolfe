package com.thoughtworks.winstonwolfe.endpoint;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class HttpServiceEndPoint implements ServiceEndPoint {

    private String url;

    public HttpServiceEndPoint(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public void send(String data) throws IOException {
        HttpClient client = new DefaultHttpClient();

        HttpPost post = new HttpPost(url);
        post.setEntity(new StringEntity(data));

        client.execute(post);
//            ResponseHandler<String> responseHandler = new BasicResponseHandler();
//            responseHandler.

    }
}