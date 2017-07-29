package bizzo0_munro.rushdroid.model;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import bizzo0_munro.rushdroid.ClassePrincipale;

public class Model implements IModel {
	private Grid grille;
	private ArrayList<Piece> listePieces;
	private String monde;
	private String recordCoups;
	private String recordTemps;

	public Model(int puzzleNumber, ClassePrincipale app) throws ParserConfigurationException, SAXException, IOException, WhereTheFuckIsThePuzzleException {
		// Création de la grille et de la liste des pièces
		grille = new Grid();
		listePieces = new ArrayList<>();

		// Construction du model de jeu précisé dans l'enoncé
		// Lecture et parsage du document
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		InputStream cheminDuFichier = app.getAssets().open("rushpuzzles.xml");
		Document doc = db.parse(cheminDuFichier);
		// Obtention de l'element racine du document
		Element racine = doc.getDocumentElement();
		// Obtention de la liste des puzzles du document
		NodeList listePuzzles = racine.getChildNodes();

		// On parcourt la listeDesPuzzles et on s'arrete dès qu'on trouve le bon puzzle
		Node leBonPuzzle = null;
		for (int i = 0; i < listePuzzles.getLength(); i++) {
			Node itemPuzzle = listePuzzles.item(i);
			if (itemPuzzle.getNodeType() != Node.ELEMENT_NODE)
				continue;

			NamedNodeMap attribusItem = itemPuzzle.getAttributes();
			String nomDeLItem = ((Attr) attribusItem.getNamedItem("name")).getValue();

			if (nomDeLItem.equals("pb" + puzzleNumber)) {
				// Obtention du puzzle passé en argument
				leBonPuzzle = listePuzzles.item(i);
				break;
			}
		}

		// Cas où le puzzle demandé n'existe pas dans la liste
		if (leBonPuzzle == null) {
			throw new WhereTheFuckIsThePuzzleException("Le puzzle numéro " + puzzleNumber + " n'existe pas.");
		}

		// Obtention du numero du monde ainsi que des records
		NamedNodeMap listeAttributsPuzzle = leBonPuzzle.getAttributes();
		monde = ((Attr) listeAttributsPuzzle.getNamedItem("monde")).getValue();

		// Obtention de la liste des pieces du puzzle
		NodeList listeVehicules = leBonPuzzle.getChildNodes();
		int nbDePieces = listeVehicules.getLength();

		// Association de tous les attributs à chaque pièce
		for (int j = 0; j < nbDePieces; j++) {
			Node vehicule = listeVehicules.item(j);

			if (vehicule.getNodeType() == Node.ELEMENT_NODE) {
				NamedNodeMap listeAttributs = vehicule.getAttributes();
				String attrid = ((Attr) listeAttributs.getNamedItem("id")).getValue();
				String attrsize = ((Attr) listeAttributs.getNamedItem("len")).getValue();
				String attrdir = ((Attr) listeAttributs.getNamedItem("dir")).getValue();
				String attrcol = ((Attr) listeAttributs.getNamedItem("col")).getValue();
				String attrlig = ((Attr) listeAttributs.getNamedItem("lig")).getValue();
				Piece p = new Piece(Integer.parseInt(attrid),
						Integer.parseInt(attrsize),
						(attrdir.equals("H") ? Direction.HORIZONTAL : Direction.VERTICAL),
						Integer.parseInt(attrcol),
						Integer.parseInt(attrlig));
				listePieces.add(p);
			}
		}

		// Ajout des pièces sur la grille
		for (Piece p : listePieces) {
			if (p.getOrientation() == Direction.HORIZONTAL) {
				for (int i = 0; i < p.getSize(); i++)
					grille.set(new Position(p.getPos().getCol() + i, p.getPos().getLig()), p.getId());
			}
			if (p.getOrientation() == Direction.VERTICAL) {
				for (int i = 0; i < p.getSize(); i++)
					grille.set(new Position(p.getPos().getCol(), p.getPos().getLig() + i), p.getId());
			}
		}

		// Chargement des records dans le fichier des statistiques
		DocumentBuilderFactory dbf2 = DocumentBuilderFactory.newInstance();
		DocumentBuilder db2 = dbf2.newDocumentBuilder();
		FileInputStream cheminDuFichier2 = app.getBaseContext().openFileInput("statistics.xml");
		Document doc2 = db2.parse(cheminDuFichier2);
		// Obtention de l'element racine du document
		Element racine2 = doc2.getDocumentElement();
		// Obtention de l'element informationsNiveaux
		NodeList listeElementInformationsNiveaux = racine2.getElementsByTagName("informationsNiveaux");
		Node elementInformationsNiveaux = listeElementInformationsNiveaux.item(0);
		// Obtention de l'element du niveau chargé
		NodeList listeElementNiveauCharge = elementInformationsNiveaux.getChildNodes();
		Node elementNiveauCharge = null;
		for (int i = 0; i < listeElementNiveauCharge.getLength(); i++) {
			Node itemElement = listeElementNiveauCharge.item(i);
			if (itemElement.getNodeType() != Node.ELEMENT_NODE)
				continue;

			NamedNodeMap attribusItem = itemElement.getAttributes();
			String nomDeLItem = ((Attr) attribusItem.getNamedItem("name")).getValue();

			if (nomDeLItem.equals("pb" + puzzleNumber)) {
				// Obtention du puzzle passé en argument
				elementNiveauCharge = listeElementNiveauCharge.item(i);
				break;
			}
		}
		// Attribution des valeurs des records
		NamedNodeMap listeAttributs2 = elementNiveauCharge.getAttributes();
		recordCoups = ((Attr) listeAttributs2.getNamedItem("recordCoups")).getValue();
		recordTemps = ((Attr) listeAttributs2.getNamedItem("recordTemps")).getValue();
	}

	public ArrayList<Piece> getListePieces() {
		return listePieces;
	}

	public Grid getGrille() {
		return grille;
	}

	public Integer getIdByPos(Position pos) {
		return grille.get(pos);
	}

	public Direction getOrientation(int id) {
		for (Piece p : listePieces) {
			if (id == p.getId())
				return p.getOrientation();
		}

		// Cas où l'id de la piece entrée en argument n'existe pas
		return null;
	}

	public Integer getLig(int id) {
		for (Piece p : listePieces) {
			if (id == p.getId())
				return p.getPos().getLig();
		}

		// Cas où l'id de la piece entrée en argument n'existe pas
		return null;
	}

	public Integer getCol(int id) {
		// On recherche la bonne piece dans la liste
		for (Piece p : listePieces) {
			if (id == p.getId())
				return p.getPos().getCol();
		}

		// Cas où l'id de la piece entrée en argument n'existe pas
		return null;
	}

	public boolean endOfGame() {
		// On recherche la bonne piece dans la liste
		for (Piece p : listePieces) {
			// Si on a trouvé la bonne pièce
			if (p.getId() == 0) {
				// Si elle est positionnée à la sortie
				if ((p.getPos().getCol() + p.getSize() - 1 == 5) && (p.getPos().getLig() == 2)) {
					// Et si elle est bien horizontale
					if (p.getOrientation() == Direction.HORIZONTAL)
						// Alors le jeu est fini
						return true;
				}
			}
		}
		return false;
	}

	public boolean moveForward(int id) {
		// On recherche la bonne piece dans la liste
		for (Piece p : listePieces) {
			// Si on a trouvé la bonne pièce
			if (id == p.getId()) {
				Position pos = p.getPos();
				// Si cette pièce est horizontale
				if (p.getOrientation() == Direction.HORIZONTAL) {
					// Si le plateau est assez grand pour faire avancer la pièce
					if (pos.getCol() + p.getSize() - 1 < 5) {
						// Si aucune autre pièce ne se trouve devant
						if (grille.isEmpty(new Position(pos.getCol() + p.getSize(), pos.getLig()))) {
							// Alors on peut faire avancer la pièce
							grille.unset(new Position(pos.getCol(), pos.getLig()));
							p.setPos(new Position(pos.getCol() + 1, pos.getLig()));
							grille.set(new Position(pos.getCol() + p.getSize(), pos.getLig()), p.getId());
							return true;
						}
					}
				}
				// Si cette pièce est verticale
				else if (p.getOrientation() == Direction.VERTICAL) {
					// Si le plateau est assez grand pour faire avancer la pièce
					if (pos.getLig() + p.getSize() - 1 < 5) {
						// Si aucune autre pièce ne se trouve devant
						if (grille.isEmpty(new Position(pos.getCol(), pos.getLig() + p.getSize()))) {
							// Alors on peut faire avancer la pièce
							grille.unset(new Position(pos.getCol(), pos.getLig()));
							p.setPos(new Position(pos.getCol(), pos.getLig() + 1));
							grille.set(new Position(pos.getCol(), pos.getLig() + p.getSize()), p.getId());
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	public boolean moveBackward(int id) {
		// On recherche la bonne piece dans la liste
		for (Piece p : listePieces) {
			// Si on a trouvé la bonne pièce
			if (id == p.getId()) {
				Position pos = p.getPos();
				// Si cette pièce est horizontale
				if (p.getOrientation() == Direction.HORIZONTAL) {
					// Si le plateau est assez grand pour faire reculer la pièce
					if (pos.getCol() > 0) {
						// Si aucune autre pièce ne se trouve derriere
						if (grille.isEmpty(new Position(pos.getCol() - 1, pos.getLig()))) {
							// Alors on peut faire reculer la pièce
							grille.unset(new Position(pos.getCol() + p.getSize() - 1, pos.getLig()));
							p.setPos(new Position(pos.getCol() - 1, pos.getLig()));
							grille.set(new Position(pos.getCol() - 1, pos.getLig()), p.getId());
							return true;
						}
					}
				}
				// Si cette pièce est verticale
				else if (p.getOrientation() == Direction.VERTICAL) {
					// Si le plateau est assez grand pour faire reculer la pièce
					if (pos.getLig() > 0) {
						// Si aucune autre pièce ne se trouve derriere
						if (grille.isEmpty(new Position(pos.getCol(), pos.getLig() - 1))) {
							// Alors on peut faire reculer la pièce
							grille.unset(new Position(pos.getCol(), pos.getLig() + p.getSize() - 1));
							p.setPos(new Position(pos.getCol(), pos.getLig() - 1));
							grille.set(new Position(pos.getCol(), pos.getLig() - 1), p.getId());
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	public String toString() {
		String str = "";
		// Modelisation du haut de la grille
		str += '+';
		for (int x = 0; x < 6; x++)
			str += "--+";

		// Remplissage du centre de la grille
		for (int y = 0; y < 6; y++) {
			str += '\n';
			for (int x = 0; x < 6; x++) {
				str += "|";
				if (grille.isEmpty(new Position(x, y)))
					str += "  ";
				else
					str += "0" + grille.get(new Position(x, y));
			}
			str += "\n+";
			for (int x = 0; x < 6; x++)
				str += "--+";
		}

		return str;
	}

	public Piece getPieceById(int id) {
		for (Piece p : listePieces) {
			if (p.getId() == id)
				return p;
		}

		return null;
	}

	public String getRecordCoups() {
		return recordCoups;
	}

	public String getMonde() {
		return monde;
	}

	public String getRecordTemps() {
		return recordTemps;
	}

	public void setListePieces(ArrayList<Piece> listePieces) {
		this.listePieces = listePieces;
	}
}
