package com.rn.companysearch;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.rn.companysearch.dto.CompanyDetails;
import com.rn.companysearch.model.CompanySearchRequest;
import com.rn.companysearch.model.CompanySearchResponse;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CompanySearchControllerIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;

    static WireMockServer wireMockServer;

    @BeforeAll
    static void setupWireMock() {
        wireMockServer = new WireMockServer(8080);
        wireMockServer.start();
    }

    @AfterEach
    void cleanup() {
        wireMockServer.resetAll();
    }

    @AfterAll
    static void tearDown() {
        wireMockServer.stop();
    }
    @Test
    void searchCompanyByCompanyNumber() {
        wireMockServer.stubFor(get(urlPathMatching("/TruProxyAPI/rest/Companies/v1/Search"))
                .withQueryParam("Query", equalTo("06500244"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody("""
                                {
                                  "total_results": 1,
                                       "items": [
                                           {
                                               "company_number": "06500244",
                                               "company_type": "ltd",
                                               "title": "BBC LIMITED",
                                               "company_status": "active",
                                               "date_of_creation": "2008-02-11",
                                               "address": {
                                                   "locality": "Retford",
                                                   "postal_code": "DN22 0AD",
                                                   "premises": "Boswell Cottage Main Street",
                                                   "address_line_1": "North Leverton",
                                                   "country": "England"
                                               },
                                               "officers": [
                                                   {
                                                       "name": "BOXALL, Sarah Victoria",
                                                       "officer_role": "secretary",
                                                       "appointed_on": "2008-02-11",
                                                       "address": {
                                                           "premises": "5",
                                                           "locality": "London",
                                                           "address_line_1": "Cranford Close",
                                                           "country": "England",
                                                           "postal_code": "SW20 0DP"
                                                       }
                                                   }
                                               ]
                                           }
                                       ]
                                }""")));

        CompanySearchRequest request = new CompanySearchRequest("BBC LIMITED","06500244");
        request.setCompanyNumber("06500244");
        ResponseEntity<CompanySearchResponse> response = restTemplate.postForEntity("/api/companies/search?onlyActive=true", request, CompanySearchResponse.class);

        CompanySearchResponse responseBody = response.getBody();
        assertNotNull(responseBody, "Response body is null");
        assertNotNull(responseBody.getItems(), "Response items are null");
        assertFalse(responseBody.getItems().isEmpty(), "Response items are empty");

        CompanyDetails company = responseBody.getItems().get(0);
        assertEquals("06500244", company.getCompany_number(), "Unexpected company number");
        assertEquals("ltd", company.getCompany_type(), "Unexpected company type");
        assertEquals("BBC LIMITED", company.getTitle(), "Unexpected company title");
        assertEquals("active", company.getCompany_status(), "Unexpected company status");
        assertEquals("2008-02-11", company.getDate_of_creation(), "Unexpected date of creation");

    }

}