package com.rn.companysearch.controller;


import com.rn.companysearch.model.CompanySearchRequest;
import com.rn.companysearch.model.CompanySearchResponse;
import com.rn.companysearch.service.CompanySearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/companies")
public class CompanySearchController {
    private final CompanySearchService companySearchService;

    public CompanySearchController(CompanySearchService companySearchService) {
        this.companySearchService = companySearchService;
    }

    @PostMapping("/search")
    public ResponseEntity<CompanySearchResponse> searchCompany(@RequestBody CompanySearchRequest request,
                                                               @RequestParam(defaultValue = "true") boolean onlyActive) {
        if (request == null) {
            return ResponseEntity.badRequest().build();
        }

        try {
            CompanySearchResponse response = companySearchService.searchCompany(request, onlyActive);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error occurred while searching for companies", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

}