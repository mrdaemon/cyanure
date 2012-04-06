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
import java.io.FileNotFoundException;
import java.io.IOException;

import org.jibble.pircbot.*;

import org.underwares.cyanure.ai.Soul;
import org.underwares.cyanure.Configuration;
import org.underwares.cyanure.Constants;
import org.underwares.cyanure.DaemonTaskManager;

/**
 * IRC connectivity Bridge
 * 
 * @author Alexandre Gauthier
 */
public class InternetChatRelay extends PircBot{

    Soul soul = null;
    DaemonTaskManager taskmanager = DaemonTaskManager.getInstance();

    /**
     * Construct an instance of the IRC Bridge
     *
     * @param soul  Soul Instance to use
     */
    public InternetChatRelay(Soul soul){
        this.setName(Configuration.getAiname());
        this.soul = soul;
    }

    /**
     * Respond to CTCP VERSION calls with Cyanure's current version number.
     * 
     * {@inheritDoc}
     */
    @Override
    protected void onVersion(String sourceNick, String sourceLogin, String sourceHostname, String target) {
        this.sendRawLine("NOTICE " + sourceNick + " :\u0001VERSION "
                + "Cyanure " + Constants.getVersionString() + "\u0001");
    }


    /**
     * Take action on message received through channel
     * 
     * {@inheritDoc}
     */
    @Override
    public void onMessage(String channel, String sender,
            String login, String hostname, String message){

        String input = ""; // interesting message content

        if(isConcernedBy(message)){
            /*
             * Detecting if the bot was directly addressed seems like the
             * absolute best course of action.
             *
             * It allows us to prevent commands from being triggered by mistake.
             */
            if(isDirectlyAddressedBy(message)){

                // Strip prefix from raw message, keeping meaningful contents.
                input = stripPrefix(message);

                // System Commands
                // TODO: Allow trigger to be customized
                // TODO: Cleanup this code. It is the ugliest and dirtiest.
                if (input.equalsIgnoreCase("*time")) {
                    String time = new java.util.Date().toString();
                    sendMessage(channel, sender + ": Local time here is " + time);
                } else if (input.equalsIgnoreCase("*talk")) {
                    sendMessage(channel, sender + ": " + soul.speak());
                } else if (input.equalsIgnoreCase("*save")) {
                    sendMessage(channel, "## Saving soul...");
                    try {
                        soul.save(Configuration.getBrainFile());
                    } catch (FileNotFoundException ex) {
                        sendMessage(channel, "## ERROR: File not found.");
                    } catch (IOException ex) {
                        sendMessage(channel, "## ERROR: Generic IO Exception.");
                    } catch (Exception ex) {
                        sendMessage(channel, "## ERROR: Unhandled Exception. Check logs for details.");
                    } finally {
                        sendMessage(channel, "## Done.");
                    }
                //} else if (input.equalsIgnoreCase("*stats")){
                    // unimplemented
                } else if (input.equalsIgnoreCase("*version") || input.equalsIgnoreCase("*about")) {
                    sendMessage(channel, sender + ": Multi Purpose Artificial Inelegance Program" +
                                            " - " + Constants.getVersionString() +
                                            " - (c) Alexandre Gauthier <alex@underwares.org>");
                    sendMessage(channel, sender + ": https://github.com/mrdaemon/cyanure");
                } else if (input.equalsIgnoreCase("*tasks")) {
                    sendMessage(channel, sender + ": " + taskmanager.toString());
                } else {
                    // That definitely was not a command.
                    // Converse with user as a direct reply.
                    sendMessage(channel, sender + ": " + soul.converse(input));
                }
            } else {
                // Message was not addressed to the bot.
                // Speak globally in channel.
                sendMessage(channel, soul.converse(input));
            }
        } else {
            // Was not concerned by message at all.
            // Just learn from it.
            soul.learn(message);
        }
    }

    /**
     * Determine if we should take interest in message,
     * This is based solely on the presence of our name within it.
     *
     * @param message  message to analyze
     * @return true  on interest.
     */
    private boolean isConcernedBy(String message){
        return message.matches("(?i)^(.*\\s+)?" + this.getNick() + "\\W?(\\s+.*)?(\\W+)?$");
    }

    /**
     * Determine if we were directly addressed, 
     * i.e. if the sentence began with our name.
     *
     * @param message  message to analyze
     * @return true  on prefix presence
     */
    private boolean isDirectlyAddressedBy(String message){
        return message.matches("(?i)^" + this.getNick() + ":?\\s?.*");
    }

    /**
     * Strip the prefix from a message directly addressed to the bot.
     * 
     * @param message  raw message to strip prefix from
     * @return  message contents
     */
    private String stripPrefix(String message){
        return message.replaceFirst("(?i)(" + this.getNick() + ":?\\s?)(.*)","$2");
    }
}
