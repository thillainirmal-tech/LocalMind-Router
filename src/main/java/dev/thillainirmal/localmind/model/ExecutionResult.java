package dev.thillainirmal.localmind.model;

import java.time.Instant;
import java.util.List;

public record ExecutionResult(
        String provider,
        String model,
        String response,
        boolean executed,
        Instant completedAt,
        List<String> policyTrace) {
}
