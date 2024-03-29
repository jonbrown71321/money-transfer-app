package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.util.BasicLogger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

public class AccountServices {

    public static final String API_BASE_URL = "http://localhost:8080/";

    private RestTemplate restTemplate = new RestTemplate();

    private  String authToken = null;

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }


   public double getAccountBalance(){
       Account account = null;
       try {
           // Add code here to send the request to the API and get the account from the response.
           ResponseEntity<Account> response =
                   restTemplate.exchange(API_BASE_URL + "balance", HttpMethod.GET, makeAuthEntity(), Account.class);
           account = response.getBody();
       } catch (RestClientResponseException | ResourceAccessException e) {
           BasicLogger.log(e.getMessage());
       }
       return account.getBalance();
   }



    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }
}
