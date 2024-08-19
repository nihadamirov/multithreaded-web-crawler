package com.multithreadedwebcrawler.repository;

import com.multithreadedwebcrawler.entity.CrawlerResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CrawlerResponseRepository extends JpaRepository<CrawlerResponse, Long> {
}
