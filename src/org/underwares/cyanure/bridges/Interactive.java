package org.underwares.cyanure.bridges;
/* $Id$
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

import java.io.*;
import org.underwares.cyanure.ai.Soul;

/**
 * Interactive mode static class
 * @author supernaut
 */
public class Interactive {

    public static int run(Soul soul){
        BufferedReader stdin = new BufferedReader(
                new InputStreamReader(System.in));
        System.out.println("## Entering Interactive mode");

        while(true){
            System.out.print("<< ");
            try {
                String input = stdin.readLine();
                // System Command Entered
                if(input.startsWith("*")){
                    if(input.equals("*STOP")){
                        System.out.println("## Stopping Execution");
                        break;
                    } else if(input.equals("*HELP")){
                        System.out.println("############ SYSCMDS ############");
                        System.out.println("## *STOP\tStop Execution");
                        System.out.println("## *HELP\tDisplay this screen");
                        System.out.println("#################################");
                    } else if(input.equals("*SAVE")){
                        System.out.println("## Dumping memory to file...");
                    } else {
                        System.out.println("##ERR: System Command \"" +
                                input + "\" not understood.");
                        System.out.println("## Call '*HELP' for help.");
                    }
                } else {
                    System.out.println(">> " + soul.converse(input));
                }
            } catch(IOException e) {
                System.out.println("## Returning with status 0");
                return(0);
            }
        }
        System.out.println("## Returning with status 1");
        return(1);
    }
}
