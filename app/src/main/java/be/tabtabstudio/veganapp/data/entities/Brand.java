package be.tabtabstudio.veganapp.data.entities;

public class Brand {
    public static Brand getMock() {
        Brand b = new Brand();
        b.brandid = 379;
        b.brandname = "Lotus";
        return b;
    }

    public int brandid;
    public String brandname;
}
