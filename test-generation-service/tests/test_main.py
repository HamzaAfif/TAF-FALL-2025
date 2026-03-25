from pathlib import Path
import sys

from fastapi.testclient import TestClient

sys.path.insert(0, str(Path(__file__).resolve().parents[1]))

from app.main import (
    JMX_TEST_GENERATION_ENDPOINT,
    _extract_number,
    _fallback_generate,
    app,
)


client = TestClient(app)


def test_health_endpoint_returns_ok() -> None:
    response = client.get("/health")

    assert response.status_code == 200
    assert response.json() == {"status": "ok"}


def test_generate_jmeter_plan_returns_success_payload() -> None:
    payload = {
        "prompt": "Create an HTTPS POST load test for api.example.com with 20 users and ramp up 10"
    }

    response = client.post(JMX_TEST_GENERATION_ENDPOINT, json=payload)

    assert response.status_code == 200

    body = response.json()
    assert body["status"] == "success"
    assert "test_plan" in body
    assert body["test_plan"]["domain"] == "api.example.com"


def test_extract_number_returns_default_when_missing() -> None:
    value = _extract_number("no numeric hint", [r"(\d+)\s*users"], "10")

    assert value == "10"


def test_extract_number_matches_first_valid_pattern() -> None:
    value = _extract_number(
        "ramp-up 15 users",
        [r"ramp(?:-|\s)?up\s*(\d+)", r"(\d+)\s*users"],
        "5",
    )

    assert value == "15"


def test_fallback_generate_extracts_url_and_json_payload() -> None:
    prompt = (
        'Create an HTTPS POST test for https://api.example.com/orders with 25 users '
        'ramp-up 8 for 120 seconds and 3 loops body {"sku":"A1","qty":2}'
    )

    plan = _fallback_generate(prompt)

    assert plan.protocol == "HTTPS"
    assert plan.method == "POST"
    assert plan.domain == "api.example.com"
    assert plan.path == "/orders"
    assert plan.nbThreads == "25"
    assert plan.rampTime == "8"
    assert plan.duration == "120"
    assert plan.loop == "3"
    assert plan.data == '{"sku":"A1","qty":2}'


def test_fallback_generate_extracts_domain_and_path_without_full_url() -> None:
    prompt = "Build a GET test for app.example.net path /v1/health with 12 users"

    plan = _fallback_generate(prompt)

    assert plan.protocol == "HTTP"
    assert plan.method == "GET"
    assert plan.domain == "app.example.net"
    assert plan.path == "/v1/health"
    assert plan.nbThreads == "12"
