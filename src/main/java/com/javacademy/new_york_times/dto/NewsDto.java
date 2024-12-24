package com.javacademy.new_york_times.dto;

import lombok.Data;

@Data
public class NewsDto {
    private Integer number;
    private String title;
    private String text;
    private String author;
}
