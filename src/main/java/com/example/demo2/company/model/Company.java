package com.example.demo2.company.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String websiteUrl;
    private String linkedinURL;
    @Column(columnDefinition = "TEXT")
    private String description;


    private Integer employeeCount;

    @Column
    @ElementCollection(targetClass = String.class)
    private List<String> specialities;

    @Embedded
    private Address address;

    private Integer openJobs;


    @OneToOne(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    @PrimaryKeyJoinColumn()
    @JsonManagedReference
    private Post post;

}

