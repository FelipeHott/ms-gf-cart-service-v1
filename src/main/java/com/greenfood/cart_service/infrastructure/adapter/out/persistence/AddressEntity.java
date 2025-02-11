package com.greenfood.cart_service.infrastructure.adapter.out.persistence;

import lombok.Data;

@Data
public class AddressEntity {
    private String street;
    private String number;
    private String complement;
    private String neighborhood;
    private String city;
    private String state;
    private String zipCode;
}
