package bizzo0_munro.rushdroid.model;

public interface IModel {
	
	/**********************************************************************
     * Retourne l'ID contenu à la position pos de la grille de l'instance *
     **********************************************************************/
    Integer getIdByPos(Position pos);
    
    /****************************************************************************
     * Retourne l'orientation de la pièce ayant pour ID celui passé en argument *
     ****************************************************************************/
    Direction getOrientation(int id);
    
    /****************************************************
     * Retourne la valeur de la ligne de la pièce ayant *
     * pour ID celui passé en argument					*
     ****************************************************/
    Integer getLig(int id);
    
    /****************************************************
     * Retourne la valeur de la coone de la pièce ayant *
     * pour ID celui passé en argument					*
     ****************************************************/
    Integer getCol(int id);
    
    /************************************************************
     * Retourne true si la pièce principale a atteint la sortie *
     ************************************************************/
    boolean endOfGame();
    
    /***********************************************
     * Fait avancer la pièce avec l'identifiant id *
     ***********************************************/
    boolean moveForward(int id);
    
    /***********************************************
     * Fait reculer la pièce avec l'identifiant id *
     ***********************************************/
    boolean moveBackward(int id);
}