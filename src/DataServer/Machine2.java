/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataServer;

import RMIServer.DSRMI;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Machine2 extends UnicastRemoteObject implements DSRMI{
    private static File dataDirectory;
    private static ArrayList<Integer> clients = new ArrayList();;
    private static Registry reg;
    
    public Machine2(File dataDirectory2) throws RemoteException {
        super();
        dataDirectory= dataDirectory2;
    }

    public File getDataDirectory() {
        return dataDirectory;
    }

    public void setDataDirectory(File dataDirectory2) {
        dataDirectory = dataDirectory2;
    }

    public ArrayList<Integer> getClients() {
        return clients;
    }

    
    public boolean addCredential(Integer c){
        return clients.add(c);
    }
    
    @Override
    public void printInServerSide(String msg) throws RemoteException {
        System.out.println(msg);
    }
    
    public static void main(String args[]) {
        //loadBinaryFile();
        try {
            dataDirectory = new File("./Data/Machine2/DFS");
            if (!dataDirectory.exists()) {
                try {
                    System.out.println("No Existe");
                    if (dataDirectory.mkdirs()) {
                        System.out.println("Success");
                    }else{
                        System.err.println("Fail");
                    }
                } catch (SecurityException se) {
                    System.err.println(se);
                }
            }else{
                System.out.println("Existe!");
            }
            reg = LocateRegistry.createRegistry(1103);
            reg.rebind("Machine2", new Machine2(dataDirectory));
            System.out.println("Machine2 started..");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public File getFile(String name) throws RemoteException {
        for (final File fileEntry : dataDirectory.listFiles()) {
            if (fileEntry.getName().equals(name)) {
                return fileEntry;
            }
        }
        System.err.println("No se encontro el archivo");
        return null;
    }
    @Override
    public boolean createFile(String content, String name) throws RemoteException {
        PrintWriter writer = null;
        name = dataDirectory.getAbsolutePath()+"\\"+name;
        System.out.println("Nombre del Archivo: "+name);
        try {
            writer = new PrintWriter(name, "UTF-8");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Machine1.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Machine1.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        writer.println(content);
        writer.close();
        return true;
    }
    @Override
    public boolean deleteFile(String name) throws RemoteException {
        name = dataDirectory.getAbsolutePath()+"\\"+name;
        File file = new File(name);
        return file.delete();
    }
    
    @Override
    public String getFileContent(String name)throws RemoteException{
         String collectedInfo = "";
            try {
                String line = "";
                name = dataDirectory.getAbsolutePath()+"\\"+name;
                FileReader f = new FileReader(name);
                BufferedReader b = new BufferedReader(f);
                while ((line = b.readLine()) != null) {
                    collectedInfo += line;
                }
                b.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return collectedInfo;
    }
    
}
