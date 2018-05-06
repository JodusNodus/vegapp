package be.tabtabstudio.veganapp.data.network.results;

import java.util.List;

import be.tabtabstudio.veganapp.data.entities.Product;

public class GetProductsResult implements ApiResult {
    public List<Product> products;
    public long total;
    public int page;
    public int size;
}
