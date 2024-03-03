package com.axonactive.dojo.base.config;

import java.util.ResourceBundle;

public class JwtConfig {
    private static final ResourceBundle rb = ResourceBundle.getBundle("jwt");

    private static final String SECRET_KEY = rb.getString("jwt.secret.key");

    private static final String ISSUER = rb.getString("jwt.issuer");

    private static final int ACCESS_TOKEN_TIME_TO_LIVE = Integer.parseInt(rb.getString("jwt.access.time-to-live"));

    private static final int REFRESH_TOKEN_TIME_TO_LIVE = Integer.parseInt(rb.getString("jwt.refresh.time-to-live"));

    private static final int VERIFY_TOKEN_TIME_TO_LIVE = Integer.parseInt(rb.getString("jwt.verify.time-to-live"));

    public static String getSecretKey() {
        return SECRET_KEY;
    }

    public static String getIssuer() {
        return ISSUER;
    }

    public static int getAccessTokenTimeToLive() {
        return ACCESS_TOKEN_TIME_TO_LIVE;
    }

    public static int getRefreshTokenTimeToLive() {
        return REFRESH_TOKEN_TIME_TO_LIVE;
    }

    public static int getVerifyTokenTimeToLive() {
        return VERIFY_TOKEN_TIME_TO_LIVE;
    }
}
