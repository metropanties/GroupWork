package me.metropanties.groupwork.object;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TokenRefreshRequest(@JsonProperty("refresh_token") String refreshToken) {

}
