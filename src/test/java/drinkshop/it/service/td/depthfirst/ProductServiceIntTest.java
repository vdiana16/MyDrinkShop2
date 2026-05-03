package drinkshop.it.service.td.depthfirst;

import drinkshop.domain.CategorieBautura;
import drinkshop.domain.Product;
import drinkshop.domain.TipBautura;
import drinkshop.repository.file.FileProductRepository;
import drinkshop.service.ProductService;
import drinkshop.service.validator.ProductValidator;
import drinkshop.service.validator.ValidationException;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.fail;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductServiceIntTest {
    private Product product;
    private ProductValidator productValidator; // REAL
    private FileProductRepository productRepo; // REAL

    private ProductService productService;

    @BeforeEach
    void setUp() {
        productValidator = new ProductValidator();
        productRepo = new FileProductRepository("data/test_produse.txt");
        product = null;
        productService = new ProductService(productRepo, productValidator);
    }

    @Test
    @Order(1)
    void testAddValid_withRealRepo() {

        Product product = new Product(999, "Ceai", 10.0, CategorieBautura.JUICE, TipBautura.WATER_BASED);
        int initialSize = productService.getAllProducts().size();


        try {
            productService.addProduct(product);
        } catch (Exception e) {
            fail("Invalid add operation " + e);
        }

        assert (initialSize + 1) == productRepo.findAll().size();
        assert (initialSize + 1) == productService.getAllProducts().size();

        productService.deleteProduct(999);
    }

    @Test
    @Order(2)
    void testAddInvalid_withRealRepo() {
        Product product = new Product(-1, "", -5.0, CategorieBautura.JUICE, TipBautura.WATER_BASED);
        int initialSize = productService.getAllProducts().size();


        Assertions.assertThrows(ValidationException.class, () -> {
            productService.addProduct(product);
        });

        assert initialSize == productRepo.findAll().size();
        assert initialSize == productService.getAllProducts().size();
    }
}