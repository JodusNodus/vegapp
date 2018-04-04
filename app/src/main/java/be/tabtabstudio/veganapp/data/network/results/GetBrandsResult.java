package be.tabtabstudio.veganapp.data.network.results;

import java.util.List;

import be.tabtabstudio.veganapp.data.entities.Brand;

public class GetBrandsResult implements ApiResult {
    public List<Brand> brands;
}
