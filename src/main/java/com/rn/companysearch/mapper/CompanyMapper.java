package com.rn.companysearch.mapper;


import com.rn.companysearch.dto.CompanyDetails;
import com.rn.companysearch.dto.Officer;
import com.rn.companysearch.trueproxy.CompanyResponse;

import java.util.List;

public class CompanyMapper {
    public static CompanyDetails mapCompanyResponseToCompany(CompanyResponse apiCompany, List<Officer> officers) {
        CompanyDetails response = new CompanyDetails();

        response.setCompany_number(apiCompany.getCompany_number());
        response.setCompany_type(apiCompany.getCompany_type());
        response.setTitle(apiCompany.getTitle());
        response.setCompany_status(apiCompany.getCompany_status());
        response.setDate_of_creation(apiCompany.getDate_of_creation());
        response.setAddress(apiCompany.getAddress());
        response.setOfficers(officers);

        return response;
    }


}
