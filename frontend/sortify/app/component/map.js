"use client"; // Ensure this component runs only on the client

import { MapContainer, TileLayer, Marker, Popup } from "react-leaflet";
import "leaflet/dist/leaflet.css";
import L from "leaflet";
import { useEffect, useState } from "react";

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
    const [userLocation, setUserLocation] = useState(null);

    useEffect(() => {
        if (typeof window === 'undefined') return; // Prevent SSR issues

        navigator.geolocation.getCurrentPosition((position) => {
            const locationData = {
                lat: position.coords.latitude,
                lon: position.coords.longitude
            };
            setUserLocation(locationData);
        }, (error) => {
            console.error("Error getting location", error.message);
        });

        const map = L.map('map').setView([60.39, 5.32], 11);

        L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
            maxZoom: 19,
            attribution:
                '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a>',
        }).addTo(map);

        if (userLocation) {
            L.marker([userLocation.lat, userLocation.lon], 13)
                .addTo(map)
                .bindPopup("You are here!")
                .openPopup();

            map.setView([userLocation.lat, userLocation.lon], 13)
        }

        map.on('popupopen', () => {
            const link = document.getElementById('popuplink');
            if (link) {
                link.addEventListener('click', (e) => {
                    e.preventDefault();
                    window.open(link.href, '_blank');
                });
            }
        });

        return () => {
            map.remove();
        };
    }, [userLocation]);

    return (
        <div
            id="map"
            style={{ height: '400px', width: '500px' }}
        />
    );
}


async function fetchLocations(map) {
    try {
        const response = await fetch('http://localhost:9876/locations');
        if (!response.ok) throw new Error('Failed to fetch locations');
        const locations = await response.json();

        locations.forEach((location) => {
            L.circleMarker([location.latitude, location.longitude], {
                radius: 8, // base size
                color: 'blue',
                fillColor: '#3f51b5',
                fillOpacity: 0.8,
            })
                .addTo(map)
                .bindPopup(
                    `<b>${location.name}, ${location.address}</b><br>${location.info}`
                );
        });
    } catch (error) {
        console.error('Error fetching locations:', error);
    }
}
