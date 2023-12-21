package com.movii.web.rest;

import org.apache.commons.compress.utils.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:8083")
public class IMDbScrapeController {

    @GetMapping("/scrape-imdb")
    public ResponseEntity<String> scrapeIMDb(@RequestParam String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
        HttpEntity<String> entity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
//        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);

        return new ResponseEntity<>(response.getBody(), headers, HttpStatus.OK);
    }

//    public String scrapeIMDb(@RequestParam(value = "url") String url) {
//        String imdbHtml = fetchIMDbHtml(url);
////        IMDbData imdbData = scrapeDataFromHtml(imdbHtml);
//        return imdbHtml;
//    }

//    private String fetchIMDbHtml(String url) {
//        RestTemplate restTemplate = new RestTemplate();
//        return restTemplate.getForObject(url, String.class);
//    }

    @GetMapping("/image-to-base64")
    public String imageToBase64(@RequestParam String imageUrl, @RequestHeader HttpHeaders headers) throws IOException {
        try (InputStream inputStream = new URL(imageUrl).openStream()) {
            HttpURLConnection connection = (HttpURLConnection) new URL(imageUrl).openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
            byte[] bytes = IOUtils.toByteArray(inputStream);
            return Base64.getEncoder().encodeToString(bytes);
        }
    }


//    @GetMapping("/scrape/image")
//    public ResponseEntity<String> scrapeImage(@RequestParam String url) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
//        HttpEntity<String> entity = new HttpEntity<>(headers);
//        RestTemplate restTemplate = new RestTemplate();
//        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
//        return new ResponseEntity<>(response.getBody(), headers, HttpStatus.OK);
//    }

//    private IMDbData scrapeDataFromHtml(String html) {
//        String title = extractTitleFromHtml(html);
//        String description = extractDescriptionFromHtml(html);
//
//        return new IMDbData(title, description);
//    }
//
//    private String extractTitleFromHtml(String html) {
//        try {
//            Document document = Jsoup.parse(html);
//
//            Elements titleElements = document.select("#__next > main > div > section.ipc-page-background.ipc-page-background--base.sc-304f99f6-0.fSJiHR > section > div:nth-child(4) > section > section > div.sc-e226b0e3-3.dwkouE > div.sc-69e49b85-0.jqlHBQ > h1 > span");
//
//            return titleElements.text().trim();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "Title extraction failed";
//        }
//    }
//
//    private String extractDescriptionFromHtml(String html) {
//        try {
//            Document document = Jsoup.parse(html);
//
//            Elements descriptionElements = document.select("#__next > main > div > section.ipc-page-background.ipc-page-background--base.sc-304f99f6-0.fSJiHR > section > div:nth-child(4) > section > section > div.sc-e226b0e3-4.dEqUUl > div.sc-e226b0e3-6.CUzkx > div.sc-e226b0e3-10.hbBxmX > section > p > span.sc-466bb6c-0.hlbAws");
//
//            return descriptionElements.text().trim();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "Description extraction failed";
//        }
//    }
//    static class IMDbData {
//        private final String title;
//        private final String description;
//
//        public IMDbData(String title, String description) {
//            this.title = title;
//            this.description = description;
//        }
//
//        public String getTitle() {
//            return title;
//        }
//
//        public String getDescription() {
//            return description;
//        }
//    }
}
