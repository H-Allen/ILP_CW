// Initialize map centered on Edinburgh
const map = L.map('map').setView([55.9533, -3.1883], 13);

// Add tile layer
L.tileLayer('https://{s}.basemaps.cartocdn.com/light_all/{z}/{x}/{y}{r}.png', {
    attribution: '&copy; OpenStreetMap contributors &copy; CARTO',
    maxZoom: 20
}).addTo(map);

// Global variables
let deliveryMarker = null;
let deliveryLocation = null;
let pathLayer = null;
let orderCounter = 1;

// Handle map clicks to set delivery location
map.on('click', function(e) {
    const lat = e.latlng.lat;
    const lng = e.latlng.lng;

    deliveryLocation = { lat, lng };
    document.getElementById('coordinates').value =
        `${lat.toFixed(6)}, ${lng.toFixed(6)}`;

    // Update or create marker
    if (deliveryMarker) {
        deliveryMarker.setLatLng(e.latlng);
    } else {
        deliveryMarker = L.marker(e.latlng).addTo(map);
    }

    // Clear any previous messages
    hideMessages();
});

async function placeOrder() {
    if (!deliveryLocation) {
        showError('Please select a delivery location on the map');
        return;
    }

    const dateValue = document.getElementById('date').value;
    const timeValue = document.getElementById('time').value;
    const maxCostValue = document.getElementById('maxCost').value;

    // Build the order object
    const order = {
        id: orderCounter++,
        date: dateValue || null,
        time: timeValue || null,
        requirements: {
            capacity: parseFloat(document.getElementById('capacity').value) || 1.0,
            cooling: document.getElementById('cooling').checked,
            heating: document.getElementById('heating').checked,
            maxCost: maxCostValue ? parseFloat(maxCostValue) : null
        },
        delivery: {
            lat: deliveryLocation.lat,
            lng: deliveryLocation.lng
        }
    };

    // Disable button during processing
    const btn = document.getElementById('dispatchBtn');
    btn.disabled = true;
    btn.textContent = 'Processing...';

    hideMessages();

    try {
        // Get the delivery path
        const pathResponse = await fetch('/api/v1/calcDeliveryPath', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify([order])
        });

        if (!pathResponse.ok) {
            throw new Error('No available drones for this delivery. Check requirements.');
        }

        const pathResult = await pathResponse.json();

        // Get the GeoJSON visualization
        const geoResponse = await fetch('/api/v1/calcDeliveryPathAsGeoJson', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify([order])
        });

        if (!geoResponse.ok) {
            throw new Error('Failed to generate visualization');
        }

        const geoJson = await geoResponse.json();

        // Display success message
        showSuccess(
            `Order #${order.id} placed successfully | ` +
            `Total Cost: £${pathResult.totalCost.toFixed(2)} | ` +
            `Total Moves: ${pathResult.totalMoves} | ` +
            `Drone(s): ${pathResult.dronePaths.map(p => p.droneId).join(', ')}`
        );

        // Visualize the path
        visualizePath(geoJson);

    } catch (error) {
        showError(error.message);
    } finally {
        // Re-enable button
        btn.disabled = false;
        btn.textContent = 'Dispatch';
    }
}

function visualizePath(geoJson) {
    // Remove existing path
    if (pathLayer) {
        map.removeLayer(pathLayer);
    }

    // Draw the new path
    pathLayer = L.geoJSON(geoJson, {
        style: function(feature) {
            return {
                color: '#333',
                weight: 3,
                opacity: 0.7,
                dashArray: '8, 4'
            };
        },
        onEachFeature: function(feature, layer) {
            if (feature.properties && feature.properties.droneId) {
                layer.bindPopup(`Drone: ${feature.properties.droneId}`);
            }
        }
    }).addTo(map);

    // Fit map to show entire path
    if (pathLayer.getBounds().isValid()) {
        map.fitBounds(pathLayer.getBounds(), { padding: [50, 50] });
    }
}

function clearSelection() {
    // Remove marker
    if (deliveryMarker) {
        map.removeLayer(deliveryMarker);
        deliveryMarker = null;
    }

    // Remove path
    if (pathLayer) {
        map.removeLayer(pathLayer);
        pathLayer = null;
    }

    // Clear form
    deliveryLocation = null;
    document.getElementById('coordinates').value = '';
    document.getElementById('date').value = '';
    document.getElementById('time').value = '';
    document.getElementById('capacity').value = '1.0';
    document.getElementById('cooling').checked = false;
    document.getElementById('heating').checked = false;
    document.getElementById('maxCost').value = '';

    // Hide messages
    hideMessages();

    // Reset map view
    map.setView([55.9533, -3.1883], 13);
}

function showSuccess(message) {
    const box = document.getElementById('messageBox');
    box.textContent = message;
    box.className = 'success';
    box.style.display = 'block';
}

function showError(message) {
    const box = document.getElementById('messageBox');
    box.textContent = message;
    box.className = 'error';
    box.style.display = 'block';
}

function hideMessages() {
    document.getElementById('messageBox').style.display = 'none';
}
