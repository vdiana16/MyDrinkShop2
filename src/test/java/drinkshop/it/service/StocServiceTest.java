package drinkshop.it.service;

import drinkshop.domain.Stoc;
import drinkshop.repository.Repository;
import drinkshop.repository.file.FileStocRepository;
import drinkshop.service.StocService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class StocServiceTest {

    StocService stocService;

    @BeforeEach
    void setUp() {
        Repository<Integer, Stoc> stocRepo= new FileStocRepository("data/stocuri.txt");
        stocService  = new StocService(stocRepo);
    }

    @AfterEach
    void tearDown() {
        stocService=null;
    }

    @Test
    void getAll() {
        assert 10== stocService.getAll().size();
    }

    @Test
    @Disabled
    void add() {
    }

    @Test
    @Disabled
    void update() {
    }

    @Test
    @Disabled
    void delete() {
    }

    @Test
    @Disabled
    void areSuficient() {
    }

    @Test
    @Disabled
    void consuma() {
    }
}