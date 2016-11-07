/* CRITTERS GUI <Critter1.java>
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
 * Critter 1 can run in 5 possible directions; East, North East, North, North
 * West, and West. It has a 50 percent chance of engaging in a fight. Critter 1
 * requires little energy to reproduce (> 25), so it should reproduce
 * frequently. Critter 1 is a yellow square with an orange outline.
 */

public class Critter1 extends Critter {
	@Override
	public String toString() {
		return "1";
	}

	private int dir;

	/**
	 * Constructs Critter1
	 */
	public Critter1() {
		dir = getDir();
	}

	/**
	 * Returns true if critter will fight
	 */
	public boolean fight(String not_used) {
		look(dir, true);
		return Critter.getRandomInt(10) > 4;

	}

	/**
	 * Moves and reproduces
	 */
	@Override
	public void doTimeStep() {
		/* take one step forward */
		run(dir);

		if (getEnergy() > 25) {
			Critter1 kid = new Critter1();
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
		if (direction > 0 && direction < 4) {
			direction += 4;
		}
		return direction;
	}

	@Override
	public javafx.scene.paint.Color viewFillColor() {
		return javafx.scene.paint.Color.YELLOW;
	}

	@Override
	public javafx.scene.paint.Color viewOutlineColor() {
		return javafx.scene.paint.Color.ORANGE;
	}

	@Override
	public CritterShape viewShape() {
		return CritterShape.SQUARE;
	}
}
