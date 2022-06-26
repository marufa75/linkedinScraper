package com.example.demo2.controller;

import com.example.demo2.company.service.CSVCompanyService;
import com.example.demo2.company.repository.CompanyRepository;
import com.example.demo2.company.model.Company;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/company")
@AllArgsConstructor
public class AccountController {
    private CSVCompanyService csvExportService;
    private CompanyRepository companyRepository;

    @GetMapping(value = "/all", produces = "application/json")
    public Iterable<Company> getAll() {
        return companyRepository.findAll();
    }

    @GetMapping(value = "/csv", produces = "text/csv")
    public void getAllEmployeesInCsv(HttpServletResponse servletResponse) throws IOException {
        // servletResponse.setContentType("text/csv");
        servletResponse.addHeader("Content-Disposition", "attachment; filename=\"companies.csv\"");
        csvExportService.writeCompanyToCsv(servletResponse.getWriter());
    }


}
