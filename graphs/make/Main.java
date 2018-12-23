package make;

import java.util.ArrayList;
import ucb.util.CommandArgs;

/** Initial class for the 'make' program.
 *  @author P. N. Hilfinger
 */
public final class Main {

    /** Entry point for the CS61B make program.  RAWARGS may contain options
     *  and targets:
     *      [ -f MAKEFILE ] [ -D FILEINFO ] TARGET1 TARGET2 ...
     */
    public static void main(String... rawArgs) {
        String makefileName;
        String fileInfoName;
        CommandArgs args = new CommandArgs("-f={0,1} -D={0,1} --={1,}",
                                           rawArgs);

        if (!args.ok()) {
            usage();
        }

        makefileName = args.getLast("-f", "Makefile");
        fileInfoName = args.getLast("-D", "fileinfo");

        ArrayList<String> targets = new ArrayList<String>();

        Maker maker = new Maker();

        try {
            maker.readFileAges(fileInfoName);
            maker.readMakefile(makefileName);
            for (String target : args.get("--")) {
                maker.build(target);
            }
        } catch (IllegalArgumentException | IllegalStateException excp) {
            System.err.printf("make: %s%n", excp.getMessage());
            System.exit(1);
        }
    }

    /** Throw an exception containing a message constructed from FORMAT
     *  and ARGS, as for String.format. */
    static void error(String format, Object... args) {
        throw new IllegalArgumentException(String.format(format, args));
    }

    /** Print a brief usage message and exit program abnormally. */
    private static void usage() {
        System.err.printf("Usage: java make [-D FILEINFO] [-f MAKEFILE] "
                          + "TARGET...%n"
                          + "   FILEINFO contains the current time and change "
                          + "times for files%n"
                          + "      (default 'fileinfo').%n"
                          + "   MAKEFILE contains dependency information and "
                          + "build commands%n"
                          + "      (default Makefile).%n");
        System.exit(1);
    }

}
