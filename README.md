## 🔧 Setup Instructions

Follow these steps to set up and run the backend project locally:

1. **Clone the repository**:
   ```bash
   git clone https://github.com/Thoraat/mercor-challenge.git
   cd mercor-challenge
   ```

2. **Ensure you have Java 21 installed**.

3. **Build and run the application using Maven**:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

4. **Run tests and generate reports**:
   ```bash
   mvn clean test
   ```

5. **View reports**:
   - Code coverage: `reports/jacoco/index.html`
   - JUnit reports: `reports/junit/*.txt`

---

# Referral Network Backend

This is a Spring Boot-based backend service designed to manage a referral network system. It allows adding referrals between users, querying referral relationships, and performing analytics such as top referrers, reach, flow centrality, and optimal bonus allocation.

---

## 🚀 Features

- **Add Referrals**: Connect a candidate to a referrer while avoiding cycles or self-referrals.
- **Query Referrals**:
    - Get direct referrals of a user.
    - Get total reach (users referred directly or indirectly).
    - Identify top referrers by total reach.
- **Analytics**:
    - Unique coverage-based top expanders.
    - Flow centrality scores based on shortest paths.
    - Compute the minimum bonus needed to reach adoption goals.

---

## 📦 Tech Stack

- **Spring Boot 3.5.4**
- **Java 21**
- **JUnit 5** for testing
- **Maven** for build and dependency management
- **JaCoCo** for code coverage reports

---

## 🧪 Running Tests & Generating Reports

To run all tests and generate coverage and JUnit reports:

```bash
mvn clean test
```

- **Coverage Reports**: Generated at  
  `target/site/jacoco/index.html`

- **JUnit Reports**: Generated at  
  `target/surefire-reports/`

Open coverage report in browser:

```bash
xdg-open target/site/jacoco/index.html
```

---

## 🗂️ Project Structure

```bash
src/
├── main/
│   └── java/
│       └── com.example.backend/
│           ├── controller/         # REST controller (if applicable)
│           └── service/            # Core referral logic
└── test/
    └── java/
        └── com.example.backend/
            └── service/            # Unit tests for service layer
```

---

## 📊 Functionality Highlights

- **Cycle Detection**: Ensures a user cannot be referred by someone already downstream in their own referral tree.
- **Reach Calculation**: Uses BFS to measure the size of the subtree (excluding self).
- **Top Unique Expanders**: Greedy algorithm to choose users covering the maximum unique downstream candidates.
- **Flow Centrality**: Calculates how often a user lies on the shortest path between all pairs.
- **Bonus Optimization**: Determines the minimum bonus needed to achieve a specific adoption target over time.

---

## ✅ Example Endpoints (Optional)

If a controller is present, endpoints might look like:

- `POST /referrals` – Add a new referral
- `GET /referrals/{userId}` – Get direct referrals
- `GET /reach/{userId}` – Get total reach
- `GET /top-referrers?k=3`
- `GET /centrality`

(Controller can be added as needed)

---

## 🧑‍💻 Author

- **Amosh Singh**
