package com.greenfood.cart_service.domain.model;

import lombok.Data;

@Data
public class Adress {
    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String complement;
    private String number;
    private String neighborhood;
}
