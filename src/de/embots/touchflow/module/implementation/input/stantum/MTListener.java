package de.embots.touchflow.module.implementation.input.stantum;

/**
 * Interface for multitouch events coming from the Stantum device.
 * 
 * @author Michael Kipp
 */
public interface MTListener {
    public static final int UP = 0, DOWN = 1, MOVED = 2;
    
    public void cursorCreated(int id);
    public void cursorDestroyed(int id);
    /**
     * Triggered by three types of action: press, release and move.
     * Coordinates are given as floats between (0,1) where (0,0) is the
     * upper left corner.
     * 
     * @param id Finger Id
     * @param actionType Action type: UP, DOWN, MOVED
     * @param x X coord as float between [0, 1]
     * @param y Y coord as float between [0, 1]
     * @param clickCount
     */
    public void cursorAction(MTController controller, int id, int actionType, MTPoint point, int clickCount);
    
}
