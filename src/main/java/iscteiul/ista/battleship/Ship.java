/**
 *
 */
/**
 * Sou o Edu
 */
package iscteiul.ista.battleship;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Abstract representation of a ship in the Discoveries Battleship Game.
 *
 * A ship has:
 * - A category (e.g., galleao, fragata, nau, caravela, barca)
 * - A bearing (orientation on the board)
 * - An initial reference position
 * - A list of positions occupied on the board
 *
 * This class implements common behavior shared by all ship types.
 * Concrete ship types (e.g., Galleon, Frigate, Carrack, etc.)
 * extend this class and define their specific size.
 *
 * The ship tracks which of its positions have been hit
 * and provides utility methods to determine adjacency,
 * floating status and board boundaries.
 *
 * @author YourName
 */
public abstract class Ship implements IShip {

    /** Constant representing a Galleon ship type. */
    private static final String GALEAO = "galeao";

    /** Constant representing a Frigate ship type. */
    private static final String FRAGATA = "fragata";

    /** Constant representing a Carrack ship type. */
    private static final String NAU = "nau";

    /** Constant representing a Caravel ship type. */
    private static final String CARAVELA = "caravela";

    /** Constant representing a Barge ship type. */
    private static final String BARCA = "barca";

    /**
     * Factory method used to create a specific type of ship.
     *
     * @param shipKind the type of ship to create
     * @param bearing the orientation of the ship
     * @param pos the initial position of the ship
     * @return a concrete Ship instance corresponding to the given type,
     *         or null if the type is invalid
     */
    static Ship buildShip(String shipKind, Compass bearing, Position pos) {
        Ship s;
        switch (shipKind) {
            case BARCA:
                s = new Barge(bearing, pos);
                break;
            case CARAVELA:
                s = new Caravel(bearing, pos);
                break;
            case NAU:
                s = new Carrack(bearing, pos);
                break;
            case FRAGATA:
                s = new Frigate(bearing, pos);
                break;
            case GALEAO:
                s = new Galleon(bearing, pos);
                break;
            default:
                s = null;
        }
        return s;
    }

    /** Category of the ship (e.g., galleao, fragata, etc.). */
    private String category;

    /** Orientation of the ship on the board. */
    private Compass bearing;

    /** Initial reference position of the ship. */
    private IPosition pos;

    /** List of board positions occupied by the ship. */
    protected List<IPosition> positions;

    /**
     * Creates a new Ship.
     *
     * @param category the category of the ship
     * @param bearing the orientation of the ship
     * @param pos the initial position of the ship
     * @throws AssertionError if bearing or pos are null
     */
    public Ship(String category, Compass bearing, IPosition pos) {
        assert bearing != null;
        assert pos != null;

        this.category = category;
        this.bearing = bearing;
        this.pos = pos;
        positions = new ArrayList<>();
    }

    /**
     * Returns the category of the ship.
     *
     * @return the ship category
     */
    @Override
    public String getCategory() {
        return category;
    }

    /**
     * Returns the list of positions occupied by the ship.
     *
     * @return list of occupied positions
     */
    public List<IPosition> getPositions() {
        return positions;
    }

    /**
     * Returns the initial reference position of the ship.
     *
     * @return the ship's reference position
     */
    @Override
    public IPosition getPosition() {
        return pos;
    }

    /**
     * Returns the orientation (bearing) of the ship.
     *
     * @return the ship bearing
     */
    @Override
    public Compass getBearing() {
        return bearing;
    }

    /**
     * Checks whether the ship is still floating.
     *
     * A ship is considered floating if at least one of its
     * positions has not been hit.
     *
     * @return true if the ship still has unhit positions,
     *         false if it has been completely sunk
     */
    @Override
    public boolean stillFloating() {
        for (int i = 0; i < getSize(); i++)
            if (!getPositions().get(i).isHit())
                return true;
        return false;
    }

    /**
     * Returns the top-most row occupied by the ship.
     *
     * @return the smallest row index occupied by the ship
     */
    @Override
    public int getTopMostPos() {
        int top = getPositions().get(0).getRow();
        for (int i = 1; i < getSize(); i++)
            if (getPositions().get(i).getRow() < top)
                top = getPositions().get(i).getRow();
        return top;
    }

    /**
     * Returns the bottom-most row occupied by the ship.
     *
     * @return the largest row index occupied by the ship
     */
    @Override
    public int getBottomMostPos() {
        int bottom = getPositions().get(0).getRow();
        for (int i = 1; i < getSize(); i++)
            if (getPositions().get(i).getRow() > bottom)
                bottom = getPositions().get(i).getRow();
        return bottom;
    }

    /**
     * Returns the left-most column occupied by the ship.
     *
     * @return the smallest column index occupied by the ship
     */
    @Override
    public int getLeftMostPos() {
        int left = getPositions().get(0).getColumn();
        for (int i = 1; i < getSize(); i++)
            if (getPositions().get(i).getColumn() < left)
                left = getPositions().get(i).getColumn();
        return left;
    }

    /**
     * Returns the right-most column occupied by the ship.
     *
     * @return the largest column index occupied by the ship
     */
    @Override
    public int getRightMostPos() {
        int right = getPositions().get(0).getColumn();
        for (int i = 1; i < getSize(); i++)
            if (getPositions().get(i).getColumn() > right)
                right = getPositions().get(i).getColumn();
        return right;
    }

    /**
     * Determines whether the ship occupies a given position.
     *
     * @param pos the position to check
     * @return true if the ship occupies that position,
     *         false otherwise
     */
    @Override
    public boolean occupies(IPosition pos) {
        assert pos != null;

        for (int i = 0; i < getSize(); i++)
            if (getPositions().get(i).equals(pos))
                return true;
        return false;
    }

    /**
     * Determines whether this ship is too close to another ship.
     *
     * Ships are considered too close if any of their positions
     * are adjacent.
     *
     * @param other the other ship
     * @return true if the ships are adjacent, false otherwise
     */
    @Override
    public boolean tooCloseTo(IShip other) {
        assert other != null;

        Iterator<IPosition> otherPos = other.getPositions().iterator();
        while (otherPos.hasNext())
            if (tooCloseTo(otherPos.next()))
                return true;

        return false;
    }

    /**
     * Determines whether this ship is adjacent to a given position.
     *
     * @param pos the position to check
     * @return true if any position of this ship is adjacent to the given position
     */
    @Override
    public boolean tooCloseTo(IPosition pos) {
        for (int i = 0; i < this.getSize(); i++)
            if (getPositions().get(i).isAdjacentTo(pos))
                return true;
        return false;
    }

    /**
     * Registers a shot at a given position.
     *
     * If the position belongs to this ship, it is marked as hit.
     *
     * @param pos the position being targeted
     */
    @Override
    public void shoot(IPosition pos) {
        assert pos != null;

        for (IPosition position : getPositions()) {
            if (position.equals(pos))
                position.shoot();
        }
    }

    /**
     * Returns a string representation of the ship.
     *
     * @return string containing category, bearing and reference position
     */
    @Override
    public String toString() {
        return "[" + category + " " + bearing + " " + pos + "]";
    }
}