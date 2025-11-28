//Add a listner to the login button
document.getElementById('loginForm').addEventListener('submit', async (e) => {
    //Stop the form submitting immediately
    e.preventDefault();

    //Get the email and password from the user to look up
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;

    //Send the POST request to the authorisation endpoint to log in
    const response = await fetch('/api/auth/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email, password })
    });

    //If the user logs in then redirect to index.html otherwise display an error
    if (response.ok) {
        window.location.href = 'index.html';
    } else {
        const error = await response.json();
        document.getElementById('error').textContent = error.error || 'Login failed';
        document.getElementById('error').style.display = 'block';
    }
});
