package entity;

public class User {
    private int id;
    private String name;
    private String pwd;
    private String statusLogin;

    public static User instance;
    public static User getInstance()
    {
        if(instance == null)
        {
            instance = new User();
        }
        return instance;
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

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getStatusLogin() {
        return statusLogin;
    }

    public void setStatusLogin(String statusLogin) {
        this.statusLogin = statusLogin;
    }

    public User() {
    }

    public User(int id, String name, String pwd, String statusLogin) {
        this.id = id;
        this.name = name;
        this.pwd = pwd;
        this.statusLogin = statusLogin;
    }
}
