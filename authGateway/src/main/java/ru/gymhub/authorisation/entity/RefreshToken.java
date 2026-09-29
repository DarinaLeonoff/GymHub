package ru.gymhub.authorisation.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(value = "refresh_tokens")
public class RefreshToken {

    @Id
    private String id;

    @Indexed
    private Long userId;

    @Indexed
    private String token;

    @TimeToLive
    private Long ttlInSeconds;
}