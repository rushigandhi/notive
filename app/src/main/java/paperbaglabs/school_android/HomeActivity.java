package paperbaglabs.school_android;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {

    //Constants
    private final String TAG = "HomeActivity";

    //Local Members
    private Button mLogOutBtn;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Acquiring an instance of FirebaseAuth, and localizing it
        mAuth = FirebaseAuth.getInstance();

        //Initializing a FirebaseAuth StateListener
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            //Overriding stateChanged method to determine when the state of auth has varied
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null)
                    startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            }
        };

        //Acquiring reference to inflated Button
        mLogOutBtn = (Button) findViewById(R.id.logOut);
        //Respective onClickListener
        mLogOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               mAuth.signOut();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
}
