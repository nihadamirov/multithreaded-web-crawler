package com.multithreadedwebcrawler.service;

import com.crawlbase.API;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.multithreadedwebcrawler.entity.CrawlerRequest;
import com.multithreadedwebcrawler.repository.CrawlerRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MainService {

    private final CrawlerRequestRepository crawlerRequestRepository;

    @Value("${crawlbase.token}")
    private String crawlbaseToken;

    @Value("${crawlbase.crawler}")
    private String crawlbaseCrawlerName;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void pushUrlsToCrawler(List<String> urls, String type) {
        HashMap<String, Object> options = new HashMap<>();
        options.put("callback", "true");
        options.put("crawler", crawlbaseCrawlerName);
        options.put("callback_headers", "type:" + type);

        API api = null;
        CrawlerRequest req = null;
        JsonNode jsonNode = null;
        String rid = null;

        for (String url : urls) {
            try {
                api = new API(crawlbaseToken);
                api.get(url, options);
                jsonNode = objectMapper.readTree(api.getBody());
                rid = jsonNode.get("rid").asText();
                if(rid != null) {
                    req = CrawlerRequest.builder().url(url).type(type).
                            status(api.getStatusCode()).rid(rid).build();
                    crawlerRequestRepository.save(req);
                }
            } catch(Exception e) {
                log.error("Error in pushUrlsToCrawler function: " + e.getMessage());
            }
        }

    }

}
