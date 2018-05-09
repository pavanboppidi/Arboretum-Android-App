package nwmissouri.edu.missouriarboretum;

import java.io.Serializable;

public class TreeImageBean implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -6718887585969182648L;
    private String treeId;
    private String imageUrl;
    String description;
    String cName;
    String sName;

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTreeId() {
        return treeId;
    }

    public void setTreeId(String treeId) {
        this.treeId = treeId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}
