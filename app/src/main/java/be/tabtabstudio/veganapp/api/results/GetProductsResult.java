package be.tabtabstudio.veganapp.api.results;

import java.util.List;

import be.tabtabstudio.veganapp.model.entities.Product;

public class GetProductsResult implements ApiResult {
    public List<Product> products;
    public GetProductsResultParams params;
}
