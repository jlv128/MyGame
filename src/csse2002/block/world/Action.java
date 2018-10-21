package csse2002.block.world;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents an Action which can be performed on the block world
 * (also called world map).
 */
public class Action {
    //MOVE_BUILDER action which is represented by integer 0
    public static final int MOVE_BUILDER = 0;
    //DIG action which is represented by integer 2
    public static final int DIG = 2;
    //MOVE_BLOCK action which is represented by integer 1
    public static final int MOVE_BLOCK = 1;
    //DROP action which is represented by integer 3
    public static final int DROP = 3;

    private int primaryAction;
    private String secondaryAction;

    /**
     * Create an Action that represents a manipulation of the blockworld.
     * @param primaryAction the action to be created
     * @param secondaryAction the supplementary information associated
     * with the primary action
     */
    public Action(int primaryAction, java.lang.String secondaryAction) {
        this.primaryAction = primaryAction;
        this.secondaryAction = secondaryAction;
    }

    /**
     * Get the integer representing the Action
     * (e.g., return 0 if Action is MOVE_BUILDER)
     * @return the primary action
     */
    public int getPrimaryAction() {
        return this.primaryAction;
    }

    /**
     * Gets the supplementary information associated with the Action
     * @return the secondary action,
     * or "" (empty string) if no secondary action exists
     */
    public java.lang.String getSecondaryAction() {
        if (secondaryAction == null) {
            return "";
        } else {
            return this.secondaryAction;
        }
    }

    /**
     * Read a line from the given reader and load the Action on that line.
     * @param reader the reader to read the action contents form
     * @return the created action, or null if the reader
     * is at the end of the file.
     * @throws ActionFormatException if the line has invalid contents and the
     * action cannot be created
     */
    public static Action loadAction(BufferedReader reader)
            throws ActionFormatException {
        List<String> allPrimaryActions = Arrays.asList(
                "MOVE_BUILDER", "DIG", "MOVE_BLOCK", "DROP");
        List<String> requireSecondary = Arrays.asList(
                "MOVE_BUILDER", "MOVE_BLOCK", "DROP");

        Map<String, Integer> primaryActions = new HashMap<>();
        primaryActions.put("MOVE_BUILDER", MOVE_BUILDER);
        primaryActions.put("DIG", DIG);
        primaryActions.put("MOVE_BLOCK", MOVE_BLOCK);
        primaryActions.put("DROP", DROP);

        String line;
        try {
            line=reader.readLine();
            } catch (IOException ioException) {
            System.err.println("length > 2");
            throw new ActionFormatException();
            }

        String firstWord = line.split(" ")[0];

        String secondWord;

        if (line.split(" ").length > 2) {
            System.err.println("length > 2");
            throw new ActionFormatException();
        } else if (!allPrimaryActions.contains(firstWord)) {
            System.err.println("!allPrimaryActions.contains(firstWord)");
            throw new ActionFormatException();
        } else if (requireSecondary.contains(firstWord)
                && line.split(" ").length < 2) {
            System.err.println("length < 2");
            throw new ActionFormatException();
        } else if (firstWord.equals("DIG") && (
                line.split(" ").length > 1
                || line.contains(" "))) {
            System.err.println("length > 1");
            throw new ActionFormatException();
        }  else if (line.split(" ").length > 1
                && requireSecondary.contains(firstWord)) {
            secondWord = line.split(" ")[1];
            Action action = new Action(primaryActions.get(firstWord),
                    secondWord);
            return action;
        } else if (firstWord.equals("DIG")) {
            Action action = new Action(primaryActions.get(firstWord),
                    "");
            return action;
        } else if (line == null) {
            return null;
        } else {
            return null;
        }
    }

    /**
     * Read all the actions from the given reader and perform them
     * on the given block world.
     * @param reader
     * @param startingMap
     * @throws ActionFormatException
     */
    public static void processActions(java.io.BufferedReader reader,
                                      WorldMap startingMap)
            throws ActionFormatException {
        String line;
        try{
            while ((line = reader.readLine())!= null) {
                BufferedReader newReader =
                        new BufferedReader(new StringReader(line));
                processAction(loadAction(newReader), startingMap);
            }
        } catch (IOException ioe) {
        } catch (NullPointerException e) {
            System.out.println("here");
        }

    }



    /**
     * Perform the given action on a WorldMap, and print output to System.out.
     * @param action the action to be done on the map
     * @param map the map to perform the action on
     */
    public static void processAction(Action action,
                                     WorldMap map) {
        List<String> validDirections = Arrays.asList("north", "east" ,"south"
                , "west");
        try {
        int primaryAction = action.getPrimaryAction();

        String secondaryAction = action.getSecondaryAction();

        if (primaryAction < 0 || primaryAction > 3 ||
                (!validDirections.contains(secondaryAction))
                        &&(Integer.parseInt(secondaryAction) < 0
                        || Integer.parseInt(secondaryAction)
                        >= map.getBuilder().getInventory().size()) ) {
            System.out.println("Error: Invalid action");
        }

        switch(primaryAction) {
            case 2 :
                try {
                    map.getBuilder().digOnCurrentTile();
                    System.out.println("Top block on current tile removed");
                } catch (TooLowException e) {
                    System.out.println("Too Low");
                } catch (InvalidBlockException e) {
                    System.out.println("Cannot use that block");
                } break;
            case 3 :
                try {
                    map.getBuilder().dropFromInventory
                            (Integer.parseInt(secondaryAction));
                    System.out.println("Dropped a block from inventory");
                } catch (NumberFormatException e) {
                    System.out.println("Error: Invalid action");
                } catch (InvalidBlockException e) {
                    System.out.println("Cannot use that block");
                } catch (TooHighException e) {
                    System.out.println("Too high");
                } break;
            case 1 :
                try {
                    map.getBuilder().getCurrentTile().moveBlock(secondaryAction);
                    System.out.println("Moved block " + secondaryAction);
                } catch (TooHighException e) {
                    System.out.println("Too high");
                } catch (InvalidBlockException e) {
                    System.out.println("Cannot use that block");
                } catch (NoExitException e) {
                    System.out.println("No exit this way");

                } break;
            case 0 :
                try {
                    map.getBuilder().moveTo(map.getBuilder()
                            .getCurrentTile().getExits().get(secondaryAction));
                    System.out.println("Moved builder " + secondaryAction);
                } catch (NoExitException e) {
                    System.out.println("No exit this way");
                } break;
            }
        } catch (NumberFormatException e){
            System.out.println(" Error: Invalid action");
        }
    }


}
