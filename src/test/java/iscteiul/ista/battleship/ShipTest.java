package iscteiul.ista.battleship;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for class Ship.
 * Author: raulf
 * Date: 2025-11-18 12:30
 * Cyclomatic Complexity:
 * - buildShip(): 6
 * - constructor: 1
 * - getCategory(): 1
 * - getPositions(): 1
 * - getPosition(): 1
 * - getBearing(): 1
 * - stillFloating(): 2
 * - getTopMostPos(): 2
 * - getBottomMostPos(): 2
 * - getLeftMostPos(): 2
 * - getRightMostPos(): 2
 * - occupies(): 2
 * - tooCloseTo(IShip): 2
 * - tooCloseTo(IPosition): 2
 * - shoot(): 2
 * - toString(): 1
 */
public class ShipTest {

    private Ship sut; // system under test
    private Position origin;

    @BeforeEach
    public void setup() {
        // create a concrete Ship instance (Carrack has size 3 and predictable positions)
        origin = new Position(5, 5);
        sut = new Carrack(Compass.NORTH, origin);
    }

    @AfterEach
    public void teardown() {
        sut = null;
        origin = null;
    }

    // buildShip(): CC = 6 -> buildShip1..buildShip6
    @Test
    public void buildShip1() {
        // BARCA -> returns a Barge instance
        Ship s = Ship.buildShip("barca", Compass.NORTH, origin);
        assertNotNull(s, "Error: expected buildShip('barca', ...) to return a non-null Ship instance");
        assertEquals(Barge.class, s.getClass(), "Error: expected a Barge instance for 'barca' but got " + s.getClass());
    }

    @Test
    public void buildShip2() {
        // CARAVELA -> Caravel instance
        Ship s = Ship.buildShip("caravela", Compass.NORTH, origin);
        assertNotNull(s, "Error: expected buildShip('caravela', ...) to return a non-null Ship instance");
        assertEquals(Caravel.class, s.getClass(), "Error: expected a Caravel instance for 'caravela' but got " + s.getClass());
    }

    @Test
    public void buildShip3() {
        // NAU -> Carrack instance
        Ship s = Ship.buildShip("nau", Compass.NORTH, origin);
        assertNotNull(s, "Error: expected buildShip('nau', ...) to return a non-null Ship instance");
        assertEquals(Carrack.class, s.getClass(), "Error: expected a Carrack instance for 'nau' but got " + s.getClass());
    }

    @Test
    public void buildShip4() {
        // FRAGATA -> Frigate instance
        Ship s = Ship.buildShip("fragata", Compass.NORTH, origin);
        assertNotNull(s, "Error: expected buildShip('fragata', ...) to return a non-null Ship instance");
        assertEquals(Frigate.class, s.getClass(), "Error: expected a Frigate instance for 'fragata' but got " + s.getClass());
    }

    @Test
    public void buildShip5() {
        // GALEAO -> Galleon instance
        Ship s = Ship.buildShip("galeao", Compass.NORTH, origin);
        assertNotNull(s, "Error: expected buildShip('galeao', ...) to return a non-null Ship instance");
        assertEquals(Galleon.class, s.getClass(), "Error: expected a Galleon instance for 'galeao' but got " + s.getClass());
    }

    @Test
    public void buildShip6() {
        // default -> null
        Ship s = Ship.buildShip("unknown", Compass.NORTH, origin);
        assertNull(s, "Error: expected buildShip('unknown', ...) to return null for unknown kind but got " + s);
    }

    // constructor: CC = 1
    @Test
    public void constructor() {
        // verify that the Carrack (subclass) used in setup correctly set category, bearing and position
        assertAll("Error: constructor should initialize basic fields",
                () -> assertEquals("Nau", sut.getCategory(), "Error: expected category 'Nau' but got " + sut.getCategory()),
                () -> assertEquals(Compass.NORTH, sut.getBearing(), "Error: expected bearing NORTH but got " + sut.getBearing()),
                () -> assertEquals(origin, sut.getPosition(), "Error: expected position to be the origin but got " + sut.getPosition())
        );
    }

    // getCategory(): CC = 1
    @Test
    public void getCategory() {
        String cat = sut.getCategory();
        assertNotNull(cat, "Error: expected getCategory() to return non-null category");
    }

    // getPositions(): CC = 1
    @Test
    public void getPositions() {
        assertEquals(sut.getSize().intValue(), sut.getPositions().size(), "Error: expected getPositions().size() to equal getSize()");
    }

    // getPosition(): CC = 1
    @Test
    public void getPosition() {
        assertEquals(origin, sut.getPosition(), "Error: expected getPosition() to return the origin passed to the constructor");
    }

    // getBearing(): CC = 1
    @Test
    public void getBearing() {
        assertEquals(Compass.NORTH, sut.getBearing(), "Error: expected getBearing() to return NORTH for the setup ship");
    }

    // stillFloating(): CC = 2 -> stillFloating1(), stillFloating2()
    @Test
    public void stillFloating1() {
        // Initially none of the positions are hit, so ship should still float
        assertTrue(sut.stillFloating(), "Error: expected stillFloating() to be true when no positions are hit");
    }

    @Test
    public void stillFloating2() {
        // Hit all positions and expect stillFloating() to be false
        for (IPosition p : sut.getPositions())
            p.shoot();
        assertFalse(sut.stillFloating(), "Error: expected stillFloating() to be false after all positions have been hit");
    }

    // getTopMostPos(): CC = 2 -> getTopMostPos1(), getTopMostPos2()
    @Test
    public void getTopMostPos1() {
        // For the Carrack built at origin (5,5) with NORTH bearing, topmost should be 5
        assertEquals(5, sut.getTopMostPos(), "Error: expected topmost row to be 5 for a Carrack starting at row 5");
    }

    @Test
    public void getTopMostPos2() {
        // Create a custom ship where first position is not the topmost to exercise the branch
        Ship custom = new Ship("test", Compass.NORTH, new Position(10, 2)) {
            @Override
            public Integer getSize() {
                return getPositions().size();
            }
        };
        // add positions with the first having larger row than a subsequent one
        custom.getPositions().add(new Position(10, 2));
        custom.getPositions().add(new Position(3, 2));
        assertEquals(3, custom.getTopMostPos(), "Error: expected topmost row to be updated to 3 after scanning positions");
    }

    // getBottomMostPos(): CC = 2 -> getBottomMostPos1(), getBottomMostPos2()
    @Test
    public void getBottomMostPos1() {
        // Carrack at origin rows 5,6,7 -> bottommost should be 7
        assertEquals(7, sut.getBottomMostPos(), "Error: expected bottommost row to be 7 for Carrack starting at row 5");
    }

    @Test
    public void getBottomMostPos2() {
        Ship custom = new Ship("test", Compass.NORTH, new Position(2, 2)) {
            @Override
            public Integer getSize() {
                return getPositions().size();
            }
        };
        custom.getPositions().add(new Position(2, 2));
        custom.getPositions().add(new Position(9, 2));
        assertEquals(9, custom.getBottomMostPos(), "Error: expected bottommost row to be updated to 9 after scanning positions");
    }

    // getLeftMostPos(): CC = 2 -> getLeftMostPos1(), getLeftMostPos2()
    @Test
    public void getLeftMostPos1() {
        // Carrack NORTH places at columns 5 -> leftmost should be 5
        assertEquals(5, sut.getLeftMostPos(), "Error: expected leftmost column to be 5 for Carrack starting at column 5");
    }

    @Test
    public void getLeftMostPos2() {
        Ship custom = new Ship("test", Compass.NORTH, new Position(2, 5)) {
            @Override
            public Integer getSize() {
                return getPositions().size();
            }
        };
        custom.getPositions().add(new Position(2, 10));
        custom.getPositions().add(new Position(2, 1));
        assertEquals(1, custom.getLeftMostPos(), "Error: expected leftmost column to be updated to 1 after scanning positions");
    }

    // getRightMostPos(): CC = 2 -> getRightMostPos1(), getRightMostPos2()
    @Test
    public void getRightMostPos1() {
        assertEquals(5, sut.getRightMostPos(), "Error: expected rightmost column to be 5 for Carrack starting at column 5");
    }

    @Test
    public void getRightMostPos2() {
        Ship custom = new Ship("test", Compass.NORTH, new Position(2, 2)) {
            @Override
            public Integer getSize() {
                return getPositions().size();
            }
        };
        custom.getPositions().add(new Position(2, 2));
        custom.getPositions().add(new Position(2, 20));
        assertEquals(20, custom.getRightMostPos(), "Error: expected rightmost column to be updated to 20 after scanning positions");
    }

    // occupies(): CC = 2 -> occupies1(), occupies2()
    @Test
    public void occupies1() {
        // a position known to be part of sut
        IPosition p = sut.getPositions().get(0);
        assertTrue(sut.occupies(p), "Error: expected occupies() to return true for a position that belongs to the ship");
    }

    @Test
    public void occupies2() {
        // a position not belonging to sut
        IPosition p = new Position(99, 99);
        assertFalse(sut.occupies(p), "Error: expected occupies() to return false for a far away position");
    }

    // tooCloseTo(IShip): CC = 2 -> tooCloseTo1(), tooCloseTo2()
    @Test
    public void tooCloseTo1() {
        // other ship with adjacent position -> should be true
        Ship other = new Barge(Compass.NORTH, new Position(6, 5)); // adjacent to one of sut's positions
        assertTrue(sut.tooCloseTo(other), "Error: expected tooCloseTo() to be true for adjacent ships");
    }

    @Test
    public void tooCloseTo2() {
        // other ship far away -> should be false
        Ship other = new Barge(Compass.NORTH, new Position(20, 20));
        assertFalse(sut.tooCloseTo(other), "Error: expected tooCloseTo() to be false for distant ships");
    }

    // tooCloseTo(IPosition): CC = 2 -> tooCloseToPos1(), tooCloseToPos2()
    @Test
    public void tooCloseToPos1() {
        IPosition near = new Position(6, 5);
        assertTrue(sut.tooCloseTo(near), "Error: expected tooCloseTo(IPosition) to be true for an adjacent position");
    }

    @Test
    public void tooCloseToPos2() {
        IPosition far = new Position(50, 50);
        assertFalse(sut.tooCloseTo(far), "Error: expected tooCloseTo(IPosition) to be false for a distant position");
    }

    // shoot(): CC = 2 -> shoot1(), shoot2()
    @Test
    public void shoot1() {
        // shoot a known position and assert it becomes hit
        IPosition target = sut.getPositions().get(0);
        sut.shoot(target);
        assertTrue(target.isHit(), "Error: expected target position to be hit after calling shoot()");
    }

    @Test
    public void shoot2() {
        // shoot a position not present -> no position should become hit
        IPosition target = new Position(99, 99);
        // mark all as not hit to start
        for (IPosition p : sut.getPositions()) {
            // ensure initial state
            assertFalse(p.isHit(), "Error: expected positions to start not hit");
        }
        sut.shoot(target);
        // verify no changes
        for (IPosition p : sut.getPositions()) {
            assertFalse(p.isHit(), "Error: expected no position to be hit when shooting a non-existing coordinate");
        }
    }

    // toString(): CC = 1
    @Test
    public void toStringTest() {
        String s = sut.toString();
        assertTrue(s.contains(sut.getCategory()) && s.contains(sut.getBearing().toString()), "Error: expected toString() to contain category and bearing but got: " + s);
    }

}

