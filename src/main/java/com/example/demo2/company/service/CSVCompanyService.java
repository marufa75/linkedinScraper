package com.example.demo2.company.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.PrintWriter;

@Service
public interface CSVCompanyService {
    void writeCompanyToCsv(PrintWriter writer) throws IOException;
}
