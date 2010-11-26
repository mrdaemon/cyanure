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
import org.underwares.cyanure.tasks.SaveSoulTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.FileNotFoundException;

import com.martiansoftware.jsap.*;
import org.jibble.jmegahal.JMegaHal;
import org.jibble.pircbot.IrcException;

/**
 * Main Class
 * @author Alexandre Gauthier
 */
public class Main {

    /**
     * Command line arguments configuration results.
     * Declared with package scope to allow internal components 
     * to access it.
     *
     * <b>NOTE: You should really use the <code>Configuration</code>
     * class instead unless you absolutely know what you are doing.</b>
     * 
     * @see Configuration#getBrainFile()
     */
    static JSAPResult config;

    /**
     * Program entry point
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        /* TODO: Cleanup this file. Some logic should be moved to private
         subroutines for clarity. Right now it is rather... urgh.*/

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
        System.out.println(Constants.getVersionString());
        System.out.println("Copyright (c) " + Constants.AUTHOR + " 2010-2011");
        System.out.println();
        System.out.println(Constants.getBuildInfoString(true));
        System.out.println();
        // TODO: implement RuntimeData class, with counters and jvm/platform info.

        // Parameters Setup
        JSAP jsap = new JSAP();
        
        try {
            registerParameters(jsap);
            config = jsap.parse(args);
        } catch (JSAPException ex) {
           System.err.println("Internal Error setting up parameters.");
           ex.printStackTrace(System.err);
        }
        
        // Display help on configuration failure
        if(!config.success()){
            printHelp(jsap, config);
            System.exit(1);
        }

        // Stop after displaying header if version was requested.
        // TODO: Fix this so it outputs a nice string, or move to above block.
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
            System.out.println(Configuration.getConfiguration());
            System.exit(0);
        }

        // Load Soul
        System.out.println("Initalizing soul...");
        
        Soul soul = null;
        File brainfile = new File(config.getString("brainfile"));

        if(brainfile.exists()){
            FileInputStream fis = null;
            ObjectInputStream ois = null;
            System.out.println("Loading memory from " + brainfile + "...");
            try {
                fis = new FileInputStream(brainfile);
                ois = new ObjectInputStream(fis);
                soul = new Soul((JMegaHal) ois.readObject());
            } catch (FileNotFoundException ex) {
                System.err.println("Unable to load memory!");
                System.err.println(brainfile + ": No such file or directory.");
                System.exit(1);
            } catch (ClassNotFoundException ex) {
                System.err.println("Error reading serialized data from " +
                        brainfile + ". Data is either corrupt or invalid.");
                System.exit(1);
            } catch (IOException ex) {
                System.err.println("An unexpected IO Exception occured " +
                        "while trying to read memory file.");
                System.err.println("Stack trace follows.");
                ex.printStackTrace(System.err);
                System.exit(1);
            } finally {
                try {
                    // Do attempt to close the file handle.
                    ois.close();
                    fis.close();
                } catch (IOException ex) {
                    System.err.println("WARNING: Unable to close memory file handle(s)");
                    ex.printStackTrace(System.err);
                }
            }
        } else {
            System.out.println("Creating new, blank soul at "
                                + brainfile + "...");
            soul = new Soul();

            // Fill in minimal initial data. With apologies to Johnny-5.
            soul.learn("NEED INPUT");
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

        // Launch Shell Server Daemon, if requested via configuration.properties
        if(Configuration.getDebug_shell()){
            try {
                ShellServer server = ShellServer.getInstance();
                server.start();
            } catch (ShellServerErrorException ex) {
                System.err.println("FATAL: Shell Server failed to initalize: " +
                        ex.getLocalizedMessage());
                System.err.println("Aborting execution.");
                System.exit(1);
            }
        }


        // Prepare DaemonTaskManager and register Tasks
        System.out.println("Initializing Daemon Tasks...");
        DaemonTaskManager bgtasks = DaemonTaskManager.getInstance();

        bgtasks.scheduleTask(new SaveSoulTask(soul), 15);

        System.out.println("Initialized Daemon Task Manager with " +
                                bgtasks.getTaskCount() + " task(s).");
        System.out.println(bgtasks.toString());

        
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
            InternetChatRelay irc = new InternetChatRelay(soul);
            irc.setVerbose(true);
            try {
                irc.connect(Configuration.getIrc_server());
            } catch (IOException ex) {
                System.err.println("Unable to connect to " +
                                    Configuration.getIrc_server());
                System.err.println(ex.getLocalizedMessage());
            } catch (IrcException ex) {
                System.err.println("Error connecting to IRC:" + ex.getLocalizedMessage());
            }

            // IDENTIFY support. Very basic.
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
        Switch versionop = new Switch("version")
                            .setShortFlag('v')
                            .setLongFlag("version");
        versionop.setHelp("Display version numbers and exit");
        jsapinstance.registerParameter(versionop);

        /**
         * Interactive mode
         */
        Switch interactiveop = new Switch("interactive")
                            .setShortFlag('i')
                            .setLongFlag("interactive");
        interactiveop.setHelp("Run Cyanure in interactive mode (conversation)");
        jsapinstance.registerParameter(interactiveop);

        /**
         * Brain File
         */
        FlaggedOption brainop = new FlaggedOption("brainfile")
                            .setShortFlag('f')
                            .setLongFlag("file")
                            .setDefault(System.getProperty("user.dir")
                                        + File.separator
                                        +"brain.dat")
                            .setRequired(false);
        brainop.setHelp("Brain file to use for this session");
        jsapinstance.registerParameter(brainop);

        /**
         * Configuration File
         */
        FlaggedOption configop = new FlaggedOption("config")
                               .setShortFlag('c')
                               .setLongFlag("config")
                               .setDefault(System.getProperty("user.dir")
                                           + File.separator
                                           + "configuration.properties")
                               .setRequired(false);
        configop.setHelp("Configuration file to use.");
        jsapinstance.registerParameter(configop);
        
        /**
         * Learning mode
         */
        FlaggedOption learnop = new QualifiedSwitch("learn")
                              .setShortFlag('l')
                              .setLongFlag("learn")
                              .setRequired(false);
        learnop.setHelp("Learn the content of the specified text file");
        jsapinstance.registerParameter(learnop);

        /**
         * Validate configuration file
         */
        Switch validateop = new Switch("validate")
                          .setShortFlag('x')
                          .setLongFlag("validateconf");
        validateop.setHelp("Validate config file and output values");
        jsapinstance.registerParameter(validateop);


    }
}
