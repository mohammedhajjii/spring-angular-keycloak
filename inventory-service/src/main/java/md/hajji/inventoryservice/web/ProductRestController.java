package md.hajji.inventoryservice.web;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import md.hajji.inventoryservice.exceptions.ProductNotFoundException;
import md.hajji.inventoryservice.repositories.ProductRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/products")
@RequiredArgsConstructor
public class ProductRestController {


    private final ProductRepository productRepository;


    @GetMapping
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok(productRepository.findAll());
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<?> get(@PathVariable String id){
        return ResponseEntity.ok(
                productRepository.findById(id)
                        .orElseThrow(() ->  new ProductNotFoundException(id))
        );
    }

}
