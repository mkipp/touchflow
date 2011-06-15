package module.implementation.input.stantum;

/**
 * @author kipp
 */
public interface MTController {
    public MTPoint getPoint(int id);
    public int getNumPoints();
    public MTPoint getNeighbor(int id);
}
