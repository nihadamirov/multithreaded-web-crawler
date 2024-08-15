package com.multithreadedwebcrawler.service;

import com.multithreadedwebcrawler.model.JobData;
import com.multithreadedwebcrawler.repository.JobDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JobDataService {

    private final JobDataRepository jobDataRepository;

    public List<JobData> getAllJobData() {
        return jobDataRepository.findAll();
    }

}
