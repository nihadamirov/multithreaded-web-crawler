package com.multithreadedwebcrawler.controller;

import com.multithreadedwebcrawler.request.ScrapeUrlRequest;
import com.multithreadedwebcrawler.service.MainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("/scrape")
@RequiredArgsConstructor
@Slf4j
public class MainController {

    private final MainService mainService;

    @PostMapping("/push-urls")
    public ResponseEntity<Object> pushUrlsToCrawler(@RequestBody ScrapeUrlRequest request) {
        try {
            if (!request.getUrls().isEmpty()) {
                mainService.pushUrlsToCrawler(Collections.singletonList(request.getUrls()), "parent");
            }
            return ResponseEntity.status(HttpStatus.OK).build();

        } catch (Exception e) {
            log.error("Error in pushUrlsToCrawler function: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        }
    }
}
