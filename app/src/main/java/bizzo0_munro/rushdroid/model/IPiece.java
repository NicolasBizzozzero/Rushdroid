package bizzo0_munro.rushdroid.model;

public interface IPiece {

	/********************************************
     * Retourne la valeur de l'ID de l'instance *
     ********************************************/
    int getId();
    
    /****************************************************
     * Retourne la valeur de la direction de l'instance *
     ****************************************************/
    Direction getOrientation();
    
    /*************************************************
     * Retourne la valeur de la taille de l'instance *
     *************************************************/
    int getSize();
    
    /***************************************************
     * Retourne la valeur de la position de l'instance *
     ***************************************************/
    Position getPos();
    
    /*******************************************************************
     * Affecte la valeur pos à l'attribut de la position de l'instance *
     *******************************************************************/
    void setPos(Position pos);
    
}
