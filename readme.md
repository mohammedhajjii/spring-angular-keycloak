

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











