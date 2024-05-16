package com.rn.companysearch.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CompanySearchRequest {
    private String companyName;
    private String companyNumber;

}
