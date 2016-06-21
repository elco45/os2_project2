/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

public class MyRenderer extends DefaultTreeCellRenderer {

    Icon Folder, File;

    public MyRenderer(Icon Folder, Icon File) {
        this.Folder = Folder;
        this.File = File;
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value,
            boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {

        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row,
                hasFocus);
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
        if (node.getUserObject() instanceof String) {
            setIcon(Folder);
            return this;
        }
        entryNode nodeInfo = (entryNode) (node.getUserObject());

        if (nodeInfo.isDir()) {
            setIcon(Folder);

        } else {
            setIcon(File);

        }

        return this;
    }

}
