import React, { useState, useCallback } from "react";
import { GoogleMap, LoadScript, Marker } from "@react-google-maps/api";

type LocationSelectorProps = {
    lat: number;
    lng: number;
    onLocationChange: (latitude: number, longitude: number, street: string, number: number, postalCode: string) => void;
};

const LocationSelector: React.FC<LocationSelectorProps> = ({ lat, lng, onLocationChange }) => {
    const [position, setPosition] = useState<{ lat: number; lng: number }>({ lat, lng });

    const handleMapClick = useCallback(async (e: google.maps.MapMouseEvent) => {
        if (e.latLng) {
            const newLat = e.latLng.lat();
            const newLng = e.latLng.lng();
            setPosition({ lat: newLat, lng: newLng });
            await fetchAddress(newLat, newLng); // Fetch and update the address
           // onLocationChange(newLat, newLng); // Update parent component
        }
    }, [onLocationChange]);

    const fetchAddress = async (lat: number, lng: number) => {
        const apiKey = 'AIzaSyBRjYJ-6-HMhU-_s9GOuT5t9B4cfJKY6KY';
        try {
            const response = await fetch(
                `https://maps.googleapis.com/maps/api/geocode/json?latlng=${lat},${lng}&key=${apiKey}`
            );
            const data = await response.json();
            if (data.results && data.results.length > 0) {
                const addressComponents = data.results[0].address_components;

                const street = addressComponents.find((comp: { types: string | string[]; }) => comp.types.includes("route"))?.long_name || '';
                const number = addressComponents.find((comp: { types: string | string[]; }) => comp.types.includes("street_number"))?.long_name || '';
                const postalCode = addressComponents.find((comp: { types: string | string[]; }) => comp.types.includes("postal_code"))?.long_name || '';
                const fullAddress = data.results[0].formatted_address;

                // Call the parent callback function with the new values
                onLocationChange(lat, lng, street, number, postalCode);

                console.log("Address:", fullAddress);
            } else {
                console.error("No address found for the given coordinates");
            }
        } catch (error) {
            console.error("Error fetching address:", error);
        }
    };

    return (
        <LoadScript googleMapsApiKey="AIzaSyBRjYJ-6-HMhU-_s9GOuT5t9B4cfJKY6KY">
            <GoogleMap
                mapContainerStyle={{ height: "300px", width: "100%" }}
                center={position}
                zoom={13}
                onClick={handleMapClick}
            >
                <Marker position={position} />
            </GoogleMap>
        </LoadScript>
    );
};

export default LocationSelector;
