package drinkshop.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ProductTest {

    Product product;

    @BeforeEach
    public void setUp() {
        product =new Product(100, "Limonada", 10.0, CategorieBautura.JUICE, TipBautura.WATER_BASED);
    }

    @AfterEach
    public void tearDown() {
        product = null;
    }

    @Test
    public void testGetId() {
        assert 100 == product.getId();
    }


}