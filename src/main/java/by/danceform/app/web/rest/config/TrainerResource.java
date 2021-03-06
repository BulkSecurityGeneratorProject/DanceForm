package by.danceform.app.web.rest.config;

import by.danceform.app.dto.config.TrainerDTO;
import by.danceform.app.security.AuthoritiesConstants;
import by.danceform.app.security.SecurityUtils;
import by.danceform.app.service.config.TrainerService;
import by.danceform.app.web.rest.util.HeaderUtil;
import com.codahale.metrics.annotation.Timed;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Trainer.
 */
@RestController
@RequestMapping("/api/config/trainers")
@Secured(AuthoritiesConstants.ADMIN)
public class TrainerResource {

    private final Logger log = LoggerFactory.getLogger(TrainerResource.class);

    @Inject
    private TrainerService trainerService;

    /**
     * POST  /trainers : Create a new trainer.
     *
     * @param trainer the trainer to create
     * @return the ResponseEntity with status 201 (Created) and with body the new trainer, or with status 400 (Bad Request) if the trainer has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TrainerDTO> createTrainer(@Valid @RequestBody TrainerDTO trainer) throws URISyntaxException {
        log.debug("REST request to save Trainer : {}", trainer);
        if(trainer.getId() != null) {
            return ResponseEntity.badRequest()
                .headers(HeaderUtil.createFailureAlert("trainer",
                    "idexists",
                    "A new trainer cannot already have an ID"))
                .body(null);
        }
        TrainerDTO result = trainerService.save(trainer);
        return ResponseEntity.created(new URI("/api/config/trainers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("trainer", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /trainers : Updates an existing trainer.
     *
     * @param trainer the trainer to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated trainer,
     * or with status 400 (Bad Request) if the trainer is not valid,
     * or with status 500 (Internal Server Error) if the trainer couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TrainerDTO> updateTrainer(@Valid @RequestBody TrainerDTO trainer) throws URISyntaxException {
        log.debug("REST request to update Trainer : {}", trainer);
        if(trainer.getId() == null) {
            return createTrainer(trainer);
        }
        TrainerDTO result = trainerService.save(trainer);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("trainer", trainer.getId().toString()))
            .body(result);
    }

    /**
     * GET  /trainers : get all the trainers.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of trainers in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured({ AuthoritiesConstants.USER, AuthoritiesConstants.ADMIN, AuthoritiesConstants.ANONYMOUS })
    public ResponseEntity<List<TrainerDTO>> getAllTrainers() throws URISyntaxException {
        log.debug("REST request to get a page of Trainers");
        List<TrainerDTO> result;
        if(SecurityUtils.isAdmin()) {
            result = trainerService.findAll();
        } else {
            result = trainerService.findVisible();
        }
        Collections.sort(result,
            (o1, o2) -> ObjectUtils.compare(o1.getSurname() + " " + o1.getName(),
                o2.getSurname() + " " + o2.getName()));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * GET  /trainers/:id : get the "id" trainer.
     *
     * @param id the id of the trainer to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the trainer, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/{id}",
                    method = RequestMethod.GET,
                    produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured({ AuthoritiesConstants.USER, AuthoritiesConstants.ADMIN, AuthoritiesConstants.ANONYMOUS })
    public ResponseEntity<TrainerDTO> getTrainer(@PathVariable Long id) {
        log.debug("REST request to get Trainer : {}", id);
        TrainerDTO trainer = trainerService.findOne(id);
        return Optional.ofNullable(trainer)
            .filter(result -> {
                if(SecurityUtils.isAdmin()) {
                    return true;
                }
                return result.isVisible();
            })
            .map(result -> new ResponseEntity<>(result, HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /trainers/:id : delete the "id" trainer.
     *
     * @param id the id of the trainer to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/{id}",
                    method = RequestMethod.DELETE,
                    produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteTrainer(@PathVariable Long id) {
        log.debug("REST request to delete Trainer : {}", id);
        trainerService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("trainer", id.toString())).build();
    }

}
