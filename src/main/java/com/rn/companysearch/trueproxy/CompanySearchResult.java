package com.rn.companysearch.trueproxy;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompanySearchResult {
    private int total_results;
    private List<CompanyResponse> items;
}
