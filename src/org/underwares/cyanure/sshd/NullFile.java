/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.underwares.cyanure.sshd;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import org.apache.sshd.server.SshFile;
/**
 *
 * @author supernaut
 */
public class NullFile implements SshFile {

    public String getAbsolutePath() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getName() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isDirectory() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isFile() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean doesExist() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isReadable() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isWritable() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isRemovable() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public SshFile getParentFile() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public long getLastModified() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean setLastModified(long l) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public long getSize() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean mkdir() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean delete() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void truncate() throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean move(SshFile sf) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<SshFile> listSshFiles() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public OutputStream createOutputStream(long l) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public InputStream createInputStream(long l) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void handleClose() throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
