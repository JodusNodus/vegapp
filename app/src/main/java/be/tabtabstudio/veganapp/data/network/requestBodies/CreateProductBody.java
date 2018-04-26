package be.tabtabstudio.veganapp.data.network.requestBodies;

import java.util.List;

import be.tabtabstudio.veganapp.data.entities.Supermarket;

public class CreateProductBody {
    public final String name;
    public final long ean;
    public final String brandname;
    public final List<String> labels;
    public final String placeid;

    public CreateProductBody(long ean, String name, String brandname, List<String> labels, Supermarket supermarket) {
        this.name = name;
        this.ean = ean;
        this.brandname = brandname;
        this.labels = labels;
        this.placeid = supermarket.placeid;
    }
}
