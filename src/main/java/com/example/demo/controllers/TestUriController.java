package com.example.demo.controllers;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpClient.Version;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class TestUriController {

    private static final String SITE_IS_ONLINE = "The site is online!";
    private static final String SITE_IS_OFFLINE = "The site is offline!";
    private static final String URL_IS_MALFORMED = "Must specify a valid URL!";
    private static final int CATEGORY_SUCCESS = 2;
    private static final int CATEGORY_REDIRECTION = 3;

    @GetMapping("/testurl")
    public String getUrlStatus(@RequestParam String url) {
        try {
            return checkUrlStatus(url) ? SITE_IS_ONLINE : SITE_IS_OFFLINE;
        } catch (URISyntaxException ex) {
            return URL_IS_MALFORMED;
        } catch (IOException | InterruptedException ex) {
            return SITE_IS_OFFLINE;
        }
    }

    private boolean checkUrlStatus(String url) throws IOException, InterruptedException, URISyntaxException {
        HttpClient client = HttpClient.newBuilder().version(Version.HTTP_1_1).build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .GET()
                .build();

        HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
        int statusCode = response.statusCode() / 100;
        return statusCode == CATEGORY_SUCCESS || statusCode == CATEGORY_REDIRECTION;
    }
}