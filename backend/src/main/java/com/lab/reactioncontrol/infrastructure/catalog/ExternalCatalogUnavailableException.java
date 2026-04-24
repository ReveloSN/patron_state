package com.lab.reactioncontrol.infrastructure.catalog;

public class ExternalCatalogUnavailableException extends RuntimeException {

    public ExternalCatalogUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExternalCatalogUnavailableException(String message) {
        super(message);
    }
}
