package com.devar.cabs.responseDTO;

import lombok.Data;

@Data
public class AuthResponseDTO {

    private Long id;
    private String userName;
    private String email;
    private String firstname;
    private String phoneNumber;
    private String token;
    
}
