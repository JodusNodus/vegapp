package be.tabtabstudio.veganapp.data.network.results;

import java.util.List;
import be.tabtabstudio.veganapp.data.entities.Brand;

public class UploadProductImageResult implements ApiResult {

    public List<String> labelSuggestions;
    public List<Brand> brandSuggestions;

}
