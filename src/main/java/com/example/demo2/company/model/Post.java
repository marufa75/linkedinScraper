package com.example.demo2.company.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Post {

    @Id
    @Column(name = "company_id")
    private Integer id;


    @OneToOne
    @MapsId
    @JoinColumn(name = "company_id")
    @JsonBackReference
    private Company company;

    private String originalLanguage;
    @Column(columnDefinition = "TEXT")
    private String text;
}
