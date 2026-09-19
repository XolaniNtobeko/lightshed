# LightShed PlaceName Service

[![LightShed CI](https://github.com/XolaniNtobeko/lightshed/actions/workflows/ci.yml/badge.svg)](https://github.com/XolaniNtobeko/lightshed/actions/workflows/ci.yml)

Microservice for querying South African town names and load-shedding placement mapping.
# LightShed - Distributed Loadshedding Information System

LightShed is a lightweight, distributed loadshedding information system designed to deliver real-time load-shedding stage updates and localized suburb/town schedule filtering across South Africa.

The application is built on modern Java features (Virtual Threads / Project Loom) and follows strict **Trunk-Based Development** (direct commits to `main`) and **Test-Driven Development (RED-GREEN-REFACTOR)** methodologies.

---

## 🛠️ Tech Stack & Prerequisites

### Tech Stack
* **Language:** Java 21 (LTS)
* **Web Framework:** Javalin 5.6.3 (embedded Jetty with Loom Virtual Threads)
* **Build Tool:** Apache Maven 3.8+
* **Testing:** JUnit 5 (Jupiter)
* **Containerization:** Docker & GitHub Container Registry (GHCR)
* **CI/CD:** GitHub Actions (`ci.yml`, `release.yml`)

### Prerequisites
Before running or building the application, ensure the following tools are installed on your machine:

1. **OpenJDK 21:**
   ```bash
   java -version

Apache Maven:Bashmvn -version
Docker Engine:Bashsudo docker --version
cURL or Postman (for testing REST endpoints).📁 Project StructurePlaintextlightshed/
├── .github/
│   └── workflows/
│       ├── ci.yml               # Automated test execution on push to main
│       └── release.yml          # Tag-based container builds to GHCR
├── src/
│   ├── main/
│   │   ├── java/za/co/wethinkcode/lightshed/
│   │   │   ├── controller/
│   │   │   │   └── TownController.java
│   │   │   ├── model/
│   │   │   │   ├── ScheduleSlot.java
│   │   │   │   ├── Stage.java
│   │   │   │   ├── Town.java
│   │   │   │   └── TownSchedule.java
│   │   │   ├── service/
│   │   │   │   ├── ScheduleService.java
│   │   │   │   ├── StageService.java
│   │   │   │   ├── TownCleaner.java
│   │   │   │   └── TownRepository.java
│   │   │   └── Main.java        # Javalin application entrypoint
│   │   └── resources/
│   │       └── town.csv         # Raw place-name data source
│   └── test/
│       └── java/za/co/wethinkcode/lightshed/
│           ├── PlaceNameServerTest.java
│           ├── ScheduleServiceTest.java
│           ├── StageServiceTest.java
│           ├── TownCleanerTest.java
│           ├── TownControllerTest.java
│           └── TownRepositoryTest.java
├── Dockerfile                   # Multi-stage Docker build file
├── pom.xml                      # Maven project configuration
└── README.md
🚀 Installation & Setup1. Clone the RepositoryBashgit clone [https://github.com/xolanintobeko/lightshed.git](https://github.com/xolanintobeko/lightshed.git)
cd lightshed
2. Verify and Run Unit TestsAlways run the test suite locally before pushing to main:Bashmvn clean test
   ⚙️ Running the ApplicationOption 1: Fast Developer Run (Maven Exec)Bashmvn compile exec:java -Dexec.mainClass="za.co.wethinkcode.lightshed.Main"
   Option 2: Standalone JARBash# Package application
   mvn clean package -DskipTests

# Run compiled JAR
java -jar target/lightshed-placename-service-1.0-SNAPSHOT.jar
Option 3: Local Docker BuildBash# Build Docker image
sudo docker build -t lightshed:dev .

# Run container on port 7000
sudo docker run --rm -p 7000:7000 --name lightshed-dev lightshed:dev
Option 4: Published Image from GHCRBashsudo docker run --rm -p 7000:7000 --name lightshed-ghcr ghcr.io/xolanintobeko/lightshed:v1.0.0-iter1
📡 REST API DocumentationServer runs by default at http://localhost:7000.1. Place-Name Service (Iteration 1)GET /api/townsBashcurl -i http://localhost:7000/api/towns
2. Stage Service (Iteration 2)GET /api/stageBashcurl -i http://localhost:7000/api/stage
   POST /api/stageBashcurl -i -X POST http://localhost:7000/api/stage \
   -H "Content-Type: application/json" \
   -d '{"stage": 3}'
3. Schedule Service (Iteration 2)GET /api/schedule/{province}/{town}Bashcurl -i http://localhost:7000/api/schedule/Western%20Cape/George
   🐳 Docker Management ReferenceActionCommandList Running Containerssudo docker psList All Containerssudo docker ps -aView Live Server Logssudo docker logs -f lightshed-ghcrStop Server Gracefullysudo docker stop lightshed-ghcrForce Stop & Remove Containersudo docker rm -f lightshed-ghcr🔄 TDD & Trunk-Based Workflow RulesTrunk Only: Commit directly to main.RED Phase: Write unit tests defining expected behavior first (mvn clean test).GREEN Phase: Implement minimal code to make tests pass.REFACTOR Phase: Clean structure without breaking tests.Pre-Push Validation: Never push to main without a passing test run.