importScripts('https://www.gstatic.com/firebasejs/10.12.0/firebase-app-compat.js');
importScripts('https://www.gstatic.com/firebasejs/10.12.0/firebase-messaging-compat.js');

// ✅ INICIALIZAR FIREBASE EN EL SERVICE WORKER
firebase.initializeApp({
    apiKey: "AIzaSyB7va5Y5oL7US3W65JfMCYjsZbnE0iGUvI",
    authDomain: "xpilot-notif.firebaseapp.com",
    projectId: "xpilot-notif",
    storageBucket: "xpilot-notif.firebasestorage.app",
    messagingSenderId: "872077118353",
    appId: "1:872077118353:web:1455bccab759151ee86830"
});

const messaging = firebase.messaging();

messaging.onBackgroundMessage(function(payload) {
    console.log('📩 Notificación en background:', payload);

    self.registration.showNotification(
        payload.notification.title,
        {
            body: payload.notification.body,
            icon: '/media/avatar.png'
        }
    );
});
