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
public class CrawlerResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Integer pcStatus;
    private Integer originalStatus;

    @Column(columnDefinition = "LONGTEXT")
    private String pageHtml;

    @OneToOne
    @JoinColumn(table = "request_id")
    private CrawlerRequest crawlerRequest;
}
