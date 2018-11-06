package entity;

public class Material {
    private int id;
    private String name;
    private String img;
    public Material(){

    }
    public Material(int id, String name,String img) {
        this.id = id;
        this.name = name;
        this.img = img;
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

    public String getImg() {
        return "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQHTvUjmTSh9x7NdDA7j_aT7RchWcyO8e3JY5alrJv-GvvXeuo7Zw";
    }

    public void setImg(String img) {
        this.img = img;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
