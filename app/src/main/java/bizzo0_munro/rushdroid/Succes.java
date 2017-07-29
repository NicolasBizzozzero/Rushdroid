package bizzo0_munro.rushdroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.plus.Plus;
import com.google.example.games.basegameutils.BaseGameUtils;

public class Succes extends Activity
        implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, View.OnClickListener{
    private GoogleApiClient mGoogleApiClient;
    public int REQUEST_ACHIEVEMENTS = 100;

    /*// Variables que je sais pas à quoi ça sert
    private static int RC_SIGN_IN = 9001;
    private boolean mResolvingConnectionFailure = false;
    private boolean mAutoStartSignInFlow = true;
    private boolean mSignInClicked = false;*/

    // 2eme essai de variables on sait pas a quoi ca sert
    private static final String TAG = "RushDroid";
    // Request code used to invoke sign in user interactions.
    private static final int RC_SIGN_IN = 9001;
    // Are we currently resolving a connection failure?
    private boolean mResolvingConnectionFailure = false;
    // Has the user clicked the sign-in button?
    private boolean mSignInClicked = false;
    // Set to true to automatically start the sign in flow when the Activity starts.
    // Set to false to require the user to click the button in order to sign in.
    private boolean mAutoStartSignInFlow = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create the Google Api Client with access to Plus and Games
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                .build();

        setContentView(R.layout.activity_succes);

        // set this class to listen for the button clicks
        //findViewById(R.id.button_sign_in).setOnClickListener(this);
        //findViewById(R.id.button_sign_out).setOnClickListener(this);
        findViewById(R.id.bouton_achievement).setOnClickListener(this);
    }

    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    // Shows the "sign in" bar (explanation and button).
    private void showSignInBar() {
        Log.d(TAG, "Showing sign in bar");
        //findViewById(R.id.sign_in_bar).setVisibility(View.VISIBLE);
        //findViewById(R.id.sign_out_bar).setVisibility(View.GONE);
    }

    // Shows the "sign out" bar (explanation and button).
    private void showSignOutBar() {
        Log.d(TAG, "Showing sign out bar");
        //findViewById(R.id.sign_in_bar).setVisibility(View.GONE);
        //findViewById(R.id.sign_out_bar).setVisibility(View.VISIBLE);
    }

    public void onClickDebloqueAchivement(View v) {
        BaseGameUtils.showAlert(this, "Vous avez débloqué un achievement coucou !");
        Games.Achievements.unlock(mGoogleApiClient, "CgkIs4fd4JUVEAIQAA");
        /*if (mGoogleApiClient.isConnected()) {

        }*/

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            /*case R.id.button_sign_in:
                // start the sign-in flow
                Log.d(TAG, "Sign-in button clicked");
                mSignInClicked = true;
                mGoogleApiClient.connect();
                break;
            case R.id.button_sign_out:
                // sign out.
                Log.d(TAG, "Sign-out button clicked");
                mSignInClicked = false;
                Games.signOut(mGoogleApiClient);
                mGoogleApiClient.disconnect();
                showSignInBar();
                break;
            case R.id.button_win:
                // win!
                Log.d(TAG, "Win button clicked");
                BaseGameUtils.showAlert(this, "Tu as gagné");
                if (mGoogleApiClient.isConnected()) {
                    // unlock the "Trivial Victory" achievement.
                    Games.Achievements.unlock(mGoogleApiClient, "CgkIs4fd4JUVEAIQAA");
                }
                break;*/
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "onConnected() called. Sign in successful!");
        showSignOutBar();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended() called. Trying to reconnect.");
        mGoogleApiClient.connect();
    }

    public void onClickVoirAchievements(View v) {
        startActivityForResult(Games.Achievements.getAchievementsIntent(mGoogleApiClient), REQUEST_ACHIEVEMENTS);
    }

    public void onClickSeConnecter(View v) {
        Log.d("JEDEBUG", "Avant la tentative de connexion :" + mGoogleApiClient.isConnected());
        mGoogleApiClient.connect();
        Log.d("JEDEBUG", "Après la tentative de connexion :" + mGoogleApiClient.isConnected());
    }

    public void onClickSeDeconnecter(View v) {
        if (mGoogleApiClient.isConnected()) {
            mSignInClicked = false;
            Games.signOut(mGoogleApiClient);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed() called, result: " + connectionResult);

        if (mResolvingConnectionFailure) {
            Log.d(TAG, "onConnectionFailed() ignoring connection failure; already resolving.");
            return;
        }

        if (mSignInClicked || mAutoStartSignInFlow) {
            mAutoStartSignInFlow = false;
            mSignInClicked = false;
            mResolvingConnectionFailure = BaseGameUtils.resolveConnectionFailure(this, mGoogleApiClient,
                    connectionResult, RC_SIGN_IN, "string sign in other error");
        }
        showSignInBar();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(this, MenuPrincipal.class);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        if (requestCode == RC_SIGN_IN) {
            Log.d(TAG, "onActivityResult with requestCode == RC_SIGN_IN, responseCode="
                    + responseCode + ", intent=" + intent);
            mSignInClicked = false;
            mResolvingConnectionFailure = false;
            if (responseCode == RESULT_OK) {
                mGoogleApiClient.connect();
            } else {
                BaseGameUtils.showActivityResultError(this,requestCode,responseCode, R.string.unknown_error);
            }
        }
    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }
}
