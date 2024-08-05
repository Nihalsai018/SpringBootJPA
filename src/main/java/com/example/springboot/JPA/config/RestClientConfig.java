//package com.example.springboot.JPA.config;
//
//import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
//import org.springframework.web.client.RestTemplate;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.ssl.SSLContextBuilder;
//
//import javax.net.ssl.SSLContext;
//import java.io.InputStream;
//import java.security.KeyStore;
//
//public class RestClientConfig {
//
//    public RestTemplate restTemplate() throws Exception {
//        // Load the keystore
//        KeyStore keyStore = KeyStore.getInstance("PKCS12");
//        try (InputStream keyStoreStream = getClass().getClassLoader().getResourceAsStream("keystore.p12")) {
//            keyStore.load(keyStoreStream, "your-keystore-password".toCharArray());
//        }
//
//        // Create SSL context with the keystore
//        SSLContext sslContext = SSLContextBuilder.create()
//                .loadKeyMaterial(keyStore, "your-keystore-password".toCharArray())
//                .build();
//
//        // Create HTTP client with SSL context
//        CloseableHttpClient httpClient = HttpClients.custom()
//                .setSSLContext(sslContext)
//                .build();
//
//        // Create HTTP request factory
//        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
//
//        return new RestTemplate(factory);
//    }
//}
