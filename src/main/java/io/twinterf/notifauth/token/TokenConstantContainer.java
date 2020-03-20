package io.twinterf.notifauth.token;

public class TokenConstantContainer {
    private final String jwtIssuer = System.getenv("JWT_ISSUER");
    private final String jwtSignature = System.getenv("JWT_SIGNATURE");

    public String getJwtIssuer() {
        return jwtIssuer;
    }

    public String getJwtSignature() {
        return jwtSignature;
    }
}
