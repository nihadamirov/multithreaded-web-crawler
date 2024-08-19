package com.multithreadedwebcrawler.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class CrawlerRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String url;
    private String type;
    private Integer status;
    private String rid;

    @OneToOne(mappedBy = "CrawlerRequest", cascade = CascadeType.ALL , fetch = FetchType.LAZY)
    private CrawlerResponse crawlerResponse;
}
