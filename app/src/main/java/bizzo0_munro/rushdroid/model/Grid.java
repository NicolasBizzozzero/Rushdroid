package bizzo0_munro.rushdroid.model;

public class Grid implements IGrid{
	private Integer[][] grille;
	private int nbColonnes, nbLignes;

	public Grid(){
		grille = new Integer[6][6];
		for (int i = 0; i<6; i++){
			for (int j = 0; j<6; j++)
				grille[i][j] = null;
		}
		
		nbColonnes = 6;
		nbLignes = 6;
	}	
	
    public boolean isEmpty(Position pos) {
		// On vérifie que la position qu'on traite ne sort pas des limites du terrain
		int col = pos.getCol();
		int lig = pos.getLig();

		return (col >= nbColonnes) || (col < 0) || (lig >= nbLignes) || (lig < 0) || (grille[col][lig] == null);
	}
    
    public Integer get(Position pos){
    	// On vérifie que la position qu'on traite ne sort pas des limites du terrain
    	int col = pos.getCol();
    	int lig = pos.getLig();
    	if ((col < 0) || (col >= nbColonnes))
    		return null;
    	if ((lig < 0) || (lig >= nbLignes))
    		return null;
    	
		return grille[col][lig];
    }
    
    public void set(Position pos, int id){
    	// On vérifie que la position qu'on traite ne sort pas des limites du terrain
    	int col = pos.getCol();
    	int lig = pos.getLig();
    	if ((col < 0) || (col >= nbColonnes))
    		return;
    	if ((lig < 0) || (lig >= nbLignes))
    		return;
    	
    	grille[col][lig] = id;
    }
    
    public void unset(Position pos){
    	// On vérifie que la position qu'on traite ne sort pas des limites du terrain
    	int col = pos.getCol();
    	int lig = pos.getLig();
    	if ((col >= nbColonnes) || (col < 0))
    		return;
    	if ((lig >= nbLignes) || (lig < 0))
    		return;
    	
    	grille[col][lig] = null;
    }

	public int getNbColonnes(){
		return nbColonnes;
	}

	public int getNbLignes(){
		return nbLignes;
	}
}