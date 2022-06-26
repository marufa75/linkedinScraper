package com.example.demo2.controller;

import lombok.Data;
import org.checkerframework.common.value.qual.MinLen;

import java.util.List;
@Data
public class URLSWrapper {
    @MinLen(1)
    private List<String> urls;
}
