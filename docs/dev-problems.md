# Dev Problems and Fixes 🛠️

A running list of issues I ran into while developing my project, and how I solved them.

---

## 🐘 Postgres Docker Port Conflict (Windows)

**Date:** 2025-05-16  
**Problem:** Could not connect to Postgres running in Docker. Password authentication failed on `localhost:5432`.

**Cause:** A local Postgres install was already running on port 5432.

**Fix:**
- Stopped local Postgres via `services.msc`, or

**Tips:**
- Use `netstat -aon | findstr :5432` to check if a port is in use.

---