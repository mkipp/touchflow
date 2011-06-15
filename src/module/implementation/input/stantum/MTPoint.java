package module.implementation.input.stantum;

/**
 *
 * @author Michael Kipp, Frederic Raber
 */
public class MTPoint {

    public int x, y; // current point
    public int px=-1, py=-1; // previous point
    public int id=-1;

    public MTPoint(int nx, int ny) {
        x = nx;
        y = ny;
    }

    public void push(int nx, int ny) {
        px = x;
        py = y;
        x = nx;
        y = ny;
    }
    
    public String toString() {
        return "<MTPoint x=" + x + " y=" + y + " px=" + px + " py=" + py + ">";
    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
    

}
