package com.thoughtworks.winstonwolfe.endpoint;

import com.thoughtworks.winstonwolfe.config.WinstonConfig;
import com.thoughtworks.winstonwolfe.datasource.DataSource;
import com.thoughtworks.winstonwolfe.datasource.StringDataSource;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;

public class HttpServiceEndPoint implements ServiceEndPoint {
    private String url;

    public HttpServiceEndPoint(WinstonConfig endpointConfig) {
        url = endpointConfig.getString("http_url");
    }

    public String getUrl() {
        return url;
    }

    @Override
    public DataSource send(DataSource dataSource) throws IOException {
        HttpClient client = new DefaultHttpClient();

        HttpPost post = new HttpPost(url);
        post.setEntity(new StringEntity(dataSource.getData()));

        String responseData = client.execute(post, new BasicResponseHandler());

        return new StringDataSource(responseData);
    }
}