package com.sstree.streaming.streamingserver.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Entity
@NoArgsConstructor
public class RefreshToken {
    @Id
    private String rtKey;

    private String reValue;

    @Builder
    public RefreshToken(String rtKey, String reValue) {
        this.rtKey = rtKey;
        this.reValue = reValue;
    }

    public RefreshToken updateValue(String token){
        this.reValue = token;
        return this;
    }
}
