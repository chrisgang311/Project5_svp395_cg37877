/* CRITTERS GUI <Critter4.java>
 * EE422C Project 4b submission by
 * <Samuel Patterson>
 * <svp395>
 * <16455>
 * <Christopher Gang>
 * <cg37877>
 * <16450>
 * Slip days used: <0>
 * Fall 2016
 */

package assignment5;

import assignment5.Critter.CritterShape;

/**
 * Critter 4 can walk either North or South. It will always elect to fight.
 * Critter 4 requires (> 100) energy to reproduce. It is represented by the
 * maroon circle with a beige outline.
 */
public class Critter4 extends Critter {
	@Override
	public String toString() {
		return "4";
	}

	private int dir;

	/**
	 * Constructs Critter4
	 */
	public Critter4() {
		dir = getDir();
	}

	/**
	 * Returns true if critter will fight
	 */
	public boolean fight(String not_used) {
		return true;
	}

	/**
	 * Moves and reproduces
	 */
	@Override
	public void doTimeStep() {
		/* take one step forward */
		walk(dir);

		if (getEnergy() > 100) {
			Critter4 kid = new Critter4();
			reproduce(kid, getDir());
		}

		dir = getDir();
	}

	/**
	 * Provides direction of critter
	 * 
	 * @return direction
	 */
	private static int getDir() {
		int fifty = Critter.getRandomInt(2);
		if (fifty != 0)
			return 2;
		return 6;
	}

	@Override
	public javafx.scene.paint.Color viewFillColor() {
		return javafx.scene.paint.Color.MAROON;
	}

	@Override
	public javafx.scene.paint.Color viewOutlineColor() {
		return javafx.scene.paint.Color.BEIGE;
	}

	@Override
	public CritterShape viewShape() {
		return CritterShape.CIRCLE;
	}
}