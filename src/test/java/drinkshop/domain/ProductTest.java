package drinkshop.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ProductTest {

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
    public void getId() {
        assert 100 == product.getId();
    }

    @Test
    public void getNume() {
        assert "Limonada".equals(product.getNume());
    }

    @Test
    public void getPret() {
        assert 10.0 == product.getPret();
    }

    @Test
    public void getCategorie() {
        assert CategorieBautura.JUICE.equals(product.getCategorie());
    }

    @Test
    public void testSetCategorie() {
        product.setCategorie(CategorieBautura.SMOOTHIE);
        assert CategorieBautura.SMOOTHIE.equals(product.getCategorie());
    }

    @Test
    public void getTip() {
        assert TipBautura.WATER_BASED.equals(product.getTip());
    }

    @Test
    public void setTip() {
        product.setTip(TipBautura.BASIC);
        assert TipBautura.BASIC.equals(product.getTip());
    }

    @Test
    public void setNume() {
        product.setNume("newLimonada");
        assert "newLimonada".equals(product.getNume());
    }

    @Test
    public void setPret() {
        product.setPret(10.05);
        assert 10.05 == product.getPret();
    }

    @Test
    public void testToString() {
        System.out.println(product.toString());
        assert "Limonada (JUICE, WATER_BASED) - 10.0 lei".equals(product.toString());
    }
}