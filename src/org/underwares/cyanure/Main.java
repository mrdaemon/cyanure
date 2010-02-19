package org.underwares.cyanure;
/*
 *   ____
 *  / ___|   _  __ _ _ __  _   _ _ __ ___
 * | |  | | | |/ _` | '_ \| | | | '__/ _ \
 * | |__| |_| | (_| | | | | |_| | | |  __/
 *  \____\__, |\__,_|_| |_|\__,_|_|  \___|
 *       |___/
 * 
 * Multi Purpose Artificial Inelegance Program
 * Copyright (c) Alexandre Gauthier 2010-2011
 * All Rights Reserved
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
import org.underwares.cyanure.bridges.InternetChatRelay;

/**
 * Main Class
 * @author Alexandre Gauthier
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {

        System.out.println("  ####    #   #    ##    #    #  #    #  #####   ######");
        System.out.println(" #    #    # #    #  #   ##   #  #    #  #    #  #");
        System.out.println(" #          #    #    #  # #  #  #    #  #    #  #####");
        System.out.println(" #          #    ######  #  # #  #    #  #####   #");
        System.out.println(" #    #     #    #    #  #   ##  #    #  #   #   #");
        System.out.println("  ####      #    #    #  #    #   ####   #    #  ######");
        System.out.println();

        System.out.println("Multi Purpose Artifical Inelegance Program");
        System.out.println("Version");

        int i = 0;
        String argument;
        while (i < args.length && args[i].startsWith("-")) {
            argument = args[i++];
            if (argument.equals("-interactive")) {
                //LOL
            } else {
                System.out.println("Error: Malformed arguments.");
                printHelp();
                System.exit(1);
            }
        }
        // No Arguments Specified
        if (i == args.length) {
            //TODO: Implement other "personalities", right now just irc.
            //TODO: Make irc connectivity dynamic.
            InternetChatRelay irc = new InternetChatRelay();
            irc.setVerbose(true);
            irc.connect("irc.rizon.net");
            irc.identify("password");
            irc.joinChannel("#animorency");
        }
    }
    /**
     * Print help
     */
    private static void printHelp(){
        System.out.println();
        System.out.println("Usage: ");
        System.out.println("cyanure <-interactive>");
    }
}
