package com.ms.wallet;

import com.ms.broker.model.Symbol;

import java.math.BigDecimal;
import java.util.UUID;

public record WithdrawFiatMoney(
        UUID accountId,
        UUID walletId,
        Symbol symbol,
        BigDecimal amount
) {
}
