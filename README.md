# ♻️ Sortify

## 🚀 About This Project

Sortify is a group project in the Dat251 subject at Western Norway University of Applied Sciences, and is developed by Mampenda, Mina, Kaja, Petter, Erik and Morten for the semester project in DAT251 - Modern Software Development Methods.

Sortify is an application designed to make it easier for the user to recycle and dispose of waste. It invcludes an interactive map which displays collection points in an easy to read way. You can also compete against your friends and family to figure out who among you are better at recycling

## 🛠️ Tech Stack

- **Frontend:** Astro with [React/Vue/Svelte] (choose your preferred framework)
- **Backend:** Spring Boot (Kotlin)
- **Database:** PostgreSQL
- **Authentication:** JWT & OAuth
- **Testing:** JUnit (Backend), Playwright/Jest (Frontend)
- **CI/CD:** GitHub Actions

## 📦 Features

✅ Search for recycling stations by waste type or location  
✅ Get notifications for garbage collection and recycling center updates  
✅ Track personal recycling habits and statistics  
✅ Plan optimized routes to visit multiple recycling stations  
✅ Social sharing of recycling achievements

## 🚀 Getting Started

### 1️⃣ Clone the Repository

```bash
git clone https://github.com/Sortify-DAT251/DAT251-Sortify.git
cd DAT251-Sortify
```

### 2️⃣ Run the Backend without Docker

If you want to run the backend without Docker, follow these steps:

```bash
cd DAT251-Sortify/backend
./gradlew clean build
./gradlew bootJar
./gradlew bootRun
```

This will start the backend server at http://localhost:9876. You can test it via your browser or an API client like Postman.

### 3️⃣ Run the Backend with Docker Compose

To run the backend using Docker Compose, follow these steps:

1. ### START DOCKER DESKTOP ON YOUR LOCAL MACHINE
2. CD into the `backend` folder and **build the jarfile**

```bash
./gradlew clean build
./gradlew bootJar
```

3. Open another terminal and run the following commands from inside the **root** folder _where the docker-compose file is located_:

```bash
# Build the application inside the docker container
docker-compose up --build
```

This will build and start both the backend and PostgreSQL containers. The backend will be available at http://localhost:9876.

### 4️⃣ Test Database Connection

To interact with the PostgreSQL database running in Docker, open another terminal while the application is running and run the following command:

```bash
docker exec -it sortify-postgres psql -U sortify-admin -d sortify-backend-db
```

This will open a PostgreSQL shell connected to your sortify-backend-db. Here’s an example of interacting with the database:

```bash
# List tables
\dt

# Inspect the "users" table
\d users

# Query the "users" table
SELECT * FROM users;
```

---

#### Can I leave the app running while working on my feature?

**Yes!** Once you’ve started the backend with Docker Compose, you can leave the application running while you work on your feature. There’s no need to restart the backend unless you’re changing configurations or dependencies. You can continue working on your branch and test your changes without needing to restart everything.

**NB!** To ensure that the changes are reflected in the running application, close it and re-run the follwing commands in the suitable directories:

```bash
# From backend-folder
./gradlew clean build
./gradlew bootJar
```

```bash
# From root-folder
docker-compose up --build
```

## But this should be handled by the CI pipeline.

### 5️⃣ Troubleshoot

If you run into trouble with the program, here are some troubleshooting steps:

**1. Clean Gradle caches and restart the build:**

```bash
./gradlew --stop
 rm -rf ~/.gradle/caches
  rm -rf .gradle
./gradlew clean build --stacktrace
./gradlew bootJar
./gradlew bootRun
```

**2. Try closing and re-opening the IDE.**

**3. If you're using `VSCode`, try uninstalling "Kotlin by fwcd" and only use "Kotling Language by mathiasfrohlich"**

**4. Common Docker Troubleshooting:**

Connect the backend to the same network when running it locally: If you’re running Docker locally and encounter network issues, try this:

```bash
docker network connect sortify-network sortify-backend
```

Check for unused Docker volumes:

```bash
docker volume ls -f "dangling=true"
```

Delete unused volumes:

```bash
docker volume prune
```

This will prompt you to confirm the removal of all unused volumes. Make sure you review what volumes are listed before confirming.

**If you're still having troubles, ask chat..**

Numbers: 0️⃣ 1️⃣ 2️⃣ 3️⃣ 4️⃣ 5️⃣ 6️⃣ 7️⃣ 8️⃣ 9️⃣
