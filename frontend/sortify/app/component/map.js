"use client"; // Ensure this component runs only on the client

import { MapContainer, TileLayer, Marker, Popup } from "react-leaflet";
import "leaflet/dist/leaflet.css";
import L from "leaflet";
import { useEffect, useState } from "react";

export default function Map() {
    useEffect(() => {
        if (typeof window === 'undefined') return; // Prevent SSR issues

        const map = L.map('map').setView([60.39, 5.32], 11);

        L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
            maxZoom: 19,
            attribution:
                '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a>',
        }).addTo(map);

        fetchLocations(map);

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
    }, []);

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
            L.marker([location.latitude, location.longitude])
                .addTo(map)
                .bindPopup(
                    `<b>${location.name}, ${location.address}</b><br>${location.info}`
                );
        });
    } catch (error) {
        console.error('Error fetching locations:', error);
    }
}
