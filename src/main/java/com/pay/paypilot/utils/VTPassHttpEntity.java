package com.pay.paypilot.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

/**
 * @author Emmanuel Ugwueze
 * @created 26/06/2023 - 11:33
 * @project Pay-Pilot
 */

@Component
public class VTPassHttpEntity<T> {
    @Value("${vtpass.api.key}")
    private String API_KEY;
    @Value("${vtpass.secret.key}")
    private String SECRET_KEY;
    @Value("${vtpass.public.key}")
    private String PUBLIC_KEY;

    public HttpEntity<T> getEntity(T body) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("api-key", API_KEY);
        headers.add("secret-key", SECRET_KEY);
        headers.add("public-key", PUBLIC_KEY);
        headers.setContentType((MediaType.APPLICATION_JSON));
        return new HttpEntity<>(body, headers);
    }
}
