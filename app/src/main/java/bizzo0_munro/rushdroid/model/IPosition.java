package bizzo0_munro.rushdroid.model;

public interface IPosition {

	/**************************************************
     * Retourne la valeur de la colonne de l'instance *
     **************************************************/
    int getCol();
    
    /************************************************
     * Retourne la valeur de la ligne de l'instance *
     ************************************************/
    int getLig();
    
    /*************************************************************
     * Retourne une nouvelle instance de la classe Position		 *
     * avec d colonnes de plus que l'instance passée en argument *
     *************************************************************/
    Position addCol(int d);
    
    /***********************************************************
     * Retourne une nouvelle instance de la classe Position	   *
     * avec d lignes de plus que l'instance passée en argument *
     ***********************************************************/
    Position addLig(int d);

}
