# LocalMind Router

Privacy-first Java routing for Ollama, safe deterministic tools and approved cloud fallbacks.

> **Version 2 executes real workloads.** `POST /api/execute` applies the privacy policy and then calls a live Ollama `/api/chat` endpoint. Sensitive workloads never leave the configured local runtime. The older `/api/route` endpoint remains available for explainability and dry-run policy inspection.

[![CI](https://github.com/thillainirmal-tech/LocalMind-Router/actions/workflows/ci.yml/badge.svg)](https://github.com/thillainirmal-tech/LocalMind-Router/actions/workflows/ci.yml)
![Java](https://img.shields.io/badge/Java-17-ED8B00?logo=openjdk&logoColor=white)

## Capabilities

- data classification before model selection
- mandatory local execution for sensitive and secret prompts
- safe tool routing that bypasses models for deterministic work
- capacity-aware Ollama model selection
- explicit cloud-fallback consent and transparent blocking
- REST API, responsive dashboard, health probes and Docker

Policy evaluation and unit tests run without credentials. Live inference requires Ollama; the application fails explicitly instead of fabricating an AI response when the runtime is unavailable.

## Run

`mvn spring-boot:run` then open http://localhost:8083.

Start the complete local stack with `docker compose up --build`, then pull an allowed model into the Ollama container. Execute a workload with:

```bash
curl -X POST http://localhost:8083/api/execute \
  -H "Content-Type: application/json" \
  -d '{"prompt":"Summarize these private engineering notes","dataClass":"SENSITIVE","contextTokens":1200,"localRuntimeOnline":true,"allowCloudFallback":false}'
```

Prometheus metrics are exposed at `/actuator/prometheus`. Containers run with a read-only application filesystem and `no-new-privileges`.

```bash
curl -X POST http://localhost:8083/api/route -H "Content-Type: application/json" \
  -d '{"prompt":"Summarize secret notes","dataClass":"SECRET","contextTokens":6000,"localRuntimeOnline":true,"allowCloudFallback":false}'
```

## Verify

`mvn verify`

## Roadmap

- live Ollama model discovery and health
- prompt-injection classifier before tool routing
- signed policy bundles and team overrides
- Spring AI cloud adapters with circuit breakers
- OpenTelemetry routing spans

Apache-2.0. Use synthetic prompts only; never commit private data or credentials.
