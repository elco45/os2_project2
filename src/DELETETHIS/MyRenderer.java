/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DELETETHIS;

import Client.entryNode;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.IOException;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeSelectionModel;

/**
 *
 * @author C5220696
 */
public class MyRenderer extends DefaultTreeCellRenderer{
    Icon Folder,File;
    

    public MyRenderer(Icon Folder, Icon File) {
      this.Folder = Folder;
      this.File = File;
    }

    public Component getTreeCellRendererComponent(JTree tree, Object value,
        boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {

      super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row,
          hasFocus);
      DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
      entryNode nodeInfo = (entryNode) (node.getUserObject());
      
      if (nodeInfo.isDir()) {
        setIcon(Folder);
        
      } else {
        setIcon(File);
        
      }

      return this;
    }

   
  }

