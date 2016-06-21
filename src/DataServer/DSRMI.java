/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataServer;

import java.io.File;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface DSRMI extends Remote {

    public void printInServerSide(String msg) throws RemoteException;

    public File getFile(String name) throws RemoteException;

    public String getFileContent(String name) throws RemoteException;

    public boolean createFile(String content, String name) throws RemoteException;

    public boolean editFile(String content, String name) throws RemoteException;

    public boolean deleteFile(String name) throws RemoteException;

    public boolean deleteDir(String name) throws RemoteException;
}
