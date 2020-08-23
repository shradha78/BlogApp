package Model;

public class Blog {
    public String title;
    public String desc;
    public String location;
    public String timestamp;
    public String image;
    public String userid;

    public Blog() {
    }

    public Blog(String title, String desc, String timestamp, String image, String userid,String location) {
        this.title = title;
        this.desc = desc;
        this.timestamp = timestamp;
        this.location = location;
        this.image = image;
        this.userid = userid;
    }

    public Blog(String title, String desc, String location) {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
