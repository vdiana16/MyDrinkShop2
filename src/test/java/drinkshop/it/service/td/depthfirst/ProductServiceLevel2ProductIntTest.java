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
public class ProductServiceLevel2ProductIntTest {
    private Product product;
    private ProductValidator productValidator; // REAL
    private Repository<Integer, Product> productRepo;

    private ProductService productService;

    @BeforeEach
    void setUp() {
        productValidator = new ProductValidator();
        productRepo = mock(Repository.class);
        product = null;
        productService = new ProductService(productRepo, productValidator);
    }

    @Test
    @Order(1)
    void testAddValid_withRealProduct() {
        Product product = new Product(1, "Limonada", 15.0, CategorieBautura.JUICE, TipBautura.WATER_BASED);


        try {
            productService.addProduct(product);
        } catch (Exception e) {
            fail("Invalid add operation " + e);
        }


        verify(productRepo, times(1)).save(product);
    }

    @Test
    @Order(2)
    void testAddInvalid_withRealProduct() {
        Product product = new Product(-1, "", -5.0, CategorieBautura.JUICE, TipBautura.WATER_BASED);


        Assertions.assertThrows(ValidationException.class, () -> {
            productService.addProduct(product);
        });

        // verificam interactiunea obiectului testat cu obiectele mock ramase
        verify(productRepo, never()).save(any());
    }
}
