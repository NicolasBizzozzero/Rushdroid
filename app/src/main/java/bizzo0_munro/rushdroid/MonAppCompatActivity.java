package bizzo0_munro.rushdroid;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.achievement.Achievements;
import com.google.example.games.basegameutils.BaseGameUtils;

import java.util.Set;

public class MonAppCompatActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, View.OnClickListener{
    protected ClassePrincipale app;
    protected Resources.Theme theme;

    // Variables utilisées avec l'API Google Play
    protected GoogleApiClient mGoogleApiClient;
    public int REQUEST_ACHIEVEMENTS = 100;
    public int REQUEST_LEADERBOARD = 101;
    private static final int RC_SIGN_IN = 9001;          /* Request code used to invoke sign in
                                                            user interactions. */
    private boolean mResolvingConnectionFailure = false; /* Are we currently resolving a connection
                                                            failure ? */
    private boolean mSignInClicked = false;              // Has the user clicked the sign-in button?
    private boolean mAutoStartSignInFlow = true;         /* Set to true to automatically start the
                                                            sign in flow when the Activity starts.
                                                            Set to false to require the user to
                                                            click the button in order to sign in. */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Chargement du contexte de l'application
        app = (ClassePrincipale) getApplicationContext();

        // Obtention de l'orientation de l'ecran
        app.setOrientationEcran(getResources().getConfiguration().orientation);

        // Chargement du bon theme choisit par l'utilisateur
        chargementDuTheme();

        // Create the Google Api Client with access to Games
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                .build();

        // Gestion des achievements débloqués hors-ligne
        MaProgressionDeJeu maProgressionDeJeu = new MaProgressionDeJeu(this);
        if (maProgressionDeJeu.ilResteDesAchievementsASynchroniser()){
            Set<String> ensembleAchievements = maProgressionDeJeu.getAchievementsDebloquesMaisPasSynchronises();
            for (String achievement : ensembleAchievements){
                debloqueAchievement(achievement);
            }
        }
    }

    protected void chargementDuTheme(){
        /* On charge un thème différent si on est dans l'activité de selection des niveaux car
           un bug d'affichage se produisait au niveau du background de plusieurs layouts. */
        if (this.getClass().getSimpleName().equals("SelectionDesNiveaux")){
            if (app.getTHEME() == 0)
                this.setTheme(R.style.ThemeDeLApplicationEtDesActivites_DARK_SelectionDuNiveau);
            else
                this.setTheme(R.style.ThemeDeLApplicationEtDesActivites_SelectionDuNiveau);
        } else{
            if (app.getTHEME() == 0)
                this.setTheme(R.style.ThemeDeLApplicationEtDesActivites_DARK);
            else
                this.setTheme(R.style.ThemeDeLApplicationEtDesActivites);
        }
    }

    @Override
    protected void onPause(){
        super.onPause();

        // On coupe la musique si le joueur l'a activé
        if (app.isLeJoueurAActiveLaMusique()){
            app.getMediaPlayer().pause();
        }
    }

    @Override
    protected void onResume(){
        super.onResume();

        // On remet la musique si le joueur l'a activé
        if (app.isLeJoueurAActiveLaMusique()){
            app.getMediaPlayer().start();
        }
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

    public boolean debloqueAchievement(String achievementID) {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            // Call a Play Games services API method, for example:
            Games.Achievements.unlock(mGoogleApiClient, achievementID);
            return true;
        } else {
            // Alternative implementation (or warn user that they must
            // sign in to use this feature)
            MaProgressionDeJeu maProgressionDeJeu = new MaProgressionDeJeu(this);
            maProgressionDeJeu.sauvegarderAchievementPourLeSynchroniserPlusTard(achievementID);
            return false;
        }
    }

    public boolean isConnected(){
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    public void stockerAchievementHorsLigne(String achievementID){

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (mResolvingConnectionFailure) {
            // already resolving
            return;
        }

        // if the sign-in button was clicked or if auto sign-in is enabled,
        // launch the sign-in flow
        if (mSignInClicked || mAutoStartSignInFlow) {
            mAutoStartSignInFlow = false;
            mSignInClicked = false;
            mResolvingConnectionFailure = true;

            // Attempt to resolve the connection failure using BaseGameUtils.
            // The R.string.signin_other_error value should reference a generic
            // error string in your strings.xml file, such as "There was
            // an issue with sign-in, please try again later."
            if (!BaseGameUtils.resolveConnectionFailure(this,
                    mGoogleApiClient, connectionResult,
                    RC_SIGN_IN, getString(R.string.signin_other_error))) {
                mResolvingConnectionFailure = false;
                if (!ClassePrincipale.laConnectionAEchoue){
                    String texte = getString(R.string.connectezvous);
                    Toast toast = Toast.makeText(app, texte, Toast.LENGTH_LONG);
                    toast.show();
                } else
                    ClassePrincipale.laConnectionAEchoue = true;
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        if (requestCode == RC_SIGN_IN) {
            mSignInClicked = false;
            mResolvingConnectionFailure = false;
            if (resultCode == RESULT_OK) {
                mGoogleApiClient.connect();
            } else {
                // Bring up an error dialog to alert the user that sign-in
                // failed. The R.string.signin_failure should reference an error
                // string in your strings.xml file that tells the user they
                // could not be signed in, such as "Unable to sign in."
                BaseGameUtils.showActivityResultError(this,
                        requestCode, resultCode, R.string.signin_failure);
            }
        }
    }
}
