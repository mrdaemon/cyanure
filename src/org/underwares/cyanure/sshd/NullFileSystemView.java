/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.underwares.cyanure.sshd;

import org.apache.sshd.server.FileSystemView;
import org.apache.sshd.server.SshFile;
/**
 *
 * @author supernaut
 */
public class NullFileSystemView implements FileSystemView{

    public SshFile getFile(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
