package com.rn.companysearch.trueproxy;

import com.rn.companysearch.dto.Address;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompanyResponse {

    private String company_status;
    private String date_of_creation;
    private String company_number;
    private String title;
    private String company_type;
    private Address address;
    private Map<String, List<Integer>> matches;
    private String description;
}