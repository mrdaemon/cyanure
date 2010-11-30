/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.underwares.cyanure.console;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.sshd.server.Command;
import org.apache.sshd.server.Environment;
import org.apache.sshd.server.ExitCallback;

/**
 *
 * @author supernaut
 */
public class DebugShell implements Command{

    private InputStream cin;
    private OutputStream cout;
    private OutputStream cerr;
    private ExitCallback exit_cb;

    public void setInputStream(InputStream in) {
        this.cin = in;
    }

    public void setOutputStream(OutputStream out) {
        this.cout = out;
    }

    public void setErrorStream(OutputStream out) {
        this.cerr = out;
    }

    public void setExitCallback(ExitCallback ec) {
        this.exit_cb = ec;
    }

    public void start(Environment e) throws IOException {
        
    }

    public void destroy() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
