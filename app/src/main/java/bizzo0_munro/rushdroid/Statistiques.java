package bizzo0_munro.rushdroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class Statistiques extends MonAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistiques);

        try {
            chargementEtInitialisationDesStatistiquesDansLeXML();
        } catch (IOException | SAXException | TransformerException | ParserConfigurationException e) {
            e.printStackTrace();
        }

        // On change le background de certains Layout si le theme est DARK
        if (app.getTHEME() == 0)
            actualiserLinearLayoutsSiThemeDark();
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

    public void onClickRetourAuMenuPrincipal(View v){
        Intent intent = new Intent(this, MenuPrincipal.class);
        startActivity(intent);
        finish();
    }

    private String remplaceParInfiniSiLeMondeNEstPasComplet(String chaineATester, int progressionActuelle){
        if (progressionActuelle != 8)
            return "∞";
        return chaineATester;
    }

    private String remplaceParInfiniSiLeJeuNEstPasComplet(String chaineATester, int progressionActuelle){
        if (progressionActuelle != 64)
            return "∞";
        return chaineATester;
    }

    private void chargementEtInitialisationDesStatistiquesDansLeXML() throws IOException, SAXException, ParserConfigurationException, TransformerException {
        TextView tvAModifier;
        NamedNodeMap listeAttributsDuNoeudMonde;
        Node attributNiveauxTermines, attributPourcentageNiveauxTermines, attributTotalRecordsTemps,
             attributPireRecordTemps, attributPireRecordTempsNiveau, attributTotalRecordsCoups,
             attributPireRecordCoups, attributPireRecordCoupsNiveau;
        int nombreDeNiveauxTerminesDansLeMonde, nombreDeNiveauxTerminesDansLeJeu;

        // Chargement des records dans le fichier des statistiquesrecordCoups
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        FileInputStream cheminDuFichier = app.getBaseContext().openFileInput("statistics.xml");
        Document doc = db.parse(cheminDuFichier);
        // Obtention de l'element racine du document
        Element racine = doc.getDocumentElement();
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
        // Recuperation des informations du noeud progression
        // On recupere les noeuds des mondes correspondants ainsi que le noeud Jeu
        NodeList listeDesNoeudsDuNoeudProgression = noeudProgression.getChildNodes();
        Node noeudDuMonde1 = null;
        Node noeudDuMonde2 = null;
        Node noeudDuMonde3 = null;
        Node noeudDuMonde4 = null;
        Node noeudDuMonde5 = null;
        Node noeudDuMonde6 = null;
        Node noeudDuMonde7 = null;
        Node noeudDuMonde8 = null;
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
            int nomDeLItemInt = Integer.parseInt(nomDeLItem);

            switch(nomDeLItemInt){
                case 1:
                    noeudDuMonde1 = itemElement;
                    break;
                case 2:
                    noeudDuMonde2 = itemElement;
                    break;
                case 3:
                    noeudDuMonde3 = itemElement;
                    break;
                case 4:
                    noeudDuMonde4 = itemElement;
                    break;
                case 5:
                    noeudDuMonde5 = itemElement;
                    break;
                case 6:
                    noeudDuMonde6 = itemElement;
                    break;
                case 7:
                    noeudDuMonde7 = itemElement;
                    break;
                case 8:
                    noeudDuMonde8 = itemElement;
                    break;
            }
        }
        // On recupere les attributs du noeud du monde 1
        listeAttributsDuNoeudMonde = noeudDuMonde1.getAttributes();
        attributNiveauxTermines = listeAttributsDuNoeudMonde.getNamedItem("niveauxTermines");
        attributPourcentageNiveauxTermines = listeAttributsDuNoeudMonde.getNamedItem("pourcentageNiveauxTermines");
        attributTotalRecordsTemps = listeAttributsDuNoeudMonde.getNamedItem("totalRecordsTemps");
        attributPireRecordTemps = listeAttributsDuNoeudMonde.getNamedItem("pireRecordTemps");
        attributPireRecordTempsNiveau = listeAttributsDuNoeudMonde.getNamedItem("pireRecordTempsNiveau");
        attributTotalRecordsCoups = listeAttributsDuNoeudMonde.getNamedItem("totalRecordsCoups");
        attributPireRecordCoups = listeAttributsDuNoeudMonde.getNamedItem("pireRecordCoups");
        attributPireRecordCoupsNiveau = listeAttributsDuNoeudMonde.getNamedItem("pireRecordCoupsNiveau");

        // On initialise les statistiques avec les valeurs du noeud du monde 1
        nombreDeNiveauxTerminesDansLeMonde = Integer.parseInt(attributNiveauxTermines.getNodeValue());
        tvAModifier = (TextView) findViewById(R.id.monde1tv1);
        tvAModifier.setText(String.format("%d", nombreDeNiveauxTerminesDansLeMonde));
        tvAModifier = (TextView) findViewById(R.id.monde1tv2);
        tvAModifier.setText(String.format("%s %%", attributPourcentageNiveauxTermines.getNodeValue()));
        tvAModifier = (TextView) findViewById(R.id.monde1tv3);
        tvAModifier.setText(remplaceParInfiniSiLeMondeNEstPasComplet(attributTotalRecordsTemps.getNodeValue(), nombreDeNiveauxTerminesDansLeMonde));
        tvAModifier = (TextView) findViewById(R.id.monde1tv4);
        tvAModifier.setText(remplaceParInfiniSiLeMondeNEstPasComplet(attributPireRecordTemps.getNodeValue(), nombreDeNiveauxTerminesDansLeMonde));
        tvAModifier = (TextView) findViewById(R.id.monde1tv5);
        tvAModifier.setText(remplaceParInfiniSiLeMondeNEstPasComplet(attributPireRecordTempsNiveau.getNodeValue(), nombreDeNiveauxTerminesDansLeMonde));
        tvAModifier = (TextView) findViewById(R.id.monde1tv6);
        tvAModifier.setText(remplaceParInfiniSiLeMondeNEstPasComplet(attributTotalRecordsCoups.getNodeValue(), nombreDeNiveauxTerminesDansLeMonde));
        tvAModifier = (TextView) findViewById(R.id.monde1tv7);
        tvAModifier.setText(remplaceParInfiniSiLeMondeNEstPasComplet(attributPireRecordCoups.getNodeValue(), nombreDeNiveauxTerminesDansLeMonde));
        tvAModifier = (TextView) findViewById(R.id.monde1tv8);
        tvAModifier.setText(remplaceParInfiniSiLeMondeNEstPasComplet(attributPireRecordCoupsNiveau.getNodeValue(), nombreDeNiveauxTerminesDansLeMonde));

        // On recupere les attributs du noeud du monde 2
        listeAttributsDuNoeudMonde = noeudDuMonde2.getAttributes();
        attributNiveauxTermines = listeAttributsDuNoeudMonde.getNamedItem("niveauxTermines");
        attributPourcentageNiveauxTermines = listeAttributsDuNoeudMonde.getNamedItem("pourcentageNiveauxTermines");
        attributTotalRecordsTemps = listeAttributsDuNoeudMonde.getNamedItem("totalRecordsTemps");
        attributPireRecordTemps = listeAttributsDuNoeudMonde.getNamedItem("pireRecordTemps");
        attributPireRecordTempsNiveau = listeAttributsDuNoeudMonde.getNamedItem("pireRecordTempsNiveau");
        attributTotalRecordsCoups = listeAttributsDuNoeudMonde.getNamedItem("totalRecordsCoups");
        attributPireRecordCoups = listeAttributsDuNoeudMonde.getNamedItem("pireRecordCoups");
        attributPireRecordCoupsNiveau = listeAttributsDuNoeudMonde.getNamedItem("pireRecordCoupsNiveau");

        // On initialise les statistiques avec les valeurs du noeud du monde 2
        nombreDeNiveauxTerminesDansLeMonde = Integer.parseInt(attributNiveauxTermines.getNodeValue());
        tvAModifier = (TextView) findViewById(R.id.monde2tv1);
        tvAModifier.setText(String.format("%d", nombreDeNiveauxTerminesDansLeMonde));
        tvAModifier = (TextView) findViewById(R.id.monde2tv2);
        tvAModifier.setText(String.format("%s %%", attributPourcentageNiveauxTermines.getNodeValue()));
        tvAModifier = (TextView) findViewById(R.id.monde2tv3);
        tvAModifier.setText(remplaceParInfiniSiLeMondeNEstPasComplet(attributTotalRecordsTemps.getNodeValue(), nombreDeNiveauxTerminesDansLeMonde));
        tvAModifier = (TextView) findViewById(R.id.monde2tv4);
        tvAModifier.setText(remplaceParInfiniSiLeMondeNEstPasComplet(attributPireRecordTemps.getNodeValue(), nombreDeNiveauxTerminesDansLeMonde));
        tvAModifier = (TextView) findViewById(R.id.monde2tv5);
        tvAModifier.setText(remplaceParInfiniSiLeMondeNEstPasComplet(attributPireRecordTempsNiveau.getNodeValue(), nombreDeNiveauxTerminesDansLeMonde));
        tvAModifier = (TextView) findViewById(R.id.monde2tv6);
        tvAModifier.setText(remplaceParInfiniSiLeMondeNEstPasComplet(attributTotalRecordsCoups.getNodeValue(), nombreDeNiveauxTerminesDansLeMonde));
        tvAModifier = (TextView) findViewById(R.id.monde2tv7);
        tvAModifier.setText(remplaceParInfiniSiLeMondeNEstPasComplet(attributPireRecordCoups.getNodeValue(), nombreDeNiveauxTerminesDansLeMonde));
        tvAModifier = (TextView) findViewById(R.id.monde2tv8);
        tvAModifier.setText(remplaceParInfiniSiLeMondeNEstPasComplet(attributPireRecordCoupsNiveau.getNodeValue(), nombreDeNiveauxTerminesDansLeMonde));


        // On recupere les attributs du noeud du monde 3
        listeAttributsDuNoeudMonde = noeudDuMonde3.getAttributes();
        attributNiveauxTermines = listeAttributsDuNoeudMonde.getNamedItem("niveauxTermines");
        attributPourcentageNiveauxTermines = listeAttributsDuNoeudMonde.getNamedItem("pourcentageNiveauxTermines");
        attributTotalRecordsTemps = listeAttributsDuNoeudMonde.getNamedItem("totalRecordsTemps");
        attributPireRecordTemps = listeAttributsDuNoeudMonde.getNamedItem("pireRecordTemps");
        attributPireRecordTempsNiveau = listeAttributsDuNoeudMonde.getNamedItem("pireRecordTempsNiveau");
        attributTotalRecordsCoups = listeAttributsDuNoeudMonde.getNamedItem("totalRecordsCoups");
        attributPireRecordCoups = listeAttributsDuNoeudMonde.getNamedItem("pireRecordCoups");
        attributPireRecordCoupsNiveau = listeAttributsDuNoeudMonde.getNamedItem("pireRecordCoupsNiveau");

        // On initialise les statistiques avec les valeurs du noeud du monde 3
        nombreDeNiveauxTerminesDansLeMonde = Integer.parseInt(attributNiveauxTermines.getNodeValue());
        tvAModifier = (TextView) findViewById(R.id.monde3tv1);
        tvAModifier.setText(String.format("%d", nombreDeNiveauxTerminesDansLeMonde));
        tvAModifier = (TextView) findViewById(R.id.monde3tv2);
        tvAModifier.setText(String.format("%s %%", attributPourcentageNiveauxTermines.getNodeValue()));
        tvAModifier = (TextView) findViewById(R.id.monde3tv3);
        tvAModifier.setText(remplaceParInfiniSiLeMondeNEstPasComplet(attributTotalRecordsTemps.getNodeValue(), nombreDeNiveauxTerminesDansLeMonde));
        tvAModifier = (TextView) findViewById(R.id.monde3tv4);
        tvAModifier.setText(remplaceParInfiniSiLeMondeNEstPasComplet(attributPireRecordTemps.getNodeValue(), nombreDeNiveauxTerminesDansLeMonde));
        tvAModifier = (TextView) findViewById(R.id.monde3tv5);
        tvAModifier.setText(remplaceParInfiniSiLeMondeNEstPasComplet(attributPireRecordTempsNiveau.getNodeValue(), nombreDeNiveauxTerminesDansLeMonde));
        tvAModifier = (TextView) findViewById(R.id.monde3tv6);
        tvAModifier.setText(remplaceParInfiniSiLeMondeNEstPasComplet(attributTotalRecordsCoups.getNodeValue(), nombreDeNiveauxTerminesDansLeMonde));
        tvAModifier = (TextView) findViewById(R.id.monde3tv7);
        tvAModifier.setText(remplaceParInfiniSiLeMondeNEstPasComplet(attributPireRecordCoups.getNodeValue(), nombreDeNiveauxTerminesDansLeMonde));
        tvAModifier = (TextView) findViewById(R.id.monde3tv8);
        tvAModifier.setText(remplaceParInfiniSiLeMondeNEstPasComplet(attributPireRecordCoupsNiveau.getNodeValue(), nombreDeNiveauxTerminesDansLeMonde));


        // On recupere les attributs du noeud du monde 4
        listeAttributsDuNoeudMonde = noeudDuMonde4.getAttributes();
        attributNiveauxTermines = listeAttributsDuNoeudMonde.getNamedItem("niveauxTermines");
        attributPourcentageNiveauxTermines = listeAttributsDuNoeudMonde.getNamedItem("pourcentageNiveauxTermines");
        attributTotalRecordsTemps = listeAttributsDuNoeudMonde.getNamedItem("totalRecordsTemps");
        attributPireRecordTemps = listeAttributsDuNoeudMonde.getNamedItem("pireRecordTemps");
        attributPireRecordTempsNiveau = listeAttributsDuNoeudMonde.getNamedItem("pireRecordTempsNiveau");
        attributTotalRecordsCoups = listeAttributsDuNoeudMonde.getNamedItem("totalRecordsCoups");
        attributPireRecordCoups = listeAttributsDuNoeudMonde.getNamedItem("pireRecordCoups");
        attributPireRecordCoupsNiveau = listeAttributsDuNoeudMonde.getNamedItem("pireRecordCoupsNiveau");

        // On initialise les statistiques avec les valeurs du noeud du monde 4
        nombreDeNiveauxTerminesDansLeMonde = Integer.parseInt(attributNiveauxTermines.getNodeValue());
        tvAModifier = (TextView) findViewById(R.id.monde4tv1);
        tvAModifier.setText(String.format("%d", nombreDeNiveauxTerminesDansLeMonde));
        tvAModifier = (TextView) findViewById(R.id.monde4tv2);
        tvAModifier.setText(String.format("%s %%", attributPourcentageNiveauxTermines.getNodeValue()));
        tvAModifier = (TextView) findViewById(R.id.monde4tv3);
        tvAModifier.setText(remplaceParInfiniSiLeMondeNEstPasComplet(attributTotalRecordsTemps.getNodeValue(), nombreDeNiveauxTerminesDansLeMonde));
        tvAModifier = (TextView) findViewById(R.id.monde4tv4);
        tvAModifier.setText(remplaceParInfiniSiLeMondeNEstPasComplet(attributPireRecordTemps.getNodeValue(), nombreDeNiveauxTerminesDansLeMonde));
        tvAModifier = (TextView) findViewById(R.id.monde4tv5);
        tvAModifier.setText(remplaceParInfiniSiLeMondeNEstPasComplet(attributPireRecordTempsNiveau.getNodeValue(), nombreDeNiveauxTerminesDansLeMonde));
        tvAModifier = (TextView) findViewById(R.id.monde4tv6);
        tvAModifier.setText(remplaceParInfiniSiLeMondeNEstPasComplet(attributTotalRecordsCoups.getNodeValue(), nombreDeNiveauxTerminesDansLeMonde));
        tvAModifier = (TextView) findViewById(R.id.monde4tv7);
        tvAModifier.setText(remplaceParInfiniSiLeMondeNEstPasComplet(attributPireRecordCoups.getNodeValue(), nombreDeNiveauxTerminesDansLeMonde));
        tvAModifier = (TextView) findViewById(R.id.monde4tv8);
        tvAModifier.setText(remplaceParInfiniSiLeMondeNEstPasComplet(attributPireRecordCoupsNiveau.getNodeValue(), nombreDeNiveauxTerminesDansLeMonde));

        // On recupere les attributs du noeud du monde 5
        listeAttributsDuNoeudMonde = noeudDuMonde5.getAttributes();
        attributNiveauxTermines = listeAttributsDuNoeudMonde.getNamedItem("niveauxTermines");
        attributPourcentageNiveauxTermines = listeAttributsDuNoeudMonde.getNamedItem("pourcentageNiveauxTermines");
        attributTotalRecordsTemps = listeAttributsDuNoeudMonde.getNamedItem("totalRecordsTemps");
        attributPireRecordTemps = listeAttributsDuNoeudMonde.getNamedItem("pireRecordTemps");
        attributPireRecordTempsNiveau = listeAttributsDuNoeudMonde.getNamedItem("pireRecordTempsNiveau");
        attributTotalRecordsCoups = listeAttributsDuNoeudMonde.getNamedItem("totalRecordsCoups");
        attributPireRecordCoups = listeAttributsDuNoeudMonde.getNamedItem("pireRecordCoups");
        attributPireRecordCoupsNiveau = listeAttributsDuNoeudMonde.getNamedItem("pireRecordCoupsNiveau");

        // On initialise les statistiques avec les valeurs du noeud du monde 5
        nombreDeNiveauxTerminesDansLeMonde = Integer.parseInt(attributNiveauxTermines.getNodeValue());
        tvAModifier = (TextView) findViewById(R.id.monde5tv1);
        tvAModifier.setText(String.format("%d", nombreDeNiveauxTerminesDansLeMonde));
        tvAModifier = (TextView) findViewById(R.id.monde5tv2);
        tvAModifier.setText(String.format("%s %%", attributPourcentageNiveauxTermines.getNodeValue()));
        tvAModifier = (TextView) findViewById(R.id.monde5tv3);
        tvAModifier.setText(remplaceParInfiniSiLeMondeNEstPasComplet(attributTotalRecordsTemps.getNodeValue(), nombreDeNiveauxTerminesDansLeMonde));
        tvAModifier = (TextView) findViewById(R.id.monde5tv4);
        tvAModifier.setText(remplaceParInfiniSiLeMondeNEstPasComplet(attributPireRecordTemps.getNodeValue(), nombreDeNiveauxTerminesDansLeMonde));
        tvAModifier = (TextView) findViewById(R.id.monde5tv5);
        tvAModifier.setText(remplaceParInfiniSiLeMondeNEstPasComplet(attributPireRecordTempsNiveau.getNodeValue(), nombreDeNiveauxTerminesDansLeMonde));
        tvAModifier = (TextView) findViewById(R.id.monde5tv6);
        tvAModifier.setText(remplaceParInfiniSiLeMondeNEstPasComplet(attributTotalRecordsCoups.getNodeValue(), nombreDeNiveauxTerminesDansLeMonde));
        tvAModifier = (TextView) findViewById(R.id.monde5tv7);
        tvAModifier.setText(remplaceParInfiniSiLeMondeNEstPasComplet(attributPireRecordCoups.getNodeValue(), nombreDeNiveauxTerminesDansLeMonde));
        tvAModifier = (TextView) findViewById(R.id.monde5tv8);
        tvAModifier.setText(remplaceParInfiniSiLeMondeNEstPasComplet(attributPireRecordCoupsNiveau.getNodeValue(), nombreDeNiveauxTerminesDansLeMonde));

        // On recupere les attributs du noeud du monde 6
        listeAttributsDuNoeudMonde = noeudDuMonde6.getAttributes();
        attributNiveauxTermines = listeAttributsDuNoeudMonde.getNamedItem("niveauxTermines");
        attributPourcentageNiveauxTermines = listeAttributsDuNoeudMonde.getNamedItem("pourcentageNiveauxTermines");
        attributTotalRecordsTemps = listeAttributsDuNoeudMonde.getNamedItem("totalRecordsTemps");
        attributPireRecordTemps = listeAttributsDuNoeudMonde.getNamedItem("pireRecordTemps");
        attributPireRecordTempsNiveau = listeAttributsDuNoeudMonde.getNamedItem("pireRecordTempsNiveau");
        attributTotalRecordsCoups = listeAttributsDuNoeudMonde.getNamedItem("totalRecordsCoups");
        attributPireRecordCoups = listeAttributsDuNoeudMonde.getNamedItem("pireRecordCoups");
        attributPireRecordCoupsNiveau = listeAttributsDuNoeudMonde.getNamedItem("pireRecordCoupsNiveau");

        // On initialise les statistiques avec les valeurs du noeud du monde 6
        nombreDeNiveauxTerminesDansLeMonde = Integer.parseInt(attributNiveauxTermines.getNodeValue());
        tvAModifier = (TextView) findViewById(R.id.monde6tv1);
        tvAModifier.setText(String.format("%d", nombreDeNiveauxTerminesDansLeMonde));
        tvAModifier = (TextView) findViewById(R.id.monde6tv2);
        tvAModifier.setText(String.format("%s %%", attributPourcentageNiveauxTermines.getNodeValue()));
        tvAModifier = (TextView) findViewById(R.id.monde6tv3);
        tvAModifier.setText(remplaceParInfiniSiLeMondeNEstPasComplet(attributTotalRecordsTemps.getNodeValue(), nombreDeNiveauxTerminesDansLeMonde));
        tvAModifier = (TextView) findViewById(R.id.monde6tv4);
        tvAModifier.setText(remplaceParInfiniSiLeMondeNEstPasComplet(attributPireRecordTemps.getNodeValue(), nombreDeNiveauxTerminesDansLeMonde));
        tvAModifier = (TextView) findViewById(R.id.monde6tv5);
        tvAModifier.setText(remplaceParInfiniSiLeMondeNEstPasComplet(attributPireRecordTempsNiveau.getNodeValue(), nombreDeNiveauxTerminesDansLeMonde));
        tvAModifier = (TextView) findViewById(R.id.monde6tv6);
        tvAModifier.setText(remplaceParInfiniSiLeMondeNEstPasComplet(attributTotalRecordsCoups.getNodeValue(), nombreDeNiveauxTerminesDansLeMonde));
        tvAModifier = (TextView) findViewById(R.id.monde6tv7);
        tvAModifier.setText(remplaceParInfiniSiLeMondeNEstPasComplet(attributPireRecordCoups.getNodeValue(), nombreDeNiveauxTerminesDansLeMonde));
        tvAModifier = (TextView) findViewById(R.id.monde6tv8);
        tvAModifier.setText(remplaceParInfiniSiLeMondeNEstPasComplet(attributPireRecordCoupsNiveau.getNodeValue(), nombreDeNiveauxTerminesDansLeMonde));

        // On recupere les attributs du noeud du monde 7
        listeAttributsDuNoeudMonde = noeudDuMonde7.getAttributes();
        attributNiveauxTermines = listeAttributsDuNoeudMonde.getNamedItem("niveauxTermines");
        attributPourcentageNiveauxTermines = listeAttributsDuNoeudMonde.getNamedItem("pourcentageNiveauxTermines");
        attributTotalRecordsTemps = listeAttributsDuNoeudMonde.getNamedItem("totalRecordsTemps");
        attributPireRecordTemps = listeAttributsDuNoeudMonde.getNamedItem("pireRecordTemps");
        attributPireRecordTempsNiveau = listeAttributsDuNoeudMonde.getNamedItem("pireRecordTempsNiveau");
        attributTotalRecordsCoups = listeAttributsDuNoeudMonde.getNamedItem("totalRecordsCoups");
        attributPireRecordCoups = listeAttributsDuNoeudMonde.getNamedItem("pireRecordCoups");
        attributPireRecordCoupsNiveau = listeAttributsDuNoeudMonde.getNamedItem("pireRecordCoupsNiveau");

        // On initialise les statistiques avec les valeurs du noeud du monde 7
        nombreDeNiveauxTerminesDansLeMonde = Integer.parseInt(attributNiveauxTermines.getNodeValue());
        tvAModifier = (TextView) findViewById(R.id.monde7tv1);
        tvAModifier.setText(String.format("%d", nombreDeNiveauxTerminesDansLeMonde));
        tvAModifier = (TextView) findViewById(R.id.monde7tv2);
        tvAModifier.setText(String.format("%s %%", attributPourcentageNiveauxTermines.getNodeValue()));
        tvAModifier = (TextView) findViewById(R.id.monde7tv3);
        tvAModifier.setText(remplaceParInfiniSiLeMondeNEstPasComplet(attributTotalRecordsTemps.getNodeValue(), nombreDeNiveauxTerminesDansLeMonde));
        tvAModifier = (TextView) findViewById(R.id.monde7tv4);
        tvAModifier.setText(remplaceParInfiniSiLeMondeNEstPasComplet(attributPireRecordTemps.getNodeValue(), nombreDeNiveauxTerminesDansLeMonde));
        tvAModifier = (TextView) findViewById(R.id.monde7tv5);
        tvAModifier.setText(remplaceParInfiniSiLeMondeNEstPasComplet(attributPireRecordTempsNiveau.getNodeValue(), nombreDeNiveauxTerminesDansLeMonde));
        tvAModifier = (TextView) findViewById(R.id.monde7tv6);
        tvAModifier.setText(remplaceParInfiniSiLeMondeNEstPasComplet(attributTotalRecordsCoups.getNodeValue(), nombreDeNiveauxTerminesDansLeMonde));
        tvAModifier = (TextView) findViewById(R.id.monde7tv7);
        tvAModifier.setText(remplaceParInfiniSiLeMondeNEstPasComplet(attributPireRecordCoups.getNodeValue(), nombreDeNiveauxTerminesDansLeMonde));
        tvAModifier = (TextView) findViewById(R.id.monde7tv8);
        tvAModifier.setText(remplaceParInfiniSiLeMondeNEstPasComplet(attributPireRecordCoupsNiveau.getNodeValue(), nombreDeNiveauxTerminesDansLeMonde));

        // On recupere les attributs du noeud du monde 8
        listeAttributsDuNoeudMonde = noeudDuMonde8.getAttributes();
        attributNiveauxTermines = listeAttributsDuNoeudMonde.getNamedItem("niveauxTermines");
        attributPourcentageNiveauxTermines = listeAttributsDuNoeudMonde.getNamedItem("pourcentageNiveauxTermines");
        attributTotalRecordsTemps = listeAttributsDuNoeudMonde.getNamedItem("totalRecordsTemps");
        attributPireRecordTemps = listeAttributsDuNoeudMonde.getNamedItem("pireRecordTemps");
        attributPireRecordTempsNiveau = listeAttributsDuNoeudMonde.getNamedItem("pireRecordTempsNiveau");
        attributTotalRecordsCoups = listeAttributsDuNoeudMonde.getNamedItem("totalRecordsCoups");
        attributPireRecordCoups = listeAttributsDuNoeudMonde.getNamedItem("pireRecordCoups");
        attributPireRecordCoupsNiveau = listeAttributsDuNoeudMonde.getNamedItem("pireRecordCoupsNiveau");

        // On initialise les statistiques avec les valeurs du noeud du monde 8
        nombreDeNiveauxTerminesDansLeMonde = Integer.parseInt(attributNiveauxTermines.getNodeValue());
        tvAModifier = (TextView) findViewById(R.id.monde8tv1);
        tvAModifier.setText(String.format("%d", nombreDeNiveauxTerminesDansLeMonde));
        tvAModifier = (TextView) findViewById(R.id.monde8tv2);
        tvAModifier.setText(String.format("%s %%", attributPourcentageNiveauxTermines.getNodeValue()));
        tvAModifier = (TextView) findViewById(R.id.monde8tv3);
        tvAModifier.setText(remplaceParInfiniSiLeMondeNEstPasComplet(attributTotalRecordsTemps.getNodeValue(), nombreDeNiveauxTerminesDansLeMonde));
        tvAModifier = (TextView) findViewById(R.id.monde8tv4);
        tvAModifier.setText(remplaceParInfiniSiLeMondeNEstPasComplet(attributPireRecordTemps.getNodeValue(), nombreDeNiveauxTerminesDansLeMonde));
        tvAModifier = (TextView) findViewById(R.id.monde8tv5);
        tvAModifier.setText(remplaceParInfiniSiLeMondeNEstPasComplet(attributPireRecordTempsNiveau.getNodeValue(), nombreDeNiveauxTerminesDansLeMonde));
        tvAModifier = (TextView) findViewById(R.id.monde8tv6);
        tvAModifier.setText(remplaceParInfiniSiLeMondeNEstPasComplet(attributTotalRecordsCoups.getNodeValue(), nombreDeNiveauxTerminesDansLeMonde));
        tvAModifier = (TextView) findViewById(R.id.monde8tv7);
        tvAModifier.setText(remplaceParInfiniSiLeMondeNEstPasComplet(attributPireRecordCoups.getNodeValue(), nombreDeNiveauxTerminesDansLeMonde));
        tvAModifier = (TextView) findViewById(R.id.monde8tv8);
        tvAModifier.setText(remplaceParInfiniSiLeMondeNEstPasComplet(attributPireRecordCoupsNiveau.getNodeValue(), nombreDeNiveauxTerminesDansLeMonde));


        // On recupere les attributs du noeud du jeu
        listeAttributsDuNoeudMonde = noeudDuJeu.getAttributes();
        attributNiveauxTermines = listeAttributsDuNoeudMonde.getNamedItem("niveauxTermines");
        attributPourcentageNiveauxTermines = listeAttributsDuNoeudMonde.getNamedItem("pourcentageNiveauxTermines");
        attributTotalRecordsTemps = listeAttributsDuNoeudMonde.getNamedItem("totalRecordsTemps");
        attributPireRecordTemps = listeAttributsDuNoeudMonde.getNamedItem("pireRecordTemps");
        attributPireRecordTempsNiveau = listeAttributsDuNoeudMonde.getNamedItem("pireRecordTempsNiveau");
        attributTotalRecordsCoups = listeAttributsDuNoeudMonde.getNamedItem("totalRecordsCoups");
        attributPireRecordCoups = listeAttributsDuNoeudMonde.getNamedItem("pireRecordCoups");
        attributPireRecordCoupsNiveau = listeAttributsDuNoeudMonde.getNamedItem("pireRecordCoupsNiveau");

        // On initialise les statistiques avec les valeurs du noeud du jeu
        nombreDeNiveauxTerminesDansLeJeu = Integer.parseInt(attributNiveauxTermines.getNodeValue());
        tvAModifier = (TextView) findViewById(R.id.jeutv1);
        tvAModifier.setText(String.format("%d", nombreDeNiveauxTerminesDansLeJeu));
        tvAModifier = (TextView) findViewById(R.id.jeutv2);
        tvAModifier.setText(String.format("%s %%", attributPourcentageNiveauxTermines.getNodeValue()));
        tvAModifier = (TextView) findViewById(R.id.jeutv3);
        tvAModifier.setText(remplaceParInfiniSiLeJeuNEstPasComplet(attributTotalRecordsTemps.getNodeValue(), nombreDeNiveauxTerminesDansLeJeu));
        tvAModifier = (TextView) findViewById(R.id.jeutv4);
        tvAModifier.setText(remplaceParInfiniSiLeJeuNEstPasComplet(attributPireRecordTemps.getNodeValue(), nombreDeNiveauxTerminesDansLeJeu));
        tvAModifier = (TextView) findViewById(R.id.jeutv5);
        tvAModifier.setText(remplaceParInfiniSiLeJeuNEstPasComplet(attributPireRecordTempsNiveau.getNodeValue(), nombreDeNiveauxTerminesDansLeJeu));
        tvAModifier = (TextView) findViewById(R.id.jeutv6);
        tvAModifier.setText(remplaceParInfiniSiLeJeuNEstPasComplet(attributTotalRecordsCoups.getNodeValue(), nombreDeNiveauxTerminesDansLeJeu));
        tvAModifier = (TextView) findViewById(R.id.jeutv7);
        tvAModifier.setText(remplaceParInfiniSiLeJeuNEstPasComplet(attributPireRecordCoups.getNodeValue(), nombreDeNiveauxTerminesDansLeJeu));
        tvAModifier = (TextView) findViewById(R.id.jeutv8);
        tvAModifier.setText(remplaceParInfiniSiLeJeuNEstPasComplet(attributPireRecordCoupsNiveau.getNodeValue(), nombreDeNiveauxTerminesDansLeJeu));


        // Recuperation des informations du noeud des statistiques diverses
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

        // On initialise l'element du nombre de coups
        tvAModifier = (TextView) findViewById(R.id.autresstatistiquestv1);
        tvAModifier.setText(attributValeurNombreDeCoups.getNodeValue());

        // On recupere les attributs du noeud de l'element du temps passé à jouer
        NamedNodeMap listeAttributsDuNoeudElementTempsPasseAJouer = noeudTempsPasseAJouer.getAttributes();
        Node attributValeurTempsPasseAJouer = listeAttributsDuNoeudElementTempsPasseAJouer.getNamedItem("valeur");

        // On initialise l'element du temps passé à jouer
        tvAModifier = (TextView) findViewById(R.id.autresstatistiquestv2);
        tvAModifier.setText(attributValeurTempsPasseAJouer.getNodeValue());

        // On écrit le contenu modifié dans le fichier XML
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(app.getBaseContext().getFileStreamPath("statistics.xml"));
        transformer.transform(source, result);
    }

    private void actualiserLinearLayoutsSiThemeDark(){
        LinearLayout ll = (LinearLayout) findViewById(R.id.llab1);
        ll.setBackgroundResource(R.drawable.background_sirg_ab);
        ll = (LinearLayout) findViewById(R.id.llab2);
        ll.setBackgroundResource(R.drawable.background_sirg_ab);
        ll = (LinearLayout) findViewById(R.id.llab3);
        ll.setBackgroundResource(R.drawable.background_sirg_ab);
    }
}