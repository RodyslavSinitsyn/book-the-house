// Asynchronous function to get geolocation coordinates
function getGeoCoordinates() {
    return new Promise((resolve, reject) => {
        if ("geolocation" in navigator) {
            navigator.geolocation.getCurrentPosition(function(position) {
                const latitude = position.coords.latitude;
                const longitude = position.coords.longitude;

                console.log("Latitude:", latitude);
                console.log("Longitude:", longitude);

                // Resolve the promise with the coordinates
                resolve({
                    latitude: latitude,
                    longitude: longitude
                });
            }, function(error) {
                console.error("Error occurred: ", error);
                showInfoNotification("Geolocation is not available or permission is denied.");
                reject(error);
            });
        } else {
            showInfoNotification("Geolocation is not supported by your browser.");
            reject(new Error("Geolocation not supported"));
        }
    });
}

// Function to find nearest posts
function findNearestPosts() {
    // Get geolocation coordinates asynchronously
    getGeoCoordinates()
        .then(coordinates => {
            console.log(coordinates)
            window.location.href = `/posts/nearest?longitude=${coordinates.longitude}&latitude=${coordinates.latitude}`;
        })
        .catch(error => console.error('Error getting geolocation:', error));
}