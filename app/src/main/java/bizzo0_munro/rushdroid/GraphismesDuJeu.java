package bizzo0_munro.rushdroid;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import bizzo0_munro.rushdroid.model.Direction;
import bizzo0_munro.rushdroid.model.Grid;
import bizzo0_munro.rushdroid.model.Piece;
import bizzo0_munro.rushdroid.model.Position;
import bizzo0_munro.rushdroid.pile_de_coups.PileDeCoups;

public class GraphismesDuJeu extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder surfaceHolder;
    private Canvas canvas;
    private int hauteurCanvas;
    private int largeurCanvas;
    private int hauteurTaillePiece;
    private int largeurTaillePiece;
    private int largeurBorduresGrille;
    private int largeurBorduresPieces;
    private ClassePrincipale app;
    private Context contexteDuJeu;

    // Valeurs utilisées pour les couleurs
    private int couleurBackgroundDuCanvas;
    private int couleurGrilleDuCanvas;
    private int couleurPiecePrincipale;
    private int couleurPieces;
    private int couleurBorduresPieces;

    // Variables utilisées pour les effets sonores
    private SoundPool soundPool;
    private int identifiantSon;

    // Variables utilisées pour la gestion des MotionEvents
    private Integer xOriginal;
    private Integer yOriginal;
    private Position positionClickOriginale;
    private Piece pieceOriginale;
    private Position positionPieceOriginale;

    public GraphismesDuJeu(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);

        // Sauvegarde du contexte de l'activité jeu
        contexteDuJeu = context;

        // Chargement du contexte de l'application
        app = (ClassePrincipale)(this.getContext().getApplicationContext());

        // Initialisation des couleurs du plateau
        initialisationDesCouleursDuCanvasEnFonctionDuThemeEtDuMonde();

        // Creation du lecteur d'effets sonores
        if (app.isLeJoueurAActiveLesEffetsSonores()) {
            if (android.os.Build.VERSION.SDK_INT >= 21) {
                SoundPool.Builder soundPoolBuilder = new SoundPool.Builder().setMaxStreams(5);
                soundPoolBuilder.setAudioAttributes(new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .setLegacyStreamType(AudioManager.STREAM_MUSIC)
                        .build());
                soundPool = soundPoolBuilder.build();
            } else
                soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
            identifiantSon = soundPool.load(app.getApplicationContext(), R.raw.effet_sonore_deplacement_piece, 0);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder h){
        tryDrawing();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int pixelFormat, int largeurCanvas,
                               int hauteurCanvas) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder h) {

    }

    public void tryDrawing() {
        surfaceHolder = this.getHolder();

        // Redefinition de la taille du canvas selon l'orientation de l'appareil
        if (app.getOrientationEcran() == Configuration.ORIENTATION_PORTRAIT) {
            largeurCanvas = surfaceHolder.getSurfaceFrame().width();
            hauteurCanvas = largeurCanvas;
        } else {
            hauteurCanvas = surfaceHolder.getSurfaceFrame().height();
            largeurCanvas = hauteurCanvas;
        }

        canvas = surfaceHolder.lockCanvas();
        if (canvas != null) {
            /* Une vérification est necessaire pour éviter un leger bug d'affichage selon
               le type de l'appareil. */
            if (isUneTablette(contexteDuJeu))
                largeurBorduresGrille = 10;
            else
                largeurBorduresGrille = 9;

            largeurBorduresPieces = 3;
            hauteurTaillePiece = (int) Math.floor((hauteurCanvas-(largeurBorduresGrille *2))/app.getNombreDeLignesDeLaGrilleDuJeu());
            largeurTaillePiece = (int) Math.floor((largeurCanvas-(largeurBorduresGrille *2))/app.getNombreDeColonnesDeLaGrilleDuJeu());

            this.dessinerGrilleDuJeu();
            this.dessinerPiecesDuJeu();
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    public void dessinerGrilleDuJeu() {
        // Dessine le fond et la grille du Canvas
        canvas.drawColor(couleurGrilleDuCanvas);
        Paint paintGrilleDuCanvas = new Paint();
        paintGrilleDuCanvas.setColor(couleurBackgroundDuCanvas);
        canvas.drawRect(largeurBorduresGrille, largeurBorduresGrille, largeurCanvas - largeurBorduresGrille, hauteurCanvas - largeurBorduresGrille, paintGrilleDuCanvas);

        // Dessine la sortie du jeu à l'emplacement (5, 2) par défaut
        Paint paintBackgroundDuCanvas = new Paint();
        paintBackgroundDuCanvas.setColor(couleurBackgroundDuCanvas);
        canvas.drawRect(largeurCanvas - largeurBorduresGrille, (((app.getNombreDeLignesDeLaGrilleDuJeu() / 2) - 1) * hauteurTaillePiece) + largeurBorduresGrille, largeurCanvas - 1, (((app.getNombreDeLignesDeLaGrilleDuJeu() / 2)) * hauteurTaillePiece) + largeurBorduresGrille, paintBackgroundDuCanvas);
    }

    public void dessinerPiecesDuJeu(){
        Paint paintBorduresPieces = new Paint();
        paintBorduresPieces.setColor(couleurBorduresPieces);
        Paint paintBackgroundPieces = new Paint();
        paintBackgroundPieces.setColor(couleurPieces);

        // On parcourt la liste des pieces pour les dessiner une par une
        for (Piece p : app.getListeDePiecesCourantes()){
            int colonnePiece = p.getPos().getCol();
            int lignePiece = p.getPos().getLig();
            int taillePiece = p.getSize();
            Direction directionPiece = p.getOrientation();

            // On dessine la pièce principale (ID = 0) d'une autre couleur
            if (p.getId() == 0)
                paintBackgroundPieces.setColor(couleurPiecePrincipale);

            // On dessine les bordures de la pièce
            canvas.drawRect(largeurBorduresGrille + colonnePiece*largeurTaillePiece,
                    largeurBorduresGrille + lignePiece*hauteurTaillePiece,
                    largeurBorduresGrille + ((colonnePiece+(directionPiece == Direction.HORIZONTAL?taillePiece:1))*largeurTaillePiece),
                    largeurBorduresGrille + ((lignePiece+(directionPiece == Direction.VERTICAL?taillePiece:1))*hauteurTaillePiece),
                    paintBorduresPieces);

            // On dessine le background de la pièce
            canvas.drawRect((largeurBorduresGrille + colonnePiece*largeurTaillePiece)+largeurBorduresPieces,
                    (largeurBorduresGrille + lignePiece*hauteurTaillePiece)+largeurBorduresPieces,
                    (largeurBorduresGrille + ((colonnePiece+(directionPiece == Direction.HORIZONTAL?taillePiece:1))*largeurTaillePiece))-largeurBorduresPieces,
                    (largeurBorduresGrille + ((lignePiece+(directionPiece == Direction.VERTICAL?taillePiece:1))*hauteurTaillePiece))-largeurBorduresPieces,
                    paintBackgroundPieces);

            /* Si on a dessiné la pièce principale, on redonne la bonne couleur à la variable
               du background*/
            if (p.getId() == 0)
                paintBackgroundPieces.setColor(couleurPieces);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        // On transforme le click en position
        Position positionClick = floatToPosition(x, y);

        // On récupere l'id de la pièce sur laquelle le joueur a appuyé
        Grid grilleDuJeu = app.getModelDuJeu().getGrille();
        Integer id = grilleDuJeu.get(positionClick);

        // Gestion des différents MotionEvent
        final int action = event.getAction();
        switch(action) {
            case MotionEvent.ACTION_DOWN:
                // On regarde si l'utilisateur a appuyé sur une pièce
                if (id == null)
                    break;

                // Si l'utilisateur a bien appuyé sur une pièce, on enregistre ses coordonnées initiales
                xOriginal = (int) x;
                yOriginal = (int) y;
                positionClickOriginale = floatToPosition(x, y);
                pieceOriginale = app.getModelDuJeu().getPieceById(id);
                positionPieceOriginale = pieceOriginale.getPos();

                // On commence aussi a enregistrer son coup dans la pile de coups
                app.setListeDeCoupsEnCoursDEnregistrement(new PileDeCoups(pieceOriginale.getId()));

                break;

            case MotionEvent.ACTION_MOVE:
                boolean laPieceABouge = false;
                // On vérifie que l'utilisateur appuie toujours sur le terrain
                if ((positionClick.getCol() >= app.getNombreDeColonnesDeLaGrilleDuJeu()) &&
                        (positionClick.getLig() >= app.getNombreDeLignesDeLaGrilleDuJeu()) &&
                        (positionClick.getCol() < 0) &&
                        (positionClick.getLig() < 0)) {

                    break;
                }

                // On vérifie que l'utilisateur a bien appuyé sur une pièce au prealable
                if (pieceOriginale == null)
                    break;

                // On verifie que l'utilisateur deplace bien la pièce sur un emplacement vide
                if (id != null)
                    break;

                // Puis on déplace la pièce en prenant en compte son orientation
                if (pieceOriginale.getOrientation() == Direction.HORIZONTAL) {
                    if (positionClickOriginale.getCol() < positionClick.getCol()) {
                        laPieceABouge = app.getModelDuJeu().moveForward(pieceOriginale.getId());
                        positionClickOriginale = new Position(positionClickOriginale.getCol() + 1, positionClickOriginale.getLig());
                        app.getListeDeCoupsEnCoursDEnregistrement().ajouterCoup('b');
                    }
                    if (positionClickOriginale.getCol() > positionClick.getCol()) {
                        laPieceABouge = app.getModelDuJeu().moveBackward(pieceOriginale.getId());
                        positionClickOriginale = new Position(positionClickOriginale.getCol() - 1, positionClickOriginale.getLig());
                        app.getListeDeCoupsEnCoursDEnregistrement().ajouterCoup('f');
                    }
                } else if (pieceOriginale.getOrientation() == Direction.VERTICAL) {
                    if (positionClickOriginale.getLig() < positionClick.getLig()) {
                        laPieceABouge = app.getModelDuJeu().moveForward(pieceOriginale.getId());
                        positionClickOriginale = new Position(positionClickOriginale.getCol(), positionClickOriginale.getLig() + 1);
                        app.getListeDeCoupsEnCoursDEnregistrement().ajouterCoup('b');
                    }
                    if (positionClickOriginale.getLig() > positionClick.getLig()) {
                        laPieceABouge = app.getModelDuJeu().moveBackward(pieceOriginale.getId());
                        positionClickOriginale = new Position(positionClickOriginale.getCol(), positionClickOriginale.getLig() - 1);
                        app.getListeDeCoupsEnCoursDEnregistrement().ajouterCoup('f');
                    }
                }
                // Joue un effet sonore si la piece s'est deplacée et que le joueur a activé les effets sonores
                if (laPieceABouge) {
                    float volume = 0.075f;
                    if (app.isLeJoueurAActiveLesEffetsSonores())
                        soundPool.play(identifiantSon, volume, volume, 1, 0, 1f);
                }
                break;

            case MotionEvent.ACTION_UP:
                if ((xOriginal != null) && (yOriginal != null)){
                    /* On incremente le compteur de coups si l'utilisateur deplace la piece à une
                       position differente de sa position originale (au moment du ACTION_DOWN). */
                    if (!(pieceOriginale.getPos().equals(positionPieceOriginale))) {
                        app.setNombreDeCoupsEffectues(app.getNombreDeCoupsEffectues()+1);
                        app.getListeDeCoups().ajouterCoup(app.getListeDeCoupsEnCoursDEnregistrement());
                        app.setListeDeCoupsEnCoursDEnregistrement(null);
                    }
                }
                xOriginal = null;
                yOriginal = null;
                positionClickOriginale = null;
                pieceOriginale = null;
                positionPieceOriginale = null;

                // On regarde à chaque ACTION_UP si le jeu est terminé
                if (app.getModelDuJeu().endOfGame()) {
                    // On arrete le chrono !!!!!!!!!
                    int onArreteLeChrono = app.getTempsEcouleDepuisLeDebutDeLaPartie();

                    // On corrige un petit bug d'arrondi superieur dans le score de temps
                    onArreteLeChrono -= 1;

                    app.setOnArreteLeChrono(onArreteLeChrono);
                    Jeu jeu = (Jeu) contexteDuJeu;
                    jeu.lancementFinDuJeu();
                    return true;
                }
                break;
        }

        // On actualise l'état du canvas
        surfaceHolder.lockCanvas();
        dessinerGrilleDuJeu();
        dessinerPiecesDuJeu();
        surfaceHolder.unlockCanvasAndPost(canvas);
        return true ;
    }

    public Position floatToPosition(float x, float y){
        return new Position((int) Math.floor(x / (largeurTaillePiece)), (int) Math.floor(y / (hauteurTaillePiece)));
    }

    private void initialisationDesCouleursDuCanvasEnFonctionDuThemeEtDuMonde(){
        if (app.getTHEME() == 0) {
            // Cas où le thème est DARK
            couleurBackgroundDuCanvas = Color.BLACK;
            couleurGrilleDuCanvas = Color.WHITE;
            couleurPieces = 0xFF303030;
            couleurBorduresPieces = Color.WHITE;
        } else {
            // Cas où le thème est LIGHT
            couleurBackgroundDuCanvas = Color.WHITE;
            couleurGrilleDuCanvas = Color.BLACK;
            couleurPieces = 0xFFEFEFEF;
            couleurBorduresPieces = Color.BLACK;
        }
        switch(app.getModelDuJeu().getMonde()){
            case "1":
                // Couleur verte
                couleurPiecePrincipale = 0xFF04BD36;
                break;
            case "2":
                // Couleur cyan
                couleurPiecePrincipale = 0xFF00FFE5;
                break;
            case "3":
                // Couleur bleu
                couleurPiecePrincipale = 0xFF0931E4;
                break;
            case "4":
                // Couleur orange
                couleurPiecePrincipale = 0xFFFF6A00;
                break;
            case "5":
                // Couleur jaune
                couleurPiecePrincipale = 0xFFDAE107;
                break;
            case "6":
                // Couleur magenta
                couleurPiecePrincipale = 0xFFEA00FF;
                break;
            case "7":
                // Couleur rouge
                couleurPiecePrincipale = 0xFFF0B3B3;
                break;
            case "8":
                // Couleur citron
                couleurPiecePrincipale = 0xFF90FF00;
                break;
            default:
                couleurPiecePrincipale = 0xFFF0B3B3;
                break;
        }
    }

    private boolean isUneTablette(Context c) {
        return (c.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
}