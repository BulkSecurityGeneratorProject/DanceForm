package by.danceform.app.web.rest;

import by.danceform.app.DanceFormApp;
import by.danceform.app.domain.competition.Competition;
import by.danceform.app.dto.competition.CompetitionDTO;
import by.danceform.app.repository.competition.CompetitionRepository;
import by.danceform.app.service.competition.CompetitionService;
import by.danceform.app.service.mapper.CompetitionMapper;
import by.danceform.app.web.rest.competition.CompetitionResource;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for the CompetitionResource REST controller.
 *
 * @see CompetitionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DanceFormApp.class)
public class CompetitionResourceIntTest {

    private static final String DEFAULT_NAME = "A";
    private static final String UPDATED_NAME = "B";

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_IS_VISIBLE = false;
    private static final Boolean UPDATED_IS_VISIBLE = true;
    private static final String DEFAULT_ORGANIZER = "AAAAA";
    private static final String UPDATED_ORGANIZER = "BBBBB";
    private static final String DEFAULT_LOCATION = "A";
    private static final String UPDATED_LOCATION = "B";

    @Inject
    private CompetitionRepository competitionRepository;

    @Inject
    private CompetitionMapper competitionMapper;

    @Inject
    private CompetitionService competitionService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restCompetitionMockMvc;

    private Competition competition;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CompetitionResource competitionResource = new CompetitionResource();
        ReflectionTestUtils.setField(competitionResource, "competitionService", competitionService);
        this.restCompetitionMockMvc = MockMvcBuilders.standaloneSetup(competitionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter)
            .build();
    }

    /**
     * Create an entity for this test.
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Competition createEntity(EntityManager em) {
        Competition competition = new Competition();
        competition.setName(DEFAULT_NAME);
        competition.setDate(DEFAULT_DATE);
        competition.setVisible(DEFAULT_IS_VISIBLE);
        competition.setOrganizer(DEFAULT_ORGANIZER);
        competition.setLocation(DEFAULT_LOCATION);
        return competition;
    }

    @Before
    public void initTest() {
        competition = createEntity(em);
    }

    @Test
    @Transactional
    public void createCompetition() throws Exception {
        int databaseSizeBeforeCreate = competitionRepository.findAll().size();

        // Create the Competition
        CompetitionDTO competitionDTO = competitionMapper.competitionToCompetitionDTO(competition);

        restCompetitionMockMvc.perform(post("/api/competitions").contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(competitionDTO))).andExpect(status().isCreated());

        // Validate the Competition in the database
        List<Competition> competitions = competitionRepository.findAll();
        assertThat(competitions).hasSize(databaseSizeBeforeCreate + 1);
        Competition testCompetition = competitions.get(competitions.size() - 1);
        assertThat(testCompetition.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCompetition.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testCompetition.isVisible()).isEqualTo(DEFAULT_IS_VISIBLE);
        assertThat(testCompetition.getOrganizer()).isEqualTo(DEFAULT_ORGANIZER);
        assertThat(testCompetition.getLocation()).isEqualTo(DEFAULT_LOCATION);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = competitionRepository.findAll().size();
        // set the field null
        competition.setName(null);

        // Create the Competition, which fails.
        CompetitionDTO competitionDTO = competitionMapper.competitionToCompetitionDTO(competition);

        restCompetitionMockMvc.perform(post("/api/competitions").contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(competitionDTO))).andExpect(status().isBadRequest());

        List<Competition> competitions = competitionRepository.findAll();
        assertThat(competitions).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = competitionRepository.findAll().size();
        // set the field null
        competition.setDate(null);

        // Create the Competition, which fails.
        CompetitionDTO competitionDTO = competitionMapper.competitionToCompetitionDTO(competition);

        restCompetitionMockMvc.perform(post("/api/competitions").contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(competitionDTO))).andExpect(status().isBadRequest());

        List<Competition> competitions = competitionRepository.findAll();
        assertThat(competitions).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLocationIsRequired() throws Exception {
        int databaseSizeBeforeTest = competitionRepository.findAll().size();
        // set the field null
        competition.setLocation(null);

        // Create the Competition, which fails.
        CompetitionDTO competitionDTO = competitionMapper.competitionToCompetitionDTO(competition);

        restCompetitionMockMvc.perform(post("/api/competitions").contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(competitionDTO))).andExpect(status().isBadRequest());

        List<Competition> competitions = competitionRepository.findAll();
        assertThat(competitions).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCompetitions() throws Exception {
        // Initialize the database
        competitionRepository.saveAndFlush(competition);

        // Get all the competitions
        restCompetitionMockMvc.perform(get("/api/competitions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(competition.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].isVisible").value(hasItem(DEFAULT_IS_VISIBLE.booleanValue())))
            .andExpect(jsonPath("$.[*].organizer").value(hasItem(DEFAULT_ORGANIZER.toString())))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION.toString())));
    }

    @Test
    @Transactional
    public void getCompetition() throws Exception {
        // Initialize the database
        competitionRepository.saveAndFlush(competition);

        // Get the competition
        restCompetitionMockMvc.perform(get("/api/competitions/{id}", competition.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(competition.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.isVisible").value(DEFAULT_IS_VISIBLE.booleanValue()))
            .andExpect(jsonPath("$.organizer").value(DEFAULT_ORGANIZER.toString()))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCompetition() throws Exception {
        // Get the competition
        restCompetitionMockMvc.perform(get("/api/competitions/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCompetition() throws Exception {
        // Initialize the database
        competitionRepository.saveAndFlush(competition);
        int databaseSizeBeforeUpdate = competitionRepository.findAll().size();

        // Update the competition
        Competition updatedCompetition = competitionRepository.findOne(competition.getId());
        updatedCompetition.setName(UPDATED_NAME);
        updatedCompetition.setDate(UPDATED_DATE);
        updatedCompetition.setVisible(UPDATED_IS_VISIBLE);
        updatedCompetition.setOrganizer(UPDATED_ORGANIZER);
        updatedCompetition.setLocation(UPDATED_LOCATION);
        CompetitionDTO competitionDTO = competitionMapper.competitionToCompetitionDTO(updatedCompetition);

        restCompetitionMockMvc.perform(put("/api/competitions").contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(competitionDTO))).andExpect(status().isOk());

        // Validate the Competition in the database
        List<Competition> competitions = competitionRepository.findAll();
        assertThat(competitions).hasSize(databaseSizeBeforeUpdate);
        Competition testCompetition = competitions.get(competitions.size() - 1);
        assertThat(testCompetition.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCompetition.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testCompetition.isVisible()).isEqualTo(UPDATED_IS_VISIBLE);
        assertThat(testCompetition.getOrganizer()).isEqualTo(UPDATED_ORGANIZER);
        assertThat(testCompetition.getLocation()).isEqualTo(UPDATED_LOCATION);
    }

    @Test
    @Transactional
    public void deleteCompetition() throws Exception {
        // Initialize the database
        competitionRepository.saveAndFlush(competition);
        int databaseSizeBeforeDelete = competitionRepository.findAll().size();

        // Get the competition
        restCompetitionMockMvc.perform(delete("/api/competitions/{id}",
            competition.getId()).accept(TestUtil.APPLICATION_JSON_UTF8)).andExpect(status().isOk());

        // Validate the database is empty
        List<Competition> competitions = competitionRepository.findAll();
        assertThat(competitions).hasSize(databaseSizeBeforeDelete - 1);
    }
}
