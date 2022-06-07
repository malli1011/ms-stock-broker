package com.ms.broker.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.ms.broker.data.InMemoryAccountStore;
import com.ms.broker.model.Symbol;
import com.ms.broker.model.WatchList;
import io.micronaut.http.*;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
class WatchListControllerTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(WatchListController.class);
    private static final UUID TEST_ACCOUNT_ID = WatchListController.ACCOUNT_ID;

    @Inject
    @Client("/account/watchlist")
    HttpClient client;
    @Inject
    InMemoryAccountStore inMemoryAccountStore;

    @BeforeEach
    void setup() {
        inMemoryAccountStore.deleteWatchList(TEST_ACCOUNT_ID);
    }

    @Test
    @DisplayName("Returns Empty watchlist for Test Account")
    void getTest() {
        final WatchList result = client.toBlocking().retrieve(HttpRequest.GET("/"), WatchList.class);
        assertNull(result.symbols());
        assertTrue(inMemoryAccountStore.getWatchList(TEST_ACCOUNT_ID).symbols().isEmpty());
    }

    @Test
    @DisplayName("Returns WatchList for Test Account")
    void getTestWithTestAccount() {
        inMemoryAccountStore.updateWatchList(TEST_ACCOUNT_ID, new WatchList(
                Stream.of("AAPL", "GOOGL", "MSFT")
                        .map(Symbol::new)
                        .toList()
        ));

        var response = client.toBlocking().exchange("/", JsonNode.class);
        assertEquals(HttpStatus.OK, response.getStatus());

        assertEquals("""
                {
                  "symbols" : [ {
                    "value" : "AAPL"
                  }, {
                    "value" : "GOOGL"
                  }, {
                    "value" : "MSFT"
                  } ]
                }""", Objects.requireNonNull(response.body()).toPrettyString());


    }

    @Test
    @DisplayName("Can Update Watch List for Test Account")
    void testPut() {
        var symbols = Stream.of("APPL", "GOOGL", "MSFT").map(Symbol::new).toList();
        var request = HttpRequest.PUT("/", new WatchList(symbols))
                .accept(MediaType.APPLICATION_JSON);
        var response = client.toBlocking().exchange(request);
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals(symbols, inMemoryAccountStore.getWatchList(TEST_ACCOUNT_ID).symbols());

    }

    @Test
    @DisplayName("Delete given id from watch list")
    void testDelete() {
        inMemoryAccountStore.updateWatchList(TEST_ACCOUNT_ID, new WatchList(
                Stream.of("AAPL", "GOOGL", "MSFT")
                        .map(Symbol::new)
                        .toList()
        ));

        var response = client.toBlocking().exchange(HttpRequest.DELETE("/" + TEST_ACCOUNT_ID));
        assertEquals(HttpStatus.NO_CONTENT, response.getStatus());
        assertTrue(inMemoryAccountStore.getWatchList(TEST_ACCOUNT_ID).symbols().isEmpty());
    }
}