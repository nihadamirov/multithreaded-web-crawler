package com.multithreadedwebcrawler.service;

import com.multithreadedwebcrawler.model.JobData;
import com.multithreadedwebcrawler.repository.JobDataRepository;
import lombok.RequiredArgsConstructor;
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

@Service
@RequiredArgsConstructor
public class WebCrawlerService {

    private final JobDataRepository jobDataRepository;
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

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

        return jobDataRepository.saveAll(jobDataList);
    }

    private JobData fetchJobData(String url, String jobTitle) throws IOException {
        Document doc = Jsoup.connect(url).get();
        Elements elements = doc.getElementsContainingOwnText(jobTitle);

        if (!elements.isEmpty()) {
            return new JobData(url, jobTitle);
        }
        return null;
    }
}
