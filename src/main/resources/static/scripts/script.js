//Initialize map centered on Edinburgh
const map = L.map('map').setView([55.9533, -3.1883], 13);

// Add tile layer (show the map)
L.tileLayer('https://{s}.basemaps.cartocdn.com/light_all/{z}/{x}/{y}{r}.png', {
    attribution: '&copy; OpenStreetMap contributors &copy; CARTO',
    maxZoom: 20
}).addTo(map);

// Global variables
let deliveryMarker = null;
let deliveryLocation = null;
let pathLayer = null;
let orderCounter = 1;

//Handle map clicks to set delivery location
map.on('click', function(e) {
    const lat = e.latlng.lat;
    const lng = e.latlng.lng;

    deliveryLocation = {lat, lng};
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

//Function to place an order, called when the user clicks the dispatch button
async function placeOrder() {
    //If the delivery location has not been set then display a message to ask the user to select a location
    if (!deliveryLocation) {
        showError('Please select a delivery location on the map');
        return;
    }

    //Get the variables of the order
    const dateValue = document.getElementById('date').value;
    const timeValue = document.getElementById('time').value;
    const maxCostValue = document.getElementById('maxCost').value;

    // Build the MedDispatchRec object for the order as defined in the spec
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

    //Disable button during processing
    const btn = document.getElementById('dispatchBtn');
    btn.disabled = true;
    btn.textContent = 'Processing...';

    hideMessages();

    //Once the order has been set up, fetch the delivery path and show it on the map
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
    //Remove existing path
    if (pathLayer) {
        map.removeLayer(pathLayer);
    }

    //Draw the new path from the geoJson
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

    //Fit map to show entire path
    if (pathLayer.getBounds().isValid()) {
        map.fitBounds(pathLayer.getBounds(), { padding: [50, 50] });
    }
}

//Function to clear the user input if the button is clocked
function clearSelection() {
    //Remove marker
    if (deliveryMarker) {
        map.removeLayer(deliveryMarker);
        deliveryMarker = null;
    }

    //Remove path
    if (pathLayer) {
        map.removeLayer(pathLayer);
        pathLayer = null;
    }

    //Clear form
    deliveryLocation = null;
    document.getElementById('coordinates').value = '';
    document.getElementById('date').value = '';
    document.getElementById('time').value = '';
    document.getElementById('capacity').value = '1.0';
    document.getElementById('cooling').checked = false;
    document.getElementById('heating').checked = false;
    document.getElementById('maxCost').value = '';

    //Hide messages
    hideMessages();
}

//3 Helper functions to show/hide error/success messages
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

//Function to check if the user is logged in -> if not redirect immediately to the login page
async function ensureLoggedIn() {
    if (window.localStorage.href !== 'login.html' || window.localStorage.href !== 'signup.html') {
        try {
            const res = await fetch('/api/auth/session');
            const data = await res.json();
            if (!data.authenticated) {
                window.location.href = 'login.html';
            }
            //If the user is logged in, then add the account button to the webpage
            const navRight = document.querySelector('.nav-right');
            const accountBtn = document.createElement('button');
            accountBtn.textContent = 'My Account';
            accountBtn.onclick = () => window.location.href = 'account.html';
            navRight.insertBefore(accountBtn, navRight.firstChild);
        } catch (e) {
            window.location.href = 'login.html';
        }
    }
}

//call the ensure login function to redirect to the login page if user isnt logged in
ensureLoggedIn();