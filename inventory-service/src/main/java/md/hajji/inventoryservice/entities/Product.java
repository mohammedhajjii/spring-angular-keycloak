package md.hajji.inventoryservice.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Getter @Setter @AllArgsConstructor @NoArgsConstructor @Builder
@ToString
public class Product {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    private String id;
    private String name;
    private double price;
    private int quantity;
}
