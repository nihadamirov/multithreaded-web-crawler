package com.multithreadedwebcrawler.repository;

import com.multithreadedwebcrawler.entity.CrawlerRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CrawlerRequestRepository extends JpaRepository<CrawlerRequest, Long> {

    // Find by column Name and value
    List<CrawlerRequest> findByRid(String value);
}
