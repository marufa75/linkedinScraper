package com.example.demo2.company.model;

import lombok.Data;

import javax.persistence.Embeddable;

@Embeddable
@Data
public class Address {
    private String country;
    private String city;
    private String postalCode;
    private String address;

    public static String formatAddress(Address address) {
        if (address == null) {
            return "";
        }
        return String.format("%s %s %s %s", address.getAddress(), address.getCity(), address.getPostalCode(), address.getCountry());
    }

}
