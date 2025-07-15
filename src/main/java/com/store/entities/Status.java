package com.store.entities;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum Status {
    NEW, 
    PROCESSING, 
    COMPLETED, 
    CANCELED;

    public static String listAll() {
        return Arrays.stream(values())
                .map(s -> (s.ordinal() + 1) + ". " + s.name())
                .collect(Collectors.joining(", "));
    }
}

