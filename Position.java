package csse2002.block.world;

import java.util.Objects;

/**
 * Represents the position of a Tile in the SparseTileArray
 */
public class Position {
    //Position X
    private int x;
    //Position Y
    private int y;

    /**
     * Construct a position for (x, y)
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Get the x coordinate
     * @return the x coordinate
     */
    public int getX() {
        return this.x;
    }

    /**
    * Get the y coordinate
    * @return the y coordinate
    */
    public int getY() {
        return this.y;
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     * @param obj the object to compare to
     * @return true if obj is an instance of Position
     * and if obj.x == x and obj.y == y.
     */
    @Override
    public boolean equals(java.lang.Object obj) {
        //null check
        if (obj == null) {
            return false;
        }

        //this instance check
        if (this == obj){
            return true;
        }

        //getX and getY check
        if (this.getX() == ((Position) obj).getX() && this.getY()
                == ((Position) obj).getY()) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * Compute a hashCode that meets the contract of Object.hashCode
     * @return a suitable hashcode for the Position
     */
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    /**
     * Compare this position to another position.
     * @param other the other Position to compare to
     * @return -1, 0, or 1 depending on conditions above
     */
    public int compareTo(Position other) {
        if (this.getX() < other.getX()) {
            return -1;
        }
        else if (this.getX() == other.getX() && this.getY() < other.getY()) {
            return -1;
        }
        else if (this.getX() == other.getX() && this.getY() == other.getY()) {
            return 0;
        }
        else if (this.getX() > other.getX()) {
            return 1;
        }
        else {
            return 1;
        }

    }

    /**
     * Convert this position to a string.
     * @return a string representation of the position "(<x>, <y>)"
     */
    @Override
    public String toString() {
        return "(<" + x + ">, <" + y + ">)";
    }

}
