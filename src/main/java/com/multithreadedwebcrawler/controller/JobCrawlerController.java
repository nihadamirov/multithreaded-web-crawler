package com.multithreadedwebcrawler.controller;

import com.multithreadedwebcrawler.dto.JobDataDto;
import com.multithreadedwebcrawler.mapper.JobDataMapper;
import com.multithreadedwebcrawler.service.JobDataService;
import com.multithreadedwebcrawler.service.WebCrawlerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class JobCrawlerController {

    private final WebCrawlerService webCrawlerService;
    private final JobDataService jobDataService;
    private final JobDataMapper jobDataMapper;

    @PostMapping("/crawl")
    public List<JobDataDto> crawlWebsite(@RequestBody JobDataDto jobDataDto) {
        return webCrawlerService.crawlWebsite(jobDataDto.getUrl(), jobDataDto.getJobTitle()).stream()
                .map(jobDataMapper::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/jobs")
    public List<JobDataDto> getAllJobs() {
        return jobDataService.getAllJobData().stream()
                .map(jobDataMapper::toDTO)
                .collect(Collectors.toList());
    }
}
