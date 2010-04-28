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
 * Exposes members.
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

        // Set all private members
        Configuration.setAiname(config.getProperty("ainame"));
        Configuration.setIrc_server(config.getProperty("irc_server"));
        Configuration.setIrc_channel(config.getProperty("irc_channel"));
        Configuration.setIrc_doidentify(config.getProperty("irc_doidentify"));
        Configuration.setIrc_identpassword(config.getProperty("irc_identpassword"));
        Configuration.setIrc_altnickname(config.getProperty("irc_altnickname"));
    }

    /**
     * Get current AI Name
     * @return ainame
     */
    public static String getAiname() {
        return ainame;
    }

    /**
     * Get alternate IRC nickname
     * @return irc alt nick
     */
    public static String getIrc_altnickname() {
        return irc_altnickname;
    }

    /**
     * Get current IRC Channel
     * @return
     */
    public static String getIrc_channel() {
        return irc_channel;
    }

    /**
     * Inquire as if the bot is configured to do nickserv
     * identification
     * @return
     */
    public static Boolean getIrc_doidentify() {
        return irc_doidentify;
    }

    /**
     * Get current IRC nickserv password
     * @return
     */
    public static String getIrc_identpassword() {
        return irc_identpassword;
    }

    /**
     * Get current IRC Server
     * @return
     */
    public static String getIrc_server() {
        return irc_server;
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
     * @param ainame
     * @throws InvalidAIConfigException
     */
    private static void setAiname(String ainame)
            throws InvalidAIConfigException {
        Configuration.ainame = ainame;
    }

    /**
     * Set Alternate Nick for IRC
     * @param irc_altnickname
     * @throws InvalidAIConfigException
     */
    private static void setIrc_altnickname(String irc_altnickname) 
            throws InvalidAIConfigException {
        Configuration.irc_altnickname = irc_altnickname;
    }

    /**
     * Set IRC Channel
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

    private static void setIrc_identpassword(String irc_identpassword)
            throws InvalidAIConfigException {
        Configuration.irc_identpassword = irc_identpassword;
    }

    private static void setIrc_server(String irc_server)
            throws InvalidAIConfigException {
        Configuration.irc_server = irc_server;
    }

    /**
     * Get textual representation of Configuration
     * @return
     */
    public static String getConfiguration(){
        return "AI Name: " + Configuration.ainame + "\n"
                + "IRC Server: " + Configuration.irc_server + "\n"
                + "IRC Channel: " + Configuration.irc_channel + "\n"
                + "IRC Identify? " + Configuration.irc_doidentify.toString() + "\n"
                + "IRC Password? " + Configuration.irc_identpassword + "\n"
                + "IRC AltNick: " + Configuration.irc_altnickname;
    }
}
