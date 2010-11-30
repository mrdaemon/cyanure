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

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.sshd.SshServer;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;

import org.underwares.cyanure.sshd.NullFileSystemFactory;

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

    // Telnet Daemon instance member, wrapper of sorts.
    private SshServer daemon;

    // Current status
    private boolean active = false;

    /* Singleton class, so, Private Constructor. For now at least.
     * The idea is to eventually have a pool of these for various
     * purposes. This should prevent such an API change from wrecking havoc
     * all over the place.
     *
     * This constructor generates a server with the default, static config.
     */
    private ShellServer() throws ShellServerErrorException{
        System.out.println("Initializing Shell Server Daemon...");
        try {
            this.daemon = SshServer.setUpDefaultServer();
            this.daemon.setFileSystemFactory(new NullFileSystemFactory());
            this.daemon.setPort(Configuration.getDebug_shell_port());
            this.daemon.setKeyPairProvider(
                    new SimpleGeneratorHostKeyProvider(
                    Configuration.getDebug_shell_hostkey()));
        } catch (Exception ex) {
            //FIXME: Catching generic Exception here sucks. Don't.
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
    public void start() throws ShellServerErrorException{
        System.out.println("Starting Shell Server Daemon...");
        try {
            this.daemon.start();
        } catch (IOException ex) {
            throw new ShellServerErrorException(ex.getLocalizedMessage());
        }
        this.active = true;
        System.out.println("Shell Server Daemon listening on 0.0.0.0:" +
                Configuration.getDebug_shell_port());
    }

    public void shutdown() throws ShellServerErrorException {
        System.out.println("Shutting down Shell Server daemon...");
        if(this.isRunning()){
            try {
                this.daemon.stop();
            } catch (InterruptedException ex) {
                throw new ShellServerErrorException(ex.getLocalizedMessage());
            }
        } else {
            System.err.println("WARNING: Daemon was already dead?");
        }
        System.out.println("Shell Server Daemon terminated.");
    }
}
