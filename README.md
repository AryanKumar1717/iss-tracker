# 🛰️ Spring Boot ISS & Satellite Tracker API

![Java 21](https://img.shields.io/badge/Java-21-orange.svg)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.6-brightgreen.svg)
![WebSocket](https://img.shields.io/badge/WebSocket-Enabled-blue.svg)

A high-performance Spring Boot REST and WebSocket API for tracking the International Space Station (ISS) and various satellites in real-time. Built with Spring WebFlux (WebClient) and WebSocket for low-latency live streaming.

## 🏗️ Architecture Overview

This project is built using **Java 21** and **Spring Boot 4.0.6**, leveraging reactive programming principles where applicable via WebClient, and providing real-time data streaming through WebSockets.

### Tech Stack & Dependencies
*   **Language:** Java 21
*   **Framework:** Spring Boot 4.0.6, Maven
*   **Key Libraries:** 
    *   Spring WebFlux (`WebClient` for reactive HTTP requests)
    *   Spring WebSocket (for live data streaming)
    *   Spring Cache (simple)
    *   Spring Validation
    *   Lombok (Boilerplate reduction)
    *   Jackson (JSON parsing)

### Project Structure (`com.aryan.issTracker`)
*   `config/`: Configurations for `WebClient` and `WebSocket`.
*   `controller/`: REST endpoints for ISS and generic satellites.
*   `exception/`: Global error handling mechanism.
*   `model/`: Data structures like `ISSPosition`.
*   `service/`: Business logic, interacting with external APIs.
*   `websocket/`: Handlers for managing active WebSocket connections.

### External APIs
*   **[Open Notify API](http://open-notify.org/)**: Used for ISS live location and astronauts in space (Free, no API key required).
*   **[N2YO API](https://www.n2yo.com/api/)**: Used for detailed satellite tracking and flyover predictions (Free API key required).

---

## 📡 REST API Endpoints

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/api/iss/location` | Get live ISS latitude, longitude, and timestamp. |
| `GET` | `/api/iss/passes?lat={lat}&lon={lon}` | Get upcoming ISS flyover predictions for a location. |
| `GET` | `/api/iss/astronauts` | Get the list of astronauts currently in space. |
| `GET` | `/api/satellites/{noradId}` | Track any specific satellite using its NORAD ID. |

### Example JSON Response (`/api/iss/location`)

```json
{
  "timestamp": 1715276532,
  "message": "success",
  "iss_position": {
    "latitude": "23.4567",
    "longitude": "-12.3456"
  }
}
```

---

## 🛰️ Track Other Satellites (NORAD IDs)

You can track other popular satellites using the `/api/satellites/{noradId}` endpoint. Here are some common NORAD IDs:

| Satellite | NORAD ID | Description |
| :--- | :--- | :--- |
| **ISS (ZARYA)** | `25544` | International Space Station |
| **Hubble Space Telescope** | `20580` | Iconic space observatory |
| **Starlink (Random)** | `44713` | SpaceX internet constellation satellite |
| **NOAA 19** | `33591` | Weather satellite |
| **Aqua** | `27424` | Earth observation satellite |

---

## 🔌 WebSocket Live Stream

Connect to the WebSocket endpoint to receive live ISS location updates streamed every 2 seconds.

*   **Endpoint:** `ws://localhost:8080/ws/iss`

### Testing with `wscat`

1.  Install `wscat` (requires Node.js):
    ```bash
    npm install -g wscat
    ```
2.  Connect to the stream:
    ```bash
    wscat -c ws://localhost:8080/ws/iss
    ```
3.  You will receive a continuous stream of JSON messages with the ISS position.

---

## 🚀 Local Setup Instructions

1.  **Clone the repository**
    ```bash
    git clone https://github.com/AryanDevlearner/issTracker.git
    cd issTracker
    ```

2.  **Get an N2YO API Key**
    *   Sign up at [N2YO.com](https://www.n2yo.com/api/) to get a free API key.

3.  **Configure Application Properties**
    *   Open `src/main/resources/application.properties`.
    *   Add your N2YO API key:
        ```properties
        n2yo.api.key=YOUR_ACTUAL_API_KEY_HERE
        ```

    > **⚠️ SECURITY NOTE:** 
    > Never commit your actual API keys to GitHub or any public version control. If pushing this code, ensure `application.properties` is either in `.gitignore` or you use environment variables (`${N2YO_API_KEY}`) to inject the key safely.

4.  **Run the Application**
    *   Using Maven wrapper:
        ```bash
        ./mvnw spring-boot:run
        ```
    *   Or build and run the JAR:
        ```bash
        ./mvnw clean package
        java -jar target/issTracker-0.0.1-SNAPSHOT.jar
        ```

5.  **Access the API**
    *   The server will start on `http://localhost:8080` (or whichever port is configured).

---

## 👨‍💻 Author

**Aryan**
*   GitHub: [@AryanKumar1717](https://github.com/AryanKumar1717)
