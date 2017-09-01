package paperbaglabs.school_android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;


import paperbaglabs.school_android.models.User;
import paperbaglabs.school_android.variables.BaseActivity;
import paperbaglabs.school_android.variables.FirebaseUtils;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private static final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.googleBtn).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.googleBtn:
                showProgressDialog();
                signIn();
        }
    }

    // Calls For the SignInIntent found in BaseActivity
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == RC_SIGN_IN) {
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                if (result.isSuccess()) {
                    GoogleSignInAccount account = result.getSignInAccount();
                    firebaseAuthWithGoogle(account);
                } else {
                    dismissProgressDialog();
                }
            } else {
                dismissProgressDialog();
            }
        } else {
            dismissProgressDialog();
        }
    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            User user = new User();
                            String photoUrl = null;
                            if (account.getPhotoUrl() != null) {
                                user.setPhotoUrl(account.getPhotoUrl().toString());
                            }
                            //Storing user details locally
                            user.setEmail(account.getEmail());
                            user.setUser(account.getDisplayName());
                            user.setUid(mAuth.getCurrentUser().getUid());

                            // Pushes User Object to Firebase
                            FirebaseUtils.getUserRef(account.getEmail().replace(".", ","))
                                    .setValue(user, new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                            mFirebaseUser = mAuth.getCurrentUser();
                                            finish();
                                        }
                                    });
                        } else {
                            dismissProgressDialog();
                        }
                    }
                });
    }
}


/*


OLD AUTH CODE...
TODO: Integrate Peel Student or Teacher Email Check
TODO: Fix The Databse refrence to where the student object is being pushed

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());

        mProgressDialog.setMessage("Signing in please wait...");
        mProgressDialog.show();

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            //Storing user details locally
                            String name = user.getDisplayName();
                            String email = user.getEmail();
                            String school = name.substring(name.indexOf('-') + 1);

                            mDatabaseReference = FirebaseDatabase.getInstance().getReference();

                            //Example Teacher Email: p69696969@pdsb.net
                            //Example Student Email: 696969@pdsb.net

                            if(email.startsWith("p") && email.endsWith("@pdsb.net"))
                                switchToLoginAndPopulateDb(user, school, name, "teachers");

                            else if(!(email.startsWith("p")) && email.endsWith("@pdsb.net"))
                                switchToLoginAndPopulateDb(user, school, name, "students");

                            else{
                                user.delete()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful())
                                                    Log.d(TAG, "User account deleted.");
                                            }
                                        });
                                Toast.makeText(LoginActivity.this, "Please login with a @pdsb.net account",
                                        Toast.LENGTH_LONG).show();
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                        mProgressDialog.dismiss();

                    }
                });
    }
    //Type == "students" or "teachers"
    private void switchToLoginAndPopulateDb(FirebaseUser user, String school, String name, String type){
        mDatabaseReference.child(school).child(type).child(user.getUid()).child("name").setValue(name);

    }


*/