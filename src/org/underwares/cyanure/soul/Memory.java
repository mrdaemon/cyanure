/* $Id: Constants.java 1171 2010-04-04 21:06:29Z supernaut $
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
package org.underwares.cyanure.soul;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import org.jibble.jmegahal.JMegaHal;


/**
 * Brain Manager class
 * @author supernaut
 */
public class Memory {

    private JMegaHal brain;
    private FileInputStream f_in;
    private FileOutputStream f_out;

    public Memory() {
        this("memory.dat");
    }
    public Memory(String uri) {
        try {
            // Read memory from file
            FileInputStream f_in = new FileInputStream(uri);
        } catch(FileNotFoundException e) {
            System.err.println("##ERR: File '" + uri + "' doesn't exist.");

        }
    }

    public String process(String input) {
        this.brain.add(input);
        return this.brain.getSentence(input);
    }

    public void saveBrain(){

    }
    
}
