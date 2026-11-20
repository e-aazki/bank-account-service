package ma.enset.bankaccountservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureGraphQlTester;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests d'intégration : API REST, Spring Data REST (projections), Swagger et GraphQL.
 */
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureGraphQlTester
class BankAccountServiceApplicationTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private GraphQlTester graphQlTester;

    @Test
    void restCrud() throws Exception {
        mockMvc.perform(get("/api/comptes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(20))));

        String body = mockMvc.perform(post("/api/comptes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"solde\":7500,\"devise\":\"EUR\",\"type\":\"COMPTE_EPARGNE\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.devise").value("EUR"))
                .andReturn().getResponse().getContentAsString();
        String id = body.replaceAll(".*\"id\":\"([^\"]+)\".*", "$1");

        mockMvc.perform(put("/api/comptes/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"solde\":100}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.solde").value(100.0))
                .andExpect(jsonPath("$.devise").value("EUR"));

        mockMvc.perform(delete("/api/comptes/" + id)).andExpect(status().isNoContent());
        mockMvc.perform(get("/api/comptes/" + id)).andExpect(status().isNotFound());
    }

    @Test
    void springDataRestWithProjection() throws Exception {
        mockMvc.perform(get("/comptes?projection=p1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.comptes[0].id", notNullValue()))
                .andExpect(jsonPath("$._embedded.comptes[0].solde", notNullValue()))
                .andExpect(jsonPath("$._embedded.comptes[0].devise").doesNotExist());

        mockMvc.perform(get("/comptes/search/byType?t=COMPTE_COURANT&projection=details"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.comptes[*].type", everyItem(is("COMPTE_COURANT"))))
                .andExpect(jsonPath("$._embedded.comptes[0].nomClient", notNullValue()));
    }

    @Test
    void swaggerDocs() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paths['/api/comptes']", notNullValue()));
    }

    @Test
    void graphql() {
        graphQlTester.document("{ clients { nom comptes { id solde type } } }")
                .execute()
                .path("clients").entityList(Object.class).hasSize(4)
                .path("clients[0].comptes").entityList(Object.class).hasSize(5);

        String id = graphQlTester.document("""
                        mutation { addCompte(compte: {solde: 4200, devise: "USD", type: COMPTE_COURANT}) { id devise } }
                        """)
                .execute()
                .path("addCompte.id").entity(String.class).get();

        graphQlTester.document("query($id: String) { compteById(id: $id) { solde devise } }")
                .variable("id", id)
                .execute()
                .path("compteById.devise").entity(String.class).isEqualTo("USD");

        graphQlTester.document("{ compteById(id: \"inexistant\") { id } }")
                .execute()
                .errors().expect(e -> e.getMessage().contains("introuvable"));
    }
}
