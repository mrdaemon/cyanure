/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.underwares.cyanure.sshd;

import org.apache.sshd.server.FileSystemFactory;
import org.apache.sshd.server.FileSystemView;
/**
 *
 * @author supernaut
 */
public class NullFileSystemFactory implements FileSystemFactory{

    public FileSystemView createFileSystemView(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
