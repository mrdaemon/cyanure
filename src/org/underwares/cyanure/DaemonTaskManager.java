package org.underwares.cyanure;
/* $Id: Configuration.java 1233 2010-06-12 04:48:30Z supernaut $
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

import java.util.ArrayList;
import java.util.List;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.TimeUnit.*;

/**
 * Daemon thread task scheduler
 * Handles scheduling of Runnables as background threads
 * at fixed intervals. Mostly a wrapper around 
 * <code>java.util.concurrent.Executors.</code>
 * @author Alexandre Gauthier
 */
public final class DaemonTaskManager {
    private final ScheduledExecutorService threadpool =
            Executors.newScheduledThreadPool(Configuration.getTm_maxthreads());

    private final List<ScheduledFuture> taskindex =
            new ArrayList<ScheduledFuture>();

    private static DaemonTaskManager instance = null;

    /**
     * Get current instance of DaemonTaskManager
     * @return DaemonTaskManager instance
     */
    public static DaemonTaskManager getInstance(){
        if(instance == null){
            instance = new DaemonTaskManager();
        }
        return instance;
    }

    /**
     * Schedule a Runnable task at a fixed interval.
     * @param task  Runnable class instance
     * @param intervalMinutes  Interval to run task at (in minutes)
     * @param oncenow  Run the task once at schedule time?
     */
    public void scheduleTask(Runnable task,
            int intervalMinutes, Boolean oncenow){
        final Runnable dtask = task;
        final int initialdelay;
        
        if(oncenow){
            initialdelay = 0;
        } else {
            initialdelay = intervalMinutes;
        }

        ScheduledFuture<?> taskHandle =
                threadpool.scheduleAtFixedRate(dtask, initialdelay,
                    intervalMinutes, MINUTES);
        // Add handle to tasklist
        taskindex.add(taskHandle);
    }

    /**
     * Schedule a Runnable task every 30 minutes.
     * @param task  Runnable class instance
     */
    public void scheduleTask(Runnable task){
        this.scheduleTask(task, 30, false);
    }

    /**
     * Schedule a Runnable task at a fixed interval.
     * @param task  Runnable class instance
     * @param intervalMinutes  Interval to run task at (in minutes)
     */
    public void scheduleTask(Runnable task, int intervalMinutes){
        this.scheduleTask(task, intervalMinutes, false);
    }


    /**
     * Cancel all the running daemon tasks handled by the manager,
     * interrupting them if necessary.
     */
    public void cancelAllTasks(){
        for(ScheduledFuture<?> task : taskindex){
            task.cancel(true);
            taskindex.remove(task);
        }
    }

    /**
     * Get amount of currently scheduled tasks
     * @return amount of scheduled tasks
     */
    public int getTaskCount(){
        return taskindex.size();
    }

    /**
     * Obtain a textual representation of the current task list state.
     * @return List of current tasks and their next scheduled run
     */
    @Override
    public String toString(){
        String summary = "Current Tasks: ";
        for(ScheduledFuture<?> task : taskindex){
            summary = summary + task.toString() +
                    "(t-" + task.getDelay(MINUTES) + " minutes) ";
        }
        return summary;
    }
}
