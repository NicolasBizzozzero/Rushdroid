package bizzo0_munro.rushdroid;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import bizzo0_munro.rushdroid.model.Model;
import bizzo0_munro.rushdroid.model.Piece;
import bizzo0_munro.rushdroid.model.WhereTheFuckIsThePuzzleException;
import bizzo0_munro.rushdroid.pile_de_coups.GrossePileDeCoups;
import bizzo0_munro.rushdroid.pile_de_coups.PileDeCoups;

public class Jeu extends MonAppCompatActivity {
    private TextView affichageTemps;
    private TextView affichageCoups;
    private Handler handlerTempsEcouleDepuisLeDebutDeLaPartie;
    private Runnable tacheHandlerTempsEcouleDepuisLeDebutDeLaPartie;
    private Handler handlerCoupsEffectues;
    private Runnable tacheHandlerCoupsEffectues;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Chargement de elements du jeu si aucune partie n'est en cours
        if (!app.isUnePartieEstEnCours()){

            /* On recupère la clef "niveau" obtenue en ayant appuyé sur un des boutons
               de selection des niveaux.*/
            Bundle b = getIntent().getExtras();
            int niveau = b.getInt("niveau");
            app.setNiveauCourant(niveau);

            // On charge le model du jeu
            try {
                app.setModelDuJeu(new Model(niveau, app));
            } catch (ParserConfigurationException | SAXException | WhereTheFuckIsThePuzzleException | IOException e) {
                e.printStackTrace();
            }

            // Stockage de elements du jeu dans la classe principale
            app.setNombreDeColonnesDeLaGrilleDuJeu(app.getModelDuJeu().getGrille().getNbColonnes());
            app.setNombreDeLignesDeLaGrilleDuJeu(app.getModelDuJeu().getGrille().getNbLignes());
            app.setNiveauCourant(niveau);
            app.setNombreDeCoupsEffectues(0);
            app.setListeDePiecesCourantes(app.getModelDuJeu().getListePieces());
            app.setUnePartieEstEnCours(true);
            app.setTempsEcouleDepuisLeDebutDeLaPartie(0);
            app.setListeDeCoups(new GrossePileDeCoups());
        }

        setContentView(R.layout.activity_jeu);

        // On change le background de certains Layout si le theme est DARK
        if (app.getTHEME() == 0)
            actualiserLinearLayoutsSiThemeDark();

        ////// Affichage des elements sauvegardés dans la classe principale
        //// Affichage de l'HUD
        // Affichage du niveau courant
        TextView tvNiveau = (TextView) findViewById(R.id.texteNumeroNiveauCourant);
        tvNiveau.setText(String.format(" %d", app.getNiveauCourant()));
        // Affichage du monde courant
        TextView tvMonde = (TextView) findViewById(R.id.texteNumeroMondeCourant);
        tvMonde.setText(String.format(" %s", app.getModelDuJeu().getMonde()));
        // Affichage du record de coups
        TextView tvRecordCoups = (TextView) findViewById(R.id.texteRecordCoups);
        if (app.getModelDuJeu().getRecordCoups().equals("-1"))
            tvRecordCoups.setText(" ∞");
        else
            tvRecordCoups.setText(String.format(" %s", app.getModelDuJeu().getRecordCoups()));
        // Affichage du record de temps
        TextView tvRecordTemps = (TextView) findViewById(R.id.texteRecordTemps);
        if (app.getModelDuJeu().getRecordTemps().equals("-1"))
            tvRecordTemps.setText(" ∞");
        else
            tvRecordTemps.setText(String.format(" %s", app.getModelDuJeu().getRecordTemps()));
        // Affichage du timer
        affichageTemps = (TextView) findViewById(R.id.texteTempsEcoule);
        handlerTempsEcouleDepuisLeDebutDeLaPartie = new Handler();
        tacheHandlerTempsEcouleDepuisLeDebutDeLaPartie = new Runnable() {
            @Override
            public void run() {
                if (app.isUnePartieEstEnCours()) {
                    affichageTemps.setText(String.format(" %d", app.getTempsEcouleDepuisLeDebutDeLaPartie()));
                    app.setTempsEcouleDepuisLeDebutDeLaPartie(app.getTempsEcouleDepuisLeDebutDeLaPartie() + 1);
                } else {
                    handlerTempsEcouleDepuisLeDebutDeLaPartie.removeCallbacks(tacheHandlerTempsEcouleDepuisLeDebutDeLaPartie);
                }
                handlerTempsEcouleDepuisLeDebutDeLaPartie.postDelayed(tacheHandlerTempsEcouleDepuisLeDebutDeLaPartie, 1000);
            }
        };
        // Affichage du nombre de coups effectués
        affichageCoups = (TextView) findViewById(R.id.texteNombreDeCoupsEffectues);
        handlerCoupsEffectues = new Handler();
        tacheHandlerCoupsEffectues = new Runnable(){
            @Override
            public void run() {
                if(app.isUnePartieEstEnCours()){
                    affichageCoups.setText(String.format(" %d", app.getNombreDeCoupsEffectues()));
                }else{
                    handlerCoupsEffectues.removeCallbacks(tacheHandlerCoupsEffectues);
                }
                handlerCoupsEffectues.postDelayed(tacheHandlerCoupsEffectues, 100);
            }
        };

        // Reglage de la taille de la grille pour qu'elle soit carrée
        SurfaceView surfaceDuCanvas = (SurfaceView) findViewById(R.id.grilleDuJeu);
        surfaceDuCanvas.getHolder().setFixedSize(app.getLargeurDeLEcran(), app.getLargeurDeLEcran());

        // Cas où une fenetre de demande de sortie de jeu etait affichée
        if (app.isUneFenetreDeMessageEstAffichee())
            affichageMessageSortieDeJeu();
        else if (app.isUneFenetreDeMessagePourResetEstAffichee())
            onClickRestart(null);

        // Lancement des Handlers
        tacheHandlerCoupsEffectues.run();
        tacheHandlerTempsEcouleDepuisLeDebutDeLaPartie.run();
    }


    @Override
    protected void onDestroy(){
        /* Ce test permet d'assurer que le contenu de la partie est sauvegardé après une simple
           rotation de l'ecran. */
        if (!(app.isUnePartieEstEnCours())) {
            // On réaffecte toutes les valeurs par défaut des variables de la classe principale
            app.setNombreDeColonnesDeLaGrilleDuJeu(null);
            app.setNombreDeLignesDeLaGrilleDuJeu(null);
            app.setNiveauCourant(null);
            app.setListeDePiecesCourantes(new ArrayList<Piece>());
            app.setNombreDeCoupsEffectues(0);
            app.setTempsEcouleDepuisLeDebutDeLaPartie(0);
            app.setListeDeCoups(null);
            app.setListeDeCoupsEnCoursDEnregistrement(null);
        }
        handlerTempsEcouleDepuisLeDebutDeLaPartie.removeCallbacks(tacheHandlerTempsEcouleDepuisLeDebutDeLaPartie);
        handlerCoupsEffectues.removeCallbacks(tacheHandlerCoupsEffectues);

        // Puis on détruit l'activité
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            affichageMessageSortieDeJeu();
        }
        return super.onKeyDown(keyCode, event);
    }

    public void lancementFinDuJeu(){
        Intent intent = new Intent(this, EcranDeVictoire.class);
        /* On fait passer à l'ecran de victoire le nombre de coups effectués, le temps mit pour
           finir le niveau ainsi que le numero du niveau terminé. */
        Bundle b = new Bundle();
        b.putInt("tempsEcoule", app.getOnArreteLeChrono());
        b.putInt("nombreDeCoups", app.getNombreDeCoupsEffectues());
        b.putInt("niveau", app.getNiveauCourant());
        intent.putExtras(b);

        app.setOnArreteLeChrono(null);
        app.setUnePartieEstEnCours(false);
        this.startActivity(intent);
        this.finish();
    }

    public void affichageMessageSortieDeJeu(){
        final Jeu jeu = this;
        app.setUneFenetreDeMessageEstAffichee(true);

        new MonBuilderDeFenetreDeDialogue(this)
                .setMessage(R.string.dialoguejeutexte)
                .setNegativeButton(R.string.oui, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        // Si l'utilisateur clique sur oui, on quitte la partie
                        int tempsEcoule = app.getTempsEcouleDepuisLeDebutDeLaPartie();
                        app.setUneFenetreDeMessageEstAffichee(false);
                        app.setUnePartieEstEnCours(false);
                        try {
                            incrementerStatistiquesDansXML(app.getNombreDeCoupsEffectues(), tempsEcoule);
                        } catch (TransformerException | ParserConfigurationException | SAXException | IOException e) {
                            e.printStackTrace();
                        }
                        Intent intent = new Intent(jeu, SelectionDesNiveaux.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .setPositiveButton(R.string.non, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                // Si l'utilisateur clique sur non, on retourne au jeu
                                app.setUneFenetreDeMessageEstAffichee(false);
                            }
                        }
                ).show();
    }

    private void incrementerStatistiquesDansXML(int nombreDeCoups, int tempsEcoule) throws TransformerException, ParserConfigurationException, IOException, SAXException {
        // Chargement des records dans le fichier des statistiquesrecordCoups
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        FileInputStream cheminDuFichier = app.getBaseContext().openFileInput("statistics.xml");
        Document doc = db.parse(cheminDuFichier);
        // Obtention de l'element racine du document
        Element racine = doc.getDocumentElement();

        // On modifie maintenant la partie statistiques du fichier XML
        // Obtention de l'element statistiques
        NodeList listeElementStatistiques = racine.getElementsByTagName("statistiques");
        Node elementStatistiques = listeElementStatistiques.item(0);
        // Obtention des noeuds progression et statistiques diverses
        NodeList listeDesNoeudsDeLElementStatistiques = elementStatistiques.getChildNodes();
        Node noeudStatistiquesDiverses = null;
        for (int i=0; i < listeDesNoeudsDeLElementStatistiques.getLength(); i++) {
            Node itemElement = listeDesNoeudsDeLElementStatistiques.item(i);
            if (itemElement.getNodeType() != Node.ELEMENT_NODE)
                continue;

            if (itemElement.getNodeName().equals("statistiquesDiverses")){
                noeudStatistiquesDiverses = itemElement;
                break;
            }
        }

        // On modifie le contenu du noeud Statistiques Diverses
        NodeList listeNoeudsDeStatistiquesDiverses = noeudStatistiquesDiverses.getChildNodes();
        Node noeudNombreDeCoupsTotal = null;
        Node noeudTempsPasseAJouer = null;
        for (int i=0; i < listeNoeudsDeStatistiquesDiverses.getLength(); i++) {
            Node itemElement = listeNoeudsDeStatistiquesDiverses.item(i);
            if (itemElement.getNodeType() != Node.ELEMENT_NODE)
                continue;

            NamedNodeMap attribusItem = itemElement.getAttributes();
            String nomDeLItem = ((Attr) attribusItem.getNamedItem("name")).getValue();

            if (nomDeLItem.equals("nombreDeCoups")) {
                noeudNombreDeCoupsTotal = itemElement;
                continue;
            }

            if (nomDeLItem.equals("tempsPasseAJouer")) {
                noeudTempsPasseAJouer = itemElement;
                break;
            }
        }

        // On recupere les attributs du noeud de l'element du nombre de coups
        NamedNodeMap listeAttributsDuNoeudElementNombreDeCoups = noeudNombreDeCoupsTotal.getAttributes();
        Node attributValeurNombreDeCoups = listeAttributsDuNoeudElementNombreDeCoups.getNamedItem("valeur");

        // On modifie les attributs du noeud de l'element du nombre de coups
        attributValeurNombreDeCoups.setTextContent("" + (Integer.parseInt(attributValeurNombreDeCoups.getNodeValue()) + nombreDeCoups));

        // On recupere les attributs du noeud de l'element du temps passé à jouer
        NamedNodeMap listeAttributsDuNoeudElementTempsPasseAJouer = noeudTempsPasseAJouer.getAttributes();
        Node attributValeurTempsPasseAJouer = listeAttributsDuNoeudElementTempsPasseAJouer.getNamedItem("valeur");

        // On modifie les attributs du noeud de l'element du temps passé à jouer
        attributValeurTempsPasseAJouer.setTextContent(app.formaterEnTempsPasseAJouer(attributValeurTempsPasseAJouer.getNodeValue(), tempsEcoule));

        // On écrit le contenu modifié dans le fichier XML
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(app.getBaseContext().getFileStreamPath("statistics.xml"));
        transformer.transform(source, result);
    }

    private void actualiserLinearLayoutsSiThemeDark(){
        if (app.getOrientationEcran() == Configuration.ORIENTATION_PORTRAIT) {
            LinearLayout ll = (LinearLayout) findViewById(R.id.llab1);
            ll.setBackgroundResource(R.drawable.background_sirg_ab);
            ll = (LinearLayout) findViewById(R.id.llab2);
            ll.setBackgroundResource(R.drawable.background_sirg_ab);
            ll = (LinearLayout) findViewById(R.id.llsbb1);
            ll.setBackgroundResource(R.drawable.background_sirg_sbb);
        } else{
            LinearLayout ll = (LinearLayout) findViewById(R.id.llab1);
            ll.setBackgroundResource(R.drawable.background_sirg_ab);
            ll = (LinearLayout) findViewById(R.id.llsbb1);
            ll.setBackgroundResource(R.drawable.background_sirg_sbb);
            ll = (LinearLayout) findViewById(R.id.llsbb2);
            ll.setBackgroundResource(R.drawable.background_sirg_sbb);
        }
    }

    @Override
    protected void onStop(){
        // On arrête le chronomètre
        handlerTempsEcouleDepuisLeDebutDeLaPartie.removeCallbacks(tacheHandlerTempsEcouleDepuisLeDebutDeLaPartie);

        super.onStop();
    }

    @Override
    protected void onRestart(){
        // On redemarre le chronometre
        affichageTemps = (TextView) findViewById(R.id.texteTempsEcoule);
        handlerTempsEcouleDepuisLeDebutDeLaPartie = new Handler();
        tacheHandlerTempsEcouleDepuisLeDebutDeLaPartie = new Runnable() {
            @Override
            public void run() {
                if (app.isUnePartieEstEnCours()) {
                    affichageTemps.setText(String.format(" %d", app.getTempsEcouleDepuisLeDebutDeLaPartie()));
                    app.setTempsEcouleDepuisLeDebutDeLaPartie(app.getTempsEcouleDepuisLeDebutDeLaPartie() + 1);
                } else {
                    handlerTempsEcouleDepuisLeDebutDeLaPartie.removeCallbacks(tacheHandlerTempsEcouleDepuisLeDebutDeLaPartie);
                }
                handlerTempsEcouleDepuisLeDebutDeLaPartie.postDelayed(tacheHandlerTempsEcouleDepuisLeDebutDeLaPartie, 1000);
            }
        };
        tacheHandlerTempsEcouleDepuisLeDebutDeLaPartie.run();

        super.onRestart();
    }

    public void onClickCoupPrecedent(View v){
        PileDeCoups coupPrecedent = app.getListeDeCoups().retirerDernierCoupJoue();
        if (coupPrecedent == null)
            return;

        int id = coupPrecedent.getId();
        String pile = coupPrecedent.getPile();

        for (char c : pile.toCharArray()) {
            if (c == 'f')
                app.getModelDuJeu().moveForward(id);
            else if (c == 'b')
                app.getModelDuJeu().moveBackward(id);
        }

        app.setNombreDeCoupsEffectues(app.getNombreDeCoupsEffectues()-1);

        GraphismesDuJeu canvas = (GraphismesDuJeu) findViewById(R.id.grilleDuJeu);
        canvas.tryDrawing();
    }

    public void onClickPause(View v){
        Intent intent = new Intent(this, Pause.class);
        startActivity(intent);
        this.finish();
    }

    public void onClickRestart(View v){
        final Jeu jeu = this;
        app.setUneFenetreDeMessagePourResetEstAffichee(true);

        new MonBuilderDeFenetreDeDialogue(this)
                .setMessage(R.string.dialoguejeurecommencerniveau)
                .setNegativeButton(R.string.oui, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        // Si l'utilisateur clique sur oui, on demarre une partie
                        // On incrémente les satistiques
                        int tempsEcoule = app.getTempsEcouleDepuisLeDebutDeLaPartie();
                        app.setUneFenetreDeMessagePourResetEstAffichee(false);
                        app.setUnePartieEstEnCours(false);
                        try {
                            incrementerStatistiquesDansXML(app.getNombreDeCoupsEffectues(), tempsEcoule);
                        } catch (TransformerException | ParserConfigurationException | SAXException | IOException e) {
                            e.printStackTrace();
                        }

                        // Puis on redemarre la partie
                        Intent intent = new Intent(jeu, Jeu.class);
                        // On reinitialise les variables du jeu à leur valeur initiale
                        app.setOnArreteLeChrono(null);
                        app.setUnePartieEstEnCours(false);
                        app.setListeDePiecesCourantes(new ArrayList<Piece>());
                        app.setNombreDeCoupsEffectues(0);
                        app.setTempsEcouleDepuisLeDebutDeLaPartie(0);
                        app.setListeDeCoups(null);
                        app.setListeDeCoupsEnCoursDEnregistrement(null);
                        /* AJout de la clef "niveau" qui se trouvera dans le contexte de
                           l'activité Jeu, afin de savoir quel niveau le joueur a selectionné.*/
                        Bundle b = new Bundle();
                        b.putInt("niveau", app.getNiveauCourant());
                        intent.putExtras(b);
                        startActivity(intent);
                        jeu.finish();
                    }
                })
                .setPositiveButton(R.string.non, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                // Si l'utilisateur clique sur non, on retourne au jeu
                                app.setUneFenetreDeMessagePourResetEstAffichee(false);
                            }
                        }
                ).show();
    }
}
