package be.tabtabstudio.veganapp.model.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Product {

    public static Product getMock() {
        Product p = new Product();
        p.ean = 555555555555L;
        p.name = "Speculoos";
        p.brand = Brand.getMock();
        p.creationdate = new Date();
        p.user = User.getMock();
        p.rating = 4;
        p.thumbPicture = "https://storage.googleapis.com/vegstorage/thumb-555555555555";
        p.coverPicture = "https://storage.googleapis.com/vegstorage/cover-555555555555";
        p.labels = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            p.labels.add(Label.getMock());
        }
        return p;
    }

    public long ean;
    public String name;
    public Brand brand;
    public Date creationdate;
    public User user;
    public int rating;
    public String coverPicture;
    public String thumbPicture;
    public List<Label> labels;
}
