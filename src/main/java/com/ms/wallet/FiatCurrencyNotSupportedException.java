package com.ms.wallet;

import jdk.jshell.spi.ExecutionControl;

public class FiatCurrencyNotSupportedException extends RuntimeException {
    public FiatCurrencyNotSupportedException(String msg) {
        super(msg);
    }
}
