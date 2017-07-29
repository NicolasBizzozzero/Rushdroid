package bizzo0_munro.rushdroid.model;

public class Position implements IPosition{
	private int ncol;
	private int nlig;
	
	public Position(int ncol, int nlig){
		this.nlig = nlig;
		this.ncol = ncol;
	}

	public int getCol(){
    	return ncol;
    }
    
	public int getLig(){
    	return nlig;
    }
    
	public Position addCol(int d){
    	Position nouvellepos = new Position(ncol+d, nlig);
		return nouvellepos;
    }
    
	public Position addLig(int d){
    	Position nouvellepos = new Position(ncol, nlig+d);
    	return nouvellepos;
    }

	public boolean equals(Position position){
		return (this.ncol == position.getCol()) && (this.nlig == position.getLig());
	}

	public String toString(){
		return String.format("(%d, %d)", ncol, nlig);
	}
}