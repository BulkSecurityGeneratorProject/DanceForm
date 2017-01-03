package by.danceform.app.repository.system;

import by.danceform.app.domain.system.Message;
import by.danceform.app.domain.system.SystemSetting;
import by.danceform.app.repository.AbstractRepositoryTest;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Dmitry_Shanko on 1/3/2017.
 */
public class SystemSettingRepositoryTest extends AbstractRepositoryTest<SystemSettingRepository, SystemSetting, Long> {

    private static final String EXISTING_NAME = "TEST_NAME";
    private static final String EXISTING_VALUE = "test_value";

    private final SystemSetting existing = new SystemSetting();

    @Before
    public void init() {
        existing.setId(EXISTING_ID);
        existing.setName(EXISTING_NAME);
        existing.setValue(EXISTING_VALUE);
    }

    @Test
    public void testFindByName() {
        SystemSetting systemSetting = getRepository().findByName(EXISTING_NAME);
        assertThat(systemSetting, notNullValue());
        assertThat(systemSetting.getName(), equalTo(EXISTING_NAME));
    }

    @Override
    protected Long getExistingId() {
        return EXISTING_ID;
    }

    @Override
    protected SystemSetting getExistingEntity() {
        return existing;
    }

    @Override
    protected SystemSetting getNewEntity() {
        SystemSetting systemSetting = new SystemSetting();
        systemSetting.setName("new_name");
        systemSetting.setValue("new_value");
        return systemSetting;
    }
}
