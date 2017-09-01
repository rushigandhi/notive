package paperbaglabs.school_android;

import android.app.Application;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

public class Redirect extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if (FirebaseApp.getApps(this).isEmpty()) {
          // Enabling  persistence allows our app to keep all of its state even after an app restarts or crashes.
            // This fixes the issue from queue. Where the login used to show up for a quick second even if we were signed in
            // writes the data locally to the device so our app can maintain state while it's offline.
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }
    }
}
