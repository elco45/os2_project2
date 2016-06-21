package ServerNode;

import DataServer.DSRMI;
import Client.entryNode;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

public class ServerNode extends UnicastRemoteObject implements RMI {

    private static HashMap<String, Integer> connections = new HashMap();

    public ServerNode() throws RemoteException {
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
    private static int dataServerConnected = 0;
    static int roundRobin = 1;
    public static List<DSRMI> listDataServer = new LinkedList<>();
    public static List<DSRMI> listDataServerReplica = new LinkedList<>();

    public static void main(String args[]) {
        try {
            connections.put("Server", (1101));
            connections.put("DataServer1", (1102));
            connections.put("DataServer2", (1103));
            connections.put("Replica1", (1104));
            connections.put("Replica2", (1105));

            Registry reg = LocateRegistry.createRegistry(connections.get("Server"));
            reg.rebind("server", new ServerNode());
            System.out.println("Server started..");
            loadBinaryFile();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private static void loadBinaryFile() {
        File file = null;
        try {
            file = new File("./archiveStructure.bin");
            if (!file.exists()) {
                entryNode rootNode = new entryNode();
                DefaultMutableTreeNode root = new DefaultMutableTreeNode(rootNode);
                archiveStructure = new DefaultTreeModel(root);
                System.out.println("Lo cree");
            } else {
                FileInputStream entry = new FileInputStream(file);
                try (ObjectInputStream object = new ObjectInputStream(entry)) {
                    archiveStructure = (DefaultTreeModel) object.readObject();
                    System.out.println("Lo lei");
                } catch (EOFException ex) {

                } finally {
                    entry.close();
                }
            }
        } catch (IOException | ClassNotFoundException e) {
        }
    }

    private static void saveToBinaryFile() {
        File file = null;
        try {
            file = new File("./archiveStructure.bin");
            try (FileOutputStream exit = new FileOutputStream(file)) {
                ObjectOutputStream object = new ObjectOutputStream(exit);
                object.writeObject(archiveStructure);
                object.flush();
                object.close();
            }

        } catch (Exception ext) {
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
        DefaultMutableTreeNode daddy = searchForDaddy(root, NodoPadre);

        archiveStructure.insertNodeInto(new DefaultMutableTreeNode(hijo), daddy, 0);
        saveToBinaryFile();
        return true;
    }

    private DefaultMutableTreeNode searchForDaddy(DefaultMutableTreeNode Iam, entryNode NodoPadre) {
        entryNode actual;
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

    private void nextDataServer() {
        if (roundRobin == dataServerConnected) {
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
        DefaultMutableTreeNode daddy = searchForDaddy(root, NodoPadre);

        String Path = getPath(hijo);
        System.out.println("Soy un path");
        System.out.println(Path);
        archiveStructure.insertNodeInto(new DefaultMutableTreeNode(hijo), daddy, 0);
        saveToBinaryFile();
        if (!listDataServer.get(roundRobin - 1).createFile(Text, Path)) {
            System.out.println("No se pudo crear el archivo");
        }

        if (!listDataServerReplica.get(roundRobin - 1).createFile(Text, Path)) {
            System.out.println("No se pudo crear el archivo");
        }
        nextDataServer();
        return true;
    }

    public String getPath(entryNode nodo) {
        String path = nodo.getName();

        while (nodo.getFather() != null) {
            path = nodo.getFather().getName() + path;
            nodo = nodo.getFather();
        }

        return path;
    }

    @Override
    public boolean deleteFile(DefaultMutableTreeNode nodo) throws RemoteException {
        entryNode toDel = (entryNode) nodo.getUserObject();
        String path = getPath(toDel);
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) archiveStructure.getRoot();
        DefaultMutableTreeNode papa = searchForDaddy(root, (entryNode) nodo.getUserObject());

        papa = searchForDaddy(root, (entryNode) papa.getUserObject());
        DefaultMutableTreeNode real = (DefaultMutableTreeNode) papa.getParent();
        int option = toDel.getDataNode();
        String name = getPath(toDel);
        int index = real.getIndex(papa);
        real.remove(papa);
        archiveStructure.nodesWereRemoved(papa, new int[]{index}, null);

        saveToBinaryFile();

        for (DSRMI tmp : listDataServer) {
            tmp.deleteDir(path);
        }
        for (DSRMI tmps : listDataServerReplica) {
            tmps.deleteDir(path);
        }
        return listDataServer.get(option - 1).deleteFile(name) && listDataServerReplica.get(option - 1).deleteFile(name);

    }

    @Override
    public String streamFromServer(entryNode node) throws RemoteException {
        String name = getPath(node);
        System.out.println(node.getDataNode());
        String retrievedFile = "";

        retrievedFile = listDataServer.get(node.getDataNode() - 1).getFileContent(name);

        return retrievedFile;
    }

    @Override
    public void addDataServer(String IP, int Port, String Name) throws RemoteException, NotBoundException {
        Registry reg1 = LocateRegistry.getRegistry(IP, Port);
        if (Port == 1102) {
            listDataServer.add((DSRMI) reg1.lookup(Name));
            System.out.println("Connected to " + Name);
            dataServerConnected++;
        } else if (Port == 1103) {
            listDataServer.add((DSRMI) reg1.lookup(Name));
            System.out.println("Connected to " + Name);
            dataServerConnected++;
        } else if (Port == 1104) {
            listDataServerReplica.add((DSRMI) reg1.lookup(Name));
            System.out.println("Connected to " + Name);
        } else if (Port == 1105) {
            listDataServerReplica.add((DSRMI) reg1.lookup(Name));
            System.out.println("Connected to " + Name);
        }
    }

    @Override
    public boolean deletedir(DefaultMutableTreeNode nodo) throws RemoteException {

        entryNode toDel = (entryNode) nodo.getUserObject();
        String path = getPath(toDel);
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) archiveStructure.getRoot();
        DefaultMutableTreeNode papa = searchForDaddy(root, (entryNode) nodo.getUserObject());

        papa = searchForDaddy(root, (entryNode) papa.getUserObject());
        DefaultMutableTreeNode real = (DefaultMutableTreeNode) papa.getParent();
        int option = toDel.getDataNode();
        System.out.println(option);

        entryNode FAGA = (entryNode) real.getUserObject();

        int index = real.getIndex(papa);
        real.remove(papa);
        archiveStructure.nodesWereRemoved(papa, new int[]{index}, null);

        saveToBinaryFile();

        for (DSRMI tmp : listDataServer) {
            tmp.deleteDir(path);
        }
        return true;

    }

    @Override
    public boolean editFile(String Name, entryNode nodo, String Text) throws RemoteException {
        String Path = getPath(nodo);
        System.out.println("Soy un path");
        System.out.println(Path);
        saveToBinaryFile();

        if (!listDataServer.get(nodo.getDataNode() - 1).editFile(Text, Path)) {
            System.out.println("No se pudo crear el archivo");
        }
        return true;
    }
}
