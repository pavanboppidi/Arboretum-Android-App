package nwmissouri.edu.missouriarboretum;

import java.io.Serializable;

/**
 * Created by S518620 on 12/1/2014.
 */
public class TreeData implements Serializable {

    private String imageUrl;
    private String name;
    private String treeId;
    private String markerImageUrl;

    public String getMarkerImageUrl() {
        return markerImageUrl;
    }

    public void setMarkerImageUrl(String markerImageUrl) {
        this.markerImageUrl = markerImageUrl;
    }

    public TreeData(String imageUrl, String name,String markerImageUrl) {
        super();
        this.imageUrl = imageUrl;
        this.name = name;
        this.markerImageUrl=markerImageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getTreeId() {
        return treeId;
    }
    public void setTreeId(String treeId) {
        this.treeId = treeId;
    }
}
