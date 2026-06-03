package com.turkcell.cart_service.event;

import java.util.UUID;

public record TestEvent(String message, UUID productId) {
}
