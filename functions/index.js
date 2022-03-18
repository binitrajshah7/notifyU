// Create and Deploy Your First Cloud Functions
// The Cloud Functions for Firebase SDK to create cloud functions and setup triggers.
const functions = require('firebase-functions');

// The Firebase Admin SDK to access the Firebase Realtime Database
const admin = require('firebase-admin');
admin.initializeApp();

// listen for path
// Database triggered function
exports.sendNotification = functions.database.ref('/Notification/{notification_id}')
    .onCreate((snapshot, context) => { 
    const text = snapshot.val().text;                   // notification text
    const receiver_id = snapshot.val().receiver_id;     // receiver id to send notification
    const title = snapshot.val().title;                 // title is declared but its value is never used

    // get the token for the receiver_id
        return admin.database().ref('/tokens/' + receiver_id).once('value').then(function (snapshot) {
            const receiver_token = snapshot.val();
            const payload = {
                notification: {
                    title: title,
                    body: text,
                    icon: 'default',
                    click_action: 'com.vineet.firebasepushnotification'
                }
            };
            // send notification
            return admin.messaging().sendToDevice(receiver_token, payload)
        });
     });



