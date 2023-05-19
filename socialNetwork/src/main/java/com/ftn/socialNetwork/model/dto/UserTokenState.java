package com.ftn.socialNetwork.model.dto;


import javax.servlet.http.HttpServletResponse;

public class UserTokenState {

    private String accessToken;
    private Long expiresIn;
    private HttpServletResponse response;

    public UserTokenState() {
        this.accessToken = null;
        this.expiresIn = null;
        this.response = null;

    }

    public UserTokenState(String accessToken, long expiresIn) {
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;

    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }






}
