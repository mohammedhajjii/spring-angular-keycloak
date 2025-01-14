package md.hajji.inventoryservice.utils;

import md.hajji.inventoryservice.entities.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ProductFactory {

    static final List<String> NAMES = List.of("Pixel 6a", "Pixel 8", "Samsung S25", "Iphone 16 pro");
    static final List<Double> PRICE_SEEDS = List.of(3500., 4700., 4500., 8000.);
    static final Random RANDOM = new Random();


    public static Product randomProduct() {
        var index = RANDOM.nextInt(PRICE_SEEDS.size());
        return Product.builder()
                .name(NAMES.get(index))
                .price(RANDOM.nextDouble(PRICE_SEEDS.get(index)))
                .quantity(RANDOM.nextInt(20))
                .build();
    }

}
