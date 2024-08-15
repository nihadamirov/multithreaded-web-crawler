package com.multithreadedwebcrawler.repository;

import com.multithreadedwebcrawler.model.JobData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobDataRepository extends JpaRepository<JobData, Long> {

}
