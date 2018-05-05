package be.tabtabstudio.veganapp.data.network.requestBodies;

import java.util.List;

import be.tabtabstudio.veganapp.data.entities.Supermarket;

public class CreateProductBody {
    public String name;
    public long ean;
    public String brandname;
    public List<String> labels;
    public String placeid;

    public CreateProductBody(long ean, Supermarket supermarket) {
        this.ean = ean;
        this.placeid = supermarket.placeid;
    }

    public CreateProductBody(long ean, String name, String brandname, List<String> labels, Supermarket supermarket) {
        this.name = name;
        this.ean = ean;
        this.brandname = brandname;
        this.labels = labels;
        this.placeid = supermarket.placeid;
    }
}
