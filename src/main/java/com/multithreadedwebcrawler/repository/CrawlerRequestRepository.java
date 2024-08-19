package com.multithreadedwebcrawler.repository;

import com.multithreadedwebcrawler.entity.CrawlerRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CrawlerRequestRepository extends JpaRepository<CrawlerRequest, Long> {
}
