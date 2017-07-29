package bizzo0_munro.rushdroid;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.util.DisplayMetrics;
import android.util.Xml;

import org.xmlpull.v1.XmlSerializer;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import bizzo0_munro.rushdroid.model.Model;
import bizzo0_munro.rushdroid.model.Piece;
import bizzo0_munro.rushdroid.pile_de_coups.GrossePileDeCoups;
import bizzo0_munro.rushdroid.pile_de_coups.PileDeCoups;

public class ClassePrincipale extends Application {
    // Gestion de l'affichage
    private int orientationEcran; //Masque Configuration.ORIENTATION_PORTRAIT ou Configuration.ORIENTATION_LANDSCAPE
    private int largeurDeLEcran;
    private int THEME;            // Masque, 0 == DARK, 1 == LIGHT
    private boolean uneFenetreDeMessageEstAffichee;
    private boolean uneFenetreDeMessagePourResetEstAffichee;

    // Gestion du jeu et de son modèle
    private Integer nombreDeColonnesDeLaGrilleDuJeu;
    private Integer nombreDeLignesDeLaGrilleDuJeu;
    private Integer niveauCourant;
    private Integer nombreDeCoupsEffectues;
    private ArrayList<Piece> listeDePiecesCourantes;
    private boolean unePartieEstEnCours;
    private Model modelDuJeu;
    private Integer tempsEcouleDepuisLeDebutDeLaPartie;
    private Integer onArreteLeChrono;

    // Variables utilisées pour le stockage des coups
    private GrossePileDeCoups listeDeCoups;
    private PileDeCoups listeDeCoupsEnCoursDEnregistrement;

    // Musique, effets sonores et vibrations
    private boolean leJoueurAActiveLesEffetsSonores;
    private boolean leJoueurAActiveLaMusique;
    private boolean leJoueurAActiveLesVibrations;
    private MediaPlayer mediaPlayer;
    private boolean leSonDeVictoireAEteJoue;
    private boolean laVibrationDeVictoireAEteJouee;

    @Override
    public void onCreate() {
        super.onCreate();

        // Obtention et attribution de la longueur/largeur de l'écran de l'appareil
        DisplayMetrics dimensions = getResources().getDisplayMetrics();
        if (dimensions.widthPixels >= dimensions.heightPixels) {
            largeurDeLEcran = dimensions.heightPixels;
        } else {
            largeurDeLEcran = dimensions.widthPixels;
        }

        // Recuperation des préfèrences du joueur
        SharedPreferences preferencesDuJoueur = getSharedPreferences("Options", Context.MODE_PRIVATE);

        // Récuperation du thème du joueur
        THEME = preferencesDuJoueur.getInt("Theme", 1);

        // Récuperation du boolean des effets sonores du joueur
        leJoueurAActiveLesEffetsSonores = preferencesDuJoueur.getBoolean("EffetsSonores", false);

        // Récuperation du boolean de la musique
        leJoueurAActiveLaMusique = preferencesDuJoueur.getBoolean("Musique", false);

        // Récuperation du boolean des vibratios
        leJoueurAActiveLesVibrations = preferencesDuJoueur.getBoolean("Vibrations", false);

        // Assignation des valeurs par défaut des variables du jeu et de son modèle
        nombreDeColonnesDeLaGrilleDuJeu = null;
        nombreDeLignesDeLaGrilleDuJeu = null;
        niveauCourant = null;
        nombreDeCoupsEffectues = null;
        listeDePiecesCourantes = new ArrayList<>();
        unePartieEstEnCours = false;
        modelDuJeu = null;
        tempsEcouleDepuisLeDebutDeLaPartie = null;
        onArreteLeChrono = null;
        listeDeCoups = null;
        listeDeCoupsEnCoursDEnregistrement = null;

        // Assignation des valeurs par défaut des variables de l'affichage
        uneFenetreDeMessageEstAffichee = false;
        uneFenetreDeMessagePourResetEstAffichee = false;

        // Assignation des valeurs par défauts des variables du son et des vibrations
        leSonDeVictoireAEteJoue = false;
        laVibrationDeVictoireAEteJouee = false;

        // On créé le fichier XML des statistiques en mode ecriture s'il n'existe pas
        try {
            FileOutputStream fileOutputStream = this.getBaseContext().openFileOutput("statistics.xml", Context.MODE_APPEND);

            // On ouvre le fichier en lecture
            FileInputStream fileInputStream = this.getBaseContext().openFileInput("statistics.xml");

            /* Si aucun caractere n'est dedans, alors il vient d'etre créé. C'est donc la première
               fois que le joueur lance l'application. On va donc fabriquer le fichier XML des
               statistiques. */
            if ((fileInputStream.available()) == 0) {
                creationDuFichierDeStatistiques(fileOutputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Lancement de la musique si le joueur l'a activée
        if (leJoueurAActiveLaMusique){
            mediaPlayer = MediaPlayer.create(this, R.raw.musique);
            mediaPlayer.setVolume(0.3f, 0.3f);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        }
    }

    public void creationDuFichierDeStatistiques(FileOutputStream fileOutputStream) throws IOException {
        XmlSerializer serializer = Xml.newSerializer();
        serializer.setOutput(fileOutputStream, "UTF-8");
        serializer.startDocument(null, true);
        serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);

        // Début du document
        serializer.startTag(null, "elementRacine");

            // Création de la partie statistiques des niveaux
            serializer.startTag(null, "informationsNiveaux");
                for (int i=0; i<=64; i++){
                    serializer.startTag(null, "niveau");
                        serializer.attribute(null, "name", "pb" + i);
                        serializer.attribute(null, "recordCoups", "-1");
                        serializer.attribute(null, "recordTemps", "-1");
                    serializer.endTag(null, "niveau");
                }
            serializer.endTag(null, "informationsNiveaux");

            // Création de la partie des autres statistiques
            serializer.startTag(null, "statistiques");
                serializer.startTag(null, "progression");
                for (int i=1; i<=8; i++){
                    serializer.startTag(null, "Monde");
                    serializer.attribute(null, "name", "" + i);
                    serializer.attribute(null, "niveauxTermines", "0");
                    serializer.attribute(null, "pourcentageNiveauxTermines", "0");
                    serializer.attribute(null, "estEntierementTermine", "false");
                    serializer.attribute(null, "totalRecordsTemps", "0");
                    serializer.attribute(null, "pireRecordTemps", "-1");
                    serializer.attribute(null, "pireRecordTempsNiveau", "0");
                    serializer.attribute(null, "totalRecordsCoups", "0");
                    serializer.attribute(null, "pireRecordCoups", "-1");
                    serializer.attribute(null, "pireRecordCoupsNiveau", "0");
                    serializer.endTag(null, "Monde");
                }
                    serializer.startTag(null, "Jeu");
                    serializer.attribute(null, "niveauxTermines", "0");
                    serializer.attribute(null, "pourcentageNiveauxTermines", "0");
                    serializer.attribute(null, "estEntierementTermine", "false");
                    serializer.attribute(null, "totalRecordsTemps", "0");
                    serializer.attribute(null, "pireRecordTemps", "-1");
                    serializer.attribute(null, "pireRecordTempsNiveau", "0");
                    serializer.attribute(null, "totalRecordsCoups", "0");
                    serializer.attribute(null, "pireRecordCoups", "-1");
                    serializer.attribute(null, "pireRecordCoupsNiveau", "0");
                    serializer.endTag(null, "Jeu");
                serializer.endTag(null, "progression");

                serializer.startTag(null, "statistiquesDiverses");
                    serializer.startTag(null, "element");
                    serializer.attribute(null, "name", "nombreDeCoups");
                    serializer.attribute(null, "valeur", "0");
                    serializer.endTag(null, "element");

                    serializer.startTag(null, "element");
                    serializer.attribute(null, "name", "tempsPasseAJouer");
                    serializer.attribute(null, "valeur", "0-0-0-0");
                    serializer.endTag(null, "element");
                serializer.endTag(null, "statistiquesDiverses");

            serializer.endTag(null, "statistiques");
        serializer.endTag(null, "elementRacine");
        serializer.endDocument();
        serializer.flush();
        fileOutputStream.close();
    }

    public String formaterEnTempsPasseAJouer(String ancienneValeur, int tempsEcoule){
        // Recuperations des anciennes valeurs sous variables numériques
        String[] anciennesValeursSeparees = ancienneValeur.split("-", 4);
        Integer jours = Integer.parseInt(anciennesValeursSeparees[0]);
        Integer heures = Integer.parseInt(anciennesValeursSeparees[1]);
        Integer minutes = Integer.parseInt(anciennesValeursSeparees[2]);
        Integer secondes = Integer.parseInt(anciennesValeursSeparees[3]);

        // Ajout du temps du joueur
        secondes += tempsEcoule;

        // On affecte les bonnnes valeurs aux bonnes variables
        while (secondes >= 60){
            secondes -= 60;
            minutes += 1;
        }
        while (minutes >= 60){
            minutes -= 60;
            heures += 1;
        }
        while (heures >= 24){
            heures -= 24;
            jours += 1;
        }

        // On reformate la String correctement puis on la retourne
        return String.format("%d-%d-%d-%d", jours, heures, minutes, secondes);
    }

    public int getLargeurDeLEcran() {
        return largeurDeLEcran;
    }

    public Integer getNombreDeLignesDeLaGrilleDuJeu() {
        return nombreDeLignesDeLaGrilleDuJeu;
    }

    public void setNombreDeLignesDeLaGrilleDuJeu(Integer nombreDeLignesDeLaGrilleDuJeu) {
        this.nombreDeLignesDeLaGrilleDuJeu = nombreDeLignesDeLaGrilleDuJeu;
    }

    public Integer getNombreDeColonnesDeLaGrilleDuJeu() {
        return nombreDeColonnesDeLaGrilleDuJeu;
    }

    public void setNombreDeColonnesDeLaGrilleDuJeu(Integer nombreDeColonnesDeLaGrilleDuJeu) {
        this.nombreDeColonnesDeLaGrilleDuJeu = nombreDeColonnesDeLaGrilleDuJeu;
    }

    public Integer getNiveauCourant() {
        return niveauCourant;
    }

    public void setNiveauCourant(Integer niveauCourant) {
        this.niveauCourant = niveauCourant;
    }

    public Integer getNombreDeCoupsEffectues() {
        return nombreDeCoupsEffectues;
    }

    public void setNombreDeCoupsEffectues(Integer nombreDeCoupsEffectues) {
        this.nombreDeCoupsEffectues = nombreDeCoupsEffectues;
    }

    public ArrayList<Piece> getListeDePiecesCourantes() {
        return listeDePiecesCourantes;
    }

    public void setListeDePiecesCourantes(ArrayList<Piece> listeDePiecesCourantes) {
        this.listeDePiecesCourantes = listeDePiecesCourantes;
    }

    public boolean isUnePartieEstEnCours() {
        return unePartieEstEnCours;
    }

    public void setUnePartieEstEnCours(boolean unePartieEstEnCours) {
        this.unePartieEstEnCours = unePartieEstEnCours;
    }

    public int getOrientationEcran() {
        return orientationEcran;
    }

    public void setOrientationEcran(int orientationEcran) {
        this.orientationEcran = orientationEcran;
    }

    public Model getModelDuJeu() {
        return modelDuJeu;
    }

    public void setModelDuJeu(Model modelDuJeu) {
        this.modelDuJeu = modelDuJeu;
    }

    public boolean isUneFenetreDeMessageEstAffichee() {
        return uneFenetreDeMessageEstAffichee;
    }

    public void setUneFenetreDeMessageEstAffichee(boolean uneFenetreDeMessageEstAffichee) {
        this.uneFenetreDeMessageEstAffichee = uneFenetreDeMessageEstAffichee;
    }

    public Integer getTempsEcouleDepuisLeDebutDeLaPartie() {
        return tempsEcouleDepuisLeDebutDeLaPartie;
    }

    public void setTempsEcouleDepuisLeDebutDeLaPartie(Integer tempsEcouleDepuisLeDebutDeLaPartie) {
        this.tempsEcouleDepuisLeDebutDeLaPartie = tempsEcouleDepuisLeDebutDeLaPartie;
    }

    public Integer getOnArreteLeChrono() {
        return onArreteLeChrono;
    }

    public void setOnArreteLeChrono(Integer onArreteLeChrono) {
        this.onArreteLeChrono = onArreteLeChrono;
    }

    public int getTHEME() {
        return THEME;
    }

    public void setTHEME(int THEME) {
        this.THEME = THEME;
    }

    public boolean isLeJoueurAActiveLesEffetsSonores() {
        return leJoueurAActiveLesEffetsSonores;
    }

    public void setLeJoueurAActiveLesEffetsSonores(boolean leJoueurAActiveLesEffetsSonores) {
        this.leJoueurAActiveLesEffetsSonores = leJoueurAActiveLesEffetsSonores;
    }

    public boolean isLeJoueurAActiveLaMusique() {
        return leJoueurAActiveLaMusique;
    }

    public void setLeJoueurAActiveLaMusique(boolean leJoueurAActiveLaMusique) {
        this.leJoueurAActiveLaMusique = leJoueurAActiveLaMusique;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }

    public boolean isLeSonDeVictoireAEteJoue() {
        return leSonDeVictoireAEteJoue;
    }

    public void setLeSonDeVictoireAEteJoue(boolean leSonDeVictoireAEteJoue) {
        this.leSonDeVictoireAEteJoue = leSonDeVictoireAEteJoue;
    }

    public boolean isLeJoueurAActiveLesVibrations() {
        return leJoueurAActiveLesVibrations;
    }

    public void setLeJoueurAActiveLesVibrations(boolean leJouerAActiveLesVibrations) {
        this.leJoueurAActiveLesVibrations = leJouerAActiveLesVibrations;
    }

    public boolean isLaVibrationDeVictoireAEteJouee() {
        return laVibrationDeVictoireAEteJouee;
    }

    public void setLaVibrationDeVictoireAEteJouee(boolean laVibrationDeVictoireAEteJouee) {
        this.laVibrationDeVictoireAEteJouee = laVibrationDeVictoireAEteJouee;
    }

    public GrossePileDeCoups getListeDeCoups() {
        return listeDeCoups;
    }

    public void setListeDeCoups(GrossePileDeCoups listeDeCoups) {
        this.listeDeCoups = listeDeCoups;
    }

    public PileDeCoups getListeDeCoupsEnCoursDEnregistrement() {
        return listeDeCoupsEnCoursDEnregistrement;
    }

    public void setListeDeCoupsEnCoursDEnregistrement(PileDeCoups listeDeCoupsEnCoursDEnregistrement) {
        this.listeDeCoupsEnCoursDEnregistrement = listeDeCoupsEnCoursDEnregistrement;
    }

    public boolean isUneFenetreDeMessagePourResetEstAffichee() {
        return uneFenetreDeMessagePourResetEstAffichee;
    }

    public void setUneFenetreDeMessagePourResetEstAffichee(boolean uneFenetreDeMessagePourResetEstAffichee) {
        this.uneFenetreDeMessagePourResetEstAffichee = uneFenetreDeMessagePourResetEstAffichee;
    }
}