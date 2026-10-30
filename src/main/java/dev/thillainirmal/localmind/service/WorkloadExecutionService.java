package dev.thillainirmal.localmind.service;

import dev.thillainirmal.localmind.model.ExecutionResult;
import dev.thillainirmal.localmind.model.RoutingPlan;
import dev.thillainirmal.localmind.model.WorkloadRequest;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class WorkloadExecutionService {
    private final LocalMindEngine engine;
    private final OllamaGateway ollama;
    private final Counter localExecutions;
    private final Counter policyBlocks;

    public WorkloadExecutionService(LocalMindEngine engine, OllamaGateway ollama, MeterRegistry registry) {
        this.engine = engine;
        this.ollama = ollama;
        this.localExecutions = Counter.builder("localmind.executions.local").register(registry);
        this.policyBlocks = Counter.builder("localmind.executions.blocked").register(registry);
    }

    public ExecutionResult execute(WorkloadRequest request) {
        RoutingPlan plan = engine.route(request);
        if ("blocked".equals(plan.target())) {
            policyBlocks.increment();
            return new ExecutionResult(plan.target(), plan.model(),
                    "Execution denied by the configured privacy and fallback policy.", false,
                    Instant.now(), plan.trace());
        }
        if (plan.tool() != null) {
            localExecutions.increment();
            return new ExecutionResult(plan.target(), plan.model(), plan.toolResult(), true,
                    Instant.now(), plan.trace());
        }
        if (!"ollama".equals(plan.target())) {
            throw new IllegalStateException("No approved live adapter is configured for provider " + plan.target());
        }
        String output = ollama.chat(plan.model(), request.prompt());
        localExecutions.increment();
        return new ExecutionResult(plan.target(), plan.model(), output, true, Instant.now(), plan.trace());
    }
}
