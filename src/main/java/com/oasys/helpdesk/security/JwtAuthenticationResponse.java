package com.oasys.helpdesk.security;


public class JwtAuthenticationResponse {
	
	private String accessToken;
    private String tokenType = "Bearer";
    private Long expiresIn;
    
    //@JsonIgnore
    private String access_token;
    //@JsonIgnore
    private String token_type;
    //@JsonIgnore
    private Long expires_in;
    
    public JwtAuthenticationResponse(){
    	
    }
    
    /**
	 * @param access_token
	 * @param token_type
	 * @param expires_in
	 */
	public JwtAuthenticationResponse(String access_token, String token_type, Long expires_in) {
		super();
		this.accessToken = access_token;
		//this.tokenType = token_type;
		this.expiresIn = expires_in;
	}

    public JwtAuthenticationResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

	/**
	 * @return the expires_in
	 */
	public Long getExpires_in() {
		return expires_in;
	}

	/**
	 * @param expires_in the expires_in to set
	 */
	public void setExpires_in(Long expires_in) {
		this.expires_in = expires_in;
	}

	/**
	 * @return the access_token
	 */
	public String getAccess_token() {
		return access_token;
	}

	/**
	 * @param access_token the access_token to set
	 */
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	/**
	 * @return the token_type
	 */
	public String getToken_type() {
		return token_type;
	}

	/**
	 * @param token_type the token_type to set
	 */
	public void setToken_type(String token_type) {
		this.token_type = token_type;
	}

	/**
	 * @return the expiresIn
	 */
	public Long getExpiresIn() {
		return expiresIn;
	}

	/**
	 * @param expiresIn the expiresIn to set
	 */
	public void setExpiresIn(Long expiresIn) {
		this.expiresIn = expiresIn;
	}
}
