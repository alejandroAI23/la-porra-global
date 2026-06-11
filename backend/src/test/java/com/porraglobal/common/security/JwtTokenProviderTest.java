package com.porraglobal.common.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class JwtTokenProviderTest {

    private static final String SECRET = "test-secret-key-with-at-least-256-bits-for-hmac-sha256!!";

    private JwtTokenProvider provider;

    @BeforeEach
    void setUp() {
        provider = new JwtTokenProvider(SECRET, 60_000L);
    }

    @Test
    void generateToken_shouldProduceValidToken() {
        String token = provider.generateToken("juan", List.of("ROLE_USER"));

        assertThat(provider.validateToken(token)).isTrue();
        assertThat(provider.getUsername(token)).isEqualTo("juan");
        assertThat(provider.getRoles(token)).containsExactly("ROLE_USER");
    }

    @Test
    void validateToken_shouldReturnFalse_forMalformedToken() {
        assertThat(provider.validateToken("not-a-jwt")).isFalse();
    }

    @Test
    void validateToken_shouldReturnFalse_forExpiredToken() {
        var shortLived = new JwtTokenProvider(SECRET, -1000L);
        String token = shortLived.generateToken("juan", List.of("ROLE_USER"));

        assertThat(provider.validateToken(token)).isFalse();
    }

    @Test
    void validateToken_shouldReturnFalse_forTokenSignedWithDifferentKey() {
        var other = new JwtTokenProvider("another-secret-key-with-at-least-256-bits-for-hmac!!!!", 60_000L);
        String token = other.generateToken("juan", List.of("ROLE_USER"));

        assertThat(provider.validateToken(token)).isFalse();
    }
}
