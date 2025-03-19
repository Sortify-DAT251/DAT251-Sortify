<script setup>
import { onMounted } from "vue";
import "leaflet/dist/leaflet.css"; // Import Leaflet CSS
import L from "leaflet"; // Import Leaflet itself



onMounted(() => {
  if (typeof window === "undefined") return; // Prevents SSR errors
  let map = L.map("map").setView([60.39, 5.32], 11);

  L.tileLayer("https://tile.openstreetmap.org/{z}/{x}/{y}.png", {
    maxZoom: 19,
    attribution: '&copy; <a href=\"https://www.openstreetmap.org/copyright\">OpenStreetMap</a>',
  }).addTo(map);

  fetchLocations();


  map.on('popupopen', function (e){
    document.getElementById('popuplink').addEventListener('click', function (event){
      event.preventDefault();
      window.open(this.href, '_blank');
    })
  })

});

async function fetchLocations() {
  try {
    const response = await fetch("http://localhost:9876/locations");
    if (!response.ok) throw new Error("Failed to fetch locations");
    const locations = await response.json();

    locations.forEach((location) => {
      L.marker([location.latitude, location.longitude])
          .addTo(map)
          .bindPopup(`<b>${location.locationname, location.address}</b><br>${location.info}`);
    });
  } catch (error) {
    console.error("Error fetching locations:", error);
  }
}

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





