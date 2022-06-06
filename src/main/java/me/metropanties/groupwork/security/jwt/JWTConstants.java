package me.metropanties.groupwork.security.jwt;

@SuppressWarnings("unused")
public final class JWTConstants {

    public static final Long ACCESS_TOKEN_EXPIRE = 60 * 60 * 1000L;
    public static final Long REFRESH_TOKEN_EXPIRE = 60 * 60 * 24 * 7 * 1000L;

}
