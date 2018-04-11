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
}
