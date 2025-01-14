package md.hajji.inventoryservice;

import md.hajji.inventoryservice.repositories.ProductRepository;
import md.hajji.inventoryservice.utils.ProductFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.stream.Stream;

@SpringBootApplication
public class InventoryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventoryServiceApplication.class, args);
    }


    @Bean
    CommandLineRunner start(ProductRepository productRepository) {
        return args -> {
            Stream.generate(ProductFactory::randomProduct)
                    .limit(10)
                    .forEach(productRepository::save);
        };
    }

}
