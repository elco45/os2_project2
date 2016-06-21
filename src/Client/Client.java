/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import ServerNode.RMI;
import java.awt.Color;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
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

        DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) fileTree.getCellRenderer();
        MyRenderer newRender = new MyRenderer(renderer.getDefaultClosedIcon(), renderer.getDefaultLeafIcon());
        fileTree.setCellRenderer(newRender);

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
                FileDialog dialog = null;
                try {
                    dialog = new FileDialog(false, (entryNode) parent.getUserObject(), parent, fileTree);
                } catch (RemoteException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
                dialog.setVisible(true);
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

                }
            }
        });
        System.out.println(fileTree.getSelectionPath());
        JMenuItem menuItemDirectoryBorrar = new JMenuItem("Borrar Directorio");
        jPopupMenu1.add(menuItemDirectoryBorrar);
        menuItemDirectoryBorrar.addActionListener(
                new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent Event) {
                try {
                    TreePath tpos = fileTree.getSelectionPath();
                    DefaultMutableTreeNode parento = (DefaultMutableTreeNode) tpos.getLastPathComponent();
                    if (Server.deletedir(parento) && !parento.isRoot()) {
                        fileTree.setModel(Server.getTreeModel());
                        ((DefaultTreeModel) fileTree.getModel()).reload();
                        System.out.println("se borro");
                    } else {
                        System.out.println("No se pudo");
                    }
                } catch (Exception ex) {
                    System.out.println("No se pudo");
                }

            }
        }
        );

        JMenuItem editFile = new JMenuItem("Editar archivo");
        jPopupMenu2.add(editFile);
        editFile.addActionListener(
                new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent Event) {
                TreePath tp = fileTree.getSelectionPath();
                DefaultMutableTreeNode parent = (DefaultMutableTreeNode) tp.getLastPathComponent();
                FileDialog dialog = null;
                try {
                    dialog = new FileDialog(true, (entryNode) parent.getUserObject(), parent, fileTree);
                } catch (RemoteException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
                dialog.setVisible(true);
            }
        });
        JMenuItem deleteFile = new JMenuItem("Borrar archivo");
        jPopupMenu2.add(deleteFile);
        deleteFile.addActionListener(
                new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent Event) {
                TreePath tp = fileTree.getSelectionPath();
                DefaultMutableTreeNode parent = (DefaultMutableTreeNode) tp.getLastPathComponent();
                try {
                    Server.deleteFile(parent);
                    fileTree.setModel(Server.getTreeModel());
                    ((DefaultTreeModel) fileTree.getModel()).reload();
                } catch (RemoteException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        );

        FileListCellRenderer rendererCell = new FileListCellRenderer();
        jList1.setModel(new DefaultListModel());
        jList1.setCellRenderer(rendererCell);
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
        jPopupMenu2 = new javax.swing.JPopupMenu();
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
                    DefaultListModel tmp = new DefaultListModel();
                    DefaultMutableTreeNode tmpNode = new DefaultMutableTreeNode(Client.Server.streamFromServer(nodo));
                    tmp.add(0, tmpNode);
                    jList1.setModel(tmp);
                } catch (Exception ex) {

                }

            } else {
                DefaultListModel tmp = new DefaultListModel();
                for (int i = 0; i < fileTree.getModel().getChildCount(parent); i++) {
                    tmp.add(i, fileTree.getModel().getChild(parent, i));
                }
                jList1.setModel(tmp);
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
                } else {
                    jPopupMenu2.show(fileTree, pathBounds.x, pathBounds.y + pathBounds.height);
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
                if ("Windows".equals(info.getName())) {
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
            @Override
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
    private javax.swing.JPopupMenu jPopupMenu2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables
}

class FileListCellRenderer extends DefaultListCellRenderer {

    private static final long serialVersionUID = -7799441088157759804L;
    private FileSystemView fileSystemView;
    private JLabel label;
    private Color textSelectionColor = Color.BLACK;
    private Color backgroundSelectionColor = Color.CYAN;
    private Color textNonSelectionColor = Color.BLACK;
    private Color backgroundNonSelectionColor = Color.WHITE;

    FileListCellRenderer() {
        label = new JLabel();
        label.setOpaque(true);
        fileSystemView = FileSystemView.getFileSystemView();
    }

    @Override
    public Component getListCellRendererComponent(
            JList list,
            Object value,
            int index,
            boolean selected,
            boolean expanded) {

        DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
        UIDefaults defaults = UIManager.getDefaults();
        Icon fileIcon = defaults.getIcon("FileView.fileIcon");
        Icon folderIcon = defaults.getIcon("FileView.directoryIcon");
        Icon computerIcon = defaults.getIcon("FileView.computerIcon");
        if (node.getUserObject() instanceof String) {
            label.setIcon(computerIcon);
            label.setText((String) node.getUserObject());
            return label;
        }
        entryNode file = (entryNode) node.getUserObject();

        if (file.isDir()) {
            label.setIcon(folderIcon);
            File tmp = new File("./src");
            //label.setIcon(fileSystemView.getSystemIcon( ));
        } else {
            label.setIcon(fileIcon);
        };
        label.setText(file.getName());
        //label.setToolTipText(file.getPath());

        if (selected) {
            label.setBackground(backgroundSelectionColor);
            label.setForeground(textSelectionColor);
        } else {
            label.setBackground(backgroundNonSelectionColor);
            label.setForeground(textNonSelectionColor);
        }

        return label;
    }
}
