package be.tabtabstudio.veganapp.api.requestBodies;

import java.util.List;

public class CreateProductBody {
    public final String name;
    public final long ean;
    public final String brandname;
    public final List<String> labels;

    public CreateProductBody(String name, long ean, String brandname, List<String> labels) {
        this.name = name;
        this.ean = ean;
        this.brandname = brandname;
        this.labels = labels;
    }
}
