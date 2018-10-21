package csse2002.block.world;

import java.io.*;
import java.util.Scanner;

/**
 * Handles top-level interaction with performing actions on a WorldMap
 */
public class Main {
    public Main() {}

    /**
     * The entry point of the application. Takes 3 parameters an input map file
     * (args[0]), actions args[1]), and an output map file (args[2]). The
     * actions parameter can be either a filename, or the string "System.in".
     * @param args  the input arguments to the program
     */
    public static void main(java.lang.String[] args) {

        if (args.length != 3) {
            System.err.println("Usage: program inputMap actions outputMap" );
            System.exit(1);
        }

        try {
            WorldMap worldmap = new WorldMap(args[0]);

            BufferedReader reader;
            File file = new File(args[1]);

            if (file.exists()){
                reader = new BufferedReader(new FileReader(file));
                Action.processActions(reader, worldmap);
            }else{
                reader = new BufferedReader(new StringReader(args[1]));
                Action.processActions(reader, worldmap);
            }

            worldmap.saveMap(args[2]) ;

            reader.close();

        } catch (WorldMapFormatException e1) {
            System.err.println(e1);
            System.exit(2);
        } catch (WorldMapInconsistentException e1) {
            System.err.println(e1);
            System.exit(2);
        } catch (ActionFormatException e) {
            System.err.println(e);
            System.exit(4);
        } catch (IOException e) {
            System.err.println(e);
            System.exit(5);
        } catch (Exception e1) {
          System.err.println(e1);
          System.exit(3);
        }

    }
}
