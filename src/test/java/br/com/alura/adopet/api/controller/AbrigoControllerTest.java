package br.com.alura.adopet.api.controller;

import br.com.alura.adopet.api.model.Abrigo;
import br.com.alura.adopet.api.service.AbrigoService;
import br.com.alura.adopet.api.service.PetService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
class AbrigoControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AbrigoService abrigoService;

    @MockBean
    private PetService petService;

    @Mock
    private Abrigo abrigo;

    @Test
    void deveriaDevolverCodigo200PAoListarAbrigos() throws Exception {
        //ACT
        var response = mvc.perform(MockMvcRequestBuilders.get("/abrigos")
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        assertEquals(200, response.getStatus());
    }

    @Test
    void deveriaDevolverCodigo400ParaCadastroDeAbrigoComErros() throws Exception {
        //ARRANGE
        String json = "{}";

        //ACT
        var response = mvc.perform(post("/abrigos")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        //ASSERT
        assertEquals(400, response.getStatus());
    }

    @Test
    void deveriaDevolverCodigo200ParaCadastroDeAbrigoSemErros() throws Exception {
        //ARRANGE
        String json = """
                {
                    "nome": "Teste",
                    "telefone": "(00)00000-0000",
                    "email": "teste@gmail.com"
                }
                """;

        //ACT
        var response = mvc.perform(post("/abrigos")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        //ASSERT
        assertEquals(200, response.getStatus());
    }

    @Test
    void deveriaDevolverCodigo200AoListarPetsDeUmAbrigoPorNome() throws Exception {
        //ARRANGE
        String nome = "0";

        //ACT
        var response = mvc.perform(MockMvcRequestBuilders.get("/abrigos/" + nome + "/pets")
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        assertEquals(200, response.getStatus());
    }

    @Test
    void deveriaDevolverCodigo200AoListarPetsDeUmAbrigoPorId() throws Exception {
        //ARRANGE
        String id = "0";

        //ACT
        var response = mvc.perform(MockMvcRequestBuilders.get("/abrigos/" + id + "/pets")
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        assertEquals(200, response.getStatus());
    }

    @Test
    void deveriaDevolverCodigo400ParaCadastroDePetComErros() throws Exception {
        //ARRANGE
        String idOuNome = "0";
        String json = "{}";
        given(abrigoService.carregarAbrigo(idOuNome)).willReturn(abrigo);

        //ACT
        var response = mvc.perform(post("/abrigos/" + idOuNome + "/pets")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        //ASSERT
        assertEquals(400, response.getStatus());
    }

    @Test
    void deveriaDevolverCodigo200ParaCadastroDePetSemErros() throws Exception {
        //ARRANGE
        String idOuNome = "0";
        String json = """
                {
                    "tipo": 1,
                    "nome": "Teste",
                    "raca": "Teste",
                    "idade": 1,
                    "cor": "Teste",
                    "peso": 1.0
                }
                """;
        given(abrigoService.carregarAbrigo(idOuNome)).willReturn(abrigo);

        //ACT
        var response = mvc.perform(post("/abrigos/" + idOuNome + "/pets")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        //ASSERT
        assertEquals(200, response.getStatus());
    }

}