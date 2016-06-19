package Client;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.Serializable;

public class entryNode implements Serializable {

    private String name;
    private entryNode father;
    private int DataNode;
    private boolean isDir;

    //Constructor para el root
    public entryNode() {
        this.name = "/";
        this.father = null;
        this.DataNode = -1;
        this.isDir = true;
    }

    public entryNode(String name, entryNode father, int DataNode, boolean isDir) {
        this.name = name;
        this.father = father;
        this.DataNode = DataNode;
        this.isDir = isDir;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public entryNode getFather() {
        return father;
    }

    public void setFather(entryNode father) {
        this.father = father;
    }

    public int getDataNode() {
        return DataNode;
    }

    public void setDataNode(int DataNode) {
        this.DataNode = DataNode;
    }

    public boolean isDir() {
        return isDir;
    }

    public void setDir(boolean isDir) {
        this.isDir = isDir;
    }

    @Override
    public String toString() {
        return name;
    }

}
