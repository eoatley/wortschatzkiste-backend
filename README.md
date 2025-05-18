# WortSchatzKiste Backend 🧠📚

**WortSchatzKiste** (German for "word treasure chest" or "vocabulary chest") will be a playful and educational web app to help users learn German vocabulary through dictionary lookups and spaced repetition games.

---

## ✨ Planned Features

- 🔍 Look up German words and get English translations via Wiktionary
- 🧠 Searched words are automatically saved to your personal vocabulary list
- 🎮 Practice learned words using games based on spaced repetition
- 👤 Create a user profile with a username

---

## 🚀 Getting Started

### Backend

1. Clone the backend repo:
    ```bash
    git clone git@github.com:eoatley/wortschatzkiste-backend.git
    ```

2. Start PostgreSQL using Docker:
    ```bash
    docker-compose up -d
    ```

3. Run the Scala backend with sbt:
   ```bash
   sbt run
   ```

#### Routes

1. POST /users
   ```bash
   curl -i -X POST http://localhost:8080/users -H "Content-Type: application/json" -d "{\"username\":\"emma22\"}"
   ```
1. POST /login
   ```bash
   curl -i -X POST http://localhost:8080/login -H "Content-Type: application/json" -d "{\"username\":\"emma22\"}"
   ```

### Frontend

Coming soon...

---

## 🛠️ Tech Stack

- **Backend**: Scala 3, http4s, Cats Effect, Doobie, PostgreSQL
- **Frontend**: React, TypeScript
- **Deployment**: TBC

---

## 📖 License

MIT — see [LICENSE](LICENSE) file for details.
