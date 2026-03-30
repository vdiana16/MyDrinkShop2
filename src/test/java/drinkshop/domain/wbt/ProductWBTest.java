package drinkshop.domain.wbt;

import drinkshop.domain.CategorieBautura;
import drinkshop.domain.Product;
import drinkshop.domain.TipBautura;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class ProductWBTest {

    Product product;

    @BeforeEach
    void setUp() {
        product =new Product(100, "Limonada", 10.0, CategorieBautura.JUICE, TipBautura.WATER_BASED);
    }

    @AfterEach
    void tearDown() {
        product = null;
    }

    @Test
    @Tag("valid")
    void getId_P1() {
        assert 100 == product.getId();
    }

    @Test
    @Tag("non-valid")
    void getId_P2() {
        assert 10 != product.getId();
    }
}