package com.ms.auth.jwt;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.*;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthenticationProviderUserPassword implements AuthenticationProvider {

    private static final Logger LOG = LoggerFactory.getLogger(AuthenticationProviderUserPassword.class);

    @Override
    public Publisher<AuthenticationResponse> authenticate(@Nullable final HttpRequest<?> httpRequest, AuthenticationRequest<?, ?> authenticationRequest) {
        return Flowable.create(emitter -> {
            final Object identity = authenticationRequest.getIdentity();
            final Object secret = authenticationRequest.getSecret();
            LOG.debug("User {} tries to login...", identity);

            if (identity.equals("my-user") && secret.equals("secret")) {
                emitter.onNext(AuthenticationResponse.success("my-user"));
                emitter.onComplete();
            } else {
                emitter.onError(new AuthenticationException((new AuthenticationFailed())));
            }

        }, BackpressureStrategy.ERROR);
    }
}
