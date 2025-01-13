

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


#### Create realm

![create-realm](./images/create-realm.png)

