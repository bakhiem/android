package entity;

public class Material {
    private int id;
    private String name;
    private String image_link;
    public Material(){

    }
    public Material(int id, String name,String img) {
        this.id = id;
        this.name = name;
        this.image_link = img;
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

    public String getImage_link() {
        return image_link;
    }

    public void setImage_link(String image_link) {
        this.image_link = image_link;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
