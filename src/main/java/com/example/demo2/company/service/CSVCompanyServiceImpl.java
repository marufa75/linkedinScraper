package com.example.demo2.company.service;

import com.example.demo2.company.repository.CompanyRepository;
import com.example.demo2.company.model.Company;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.PrintWriter;

import static com.example.demo2.company.model.Address.formatAddress;
import static org.slf4j.LoggerFactory.getLogger;

@Service
@AllArgsConstructor
@Slf4j
public class CSVCompanyServiceImpl implements CSVCompanyService {
    private CompanyRepository companyRepository;


    public void writeCompanyToCsv(PrintWriter writer) throws IOException {
        try (CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {

            Iterable<Company> all = companyRepository.findAll();
            csvPrinter.printRecord("Id", "Name", "Website", "Linkedin URL",  "Address", "Number of employees", "Number of job openings");

            all.forEach(company ->
            {
                try {
                    csvPrinter.printRecord(
                            company.getId(),
                            company.getName(),
                            company.getWebsiteUrl(),
                            company.getLinkedinURL(),
                            formatAddress(company.getAddress()),
                            company.getEmployeeCount(),
                            company.getOpenJobs()
                    );
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

        }
    }
}
