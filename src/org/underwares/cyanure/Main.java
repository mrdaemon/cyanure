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
import org.underwares.cyanure.personalities.InternetChatRelay;
import org.jibble.pircbot.*;

/**
 * Main Class
 * @author Alexandre Gauthier
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
         System.out.println("/ ___|   _  __ _ _ __  _   _ _ __ ___ ");
         System.out.println("| |  | | | |/ _` | '_ \\| | | | '__/ _ \\");
         System.out.println(")| |__| |_| | (_| | | | | |_| | | |  __/");
         System.out.println("\\____\\__, |\\__,_|_| |_|\\__,_|_|  \\___|");
         System.out.println("|___/                            ");
         System.out.println("Multi Purpose Artifical Inelegance Program");

         //TODO: Implement other "personalities", right now just irc.
         //TODO: Make irc connectivity dynamic.
         InternetChatRelay irc = new InternetChatRelay();
         irc.setVerbose(true);
         irc.connect("irc.rizon.net");
         irc.identify("password");
         irc.joinChannel("#animorency");

    }

}
