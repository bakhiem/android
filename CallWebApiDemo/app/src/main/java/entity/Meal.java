package entity;

import java.io.Serializable;

public class Meal implements Serializable{
    private int id;
    private String name;
    private String description;
    private String image_link;

    public Meal() {
    }

    public Meal(int id, String name, String description, String imgLink) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image_link = imgLink;
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

    public String getImage_link() {
        return image_link;
    }

    public void setImage_link(String image_link) {
        this.image_link = image_link;
    }

    @Override
    public String toString() {
        return this.name + this.description;
    }
}
