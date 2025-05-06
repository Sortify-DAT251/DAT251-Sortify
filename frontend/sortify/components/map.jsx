"use client";

import { useEffect, useRef, useState } from "react";
import L from "leaflet";
import "leaflet/dist/leaflet.css";
import './map.css';

import markerIcon2x from 'leaflet/dist/images/marker-icon-2x.png';
import markerIcon from 'leaflet/dist/images/marker-icon.png';
import markerShadow from 'leaflet/dist/images/marker-shadow.png';
import {useSearch} from "@/app/context/searchContext";

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
    const [routeInfo, setRouteInfo] = useState(null);

    const { search, setSearch } = useSearch();
    const skipNextSearchEffect = useRef(false);

    const [filters, setFilters] = useState({
        "Plast": true,
        "Restavfall": false,
        "Matavfall": true,
        "Papp og papir": false,
        "El-avfall": false,
        "Klær": false,
        "Farlig avfall": false,
        "Glass og metall": false,
        "Hageavfall": false,
        "Brennbart avfall": false
    });

    const toggleFilter = (type) => {
        setFilters(prev => ({
            ...prev,
            [type]: !prev[type]
        }));
    };

    // Initialize map only once on component mount
    useEffect(() => {
        console.log("Initializing Map")
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

        console.log("Finding current position");
        navigator.geolocation.getCurrentPosition(
            (position) => {
                console.log("coords",position.coords)
                const newLocation = {
                    lat: position.coords.latitude,
                    lon: position.coords.longitude,
                };
                setUserLocation(newLocation);
                // Create the user marker immediately with the new location
                createUserMarker(mapRef.current, newLocation);
            },
            (error) => console.error("Error getting location:", error.message)
        );
    }, []);

    // When both map and location are available, mark and zoom onto the users geolocation
    useEffect(() => {
        if (!mapRef.current || !userLocation) return;
        updateUserMarker(mapRef.current, userLocation)

        fetch(`http://localhost:9876/api/locations/sorted?lat=${userLocation.lat}&lon=${userLocation.lon}`)
            .then(res => res.json())
            .then(data => {
                setLocations(data);
            })
            .catch(err => console.error("Failed to fetch locations:", err));
    }, [userLocation]);

    // Update the displayed users location and the displayed locations, if they have changed
    useEffect(() => {
        if (!mapRef.current || !userLocation || locations.length === 0) return;

        let filtered = [...locations]; // Start with all locations

        // Debug
        console.log(locations)

        // Apply search filter if there's a search term
        console.log(search)
        if (search) {
            filtered = filtered.filter(loc => loc.wasteTypes.includes(search));
            const updatedFilters = Object.fromEntries(
                Object.keys(filters).map(type => [type, type === search])
            );
            setFilters(updatedFilters); // Update the filters state
            setSearch(""); // Reset search after applying
        } else {
            // Apply checkbox filters
            const selected = Object.entries(filters)
                .filter(([_, checked]) => checked)
                .map(([key]) => key);
            if (selected.length) {
                filtered = filtered.filter(loc =>
                    loc.wasteTypes.some(type => selected.includes(type))
                );
            }
        }
        // Check if there are any filtered locations
        if (filtered.length === 0) {
            // Handle case where no locations match the filters (optional)
            setNearestLocation(null);
            addLocationMarkers(mapRef.current, []); // Clear any existing markers
            return;
        }

        // Set the nearest location
        setNearestLocation(filtered[0]);
        addLocationMarkers(mapRef.current, filtered);
    }, [search, filters, locations, userLocation]);

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

                    const distanceMeters = routeData.routes[0].distance;
                    const durationSeconds = routeData.routes[0].duration;

                    setRouteInfo({
                        distance: (distanceMeters / 1000).toFixed(2),
                        duration: (durationSeconds / 60).toFixed(2),
                    });

                    console.log(`Distance to nearest: ${(distanceMeters / 1000).toFixed(2)} km`);
                    console.log(`ETA: ${(durationSeconds / 60).toFixed(2)} minutes`);

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
        <div>
            <div className="container">
                <div className="map-and-filters">
                    <div id="map" className="map" />
                    <div className="filter-panel">
                        <h3>Filter</h3>
                        <label>
                            <input
                                type="checkbox"
                                checked={filters.Plast}
                                onChange={() => toggleFilter("Plast")}
                            />
                            Plast
                        </label>
                        <label>
                            <input
                                type="checkbox"
                                checked={filters.Restavfall}
                                onChange={() => toggleFilter("Restavfall")}
                            />
                            Restavfall
                        </label>
                        <label>
                            <input
                                type="checkbox"
                                checked={filters["Matavfall"]}
                                onChange={() => toggleFilter("Matavfall")}
                            />
                            Matavfall
                        </label>
                        <label>
                            <input
                                type="checkbox"
                                checked={filters["Papp og papir"]}
                                onChange={() => toggleFilter("Papp og papir")}
                            />
                            Papp og papir
                        </label>
                        <label>
                            <input
                                type="checkbox"
                                checked={filters["El-avfall"]}
                                onChange={() => toggleFilter("El-avfall")}
                            />
                            El-avfall
                        </label>
                        <label>
                            <input
                                type="checkbox"
                                checked={filters["Klær"]}
                                onChange={() => toggleFilter("Klær")}
                            />
                            Klær
                        </label>
                        <label>
                            <input
                                type="checkbox"
                                checked={filters["Farlig avfall"]}
                                onChange={() => toggleFilter("Farlig avfall")}
                            />
                            Farlig avfall
                        </label>
                        <label>
                            <input
                                type="checkbox"
                                checked={filters["Glass og metall"]}
                                onChange={() => toggleFilter("Glass og metall")}
                            />
                            Glass og metall
                        </label>
                        <label>
                            <input
                                type="checkbox"
                                checked={filters["Hageavfall"]}
                                onChange={() => toggleFilter("Hageavfall")}
                            />
                            Hageavfall
                        </label>
                        <label>
                            <input
                                type="checkbox"
                                checked={filters["Brennbart avfall"]}
                                onChange={() => toggleFilter("Brennbart avfall")}
                            />
                            Brennbart avfall
                        </label>

                    </div>
                </div>

                <div className="info-box-container">
                    <button
                        onClick={toggleRoute}
                        disabled={!nearestLocation}
                        className="route-button"
                    >
                        {routeVisible ? "Skjul Rute" : "Finn Rute"}
                    </button>

                    {routeInfo && (
                        <div className="info-box">
                            <div><strong>Distanse:</strong> {routeInfo.distance} km</div>
                            <div><strong>Estimert tid:</strong> {routeInfo.duration} min</div>
                            <hr style={{margin: '8px 0'}}/>
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
}

/**
 * Places a marker at the user's location and centers the map
 */
function createUserMarker(map, location) {
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
 * Updates the userMarkers location
 */
function updateUserMarker(map, location) {
    if (map._userMarker) {
        map.removeLayer(map._userMarker);
    }
    const marker = L.marker([location.lat, location.lon])
        .addTo(map)
    map._userMarker = marker;
}

/**
 * Adds circular markers for all nearby locations
 */
let currentMarkerGroup = null;
function addLocationMarkers(map, locations) {
    if (currentMarkerGroup) {
        map.removeLayer(currentMarkerGroup);
    }
    const markerGroup = L.layerGroup().addTo(map);
    currentMarkerGroup = markerGroup;

    // Function to determine color based on waste types
    function getMarkerColor(wasteTypes) {
        // Remove duplicates by converting to a Set
        const uniqueWasteTypes = [...new Set(wasteTypes)];

        if (uniqueWasteTypes.length > 1) {
            // If the location handles multiple waste types, set it to blue
            return { color: 'blue', fillColor: 'lightgray' };
        } else if (uniqueWasteTypes.includes("Plast")) {
            return { color: 'green', fillColor: 'lightgray' };
        } else if (uniqueWasteTypes.includes("Glass og metall")) {
            return { color: 'blue', fillColor: 'lightgray' };
        } else if (uniqueWasteTypes.includes("El-avfall")) {
            return { color: 'gray', fillColor: 'lightgray' };
        } else if (uniqueWasteTypes.includes("Papp og papir")) {
            return { color: 'brown', fillColor: 'lightgray' };
        } else if (uniqueWasteTypes.includes("Restavfall")) {
            return { color: 'red', fillColor: 'lightgray' };
        } else {
            return { color: 'purple', fillColor: 'lightgray' }; // Default color
        }
    }

    locations.forEach((loc) => {
        // Clean up the wasteTypes by removing duplicates
        const uniqueWasteTypes = [...new Set(loc.wasteTypes)];

        // Determine marker color based on wasteTypes
        const { color, fillColor } = getMarkerColor(uniqueWasteTypes);

        L.circleMarker([loc.latitude, loc.longitude], {
            radius: 8,
            color: color, // Border color
            fillColor: fillColor, // Fill color
            fillOpacity: 0.8,
        })
            .addTo(markerGroup)
            .bindPopup(`
                <b>${loc.name}, ${loc.address}</b><br>
                ${uniqueWasteTypes.join(", ")}
            `);
    });
}

