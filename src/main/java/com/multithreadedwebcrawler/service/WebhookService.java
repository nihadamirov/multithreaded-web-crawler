package com.multithreadedwebcrawler.service;

import com.multithreadedwebcrawler.entity.CrawlerRequest;
import com.multithreadedwebcrawler.entity.CrawlerResponse;
import com.multithreadedwebcrawler.repository.CrawlerRequestRepository;
import com.multithreadedwebcrawler.repository.CrawlerResponseRepository;
import com.multithreadedwebcrawler.request.CrawlerWebhookRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

@Slf4j
@RequiredArgsConstructor
@Service
public class WebhookService {

    private final CrawlerRequestRepository crawlerRequestRepository;
    private final CrawlerResponseRepository crawlerResponseRepository;
    private final MainService mainService;


    @Async("taskExecutor")
    public void handleWebhookResponse(HttpHeaders headers, byte[] compressedBody) {
        try {
            // Unzip the gziped body
            GZIPInputStream gzipInputStream = new GZIPInputStream(new ByteArrayInputStream(compressedBody));
            InputStreamReader reader = new InputStreamReader(gzipInputStream);

            // Process the uncompressed HTML content
            StringBuilder htmlContent = new StringBuilder();
            char[] buffer = new char[1024];
            int bytesRead;
            while ((bytesRead = reader.read(buffer)) != -1) {
                htmlContent.append(buffer, 0, bytesRead);
            }

            // The HTML String
            String htmlString = htmlContent.toString();

            // Create the request object
            CrawlerWebhookRequest request = CrawlerWebhookRequest.builder()
                    .original_status(Integer.valueOf(headers.getFirst("original_status")))
                    .pc_status(Integer.valueOf(headers.getFirst("pc_status")))
                    .rid(headers.getFirst("rid"))
                    .url(headers.getFirst("url"))
                    .body(htmlString).build();

            // Save CrawlerResponse Model
            List<CrawlerRequest> results = crawlerRequestRepository.findByRid(request.getRid());
            CrawlerRequest crawlerRequest = !results.isEmpty() ? results.get(0) : null;
            if(crawlerRequest != null) {
                // Build CrawlerResponse Model
                CrawlerResponse crawlerResponse = CrawlerResponse.builder().pcStatus(request.getPc_status())
                        .originalStatus(request.getOriginal_status()).pageHtml(request.getBody()).crawlerRequest(crawlerRequest).build();
                crawlerResponseRepository.save(crawlerResponse);
            }

            // Only Deep Crawl Parent Url
            if(headers.getFirst("type").equalsIgnoreCase("parent")) {
                deepCrawlParentResponse(request.getBody(), request.getUrl());
            }
        } catch (Exception e) {
            log.error("Error in handleWebhookResponse function: " + e.getMessage());
        }

    }


    private void deepCrawlParentResponse(String html, String baseUrl) {
        Document document = Jsoup.parse(html);
        Elements hyperLinks = document.getElementsByTag("a");
        List<String> links = new ArrayList<String>();

//        String url = null;
//        for (Element hyperLink : hyperLinks) {
//            url = processUrl(hyperLink.attr("href"), baseUrl);
//            if(url != null) {
//                links.add(url);
//            }
//        }

        mainService.pushUrlsToCrawler(links, "child");
    }

}
