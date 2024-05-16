package com.rn.companysearch.trueproxy;


import com.rn.companysearch.dto.Address;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OfficerResponse {
    private String name;
    private String officer_role;
    private String appointed_on;
    private String resigned_on;
    private Address address;
}