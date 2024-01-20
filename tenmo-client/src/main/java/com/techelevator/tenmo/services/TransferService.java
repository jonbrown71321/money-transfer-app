package com.techelevator.tenmo.services;


import com.techelevator.tenmo.model.TransferRequest;
import com.techelevator.tenmo.model.TransferResponse;
import com.techelevator.tenmo.model.User;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class TransferService {

    private String baseUrl;
    private RestTemplate restTemplate = new RestTemplate();
    private String authToken = null;
    public void setAuthToken(String token) {
        this.authToken = token;
    }

    public TransferService(String baseUrl){
        this.baseUrl = baseUrl;
    }

    public void sendTEBucks(TransferRequest transferRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<TransferRequest> entity = new HttpEntity<>(transferRequest, headers);
        try{
            restTemplate.exchange(baseUrl + "/transfer", HttpMethod.POST, entity, TransferRequest.class);
        } catch (RestClientResponseException | ResourceAccessException e){
            BasicLogger.log(e.getMessage());
        }
    }

    public List<TransferResponse> getTransfers(int userId){

        List<TransferResponse> transfers = null;
        try{
            ResponseEntity<TransferResponse[]> response = restTemplate.exchange(baseUrl + "/" + userId + "/transferHistory", HttpMethod.GET, makeAuthEntity(), TransferResponse[].class);
            HttpStatus statusCode = response.getStatusCode();
            if(statusCode == HttpStatus.OK){
                TransferResponse[] responseArray = response.getBody();
                transfers = new ArrayList<>(Arrays.asList(responseArray));
            } else {
                System.out.println("Failure: HTTP error code : " + statusCode);
            }


        } catch(RestClientResponseException | ResourceAccessException e){
            BasicLogger.log(e.getMessage());
        }
        return transfers;
    }

    public TransferResponse getTransferById(int userId, int transferResponseId){
        TransferResponse transfer = null;
        try {
            ResponseEntity<TransferResponse> response = restTemplate.exchange(baseUrl + "/" +
                        userId + "/transferHistory/" + transferResponseId, HttpMethod.GET, makeAuthEntity(), TransferResponse.class);
            HttpStatus statusCpde = response.getStatusCode();
            if(statusCpde == HttpStatus.OK){
                transfer = response.getBody();
            } else {
                System.out.println("Failure: HTTP code error: " + statusCpde);
            }
        } catch(RestClientResponseException | ResourceAccessException e){
            BasicLogger.log(e.getMessage());
        }
        return transfer;
    }

    public HttpEntity<Void> makeAuthEntity(){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(headers);
    }

}
