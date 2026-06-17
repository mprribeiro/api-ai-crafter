package com.mprribeiro.app_ai_crafter.config;

import com.stripe.Stripe;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StripePaymentConfig {

    @Value("${stripe.api.secret}")
    private String stripeSecret;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeSecret;
    }
}
