package by.danceform.app.repository.config;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import by.danceform.app.domain.config.Trainer;
import by.danceform.app.repository.AbstractRepositoryTest;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Dmitry_Shanko on 12/27/2016.
 */
public class TrainerRepositoryTest extends AbstractRepositoryTest<TrainerRepository, Trainer, Long> {

    private static final String EXISTING_NAME = "Test_name";
    private static final String EXISTING_SURNAME = "test_surname";
    private static final boolean EXISTING_VISIBLE = true;

    private static final Integer VISIBLE_AMOUNT = 2;

    private final Trainer existing = new Trainer();

    @Before
    public void init() {
        existing.setId(EXISTING_ID);
        existing.setName(EXISTING_NAME);
        existing.setSurname(EXISTING_SURNAME);
        existing.setVisible(EXISTING_VISIBLE);
    }

    @Test
    public void testFindVisible() {
        final List<Trainer> visibleTrainers = getRepository().findVisible();
        assertThat(visibleTrainers.size(), is(VISIBLE_AMOUNT));
        for(Trainer trainer : visibleTrainers) {
            assertThat(trainer.isVisible(), is(true));
        }
    }

    @Override
    protected Long getExistingId() {
        return EXISTING_ID;
    }

    @Override
    protected Trainer getExistingEntity() {
        return existing;
    }

    @Override
    protected Trainer getNewEntity() {
        final Trainer trainer = new Trainer();
        trainer.setName(randomString(5));
        trainer.setSurname(randomString(10));
        return trainer;
    }
}
