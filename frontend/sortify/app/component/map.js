"use client";

import { useEffect, useRef, useState } from "react";
import L from "leaflet";
import "leaflet/dist/leaflet.css";

import markerIcon2x from 'leaflet/dist/images/marker-icon-2x.png';
import markerIcon from 'leaflet/dist/images/marker-icon.png';
import markerShadow from 'leaflet/dist/images/marker-shadow.png';

delete L.Icon.Default.prototype._getIconUrl;

L.Icon.Default.mergeOptions({
    iconRetinaUrl: markerIcon2x.src ?? markerIcon2x,
    iconUrl: markerIcon.src ?? markerIcon,
    shadowUrl: markerShadow.src ?? markerShadow,
});

export default function Map() {
    // Store references to map and marker instances
    const mapRef = useRef(null);
    const markerRef = useRef(null);

    const [userLocation, setUserLocation] = useState(null);
    const [locations, setLocations] = useState([]);
    const [nearestLocation, setNearestLocation] = useState(null);

    const routeLayerRef = useRef(null);
    const [routeVisible, setRouteVisible] = useState(false);

    // Initialize map only once on component mount
    useEffect(() => {
        if (typeof window === 'undefined') return;

        const map = L.map('map').setView([60.39, 5.32], 11);
        mapRef.current = map;

        L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
            maxZoom: 19,
            attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a>',
        }).addTo(map);

        return () => map.remove();
    }, []);

    // Fetch user's geolocation when component mounts
    useEffect(() => {
        navigator.geolocation.getCurrentPosition(
            (position) => {
                setUserLocation({
                    lat: position.coords.latitude,
                    lon: position.coords.longitude,
                });
            },
            (error) => console.error("Error getting location:", error.message)
        );
    }, []);

    // When both map and location are available, update the map view
    useEffect(() => {
        if (!mapRef.current || !userLocation) return;
        updateUserMarker(mapRef.current, userLocation);
        fetchAndDisplayLocations(mapRef.current, userLocation, setLocations, setNearestLocation);
    }, [userLocation]);

    // Functionality to toggle the route on or off
    const toggleRoute = async () => {
        if (!mapRef.current || !userLocation || !nearestLocation) return;

        const map = mapRef.current;

        if (routeVisible && routeLayerRef.current) {
            map.removeLayer(routeLayerRef.current);
            routeLayerRef.current = null;
            setRouteVisible(false);
        } else {
            try {
                const start = `${userLocation.lon},${userLocation.lat}`;
                const end = `${nearestLocation.longitude},${nearestLocation.latitude}`;
                const routeRes = await fetch(`/api/route?start=${start}&end=${end}`);
                const routeData = await routeRes.json();

                if (routeData.routes?.length) {
                    const geoJSON = routeData.routes[0].geometry;
                    const routeLine = L.geoJSON(geoJSON, {
                        style: { color: 'blue', weight: 4 },
                    }).addTo(map);

                    routeLayerRef.current = routeLine;
                    setRouteVisible(true);
                    map.fitBounds(routeLine.getBounds());
                }
            } catch (err) {
                console.error("Error drawing route:", err);
            }
        }
    };

    // Map component
    // Button for toggling the route on or off
    return (
        <>
            <div id="map" style={{ height: '500px', width: '800px' }} />
            <button
                onClick={toggleRoute}
                disabled={!nearestLocation}
                style={{ marginTop: '10px' }}
            >
                {routeVisible ? "Hide Route" : "Show Route to Nearest Location"}
            </button>
        </>
    );
}

/**
 * Places a marker at the user's location and centers the map
 */
function updateUserMarker(map, location) {
    if (map._userMarker) {
        map.removeLayer(map._userMarker);
    }

    const marker = L.marker([location.lat, location.lon])
        .addTo(map)
        .bindPopup("You are here!")
        .openPopup();

    map._userMarker = marker;
    map.setView([location.lat, location.lon], 17);
}

/**
 * Fetches nearby locations from the backend,
 * highlights the nearest, and displays all with markers.
 */
async function fetchAndDisplayLocations(map, userLocation, setLocations, setNearestLocation) {
    try {
        const res = await fetch(`http://localhost:9876/api/locations/sorted?lat=${userLocation.lat}&lon=${userLocation.lon}`);
        if (!res.ok) throw new Error("Failed to fetch locations");

        const allLocations = await res.json();
        setLocations(allLocations);

        if (!allLocations.length) return;

        const nearest = allLocations[0];
        setNearestLocation(nearest);

        addLocationMarkers(map, allLocations);
        showNearestMarker(map, nearest);
    } catch (err) {
        console.error("Error fetching locations:", err);
    }
}

/**
 * Adds circular markers for all nearby locations
 */
function addLocationMarkers(map, locations) {
    locations.forEach((loc) => {
        L.circleMarker([loc.latitude, loc.longitude], {
            radius: 8,
            color: 'blue',
            fillColor: '#3f51b5',
            fillOpacity: 0.8,
        })
            .addTo(map)
            .bindPopup(`<b>${loc.name}, ${loc.address}</b><br>${loc.info}`);
    });
}

/**
 * Highlights the nearest location with a popup marker
 */
function showNearestMarker(map, location) {
    L.marker([location.latitude, location.longitude])
        .addTo(map)
        .bindPopup(`${location.name}, ${location.address}`)
        .openPopup();
}
