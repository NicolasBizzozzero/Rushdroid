package bizzo0_munro.rushdroid;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

import java.io.FileOutputStream;
import java.io.IOException;

public class Options extends MonAppCompatActivity {
    SharedPreferences preferencesDuJoueur;
    SharedPreferences.Editor editeurDesPreferencesDuJoueur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        // Si l'utilisateur tourne l'ecran alors qu'une fenetre etait affichée
        if (app.isUneFenetreDeMessageEstAffichee())
            affichageMessageReinitialisationFichierXML();

        // On change le background de certains Layout si le theme est DARK
        if (app.getTHEME() == 0)
            actualiserLinearLayoutsSiThemeDark();

        // Chargement des preferences du joueur et de son éditeur
        preferencesDuJoueur = this.getSharedPreferences("Options", Context.MODE_PRIVATE);
        editeurDesPreferencesDuJoueur = preferencesDuJoueur.edit();

        // On pose un écouteur sur le switch des thèmes
        Switch boutonSwitchThemes = (Switch) findViewById(R.id.switchTheme);
        if (app.getTHEME() == 0)
            boutonSwitchThemes.setChecked(true);
        boutonSwitchThemes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // On donne à l'appplication le thème DARK
                    app.setTHEME(0);
                    editeurDesPreferencesDuJoueur.putInt("Theme", 0);
                    editeurDesPreferencesDuJoueur.apply();

                    // Puis on relance l'activité
                    Intent intent = new Intent(getBaseContext(), Options.class);
                    finish();
                    startActivity(intent);
                }
                else {
                    // On donne à l'application le thème LIGHT
                    app.setTHEME(1);
                    editeurDesPreferencesDuJoueur.putInt("Theme", 1);
                    editeurDesPreferencesDuJoueur.apply();

                    // Puis on relance l'activité
                    Intent intent = new Intent(getBaseContext(), Options.class);
                    finish();
                    startActivity(intent);
                }
            }
        });

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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(this, MenuPrincipal.class);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    public void onClickReinitialiserLeFichierXML(View v){
        affichageMessageReinitialisationFichierXML();
    }

    public void onClickRetourAuMenuPrincipal(View v){
        Intent intent = new Intent(this, MenuPrincipal.class);
        startActivity(intent);
        finish();
    }

    public void onClickGoToRemerciements(View v){
        Intent intent = new Intent(this, Remerciements.class);
        startActivity(intent);
        finish();
    }

    public void onClickGoToSolveur(View v){
        Intent intent = new Intent(this, SolveurDePuzzles.class);
        startActivity(intent);
        finish();
    }

    private void reinitialiserLeFichierXML(){
        try {
            this.getBaseContext().deleteFile("statistics.xml");
            FileOutputStream fileOutputStream = this.getBaseContext().openFileOutput("statistics.xml", Context.MODE_APPEND);
            app.creationDuFichierDeStatistiques(fileOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void affichageMessageReinitialisationFichierXML(){
        app.setUneFenetreDeMessageEstAffichee(true);

        MonBuilderDeFenetreDeDialogue messageAlerte = new MonBuilderDeFenetreDeDialogue(this);
        messageAlerte.setMessage(R.string.dialogueoptionresettexte);
        messageAlerte.setPositiveButton(R.string.non, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                // Si l'utilisateur clique sur non, on retourne au menu
                app.setUneFenetreDeMessageEstAffichee(false);
            }
        });
        messageAlerte.setNegativeButton(R.string.oui, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                // Si l'utilisateur clique sur oui, on reinitialise le fichier
                app.setUneFenetreDeMessageEstAffichee(false);
                reinitialiserLeFichierXML();
            }
        });
        messageAlerte.show();
    }

    private void actualiserLinearLayoutsSiThemeDark(){
        LinearLayout ll = (LinearLayout) findViewById(R.id.llab1);
        ll.setBackgroundResource(R.drawable.background_sirg_ab);
        ll = (LinearLayout) findViewById(R.id.llab2);
        ll.setBackgroundResource(R.drawable.background_sirg_ab);
        ll = (LinearLayout) findViewById(R.id.llab3);
        ll.setBackgroundResource(R.drawable.background_sirg_ab);
        ll = (LinearLayout) findViewById(R.id.llab4);
        ll.setBackgroundResource(R.drawable.background_sirg_ab);
        ll = (LinearLayout) findViewById(R.id.llab5);
        ll.setBackgroundResource(R.drawable.background_sirg_ab);
        ll = (LinearLayout) findViewById(R.id.llab6);
        ll.setBackgroundResource(R.drawable.background_sirg_ab);
    }
}