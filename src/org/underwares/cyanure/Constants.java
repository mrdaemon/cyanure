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

import java.util.ResourceBundle;
import java.util.MissingResourceException;

/**
 *
 * @author gauthiera
 */
public class Constants {
    public static final String AUTHOR = "Alexandre Gauthier <alex@lab.underwares.org>";
    public static final String URL = "https://github.com/mrdaemon/cyanure";
    public static final String VERSION;
    public static final String BRANCH;
    public static final String REVISION;
    
    // Build Info
    public static final String BUILDTIME;
    public static final String BUILDER;
    public static final String BUILDHOST;
    public static final String BUILDPLATFORM;

    // System Properties
    public static final String PLATFORM;
    public static final String JVMINFO;

    static{
        try {
           ResourceBundle props =
                   ResourceBundle.getBundle("org.underwares.cyanure.version");
           
           VERSION = props.getString("version");
           BRANCH = props.getString("branch");
           REVISION = props.getString("revision");
           BUILDTIME = props.getString("buildtime");
           BUILDER = props.getString("builder");
           BUILDHOST = props.getString("buildhost");
           BUILDPLATFORM = props.getString("buildplatform");

        } catch(MissingResourceException ex) {
            // I AM LAZY: Default values for final members: haha no.
            throw new RuntimeException("INTERNAL ERROR: Missing property or " +
                    "ressource in version.properties." +
                    " Did you build with Ant?");
        }

        // Set System Properties
        PLATFORM = System.getProperty("os.name") + " "
                + System.getProperty("os.version") + " (" 
                + System.getProperty("os.arch") + ")";

        JVMINFO = "Java " + System.getProperty("java.version") + " ("
                + System.getProperty("java.vendor") + ")";

    }


    /**
     * Obtain a textual representation of the program Version,
     * including the words "Version", version number, branch name
     * and revision/build number.
     *
     * Example:
     *   Version 1.0.8-trunk, r2814
     *
     * @return Version information string
     */
    public static String getVersionString(){
        return "Version " + VERSION + "-" + BRANCH + " (" + REVISION + ")";
    }

    /**
     * Obtain a textual representation of the program build information,
     * including the words "Built:", the build time, the builder (user),
     * as well ass the builder's platform/host.
     *
     * Example:
     *   Built: Jun 15 2010 by user&#064;host (Linux)
     *
     * @param splitlines  Split the line with a \n between date and builder
     * @return Build information string
     */
    public static String getBuildInfoString(Boolean splitlines){
        String delim; // Split line delimiter

        if(splitlines){
            delim = "\nBy ";
        } else {
            delim = " by ";
        }
            return "Built: " + BUILDTIME + delim +
                    BUILDER + "@" +  BUILDHOST +
                    "(" + BUILDPLATFORM + ")";
    }

    public static String getBuildInfoString(){
        return getBuildInfoString(false);
    }

    /**
     * Ontain a textual representation of the platform running the
     * application, including the operating system and the JVM.
     *
     * Example:
     *
     *  Java 1.6.0_15 (Sun Microsystems) on Linux 2.6.35-22-generic (amd64)
     *
     * @return
     */
    public static String getPlatformInfoString(){
        return JVMINFO + " on " + PLATFORM;
    }
}
