package drinkshop.it.service.td.depthfirst;

import drinkshop.domain.CategorieBautura;
import drinkshop.domain.Product;
import drinkshop.domain.TipBautura;
import drinkshop.repository.Repository;
import drinkshop.service.ProductService;
import drinkshop.service.validator.ProductValidator;
import drinkshop.service.validator.ValidationException;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductServiceLevel1ValidatorIntTest {
    private Product product;
    private ProductValidator productValidator; // REAL
    private Repository<Integer, Product> productRepo;

    private ProductService productService;

    @BeforeEach
    void setUp() {
        product = mock(Product.class);
        productValidator = new ProductValidator();
        productRepo = mock(Repository.class);

        productService = new ProductService(productRepo, productValidator);
    }

    @Test
    @Order(1)
    void testAddValid_withRealValidator() {
        // asociem comportamentul pentru obiectele mock
        when(product.getId()).thenReturn(1);
        when(product.getNume()).thenReturn("Limonada");
        when(product.getPret()).thenReturn(15.0);

        try {
            productService.addProduct(product);
        } catch (Exception e) {
            fail("Invalid add operation");
        }


        verify(productRepo, times(1)).save(product);
        verify(product, atLeastOnce()).getPret(); // se apeleaza in validator
    }

    @Test
    @Order(2)
    void testAddInvalid_withRealValidator() {
        // asociem comportamente obiectelor mock
        when(product.getId()).thenReturn(-1);

        // apelam metoda si evaluam invalidarea obiectului
        Assertions.assertThrows(ValidationException.class, () -> {
            productService.addProduct(product);
        });

        // verificam interactiunea obiectului testat cu obiectele mock ramase
        verify(productRepo, never()).save(any());
        verify(product, atLeastOnce()).getId();
    }
}
