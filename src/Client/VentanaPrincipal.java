package Client;

import RMIServer.RMI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

public class VentanaPrincipal extends JFrame {

    JPanel Panel = new JPanel();
    JTextArea TextArea = new JTextArea(25, 25);
    JTree Tree = new JTree();
    JScrollPane ScrollPane;

    public VentanaPrincipal() throws RemoteException {
        super("Ventana Principal");
        this.dispose();
        this.setLocationRelativeTo(null);
        this.setSize(615, 480);

        Panel.setBackground(Color.GRAY);
        final RMI serverConn = initRMI();
        Tree.setModel(serverConn.getTreeModel());
        DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) Tree.getCellRenderer();
        MyRenderer newRender = new MyRenderer(renderer.getDefaultClosedIcon(), renderer.getDefaultLeafIcon());

        Tree.setCellRenderer(newRender);

        Tree.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    if (e.getClickCount() == 2) {
                        TreePath path = Tree.getPathForLocation(e.getX(), e.getY());
                        Rectangle pathBounds = Tree.getUI().getPathBounds(Tree, path);
                        if (pathBounds != null && pathBounds.contains(e.getX(), e.getY())) {
                            DefaultTreeModel model = (DefaultTreeModel) Tree.getModel();
                            TreePath tp = Tree.getSelectionPath();
                            DefaultMutableTreeNode parent = (DefaultMutableTreeNode) tp.getLastPathComponent();
                            entryNode nodo = (entryNode) parent.getUserObject();

                            if (!nodo.isDir()) {
                                try {
                                    // open archivo
                                    TextArea.setText(serverConn.streamFromServer(nodo));

                                } catch (RemoteException ex) {
                                    Logger.getLogger(VentanaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            } else {
                                System.out.println("no es archivo");
                            }
                        }
                    }
                }
                if (SwingUtilities.isRightMouseButton(e)) {
                    TreePath path = Tree.getPathForLocation(e.getX(), e.getY());
                    try {
                        Rectangle pathBounds = Tree.getUI().getPathBounds(Tree, path);
                        if (pathBounds != null && pathBounds.contains(e.getX(), e.getY())) {
                            DefaultTreeModel model = (DefaultTreeModel) Tree.getModel();
                            TreePath tp = Tree.getSelectionPath();
                            DefaultMutableTreeNode parent = (DefaultMutableTreeNode) tp.getLastPathComponent();
                            entryNode nodo = (entryNode) parent.getUserObject();

                            JPopupMenu menu = new JPopupMenu();
                            if (nodo.isDir()) {
                                JMenuItem MenuItemFile = new JMenuItem("Crear Archivo");
                                menu.add(MenuItemFile);
                                JMenuItem MenuItemDirectory = new JMenuItem("Crear Directorio");
                                menu.add(MenuItemDirectory);
                                JMenuItem menuItemDirectoryBorrar = new JMenuItem("Borrar Directorio");
                                menu.add(menuItemDirectoryBorrar);

                                menu.show(Tree, pathBounds.x, pathBounds.y + pathBounds.height);
                                MenuItemFile.addActionListener(
                                        new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent Event) {
                                        try {
                                            String Text = TextArea.getText();
                                            DefaultTreeModel model = (DefaultTreeModel) Tree.getModel();
                                            TreePath tp = Tree.getSelectionPath();
                                            String archivo = JOptionPane.showInputDialog("Ingrese el nombre del archivo");

                                            DefaultMutableTreeNode parent = (DefaultMutableTreeNode) tp.getLastPathComponent();

                                            if (serverConn.addFile(archivo, parent, Text)) {
                                                Tree.setModel(serverConn.getTreeModel());
                                                ((DefaultTreeModel) Tree.getModel()).reload();
                                            } else {

                                                System.out.println("No se pudo");
                                            }
                                        } catch (RemoteException ex) {
                                            Logger.getLogger(VentanaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                    }
                                });

                                //borrar dirrectorio 
                                menuItemDirectoryBorrar.addActionListener(
                                        new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent Event) {
                                        System.out.println("Borrar Dir");
                                        DefaultTreeModel model = (DefaultTreeModel) Tree.getModel();
                                        TreePath tpo = Tree.getSelectionPath();
                                        System.out.println(tpo);

                                        try {
                                            if (serverConn.deletedir(tpo)) {
                                                Tree.setModel(serverConn.getTreeModel());
                                                ((DefaultTreeModel) Tree.getModel()).reload();
                                                System.out.println("se borro");
                                            } else {
                                                System.out.println("No se pudo");
                                            }
                                        } catch (RemoteException ex) {
                                            Logger.getLogger(VentanaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
                                        }

                                    }
                                }
                                );

                                MenuItemDirectory.addActionListener(
                                        new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent Event) {
                                        try {
                                            System.out.println("Directorio");
                                            DefaultTreeModel model = (DefaultTreeModel) Tree.getModel();
                                            TreePath tp = Tree.getSelectionPath();
                                            String Dir = JOptionPane.showInputDialog("Ingrese el nombre del Directorio");

                                            DefaultMutableTreeNode parent = (DefaultMutableTreeNode) tp.getLastPathComponent();

                                            if (serverConn.addDirectory(parent, Dir)) {
                                                Tree.setModel(serverConn.getTreeModel());
                                                ((DefaultTreeModel) Tree.getModel()).reload();
                                            } else {

                                                System.out.println("No se pudo");
                                            }
                                        } catch (Exception ex) {
                                            Logger.getLogger(VentanaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                    }
                                });
                            } else {
                                JMenuItem MenuItemDelete = new JMenuItem("Borrar Archivo");
                                menu.add(MenuItemDelete);

                                menu.show(Tree, pathBounds.x, pathBounds.y + pathBounds.height);

                                DefaultTreeModel modelo = (DefaultTreeModel) Tree.getModel();
                                TreePath tpo = Tree.getSelectionPath();
                                final DefaultMutableTreeNode parento = (DefaultMutableTreeNode) tpo.getLastPathComponent();

                                MenuItemDelete.addActionListener(
                                        new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent Event) {

                                        try {
                                            if (serverConn.deleteFile(parento)) {
                                                Tree.setModel(serverConn.getTreeModel());
                                                ((DefaultTreeModel) Tree.getModel()).reload();
                                            } else {
                                                JOptionPane.showMessageDialog(null, "No se puede acceder en estos momentos al servidor");
                                            }
                                        } catch (RemoteException ex) {
                                            Logger.getLogger(VentanaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
                                        }

                                    }
                                });
                            }
                        }
                    } catch (Exception ex) {

                    }

                }
            }
        });

        ScrollPane = new JScrollPane(Tree);
        Dimension size = new Dimension(260, 400);
        ScrollPane.setPreferredSize(size);
        ScrollPane.setMinimumSize(size);
        Panel.add(ScrollPane);
        Panel.add(TextArea);

        add(Panel);
        this.setVisible(true);
    }

    public static void main(String[] args) throws RemoteException {
        new VentanaPrincipal();
    }

    private RMI initRMI() {
        try {
            Registry reg = LocateRegistry.getRegistry("127.0.0.1", 1101);

            RMI rmi = (RMI) reg.lookup("server");
            System.out.println("Client connection to the server was successful");
            return rmi;
        } catch (Exception ex) {
            Logger.getLogger(VentanaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;

    }

    private void addDirectory(DefaultMutableTreeNode Father, String Name) {

        DefaultTreeModel model = (DefaultTreeModel) Tree.getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
//        entryNode novo = new entryNode(Name, (entryNode) Father.getUserObject(), -1, true);
        model.insertNodeInto(new DefaultMutableTreeNode(Name), Father, 0);
    }

    private void addTextFile(DefaultMutableTreeNode Father, String Name, String Content) {

    }

}
