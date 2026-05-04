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

public class ProductUpdateTest {

    private ProductService service;
    private ProductValidator validator;

    // Creăm un mock manual de repository (ca la BBT) pentru a izola logica
    private Repository<Integer, Product> createMockRepo() {
        return new Repository<>() {
            private List<Product> list = new ArrayList<>();

            @Override
            public Product save(Product entity) {
                list.add(entity);
                return entity;
            }

            @Override
            public Product update(Product entity) {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getId() == entity.getId()) {
                        list.set(i, entity);
                        return entity;
                    }
                }
                return null; // Nu a fost găsit
            }

            @Override public Product findOne(Integer id) { return null; }
            @Override public List<Product> findAll() { return list; }
            @Override public Product delete(Integer id) { return null; }
        };
    }

    @BeforeEach
    void setUp() {
        validator = new ProductValidator();
        Repository<Integer, Product> repo = createMockRepo();

        // PRECONDIȚIE TESTLINK: "În sistem există deja un produs salvat cu un ID cunoscut, ex: id = 10"
        repo.save(new Product(10, "Produs Vechi", 10.0, CategorieBautura.JUICE, TipBautura.WATER_BASED));

        service = new ProductService(repo, validator);
    }

    @Test
    public void TestModificareProdus_Valid() {
        // Act: Modificăm cu noile valori cerute în TestLink
        assertDoesNotThrow(() -> {
            service.updateProduct(10, "Limonadă Fresh", 18.0, CategorieBautura.JUICE, TipBautura.WATER_BASED);
        }, "Modificarea cu date valide ar trebui să reușească fără excepții.");

        // Assert: Verificăm că datele s-au salvat corect
        Product updated = service.getAllProducts().get(0);
        assertEquals("Limonadă Fresh", updated.getNume(), "Numele ar trebui să fie actualizat.");
        assertEquals(18.0, updated.getPret(), "Prețul ar trebui să fie actualizat.");
    }

    @Test
    public void TestModificareProdus_Invalid() {
        // Act & Assert: Încercăm să folosim ID-ul 999 și un preț invalid (-50.0) cum cere TestLink
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            service.updateProduct(999, "Limonadă", -50.0, CategorieBautura.JUICE, TipBautura.WATER_BASED);
        });

        // Assert adițional: Ne asigurăm că excepția aruncată este corectă
        assertTrue(exception.getMessage().contains("Pret invalid") || exception.getMessage().contains("ID"),
                "Sistemul trebuie să arunce eroare de validare pentru date incorecte.");
    }
}