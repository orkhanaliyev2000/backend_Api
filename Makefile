.PHONY: dev-up dev-down run-dev test fmt lint

dev-up:
	docker compose up -d

dev-down:
	docker compose down -v

run-dev:
	SPRING_PROFILES_ACTIVE=dev gradle bootRun

test:
	gradle test

fmt:
	gradle spotlessApply

lint:
	gradle checkstyleMain spotbugsMain
