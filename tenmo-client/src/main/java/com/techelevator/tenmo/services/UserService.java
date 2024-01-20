package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.User;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserService {

    private final String baseUrl;
    private RestTemplate restTemplate = new RestTemplate();
    private String authToken = null;

    public void setAuthToken(String authToken){
        this.authToken = authToken;
    }
    public UserService(String baseUrl){
        this.baseUrl = baseUrl;
    }

    public List<User> getUsers(){
        List<User> users = null;

       ResponseEntity<User[]> response = restTemplate.exchange(baseUrl + "/users",
                HttpMethod.GET, makeAuthEntity(), User[].class);

        HttpStatus statusCode = response.getStatusCode();
        if(statusCode == HttpStatus.OK){
            User[] responseArray = response.getBody();
            users = new ArrayList<>(Arrays.asList(responseArray));
        } else {
            System.out.println("Failure: HTTP error code : " + statusCode);
        }

        return users;
    }

    public User getUserById(int userId){
        User user = null;
        ResponseEntity<User> response = restTemplate.exchange(baseUrl + "/users/" + userId,
                HttpMethod.GET, makeAuthEntity(), User.class);

        HttpStatus statusCode = response.getStatusCode();
        if(statusCode == HttpStatus.OK){
             user = response.getBody();
        }  else {
            System.out.println("Failure: HTTP error code: " + statusCode);
        }
        return user;
    }

    private HttpEntity<Void> makeAuthEntity(){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(headers);
    }


}

