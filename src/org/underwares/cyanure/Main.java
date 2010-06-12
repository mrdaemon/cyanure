package org.underwares.cyanure;
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
import org.underwares.cyanure.bridges.*;
import org.underwares.cyanure.ai.Soul;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import com.martiansoftware.jsap.*;
import org.jibble.jmegahal.JMegaHal;
/**
 * Main Class
 * @author Alexandre Gauthier
 */
public class Main {
    /**
     * Program entry point
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        // Print Banner
        System.out.println();
        System.out.println("  ####    #   #    ##    #    #  #    #  #####   ######");
        System.out.println(" #    #    # #    #  #   ##   #  #    #  #    #  #");
        System.out.println(" #          #    #    #  # #  #  #    #  #    #  #####");
        System.out.println(" #          #    ######  #  # #  #    #  #####   #");
        System.out.println(" #    #     #    #    #  #   ##  #    #  #   #   #");
        System.out.println("  ####      #    #    #  #    #   ####   #    #  ######");
        System.out.println();
        System.out.println("Multi Purpose Artifical Inelegance Program");
        System.out.println("Version " + Constants.VERSION);
        System.out.println("Copyright (c) " + Constants.AUTHOR + " 2010-2011");
        System.out.println();

        // Parameters Setup
        JSAP jsap = new JSAP();
        registerParameters(jsap);
        JSAPResult config = jsap.parse(args);

        if(!config.success()){
            printHelp(jsap, config);
            System.exit(1);
        }

        // Stop after displaying header if version was requested.
        if(config.getBoolean("version")){
            System.exit(0);
        }

        // Read configuration file
        System.out.println("Reading configuration file: "
                            + config.getString("config"));
        File cf = new File(config.getString("config"));
        try {
            Configuration.loadFile(cf);
            System.out.println("Configuration Loaded.");
        } catch (IOException e) {
            System.err.println("ERROR: Unable to read configuration file.");
            System.err.println(e.getMessage());
            System.exit(1);
        } catch (InvalidAIConfigException e) {
            System.err.println("ERROR: Malformed configuration file.");
            System.err.println(e.getMessage());
            System.exit(1);
        }

        // Display configuration and exit if --checkconf specified
        if(config.getBoolean("validate")){
            System.out.println("Loaded Configuration:");
            System.out.println(Configuration.toString());
            System.exit(0);
        }

        // Load Soul
        System.out.println("Initalizing soul...");
        Soul soul;
        File brainfile = new File(config.getString("brainfile"));
        if(brainfile.exists()){
            System.out.println("Loading memory from " + brainfile + "...");
            FileInputStream fis = new FileInputStream(brainfile);
            ObjectInputStream ois = new ObjectInputStream(fis);
            soul = new Soul((JMegaHal)ois.readObject());
            ois.close();
            fis.close();
        } else {
            System.out.println("Creating new, blank soul at "
                                + brainfile + "...");
            soul = new Soul();
            // TODO: Learn default sentence
        }
        System.out.println("Soul initialized.");
        
        // If specified, learn the contents of a text file
        if(config.getBoolean("learn")){
            System.out.println("Learning mode activated.");
            System.out.println("Reading in " + config.getString("learn"));
            try {
                soul.readFile(config.getString("learn"));
                System.out.println("Successfully learned "
                        + config.getString("learn"));
            } catch(IOException e) {
                System.err.println("Error: No such file: " +
                        config.getString("learn"));
                System.err.println(e.toString());
                System.exit(1);
            }
        }
        // Interactive mode
        if(config.getBoolean("interactive")){
            int status = Interactive.run(soul);
            System.out.println("Saving soul to "
                                + config.getString("brainfile") + "...");
            try{
                soul.save(config.getString("brainfile"));
            } catch(IOException e){
                System.err.print("Unable to save "
                                + config.getString("brainfile"));
            } finally {
                System.exit(status);
            }

        } else {
            // No Arguments Specified
            // IRC bot mode
            InternetChatRelay irc = new InternetChatRelay(soul, config);
            irc.setVerbose(true);
            irc.connect(Configuration.getIrc_server());
            if(Configuration.getIrc_doidentify()){
                    irc.identify(Configuration.getIrc_identpassword());
            }
            irc.joinChannel(Configuration.getIrc_channel());
        }
    }
    /**
     * Print help
     */
    private static void printHelp(JSAP jsap, JSAPResult config){
        // Parse and print encountered errors
        System.out.println();
        for (java.util.Iterator errs = config.getErrorMessageIterator();
                errs.hasNext();) {
            System.err.println("Error: " + errs.next());
        }
        System.out.println();
        System.out.println("Usage: ");
        System.out.print("cyanure ");
        System.out.println(jsap.getUsage());
        System.out.println();
        System.out.println(jsap.getHelp());
    }
    /**
     * Configure arguments for later parsing
     * @param jsapinstance
     */
    private static void registerParameters(JSAP jsapinstance) throws JSAPException{
        /**
         * Version
         */
        Switch version = new Switch("version")
                            .setShortFlag('v')
                            .setLongFlag("version");
        version.setHelp("Display version numbers and exit");
        jsapinstance.registerParameter(version);

        /**
         * Interactive mode
         */
        Switch interactive = new Switch("interactive")
                            .setShortFlag('i')
                            .setLongFlag("interactive");
        interactive.setHelp("Run Cyanure in interactive mode (conversation)");
        jsapinstance.registerParameter(interactive);

        /**
         * Brain File
         */
        FlaggedOption brain = new FlaggedOption("brainfile")
                            .setShortFlag('f')
                            .setLongFlag("file")
                            .setDefault(System.getProperty("user.dir")
                                        + File.separator
                                        +"brain.dat")
                            .setRequired(false);
        brain.setHelp("Brain file to use for this session");
        jsapinstance.registerParameter(brain);

        /**
         * Configuration File
         */
        FlaggedOption config = new FlaggedOption("config")
                               .setShortFlag('c')
                               .setLongFlag("config")
                               .setDefault(System.getProperty("user.dir")
                                           + File.separator
                                           + "configuration.properties")
                               .setRequired(false);
        config.setHelp("Configuration file to use.");
        jsapinstance.registerParameter(config);
        
        /**
         * Learning mode
         */
        FlaggedOption learn = new QualifiedSwitch("learn")
                              .setShortFlag('l')
                              .setLongFlag("learn")
                              .setRequired(false);
        learn.setHelp("Learn the content of the specified text file");
        jsapinstance.registerParameter(learn);

        /**
         * Validate configuration file
         */
        Switch validate = new Switch("validate")
                          .setShortFlag('x')
                          .setLongFlag("validateconf");
        validate.setHelp("Validate config file and output values");
        jsapinstance.registerParameter(validate);


    }
}
