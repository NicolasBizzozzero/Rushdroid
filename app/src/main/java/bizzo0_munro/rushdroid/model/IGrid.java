package bizzo0_munro.rushdroid.model;

public interface IGrid {
	
	/*************************************************************
     * Retourne true si la case en position pos ne contient rien *
     *************************************************************/
    boolean isEmpty(Position pos);
    
    /*************************************************
     * Retourne la valeur contenue à la position pos *
     *************************************************/
    Integer get(Position pos);
    
    /********************************************
     * Affecte la valeur id à l'emplacement pos *
     ********************************************/
    void set(Position pos, int id);
    
    /**************************************************************
     * Retire la valeur contenue à la position pos de l'instance. *
     * La case à cette position sera alors vide.				  *
     **************************************************************/
    void unset(Position pos);
    
}
