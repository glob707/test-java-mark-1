# test-java-mark-1 — Template autotest project

## Structure (multi-module)
```
test-java-mark-1/
├── pom.xml              (parent — packaging pom, manages versions)
├── core/                (shared core: Element, Be, Have, ApiClient, ...)
│   └── src/main/java/com/test/mark1/
│       ├── core/        — Element, Button, Input, CheckBox, TextLabel,
│       │                  Select, Event, Condition, Be, Have, RetryAnalyzer
│       ├── pages/       — BasePage
│       ├── config/      — Settings
│       ├── utils/       — Generator
│       └── api/         — ApiConfig, ApiClient
├── ui/                  (UI tests — Playwright + TestNG)
│   └── src/test/java/com/test/mark1/
│       ├── PlaywrightManager.java
│       ├── BaseTest.java
│       └── listeners/   — AllureListener (screenshot + video + trace)
└── api/                 (API tests — REST Assured + TestNG)
    └── src/test/java/com/test/mark1/
        └── api/fixtures/  — ApiFixture
```

## Run

```bash
# All UI tests
mvn test -pl ui -am ``-Dheadless=true`

# All API tests
mvn test -pl api -am

# Specific UI test
mvn test -pl ui -am ``-Dtest=TestName` ``-Dheadless=true`

# Specific API test
mvn test -pl api -am ``-Dtest=TestName`

# Allure report
mvn allure:serve
```

### Makefile shortcuts
```bash
make ui       # mvn test -pl ui -am ``-Dheadless=true`
make api      # mvn test -pl api -am
make t T=XYZ  # specific UI test
make api-t T=XYZ  # specific API test
```

## Patterns

### UI
- Components extend BasePage
- Elements use `should(be.visible)`, `should(have.text("foo"))`, `shouldNot(be.visible)`
- Use `Element.byTestId(page, "id")`, `Element.byRole(page, role, name)`, `Element.byLabel(page, "label")` for robust selectors
- Input: `fill(text)` / `fillWithClear(text)` / `typeSlowly(text)` (NOT `type` — that was renamed to `fill`)
- Dialogs: `acceptDialog(() -> page.click("#trigger"))` — takes an action that triggers the dialog
- Allure `@Step` on business methods
- Tests extend `BaseTest`, use `page()` to get Playwright Page
- Flaky tests: `@Test(retryAnalyzer = RetryAnalyzer.class)` — 2 retries
- On failure: Allure attaches screenshot + video + HTML + Playwright trace
- Console error detection: BaseTest auto-fails test if unexpected console errors occur
- Dynamic timeout: 30s+ in Docker, 10s locally (auto-detected via /.dockerenv)
- HAR mock mode: run without backend via `-Dmock=on` (playback) or `-Dmock=record` (record new HAR files to `target/har/`)

### API
- ApiClient wraps REST Assured with logging + Allure
- Tests extend `ApiFixture`, use `client.request()` for calls

### Environment config
- `config.properties` — defaults (dev)
- `config-stage.properties` — stage overrides
- `config-prod.properties` — prod overrides
- Select via `-Denv=stage` or `-Denv=prod`

## First setup
1. Set `base.url` in `core/src/main/resources/config.properties`
2. (Optional) Create `config-stage.properties` / `config-prod.properties` for envs
3. Add your page components in `ui/src/test/.../components/`
4. Add your API tests in `api/src/test/.../api/tests/`
5. Set `headless=true` for CI

## Context7

Always use Context7 MCP tools when you need library/framework/API documentation, code examples, setup steps, or configuration — do this automatically without waiting for the user to say "use context7". Call `resolve-library-id` to find the right library, then `query-docs` to get the relevant docs.

## PITest (Mutation Testing)

### Run PITest
```bash
# Run mutation tests on core module
mvn test -pl core -am -DskipTests -DskipITs -DskipUI

# Generate mutation report
mvn org.pitest:pitest-maven:mutationCoverageReport -pl core -am

# Run with specific configuration
mvn org.pitest:pitest-maven:mutationCoverageReport -pl core -am -DmutationThreshold=60 -DcoverageThreshold=80
```

### Configuration
- **Mutation Threshold**: 60% (minimum mutation score)
- **Coverage Threshold**: 80% (minimum line coverage)
- **Output Formats**: XML and HTML reports
- **Threads**: 4 (parallel execution)

### Reports
- **XML Report**: `target/pit-reports/pit-reports.xml`
- **HTML Report**: `target/pit-reports/index.html`

## CI/CD

```bash
# Docker UI tests
docker compose run --rm ui-tests

# Docker API tests
docker compose run --rm api-tests

# With custom URL
BASE_URL=https://stage.example.com docker compose run --rm ui-tests

# Maven directly
mvn test -pl ui ``-Dheadless=true` ``-Dbase.url=https://stage.example.com
```
