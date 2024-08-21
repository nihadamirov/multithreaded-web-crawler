package com.multithreadedwebcrawler.controller;

import com.multithreadedwebcrawler.service.WebhookService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/webhook")
@Slf4j
@RequiredArgsConstructor
public class WebhookController {

    private final WebhookService webhookService;

    @PostMapping("/crawlbase")
    public ResponseEntity<Void> crawlbaseCrawlerResponse(@RequestHeader HttpHeaders headers, @RequestBody byte[] compressedBody) {
        try {
            if(!headers.getFirst(HttpHeaders.USER_AGENT).equalsIgnoreCase("Crawlbase Monitoring Bot 1.0") &&
                    "gzip".equalsIgnoreCase(headers.getFirst(HttpHeaders.CONTENT_ENCODING)) &&
                    headers.getFirst("pc_status").equals("200")) {
                // Asynchronously Process The Request
                webhookService.handleWebhookResponse(headers, compressedBody);
            }
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            log.error("Error in crawlbaseCrawlerResponse function: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

}