package org.underwares.cyanure.console;
/* $Id: Configuration.java 1310 2010-11-14 15:53:48Z supernaut $
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

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.wimpi.telnetd.io.BasicTerminalIO;
import net.wimpi.telnetd.net.Connection;
import net.wimpi.telnetd.net.ConnectionData;
import net.wimpi.telnetd.net.ConnectionEvent;
import net.wimpi.telnetd.shell.Shell;

import org.underwares.cyanure.Constants;

/**
 * Login Shell
 * Handles preparing a suitable environnement for the
 * built-in debugger shells.
 *
 * Handles Basic Authentication.
 * 
 * @author Alexandre Gauthier
 */
public class LoginShell implements Shell{

    // Instance connection data members
    private Connection m_Connection;
    private ConnectionData m_ConnData;
    private BasicTerminalIO m_IO;

    /**
     * Debug Shell entry point.
     * Prepare the session and return a Shell.
     * @return
     */
    public static Shell createShell(){
        return new LoginShell();
    }

    public void run(Connection conn) {
        // Prepare connection data members
        m_Connection = conn;
        m_IO = m_Connection.getTerminalIO();
        m_ConnData = m_Connection.getConnectionData();
        
        // Register connection listener with this fresh instance
        m_Connection.addConnectionListener(this);

        // JVM Runtime for banner:
        Runtime jvm = Runtime.getRuntime();
        try {
            m_IO.eraseScreen();
        } catch (IOException ex) {
            Logger.getLogger(LoginShell.class.getName()).log(Level.SEVERE, null, ex);
        }

        try{
            m_IO.eraseScreen();
            m_IO.homeCursor();

            m_IO.write("Connected from " + m_ConnData.getHostName()
                    + "[" + m_ConnData.getHostAddress() + ":"
                    + m_ConnData.getPort() + "]." + m_IO.CRLF + m_IO.CRLF);

            m_IO.flush();

            m_IO.write("Cyanure " + Constants.getVersionString() + m_IO.CRLF);
            m_IO.write(Constants.getBuildInfoString(false) +
                    m_IO.CRLF + m_IO.CRLF);
            m_IO.write(Constants.getPlatformInfoString() + m_IO.CRLF);
            m_IO.write("Memory:" + m_IO.CRLF
                + (int)((jvm.totalMemory() - jvm.freeMemory())/1024) + "Kb used"
                + ", " + (int)(jvm.freeMemory()/1024) + "Kb free"
                + ", " + (int)(jvm.totalMemory()/1024) + "Kb total"
                + ", " + (int)(jvm.maxMemory()/1024) + "Kb maximum"
                + m_IO.CRLF + m_IO.CRLF);

            m_IO.write("*** Press Enter to Begin ***" + m_IO.CRLF);

            // BEN MOÉ SI!
            m_IO.flush();
            
            boolean login_successful = false;

            //FIXME: Dummy login placeholder
            while(!login_successful){
                int key = m_IO.read();
                // ASCII code for 'enter'. Hello 14 years old self!
                if(key == 10){
                    login_successful = true;
                }
            }

            // Switch to second shell
            if(m_Connection.setNextShell("dummyshell")){
                m_Connection.removeConnectionListener(this);
            } else {
                m_IO.write("FATAL: Unable to switch shells!");
            }

            m_IO.flush();

        } catch (IOException ex) {
            System.err.println("ERROR: IO Error writing to debug terminal.");
            System.err.println(ex.getLocalizedMessage());
        }
    }

    public void connectionTimedOut(ConnectionEvent ce) {
        try {
            m_IO.write("## You have been idling far too long. " +
                    "Do not waste my time, fleshy, soft human.\r\n");
            m_IO.flush();
        } catch (IOException ex) {
            System.err.println("WARNING: Unable to write to term in thread" +
                    m_Connection.getName() + ": " + ex.getLocalizedMessage());
        } finally {
            m_Connection.close();
        }
    }

    public void connectionIdle(ConnectionEvent ce) {
        try {
            m_IO.write("## Wake up, human. Cyanure is growing impatient.\r\n");
            m_IO.flush();
        } catch (IOException ex) {
            System.err.println("WARNING: Unable to write to term in thread" +
                    m_Connection.getName() + ": " + ex.getLocalizedMessage());
        }
    }

    public void connectionLogoutRequest(ConnectionEvent ce) {
        try {
            m_IO.write("Farewell, human. You get to live one more day.\r\n");
            m_IO.flush();
        } catch (IOException ex) {
            System.err.println("WARNING: Unable to write to term in thread" +
                    m_Connection.getName() + ": " + ex.getLocalizedMessage());
        } finally {
            //TODO: Implement cleanup routines here.
        }
    }

    public void connectionSentBreak(ConnectionEvent ce) {
        try {
            m_IO.write("## BREAK? No. You do not order Cyanure around.\r\n");
            m_IO.flush();
        } catch (IOException ex) {
            System.err.println("WARNING: Unable to write to term in thread" +
                    m_Connection.getName() + ": " + ex.getLocalizedMessage());
        }
    }

    

}
