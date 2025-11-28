document.getElementById('signupForm').addEventListener('submit', async (e) => {
    //Don't submit the form immediately
    e.preventDefault();

    //Get the variables inputted by the user
    const name = document.getElementById('name').value;
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;

    //Call the signup endpoint from authorisation controller
    const response = await fetch('/api/auth/signup', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ name, email, password })
    });


    const data = await response.json();
    const messageDiv = document.getElementById('message');

    //If the login was successful redirect to the login page for the user to login otherwise display why the user failed
    if (response.ok) {
        messageDiv.textContent = 'Account created! Redirecting...';
        messageDiv.className = 'success';
        messageDiv.style.display = 'block';
        setTimeout(() => window.location.href = 'login.html', 1500);
    } else {
        messageDiv.textContent = data.error || 'Signup failed';
        messageDiv.className = 'error';
        messageDiv.style.display = 'block';
    }
});