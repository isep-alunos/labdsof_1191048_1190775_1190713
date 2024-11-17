import React, { useState } from "react";
import { MapContainer, TileLayer, Marker, useMapEvents } from "react-leaflet";
import "leaflet/dist/leaflet.css";
import L from "leaflet";

L.Icon.Default.mergeOptions({
    iconUrl: require('leaflet/dist/images/marker-icon.png'),
    iconRetinaUrl: require('leaflet/dist/images/marker-icon-2x.png'),
    shadowUrl: require('leaflet/dist/images/marker-shadow.png'),
});

type LocationSelectorProps = {
    lat: number;
    lng: number;
    onLocationChange: (latitude: number, longitude: number) => void;
};

const LocationSelector: React.FC<LocationSelectorProps> = ({ lat, lng, onLocationChange }) => {
    const [position, setPosition] = useState<[number, number]>([lat, lng]);

    const LocationMarker = () => {
        useMapEvents({
            click(e) {
                const { lat, lng } = e.latlng;
                setPosition([lat, lng]);
                onLocationChange(lat, lng); // Update parent component
            },
        });

        return position ? <Marker position={position}></Marker> : null;
    };

    return (
        <MapContainer
            center={position}
            zoom={13}
            style={{ height: "300px", width: "100%" }}
        >
            <TileLayer
                url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
            />
            <LocationMarker />
        </MapContainer>
    );
};

export default LocationSelector;
