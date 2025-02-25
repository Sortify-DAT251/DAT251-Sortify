<script setup>
import { onMounted } from "vue";
import "leaflet/dist/leaflet.css"; // Import Leaflet CSS
import L from "leaflet"; // Import Leaflet itself

onMounted(() => {
  if (typeof window === "undefined") return; // Prevents SSR errors

  // var mapContainer = document.getElementById("map");
  // if (!mapContainer) return;

  // var map = L.map(mapContainer).setView([51.505, -0.09], 13);
  var map = L.map('map').setView([60.39, 5.32], 13);

  L.tileLayer("https://tile.openstreetmap.org/{z}/{x}/{y}.png", {
    maxZoom: 19,
    attribution: '&copy; <a href=\"https://www.openstreetmap.org/copyright\">OpenStreetMap</a>',
  }).addTo(map);
});

</script>

<template>
  <div id="map"></div>
</template> 
<style>
  #map {
    height: 400px;
    width: 500px
  }
</style>





