package assignment5;

import java.util.List;

public abstract class Critter {
	/* NEW FOR PROJECT 5 */
	public enum CritterShape {
		CIRCLE,
		SQUARE,
		TRIANGLE,
		DIAMOND,
		STAR
	}
	
	/* the default color is white, which I hope makes critters invisible by default
	 * If you change the background color of your View component, then update the default
	 * color to be the same as you background 
	 * 
	 * critters must override at least one of the following three methods, it is not 
	 * proper for critters to remain invisible in the view
	 * 
	 * If a critter only overrides the outline color, then it will look like a non-filled 
	 * shape, at least, that's the intent. You can edit these default methods however you 
	 * need to, but please preserve that intent as you implement them. 
	 */
	public javafx.scene.paint.Color viewColor() { 
		return javafx.scene.paint.Color.WHITE; 
	}
	
	public javafx.scene.paint.Color viewOutlineColor() { return viewColor(); }
	public javafx.scene.paint.Color viewFillColor() { return viewColor(); }
	
	public abstract CritterShape viewShape(); 
	
	private static String myPackage;
	private	static List<Critter> population = new java.util.ArrayList<Critter>();
	private static List<Critter> babies = new java.util.ArrayList<Critter>();

	// Gets the package name.  This assumes that Critter and its subclasses are all in the same package.
	static {
		myPackage = Critter.class.getPackage().toString().split(" ")[1];
	}
	
	protected String look(int direction, boolean steps) {return "";}
	
	/* rest is unchanged from Project 4 */
	
	
	private static java.util.Random rand = new java.util.Random();
	public static int getRandomInt(int max) {
		return rand.nextInt(max);
	}
	
	public static void setSeed(long new_seed) {
		rand = new java.util.Random(new_seed);
	}
	
	
	/* a one-character long string that visually depicts your critter in the ASCII interface */
	public String toString() { return ""; }
	
	private int energy = 0;
	protected int getEnergy() { return energy; }
	
	private int x_coord;
	private int y_coord;
	
	/**
	 * The move method takes in a stepSize and direction and moves the critter in that direction with
	 * the given step size. If the critter goes over the edge of the map it appears at the opposite edge.
	 * @param stepSize size of step critter will take
	 * @param direction direction critter will move in
	 */
	private void move(int stepSize, int direction) {
		boolean xEdge = false;
		boolean yEdge = false;
		
		// Find exceptions for critters at edges of map
		// x_coord is at left edge
		if (x_coord - stepSize < 0 && direction >= 3 && direction <= 5) {
			x_coord = Params.world_width + x_coord - stepSize;
			xEdge = true;
		}
		// y_coord is at top edge
		if (y_coord - stepSize < 0 && direction >= 1 && direction <= 3) {
			y_coord = Params.world_height + y_coord - stepSize;
			yEdge = true;
		}
		// x_coord is at right edge
		if (x_coord + stepSize > Params.world_width - 1 && (direction == 1 || direction == 0 || direction == 7)) {
			x_coord = (x_coord + stepSize) % Params.world_width;
			xEdge = true;
		}
		// y_coord is at bottom edge
		if (y_coord + stepSize > Params.world_height - 1 && direction >= 5 && direction <= 7) {
			y_coord = (y_coord + stepSize) % Params.world_height;

			yEdge = true;
		}
		
		// Changes the x and y coordinates of the critter
		switch (direction) {
		case 0: if (!xEdge)	x_coord += stepSize;
				break;
		case 1: if (!xEdge)	x_coord += stepSize;
				if (!yEdge) y_coord -= stepSize;
				break;
		case 2: if (!yEdge) y_coord -= stepSize;
				break;
		case 3: if (!xEdge) x_coord -= stepSize;
				if (!yEdge) y_coord -= stepSize;
				break;
		case 4: if (!xEdge) x_coord -= stepSize;
				break;
		case 5: if (!xEdge) x_coord -= stepSize;
				if (!yEdge) y_coord += stepSize;
				break;
		case 6: if (!yEdge) y_coord += stepSize;
				break;
		case 7: if (!xEdge)	x_coord += stepSize;
				if (!yEdge) y_coord += stepSize;
				break;
		default: break;
		}
		
		
		// Deduct energy cost for movement
		if (stepSize == 1) {
			energy -= Params.walk_energy_cost;
		} else if (stepSize == 2) { 
			energy -= Params.run_energy_cost;
		}
	}
	
	/**
	 * Critter will move 1 step in direction specified.
	 * @param direction direction critter will move
	 */
	protected final void walk(int direction) {
		move(1, direction);
	}
	
	/**
	 * Critter will move 2 steps in direction specified.
	 * @param direction direction critter will move
	 */
	protected final void run(int direction) {
		move(2, direction);
	}
	
	/**
	 * Reproduce will initialize the critter offspring passed in the parameter. The offspring will be 
	 * placed in an adjacent space of the parent
	 * @param offspring the critter object that will be initialized
	 * @param direction the direction the critter offspring will be placed
	 */
	protected final void reproduce(Critter offspring, int direction) {
		if (this.energy < Params.min_reproduce_energy) {
			return;
		}
		offspring.energy = (int) Math.floor((double) this.energy / 2);
		this.energy = (int) Math.ceil((double) this.energy / 2);
		offspring.x_coord = this.x_coord;
		offspring.y_coord = this.y_coord;
		offspring.move(1, direction);
		babies.add(offspring);
	}

	public abstract void doTimeStep();
	public abstract boolean fight(String oponent);
	
	/**
	 * create and initialize a Critter subclass.
	 * critter_class_name must be the unqualified name of a concrete subclass of Critter, if not,
	 * an InvalidCritterException must be thrown.
	 * (Java weirdness: Exception throwing does not work properly if the parameter has lower-case instead of
	 * upper. For example, if craig is supplied instead of Craig, an error is thrown instead of
	 * an Exception.)
	 * @param critter_class_name
	 * @throws InvalidCritterException
	 */
	public static void makeCritter(String critter_class_name) throws InvalidCritterException {
		try {
			Class critterSub = Class.forName("assignment4." + critter_class_name);
			Critter critter = (Critter) critterSub.newInstance();
			critter.x_coord = Critter.getRandomInt(Params.world_width);
			critter.y_coord = Critter.getRandomInt(Params.world_height);
			critter.energy = Params.start_energy;
			population.add(critter);
		}
		catch (ClassNotFoundException e) {
			throw new InvalidCritterException("error processing: " + critter_class_name);
		}
		catch (IllegalAccessException e) {
			throw new InvalidCritterException("error processing: " + critter_class_name);
		}
		catch (InstantiationException e) {
			throw new InvalidCritterException("error processing: " + critter_class_name);
		}
	}
	
	/**
	 * Gets a list of critters of a specific type.
	 * @param critter_class_name What kind of Critter is to be listed.  Unqualified class name.
	 * @return List of Critters.
	 * @throws InvalidCritterException
	 */
	public static List<Critter> getInstances(String critter_class_name) throws InvalidCritterException {
		List<Critter> result = new java.util.ArrayList<Critter>();
		try {
			for (Critter c : population) {
				if (Class.forName("assignment4." + critter_class_name).isInstance(c)) {
					result.add(c);
				}
			}
		} catch (ClassNotFoundException e) {
			System.out.println("error handling: " + critter_class_name);
		}
		return result;
	}
	
	/**
	 * Prints out how many Critters of each type there are on the board.
	 * @param critters List of Critters.
	 */
	public static void runStats(List<Critter> critters) {
		System.out.print("" + critters.size() + " critters as follows -- ");
		java.util.Map<String, Integer> critter_count = new java.util.HashMap<String, Integer>();
		for (Critter crit : critters) {
			String crit_string = crit.toString();
			Integer old_count = critter_count.get(crit_string);
			if (old_count == null) {
				critter_count.put(crit_string,  1);
			} else {
				critter_count.put(crit_string, old_count.intValue() + 1);
			}
		}
		String prefix = "";
		for (String s : critter_count.keySet()) {
			System.out.print(prefix + s + ":" + critter_count.get(s));
			prefix = ", ";
		}
		System.out.println();		
	}
	
	/* the TestCritter class allows some critters to "cheat". If you want to 
	 * create tests of your Critter model, you can create subclasses of this class
	 * and then use the setter functions contained here. 
	 * 
	 * NOTE: you must make sure that the setter functions work with your implementation
	 * of Critter. That means, if you're recording the positions of your critters
	 * using some sort of external grid or some other data structure in addition
	 * to the x_coord and y_coord functions, then you MUST update these setter functions
	 * so that they correctly update your grid/data structure.
	 */
	static abstract class TestCritter extends Critter {
		protected void setEnergy(int new_energy_value) {
			super.energy = new_energy_value;
		}
		
		protected void setX_coord(int new_x_coord) {
			super.x_coord = new_x_coord;
		}
		
		protected void setY_coord(int new_y_coord) {
			super.y_coord = new_y_coord;
		}
		
		protected int getX_coord() {
			return super.x_coord;
		}
		
		protected int getY_coord() {
			return super.y_coord;
		}
		

		/*
		 * This method getPopulation has to be modified by you if you are not using the population
		 * ArrayList that has been provided in the starter code.  In any case, it has to be
		 * implemented for grading tests to work.
		 */
		protected static List<Critter> getPopulation() {
			return population;
		}
		
		/*
		 * This method getBabies has to be modified by you if you are not using the babies
		 * ArrayList that has been provided in the starter code.  In any case, it has to be
		 * implemented for grading tests to work.  Babies should be added to the general population 
		 * at either the beginning OR the end of every timestep.
		 */
		protected static List<Critter> getBabies() {
			return babies;
		}
	}

	/**
	 * Clear the world of all critters, dead and alive
	 */
	public static void clearWorld() {
		population.removeAll(population);
		babies.removeAll(babies);
	}

	/**
	 * This method will move each critter using its doTimeStep method and will resolve conflicts between critters.
	 * It will also remove critters if they are dead. Finally the method will add offspring and algae to the critter
	 * population at the end of the method.
	 */
	public static void worldTimeStep() {
		// Incrementing 2d int map where a critter exists
		int[][] mapCount = new int[Params.world_width][Params.world_height];
		for (int i = 0; i < Params.world_width * Params.world_height; i++) {
			mapCount[i % Params.world_width][i / Params.world_width] = 0;
		}
		for (Critter c : population) {
			mapCount[c.x_coord][c.y_coord]++;
		}
		
		// If  a place in the map is greater than 1 then 2 or more critter occupy 1 place

		ArrayList<Critter> moved = new ArrayList<Critter>();
		for (int i = 0; i < Params.world_height * Params.world_width; i++) {
			if (mapCount[i % Params.world_width][i / Params.world_width] > 1) {
				ArrayList<Critter> fightList = new ArrayList<Critter>();
				for (Critter c : population) {
					if (c.x_coord == i % Params.world_width && c.y_coord == i / Params.world_width) {
						fightList.add(c);
					}
				}
				moved.addAll(fightClub(fightList));
			}
		}
		
		
		ArrayList<Critter> removePop = new ArrayList<Critter>();
		for (Critter c : population) {
			if (!moved.contains(c)) c.doTimeStep();
			c.energy -= Params.rest_energy_cost;
			if (c.energy <= 0) {
				removePop.add(c);
			}
		}
		
		// Culling the dead critters from the population
		population.removeAll(removePop);
		
		// Adding all offspring to population
		population.addAll(babies);
		
		// Clearing offspring array list
		babies.clear();
		
		// Creating algae
		try {
			for (int i = 0; i < Params.refresh_algae_count; i++) {
				Critter.makeCritter("Algae");
			}
		} catch (InvalidCritterException e) {
			System.out.println("error processing: " + "Algae");
		}
	}

	/**
	 * Resolves conflicts between critters and returns an ArrayList of critters specifying the critters
	 * that have moved during this method.
	 * @param fightList the list of critters that are in conflict
	 * @return moved the list of critters that have moved during this method
	 */
	public static ArrayList<Critter> fightClub(ArrayList<Critter> fightList) {
		ArrayList<Critter> moved = new ArrayList<Critter>();
		int oldXCoord = 0;
		int oldYCoord = 0;
		boolean aHasMoved = false;
		boolean bHasMoved = false;
		while (fightList.size() > 1) {
			oldXCoord = fightList.get(0).x_coord;
			oldYCoord = fightList.get(0).y_coord;
			boolean aFight = fightList.get(0).fight(fightList.get(1).toString());
			if (fightList.get(0).x_coord != oldXCoord || fightList.get(0).y_coord != oldYCoord) {
				aHasMoved = true;
			}
			oldXCoord = fightList.get(1).x_coord;
			oldYCoord = fightList.get(1).y_coord;
			boolean bFight = fightList.get(1).fight(fightList.get(0).toString());
			if (fightList.get(1).x_coord != oldXCoord || fightList.get(1).y_coord != oldYCoord) {
				bHasMoved = true;
			}
			if (!aFight) {
				oldXCoord = fightList.get(0).x_coord;
				oldYCoord = fightList.get(0).y_coord;
				if (!aHasMoved) {
					fightList.get(0).doTimeStep();
				}
				for (Critter c : population) {
					if (fightList.get(0).x_coord == c.x_coord && fightList.get(0).y_coord == c.y_coord && !fightList.get(0).equals(c)) {
						fightList.get(0).x_coord = oldXCoord;
						fightList.get(0).y_coord = oldYCoord;
						aHasMoved = false;
					}
				}
				
				if (aHasMoved) 
					moved.add(fightList.get(0));
			}
			if (!bFight) {
				oldXCoord = fightList.get(1).x_coord;
				oldYCoord = fightList.get(1).y_coord;
				if (!bHasMoved) {
					fightList.get(1).doTimeStep();
				}
				for (Critter c : population) {
					if (fightList.get(1).x_coord == c.x_coord && fightList.get(1).y_coord == c.y_coord && !fightList.get(1).equals(c)) {
						fightList.get(1).x_coord = oldXCoord;
						fightList.get(1).y_coord = oldYCoord;
						bHasMoved = false;
					}
				}
				if (bHasMoved) 
					moved.add(fightList.get(1));
			}
			if (!aFight) {
				fightList.remove(0);
			}
			if (!bFight) {
				fightList.remove(0);
			}
			if (fightList.size() >= 2) {
				if (fightList.get(0).x_coord == fightList.get(1).x_coord &&
						fightList.get(0).y_coord == fightList.get(1).y_coord &&
						fightList.get(0).energy > 0 && fightList.get(1).energy > 0) {
					int aPower, bPower;
					if (aFight) {
						aPower = Critter.getRandomInt(fightList.get(0).energy + 1);
					} else {
						aPower = 0;
					}
					if (bFight) {
						bPower = Critter.getRandomInt(fightList.get(1).energy + 1);
					} else {
						bPower = 0;
					}
					if (aPower >= bPower) {
						fightList.get(0).energy += fightList.get(1).energy / 2;
						fightList.get(1).energy = 0;
						fightList.remove(1);
					} else if (aPower < bPower) {
						fightList.get(1).energy += fightList.get(0).energy / 2;
						fightList.get(0).energy = 0;
						fightList.remove(0);
					}
				}
			}
		}
		return moved;
	}	

	/**
	 * Displays the critter population on the console in their respective coordinates.
	 */
	public static void displayWorld() {
		// Creates 2D array of critters for positions on map
		Critter[][] critterMap = new Critter[Params.world_width][Params.world_height];
		for (Critter c : population) {
			critterMap[c.x_coord][c.y_coord] = c;
		}
		
		
		// Prints top border
		System.out.print("+");
		for (int i = 0; i < Params.world_width; i++) {
			System.out.print("-");
		}
		System.out.println("+");
		
		// Prints map
		for (int i = 0; i < Params.world_height; i++) {
			System.out.print("|");
			for (int j = 0; j < Params.world_width; j++) {
				if (critterMap[j][i] != null) {
					System.out.print(critterMap[j][i].toString());
				} else {
					System.out.print(" ");
				}
			}
			System.out.println("|");
		}
		
		// Prints bottom border
		System.out.print("+");
		for (int i = 0; i < Params.world_width; i++) {
			System.out.print("-");
		}
		System.out.println("+");
	}

}
