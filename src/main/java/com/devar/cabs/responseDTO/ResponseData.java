package com.devar.cabs.responseDTO;

import java.time.Instant;

import lombok.Data;

@Data
public class ResponseData {
    private AuthResponseDTO auth;
    private Instant lastLoginTime;
}
