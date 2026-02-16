package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dto.CadastroAbrigoDto;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.model.Abrigo;
import br.com.alura.adopet.api.repository.AbrigoRepository;
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
class AbrigoServiceTest {

    @InjectMocks
    private AbrigoService abrigoService;

    @Mock
    private AbrigoRepository abrigoRepository;

    private CadastroAbrigoDto dto;

    @Captor
    private ArgumentCaptor<Abrigo> abrigoCaptor;

    @Test
    void deveriaSalvarAbrigoAoCadastrar() {
        //ARRANGE
        dto = new CadastroAbrigoDto("Teste", "(00) 0000-0000", "teste@gmail.com");
        given(abrigoRepository.existsByNomeOrTelefoneOrEmail(dto.nome(), dto.telefone(), dto.email())).willReturn(false);

        //ACT + ASSERT
        assertDoesNotThrow(() -> abrigoService.cadatrar(dto));
        then(abrigoRepository).should().save(abrigoCaptor.capture());
        Abrigo abrigoSalvo = abrigoCaptor.getValue();
        assertEquals(dto.nome(), abrigoSalvo.getNome());
        assertEquals(dto.telefone(), abrigoSalvo.getTelefone());
        assertEquals(dto.email(), abrigoSalvo.getEmail());
    }

    @Test
    void naoDeveriaSalvarAbrigoAoCadastrarRepetido() {
        //ARRANGE
        dto = new CadastroAbrigoDto("Teste", "(00) 0000-0000", "teste@gmail.com");
        given(abrigoRepository.existsByNomeOrTelefoneOrEmail(dto.nome(), dto.telefone(), dto.email())).willReturn(true);

        //ACT + ASSERT
        assertThrows(ValidacaoException.class, () -> abrigoService.cadatrar(dto));
    }

}