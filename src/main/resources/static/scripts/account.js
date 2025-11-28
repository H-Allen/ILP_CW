//fetch all of the orders for a specific user
async function loadOrders() {
    try {
        //Fetch ther user session from the authorisation controller endpoint
        const sessionRes = await fetch('/api/auth/session');
        const session = await sessionRes.json();

        //If the user isn't logged in go to the login page
        if (!session.authenticated) {
            window.location.href = 'login.html';
            return;
        }

        //Set the welcome message
        document.getElementById('userInfo').textContent = `Welcome, ${session.name}`;

        //Fetch the orders from the authorisation controller endpoint for the logged in user
        const ordersRes = await fetch('/api/auth/orders');
        const orders = await ordersRes.json();

        const container = document.getElementById('ordersContainer');

        //If the user has no orders display "No orders yet"
        if (orders.length === 0) {
            container.innerHTML = '<div class="no-orders">No orders yet</div>';
            return;
        }

        //Otherwise create the table to display all of the fetched orders
        let html = '<table class="orders-table"><thead><tr><th>Date</th><th>Location</th><th>Capacity</th><th>Features</th><th>Cost</th><th>Moves</th><th>Drone</th></tr></thead><tbody>';

        //Add a row for each order
        orders.forEach(order => {
            const date = new Date(order.createdAt).toLocaleDateString();
            const features = [];
            if (order.cooling) features.push('Cooling');
            if (order.heating) features.push('Heating');

            html += `<tr>
                        <td>${date}</td>
                        <td>${order.lat.toFixed(4)}, ${order.lng.toFixed(4)}</td>
                        <td>${order.capacity} kg</td>
                        <td>${features.join(', ') || 'None'}</td>
                        <td>£${order.totalCost.toFixed(2)}</td>
                        <td>${order.totalMoves}</td>
                        <td>${order.droneId}</td>
                    </tr>`;
        });

        html += '</tbody></table>';
        container.innerHTML = html;
    } catch (error) {
        console.error('Error loading orders:', error);
    }
}

//If the user clicks logout, call the logout endpoint
async function logout() {
    await fetch('/api/auth/logout', { method: 'POST' });
    window.location.href = 'login.html';
}

//Call the async load Orders function
loadOrders();