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
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Static Configuration class, handles reading the
 * properties file as well as validating it.
 * Exposes members and configuration values globally.
 * 
 * @author Alexandre Gauthier
 */
public class Configuration {
    // Properties
    private static Properties config = new Properties();

    // Internal Settings
    private static String ainame;

    // IRC Bridge
    private static String irc_server;
    private static String irc_channel;
    private static Boolean irc_doidentify = false;
    private static String irc_identpassword;
    private static String irc_altnickname;

    // Runtime settings Settings (external, not defined through properties)
    private static String rt_brainfile;

    // Advanced Settings, with defaults
    private static int tm_maxthreads = 1;
    private static Boolean debug_shell = false;
    private static int debug_shell_port = 9998;
    private static String debug_shell_hostkey = "ssh_hostkey";

    /**
     * Load Configuration File
     *
     * @param file  Properties file to load
     * @throws IOException
     * @throws InvalidAIConfigException
     */
    public static void loadFile(File file) 
            throws IOException, InvalidAIConfigException {
        // Load Properties
        if(file.exists()){
            FileInputStream fis = new FileInputStream(file);
            Configuration.config.load(fis);
            fis.close();
        } else {
            throw new IOException();
        }

        if(Configuration.config == null){
            throw new InvalidAIConfigException("Configuration file is empty");
        }

        // Set all standard private members
        Configuration.setAiname(config.getProperty("ainame"));
        Configuration.setIrc_server(config.getProperty("irc_server"));
        Configuration.setIrc_channel(config.getProperty("irc_channel"));
        Configuration.setIrc_doidentify(config.getProperty("irc_doidentify"));
        Configuration.setIrc_identpassword(
                config.getProperty("irc_identpassword"));
        Configuration.setIrc_altnickname(config.getProperty("irc_altnickname"));

        // Set advanced private members (with defaults)
        if(config.containsKey("tm_maxthreads")){
            Configuration.setTm_maxthreads(config.getProperty("tm_maxthreads"));
        }

        if(config.containsKey("debug_shell")){
            Configuration.setDebug_shell(config.getProperty("debug_shell"));
        }

        if(config.containsKey("debug_shell_port")){
            Configuration.setDebug_shell_port(
                    config.getProperty("debug_shell_port"));
        }

        if(config.containsKey("debug_shell_hostkey")){
            Configuration.setDebug_shell_hostkey(
                    config.getProperty("debug_shell_hostkey"));
        }
    }

    /**
     * Get current AI Name
     *
     * @return ainame  AI Name
     */
    public static String getAiname() {
        return ainame;
    }

    /**
     * Get alternate IRC nickname
     *
     * @return irc_altnickname  irc alt nick
     */
    public static String getIrc_altnickname() {
        return irc_altnickname;
    }

    /**
     * Get current IRC Channel
     *
     * @return irc_channel   channel name
     */
    public static String getIrc_channel() {
        return irc_channel;
    }

    /**
     * Inquire as if the bot is configured to do nickserv
     * identification
     *
     * @return irc_doidentify  Do irc ident?
     */
    public static Boolean getIrc_doidentify() {
        return irc_doidentify;
    }

    /**
     * Get current IRC nickserv password
     *
     * @return irc_identpassword  irc server password
     */
    public static String getIrc_identpassword() {
        return irc_identpassword;
    }

    /**
     * Get current IRC Server
     *
     * @return irc_server  irc server host/address
     */
    public static String getIrc_server() {
        return irc_server;
    }

    /**
     * Get maximum amount of threads for task manager pool
     *
     * @return tm_maxthreads  thread pool size
     */
    public static int getTm_maxthreads() {
        return tm_maxthreads;
    }

    /**
     * Get boolean value as to enable remote debugging shell
     *
     * @return debug_shell  Spawn debug shell?
     */
    public static Boolean getDebug_shell() {
        return debug_shell;
    }

    /**
     * Get TCP Port to run debug shell on.
     * Default is 9998
     *
     * @return debug_shell_port  Debug Shell TCP Port
     */
    public static int getDebug_shell_port() {
        return debug_shell_port;
    }

    public static String getDebug_shell_hostkey() {
        return debug_shell_hostkey;
    }

    /*
     * External Getters
     * The following getters rely on external classes to provide
     * information. This was done to centralize tunables and configuration
     * to a single class.
     *
     * You may see them as wrappers if you wish.
     */

    /**
     * Get current brainfile, as defined on the command line.
     *
     * @see Main#config
     * @return brainfile URI
     */
    public static String getBrainFile() {
        return Main.config.getString("brainfile");
    }

    /*
     * Private setters, used internally for validation of properties file.
     * I know this is cheap and dirty, but it was the easiest way to do it.
     * Sorry. It is actually convenient because it organises the sanity checks
     * for each setting, and looks better than just a bunch of if/else constructs
     * that may or may not throw an InvalidAIConfigException. This makes it easier
     * to add Properties, too.
     */

    /**
     * Set AI Name
     *
     * @param ainame
     * @throws InvalidAIConfigException
     */
    private static void setAiname(String ainame)
            throws InvalidAIConfigException {
        Configuration.ainame = ainame;
    }

    /**
     * Set Alternate Nick for IRC
     *
     * @param irc_altnickname
     * @throws InvalidAIConfigException
     */
    private static void setIrc_altnickname(String irc_altnickname) 
            throws InvalidAIConfigException {
        Configuration.irc_altnickname = irc_altnickname;
    }

    /**
     * Set IRC Channel
     *
     * @param irc_channel
     * @throws InvalidAIConfigException
     */
    private static void setIrc_channel(String irc_channel) 
            throws InvalidAIConfigException {
        if(irc_channel.startsWith("#")){
            Configuration.irc_channel = irc_channel;
        } else {
            throw new InvalidAIConfigException("IRC Channel must start with #");
        }
    }

    /**
     * Set irc_doidentify setting
     *
     * @param irc_doidentify  Do IRC ident?
     * @throws InvalidAIConfigException
     */
    private static void setIrc_doidentify(String irc_doidentify)
            throws InvalidAIConfigException {
        if(irc_doidentify.equalsIgnoreCase("true")){
            Configuration.irc_doidentify = true;
        } else if(irc_doidentify.equalsIgnoreCase("false")) {
            Configuration.irc_doidentify = false;
        } else {
            throw new InvalidAIConfigException("irc_doidentify must be 'true' or 'false'.");
        }
    }

    /**
     * Set IRC ident Password
     *
     * @param irc_identpassword
     * @throws InvalidAIConfigException
     */
    private static void setIrc_identpassword(String irc_identpassword)
            throws InvalidAIConfigException {
        Configuration.irc_identpassword = irc_identpassword;
    }

    /**
     * Set IRC Server hostname
     *
     * @param irc_server
     * @throws InvalidAIConfigException
     */
    private static void setIrc_server(String irc_server)
            throws InvalidAIConfigException {
        Configuration.irc_server = irc_server;
    }

    /**
     * Set maximum thread pool size for Task Manager.
     * Setter takes an integer because it is what the properties parser
     * will obviously return, and it makes it easier to react to someone
     * entering a string there. Sorry if it seems backwards.
     *
     * @param tm_maxthreads (must be an integer)
     * @throws InvalidAIConfigException
     */
    private static void setTm_maxthreads(String tm_maxthreads)
            throws InvalidAIConfigException {
        try{
            int mtvalue = Integer.parseInt(tm_maxthreads);
            Configuration.tm_maxthreads = mtvalue;
        } catch(NumberFormatException ex) {
            throw new InvalidAIConfigException("tm_maxthreads must be an integer.");
        }
    }

    /**
     * Spawn a Shell Server?
     *
     * @param debugshell (must be textual boolean)
     * @throws InvalidAIConfigException
     */
    private static void setDebug_shell(String debugshell)
            throws InvalidAIConfigException {
        if(debugshell.equalsIgnoreCase("true")){
            Configuration.debug_shell = true;
        } else if(debugshell.equalsIgnoreCase("false")){
            Configuration.debug_shell = false;
        } else {
            throw new InvalidAIConfigException("debugshell must be 'true' or 'false'.");
        }
    }

    /**
     * Set the TCP port to bind Shell Server to
     * Default: 9998
     *
     * @param debug_shell_port  TCP Port to bind Shell Server to
     * @throws InvalidAIConfigException
     */
    private static void setDebug_shell_port(String debug_shell_port)
            throws InvalidAIConfigException {
        int dsport;
        try{
            dsport = Integer.parseInt(debug_shell_port);
        } catch(NumberFormatException ex) {
            throw new InvalidAIConfigException("debug_shell_port must be an integer.");
        }

        // The crappiest validation ever.
        if(dsport > 65535 || dsport <= 1){
            throw new InvalidAIConfigException("debug_shell_port must be a valid TCP Port [1-65535]");
        } else {
            Configuration.debug_shell_port = dsport;
        }
    }

    /**
     * Set the host key file used by the SSH Server
     * Default: 'ssh_hostkey'
     *
     * @param ssh_hostkey Filename to use as SSH host key store
     * @throws InvalidAIConfigException
     */
    private static void setDebug_shell_hostkey(String debug_shell_hostkey)
            throws InvalidAIConfigException {
        //TODO: Implement basic validation
        Configuration.debug_shell_hostkey = debug_shell_hostkey;
    }

    /**
     * Get textual representation of Configuration
     * 
     * @return
     */
    public static String getConfiguration(){
        return "AI Name: " + Configuration.ainame + "\n"
                + "IRC Server: " + Configuration.irc_server + "\n"
                + "IRC Channel: " + Configuration.irc_channel + "\n"
                + "IRC Identify? " + Configuration.irc_doidentify.toString() + "\n"
                + "IRC Password? " + Configuration.irc_identpassword + "\n"
                + "IRC AltNick: " + Configuration.irc_altnickname + "\n"
                + "\n"
                + "** Optional Settings **\n"
                + "Daemon Task Manager Thread Pool Size: "
                + Configuration.tm_maxthreads + "\n"
                + "Spawn shell server: " + Configuration.debug_shell.toString() + "\n"
                + "Debug Shell TCP Port: " + Configuration.debug_shell_port + "\n";
    }
}
