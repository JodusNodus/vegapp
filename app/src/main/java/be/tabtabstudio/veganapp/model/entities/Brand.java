package be.tabtabstudio.veganapp.model.entities;

public class Brand {
    public static Brand getMock() {
        Brand b = new Brand();
        b.brandid = 379;
        b.name = "Lotus";
        return b;
    }

    public int brandid;
    public String name;
}
