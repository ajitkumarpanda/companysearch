package com.rn.companysearch.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Officer{
    private String name;
    private String officer_role;
    private String appointed_on;
    private Address address;
}
