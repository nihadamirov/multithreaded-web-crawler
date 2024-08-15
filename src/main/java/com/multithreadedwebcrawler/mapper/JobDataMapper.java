package com.multithreadedwebcrawler.mapper;

import com.multithreadedwebcrawler.dto.JobDataDto;
import com.multithreadedwebcrawler.model.JobData;
import org.springframework.stereotype.Component;

@Component
public class JobDataMapper {

    public JobDataDto toDTO(JobData jobData) {
        return new JobDataDto(jobData.getId(), jobData.getUrl(), jobData.getJobTitle());
    }

    public JobData toEntity(JobDataDto jobDataDto) {
        return new JobData(jobDataDto.url(), jobDataDto.JobTitle());
    }
}
