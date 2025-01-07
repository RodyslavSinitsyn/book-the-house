// Import the functions you need from the SDKs you need
import {initializeApp} from "https://www.gstatic.com/firebasejs/11.1.0/firebase-app.js";
import {getMessaging, getToken} from "https://www.gstatic.com/firebasejs/11.1.0/firebase-messaging.js";

// Your web app's Firebase configuration
const firebaseConfig = {
    apiKey: document.querySelector('meta[name="firebase-api-key"]').content,
    authDomain: "book-the-house-ee097.firebaseapp.com",
    projectId: "book-the-house-ee097",
    storageBucket: "book-the-house-ee097.firebasestorage.app",
    messagingSenderId: "334388783801",
    appId: "1:334388783801:web:30539ce04cd95c109bc968"
};

// Initialize Firebase
navigator.serviceWorker.register("/js/fcm-sw.js")
    .then(registration => {
        const tokenStored = localStorage.getItem("fcm_token_stored")
        if (tokenStored) {
            console.log("Do not request fcm token again...")
            return;
        }
        getToken(messaging,
            {
                serviceWorkerRegistration: registration,
                vapidKey: document.querySelector('meta[name="firebase-vapid-key"]').content,
            })
            .then((currentToken) => {
                if (currentToken) {
                    fetch('/notification/token', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json',
                            [csrfHeader()]: csrfToken()
                        },
                        body: JSON.stringify({token: currentToken})
                    })
                        .then(result => {
                            localStorage.setItem("fcm_token_stored", true)
                            console.log(result.text());
                        })
                        .catch(err => console.error("Token wasn't saved: ", err))
                } else {
                    console.log('No registration token available. Request permission to generate one.');
                }
            }).catch((err) => {
            console.log('An error occurred while retrieving token. ', err);
        })
    })

const app = initializeApp(firebaseConfig);
const messaging = getMessaging(app);