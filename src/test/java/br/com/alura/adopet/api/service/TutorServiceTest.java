package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dto.AtualizacaoTutorDto;
import br.com.alura.adopet.api.dto.CadastroTutorDto;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.model.Tutor;
import br.com.alura.adopet.api.repository.TutorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class TutorServiceTest {

    @InjectMocks
    private TutorService tutorService;

    @Mock
    private TutorRepository repository;

    private CadastroTutorDto dtoCadastro;

    private AtualizacaoTutorDto dtoAtualizacao;

    @Mock
    private Tutor tutor;

    @Captor
    private ArgumentCaptor<Tutor> tutorCaptor;

    @Test
    void deveriaSalvarTutorAoCadastrar() {
        //ARRANGE
        dtoCadastro = new CadastroTutorDto("Teste", "(00) 0000-0000", "teste@gmail.com");
        given(repository.existsByTelefoneOrEmail(dtoCadastro.telefone(), dtoCadastro.email())).willReturn(false);

        //ACT + ASSERT
        assertDoesNotThrow(() -> tutorService.cadastrar(dtoCadastro));
        then(repository).should().save(tutorCaptor.capture());
        Tutor tutorSalvo = tutorCaptor.getValue();
        assertEquals(dtoCadastro.nome(), tutorSalvo.getNome());
        assertEquals(dtoCadastro.telefone(), tutorSalvo.getTelefone());
        assertEquals(dtoCadastro.email(), tutorSalvo.getEmail());
    }

    @Test
    void naoDeveriaSalvarTutorAoCadastrarRepetido() {
        //ARRANGE
        dtoCadastro = new CadastroTutorDto("Teste", "(00) 0000-0000", "teste@gmail.com");
        given(repository.existsByTelefoneOrEmail(dtoCadastro.telefone(), dtoCadastro.email())).willReturn(true);

        //ACT + ASSERT
        assertThrows(ValidacaoException.class, () -> tutorService.cadastrar(dtoCadastro));
    }

    @Test
    void deveriaAtualizarInformacoesDoTutor() {
        //ARRANGE
        dtoAtualizacao = new AtualizacaoTutorDto(null, "Teste", "() 0000-0000", "teste@gmail.com");
        given(repository.getReferenceById(dtoAtualizacao.id())).willReturn(tutor);

        //ACT
        tutorService.atualizar(dtoAtualizacao);

        //ASSERT
        then(tutor).should().atualizarDados(dtoAtualizacao);
    }

}