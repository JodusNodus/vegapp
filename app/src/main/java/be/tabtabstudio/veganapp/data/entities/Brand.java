package be.tabtabstudio.veganapp.data.entities;

import android.arch.persistence.room.ColumnInfo;

public class Brand {
    public static Brand getMock() {
        Brand b = new Brand();
        b.brandid = 379;
        b.name = "Lotus";
        return b;
    }

    public int brandid;

    @ColumnInfo(name = "brandname")
    public String name;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Brand) {
            return ((Brand) obj).brandid == brandid;
        } else {
            return false;
        }
    }
}
