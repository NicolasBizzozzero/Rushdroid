package bizzo0_munro.rushdroid.model;

public class Piece implements IPiece {
	private final Direction dir;
	private final int id;
	private final int size;
	private Position pos;

	public Piece(int id, int size, Direction dir, int ncol, int nlig){
		this.id = id;
		this.size = size;
		this.dir = dir;
		this.pos = new Position(ncol, nlig);
	}
	
    public int getId(){
		return id;
    }
    
    public Direction getOrientation(){
		return dir;
    }
    
    public int getSize(){
		return size;
    }
    
    public Position getPos(){
		return pos;
    }
    
    public void setPos(Position pos){
    	this.pos = pos; 
    }

	public String toString(){
		return String.format("(%d, %d)", pos.getCol(), pos.getLig());
	}

}
