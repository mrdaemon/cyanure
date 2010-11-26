package org.underwares.cyanure;
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

import java.util.Properties;
import net.wimpi.telnetd.BootException;
import net.wimpi.telnetd.TelnetD;

/**
 * Manage and run the Shell Server process.
 * Acts as a wrapper around whatever server protocol or method
 * I elect to use.
 *
 * @author Alexandre Gauthier
 */
public class ShellServer {

    /* Private static members, instance (singleton),
     * and default server configuration, initialized below.
     */
    private static ShellServer instance = null;
    private static final Properties default_config;

    // Telnet Daemon instance member, wrapper of sorts.
    private TelnetD daemon;

    // Current status
    private boolean active = false;

    /*
     * Static constructor, initializes default configuration.
     */
    static {
        /*
         * Default Settings,
         * Bundled in a Properties object.
         */
        default_config = new Properties();

        /*
         * Default Terminals
         */
        default_config.setProperty("terminals", "vt100,ansi,windoof,xterm");

        // vt100 implementation
        default_config.setProperty("term.vt100.class",
                "net.wimpi.telnetd.io.terminal.vt100");
        default_config.setProperty("term.vt100.aliases",
                "default,vt100-am,vt102,dec-vt100");

        // ANSI implementation
        default_config.setProperty("term.ansi.class",
                "net.wimpi.telnetd.io.terminal.ansi");
        default_config.setProperty("term.ansi.aliases",
                "color-xterm,xterm-color,vt320,vt220,linux,screen");

        // Windows ANSI.SYS/telnet.exe implementation
        default_config.setProperty("term.windoof.class",
                "net.wimpi.telnetd.io.terminal.Windoof");
        default_config.setProperty("term.windoof.aliases","");

        // xterm implementation
        default_config.setProperty("term.xterm.class",
                "net.wimpi.telnetd.io.terminal.xterm");
        default_config.setProperty("term.xterm.aliases", "");

        /*
         * Shells and their implementations
         * At the moment, only DebugShell
         */
        default_config.setProperty("shells", "loginshell,dummyshell");
        default_config.setProperty("shell.loginshell.class",
                "org.underwares.cyanure.console.LoginShell");
        default_config.setProperty("shell.dummyshell.class", 
                "net.wimpi.telnetd.shell.DummyShell");

        /*
         * Listeners
         */
        default_config.setProperty("listeners", "std");

        // TCP Port fetched from global Configuration
        default_config.setProperty("std.port",
                Integer.toString(Configuration.getDebug_shell_port()));

        // Flood protection and max connections
        default_config.setProperty("std.floodprotection", "5");
        default_config.setProperty("std.maxcon", "1"); // Can't deal with mu yet

        // Timeout Values (ms)
        default_config.setProperty("std.time_to_warning", "3600000");
        default_config.setProperty("std.time_to_timedout", "60000");

        // Housekeeping Thread Activity Treshold
        default_config.setProperty("std.housekeepinginterval", "1000");

        // Listener input mode
        default_config.setProperty("std.inputmode", "character");

        // Login Shell
        default_config.setProperty("std.loginshell", "loginshell");

        // Connection filtering
        // TODO: Expose configurable property through Configuration for IP limits
        default_config.setProperty("std.connectionfilter", "none");
    }

    /* Singleton class, so, Private Constructor. For now at least.
     * The idea is to eventually have a pool of these for various
     * purposes. This should prevent such an API change from wrecking havoc
     * all over the place.
     *
     * This constructor generates a server with the default, static config.
     */
    private ShellServer() throws ShellServerErrorException {
        this(default_config);
    }

    /*
     * Actual private constructor. Left this option open,
     * should I ever wish to read the configuration from a Properties file.
     */
    private ShellServer(Properties config) throws ShellServerErrorException{
        System.out.println("Initializing Shell Server Daemon...");
        try {
            this.daemon = TelnetD.createTelnetD(config);
        } catch (BootException ex) {
            throw new ShellServerErrorException(ex.getLocalizedMessage());
        }
    }

    /**
     * Obtain a new ShellServer instance
     *
     * @return ShellServer Instance
     */
    public static ShellServer getInstance() throws ShellServerErrorException{
        if(instance == null){
            // Use default configuration for now.
            instance = new ShellServer();
        }
        return instance;
    }

    /**
     * Get current Daemon status
     * 
     * @return daemon status
     */
    public boolean isRunning(){
        // TODO: implement a more thourough check.
        return this.active;
    }

    /**
     * Start serving with current configuration.
     */
    public void start(){
        System.out.println("Starting Shell Server Daemon...");
        this.daemon.start();
        this.active = true;
        System.out.println("Shell Server Daemon listening on 0.0.0.0:" +
                Configuration.getDebug_shell_port());
    }

    public void shutdown() {
        System.out.println("Shutting down Shell Server daemon...");
        if(this.isRunning()){
            this.daemon.stop();
        } else {
            System.err.println("WARNING: Daemon was already dead?");
        }
        
        System.out.println("Shell Server Daemon terminated.");
    }
}
