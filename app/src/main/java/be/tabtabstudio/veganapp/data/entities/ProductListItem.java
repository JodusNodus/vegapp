package be.tabtabstudio.veganapp.data.entities;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.fasterxml.jackson.annotation.JsonIgnore;

import be.tabtabstudio.veganapp.utilities.StringUtils;

public class ProductListItem {
    public static Product getMock() {
        Product p = new Product();
        p.ean = 555555555555L;
        p.name = "Speculoos";
        p.brand = Brand.getMock();
        p.thumbPicture = "https://storage.googleapis.com/vegstorage/thumb-555555555555";
        p.rating = 3;
        p.hits = 452;
        return p;
    }

    @PrimaryKey
    public long ean;
    public String name;
    public String thumbPicture;
    public int rating;
    public long hits;

    @Embedded
    public Brand brand;

    @Ignore
    @JsonIgnore
    public String getProductName() {
        return StringUtils.capitize(brand.name) + " " + StringUtils.capitize(name);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ProductListItem) {
            return ((ProductListItem) obj).ean == ean;
        } else {
            return false;
        }
    }
}
