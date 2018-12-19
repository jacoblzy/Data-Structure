package galaxy;

import java.util.Scanner;

import ucb.util.CommandArgs;

/** The main class for the Galaxy puzzle.
 *  @author P. N. Hilfinger
 */
public class Main {

    /** The main program.  ARGS may contain the options --seed=NUM,
     *  (random seed); --log (record moves and random tiles
     *  selected.); --testing (take random center places, options settings,
     *  and boundary clicks from standard input); and --no-display. */
    public static void main(String... args) {

        CommandArgs options =
            new CommandArgs("--seed=(\\d+) --log --testing --no-display",
                            args);
        if (!options.ok()) {
            System.err.println("Usage: java galaxy.Main [ --seed=NUM ] "
                               + "[ --log ] [ --testing ] [ --no-display ]");
            System.exit(1);
        }

        Model model = new Model();

        GUI gui;

        if (options.contains("--no-display")) {
            gui = null;
        } else {
            gui = new GUI("Galaxies 61B", model);
            gui.display(true);
        }

        CommandSource cmds;
        PuzzleSource puzzles;
        if (gui == null && !options.contains("--testing")) {
            System.err.println("Error: no input source.");
            System.exit(1);
            return;
        } else if (options.contains("--testing")) {
            TestSource src = new TestSource(new Scanner(System.in));
            cmds = src;
            puzzles = src;
        } else {
            cmds = new GUISource(gui);
            long seed;
            if (options.contains("--seed")) {
                seed = options.getLong("--seed");
            } else {
                seed = (long) (Math.random() * SEED_RANGE);
            }
            puzzles = new PuzzleGenerator(seed);
        }

        Controller puzzler =
            new Controller(model, gui, cmds, puzzles,
                           options.contains("--log"),
                           options.contains("--testing"));

        try {
            while (puzzler.solving()) {
                puzzler.solvePuzzle();
            }
        } catch (IllegalStateException excp) {
            System.err.printf("Internal error: %s%n", excp.getMessage());
            System.exit(1);
        }

        System.exit(0);
    }

    /** Range of default seeds. */
    private static final double SEED_RANGE = 1e12;
}
