/* CRITTERS GUI <Critter3.java>
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
 * Critter 3 can run either East or West. It has a 10% chance of engaging in a
 * fight. Critter 3 requires (> 69) energy to reproduce. It is represented by
 * a whit square with a pink outline. 
 */

public class Critter3 extends Critter {
	@Override
	public String toString() {
		return "3";
	}

	private int dir;

	/**
	 * Constructs Critter3
	 */
	public Critter3() {
		dir = getDir();
	}

	/**
	 * Returns true if critter will fight
	 */
	public boolean fight(String not_used) {
		return Critter.getRandomInt(10) < 1;
	}

	/**
	 * Moves and reproduces
	 */
	@Override
	public void doTimeStep() {
		/* take one step forward */
		run(dir);

		if (getEnergy() > 69) {
			Critter3 kid = new Critter3();
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
			return 4;
		return 0;
	}

	@Override
	public javafx.scene.paint.Color viewFillColor() {
		return javafx.scene.paint.Color.WHITE;
	}

	@Override
	public javafx.scene.paint.Color viewOutlineColor() {
		return javafx.scene.paint.Color.PINK;
	}

	@Override
	public CritterShape viewShape() {
		return CritterShape.SQUARE;
	}
}