package com.ms.broker.data;

import com.ms.broker.model.WatchList;
import com.ms.wallet.DepositFiatMoney;
import com.ms.wallet.Wallet;
import com.ms.wallet.WithdrawFiatMoney;
import jakarta.inject.Singleton;

import java.math.BigDecimal;
import java.util.*;

@Singleton
public class InMemoryAccountStore {
    public static final UUID ACCOUNT_ID = UUID.fromString("f4245629-83df-4ed8-90d9-7401045b5921");
    private final Map<UUID, WatchList> watchListPerAccount = new HashMap<>();
    private final Map<UUID, Map<UUID, Wallet>> walletsPerAccount = new HashMap<>();

    public WatchList getWatchList(final UUID accountId) {
        return watchListPerAccount.getOrDefault(accountId, new WatchList());
    }

    public WatchList updateWatchList(final UUID accountId, final WatchList watchList) {
        watchListPerAccount.put(accountId, watchList);
        return getWatchList(accountId);
    }

    public void deleteWatchList(final UUID accountId) {
        watchListPerAccount.remove(accountId);
    }

    public Collection<Wallet> getWallets(UUID accountId) {
        return Optional.ofNullable(walletsPerAccount.get(accountId)).orElse(new HashMap<>()).values();
    }

    public Wallet depositToWallet(DepositFiatMoney deposit) {
        final var wallets = Optional.ofNullable(
                walletsPerAccount.get(deposit.accountId())).orElse(new HashMap<>());
        var oldWallet = Optional.ofNullable(wallets.get(deposit.walletId())).orElse(
                new Wallet(ACCOUNT_ID, deposit.walletId(), deposit.symbol(), BigDecimal.ZERO, BigDecimal.ZERO)
        );

        var newWallet = oldWallet.addAvailable(deposit.amount());
        wallets.put(newWallet.walletId(), newWallet);
        walletsPerAccount.put(newWallet.accountId(), wallets);
        return newWallet;
    }


    public Wallet withdrawFromWallet(WithdrawFiatMoney withdraw) {
        final var wallets = Optional.ofNullable(
                        walletsPerAccount.get(withdraw.accountId()))
                .orElseThrow(() -> new RuntimeException("No Account available with given id: " + withdraw.accountId()));

        var oldWallet = Optional.ofNullable(wallets.get(withdraw.walletId()))
                .orElseThrow(() -> new RuntimeException("No Wallet available with given id: " + withdraw.walletId()));
        var newWallet = oldWallet.minusAvailable(withdraw.amount());
        wallets.put(newWallet.walletId(), newWallet);
        walletsPerAccount.put(newWallet.accountId(), wallets);
        return newWallet;
    }
}
