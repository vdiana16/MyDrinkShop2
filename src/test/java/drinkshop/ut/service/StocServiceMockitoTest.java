package drinkshop.ut.service;

import drinkshop.domain.Stoc;
import drinkshop.repository.Repository;
import drinkshop.service.StocService;
import drinkshop.service.validator.ValidationException;
import drinkshop.service.validator.Validator;
import org.junit.jupiter.api.*;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class StocServiceMockitoTest {

    private Stoc stoc;
    private Validator<Stoc> stocValidator;
    private Repository<Integer, Stoc> stocRepo;

    private StocService stocService;

    @BeforeEach
    public void setUp() {
        // Creăm obiecte mock
        stoc = mock(Stoc.class); // În mod intenționat folosim obiecte mock pentru entități (cerință specială Lab04)
        stocValidator = mock(Validator.class);
        stocRepo = mock(Repository.class);

        // Creăm obiectul testat
        stocService = new StocService(stocRepo, stocValidator);
    }

    @AfterEach // CORECTAT: Era @BeforeEach, acum este @AfterEach pentru a rula la finalul fiecărui test
    public void tearDown() {
        stocService = null;
        stocRepo = null;
        stocValidator = null;
        stoc = null;
    }

    @Test
    @Order(1)
    public void testGetAllValid() {
        // Creăm obiecte mock suplimentare
        Stoc stoc1 = mock(Stoc.class);
        Stoc stoc2 = mock(Stoc.class);

        // Asociem comportamente obiectelor mock
        when(stocRepo.findAll()).thenReturn(Arrays.asList(stoc1, stoc2));

        // CORECTAT: Testăm metoda getAll din StocService folosind assertEquals din JUnit 5
        assertEquals(2, stocService.getAll().size(), "Lista returnată ar trebui să aibă dimensiunea 2");

        // Verificăm interacțiunile obiectului testat cu obiectele mock
        verify(stocValidator, never()).validate(stoc1);
        verify(stocRepo, times(1)).findAll();
    }

    @Test
    @Order(2)
    void testAddInvalid() {
        // Simulăm adăugarea unui stoc invalid
        // Stabilim comportamente pentru obiectele mock

        // Folosim lenient() pentru stocRepo.save ca să nu primim eroare de tip UnnecessaryStubbingException,
        // deoarece ne așteptăm ca execuția să se oprească la validator și save să nu fie apelat.
        lenient().when(stocRepo.save(stoc)).thenReturn(stoc);

        doThrow(new ValidationException("ID invalid!\n")).when(stocValidator).validate(stoc);

        // CORECTAT: Folosim assertThrows în loc de blocul try-catch
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            stocService.add(stoc);
        }, "Metoda ar trebui să arunce ValidationException pentru un stoc invalid");

        assertEquals("ID invalid!\n", exception.getMessage());

        // Verificăm interacțiunile obiectului testat cu obiectele mock
        verify(stocValidator, times(1)).validate(stoc);
        verify(stocRepo, never()).save(any()); // Ne asigurăm că nu se face nicio salvare
    }

    @Test
    @Order(3)
    void testAddValid() {
        // Creăm obiecte mock suplimentare nefolosite
        Stoc stoc1 = mock(Stoc.class);

        // Asociem comportament obiectului mock (cazul în care validarea trece cu succes)
        doNothing().when(stocValidator).validate(stoc);
        when(stocRepo.save(stoc)).thenReturn(stoc);

        // CORECTAT: Folosim assertDoesNotThrow în loc de blocul try-catch
        assertDoesNotThrow(() -> {
            stocService.add(stoc);
        }, "Metoda nu ar trebui să arunce nicio excepție la adăugarea unui stoc valid");

        // Dimensiunea este 0 deoarece stocRepo.findAll() întoarce o listă goală (nu a fost mock-uit în acest test)
        assertEquals(0, stocService.getAll().size());

        // Verificări ale interacțiunii
        verify(stocValidator, times(1)).validate(stoc);
        verify(stocRepo, times(1)).save(stoc);

        // Verificări ale interacțiunii cu mock-uri nefolosite (stoc1)
        verify(stocValidator, never()).validate(stoc1);
        verify(stocRepo, never()).save(stoc1);
    }
}