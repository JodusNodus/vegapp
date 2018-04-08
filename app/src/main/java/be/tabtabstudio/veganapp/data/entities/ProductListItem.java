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
        return p;
    }

    @PrimaryKey
    public long ean;
    public String name;

    @Embedded
    public Brand brand;

    @Ignore
    @JsonIgnore
    public String getProductName() {
        return StringUtils.capitize(brand.brandname) + " " + StringUtils.capitize(name);
    }

    public String getCover() {
        return "https://storage.googleapis.com/vegstorage/cover-" + ean;
    }

    public String getThumbnail() {
        return "https://storage.googleapis.com/vegstorage/thumb-" + ean;
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
