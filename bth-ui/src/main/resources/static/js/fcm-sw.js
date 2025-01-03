self.addEventListener('push', event => {
    const notification = event.data.json().notification
    event.waitUntil(self.registration.showNotification(
        notification.title,
        {
            body: notification.body,
            icon: notification.image,
            data: {
                url: notification.click_action
            }
        }
    ));
});

self.addEventListener('notificationClick', event => {
    console.log(event)
    event.waitUntil(clients.openWindow(event.notification.data.url));
})