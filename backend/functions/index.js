//import firebase functions modules
const functions = require('firebase-functions');
//import admin module
const admin = require('firebase-admin');

admin.initializeApp(functions.config().firebase);


/* Listens for new messages added to /messages/:pushId and sends a notification to subscribed users */
exports.pushNotification = functions.database.ref('/posts/{pushId}').onWrite( event => {
console.log('Push notification event triggered');
/* Grab the current value of what was written to the Realtime Database */
    var valueObject = event.data.val();
/* Create a notification and data payload. They contain the notification information, and message to be sent respectively */ 
    const payload = {
        notification: {
            title: 'New notive',
            body: valueObject.postText,
            sound: "default"
        },
        data: {
            title: valueObject.user.user,
            message: valueObject.postText
        }
    };
/* Create an options object that contains the time to live for the notification and the priority. */
    const options = {
        priority: "high",
        timeToLive: 60 * 60 * 24 //24 hours
    };
return admin.messaging().sendToTopic("notifications", payload, options);
});