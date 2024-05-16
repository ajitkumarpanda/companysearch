package com.rn.companysearch.mapper;

import com.rn.companysearch.dto.Officer;
import com.rn.companysearch.trueproxy.OfficerResponse;

public class OfficerMapper {
    public static Officer mapOfficerResponseToOfficer(OfficerResponse apiOfficer) {
        Officer officer = new Officer();
        officer.setName(apiOfficer.getName());
        officer.setOfficer_role(apiOfficer.getOfficer_role());
        officer.setAddress(apiOfficer.getAddress());
        officer.setAppointed_on(apiOfficer.getAppointed_on());
        return officer;
    }
}
