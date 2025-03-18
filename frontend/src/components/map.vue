<script setup>
import { onMounted } from "vue";
import "leaflet/dist/leaflet.css"; // Import Leaflet CSS
import L from "leaflet"; // Import Leaflet itself

onMounted(() => {
  if (typeof window === "undefined") return; // Prevents SSR errors
  map = L.map("map").setView([60.39, 5.32], 11);

  L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
    attribution: '&copy; OpenStreetMap contributors',
  }).addTo(map);

  fetchLocations();

  });





  // var mapContainer = document.getElementById("map");
  // if (!mapContainer) return;

  let map;



  async function fetchLocations() {
    try {
      const response = await fetch("http://localhost:9876/locations");
      if (!response.ok) throw new Error("Failed to fetch locations");
      const locations = await response.json();

      locations.forEach((location) => {
        L.marker([location.latitude, location.longitude])
            .addTo(map)
            .bindPopup(`<b>${location.locationname}</b><br>${location.address}`);
      });
    } catch (error) {
      console.error("Error fetching locations:", error);
    }
  }







  map.on('popupopen', function (e){
    document.getElementById('popuplink').addEventListener('click', function (event){
      event.preventDefault();
      window.open(this.href, '_blank');
    })
  })



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





