# LocalMind Router

Privacy-first Java routing for Ollama, safe deterministic tools and approved cloud fallbacks.

[![CI](https://github.com/thillainirmal-tech/LocalMind-Router/actions/workflows/ci.yml/badge.svg)](https://github.com/thillainirmal-tech/LocalMind-Router/actions/workflows/ci.yml)
![Java](https://img.shields.io/badge/Java-17-ED8B00?logo=openjdk&logoColor=white)

## What it demonstrates

- data classification before model selection
- mandatory local execution for sensitive and secret prompts
- safe tool routing that bypasses models for deterministic work
- capacity-aware Ollama model selection
- explicit cloud-fallback consent and transparent blocking
- REST API, responsive dashboard, health probes and Docker

The repository runs without Ollama or cloud credentials because it demonstrates and tests the routing control plane. Production adapters can execute the returned plan.

## Run

`mvn spring-boot:run` then open http://localhost:8083.

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
