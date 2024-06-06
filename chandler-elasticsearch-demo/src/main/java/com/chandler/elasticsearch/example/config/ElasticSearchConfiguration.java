/*
 * chandler-elasticsearch-demo
 * 2024/3/11 15:12
 *
 * Please contact chandler
 * if you need additional information or have any questions.
 * Please contact chandler Corporation or visit:
 * https://www.jianshu.com/u/117796446366
 * @author 钱丁君-chandler
 * @version 1.0
 */
package com.chandler.elasticsearch.example.config;

import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import javax.net.ssl.*;
import java.security.*;
import java.security.cert.X509Certificate;

/**
 * 类功能描述
 *
 * @author 钱丁君-chandler 2024/3/11 15:12
 * @since 1.8
 */
@Slf4j
@Configuration
@ConfigurationProperties(prefix = "elasticsearch")
public class ElasticSearchConfiguration {
    @Setter
    String apiKey = "V2hGZkxJNEJKdUg4c0RPRHhKQmI6VDRQVzlIYUxUa0NEUGpHZlVlUnpVUQ==";

    @Setter
    String hosts= "127.0.0.1:9200";

    @Bean
    public ElasticsearchAsyncClient asyncEsClient(ElasticsearchTransport transport) {
        return new ElasticsearchAsyncClient(transport);
    }

    @Bean
    public ElasticsearchClient esClient(ElasticsearchTransport transport){
        return new ElasticsearchClient(transport);
    }

    @Bean
    public RestClient restClient(){
        HttpHost[] httpHosts = toHttpHost();
        RestClientBuilder.HttpClientConfigCallback callback = httpAsyncClientBuilder -> httpAsyncClientBuilder
                .setSSLContext(buildSSLContext())
                .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE);

        return RestClient
                .builder(httpHosts)
                .setDefaultHeaders(new Header[]{
                        new BasicHeader("Authorization", "ApiKey " + apiKey)
                })
                .setHttpClientConfigCallback(callback)
                .build();
    }

    @Bean
    public ElasticsearchTransport transport(RestClient restClient){
        return new RestClientTransport(
                restClient, new JacksonJsonpMapper());
    }

    private HttpHost[] toHttpHost() {
        if (!StringUtils.hasLength(hosts)) {
            throw new RuntimeException("invalid elasticsearch configuration. elasticsearch.hosts不能为空！");
        }

        // 多个IP逗号隔开
        String[] hostArray = hosts.split(",");
        HttpHost[] httpHosts = new HttpHost[hostArray.length];
        HttpHost httpHost;
        for (int i = 0; i < hostArray.length; i++) {
            String[] strings = hostArray[i].split(":");
            httpHost = new HttpHost(strings[0], Integer.parseInt(strings[1]), "https");
            httpHosts[i] = httpHost;
        }

        return httpHosts;
    }

    private SSLContext buildSSLContext() {
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("TLS");
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};
            sslContext.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
        } catch (NoSuchAlgorithmException |
                 KeyManagementException e) {
            log.error("ES连接认证失败", e);
        }
        return sslContext;
    }
}