package be.tabtabstudio.veganapp.data.entities;

import java.util.ArrayList;
import java.util.List;

public class Favorites {
    private List<Product> products;

    public Favorites(List<Product> products) {
        this.products = products;
    }

    public Favorites() {
        this.products = new ArrayList<>();
    }

    public void add(Product p) {
        products.add(p);
    }

    public void remove(Product p) {
        products.remove(p);
    }

    public boolean contains(Product p) {
        return products.contains(p);
    }

    public List<Product> getAll() {
        return products;
    }

    public Product findById(long ean) {
        for(Product p : products) {
            if (p.ean == ean) {
                return p;
            }
        }
        return null;
    }
}
