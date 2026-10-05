package com.banfico.banking.dto;

import jakarta.validation.constraints.NotBlank;

public class KeycloakUserLinkRequest {

    @NotBlank(message="Keycloak user Id is required")

    private String keycloakUserId;

    public  String getKeycloakUserId(){
        return keycloakUserId;
    }
    public void setKeycloakUserId(String keycloakUserId){
        this.keycloakUserId=keycloakUserId;
    }
    
}
