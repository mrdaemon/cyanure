package org.underwares.cyanure.tasks;
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
import java.util.concurrent.TimeUnit.*;

import org.underwares.cyanure.ai.Soul;
import org.underwares.cyanure.Configuration;

/**
 * Runnable task that saves the Soul object to disk
 * @author Alexandre Gauthier
 */
public class SaveSoulTask implements Runnable {
    
    private Soul soulobject;

    public SaveSoulTask(Soul soulobject) {
        this.soulobject = soulobject;
    }

    public void run() {
        try {
            System.out.println(this.getClass().getSimpleName() +
                    ": Saving soul...");
            soulobject.save(Configuration.getBrainFile());
            System.out.println(this.getClass().getSimpleName() +
                    ": Soul saved.");
        } catch (FileNotFoundException ex) {
            System.err.println("Unable to save our soul! " +
                    Configuration.getBrainFile() + ": No such file!");
        } catch (IOException ex) {
            System.err.println("Unable to save our soul! " +
                    Configuration.getBrainFile() + ": IO Error.");
            System.err.println(ex.getMessage());
        } catch (Exception ex) {
            System.err.println("Unhandled exception while saving soul:");
            System.err.println(ex.getStackTrace().toString());
        }
    }

}
