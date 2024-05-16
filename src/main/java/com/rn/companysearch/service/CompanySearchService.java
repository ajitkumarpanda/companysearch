package com.rn.companysearch.service;

import com.rn.companysearch.dto.CompanyDetails;
import com.rn.companysearch.dto.Officer;
import com.rn.companysearch.mapper.CompanyMapper;
import com.rn.companysearch.mapper.OfficerMapper;
import com.rn.companysearch.model.CompanySearchRequest;
import com.rn.companysearch.model.CompanySearchResponse;
import com.rn.companysearch.trueproxy.CompanyResponse;
import com.rn.companysearch.trueproxy.CompanySearchResult;
import com.rn.companysearch.trueproxy.OfficerSearchResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CompanySearchService {
    @Value("${trueproxy.api.key}")
    private String apiKey;
    @Value("${trueproxy.api.baseUrl}")
    private String baseUrl;
    private final RestTemplate restTemplate;

    public CompanySearchService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public CompanySearchResponse searchCompany(CompanySearchRequest request, boolean onlyActiveCompanies){
        try{
            log.info("Initiating company search with request: {}", request);
            ResponseEntity<CompanySearchResult> apiResponse = getAPIResponseFromTrueProxyService( request);
            List<CompanyResponse> companyResponseList = extractCompaniesWithFilters(onlyActiveCompanies, apiResponse);
            log.info("Received {} companies from the API response.", companyResponseList.size());

            CompanySearchResponse response = new CompanySearchResponse();
            response.setTotalResults(companyResponseList.size());

            for(CompanyResponse companyResponse : companyResponseList) {
                List<Officer> officers = getOfficersFromTrueProxyAPI(companyResponse.getCompany_number());
                CompanyDetails companyDetails = CompanyMapper.mapCompanyResponseToCompany(companyResponse, officers);
                response.addItems(companyDetails);
            }
            log.info("Company search completed successfully with {} results.", response.getTotalResults());

            return response;

        } catch (HttpClientErrorException ex) {
            throw new RuntimeException("Error while communicating with the TruProxy service: " + ex.getMessage(), ex);
        } catch (Exception ex) {
            throw new RuntimeException("An unexpected error occurred: " + ex.getMessage(), ex);
        }
    }

    private ResponseEntity<CompanySearchResult> getAPIResponseFromTrueProxyService(CompanySearchRequest request){
        String searchParam = request.getCompanyNumber() != null ? request.getCompanyNumber() : request.getCompanyName();
        String queryUrl = String.format("%s/Companies/v1/Search?Query=%s",baseUrl,searchParam);

        HttpHeaders headers = new HttpHeaders();
        headers.set("x-api-key",apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        return restTemplate.exchange(
                queryUrl,
                HttpMethod.GET,
                entity,
                CompanySearchResult.class
        );
    }

    private static List<CompanyResponse> extractCompaniesWithFilters(boolean onlyActiveCompanies, ResponseEntity<CompanySearchResult> response) {
        CompanySearchResult result = response.getBody();
        List<CompanyResponse> companies = result != null ? result.getItems() : null;

        if (companies == null || companies.isEmpty()) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "No company found.");
        }

        return companies.stream()
                .filter(c -> !onlyActiveCompanies || "active".equalsIgnoreCase(c.getCompany_status()))
                .collect(Collectors.toList());
    }

    private List<Officer> getOfficersFromTrueProxyAPI(String companyNumber) {
        String officerUrl = String.format("%s/Companies/v1/Officers?CompanyNumber=%s", baseUrl, companyNumber);
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-api-key",apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<OfficerSearchResult> officersResults = restTemplate.exchange(
                officerUrl,
                HttpMethod.GET,
                entity,
                OfficerSearchResult.class
        );
        return Objects.requireNonNull(officersResults.getBody())
                .getItems()
                .stream()
                .filter(o -> o.getResigned_on() == null)
                .map(OfficerMapper::mapOfficerResponseToOfficer)
                .collect(Collectors.toList());
    }

}
