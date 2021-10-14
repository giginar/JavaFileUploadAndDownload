package com.kucukcinar.responses;

/**
 * The type Jwt response.
 */
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Integer id;
    private String email;
    private String role;

    /**
     * Instantiates a new Jwt response.
     *
     * @param accessToken the access token
     * @param id          the id
     * @param email       the email
     * @param role        the role
     */
    public JwtResponse(String accessToken, Integer id, String email, String role) {
        this.token = accessToken;
        this.id = id;
        this.email = email;
        this.role = role;
    }

    /**
     * Gets access token.
     *
     * @return the access token
     */
    public String getAccessToken() {
        return token;
    }

    /**
     * Sets access token.
     *
     * @param accessToken the access token
     */
    public void setAccessToken(String accessToken) {
        this.token = accessToken;
    }

    /**
     * Gets token type.
     *
     * @return the token type
     */
    public String getTokenType() {
        return type;
    }

    /**
     * Sets token type.
     *
     * @param tokenType the token type
     */
    public void setTokenType(String tokenType) {
        this.type = tokenType;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Gets email.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets email.
     *
     * @param email the email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets role.
     *
     * @return the role
     */
    public String getRole() {
        return role;
    }
}
