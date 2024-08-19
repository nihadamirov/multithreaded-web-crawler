package com.multithreadedwebcrawler.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobDataDto {
    private String url;
    private String jobTitle;
}
