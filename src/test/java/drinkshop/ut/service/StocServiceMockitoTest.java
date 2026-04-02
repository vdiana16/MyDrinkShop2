package drinkshop.ut.service;

import drinkshop.domain.Stoc;
import drinkshop.repository.Repository;
import drinkshop.service.StocService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;

import static org.mockito.Mockito.*;

public class StocServiceMockitoTest {

    private Repository<Integer, Stoc> stocRepo;

    private StocService stocService;

    @BeforeEach
    public void setUp(){
        stocRepo=mock(Repository.class);
        stocService = new StocService(stocRepo);
    }

    @Test
    public void test01_size_quizRepo1() {
        Mockito.when(stocRepo.findAll()).thenReturn(new ArrayList<Stoc>());

        assert 0 == stocService.getAll().size();

        Mockito.verify(stocRepo).findAll();
        Mockito.verify(stocRepo, times(1)).findAll();

        Mockito.verify(stocRepo, never()).save(new Stoc(15, "5", 10, 3));

        //assert examples
        Assertions.assertEquals(0, stocService.getAll().size());
        assert 0 == stocService.getAll().size();
    }


}
