

# Spring-Angular-keycloak

## Keycloak setup

### docker-compose file

```yaml

services:
  postgres:
    image: postgres:16.2
    container_name: postgres
    hostname: postgres
    volumes:
      - postgres_data:/var/lib/postgresql/data
    env_file:
      - ./init.Ini
    networks:
      - keycloak_network

  keycloak:
    image: quay.io/keycloak/keycloak:26.0.7
    container_name: keycloak
    hostname: keycloak
    command: start-dev
    env_file:
      - ./init.Ini
    ports:
      - 8080:8080
    restart: always
    depends_on:
      - postgres
    networks:
      - keycloak_network

volumes:
  postgres_data:
    driver: local

networks:
  keycloak_network:
    driver: bridge
```

### Start keycloak

![start-keycloak](./images/start-keycloak-docker.png)

![verify-keycloak](./images/verify-keycloak-started.png)

### keycloak interface

#### Admin Login page

![admin-login-page](./images/keycloak-admin-login.png)


#### Create realm (my-realm)

Create realm called: `my-realm`:

![create-realm](./images/create-realm.png)

#### Create client to secure (my-client)

Create client called: `my-client`:

![create-client-1](./images/create-client-1.png)

configure `authentication methods`:

![create-client-2](./images/create-client-2.png)

Configure `redirect` urls and `cors`:

![create-client-3](./images/create-client-3.png)


#### Create roles (USER and ADMIN roles)


##### create USER role

![create-user-role](./images/create-user-role.png)

##### create ADMIN role

![create-admin-role](./images/create-admin-role.png)


##### Realm role list

![realm-role-list](./images/realm-roles.png)

#### Create users

##### Create first user

![create-user-1](./images/create-user1.png)

![add-password](./images/user1-password.png)


##### Create second user

![create-user-2](./images/create-user2.png)

![add-password-2](./images/user2-password.png)


#### User list

![user-list](./images/user-list.png)

#### roles assignment

Assign role `USER` to `hajjimohammed` user:

![assign-role-1](./images/add-user-role-to-user1.png)

Assign role `ADMIN` to `fadssiamine` user:

![assign-role-2](./images/add-admin-role-to-user2.png)


### Test with Postman


#### Setting content-type header

![content-type-header](./images/auth-content-type.png)

#### Password authentication

For user: `hajjimohammed`:

![password-auth-user1](./images/password-auth-user1.png)

For user: `fadssiamine`:

![password-auth-user2](./images/password-auth-user2.png)


#### Access token and refresh token content

##### Access token

![access-token-header](./images/header-access-token-user2.png)

![access-token-payload](./images/payload-access-token-user2.png)

![access-token-signature](./images/signature-access-token-user2.png)


##### Refresh token

![refresh-token-content](./images/refresh-token-user2.png)


#### Refresh token authentication

![refresh-token-auth](./images/refresh-token-auth-user2.png)

#### Client secret authentication

##### Enable client authentication

![enable-client-auth](./images/activate-client-auth.png)

##### Get client secret appropriate to my-client

![client-secret](./images/client-secret.png)

##### Client secret auth

![client-secret-auth](./images/client-secret-auth.png)



### modify tokens properties

modify signature algorithm from `RSA` to `EDSA`:

![edsa](./images/ecdsa-sig.png)

modify lifespan of access token:

![access-token-lifespan](./images/access-token-lifespan.png)

After modification, we got:

![access-token-after-mod](./images/access-token-after-update.png)


## Backend: Inventory-service


### Dependencies

Dependencies used for this microservice are:

- `Spring data JPa`
- `Spring web`
- `H2 database`
- `oauth2 resource server`
- `Eureka discorvery client`
- `Config client`
- `Lombok`


### application.yml file

```yaml
spring:
  application:
    name: inventory-service

  datasource:
    url: jdbc:h2:mem:inventory-db


  h2:
    console:
      enabled: true

  cloud:
    config:
      enabled: false

    discovery:
      enabled: false



  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: http://localhost:8080/realms/my-realm
          jwk-set-uri: http://localhost:8080/realms/my-realm/protocol/openid-connect/certs
server:
  port: 9090




```


### Entities

#### Product entity

```java
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

```
### Repositories

#### ProductRepository

```java
package md.hajji.inventoryservice.repositories;

import md.hajji.inventoryservice.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, String> {
}

```

### Web

#### ProductRestController

```java
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

```


### Exceptions

#### ProductNotFoundException

```java
package md.hajji.inventoryservice.exceptions;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String id) {
        super("Product with id " + id + " not found");
    }
}

```

#### ExceptionsHandler

```java
package md.hajji.inventoryservice.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionsHandler {



    @ExceptionHandler({ProductNotFoundException.class})
    public ResponseEntity<String> handleProductNotFoundException(ProductNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(exception.getMessage());
    }
}

```

### Security

#### SecurityConfiguration



```java
package md.hajji.inventoryservice.security;


import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    @SneakyThrows({Exception.class})
    public SecurityFilterChain securityFilterChain(HttpSecurity http){

        return http
                .authorizeHttpRequests(auth -> auth.requestMatchers("/products/**").permitAll())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                .build();

    }


}

```

At this point, we don't have any kind of security because we `permit` all request to `/products` endpoint.

Later we will update this class to ensure security using `keycloak`.

### Utils

#### ProductFactory

```java
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
```

### First Test

```java
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

```

And we got the following results:

![all-products-no-sec](./images/products-no-security.png)


![product-no-sec](./images/product-1-no-security.png)





