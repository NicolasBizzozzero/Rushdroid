package bizzo0_munro.rushdroid;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

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
import javax.xml.transform.TransformerException;

public class SelectionDesNiveaux extends MonAppCompatActivity {
    private boolean[] tableauNiveauxTermines;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection_du_niveau);

        // On charge un tableau correspondant aux niveaux terminés
        try {
            tableauNiveauxTermines = tableauDesNiveauxTermines();
        } catch (TransformerException | IOException | SAXException | ParserConfigurationException e) {
            e.printStackTrace();
        }

        /* On utilise ce tableau pour dessiner une légère interface graphique permettant de
        visualiser quels niveaux sont terminés ainsi que quels mondes sont débloqués. */
        if (app.getTHEME() == 0)
            rendreLesNiveauxInnaccessiblesEnFonctionDeLaProgression();
        else
            rendreLesNiveauxInnaccessiblesEnFonctionDeLaProgression();

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

    public void onClickGoToNiveau(View v){
        int niveau = 0;

        switch(v.getId()){
            case R.id.boutonNiveau1:
                niveau = 1;
                break;

            case R.id.boutonNiveau2:
                niveau = 2;
                break;

            case R.id.boutonNiveau3:
                niveau = 3;
                break;

            case R.id.boutonNiveau4:
                niveau = 4;
                break;

            case R.id.boutonNiveau5:
                niveau = 5;
                break;

            case R.id.boutonNiveau6:
                niveau = 6;
                break;

            case R.id.boutonNiveau7:
                niveau = 7;
                break;

            case R.id.boutonNiveau8:
                niveau = 8;
                break;

            case R.id.boutonNiveau9:
                niveau = 9;
                break;

            case R.id.boutonNiveau10:
                niveau = 10;
                break;

            case R.id.boutonNiveau11:
                niveau = 11;
                break;

            case R.id.boutonNiveau12:
                niveau = 12;
                break;

            case R.id.boutonNiveau13:
                niveau = 13;
                break;

            case R.id.boutonNiveau14:
                niveau = 14;
                break;

            case R.id.boutonNiveau15:
                niveau = 15;
                break;

            case R.id.boutonNiveau16:
                niveau = 16;
                break;

            case R.id.boutonNiveau17:
                niveau = 17;
                break;

            case R.id.boutonNiveau18:
                niveau = 18;
                break;

            case R.id.boutonNiveau19:
                niveau = 19;
                break;

            case R.id.boutonNiveau20:
                niveau = 20;
                break;

            case R.id.boutonNiveau21:
                niveau = 21;
                break;

            case R.id.boutonNiveau22:
                niveau = 22;
                break;

            case R.id.boutonNiveau23:
                niveau = 23;
                break;

            case R.id.boutonNiveau24:
                niveau = 24;
                break;

            case R.id.boutonNiveau25:
                niveau = 25;
                break;

            case R.id.boutonNiveau26:
                niveau = 26;
                break;

            case R.id.boutonNiveau27:
                niveau = 27;
                break;

            case R.id.boutonNiveau28:
                niveau = 28;
                break;

            case R.id.boutonNiveau29:
                niveau = 29;
                break;

            case R.id.boutonNiveau30:
                niveau = 30;
                break;

            case R.id.boutonNiveau31:
                niveau = 31;
                break;

            case R.id.boutonNiveau32:
                niveau = 32;
                break;

            case R.id.boutonNiveau33:
                niveau = 33;
                break;

            case R.id.boutonNiveau34:
                niveau = 34;
                break;

            case R.id.boutonNiveau35:
                niveau = 35;
                break;

            case R.id.boutonNiveau36:
                niveau = 36;
                break;

            case R.id.boutonNiveau37:
                niveau = 37;
                break;

            case R.id.boutonNiveau38:
                niveau = 38;
                break;

            case R.id.boutonNiveau39:
                niveau = 39;
                break;

            case R.id.boutonNiveau40:
                niveau = 40;
                break;

            case R.id.boutonNiveau41:
                niveau = 41;
                break;

            case R.id.boutonNiveau42:
                niveau = 42;
                break;

            case R.id.boutonNiveau43:
                niveau = 43;
                break;

            case R.id.boutonNiveau44:
                niveau = 44;
                break;

            case R.id.boutonNiveau45:
                niveau = 45;
                break;

            case R.id.boutonNiveau46:
                niveau = 46;
                break;

            case R.id.boutonNiveau47:
                niveau = 47;
                break;

            case R.id.boutonNiveau48:
                niveau = 48;
                break;

            case R.id.boutonNiveau49:
                niveau = 49;
                break;

            case R.id.boutonNiveau50:
                niveau = 50;
                break;

            case R.id.boutonNiveau51:
                niveau = 51;
                break;

            case R.id.boutonNiveau52:
                niveau = 52;
                break;

            case R.id.boutonNiveau53:
                niveau = 53;
                break;

            case R.id.boutonNiveau54:
                niveau = 54;
                break;

            case R.id.boutonNiveau55:
                niveau = 55;
                break;

            case R.id.boutonNiveau56:
                niveau = 56;
                break;

            case R.id.boutonNiveau57:
                niveau = 57;
                break;

            case R.id.boutonNiveau58:
                niveau = 58;
                break;

            case R.id.boutonNiveau59:
                niveau = 59;
                break;

            case R.id.boutonNiveau60:
                niveau = 60;
                break;

            case R.id.boutonNiveau61:
                niveau = 61;
                break;

            case R.id.boutonNiveau62:
                niveau = 62;
                break;

            case R.id.boutonNiveau63:
                niveau = 63;
                break;

            case R.id.boutonNiveau64:
                niveau = 64;
                break;

            default:
                // On charge le tutoriel 0
                break;
        }

        Intent intent = new Intent(this, Jeu.class);

        /* AJout de la clef "niveau" qui se trouvera dans le contexte de
           l'activité Jeu, afin de savoir quel niveau le joueur a selectionné.*/
        Bundle b = new Bundle();
        b.putInt("niveau", niveau);
        intent.putExtras(b);

        startActivity(intent);
        finish();
    }

    private boolean[] tableauDesNiveauxTermines() throws TransformerException, IOException, SAXException, ParserConfigurationException {
       boolean[] tableauDesNiveauxTermines = new boolean[65];

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
        // Obtention de la liste des noeuds de informationNiveaux
        NodeList listeElementNiveauCharge = elementInformationsNiveaux.getChildNodes();

        // On parcourt les 64 noeuds de la liste en remplissant notre tableaux au fur et à mesure
        int compteurDeNiveaux = 0;
        int i = 0;
        while (compteurDeNiveaux != 65) {
            Node itemElement = listeElementNiveauCharge.item(i);
            i++;
            if (itemElement.getNodeType() != Node.ELEMENT_NODE)
                continue;

            NamedNodeMap attribusItem = itemElement.getAttributes();
            String recordCoups = ((Attr) attribusItem.getNamedItem("recordCoups")).getValue();

            if (!recordCoups.equals("-1"))
                tableauDesNiveauxTermines[compteurDeNiveaux] = true;
            else
                tableauDesNiveauxTermines[compteurDeNiveaux] = false;
            compteurDeNiveaux++;
        }

        // Enfin on retourne notre tableau
        return tableauDesNiveauxTermines;
    }

    private void rendreLesNiveauxInnaccessiblesEnFonctionDeLaProgression(){
        // Si le monde 1 n'est pas terminé, on bloque tous les autres mondes
        for (int i=1; i<=8; i++){
            // Si un des niveaux du monde 1 n'est pas fini, on bloque les mondes 2, 3, 4, 5, 6, 7 et 8
            if (!(tableauNiveauxTermines[i])){
                // On récupère les layout
                LinearLayout layoutMonde2 = (LinearLayout) findViewById(R.id.layoutMonde2);
                LinearLayout layoutMonde3 = (LinearLayout) findViewById(R.id.layoutMonde3);
                LinearLayout layoutMonde4 = (LinearLayout) findViewById(R.id.layoutMonde4);
                LinearLayout layoutMonde5 = (LinearLayout) findViewById(R.id.layoutMonde5);
                LinearLayout layoutMonde6 = (LinearLayout) findViewById(R.id.layoutMonde6);
                LinearLayout layoutMonde7 = (LinearLayout) findViewById(R.id.layoutMonde7);
                LinearLayout layoutMonde8 = (LinearLayout) findViewById(R.id.layoutMonde8);

                // On rend les layout intouchables
                setIntouchable(layoutMonde2);
                setIntouchable(layoutMonde3);
                setIntouchable(layoutMonde4);
                setIntouchable(layoutMonde5);
                setIntouchable(layoutMonde6);
                setIntouchable(layoutMonde7);
                setIntouchable(layoutMonde8);

                // On affiche des petits checks sur tous les boutons des niveaux terminés
                afficheCheckSiNiveauTermine(1);

                return;
            }
        }

        // Si le monde 2 n'est pas terminé, on bloque tous les autres mondes suivants
        for (int i=9; i<=16; i++){
            // Si un des niveaux du monde 2 n'est pas fini, on bloque les mondes 3, 4, 5, 6, 7 et 8
            if (!(tableauNiveauxTermines[i])){
                // On récupère les layout
                LinearLayout layoutMonde3 = (LinearLayout) findViewById(R.id.layoutMonde3);
                LinearLayout layoutMonde4 = (LinearLayout) findViewById(R.id.layoutMonde4);
                LinearLayout layoutMonde5 = (LinearLayout) findViewById(R.id.layoutMonde5);
                LinearLayout layoutMonde6 = (LinearLayout) findViewById(R.id.layoutMonde6);
                LinearLayout layoutMonde7 = (LinearLayout) findViewById(R.id.layoutMonde7);
                LinearLayout layoutMonde8 = (LinearLayout) findViewById(R.id.layoutMonde8);

                // On rend les layout intouchables
                setIntouchable(layoutMonde3);
                setIntouchable(layoutMonde4);
                setIntouchable(layoutMonde5);
                setIntouchable(layoutMonde6);
                setIntouchable(layoutMonde7);
                setIntouchable(layoutMonde8);

                // On affiche des petits checks sur tous les boutons des niveaux terminés
                afficheCheckSiNiveauTermine(2);

                return;
            }
        }

        // Si le monde 3 n'est pas terminé, on bloque les autres mondes
        for (int i=17; i<=24; i++){
            // Si un des niveaux du monde 3 n'est pas fini, on bloque les mondes 4, 5, 6, 7 et 8
            if (!(tableauNiveauxTermines[i])){
                // On récupère les layout
                LinearLayout layoutMonde4 = (LinearLayout) findViewById(R.id.layoutMonde4);
                LinearLayout layoutMonde5 = (LinearLayout) findViewById(R.id.layoutMonde5);
                LinearLayout layoutMonde6 = (LinearLayout) findViewById(R.id.layoutMonde6);
                LinearLayout layoutMonde7 = (LinearLayout) findViewById(R.id.layoutMonde7);
                LinearLayout layoutMonde8 = (LinearLayout) findViewById(R.id.layoutMonde8);

                // On rend les layout intouchable
                setIntouchable(layoutMonde4);
                setIntouchable(layoutMonde5);
                setIntouchable(layoutMonde6);
                setIntouchable(layoutMonde7);
                setIntouchable(layoutMonde8);

                // On affiche des petits checks sur tous les boutons des niveaux terminés
                afficheCheckSiNiveauTermine(3);

                return;
            }
        }
        // Si le monde 4 n'est pas terminé, on bloque les autres mondes
        for (int i=25; i<=32; i++){
            // Si un des niveaux du monde 4 n'est pas fini, on bloque les mondes 5, 6, 7 et 8
            if (!(tableauNiveauxTermines[i])){
                // On récupère les layout
                LinearLayout layoutMonde5 = (LinearLayout) findViewById(R.id.layoutMonde5);
                LinearLayout layoutMonde6 = (LinearLayout) findViewById(R.id.layoutMonde6);
                LinearLayout layoutMonde7 = (LinearLayout) findViewById(R.id.layoutMonde7);
                LinearLayout layoutMonde8 = (LinearLayout) findViewById(R.id.layoutMonde8);

                // On rend les layout intouchable
                setIntouchable(layoutMonde5);
                setIntouchable(layoutMonde6);
                setIntouchable(layoutMonde7);
                setIntouchable(layoutMonde8);

                // On affiche des petits checks sur tous les boutons des niveaux terminés
                afficheCheckSiNiveauTermine(4);

                return;
            }
        }
        // Si le monde 5 n'est pas terminé, on bloque les autres mondes
        for (int i=33; i<=40; i++){
            // Si un des niveaux du monde 5 n'est pas fini, on bloque les mondes 6, 7 et 8
            if (!(tableauNiveauxTermines[i])){
                // On récupère les layout
                LinearLayout layoutMonde6 = (LinearLayout) findViewById(R.id.layoutMonde6);
                LinearLayout layoutMonde7 = (LinearLayout) findViewById(R.id.layoutMonde7);
                LinearLayout layoutMonde8 = (LinearLayout) findViewById(R.id.layoutMonde8);

                // On rend les layout intouchable
                setIntouchable(layoutMonde6);
                setIntouchable(layoutMonde7);
                setIntouchable(layoutMonde8);

                // On affiche des petits checks sur tous les boutons des niveaux terminés
                afficheCheckSiNiveauTermine(5);

                return;
            }
        }
        // Si le monde 6 n'est pas terminé, on bloque les autres mondes
        for (int i=41; i<=48; i++){
            // Si un des niveaux du monde 6 n'est pas fini, on bloque les mondes 7 et 8
            if (!(tableauNiveauxTermines[i])){
                // On récupère les layout
                LinearLayout layoutMonde7 = (LinearLayout) findViewById(R.id.layoutMonde7);
                LinearLayout layoutMonde8 = (LinearLayout) findViewById(R.id.layoutMonde8);

                // On rend les layout intouchable
                setIntouchable(layoutMonde7);
                setIntouchable(layoutMonde8);

                // On affiche des petits checks sur tous les boutons des niveaux terminés
                afficheCheckSiNiveauTermine(6);

                return;
            }
        }
        // Si le monde 7 n'est pas terminé, on bloque le monde 8
        for (int i=17; i<=24; i++){
            // Si un des niveaux du monde 7 n'est pas fini, on bloque le monde 8
            if (!(tableauNiveauxTermines[i])){
                // On récupère le layout
                LinearLayout layoutMonde8 = (LinearLayout) findViewById(R.id.layoutMonde8);

                // On rend les layout intouchable
                setIntouchable(layoutMonde8);

                // On affiche des petits checks sur tous les boutons des niveaux terminés
                afficheCheckSiNiveauTermine(7);

                return;
            }
        }

        // On affiche des petits checks sur tous les boutons des niveaux terminés
        afficheCheckSiNiveauTermine(8);
    }

    private void afficheCheckSiNiveauTermine(int mondeMax){
        int backgroundID = ((app.getTHEME() == 0)?R.drawable.bouton_dark_fini:R.drawable.bouton_light_fini);
        int tableauIDBoutons[] = new int[0];
        switch (mondeMax){
            case 1:
                tableauIDBoutons = new int[]{R.id.boutonNiveau1, R.id.boutonNiveau2,
                        R.id.boutonNiveau3, R.id.boutonNiveau4, R.id.boutonNiveau5,
                        R.id.boutonNiveau6, R.id.boutonNiveau7, R.id.boutonNiveau8};
                break;

            case 2:
                tableauIDBoutons = new int[]{R.id.boutonNiveau1, R.id.boutonNiveau2,
                        R.id.boutonNiveau3, R.id.boutonNiveau4, R.id.boutonNiveau5,
                        R.id.boutonNiveau6, R.id.boutonNiveau7, R.id.boutonNiveau8,
                        R.id.boutonNiveau9, R.id.boutonNiveau10, R.id.boutonNiveau11,
                        R.id.boutonNiveau12, R.id.boutonNiveau13, R.id.boutonNiveau14,
                        R.id.boutonNiveau15, R.id.boutonNiveau16};
                break;
            case 3:
                tableauIDBoutons = new int[]{R.id.boutonNiveau1, R.id.boutonNiveau2,
                        R.id.boutonNiveau3, R.id.boutonNiveau4, R.id.boutonNiveau5,
                        R.id.boutonNiveau6, R.id.boutonNiveau7, R.id.boutonNiveau8,
                        R.id.boutonNiveau9, R.id.boutonNiveau10, R.id.boutonNiveau11,
                        R.id.boutonNiveau12, R.id.boutonNiveau13, R.id.boutonNiveau14,
                        R.id.boutonNiveau15, R.id.boutonNiveau16, R.id.boutonNiveau17,
                        R.id.boutonNiveau18, R.id.boutonNiveau19, R.id.boutonNiveau20,
                        R.id.boutonNiveau21, R.id.boutonNiveau22, R.id.boutonNiveau23,
                        R.id.boutonNiveau24};
                break;
            case 4:
                tableauIDBoutons = new int[]{R.id.boutonNiveau1, R.id.boutonNiveau2,
                        R.id.boutonNiveau3, R.id.boutonNiveau4, R.id.boutonNiveau5,
                        R.id.boutonNiveau6, R.id.boutonNiveau7, R.id.boutonNiveau8,
                        R.id.boutonNiveau9, R.id.boutonNiveau10, R.id.boutonNiveau11,
                        R.id.boutonNiveau12, R.id.boutonNiveau13, R.id.boutonNiveau14,
                        R.id.boutonNiveau15, R.id.boutonNiveau16, R.id.boutonNiveau17,
                        R.id.boutonNiveau18, R.id.boutonNiveau19, R.id.boutonNiveau20,
                        R.id.boutonNiveau21, R.id.boutonNiveau22, R.id.boutonNiveau23,
                        R.id.boutonNiveau24, R.id.boutonNiveau25, R.id.boutonNiveau26,
                        R.id.boutonNiveau27, R.id.boutonNiveau28, R.id.boutonNiveau29,
                        R.id.boutonNiveau30, R.id.boutonNiveau31, R.id.boutonNiveau32};
            case 5:
                tableauIDBoutons = new int[]{R.id.boutonNiveau1, R.id.boutonNiveau2,
                        R.id.boutonNiveau3, R.id.boutonNiveau4, R.id.boutonNiveau5,
                        R.id.boutonNiveau6, R.id.boutonNiveau7, R.id.boutonNiveau8,
                        R.id.boutonNiveau9, R.id.boutonNiveau10, R.id.boutonNiveau11,
                        R.id.boutonNiveau12, R.id.boutonNiveau13, R.id.boutonNiveau14,
                        R.id.boutonNiveau15, R.id.boutonNiveau16, R.id.boutonNiveau17,
                        R.id.boutonNiveau18, R.id.boutonNiveau19, R.id.boutonNiveau20,
                        R.id.boutonNiveau21, R.id.boutonNiveau22, R.id.boutonNiveau23,
                        R.id.boutonNiveau24, R.id.boutonNiveau25, R.id.boutonNiveau26,
                        R.id.boutonNiveau27, R.id.boutonNiveau28, R.id.boutonNiveau29,
                        R.id.boutonNiveau30, R.id.boutonNiveau31, R.id.boutonNiveau32,
                        R.id.boutonNiveau33, R.id.boutonNiveau34, R.id.boutonNiveau35,
                        R.id.boutonNiveau36, R.id.boutonNiveau37, R.id.boutonNiveau38,
                        R.id.boutonNiveau39, R.id.boutonNiveau40};
            case 6:
                tableauIDBoutons = new int[]{R.id.boutonNiveau1, R.id.boutonNiveau2,
                        R.id.boutonNiveau3, R.id.boutonNiveau4, R.id.boutonNiveau5,
                        R.id.boutonNiveau6, R.id.boutonNiveau7, R.id.boutonNiveau8,
                        R.id.boutonNiveau9, R.id.boutonNiveau10, R.id.boutonNiveau11,
                        R.id.boutonNiveau12, R.id.boutonNiveau13, R.id.boutonNiveau14,
                        R.id.boutonNiveau15, R.id.boutonNiveau16, R.id.boutonNiveau17,
                        R.id.boutonNiveau18, R.id.boutonNiveau19, R.id.boutonNiveau20,
                        R.id.boutonNiveau21, R.id.boutonNiveau22, R.id.boutonNiveau23,
                        R.id.boutonNiveau24, R.id.boutonNiveau25, R.id.boutonNiveau26,
                        R.id.boutonNiveau27, R.id.boutonNiveau28, R.id.boutonNiveau29,
                        R.id.boutonNiveau30, R.id.boutonNiveau31, R.id.boutonNiveau32,
                        R.id.boutonNiveau33, R.id.boutonNiveau34, R.id.boutonNiveau35,
                        R.id.boutonNiveau36, R.id.boutonNiveau37, R.id.boutonNiveau38,
                        R.id.boutonNiveau39, R.id.boutonNiveau40, R.id.boutonNiveau41,
                        R.id.boutonNiveau42, R.id.boutonNiveau43, R.id.boutonNiveau44,
                        R.id.boutonNiveau45, R.id.boutonNiveau46, R.id.boutonNiveau47,
                        R.id.boutonNiveau48};
            case 7:
                tableauIDBoutons = new int[]{R.id.boutonNiveau1, R.id.boutonNiveau2,
                        R.id.boutonNiveau3, R.id.boutonNiveau4, R.id.boutonNiveau5,
                        R.id.boutonNiveau6, R.id.boutonNiveau7, R.id.boutonNiveau8,
                        R.id.boutonNiveau9, R.id.boutonNiveau10, R.id.boutonNiveau11,
                        R.id.boutonNiveau12, R.id.boutonNiveau13, R.id.boutonNiveau14,
                        R.id.boutonNiveau15, R.id.boutonNiveau16, R.id.boutonNiveau17,
                        R.id.boutonNiveau18, R.id.boutonNiveau19, R.id.boutonNiveau20,
                        R.id.boutonNiveau21, R.id.boutonNiveau22, R.id.boutonNiveau23,
                        R.id.boutonNiveau24, R.id.boutonNiveau25, R.id.boutonNiveau26,
                        R.id.boutonNiveau27, R.id.boutonNiveau28, R.id.boutonNiveau29,
                        R.id.boutonNiveau30, R.id.boutonNiveau31, R.id.boutonNiveau32,
                        R.id.boutonNiveau33, R.id.boutonNiveau34, R.id.boutonNiveau35,
                        R.id.boutonNiveau36, R.id.boutonNiveau37, R.id.boutonNiveau38,
                        R.id.boutonNiveau39, R.id.boutonNiveau40, R.id.boutonNiveau41,
                        R.id.boutonNiveau42, R.id.boutonNiveau43, R.id.boutonNiveau44,
                        R.id.boutonNiveau45, R.id.boutonNiveau46, R.id.boutonNiveau47,
                        R.id.boutonNiveau48, R.id.boutonNiveau49, R.id.boutonNiveau50,
                        R.id.boutonNiveau51, R.id.boutonNiveau52, R.id.boutonNiveau53,
                        R.id.boutonNiveau54, R.id.boutonNiveau55, R.id.boutonNiveau56};
            case 8:
                tableauIDBoutons = new int[]{R.id.boutonNiveau1, R.id.boutonNiveau2,
                        R.id.boutonNiveau3, R.id.boutonNiveau4, R.id.boutonNiveau5,
                        R.id.boutonNiveau6, R.id.boutonNiveau7, R.id.boutonNiveau8,
                        R.id.boutonNiveau9, R.id.boutonNiveau10, R.id.boutonNiveau11,
                        R.id.boutonNiveau12, R.id.boutonNiveau13, R.id.boutonNiveau14,
                        R.id.boutonNiveau15, R.id.boutonNiveau16, R.id.boutonNiveau17,
                        R.id.boutonNiveau18, R.id.boutonNiveau19, R.id.boutonNiveau20,
                        R.id.boutonNiveau21, R.id.boutonNiveau22, R.id.boutonNiveau23,
                        R.id.boutonNiveau24, R.id.boutonNiveau25, R.id.boutonNiveau26,
                        R.id.boutonNiveau27, R.id.boutonNiveau28, R.id.boutonNiveau29,
                        R.id.boutonNiveau30, R.id.boutonNiveau31, R.id.boutonNiveau32,
                        R.id.boutonNiveau33, R.id.boutonNiveau34, R.id.boutonNiveau35,
                        R.id.boutonNiveau36, R.id.boutonNiveau37, R.id.boutonNiveau38,
                        R.id.boutonNiveau39, R.id.boutonNiveau40, R.id.boutonNiveau41,
                        R.id.boutonNiveau42, R.id.boutonNiveau43, R.id.boutonNiveau44,
                        R.id.boutonNiveau45, R.id.boutonNiveau46, R.id.boutonNiveau47,
                        R.id.boutonNiveau48, R.id.boutonNiveau49, R.id.boutonNiveau50,
                        R.id.boutonNiveau51, R.id.boutonNiveau52, R.id.boutonNiveau53,
                        R.id.boutonNiveau54, R.id.boutonNiveau55, R.id.boutonNiveau56,
                        R.id.boutonNiveau57, R.id.boutonNiveau58, R.id.boutonNiveau59,
                        R.id.boutonNiveau60, R.id.boutonNiveau61, R.id.boutonNiveau62,
                        R.id.boutonNiveau63, R.id.boutonNiveau64};
        }

        int i;
        for (i=0; i<(mondeMax*8); i++){
            // Si le niveau n'est pas fini, on saute une iteration
            if (!(tableauNiveauxTermines[i+1]))
                continue;

            // Sinon, on change dynamiquement le background du bouton correspondant
            View bouton = findViewById(tableauIDBoutons[i]);
            bouton.setBackgroundResource(backgroundID);
        }
    }

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