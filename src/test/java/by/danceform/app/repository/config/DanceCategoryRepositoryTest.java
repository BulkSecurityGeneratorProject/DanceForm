package by.danceform.app.repository.config;

import by.danceform.app.domain.config.DanceCategory;
import by.danceform.app.domain.config.Trainer;
import by.danceform.app.repository.AbstractRepositoryTest;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Dmitry_Shanko on 12/27/2016.
 */
public class DanceCategoryRepositoryTest extends AbstractRepositoryTest<DanceCategoryRepository, DanceCategory, Long> {

    private static final String EXISTING_NAME = "Test_name";

    private final DanceCategory existing = new DanceCategory();

    @Before
    public void init() {
        existing.setId(EXISTING_ID);
        existing.setName(EXISTING_NAME);
    }

    @Override
    protected Long getExistingId() {
        return EXISTING_ID;
    }

    @Override
    protected DanceCategory getExistingEntity() {
        return existing;
    }

    @Override
    protected DanceCategory getNewEntity() {
        final DanceCategory danceCategory = new DanceCategory();
        danceCategory.setName(RandomStringUtils.random(5));
        return danceCategory;
    }
}
