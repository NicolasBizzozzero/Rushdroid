package bizzo0_munro.rushdroid;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MonAppCompatActivity extends AppCompatActivity {
    protected ClassePrincipale app;
    protected Resources.Theme theme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Chargement du contexte de l'application
        app = (ClassePrincipale) getApplicationContext();

        // Obtention de l'orientation de l'ecran
        app.setOrientationEcran(getResources().getConfiguration().orientation);

        // Chargement du bon theme choisit par l'utilisateur
        chargementDuTheme();
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
}
