package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dto.CadastroPetDto;
import br.com.alura.adopet.api.model.Abrigo;
import br.com.alura.adopet.api.model.Pet;
import br.com.alura.adopet.api.model.TipoPet;
import br.com.alura.adopet.api.repository.PetRepository;
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
class PetServiceTest {

    @InjectMocks
    private PetService petService;

    @Mock
    private Abrigo abrigo;

    private CadastroPetDto dto;

    @Mock
    private PetRepository repository;

    @Captor
    private ArgumentCaptor<Pet> petCaptor;

    @Test
    void deveriaSalvarAsInformacoesDoPetAoCadastrar() {
        //ARRANGE
        dto = new CadastroPetDto(TipoPet.CACHORRO, "Teste", "Teste", 5, "Teste", 10.0f);

        //ACT + ASSERT
        assertDoesNotThrow(() -> petService.cadastrarPet(abrigo, dto));
        then(repository).should().save(petCaptor.capture());
        Pet petSalvo = petCaptor.getValue();
        assertEquals(dto.tipo(), petSalvo.getTipo());
        assertEquals(dto.nome(), petSalvo.getNome());
        assertEquals(dto.raca(), petSalvo.getRaca());
        assertEquals(dto.idade(), petSalvo.getIdade());
        assertEquals(dto.cor(), petSalvo.getCor());
        assertEquals(dto.peso(), petSalvo.getPeso());
        assertEquals(abrigo, petSalvo.getAbrigo());
    }
    
}