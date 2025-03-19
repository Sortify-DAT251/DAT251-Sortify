<script setup>
import { onMounted } from "vue";
import "leaflet/dist/leaflet.css"; // Import Leaflet CSS
import L from "leaflet"; // Import Leaflet itself

onMounted(() => {
  if (typeof window === "undefined") return; // Prevents SSR errors

  // var mapContainer = document.getElementById("map");
  // if (!mapContainer) return;

  // var map = L.map(mapContainer).setView([51.505, -0.09], 13);
  var map = L.map("map").setView([60.39, 5.32], 11);

  L.tileLayer("https://tile.openstreetmap.org/{z}/{x}/{y}.png", {
    maxZoom: 19,
    attribution:
      '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a>',
  }).addTo(map);

  var remaKjøkkelvik = L.marker([60.382042650854174, 5.229395571867122]).addTo(
    map
  );
  remaKjøkkelvik.bindPopup(
    "<b>Rema1000 Kjøkkelvik</b><br>Her kan du levere klær, og kaste glass, metall og matavfall!"
  );

  var stasjonStognafjell = L.marker([
    60.405207615694536, 5.210220229866483,
  ]).addTo(map);
  stasjonStognafjell.bindPopup(
    "<b>Stognafjellsvegen</b><br>Her kan du levere klær, og kaste glass, metall og matavfall"
  );

  var birMøllendal = L.marker([60.38080146477514, 5.354526442798485]).addTo(
    map
  );
  birMøllendal.bindPopup(
    "<b>BIR Møllendalsveien</b><br>For full overisikt over hva som kan leveres her, se: <a href='https://bir.no/bli-kvitt-boss/sortering-i-miljoeparken/' id='popuplink'>bir.no</a>"
  );

  map.on("popupopen", function (e) {
    document
      .getElementById("popuplink")
      .addEventListener("click", function (event) {
        event.preventDefault();
        window.open(this.href, "_blank");
      });
  });
});
</script>

<template>
  <div id="map"></div>
</template>
<style>
#map {
  height: 400px;
  width: 500px;
}
</style>
