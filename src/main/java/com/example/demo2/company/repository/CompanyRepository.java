package com.example.demo2.company.repository;

import com.example.demo2.company.model.Company;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends CrudRepository<Company, String> {

    Optional<Company> findByLinkedinURL(String linkedinURL);
}