package com.greenfood.cart_service.domain.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdressClient {
    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String complement;
    private String number;
    private String neighborhood;
}
