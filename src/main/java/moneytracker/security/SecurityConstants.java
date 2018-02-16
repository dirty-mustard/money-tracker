package moneytracker.security;

public class SecurityConstants {

    public static final long EXPIRATION_TIME = 86_4000_00; // 1 day
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String API_AUTH_SIGNUP = "/api/auth/sign-up";
    public static final String API_AUTH_REFRESH = "/api/auth/refresh";

}
