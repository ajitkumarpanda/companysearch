package com.rn.companysearch.model;


import com.rn.companysearch.dto.CompanyDetails;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompanySearchResponse {
    private int totalResults;
    private List<CompanyDetails> items;

    public void addItems(CompanyDetails companyDetails){
        if(items == null) {
            items = new ArrayList<>();
        }
        items.add(companyDetails);
    }

}
