package com.ms.wallet;

import com.ms.broker.api.RestApiResponse;

public record CustomError(
        int status,
        String error,
        String message
)implements RestApiResponse {
}
