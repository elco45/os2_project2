package RMIServer;


import Client.entryNode;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;

public class RMIServer extends UnicastRemoteObject implements RMI {

    private static HashMap<String, Integer> connections = new HashMap();

    public RMIServer() throws RemoteException {
        super();
    }

    @Override
    public Object getNode() throws RemoteException {
        return new entryNode();
    }

    @Override
    public String getData(String text) throws RemoteException {
        text = "Hello " + text;
        return text;
    }

    @Override
    public void printInServerSide(String msg) throws RemoteException {
        System.out.println(msg);
    }

    static DefaultTreeModel archiveStructure = null;
    static int roundRobin = 1;

    public static DSRMI rmi1;
    public static DSRMI rmi2;
    public static DSRMI rmi3;

    public static void main(String args[]) {

        //loadBinaryFile();
        try {
            connections.put("Server", (1101));
            connections.put("Machine1", (1102));
            connections.put("Machine2", (1103));
            connections.put("Machine3", (1104));

            Registry reg = LocateRegistry.createRegistry(connections.get("Server"));
            reg.rebind("server", new RMIServer());
            System.out.println("Server started..");
            loadBinaryFile();

            Registry reg1 = LocateRegistry.getRegistry("127.0.0.1", connections.get("Machine1"));
            rmi1 = (DSRMI) reg1.lookup("Machine1");
            System.out.println("Connected to Machine1");
            //rmi1.printInServerSide("Popeye, Why you do this?");

            Registry reg2 = LocateRegistry.getRegistry("127.0.0.1", connections.get("Machine2"));
            rmi2 = (DSRMI) reg2.lookup("Machine2");
            System.out.println("Connected to Machine2");
            //rmi2.printInServerSide("Popeye, Why you do this? Again?");

            Registry reg3 = LocateRegistry.getRegistry("127.0.0.1", connections.get("Machine3"));
            rmi3 = (DSRMI) reg3.lookup("Machine3");
            System.out.println("Connected to Machine3");
            //rmi3.printInServerSide("Popeye, Why you do this? Over and Over Again?");

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private static void loadBinaryFile() {
        File file = null;
        try {
            file = new File("./archiveStructure.bin");
            //If file does not exist, it creates a new archiveStructure
            //which is the structure of the file in the system
            if (!file.exists()) {
                entryNode rootNode = new entryNode();
                DefaultMutableTreeNode root = new DefaultMutableTreeNode(rootNode);
                archiveStructure = new DefaultTreeModel(root);
                System.out.println("Lo cree");
            } else {

                //If file exists, it reads the file and cast
                //that file in order to load it in archiveStructure
                FileInputStream entry = new FileInputStream(file);
                ObjectInputStream object = new ObjectInputStream(entry);
                try {
                    archiveStructure = (DefaultTreeModel) object.readObject();
                    System.out.println("Lo lei");
                } catch (EOFException ex) {

                } finally {
                    //Always close the FileInputStream and the ObjectInputStream
                    //to avoid errors
                    object.close();
                    entry.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void saveToBinaryFile() {
        File file = null;
        try {
            //Save the archiveStructure into a binaryFile
            //this method should be executed before the program
            //finishes its execution
            file = new File("./archiveStructure.bin");
            FileOutputStream exit = new FileOutputStream(file);
            ObjectOutputStream object = new ObjectOutputStream(exit);
            object.writeObject(archiveStructure);
            object.flush();
            object.close();
            exit.close();

        } catch (Exception ext) {
            ext.printStackTrace();
        }
    }

    @Override
    public DefaultTreeModel getTreeModel() throws RemoteException {
        loadBinaryFile();
        return archiveStructure;
    }

    @Override
    public boolean addDirectory(DefaultMutableTreeNode Parent, String Name) throws RemoteException {
        if (!Name.endsWith("/")) {
            Name += "/";
        }
        entryNode hijo = new entryNode(Name, (entryNode) Parent.getUserObject(), -1, true);
        entryNode NodoPadre = (entryNode) Parent.getUserObject();

        DefaultMutableTreeNode root = (DefaultMutableTreeNode) archiveStructure.getRoot();
        Enumeration children = root.children();
        entryNode actual = (entryNode) root.getUserObject();
        DefaultMutableTreeNode daddy = searchForDaddy(root, NodoPadre);

        archiveStructure.insertNodeInto(new DefaultMutableTreeNode(hijo), daddy, 0);
        saveToBinaryFile();
        return true;
    }

    private static String getTreeText(DefaultTreeModel model, Object object, String indent) {
        String myRow = indent + object + "\n";
        for (int i = 0; i < model.getChildCount(object); i++) {
            myRow += getTreeText(model, model.getChild(object, i), indent + "  ");
        }
        return myRow;
    }

    private DefaultMutableTreeNode searchForDaddy(DefaultMutableTreeNode Iam, entryNode NodoPadre) {

        Enumeration children = Iam.children();
        entryNode actual;
        DefaultMutableTreeNode daddy;
        DefaultMutableTreeNode siguiente = null;
        Enumeration<DefaultMutableTreeNode> e = Iam.depthFirstEnumeration();

        if (NodoPadre.getFather() == null) {
            return Iam;
        } else {
            while (e.hasMoreElements()) {
                siguiente = (DefaultMutableTreeNode) e.nextElement();
                actual = (entryNode) siguiente.getUserObject();
                if (actual.getName().equals(NodoPadre.getName()) && actual.getFather().getName().equals(NodoPadre.getFather().getName())) {
                    return siguiente;
                }
            }
        }
        return null;

    }

    private void nextMachine() {
        if (roundRobin == 3) {
            roundRobin = 1;
        } else {
            roundRobin++;
        }
    }

    @Override
    public boolean addFile(String Name, DefaultMutableTreeNode Parent, String Text) throws RemoteException {
        if (!Name.endsWith(".txt")) {
            Name += ".txt";
        }

        Enumeration hijos = Parent.children();
        while (hijos.hasMoreElements()) {
            DefaultMutableTreeNode act = (DefaultMutableTreeNode) hijos.nextElement();
            entryNode nodoActual = (entryNode) act.getUserObject();
            if (nodoActual.getName().equals(Name)) {
                return false;
            }
        }

        entryNode hijo = new entryNode(Name, (entryNode) Parent.getUserObject(), roundRobin, false);

        entryNode NodoPadre = (entryNode) Parent.getUserObject();

        DefaultMutableTreeNode root = (DefaultMutableTreeNode) archiveStructure.getRoot();
        Enumeration children = root.children();
        entryNode actual = (entryNode) root.getUserObject();
        DefaultMutableTreeNode daddy = searchForDaddy(root, NodoPadre);

        String Path = getPath(hijo);
        System.out.println("Soy un path");
        System.out.println(Path);
        archiveStructure.insertNodeInto(new DefaultMutableTreeNode(hijo), daddy, 0);
        saveToBinaryFile();

        //MAGIA DE DATACENTERS PARAMS = TEXT,PATH
        if (roundRobin == 1) {
            if (!rmi1.createFile(Text, Path)) {
                System.out.println("No se pudo crear el archivo");
            }

        } else if (roundRobin == 2) {
            if (!rmi2.createFile(Text, Path)) {
                System.out.println("No se pudo crear el archivo");
            }
        } else {
            if (!rmi3.createFile(Text, Path)) {
                System.out.println("No se pudo crear el archivo");
            }
        }
        nextMachine();
        return true;
    }

    public String getPath(entryNode nodo) {
        String path = nodo.getName();

        while (nodo.getFather() != null) {
            path = nodo.getFather().getName() + path;
            nodo = nodo.getFather();
        }

        return path.replace('/', '#');
    }

    @Override
    public boolean deleteFile(DefaultMutableTreeNode nodo) throws RemoteException {
        entryNode toDel = (entryNode) nodo.getUserObject();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) archiveStructure.getRoot();
        DefaultMutableTreeNode papa = searchForDaddy(root,(entryNode)nodo.getUserObject());
        
        papa = searchForDaddy(root,(entryNode)papa.getUserObject());
        DefaultMutableTreeNode real = (DefaultMutableTreeNode) papa.getParent();
        int option = toDel.getDataNode();
        String name = getPath(toDel);
        
        entryNode FAGA = (entryNode)real.getUserObject();
        System.out.println("FAGA NAME:");
        System.out.println(FAGA.getName());
        Enumeration<DefaultMutableTreeNode> e = real.children();
        
        int index =real.getIndex(papa);
        real.remove(papa);
        archiveStructure.nodesWereRemoved(papa, new int[]{index}, null);
       
        
      
        
        if (option == 1) {
            saveToBinaryFile();
            return rmi1.deleteFile(name);
        } else if (option == 2) {
            saveToBinaryFile();
            return rmi2.deleteFile(name);
        } else {
            saveToBinaryFile();
            return rmi3.deleteFile(name);
        }
    }

    @Override
    public String streamFromServer(entryNode node) throws RemoteException {
        String name = getPath(node);
        System.out.println(node.getDataNode());
        String retrievedFile = "";
        if (node.getDataNode() == 1) {
            retrievedFile = rmi1.getFileContent(name);
        } else {
            if (node.getDataNode() == 2) {
                retrievedFile = rmi2.getFileContent(name);
            } else {
                if (node.getDataNode() == 3) {
                    retrievedFile = rmi3.getFileContent(name);
                }
            }
        }
        return retrievedFile;
    }
}
