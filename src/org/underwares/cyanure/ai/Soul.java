package org.underwares.cyanure.ai;
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
import org.jibble.jmegahal.*;

/**
 *
 * @author supernaut
 */
public class Soul {
    private JMegaHal ai;

    /**
     * Create a new soul based on an existing ai instance
     * @param ai
     */
    public Soul(JMegaHal ai){
        this.ai = ai;
    }

    /**
     * Create a new soul
     */
    public Soul(){
        this(new JMegaHal());
    }

    /**
     * Read and learn from a text file.
     * @param uri Path to file that must be read.
     * @throws IOException
     */
    public void readFile(String uri) throws IOException{
        File f = new File(uri);
        this.readFile(f);
    }

    /**
     * Read and learn from a text file
     * @param f File that must be read.
     * @throws IOException
     */
    public void readFile(File f) throws IOException{
        if(f.exists()){
            this.ai.addDocument(f.toURI().toString());
        } else {
            throw new IOException("File " + f.toURI().toString()
                    + " does not exists.");
        }
    }

    /**
     * Learn from a sentence.
     * @param input
     */
    public void learn(String input){
        this.ai.add(input);
    }

    /**
     * Speak a sentence related to a word.
     * @param word
     * @return sentence
     */
    public String speak(String word){
        return this.ai.getSentence(word);
    }
    /**
     * Speak a sentence.
     * @return sentence
     */
    public String speak(){
        return this.ai.getSentence();
    }

    /**
     * Speak a sentence related to the input, and learn in the process.
     * @param input
     * @return sentence
     */
    public String converse(String input){
        this.learn(input);
        return this.speak(input);
    }

    /**
     * Dumps current knowledge to disk.
     */
    public void save(String uri) throws FileNotFoundException, IOException {
        FileOutputStream fos = new FileOutputStream(uri);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(this.ai);
        oos.flush();
    }
}
