export async function initMap() {
    if (typeof window !== "undefined") {
        const L = (await import("leaflet")).default;

        // Ensure the container exists
        if (!document.getElementById("map")) {
            console.error("Map container not found");
            return;
        }

        var map = L.map("map").setView([60.3913, 5.3221], 13);

        L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
            maxZoom: 19,
            attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
        }).addTo(map);
    }
}


