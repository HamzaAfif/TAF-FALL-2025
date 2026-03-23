# 📊 Diagrammes Architecture - TAF-Refactored

Cette page contient les diagrammes automatiquement générés (Mermaid) qui documentent l'architecture, le flux et les dépendances du projet TAF.

## 1️⃣ Architecture Générale

```mermaid
graph TB
    subgraph Client["🖥️ Client Layer"]
        UI["Angular Frontend<br/>Port 4200"]
        Browser["Web Browser"]
    end

    subgraph API["🔌 API Layer"]
        Gateway["Spring Boot Backend<br/>Port 8080"]
        Auth["JWT Security<br/>& Auth Service"]
        OpenAPI["OpenAPI/Swagger<br/>Documentation"]
    end

    subgraph Services["⚙️ Services"]
        TestCtrl["Test Controller<br/>(REST API)"]
        SeleniumSvc["Selenium Service<br/>(Browser Automation)"]
        PerfSvc["Performance Service<br/>(Gatling/JMeter)"]
    end

    subgraph Data["💾 Data Layer"]
        MongoDB["MongoDB<br/>Test Results"]
        Cache["Redis Cache<br/>(Optional)"]
    end

    subgraph Tools["🛠️ External Tools"]
        Selenium["Selenium Hub<br/>Driver Management"]
        Gatling["Gatling Engine<br/>Performance Tests"]
        JMeter["JMeter Engine<br/>Load Testing"]
    end

    subgraph Python["🐍 Python Service"]
        GenSvc["Test Generation<br/>Service (FastAPI)"]
        BAML["BAML Configuration<br/>LLM Integration"]
    end

    subgraph CI["🚀 CI/CD & Ops"]
        GitHub["GitHub Actions<br/>Pipeline"]
        Sonar["SonarCloud<br/>Quality Gate"]
        Docker["Docker Registry<br/>GHCR + Docker Hub"]
    end

    Browser -->|HTTP/REST| UI
    UI -->|API Calls| Gateway
    Gateway -->|Auth| Auth
    Gateway -->|Orchestrate| TestCtrl
    TestCtrl -->|Use| SeleniumSvc
    TestCtrl -->|Use| PerfSvc
    SeleniumSvc -->|Control| Selenium
    PerfSvc -->|Use| Gatling
    PerfSvc -->|Use| JMeter
    Gateway -->|Read/Write| MongoDB
    Auth -->|Cache| Cache
    GenSvc -->|LLM Config| BAML
    Gateway -->|Call| GenSvc
    GitHub -->|Build & Test| Docker
    GitHub -->|Analyze| Sonar
    
    style Client fill:#e1f5ff
    style API fill:#f3e5f5
    style Services fill:#fff3e0
    style Data fill:#f1f8e9
    style Tools fill:#fce4ec
    style Python fill:#e8f5e9
    style CI fill:#fff9c4
```

**Vue d'ensemble:**
- **Frontend**: Angular SPA communicating via REST API
- **Backend**: Spring Boot microservice (principale Gateway)
- **Services**: Selenium pour automation, Gatling/JMeter pour performance
- **Data**: MongoDB for persistence
- **CI/CD**: GitHub Actions + SonarCloud + Docker publishing

---

## 2️⃣ Pipeline CI/CD

```mermaid
graph LR
    subgraph Pipeline["🚀 CI/CD Pipeline Stages"]
        direction LR
        Checkout["1️⃣ Checkout<br/>Code"]
        BuildBackend["2️⃣ Build<br/>Backend"]
        BuildFront["2️⃣ Build<br/>Frontend"]
        BuildPython["2️⃣ Build<br/>Python"]
        BuildPerf["2️⃣ Build<br/>Performance"]
        
        LintJava["3️⃣ Lint Java<br/>Checkstyle+PMD"]
        LintJS["3️⃣ Lint JS<br/>ESLint"]
        LintPython["3️⃣ Lint Python<br/>flake8"]
        
        TestUnit["4️⃣ Unit Tests<br/>JUnit+Karma"]
        TestIntegration["4️⃣ Integration<br/>Tests"]
        TestCoverage["4️⃣ Coverage<br/>JaCoCo+Karma"]
        
        Sonar["5️⃣ SonarCloud<br/>Analysis"]
        Docker["6️⃣ Docker Build<br/>& Publish"]
        Docs["7️⃣ Generate Docs<br/>JavaDoc+Compodoc"]
        Reports["8️⃣ Publish Reports<br/>& GitHub Pages"]
    end

    Checkout --> BuildBackend
    Checkout --> BuildFront
    Checkout --> BuildPython
    Checkout --> BuildPerf
    
    BuildBackend --> LintJava
    BuildFront --> LintJS
    BuildPython --> LintPython
    
    LintJava --> TestUnit
    LintJS --> TestUnit
    LintPython --> TestUnit
    
    TestUnit --> TestIntegration
    TestIntegration --> TestCoverage
    TestCoverage --> Sonar
    
    Sonar --> Docker
    Docker --> Docs
    Docs --> Reports

    style Checkout fill:#e3f2fd
    style BuildBackend fill:#fff3e0
    style BuildFront fill:#fff3e0
    style BuildPython fill:#fff3e0
    style BuildPerf fill:#fff3e0
    style LintJava fill:#f3e5f5
    style LintJS fill:#f3e5f5
    style LintPython fill:#f3e5f5
    style TestUnit fill:#c8e6c9
    style TestIntegration fill:#c8e6c9
    style TestCoverage fill:#c8e6c9
    style Sonar fill:#ffccbc
    style Docker fill:#ffd54f
    style Docs fill:#b3e5fc
    style Reports fill:#a5d6a7
```

**Stages:**
1. Checkout code with full history
2. Build all modules in parallel
3. Lint & format validation
4. Run all test suites (unit + integration)
5. SonarCloud analysis + Quality Gate
6. Docker image build & push
7. Generate documentation
8. Publish reports & deploy to GitHub Pages

---

## 3️⃣ Flux d'Exécution d'un Test

```mermaid
sequenceDiagram
    participant User as 👤 User/Tester
    participant Frontend as 🖥️ Angular Frontend
    participant Backend as 🔌 Spring Backend
    participant TestCtrl as 🧪 Test Controller
    participant Selenium as 🤖 Selenium Service
    participant Browser as 🌐 Target Browser
    participant DB as 💾 MongoDB
    participant Results as 📊 Test Results

    User->>Frontend: 1. Input test scenario
    Frontend->>Frontend: 2. Validate form
    Frontend->>Backend: 3. POST /api/tests/execute
    Backend->>Backend: 4. JWT auth + validation
    Backend->>TestCtrl: 5. Route to Test Controller
    TestCtrl->>Selenium: 6. Create test session
    Selenium->>Browser: 7. Open browser instance
    Browser->>Browser: 8. Navigate & execute actions
    Browser->>Selenium: 9. Return results
    Selenium->>TestCtrl: 10. Aggregate results
    TestCtrl->>DB: 11. Store test execution record
    DB->>Backend: 12. Confirm save
    Backend->>Results: 13. Generate report
    Results->>Frontend: 14. Return report data
    Frontend->>User: 15. Display results & metrics

    Note over User,Results: Serial flow for single test execution<br/>Parallel execution supported in Performance module
```

---

## 4️⃣ Dépendances des Modules

```mermaid
graph TB
    subgraph Backend["Backend - Java Modules"]
        direction TB
        
        subgraph Controllers["🎯 Controllers"]
            TestCtrl["TestController"]
            TestSeleniumCtrl["TestSeleniumController"]
            TestApiCtrl["TestApiController"]
        end
        
        subgraph Services["⚙️ Services"]
            SeleniumSvc["SeleniumService"]
            TestSvc["TestService"]
            PerformanceSvc["PerformanceService"]
        end
        
        subgraph Security["🔐 Security"]
            JwtUtils["JwtUtils"]
            UserDetailsImpl["UserDetailsImpl"]
            AuthInterceptor["AuthInterceptor"]
        end
        
        subgraph Models["📦 Models"]
            DTOs["DTOs (LoginRequest, etc)"]
            Entities["Entities (User, Test, etc)"]
            Payloads["Response Payloads"]
        end
        
        subgraph Repos["🗄️ Repositories"]
            UserRepo["UserRepository"]
            TestRepo["TestRepository"]
        end
    end

    subgraph Frontend["Frontend - Angular Modules"]
        direction TB
        
        subgraph Components["🖼️ Components"]
            AppComp["AppComponent"]
            LoginComp["LoginComponent"]
            TestApiComp["TestApiComponent"]
            PerfComp["PerformanceComponent"]
        end
        
        subgraph Services2["⚙️ Services"]
            AuthService["AuthService"]
            TestApiService["TestApiService"]
            PerformanceService["PerformanceService"]
            UserService["UserService"]
        end
        
        subgraph Guards["🛡️ Guards"]
            AuthGuard["AuthGuard"]
            TokenStorage["TokenStorageService"]
        end
    end

    subgraph Performance["Performance Modules"]
        direction TB
        Gatling["Gatling Tests"]
        JMeter["JMeter Tests"]
        PerfReports["Performance Reports"]
    end

    subgraph Python["Python Service"]
        direction TB
        GenService["Test Generation API"]
        BAML["BAML LLM Config"]
    end

    TestCtrl -->|use| SeleniumSvc
    TestCtrl -->|use| TestSvc
    SeleniumSvc -->|depends| DTOs
    AuthInterceptor -->|validate| JwtUtils
    AppComp -->|use| AuthService
    AppComp -->|use| TestApiService
    TestApiService -->|call| TestCtrl
    AuthService -->|call| JwtUtils
    PerformanceSvc -->|orchestrate| Gatling
    PerformanceSvc -->|orchestrate| JMeter
    PerfComp -->|use| PerformanceService
    TestSvc -->|persist| Repos
    GenService -->|config| BAML

    style Controllers fill:#fff3e0
    style Services fill:#f3e5f5
    style Security fill:#ffebee
    style Models fill:#e8f5e9
    style Repos fill:#e1f5fe
    style Components fill:#fff3e0
    style Services2 fill:#f3e5f5
    style Guards fill:#ffebee
    style Performance fill:#fce4ec
    style Python fill:#e8f5e9
```

---

## 5️⃣ Déploiement & Containers

```mermaid
graph TB
    subgraph LocalDev["🖥️ Local Development"]
        DockerCompose["docker-compose-local-test.yml"]
        MongoDB1["MongoDB<br/>Port 27017"]
        Backend1["Backend Container<br/>Port 8080"]
        Frontend1["Frontend Container<br/>Port 4200"]
        Selenium1["Selenium Hub<br/>Port 4444"]
        
        DockerCompose -->|spins up| MongoDB1
        DockerCompose -->|spins up| Backend1
        DockerCompose -->|spins up| Frontend1
        DockerCompose -->|spins up| Selenium1
    end

    subgraph Production["☁️ Production / Staging"]
        direction TB
        GitHub["GitHub Actions<br/>CI/CD Pipeline"]
        
        subgraph Registry["Docker Registry"]
            GHCR["GHCR<br/>ghcr.io/<...>"]
            DockerHub["Docker Hub<br/>docker.io/hamzaafif/<...>"]
        end
        
        subgraph Deployment["Kubernetes / Docker Swarm"]
            K8sBackend["Backend Pod/Service<br/>Replicas: N"]
            K8sMongo["MongoDB StatefulSet<br/>Persistent Volume"]
            K8sNginx["Nginx Ingress<br/>Load Balancer"]
        end
    end

    GitHub -->|build| GHCR
    GitHub -->|build & push| DockerHub
    GHCR -->|deploy| K8sBackend
    DockerHub -->|deploy| K8sBackend
    K8sBackend -->|write| K8sMongo
    K8sNginx -->|route| K8sBackend

    style LocalDev fill:#e1f5fe
    style Production fill:#fff3e0
    style Registry fill:#f3e5f5
    style Deployment fill:#c8e6c9
```

---

## 6️⃣ Test Pyramid - Couverture

```mermaid
graph TB
    subgraph TestPyramid["🔺 Test Pyramid"]
        E2E["🔴 E2E Tests<br/>5-10%<br/>Full workflows<br/>(TODO: Add Cypress)"]
        IntegrationTests["🟠 Integration Tests<br/>15-25%<br/>Module interactions<br/>IntegrationTest.java<br/>20+ scénarios"]
        UnitTests["🟢 Unit Tests<br/>70-80%<br/>Individual functions<br/>54 Java + 30 TS<br/>Total: 84+ tests"]
    end

    subgraph JavaTests["☕ Backend Tests (Java)"]
        JUnit["JUnit 5 Framework"]
        Mockito["Mockito Mocking"]
        
        UnitJava["Unit Tests<br/>- JwtUtilsTest (7)<br/>- UserDetailsImplTest (2)<br/>- LoginRequestTest (8)<br/>- Controllers (10)<br/>- Services (20+)"]
        
        IntegrationJava["Integration Tests<br/>- IntegrationTest (20)<br/>- E2E Workflows<br/>- DB interactions<br/>- Auth flow"]
        
        JUnit -->|powers| UnitJava
        Mockito -->|mocks| UnitJava
        JUnit -->|powers| IntegrationJava
    end

    subgraph TypeScriptTests["📘 Frontend Tests (TypeScript)"]
        Karma["Karma Test Runner"]
        Jasmine["Jasmine Framework"]
        KarmaCodeCov["Karma Coverage"]
        
        UnitTS["Unit Tests (30+)<br/>- Components (10)<br/>- Services (8)<br/>- Guards (2)<br/>- Interceptors (2)<br/>- Utilities (8)"]
        
        Karma -->|runs| UnitTS
        Jasmine -->|framework| UnitTS
        KarmaCodeCov -->|measure| UnitTS
    end

    subgraph CoverageReports["📊 Coverage Reports"]
        JaCoCo["JaCoCo Backend<br/>target/site/jacoco/"]
        KarmaCov["Karma Frontend<br/>coverage/"]
        Sonar["SonarCloud<br/>Aggregated metrics<br/>+ Quality Gate"]
        
        JaCoCo -->|uploaded| Sonar
        KarmaCov -->|uploaded| Sonar
    end

    subgraph CI["🚀 CI/CD Integration"]
        GithubActions["GitHub Actions"]
        Artifacts["Artifacts Archive<br/>JUnit XML, HTML reports"]
        
        GithubActions -->|execute| UnitJava
        GithubActions -->|execute| IntegrationJava
        GithubActions -->|execute| UnitTS
        GithubActions -->|generate| CoverageReports
        CoverageReports -->|store| Artifacts
    end

    TestPyramid -->|implements| JavaTests
    TestPyramid -->|implements| TypeScriptTests
    JavaTests -->|report| CoverageReports
    TypeScriptTests -->|report| CoverageReports
    CoverageReports -->|publish| CI

    style TestPyramid fill:#fff3e0
    style JavaTests fill:#c8e6c9
    style TypeScriptTests fill:#bbdefb
    style CoverageReports fill:#ffccbc
    style CI fill:#ffd54f
```

---

## 📝 Notes

- **Tous les diagrammes** sont générés automatiquement depuis des fichiers `.mmd` (Mermaid format)
- **Format Mermaid** est supporté nativement par GitHub et rend légende directement dans Markdown
- **Sources**: Voir dossier `docs/diagrams/`
- **Génération SVG**: Le pipeline CI/CD génère les versions PNG/SVG pour publication statique

## Mise à Jour des Diagrammes

Pour modifier un diagramme, édite le fichier `.mmd` correspondant:
- `architecture.mmd` - Vue d'ensemble système
- `ci-cd-pipeline.mmd` - Stages du pipeline
- `test-execution-flow.mmd` - Séquence d'exéc test
- `module-dependencies.mmd` - Modules et dépendances
- `deployment.mmd` - Infrastructure et déploiement
- `test-pyramid.mmd` - Stratégie test et couverture

Puis commit & push - les diagrammes se mettent à jour automatiquement dans GitHub Pages.
