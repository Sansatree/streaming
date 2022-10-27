package com.sstree.streaming.streamingserver.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class TokenRequestDto {
    private String accessToken;
    private String refreshToken;
}
