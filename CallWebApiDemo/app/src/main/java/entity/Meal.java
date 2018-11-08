package entity;

import java.io.Serializable;

public class Meal implements Serializable{
    private int id;
    private String name;
    private String description;
    private String imgLink;

    public Meal() {
    }

    public Meal(int id, String name, String description, String imgLink) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imgLink = imgLink;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgLink() {
        return "https://image2.tienphong.vn/w665/Uploaded/2018/bzivobxw/2014_03_20/ngoctring-croptop%20(12)_tdij.jpg";
    }

    public void setImgLink(String imgLink) {
        this.imgLink = imgLink;
    }

    @Override
    public String toString() {
        return this.name + this.description;
    }
}
