package drinkshop.domain.bbt;

import drinkshop.domain.CategorieBautura;
import drinkshop.domain.Product;
import drinkshop.domain.TipBautura;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProductBBTest {

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
    void getId_Valid() {
        assert 100 == product.getId();
    }

    @Test
    void getId_NonValid() {
        assert 10 != product.getId();
    }


}