package be.tabtabstudio.veganapp.api.results;

import java.util.List;

import be.tabtabstudio.veganapp.model.entities.Brand;

public class GetBrandsResult implements ApiResult {
    public List<Brand> brands;
}
