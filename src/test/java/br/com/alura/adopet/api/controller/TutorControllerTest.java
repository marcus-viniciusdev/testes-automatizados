package br.com.alura.adopet.api.controller;

import br.com.alura.adopet.api.dto.AtualizacaoTutorDto;
import br.com.alura.adopet.api.service.TutorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class TutorControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TutorService service;

    @Autowired
    private JacksonTester<AtualizacaoTutorDto> jsonTesterDtoAtualizacao;

    @Test
    void deveriaDevolverCodigo400ParaCadastroDeTutorComErros() throws Exception {
        //ARRANGE
        String json = "{}";

        //ACT
        var response = mvc.perform(post("/tutores")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        //ASSERT
        assertEquals(400, response.getStatus());
    }

    @Test
    void deveriaDevolverCodigo200ParaCadastroDeTutorSemErros() throws Exception {
        //ARRANGE
        String json = """
                {
                    "nome": "Teste",
                    "telefone": "(00)99999-9999",
                    "email": "teste@gmail.com"
                }
                """;

        //ACT
        var response = mvc.perform(post("/tutores")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        //ASSERT
        assertEquals(200, response.getStatus());
    }

    @Test
    void deveriaAtualizarAsInformacoesDoTutor() throws Exception {
        //ARRANGE
        AtualizacaoTutorDto dto = new AtualizacaoTutorDto(1L, "Teste", "(00)99999-9999", "teste@gmail.com");

        //ACT
        var response = mvc.perform(put("/tutores")
                .content(jsonTesterDtoAtualizacao.write(dto).getJson())
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        //ASSERT
        assertEquals(200, response.getStatus());
        then(service).should().atualizar(dto);
    }

    @Test
    void naoDeveriaAtualizarAsInformacoesDoTutor() throws Exception {
        //ARRANGE
        AtualizacaoTutorDto dto = new AtualizacaoTutorDto(1L, "Teste", "(00)9999-99999", "teste@gmail.com");

        //ACT
        var response = mvc.perform(put("/tutores")
                .content(jsonTesterDtoAtualizacao.write(dto).getJson())
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        //ASSERT
        assertEquals(400, response.getStatus());
        then(service).shouldHaveNoInteractions();
    }

}