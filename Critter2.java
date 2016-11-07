/* CRITTERS GUI <Critter2.java>
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
 * Critter 2 can run in 5 possible directions; East, South East, South, and
 * South West, and West. It has a 50 percent chance of engaging in a fight.
 * Critter 1 requires little energy to reproduce (> 25), so it should reproduce
 * frequently. Critter 2 is a blue circle with a black outline.
 */

public class Critter2 extends Critter {
	@Override
	public String toString() {
		return "2";
	}

	private int dir;

	/**
	 * Constructs Critter2
	 */
	public Critter2() {
		dir = getDir();
	}

	/**
	 * Returns true if critter will fight
	 */
	public boolean fight(String not_used) {
		return Critter.getRandomInt(9) > 2;
	}

	/**
	 * Moves and reproduces
	 */
	@Override
	public void doTimeStep() {
		/* take one step forward */
		look(dir, false);
		walk(dir);

		if (getEnergy() > 200) {
			Critter2 kid = new Critter2();
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
		int direction = Critter.getRandomInt(8);
		if (direction > 4) {
			direction -= 4;
		}
		return direction;
	}

	@Override
	public javafx.scene.paint.Color viewFillColor() {
		return javafx.scene.paint.Color.BLUE;
	}

	@Override
	public javafx.scene.paint.Color viewOutlineColor() {
		return javafx.scene.paint.Color.BLACK;
	}

	@Override
	public CritterShape viewShape() {
		return CritterShape.CIRCLE;
	}
}
