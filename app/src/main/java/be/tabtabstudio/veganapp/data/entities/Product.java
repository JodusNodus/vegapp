package be.tabtabstudio.veganapp.data.entities;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import be.tabtabstudio.veganapp.data.local.DateConverters;
import be.tabtabstudio.veganapp.data.local.LabelListConverters;
import be.tabtabstudio.veganapp.utilities.StringUtils;

@Entity(tableName = "favorites")
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
        p.userHasCorrected = true;
        p.labels = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            p.labels.add(Label.getMock());
        }
        return p;
    }

    @PrimaryKey
    public long ean;
    public String name;

    @Embedded
    public Brand brand;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @TypeConverters(DateConverters.class)
    public Date creationdate;

    @Embedded
    public User user;

    public int rating;
    public boolean userHasCorrected;
    public String coverPicture;
    public String thumbPicture;

    @TypeConverters(LabelListConverters.class)
    public List<Label> labels;

    @Ignore
    @JsonIgnore
    public String getProductName() {
        return StringUtils.capitize(brand.brandname) + " " + StringUtils.capitize(name);
    }

    @Override
    public boolean equals(Object obj) {
        try {
            return ((Product) obj).ean == ean;
        } catch(Exception e) {
            return false;
        }
    }
}
