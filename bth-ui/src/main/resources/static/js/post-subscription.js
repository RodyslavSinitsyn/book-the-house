document.querySelector('#post-subscribe-form').addEventListener('submit', function(event) {
    event.preventDefault();

    const subscribedUserId = document.querySelector('#post-subscribe-user-id').value;
    const email = document.querySelector('#post-subscribe-email').value;

    fetch('/subscribe', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
            [csrfHeader()]: csrfToken()
        },
        body: `subscribedUserId=${subscribedUserId}&email=${email}`,
    })
        .then(response => response.text())
        .then(data => {
            console.log(data)
            showInfoNotification("Successfully subscribed on user updates");
        })
        .catch(error => {
            console.error(error)
            showInfoNotification('Not subscribed, try later');
        });
});