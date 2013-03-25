package com.thoughtworks.winstonwolfe.endpoint;

import com.thoughtworks.winstonwolfe.datasource.StringDataSource;
import com.thoughtworks.winstonwolfe.request.Request;
import com.thoughtworks.winstonwolfe.response.Response;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;

public class HttpServiceEndPoint implements ServiceEndPoint {

    private String url;

    public HttpServiceEndPoint(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public Response send(String data) throws IOException {
        HttpClient client = new DefaultHttpClient();

        HttpPost post = new HttpPost(url);
        post.setEntity(new StringEntity(data));

        ResponseHandler<String> responseHandler = new BasicResponseHandler();

        String responseData = client.execute(post, responseHandler);

        return new Response(new StringDataSource(responseData));
    }
}