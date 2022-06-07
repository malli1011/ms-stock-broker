package com.ms.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.ms.broker.data.InMemoryStore;
import com.ms.broker.model.Symbol;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
class SymbolsControllerTest {

    @Inject
    @Client("/symbols")
    HttpClient client;

    @Inject
    InMemoryStore inMemoryStore;

    @BeforeEach
    void setup() {
        inMemoryStore.initializeWith(10);
    }

    @Test
    @DisplayName("Symbols endpoint returns list of symbols")
    void getAllTest() {
        var response = client.toBlocking().exchange("/", JsonNode.class);
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals(10, response.getBody().get().size());
    }

    @Test
    @DisplayName("SymbolsByvalue endpoint returns a symbol with the given value")
    void getSymbolByValueTest() {
        var symbol = new Symbol("TEST");
        inMemoryStore.getSymbols().put(symbol.value(), symbol);
        var response = client.toBlocking().exchange("/" + symbol.value(), Symbol.class);
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals(symbol.value(), response.body().value());
    }


    @ParameterizedTest
    @MethodSource("dataProvider")
    @DisplayName("SymbolsByFilter endpoint returns a List of symbols")
    void getSymbolByFilterTest(int max, int offset, int expectedCount) {
        int val = 1;
        var response = client.toBlocking().exchange("/filter?max=" + max + "&offset=" + offset, JsonNode.class);
        assertEquals(HttpStatus.OK, response.getStatus());
        Assertions.assertEquals(expectedCount, response.body().size());

    }


    private static Stream<Arguments> dataProvider() {
        return Stream.of(
                Arguments.arguments(1, 1, 1),
                Arguments.arguments(0, 1, 0),
                Arguments.arguments(2, 1, 2),
                Arguments.arguments(5, 7, 3)
        );
    }
}