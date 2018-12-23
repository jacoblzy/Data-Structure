package make;

import graph.DepthFirstTraversal;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Scanner;

import static java.util.Arrays.asList;
import static make.Main.error;

/** Represents a makefile.
 *  @author P. N. Hilfinger
 */
class Maker {

    /** Describes Makefile lines that are ignored. */
    static final Pattern IGNORED = Pattern.compile("\\s*(#.*)?");
    /** Describes a rule header in a makefile:  TARGET: DEPENDENCIES. */
    static final Pattern HEADER =
        Pattern.compile("([^:\\s]+)\\s*:\\s*(.*?)\\s*");
    /** Describes a sequence of valid targets and whitespace. */
    static final Pattern TARGETS = Pattern.compile("[^:=#\\\\]*$");
    /** Describes an indented command line. */
    static final Pattern COMMAND = Pattern.compile("(\\s+.*)");
    /** Describes the separator on dependencies lines. */
    static final Pattern SPACES = Pattern.compile("\\p{Blank}+");

    /** Read and store the ages of existing targets from the
     *  file named FILEINFONAME. */
    void readFileAges(String fileInfoName) {
        String name;
        name = "<unknown>";
        try {
            Scanner inp = new Scanner(new File(fileInfoName));
            _currentTime = inp.nextInt();
            while (inp.hasNext()) {
                name = inp.next();
                int thisnumber = inp.nextInt();
                _ages.put(name, thisnumber);
            }
            inp.close();
        } catch (NoSuchElementException excp) {
            error("Error near entry for %s: %s", name, excp.getMessage());
        } catch (IOException excp) {
            error("Error IOE: %s, %s", name, excp.getMessage());
        }
    }

    /** Read make rules from the file named MAKEFILENAME and form the dependence
     *  graph with targets as vertices. */
    void readMakefile(String makefileName) {
        Scanner inp;
        String target;
        ArrayList<String> dependencies;
        ArrayList<String> commands;

        target = null;
        dependencies = null;
        commands = null;
        try {
            inp = new Scanner(new File(makefileName));
        } catch (IOException excp) {
            error("Could not find makefile error: %s", makefileName);
            System.exit(3);
            return;
        }

        while (inp.hasNextLine()) {
            String line = inp.nextLine();
            Matcher parsed;
            parsed = IGNORED.matcher(line);
            if (parsed.matches()) {
                continue;
            }
            parsed = HEADER.matcher(line);
            if (parsed.matches()) {
                addRule(target, dependencies, commands);
                target = parsed.group(1);
                if (!TARGETS.matcher(target).matches()) {
                    error("Bad target error: '%s'", target);
                }
                if (!TARGETS.matcher(parsed.group(2)).matches()) {
                    error("One or more bad prerequisites error: '%s'",
                          parsed.group(2));
                }
                dependencies = new ArrayList<>();
                if (!parsed.group(2).isEmpty()) {
                    dependencies.addAll(asList(SPACES.split(parsed.group(2))));
                }
                commands = new ArrayList<>();
                continue;
            }
            parsed = COMMAND.matcher(line);
            if (target != null && parsed.matches()) {
                commands.add(parsed.group(1));
            } else {
                error("Erroneous input line: '%s'", line);
            }
        }
        addRule(target, dependencies, commands);
    }

    /** Add rule
     *      TARGET: DEPENDENCIES
     *          COMMANDS
     *  to makegraph, or add DEPENDENCIES and COMMANDS to that rule, if it
     *  already exists.  Returns the rule. */
    private Rule addRule(String target,
                         List<String> dependencies,
                         List<String> commands) {
        if (target != null) {
            Rule rule;
            rule = _targets.get(target);
            if (rule == null) {
                rule = new Rule(this, target);
                _targets.put(target, rule);
            }
            for (String dependency: dependencies) {
                Rule depRule = addRule(dependency,
                                       Collections.<String>emptyList(),
                                       Collections.<String>emptyList());
                rule.addDependency(depRule);
            }
            rule.addCommands(commands);
            return rule;
        } else {
            return null;
        }
    }

    /** Issue instructions to build TARGET. */
    void build(String target) {
        Rule targetRule = addRule(target, Collections.<String>emptyList(),
                                  Collections.<String>emptyList());
        int v = targetRule.getVertex();
        if (_traversal == null) {
            _traversal = new MakeTraversal();
            _traversal.traverse(v);
        } else {
            _traversal.traverse(v);
        }
    }

    /** Return my dependence graph. */
    final Depends getGraph() {
        return _depends;
    }

    /** Return the initial age of TARGET, if it exists, or null if it
     *  does not. */
    final Integer getInitialAge(String target) {
        return _ages.get(target);
    }

    /** Returns the current time (to be attached to rebuilt targets). */
    final int getCurrentTime() {
        return _currentTime;
    }

    /** The current time.  Should be no earlier than the time on the
     *  latest file. */
    private int _currentTime;
    /** The makefile dependency graph. */
    private Depends _depends = new Depends();
    /** Mapping of target names to their ages. */
    private HashMap<String, Integer> _ages = new HashMap<>();
    /** Mapping of target names to their Rules. */
    private HashMap<String, Rule> _targets = new HashMap<>();
    /** Depth-first traversal of my vertices. */
    private MakeTraversal _traversal;

    /** Traversal for make dependency graph. */
    class MakeTraversal extends DepthFirstTraversal {
        /** A traversal of my dependency graph. */
        MakeTraversal() {
            super(_depends);
        }

        @Override
        protected boolean postVisit(int v0) {
            _depends.getLabel(v0).rebuild();
            return true;
        }

        @Override
        protected boolean shouldPostVisit(int v0) {
            return true;
        }
    }

}
