package com.sstree.streaming.streamingserver.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AccessTokenDto {
    private String grantType;
    private String accessToken;
    private Long accessTokenExpirationTime;
}
