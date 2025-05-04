"use client";

import { useEffect, useRef, useState } from "react";
import L from "leaflet";
import "leaflet/dist/leaflet.css";
import './map.css';

import markerIcon2x from 'leaflet/dist/images/marker-icon-2x.png';
import markerIcon from 'leaflet/dist/images/marker-icon.png';
import markerShadow from 'leaflet/dist/images/marker-shadow.png';

delete L.Icon.Default.prototype._getIconUrl;

L.Icon.Default.mergeOptions({
    iconRetinaUrl: markerIcon2x.src ?? markerIcon2x,
    iconUrl: markerIcon.src ?? markerIcon,
    shadowUrl: markerShadow.src ?? markerShadow,
});

export default function Map({filter}) {
    // Store references to map and marker instances
    const mapRef = useRef(null);
    const markerRef = useRef(null);

    const [userLocation, setUserLocation] = useState(null);
    const [locations, setLocations] = useState([]);
    const [nearestLocation, setNearestLocation] = useState(null);

    const routeLayerRef = useRef(null);
    const [routeVisible, setRouteVisible] = useState(false);
    const [routeInfo, setRouteInfo] = useState(null);

    // For testing purposes
    console.log("filter: " + filter)

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
                setUserLocation({
                    lat: position.coords.latitude,
                    lon: position.coords.longitude,
                });
            },
            (error) => console.error("Error getting location:", error.message)
        );
    }, []);

    // When both map and location are available, mark and zoom onto the users geolocation
    useEffect( () => {
        if (!mapRef.current || !userLocation) {
            console.log("Could not find map or userLocation")
            return;
        }
        createUserMarker(mapRef.current, userLocation);
    }, []);

    // Update the displayed users location and the displayed locations, if they have changed
    useEffect(() => {

        console.log("Trying to display locations")

        if (!mapRef.current || !userLocation) {
            console.log("Could not find map or userLocation")
            return;
        }
        updateUserMarker(mapRef.current, userLocation);
        fetchAndDisplayLocations(
            mapRef.current,
            userLocation,
            setLocations,
            setNearestLocation,
            filter
        );
    }, [userLocation, filter]);

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

                    // Retrieves distance and estimates time for a trip to the destination (driving).
                    const midpoint = routeLine.getBounds().getCenter();

                    L.popup({
                        closeButton: false,
                        autoClose: false,
                        className: 'route-popup'
                    })
                        .setLatLng(midpoint)
                        .setContent(`
                        <div>
                            <strong>Distance:</strong> ${(distanceMeters / 1000).toFixed(2)} km<br/>
                            <strong>ETA:</strong> ${(durationSeconds / 60).toFixed(2)} min
                        </div>
                    `)
                        .openOn(map);

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
            <div>
                <div id="map" className="map"/>

                <div className="map-container">
                    <button
                        onClick={toggleRoute}
                        disabled={!nearestLocation}
                        style={{marginRight: '10px'}}
                    >
                        {routeVisible ? "Hide Route" : "Find Route"}
                    </button>

                    {/* ➕ Interactive Box */}
                    {routeInfo && (
                        <div className="info-box">
                            <div><strong>Distance:</strong> {routeInfo.distance} km</div>
                            <div><strong>ETA:</strong> {routeInfo.duration} min</div>
                            <hr style={{ margin: '8px 0' }} />
                            <button onClick={() => alert('Extra action!')}>Extra Action</button>
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
 * Fetches nearby locations from the backend,
 * highlights the nearest, and displays all with markers.
 */
async function fetchAndDisplayLocations(map, userLocation, setLocations, setNearestLocation, filter) {
    console.log("Fetching Locations: ")
    try {
        const res = await fetch(`http://localhost:9876/api/locations/sorted?lat=${userLocation.lat}&lon=${userLocation.lon}`);
        if (!res.ok) throw new Error("Failed to fetch locations");

        let allLocations = await res.json();
        setLocations(allLocations);
        console.log(allLocations)

        // If filter, only keep relevant locations in list
        if(filter) {
            console.log("Before filter: " + filter, allLocations)
            allLocations = allLocations.filter(location => location.wasteTypes.includes(filter))
            console.log("After filter: " + filter, allLocations)
        }

        // If no locations, return
        if (!allLocations.length) return;

        // makes the routing go to the nearest location
        const nearest = allLocations[0];
        setNearestLocation(nearest);


        addLocationMarkers(map, allLocations);
        //showNearestMarker(map, nearest);
    } catch (err) {
        console.error("Error fetching locations:", err);
    }
}

/**
 * Adds circular markers for all nearby locations
 */
let currentMarkerGroup = null;
function addLocationMarkers(map, locations) {
    if (currentMarkerGroup) {
        map.removeLayer(currentMarkerGroup)
    }
    const markerGroup = L.layerGroup().addTo(map)
    currentMarkerGroup = markerGroup

    locations.forEach((loc) => {
        L.circleMarker([loc.latitude, loc.longitude], {
            radius: 8,
            color: 'blue',
            fillColor: '#3f51b5',
            fillOpacity: 0.8,
        })
            .addTo(markerGroup)
            .bindPopup(`
  <b>${loc.name}, ${loc.address}</b><br>
  ${[...new Set(loc.wasteTypes)].join(", ")}
`);
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
