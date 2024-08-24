package com.multithreadedwebcrawler.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScrapeUrlRequest {

    private Integer pc_status;
    private Integer original_status;
    private String rid;
    private String urls;
    private String body;
}
