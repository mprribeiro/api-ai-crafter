package com.mprribeiro.app_ai_crafter.service;

import reactor.core.publisher.Flux;

public interface AIGenerativeService {

    Flux<String> streamResponse(final String message, final Long aLong);
}
