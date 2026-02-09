.PHONY: dev-up dev-down run-dev test fmt lint

dev-up:
	docker compose up -d

dev-down:
	docker compose down -v

run-dev:
	SPRING_PROFILES_ACTIVE=dev ./gradlew bootRun

test:
	./gradlew test

fmt:
	./gradlew spotlessApply

lint:
	./gradlew checkstyleMain spotbugsMain
