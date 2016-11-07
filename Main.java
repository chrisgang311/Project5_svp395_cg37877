/* CRITTERS GUI <Main.java>
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

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextAreaBuilder;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Opens a JavaFX GUI where a critter survival simulation will be displayed. The
 * user has the option of making critters, stepping them through the world,
 * looking at the statistics of the critters, and setting a seed number so that
 * simulations with the same seed number run exactly the same way when the same
 * instructions are executed. The user also has the option of exitting the
 * program with a quit button.
 */
public class Main extends Application {
	static double scale = 5;
	static Canvas canvas = new Canvas(Params.world_width * scale + 100, Params.world_height * scale);
	static GraphicsContext context = canvas.getGraphicsContext2D();
	static TextArea ta = TextAreaBuilder.create().prefHeight(100).wrapText(true).build();
	static TextField statsOptions = new TextField();
	static TextField makeOptions = new TextField();
	public boolean running = false;

	/**
	 * Displays critter statistics to the JavaFX GUI where the statistics are
	 * located in the bottom right hand corner of the GUI.
	 */
	public static void displayStats() {
		try {
			Class critterSub;
			ta.clear();
			critterSub = Class.forName("assignment5." + statsOptions.getText());
			Critter critter = (Critter) critterSub.newInstance();
			List<Critter> instances = critter.getInstances(statsOptions.getText());
			Class<?>[] types = { List.class };
			Method sub = critterSub.getMethod("runStats", types);
			sub.invoke(null, instances);
		} catch (Exception e) {
			// TODO Auto-generated catch block
		}

	}

	/**
	 * Allows the program to redirect system output to the GUI instead of to the
	 * default console located in the IDE.
	 */
	public static class Console extends OutputStream {
		private TextArea output;

		public Console(TextArea ta) {
			this.output = ta;
		}

		@Override
		public void write(int i) throws IOException {
			output.appendText(String.valueOf((char) i));
		}
	}

	/**
	 * This method initializes the JavaFX GUI, creates all the buttons and text
	 * fields for the user, and executes instructions made by the user. This
	 * method creates the critters in the GUI and is responsible for moving them
	 * around in the GUI.
	 */
	@Override
	public void start(Stage primaryStage) throws IOException {
		try {
			ta.setMaxWidth(300);
			ta.setEditable(false);
			ta.setLayoutX(Params.world_width * scale + 50);
			ta.setLayoutY(270);
			Console console = new Console(ta);
			PrintStream ps = new PrintStream(console, true);
			System.setOut(ps);
			System.setErr(ps);
			makeOptions.setPromptText("Enter critter name");
			TextField makeNum = new TextField();
			makeNum.setPromptText("Enter number");
			Button make = new Button("make");
			make.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					if (makeOptions.getText() != null && !makeOptions.getText().isEmpty()) {
						try {
							Class critterSub;
							critterSub = Class.forName("assignment5." + makeOptions.getText());
							Critter critter = (Critter) critterSub.newInstance();
							if ((makeNum.getText() != null && !makeNum.getText().isEmpty())) {
								for (int i = 0; i < Integer.parseInt(makeNum.getText()); i++) {
									Critter.makeCritter(makeOptions.getText());
								}
							} else {
								Critter.makeCritter(makeOptions.getText());

							}
						} catch (Exception e) {
							ta.clear();
							System.out.println("Enter a valid critter name.");
						}
					}
					context.clearRect(0, 0, Params.world_width * scale, Params.world_height * scale);
					Critter.displayWorld();
					if (statsOptions.getText() != null && !statsOptions.getText().isEmpty()) {
						displayStats();
					} else {
						statsOptions.setText(makeOptions.getText());
						displayStats();
					}
				}
			});
			Button quit = new Button("quit");
			quit.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					Stage stage = (Stage) quit.getScene().getWindow();
					stage.close();
				}
			});
			TextField seedNum = new TextField();
			seedNum.setPromptText("Enter number");
			Button seed = new Button("seed");
			seed.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					if (seedNum.getText() != null && !seedNum.getText().isEmpty())
						Critter.setSeed(Integer.parseInt(seedNum.getText()));
				}
			});
			statsOptions.setPromptText("Enter critter name");
			Button stats = new Button("stats");
			stats.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					displayStats();
				}
			});
			TextField stepNum = new TextField();
			stepNum.setPromptText("Enter number");
			Button step = new Button("step");
			step.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					if ((stepNum.getText() != null && !stepNum.getText().isEmpty())) {
						for (int i = 0; i < Integer.parseInt(stepNum.getText()); i++) {
							Critter.worldTimeStep();
						}
					} else {
						Critter.worldTimeStep();
					}
					context.clearRect(0, 0, Params.world_width * scale, Params.world_height * scale);
					displayStats();
					Critter.displayWorld();
				}
			});
			TextField animNum = new TextField();
			animNum.setPromptText("Enter number");
			Button animate = new Button("animate");
			animate.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					Timer timer = new Timer();
					running = true;
					for (int i = 0; i < 10; i++) {
						timer.schedule(new TimerTask() {
							@Override
							public void run() {
								Critter.worldTimeStep();
								context.clearRect(0, 0, Params.world_width * scale, Params.world_height * scale);
								Critter.displayWorld();
								displayStats();
							}
						}, 1 * 100);

					}
				}
			});
			Button stop = new Button("stop");
			stop.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					running = false;
				}
			});
			makeNum.setMaxWidth(100);
			makeOptions.setMaxWidth(100);
			stepNum.setMaxWidth(100);
			seedNum.setMaxWidth(100);
			make.setMaxWidth(100);
			animNum.setMaxWidth(100);
			animate.setMaxWidth(100);
			stop.setMaxWidth(100);
			quit.setMaxWidth(100);
			step.setMaxWidth(100);
			seed.setMaxWidth(100);
			statsOptions.setMaxWidth(100);
			stats.setMaxWidth(100);
			Pane root = new Pane();
			VBox buttons = new VBox();
			buttons.setSpacing(10);
			buttons.setPadding(new Insets(0, 20, 10, 20));
			buttons.getChildren().addAll(make, step, seed, stats, quit);
			VBox fields = new VBox();
			fields.setSpacing(10);
			fields.setPadding(new Insets(0, 20, 10, 20));
			fields.getChildren().addAll(makeOptions, stepNum, seedNum, statsOptions);
			VBox makeNumBox = new VBox();
			makeNumBox.setSpacing(10);
			makeNumBox.setPadding(new Insets(0, 20, 10, 20));
			makeNumBox.getChildren().addAll(makeNum);
			HBox controls = new HBox();
			controls.setSpacing(-20);
			controls.setPadding(new Insets(0, 20, 10, 20));
			controls.getChildren().addAll(buttons, fields, makeNumBox);
			controls.setLayoutX(Params.world_width * scale + 10);
			controls.setLayoutY(20);
			root.getChildren().addAll(canvas, controls, ta);
			primaryStage.setScene(new Scene(root));
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}