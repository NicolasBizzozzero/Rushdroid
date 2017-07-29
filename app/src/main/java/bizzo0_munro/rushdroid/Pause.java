package bizzo0_munro.rushdroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

public class Pause extends MonAppCompatActivity{
    SharedPreferences preferencesDuJoueur;
    SharedPreferences.Editor editeurDesPreferencesDuJoueur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pause);

        // Chargement des preferences du joueur et de son éditeur
        preferencesDuJoueur = this.getSharedPreferences("Options", Context.MODE_PRIVATE);
        editeurDesPreferencesDuJoueur = preferencesDuJoueur.edit();

        // On pose un écouteur sur le bouton des effets sonores
        CheckBox checkBoxEffetsSonores = (CheckBox) findViewById(R.id.checkBoxEffetsSonores);
        if (app.isLeJoueurAActiveLesEffetsSonores())
            checkBoxEffetsSonores.setChecked(true);
        checkBoxEffetsSonores.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // On active les effets sonores
                    app.setLeJoueurAActiveLesEffetsSonores(true);
                    editeurDesPreferencesDuJoueur.putBoolean("EffetsSonores", true);
                    editeurDesPreferencesDuJoueur.apply();
                } else {
                    // On désactive les effets sonores
                    app.setLeJoueurAActiveLesEffetsSonores(false);
                    editeurDesPreferencesDuJoueur.putBoolean("EffetsSonores", false);
                    editeurDesPreferencesDuJoueur.apply();
                }
            }
        });

        // On pose un écouteur sur le bouton de la musique
        CheckBox checkBoxMusique = (CheckBox) findViewById(R.id.checkBoxMusique);
        if (app.isLeJoueurAActiveLaMusique())
            checkBoxMusique.setChecked(true);
        checkBoxMusique.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    app.setLeJoueurAActiveLaMusique(true);
                    editeurDesPreferencesDuJoueur.putBoolean("Musique", true);
                    editeurDesPreferencesDuJoueur.apply();

                    // On charge puis on demarre la musique
                    MediaPlayer mediaPlayer = MediaPlayer.create(app, R.raw.musique);
                    mediaPlayer.setVolume(0.3f, 0.3f);
                    mediaPlayer.setLooping(true);
                    mediaPlayer.start();
                    app.setMediaPlayer(mediaPlayer);
                } else {
                    app.setLeJoueurAActiveLaMusique(false);
                    editeurDesPreferencesDuJoueur.putBoolean("Musique", false);
                    editeurDesPreferencesDuJoueur.apply();

                    // On arrete la musique
                    MediaPlayer mediaPlayer = app.getMediaPlayer();
                    mediaPlayer.stop();
                    app.setMediaPlayer(mediaPlayer);
                }
            }
        });

        // On pose un écouteur sur le bouton des vibrations
        CheckBox checkBoxVibrations = (CheckBox) findViewById(R.id.checkBoxVibrations);
        if (app.isLeJoueurAActiveLesVibrations())
            checkBoxVibrations.setChecked(true);
        checkBoxVibrations.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // On active les vibrations
                    app.setLeJoueurAActiveLesVibrations(true);
                    editeurDesPreferencesDuJoueur.putBoolean("Vibrations", true);
                    editeurDesPreferencesDuJoueur.apply();
                } else {
                    // On désactive les vibrations
                    app.setLeJoueurAActiveLesVibrations(false);
                    editeurDesPreferencesDuJoueur.putBoolean("Vibrations", false);
                    editeurDesPreferencesDuJoueur.apply();
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    public void onClickRetourAuJeu(View v){
        // On corrige un petit bug d'arrondi superieur dans le score de temps
        app.setTempsEcouleDepuisLeDebutDeLaPartie(app.getTempsEcouleDepuisLeDebutDeLaPartie()-1);

        Intent intent = new Intent(this, Jeu.class);
        Bundle b = new Bundle();
        b.putInt("niveau", app.getNiveauCourant());
        intent.putExtras(b);
        startActivity(intent);
        finish();
    }

}
