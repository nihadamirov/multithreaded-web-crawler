package com.multithreadedwebcrawler.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.multithreadedwebcrawler.model.JobData;
import com.multithreadedwebcrawler.repository.JobDataRepository;
import lombok.RequiredArgsConstructor;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class WebCrawlerService {

    private final JobDataRepository jobDataRepository;
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

//    public List<JobData> crawlWebsite(String url, String jobTitle) {
//        List<Future<JobData>> futures = new ArrayList<>();
//
//        futures.add(executorService.submit(() -> fetchJobData(url, jobTitle)));
//
//        List<JobData> jobDataList = new ArrayList<>();
//        for (Future<JobData> future : futures) {
//            try {
//                JobData jobData = future.get();
//                if (jobData != null) {
//                    jobDataList.add(jobData);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        executorService.shutdown();
//
//        return jobDataRepository.saveAll(jobDataList);
//    }

    public List<JobData> crawlWebsite(String url, String jobTitle) {
        List<Future<JobData>> futures = new ArrayList<>();

        futures.add(executorService.submit(() -> fetchJobData(url, jobTitle)));

        List<JobData> jobDataList = new ArrayList<>();
        for (Future<JobData> future : futures) {
            try {
                JobData jobData = future.get();
                if (jobData != null) {
                    jobDataList.add(jobData);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException ex) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }

        return jobDataRepository.saveAll(jobDataList);
    }



    public JobData fetchJobData(String url, String jobTitle) throws IOException {
        if (jobTitle == null || jobTitle.trim().isEmpty()) {
            System.err.println("Job title cannot be null or empty");
            return null;
        }

        Connection.Response response = Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3")
                .execute();

        String contentType = response.contentType();
        if (contentType.contains("application/json")) {
            String jsonBody = response.body();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonBody);

            // Process the JSON response
            String jobTitleFromJson = jsonNode.path("title").asText();

            if (jobTitleFromJson.equals(jobTitle)) {
                return new JobData(url, jobTitle);
            }
        } else if (contentType.contains("text/html")) {
            // Handle HTML response
            Document doc = response.parse();
            Elements elements = doc.select(".job-listing .job-title:contains(" + jobTitle + ")");

            if (!elements.isEmpty()) {
                return new JobData(url, jobTitle);
            }
        } else {
            System.err.println("Unsupported content type: " + contentType);
        }

        return null;
    }


}
