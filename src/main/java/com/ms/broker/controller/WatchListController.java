package com.ms.broker.controller;

import com.ms.broker.data.InMemoryAccountStore;
import com.ms.broker.model.WatchList;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;

import java.util.UUID;

@Controller("/account/watchlist")
public record WatchListController(InMemoryAccountStore store) {
    static final UUID ACCOUNT_ID = UUID.randomUUID();

    @Get(produces = MediaType.APPLICATION_JSON)
    public WatchList get() {
        return store.getWatchList(ACCOUNT_ID);
    }

    @Put(
            consumes = MediaType.APPLICATION_JSON,
            produces = MediaType.APPLICATION_JSON
    )
    public WatchList update(@Body WatchList watchList) {
        return store.updateWatchList(ACCOUNT_ID, watchList);
    }

    @Delete("{account_id}")
    //@Status(HttpStatus.NO_CONTENT)
    public HttpResponse<Void> delete(@PathVariable UUID account_id) {
        store.deleteWatchList(account_id);
        return HttpResponse.noContent();
    }

}
