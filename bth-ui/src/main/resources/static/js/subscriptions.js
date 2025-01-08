function enableSubscription(subUserId) {
    fetch(`/subscribe`,
        {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
                [csrfHeader()]: csrfToken()
            },
            body: `subscribedUserId=${subUserId}`,
        })
        .then(response => {
            if (response.ok) {
                showInfoNotification('Subscription enabled', 1000, () => location.reload());
            } else {
                showInfoNotification('Failed to enable subscription');
            }
        });
}

function disableSubscription(subUserId) {
    fetch(`/unsubscribe`,
        {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
                [csrfHeader()]: csrfToken()
            },
            body: `subscribedUserId=${subUserId}`,
        })
        .then(response => {
            if (response.ok) {
                showInfoNotification('Subscription disabled', 1000, () => location.reload());
                // location.reload();
            } else {
                showInfoNotification('Failed to disable subscription');
            }
        });
}