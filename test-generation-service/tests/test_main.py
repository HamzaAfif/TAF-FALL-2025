from fastapi.testclient import TestClient

from app.main import JMX_TEST_GENERATION_ENDPOINT, app


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
