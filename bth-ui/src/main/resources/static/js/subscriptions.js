function enableSubscription(subUserId) {
    fetch(`/subscribe`,
        {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: `subscribedUserId=${subUserId}`,
        })
        .then(response => {
            if (response.ok) {
                alert('Subscription enabled');
                location.reload();
            } else {
                alert('Failed to enable subscription');
            }
        });
}

function disableSubscription(subUserId) {
    fetch(`/unsubscribe`,
        {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: `subscribedUserId=${subUserId}`,
        })
        .then(response => {
            if (response.ok) {
                alert('Subscription disabled');
                location.reload();
            } else {
                alert('Failed to disable subscription');
            }
        });
}