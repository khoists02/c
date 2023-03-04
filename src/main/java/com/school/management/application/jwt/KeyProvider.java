package com.school.management.application.jwt;

import org.springframework.context.annotation.Bean;
import org.springframework.data.repository.NoRepositoryBean;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.List;
@NoRepositoryBean
public interface KeyProvider {
    KeyWithId getSigningKey(String tokenIssuer);

    List<KeyWithId> getPublicKeys();

    PublicKey getPublicKey(String kid);

    PrivateKey getPrivateKey(String kid);
}
