# Astro Frontend Code Explanation

This Astro project is a frontend for a recycling application, featuring interactive maps and statistics. Below is a 
breakdown of the important components, their functions, and how they come together.

---

## Project Overview

The frontend is built with **Astro**, a modern static site generator. It integrates different technologies like **Vue** 
for maps and **React** for some interactive components. The pages are made up of Astro components, which can use both 
Vue and React. Astro compiles everything into static HTML for performance, but allows for dynamic interactivity when 
needed.

### Project Structure

```graphql
my-project/
├── backend/                         # Backend project (Spring Boot)
│   ├── src/
│   └── docker-compose.yml           # Docker for backend
├── frontend/                        # Frontend project (Astro + React)
│   ├── public/                      # Public assets (images, static files)
│   ├── src/
│   │   ├── components/              # React components (tsx files)
│   │   │   ├── App.tsx              # Main React App
│   │   │   └── SomeComponent.tsx    # Other React components
│   │   ├── layouts/                 # Astro layouts (astro files)
│   │   │   └── DefaultLayout.astro  # Default layout for the site
│   │   ├── pages/                   # Astro pages (astro files)
│   │   │   ├── index.astro          # Landing page or main page
│   │   │   └── about.astro          # Example page
│   │   ├── styles/                  # Styles for your site (CSS/SCSS)
│   │   │   └── global.css           # Global styles
│   │   ├── tsconfig.json            # TypeScript config for frontend
│   │   └── package.json             # NPM package for frontend
├── docker-compose.yml               # Root docker-compose file for both frontend and backend
└── README.md
```

### File Structure

- `src/`: Contains all the source files for the frontend.
  - `components/`: Reusable components like `Header`, `Main`, `Home`, etc.
  - `pages/`: Represents the actual pages that users navigate to, such as `index.astro`, `map.astro`, and `stats.astro`.

### Key Concepts:

- **Astro Components**: Components like `.astro` files define both markup and layout. These components can include HTML,
- CSS, and JavaScript.
- **Client-side Rendering**: Some components (like maps or interactive parts) use `client:only` to specify that they 
- should only run in the browser (client-side), not during server-side rendering (SSR).

---

## Main Files Breakdown

### 1. `src/pages/index.astro`

```astro
<Layout>
  <Main>
    <Header />
    <Home client:only="react" />
    <LeafletMap client:only="vue" />
  </Main>
</Layout>
```

- **Layout**: This wraps the content with a consistent structure.
- **Header**: A navigation bar at the top of the page, containing links like "Home" and "Statistics".
- **Home**: A React component for the home page, dynamically rendered in the browser.
- **LeafletMap**: A Vue component that initializes and displays the map using the Leaflet library. This runs on the 
- client-side only.

### 2. `src/pages/map.astro`

```astro
<Layout>
    <h1>Interactive map</h1>
    <LeafletMap client:only="vue" />
</Layout>
```

- This page contains a simple title and the map. It uses the `LeafletMap` component (same as in the `index.astro` page),
ensuring that the map runs client-side with Vue.

### 3. `src/pages/stats.astro`

```astro
<Layout>
    <Main>
        <Header />
        <Statistics />
    </Main>
</Layout>
```

### 4. `src/components/Header.astro`

```astro
<div id="container">
    <header>
        <nav>
            <a href="/" id="logo">
                <img src={sortifyHeader.src} alt="sortify logo" width="200" />
            </a>
            <div id="links">
                <a href="/" id="button">Home</a>
                <a href="/stats" id="button">Statistics</a>
                <a href="https://docs.astro.build/en/develop-and-build/" id="button">Docs</a>
            </div>
        </nav>
    </header>
</div>
```

- The **Header** component defines the site’s navigation, including a logo and links for the home, statistics, and 
documentation.

### 5. `src/components/LeafletMap.vue`

```vue
<script setup>
import { onMounted } from "vue";
import "leaflet/dist/leaflet.css";
import L from "leaflet";

onMounted(() => {
  var map = L.map("map").setView([60.39, 5.32], 11);
  L.tileLayer("https://tile.openstreetmap.org/{z}/{x}/{y}.png", {
    maxZoom: 19,
    attribution:
      '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a>',
  }).addTo(map);

  var marker = L.marker([60.382042650854174, 5.229395571867122]).addTo(map);
  marker.bindPopup("<b>Rema1000</b><br>Recycle here!");
});
</script>

<template>
  <div id="map"></div>
</template>
```

- **LeafletMap.vue** initializes a **Leaflet map** on the client-side. It uses Vue's lifecycle hooks like `onMounted` 
to ensure the map is set up after the component is mounted.
- The map uses OpenStreetMap and has several markers showing recycling locations.
