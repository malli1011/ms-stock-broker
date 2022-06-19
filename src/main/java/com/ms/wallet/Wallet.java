package com.ms.wallet;

import com.ms.broker.api.RestApiResponse;
import com.ms.broker.model.Symbol;

import java.math.BigDecimal;
import java.util.UUID;

public record Wallet(
        UUID accountId,
        UUID walletId,
        Symbol symbol,
        BigDecimal available,
        BigDecimal locked
) implements RestApiResponse {

    public Wallet addAvailable(BigDecimal amountToAdd) {
        return new Wallet(
                this.accountId,
                this.walletId,
                this.symbol,
                this.available.add(amountToAdd),
                this.locked
        );
    }

    public Wallet minusAvailable(BigDecimal amountToWithdraw) {
        if (amountToWithdraw.intValue() < this.available.intValue()) {
            return new Wallet(
                    this.accountId,
                    this.walletId,
                    this.symbol,
                    this.available.subtract(amountToWithdraw),
                    this.locked
            );
        }
        throw new RuntimeException(String.format("No Sufficient funds to withdraw. Available balance %d", this.available.intValue()));
    }
}
