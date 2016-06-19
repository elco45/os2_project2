/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import RMIServer.RMI;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

/**
 *
 * @author Kevin Barahona
 */
public class Client extends javax.swing.JFrame {

    /**
     * Creates new form Client
     */
    public static RMI Server;

    public Client() throws RemoteException, NotBoundException {
        initComponents();
        Registry reg = LocateRegistry.getRegistry("127.0.0.1", 1101);
        Server = (RMI) reg.lookup("server");
        System.out.println("Client connection to the server was successful");
        fileTree.setModel(Server.getTreeModel());
        JMenuItem MenuItemFile = new JMenuItem("Crear Archivo");
        jPopupMenu1.add(MenuItemFile);
        MenuItemFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent Event) {
                DefaultTreeModel model = (DefaultTreeModel) fileTree.getModel();
                TreePath tp = fileTree.getSelectionPath();
                DefaultMutableTreeNode parent = (DefaultMutableTreeNode) tp.getLastPathComponent();
                FileDialog dialog = new FileDialog(false, parent);
                dialog.setVisible(true);
                try {
                    fileTree.setModel(Server.getTreeModel());
                    ((DefaultTreeModel) fileTree.getModel()).reload();
                } catch (RemoteException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
                ((DefaultTreeModel) fileTree.getModel()).reload();
            }
        });

        JMenuItem MenuItemDirectory = new JMenuItem("Crear Directorio");
        jPopupMenu1.add(MenuItemDirectory);
        MenuItemDirectory.addActionListener(
                new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent Event) {
                try {
                    System.out.println("Directorio");
                    DefaultTreeModel model = (DefaultTreeModel) fileTree.getModel();
                    TreePath tp = fileTree.getSelectionPath();
                    String Dir = JOptionPane.showInputDialog("Ingrese el nombre del directorio");
                    DefaultMutableTreeNode parent = (DefaultMutableTreeNode) tp.getLastPathComponent();
                    if (Server.addDirectory(parent, Dir)) {
                        fileTree.setModel(Server.getTreeModel());
                        ((DefaultTreeModel) fileTree.getModel()).reload();
                    } else {
                        System.out.println("No se pudo");
                    }
                    ((DefaultTreeModel) fileTree.getModel()).reload();
                } catch (Exception ex) {
                    Logger.getLogger(VentanaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        JMenuItem menuItemDirectoryBorrar = new JMenuItem("Borrar Directorio");
        jPopupMenu1.add(menuItemDirectoryBorrar);
        menuItemDirectoryBorrar.addActionListener(
                new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent Event) {
                try {
                    TreePath tpos = fileTree.getSelectionPath();
                    DefaultMutableTreeNode parento = (DefaultMutableTreeNode) tpos.getLastPathComponent();
                    if (Server.deletedir(parento)) {
                        fileTree.setModel(Server.getTreeModel());
                        ((DefaultTreeModel) fileTree.getModel()).reload();
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
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPopupMenu1 = new javax.swing.JPopupMenu();
        jScrollPane1 = new javax.swing.JScrollPane();
        fileTree = new javax.swing.JTree();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        fileTree.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                fileTreeMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(fileTree);

        jList1.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(jList1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void fileTreeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fileTreeMouseClicked
        if (SwingUtilities.isLeftMouseButton(evt)) {
            DefaultTreeModel model = (DefaultTreeModel) fileTree.getModel();
            TreePath tp = fileTree.getSelectionPath();
            if (tp == null) {
                return;
            }
            DefaultMutableTreeNode parent = (DefaultMutableTreeNode) tp.getLastPathComponent();
            entryNode nodo = (entryNode) parent.getUserObject();
            if (!nodo.isDir()) {
                try {
                    System.out.println(Server.streamFromServer(nodo));
                } catch (RemoteException ex) {
                    Logger.getLogger(VentanaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                System.out.println("no es archivo");
            }
        }
        if (SwingUtilities.isRightMouseButton(evt)) {
            TreePath path = fileTree.getPathForLocation(evt.getX(), evt.getY());
            Rectangle pathBounds = fileTree.getUI().getPathBounds(fileTree, path);
            if (pathBounds != null && pathBounds.contains(evt.getX(), evt.getY())) {
                DefaultTreeModel model = (DefaultTreeModel) fileTree.getModel();
                TreePath tp = fileTree.getSelectionPath();
                if (tp == null) {
                    return;
                }
                DefaultMutableTreeNode parent = (DefaultMutableTreeNode) tp.getLastPathComponent();
                entryNode nodo = (entryNode) parent.getUserObject();
                if (nodo.isDir()) {
                    jPopupMenu1.show(fileTree, pathBounds.x, pathBounds.y + pathBounds.height);
                }
            }

        }

    }//GEN-LAST:event_fileTreeMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new Client().setVisible(true);
                } catch (RemoteException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                } catch (NotBoundException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTree fileTree;
    private javax.swing.JList<String> jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables
}
