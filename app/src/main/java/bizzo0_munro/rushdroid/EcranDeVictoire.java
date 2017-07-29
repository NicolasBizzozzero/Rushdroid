package bizzo0_munro.rushdroid;

import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class EcranDeVictoire extends MonAppCompatActivity {
    private int nombreDeCoups;
    private int tempsEcoule;
    private int niveau;
    private int numeroDuMonde;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecran_de_victoire);

        /* On récupère le nombre de coups effectués, le temps mis pour finir le niveau ainsi que le
        numéro du niveau depuis l'activité de jeu. */
        Bundle b = getIntent().getExtras();
        tempsEcoule = b.getInt("tempsEcoule");
        nombreDeCoups = b.getInt("nombreDeCoups");
        niveau = b.getInt("niveau");
        numeroDuMonde = niveauToMonde(niveau);

        // On set le contenu des textview en fonction des resultats du niveau
        TextView tvNiveau = (TextView) findViewById(R.id.texteVictoireNiveau);
        tvNiveau.setText(String.format(" %d", niveau));
        TextView tvCoups = (TextView) findViewById(R.id.texteVictoireCoups);
        tvCoups.setText(String.format(" %d", nombreDeCoups));
        TextView tvTemps = (TextView) findViewById(R.id.texteVictoireTemps);
        tvTemps.setText(String.format(" %d", tempsEcoule));

        // On actualise le XML des statistiques avec le score du joueur
        try {
            actualiserLeXMLDesStatistiques();
        } catch (IOException | ParserConfigurationException | SAXException | TransformerException ignored) {
        }

        // Si le joueur est au niveau final d'un monde, on masque le bouton "Niveau suivant"
        if ((niveau == 8) || (niveau == 16) || (niveau == 24) || (niveau == 32) || (niveau == 40) || (niveau == 48) || (niveau == 56) || (niveau == 64)){
            // On récupère le layout
            FrameLayout layoutNiveauSuivant = (FrameLayout) findViewById(R.id.layoutNiveauSuivant);

            // On rend le layout intouchable
            setIntouchable(layoutNiveauSuivant);
        }

        // Le joueur est content d'avoir gagné ! On joue un joyeux son de victoire
        if (app.isLeJoueurAActiveLesEffetsSonores()){
            /* Ce boolean evite de jouer plusieurs fois le son de victoire (quand le joueur
               retourne l'écran par exemple. */
            if (!(app.isLeSonDeVictoireAEteJoue())) {
                SoundPool soundPool;

                if (android.os.Build.VERSION.SDK_INT >= 21) {
                    SoundPool.Builder soundPoolBuilder = new SoundPool.Builder().setMaxStreams(1);
                    soundPoolBuilder.setAudioAttributes(new AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .setLegacyStreamType(AudioManager.STREAM_MUSIC)
                            .build());
                    soundPool = soundPoolBuilder.build();
                } else
                    soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
                soundPool.load(app, R.raw.effet_sonore_victoire, 0);
                // Lecture du son de victoire
                soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                    @Override
                    public void onLoadComplete(SoundPool soundPool, int identifiantSon,
                                               int status) {
                        soundPool.play(identifiantSon, ((float) 0.05), ((float) 0.05), 1, 0, 1f);
                    }
                });
                app.setLeSonDeVictoireAEteJoue(true);
            }
        }

        // On lance aussi une vibration !
        if (app.isLeJoueurAActiveLesVibrations()){
            if (!(app.isLaVibrationDeVictoireAEteJouee())){
                Vibrator vibrator = (Vibrator) app.getSystemService(VIBRATOR_SERVICE);
                vibrator.vibrate(750);
                app.setLaVibrationDeVictoireAEteJouee(true);
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            app.setLeSonDeVictoireAEteJoue(false);
            app.setLaVibrationDeVictoireAEteJouee(false);
            Intent intent = new Intent(this, SelectionDesNiveaux.class);
            startActivity(intent);
            finish();
        }

        return super.onKeyDown(keyCode, event);
    }

    public void actualiserLeXMLDesStatistiques() throws ParserConfigurationException, IOException, SAXException, TransformerException {
        // Chargement des records dans le fichier des statistiquesrecordCoups
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        FileInputStream cheminDuFichier = app.getBaseContext().openFileInput("statistics.xml");
        Document doc = db.parse(cheminDuFichier);
        // Obtention de l'element racine du document
        Element racine = doc.getDocumentElement();
        // Obtention de l'element informationsNiveaux
        NodeList listeElementInformationsNiveaux = racine.getElementsByTagName("informationsNiveaux");
        Node elementInformationsNiveaux = listeElementInformationsNiveaux.item(0);
        // Obtention de l'element du niveau chargé
        NodeList listeElementNiveauCharge = elementInformationsNiveaux.getChildNodes();
        Node elementNiveauCharge = null;
        for (int i=0; i < listeElementNiveauCharge.getLength(); i++) {
            Node itemElement = listeElementNiveauCharge.item(i);
            if (itemElement.getNodeType() != Node.ELEMENT_NODE)
                continue;

            NamedNodeMap attribusItem = itemElement.getAttributes();
            String nomDeLItem = ((Attr) attribusItem.getNamedItem("name")).getValue();

            if (nomDeLItem.equals("pb" + niveau)) {
                // Obtention du puzzle passé en argument
                elementNiveauCharge = listeElementNiveauCharge.item(i);
                break;
            }
        }

        // On recupere les attributs des records du puzzle
        NamedNodeMap listeAttributs = elementNiveauCharge.getAttributes();
        Node attributRecordTemps = listeAttributs.getNamedItem("recordTemps");
        Node attributRecordCoups = listeAttributs.getNamedItem("recordCoups");

        // On modifie les attributs des records du puzzle si le joueur les a battu
        Integer ancienRecordTemps = Integer.parseInt(attributRecordTemps.getNodeValue());
        Integer ancienRecordCoups = Integer.parseInt(attributRecordCoups.getNodeValue());
        boolean leNiveauEstTerminePourLaPremiereFois;
        if (ancienRecordTemps == -1) {
            leNiveauEstTerminePourLaPremiereFois = true;
            attributRecordTemps.setTextContent(String.format("%d", tempsEcoule));
            attributRecordCoups.setTextContent(String.format("%d", nombreDeCoups));
        } else {
            leNiveauEstTerminePourLaPremiereFois = false;
            if (ancienRecordTemps > (Integer) tempsEcoule)
                attributRecordTemps.setTextContent(String.format("%d", tempsEcoule));
            if (ancienRecordCoups > (Integer) nombreDeCoups)
                attributRecordCoups.setTextContent(String.format("%d", nombreDeCoups));
        }

        // On modifie maintenant la partie statistiques du fichier XML
        // Obtention de l'element statistiques
        NodeList listeElementStatistiques = racine.getElementsByTagName("statistiques");
        Node elementStatistiques = listeElementStatistiques.item(0);
        // Obtention des noeuds progression et statistiques diverses
        NodeList listeDesNoeudsDeLElementStatistiques = elementStatistiques.getChildNodes();
        Node noeudProgression = null;
        Node noeudStatistiquesDiverses = null;
        for (int i=0; i < listeDesNoeudsDeLElementStatistiques.getLength(); i++) {
            Node itemElement = listeDesNoeudsDeLElementStatistiques.item(i);
            if (itemElement.getNodeType() != Node.ELEMENT_NODE)
                continue;

            if(itemElement.getNodeName().equals("progression")) {
                noeudProgression = itemElement;
                continue;
            }

            if(itemElement.getNodeName().equals("statistiquesDiverses")){
                noeudStatistiquesDiverses = itemElement;
                break;
            }
        }
        // Modification du noeud progression
        // On recherche le noeud Monde correspondant ainsi que le noeud Jeu
        NodeList listeDesNoeudsDuNoeudProgression = noeudProgression.getChildNodes();
        Node noeudDuMondeDuNiveauCorrespondant = null;
        Node noeudDuJeu = null;
        for (int i=0; i < listeDesNoeudsDuNoeudProgression.getLength(); i++) {
            Node itemElement = listeDesNoeudsDuNoeudProgression.item(i);
            if (itemElement.getNodeType() != Node.ELEMENT_NODE)
                continue;

            if (itemElement.getNodeName().equals("Jeu")){
                noeudDuJeu = itemElement;
                break;
            }

            NamedNodeMap attribusItem = itemElement.getAttributes();
            String nomDeLItem = ((Attr) attribusItem.getNamedItem("name")).getValue();

            if (nomDeLItem.equals(String.format("%d", numeroDuMonde))) {
                noeudDuMondeDuNiveauCorrespondant = itemElement;
                continue;
            }
        }
        // On recupere les attributs du noeud Monde
        NamedNodeMap listeAttributsDuNoeudMonde = noeudDuMondeDuNiveauCorrespondant.getAttributes();
        Node attributNiveauxTermines = listeAttributsDuNoeudMonde.getNamedItem("niveauxTermines");
        Node attributPourcentageNiveauxTermines = listeAttributsDuNoeudMonde.getNamedItem("pourcentageNiveauxTermines");
        Node attributEstEntierementTermine = listeAttributsDuNoeudMonde.getNamedItem("estEntierementTermine");
        Node attributTotalRecordsTemps = listeAttributsDuNoeudMonde.getNamedItem("totalRecordsTemps");
        Node attributTotalRecordsCoups = listeAttributsDuNoeudMonde.getNamedItem("totalRecordsCoups");
        Node attributPireRecordTemps = listeAttributsDuNoeudMonde.getNamedItem("pireRecordTemps");
        Node attributPireRecordTempsNiveau = listeAttributsDuNoeudMonde.getNamedItem("pireRecordTempsNiveau");
        Node attributPireRecordCoups = listeAttributsDuNoeudMonde.getNamedItem("pireRecordCoups");
        Node attributPireRecordCoupsNiveau = listeAttributsDuNoeudMonde.getNamedItem("pireRecordCoupsNiveau");

        // On modifie les attributs des records du noeud Monde
        if (leNiveauEstTerminePourLaPremiereFois){
            Integer nombreDeNiveauxTerminesDuMonde = Integer.parseInt(attributNiveauxTermines.getNodeValue());
            attributNiveauxTermines.setTextContent(String.format("%d", nombreDeNiveauxTerminesDuMonde + 1));
            attributPourcentageNiveauxTermines.setTextContent("" + (Float.parseFloat(attributPourcentageNiveauxTermines.getNodeValue()) + 12.5));
            if ((nombreDeNiveauxTerminesDuMonde+1) == 8)
                attributEstEntierementTermine.setTextContent("true");
            attributTotalRecordsTemps.setTextContent(String.format("%d", Integer.parseInt(attributTotalRecordsTemps.getNodeValue()) + tempsEcoule));
            attributTotalRecordsCoups.setTextContent(String.format("%d", Integer.parseInt(attributTotalRecordsCoups.getNodeValue()) + nombreDeCoups));
        } else{
            if (ancienRecordTemps > tempsEcoule)
                attributTotalRecordsTemps.setTextContent(String.format("%d", Integer.parseInt(attributTotalRecordsTemps.getNodeValue()) - (ancienRecordTemps - tempsEcoule)));
            if (ancienRecordCoups > nombreDeCoups)
                attributTotalRecordsCoups.setTextContent(String.format("%d", Integer.parseInt(attributTotalRecordsCoups.getNodeValue()) - (ancienRecordCoups - nombreDeCoups)));
        }

        // On recupere les attributs du noeud Jeu
        NamedNodeMap listeAttributsDuNoeudJeu = noeudDuJeu.getAttributes();
        attributNiveauxTermines = listeAttributsDuNoeudJeu.getNamedItem("niveauxTermines");
        attributPourcentageNiveauxTermines = listeAttributsDuNoeudJeu.getNamedItem("pourcentageNiveauxTermines");
        attributEstEntierementTermine = listeAttributsDuNoeudJeu.getNamedItem("estEntierementTermine");
        attributTotalRecordsTemps = listeAttributsDuNoeudJeu.getNamedItem("totalRecordsTemps");
        attributTotalRecordsCoups = listeAttributsDuNoeudJeu.getNamedItem("totalRecordsCoups");
        Node attributPireRecordTempsJeu = listeAttributsDuNoeudJeu.getNamedItem("pireRecordTemps");
        Node attributPireRecordTempsNiveauJeu = listeAttributsDuNoeudJeu.getNamedItem("pireRecordTempsNiveau");
        Node attributPireRecordCoupsJeu = listeAttributsDuNoeudJeu.getNamedItem("pireRecordCoups");
        Node attributPireRecordCoupsNiveauJeu = listeAttributsDuNoeudJeu.getNamedItem("pireRecordCoupsNiveau");

        // On modifie les attributs des records du noeud Jeu
        if (leNiveauEstTerminePourLaPremiereFois){
            Integer nombreDeNiveauxTerminesDuJeu = Integer.parseInt(attributNiveauxTermines.getNodeValue());
            attributNiveauxTermines.setTextContent(String.format("%d", nombreDeNiveauxTerminesDuJeu + 1));
            attributPourcentageNiveauxTermines.setTextContent("" + ((Float.parseFloat(attributPourcentageNiveauxTermines.getNodeValue()))+1.5625));
            if ((nombreDeNiveauxTerminesDuJeu+1) == 64)
                attributEstEntierementTermine.setTextContent("true");
            attributTotalRecordsTemps.setTextContent(String.format("%d", Integer.parseInt(attributTotalRecordsTemps.getNodeValue()) + tempsEcoule));
            attributTotalRecordsCoups.setTextContent(String.format("%d", Integer.parseInt(attributTotalRecordsCoups.getNodeValue()) + nombreDeCoups));
        } else{
            if (ancienRecordTemps > tempsEcoule)
                attributTotalRecordsTemps.setTextContent(String.format("%d", Integer.parseInt(attributTotalRecordsTemps.getNodeValue()) - (ancienRecordTemps - tempsEcoule)));
            if (ancienRecordCoups > nombreDeCoups)
                attributTotalRecordsCoups.setTextContent(String.format("%d", Integer.parseInt(attributTotalRecordsCoups.getNodeValue()) - (ancienRecordCoups - nombreDeCoups)));
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
        attributValeurNombreDeCoups.setTextContent(String.format("%d", Integer.parseInt(attributValeurNombreDeCoups.getNodeValue()) + nombreDeCoups));

        // On recupere les attributs du noeud de l'element du temps passé à jouer
        NamedNodeMap listeAttributsDuNoeudElementTempsPasseAJouer = noeudTempsPasseAJouer.getAttributes();
        Node attributValeurTempsPasseAJouer = listeAttributsDuNoeudElementTempsPasseAJouer.getNamedItem("valeur");

        // On modifie les attributs du noeud de l'element du temps passé à jouer
        attributValeurTempsPasseAJouer.setTextContent(app.formaterEnTempsPasseAJouer(attributValeurTempsPasseAJouer.getNodeValue(), tempsEcoule));

        // On actualise les pires scores du joueur
        // Le tableau est de la forme [recordTemps, niveauTemps, recordCoups, niveauCoups, recordTempsJEU, niveauTempsJEU, recordCoupsJEU, niveauCoupsJEU]
        int tableauContenantLesPiresScoresEtLeursNiveaux[] = actualisationPiresScores(listeElementNiveauCharge);
        attributPireRecordTemps.setTextContent(String.format("%d", tableauContenantLesPiresScoresEtLeursNiveaux[0]));
        attributPireRecordTempsNiveau.setTextContent(String.format("%d", tableauContenantLesPiresScoresEtLeursNiveaux[1]));
        attributPireRecordCoups.setTextContent(String.format("%d", tableauContenantLesPiresScoresEtLeursNiveaux[2]));
        attributPireRecordCoupsNiveau.setTextContent(String.format("%d", tableauContenantLesPiresScoresEtLeursNiveaux[3]));
        attributPireRecordTempsJeu.setTextContent(String.format("%d", tableauContenantLesPiresScoresEtLeursNiveaux[4]));
        attributPireRecordTempsNiveauJeu.setTextContent(String.format("%d", tableauContenantLesPiresScoresEtLeursNiveaux[5]));
        attributPireRecordCoupsJeu.setTextContent(String.format("%d", tableauContenantLesPiresScoresEtLeursNiveaux[6]));
        attributPireRecordCoupsNiveauJeu.setTextContent(String.format("%d", tableauContenantLesPiresScoresEtLeursNiveaux[7]));

        // On écrit le contenu modifié dans le fichier XML
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(app.getBaseContext().getFileStreamPath("statistics.xml"));
        transformer.transform(source, result);
    }

    public void onClickGoToSelectionDesNiveaux(View v) {
        app.setLeSonDeVictoireAEteJoue(false);
        app.setLaVibrationDeVictoireAEteJouee(false);
        Intent intent = new Intent(this, SelectionDesNiveaux.class);
        startActivity(intent);
        finish();
    }

    public void onClickGoToMenuPrincipal(View v) {
        app.setLeSonDeVictoireAEteJoue(false);
        app.setLaVibrationDeVictoireAEteJouee(false);
        Intent intent = new Intent(this, MenuPrincipal.class);
        startActivity(intent);
        finish();
    }

    public void onClickGoToNiveauSuivant(View v) {
        /* On remet ce booleen à false pour pouvoir rejouer le son de victoire lors de la prochaine
           victoire ! */
        app.setLeSonDeVictoireAEteJoue(false);
        app.setLaVibrationDeVictoireAEteJouee(false);

        // On relance l'activité jeu en passant niveau+1 à la clef niveau
        Intent intent = new Intent(this, Jeu.class);
        Bundle b = new Bundle();
        b.putInt("niveau", niveau+1);
        intent.putExtras(b);

        startActivity(intent);
        finish();
    }

    private int niveauToMonde(int niveau){
        if ((niveau != 0) && (niveau <= 8))
            return 1;
        else if (niveau <=16)
            return 2;
        else if (niveau <=24)
            return 3;
        else if (niveau <=32)
            return 4;
        else if (niveau <=40)
            return 5;
        else if (niveau <=48)
            return 6;
        else if (niveau <=56)
            return 7;
        else if (niveau <=64)
            return 8;
        else
            return 0;
    }

    private int[] actualisationPiresScores(NodeList listeDesNoeudsAEtudier){
        // Le tableau est de la forme [recordTemps, niveauTemps, recordCoups, niveauCoups,
        //                             recordTempsJEU, niveauTempsJEU, recordCoupsJEU,
        //                             niveauCoupsJEU]
        int tableauDesValeursDeRetour[] = {-1, -1, -1, -1, -1, -1, -1, -1};

        /* On va parcourir toute la liste des noeuds en comparant les valeurs du Monde du niveau qui
           vient d'etre terminé entre elles mais aussi avec celle de chaque niveau du jeu. */
        for (int i=0; i < listeDesNoeudsAEtudier.getLength(); i++) {
            Node itemNode = listeDesNoeudsAEtudier.item(i);
            if (itemNode.getNodeType() != Node.ELEMENT_NODE)
                continue;

            Element itemElement = (Element) itemNode;
            String stringFormateeDuNumeroDuNiveau = (itemElement.getAttribute("name")).substring(2);
            int numeroDuNiveau = Integer.parseInt(stringFormateeDuNumeroDuNiveau);
            int recordCoupsDuNiveau = Integer.parseInt(itemElement.getAttribute("recordCoups"));
            int recordTempsDuNiveau = Integer.parseInt(itemElement.getAttribute("recordTemps"));

            // On saute les iterations des niveaux n'ayant pas encore de record
            if (recordCoupsDuNiveau == -1)
                continue;

            // En plus de comparer les valeurs à celle des autres puzzles du jeu, on les compare
            // aussi à celles du monde.
            if (niveauToMonde(numeroDuNiveau) == niveauToMonde(this.niveau)){
                if ((tableauDesValeursDeRetour[0] < recordTempsDuNiveau) || (tableauDesValeursDeRetour[0] == -1)) {
                    tableauDesValeursDeRetour[0] = recordTempsDuNiveau;
                    tableauDesValeursDeRetour[1] = numeroDuNiveau;
                }

                if ((tableauDesValeursDeRetour[2] < recordCoupsDuNiveau) || (tableauDesValeursDeRetour[2] == -1)){
                    tableauDesValeursDeRetour[2] = recordCoupsDuNiveau;
                    tableauDesValeursDeRetour[3] = numeroDuNiveau;
                }
            }

            // On effectue ensuite ces comparaisons avec tous les niveaux du jeu
            if ((tableauDesValeursDeRetour[4] < recordTempsDuNiveau) || (tableauDesValeursDeRetour[4] == -1)) {
                tableauDesValeursDeRetour[4] = recordTempsDuNiveau;
                tableauDesValeursDeRetour[5] = numeroDuNiveau;
            }

            if ((tableauDesValeursDeRetour[6] < recordCoupsDuNiveau) || (tableauDesValeursDeRetour[6] == -1)){
                tableauDesValeursDeRetour[6] = recordCoupsDuNiveau;
                tableauDesValeursDeRetour[7] = numeroDuNiveau;
            }
        }

        return tableauDesValeursDeRetour;
    }

    /* Cette fonction permet de rendre invisible et intouchable une View ainsi que toutes ses
       petites Views enfants. */
    private void setIntouchable(View view) {
        if (view != null) {
            view.setClickable(false);
            if (view instanceof ViewGroup) {
                ViewGroup vg = ((ViewGroup) view);
                for (int i = 0; i < vg.getChildCount(); i++) {
                    setIntouchable(vg.getChildAt(i));
                    view.setVisibility(View.GONE);
                }
            }
        }
    }
}
