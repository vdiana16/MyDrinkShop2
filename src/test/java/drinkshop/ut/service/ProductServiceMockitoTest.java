package drinkshop.ut.service;

import drinkshop.domain.Product;
import drinkshop.repository.Repository;
import drinkshop.service.ProductService;
import drinkshop.service.validator.ProductValidator;
import drinkshop.service.validator.ValidationException;
import drinkshop.service.validator.Validator;
import org.junit.jupiter.api.*;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductServiceMockitoTest {

    private Product product;
    private Validator<Product> productValidator;
    private Repository<Integer, Product> productRepo;

    private ProductService productService;

    @BeforeEach
    public void setUp() {

        product = mock(Product.class);
        productValidator = mock(Validator.class);
        productRepo = mock(Repository.class);


        productService = new ProductService(productRepo, productValidator);
    }

    @AfterEach
    public void tearDown() {
        productService = null;
        productRepo = null;
        productValidator = null;
        product = null;
    }

    @Test
    @Order(1)
    public void testGetAllValid() {

        Product product1 = mock(Product.class);
        Product product2 = mock(Product.class);


        when(productRepo.findAll()).thenReturn(Arrays.asList(product1, product2));

       assert 2 == productService.getAllProducts().size();


        verify(productValidator, never()).validate(product1);
        verify(productRepo).findAll();
        verify(productRepo, times(1)).findAll();
    }

    @Test
    @Order(2)
    void testAddInvalid() {
        when(product.getId()).thenReturn(-1);
        doThrow(new ValidationException("ID invalid!\n")).when(productValidator).validate(product);

        try {
            productService.addProduct(product);
        } catch (Exception e) {
            assert e.getClass().equals(ValidationException.class);
        }

        verify(productValidator, times(1)).validate(product);
        verify(productRepo, never()).save(any());
    }

    @Test
    @Order(3)
    void testAddValid() {
        Product product1 = mock(Product.class);

        doNothing().when(productValidator).validate(product);

        try {
            productService.addProduct(product);
        } catch (Exception e) {
            fail("Invalid add operation: " + e.getMessage());
        }

        assert 0 == productService.getAllProducts().size();

        verify(productValidator, times(1)).validate(product);
        verify(productRepo, times(1)).save(product);

        verify(productValidator, times(0)).validate(product1);
        verify(productRepo, never()).save(product1);
    }
}