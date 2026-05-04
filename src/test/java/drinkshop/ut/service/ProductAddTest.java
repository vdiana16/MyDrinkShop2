package drinkshop.ut.service;

import drinkshop.domain.CategorieBautura;
import drinkshop.domain.Product;
import drinkshop.domain.TipBautura;
import drinkshop.repository.Repository;
import drinkshop.service.ProductService;
import drinkshop.service.validator.ProductValidator;
import drinkshop.service.validator.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ProductAddTest {

    private ProductService service;
    private ProductValidator validator;

    // Creăm un mock manual de repository pentru a izola testele BBT de baza de date/fișiere
    private Repository<Integer, Product> createMockRepo() {
        return new Repository<>() {
            private List<Product> list = new ArrayList<>();

            @Override
            public Product save(Product entity) {
                list.add(entity);
                return entity;
            }

            @Override public Product update(Product entity) { return null; }
            @Override public Product findOne(Integer id) { return null; }
            @Override public List<Product> findAll() { return list; }
            @Override public Product delete(Integer id) { return null; }
        };
    }

    @BeforeEach
    void setUp() {
        validator = new ProductValidator();
        Repository<Integer, Product> repo = createMockRepo();
        service = new ProductService(repo, validator);
    }

    // ==========================================
    // TESTE ECP (Equivalence Class Partitioning)
    // ==========================================

    @Test
    public void TestAdaugareProdus_ECP_Valid() {
        // Arrange: Valoare normală din interiorul clasei valide (Preț: 15.50)
        Product p = new Product(1, "Limonadă cu mentă", 15.50, CategorieBautura.NON_ALCOOLIC, TipBautura.RACORITOARE);

        // Act & Assert
        assertDoesNotThrow(() -> {
            service.addProduct(p);
        }, "Produsul cu preț valid (15.50) trebuie salvat cu succes.");

        assertEquals(1, service.getAllProducts().size(), "Produsul ar trebui să fie în listă.");
    }

    @Test
    public void TestAdaugareProdus_ECP_Invalid() {
        // Arrange: Valoare dintr-o clasă invalidă (Preț: -10.00)
        Product p = new Product(2, "Cafea Neagră", -10.00, CategorieBautura.NON_ALCOOLIC, TipBautura.CAFEA);

        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            service.addProduct(p);
        });

        // Verificăm dacă mesajul este aferent prețului
        assertTrue(exception.getMessage().toLowerCase().contains("pret"),
                "Sistemul trebuie să respingă salvarea pentru preț negativ.");
    }

    // ==========================================
    // TESTE BVA (Boundary Value Analysis)
    // ==========================================

    @Test
    public void TestAdaugareProdus_BVA_Valid() {
        // Arrange: Limita inferioară validă (Preț: 0.01)
        Product p = new Product(3, "Apă plată mică", 0.01, CategorieBautura.NON_ALCOOLIC, TipBautura.RACORITOARE);

        // Act & Assert
        assertDoesNotThrow(() -> {
            service.addProduct(p);
        }, "Produsul aflat fix la limita inferioară validă (0.01) trebuie acceptat.");

        assertEquals(1, service.getAllProducts().size(), "Produsul ar trebui să fie în listă.");
    }

    @Test
    public void TestAdaugareProdus_BVA_Invalid() {
        // Arrange: Limita exactă, nepermisă (Preț: 0.00)
        Product p = new Product(4, "Pahar cu gheață", 0.00, CategorieBautura.NON_ALCOOLIC, TipBautura.DIVERSE);

        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            service.addProduct(p);
        });

        // Verificăm dacă validarea a picat pe preț
        assertTrue(exception.getMessage().toLowerCase().contains("pret"),
                "Sistemul trebuie să oprească adăugarea pentru prețul 0.00.");
    }
}