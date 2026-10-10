package com.testpilot.ai.health;

/** Immutable data returned by the health endpoint. */
public record HealthResponse(String status, String application) {
}
