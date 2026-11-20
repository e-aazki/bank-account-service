# bank-account-service — Micro-service de gestion de comptes bancaires

Spring Boot 3.5 · Java 21 · Spring Data JPA · H2 · Lombok · Spring Data REST · springdoc-openapi 2.8.13 · Spring GraphQL

## Lancement

```bash
mvn spring-boot:run      # démarre sur http://localhost:8081
mvn test                 # tests DAO + intégration (REST, Data REST, Swagger, GraphQL)
```

## Structure

| Étape | Élément | Fichier |
|---|---|---|
| 1 | Projet (Web, JPA, H2, Lombok, Data REST, GraphQL, springdoc) | `pom.xml` |
| 2 | Entités JPA `Compte`, `Client`, enum `TypeCompte` | `entities/`, `enums/` |
| 3 | Repositories Spring Data | `repositories/CompteRepository.java`, `ClientRepository.java` |
| 4 | Tests de la couche DAO | `src/test/.../repositories/CompteRepositoryTest.java` |
| 5 | Web service RESTful | `web/CompteRestController.java` |
| 6 | Tests avec Postman | `postman/bank-account-service.postman_collection.json` |
| 7 | Swagger / OpenAPI | dépendance `springdoc-openapi-starter-webmvc-ui:2.8.13` |
| 8 | Spring Data REST + projections | `CompteRepository` + `entities/CompteProjection.java`, `CompteDetailsProjection.java` |
| 9 | DTOs et Mapper | `dto/`, `mappers/CompteMapper.java` |
| 10 | Couche service (métier) | `service/CompteService.java`, `CompteServiceImpl.java` |
| 11 | Web service GraphQL | `resources/graphql/schema.graphqls`, `web/CompteGraphQLController.java` |

## URLs utiles

| Fonction | URL |
|---|---|
| API REST (contrôleur) | `GET/POST /api/comptes`, `GET/PUT/DELETE /api/comptes/{id}` |
| Spring Data REST | http://localhost:8081/comptes , http://localhost:8081/clients |
| Projection `p1` (id, solde, type) | http://localhost:8081/comptes?projection=p1 |
| Projection `details` (+ nom du client) | http://localhost:8081/comptes?projection=details |
| Recherche par type | http://localhost:8081/comptes/search/byType?t=COMPTE_COURANT |
| Swagger UI | http://localhost:8081/swagger-ui.html |
| OpenAPI JSON | http://localhost:8081/v3/api-docs |
| GraphiQL | http://localhost:8081/graphiql |
| Console H2 | http://localhost:8081/h2-console (JDBC URL : `jdbc:h2:mem:bank-db`, user `sa`) |

## Exemple de requêtes

```bash
curl -X POST http://localhost:8081/api/comptes -H "Content-Type: application/json" \
     -d '{"solde":7500,"devise":"EUR","type":"COMPTE_EPARGNE"}'
```

```graphql
query { clients { id nom comptes { id solde type } } }

mutation { addCompte(compte: {solde: 4200, devise: "USD", type: COMPTE_COURANT}) { id solde } }

mutation { updateCompte(id: "<id>", compte: {solde: 100}) { id solde } }

mutation { deleteCompte(id: "<id>") }
```
