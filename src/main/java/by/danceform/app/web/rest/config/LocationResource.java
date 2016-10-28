package by.danceform.app.web.rest.config;

import by.danceform.app.dto.config.LocationDTO;
import by.danceform.app.security.AuthoritiesConstants;
import by.danceform.app.security.SecurityUtils;
import by.danceform.app.service.config.LocationService;
import by.danceform.app.web.rest.util.HeaderUtil;
import by.danceform.app.web.rest.util.PaginationUtil;
import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
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
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Location.
 */
@RestController
@RequestMapping("/api/config/locations")
@Secured(AuthoritiesConstants.ADMIN)
public class LocationResource {

    private final Logger log = LoggerFactory.getLogger(LocationResource.class);

    @Inject
    private LocationService locationService;

    /**
     * POST  /locations : Create a new location.
     *
     * @param location the location to create
     * @return the ResponseEntity with status 201 (Created) and with body the new location, or with status 400 (Bad Request) if the location has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<LocationDTO> createLocation(@Valid @RequestBody LocationDTO location)
        throws URISyntaxException {
        log.debug("REST request to save Location : {}", location);
        if(location.getId() != null) {
            return ResponseEntity.badRequest()
                .headers(HeaderUtil.createFailureAlert("location",
                    "idexists",
                    "A new location cannot already have an ID"))
                .body(null);
        }
        LocationDTO result = locationService.save(location);
        return ResponseEntity.created(new URI("/api/config/locations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("location", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /locations : Updates an existing location.
     *
     * @param location the location to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated location,
     * or with status 400 (Bad Request) if the location is not valid,
     * or with status 500 (Internal Server Error) if the location couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<LocationDTO> updateLocation(@Valid @RequestBody LocationDTO location)
        throws URISyntaxException {
        log.debug("REST request to update Location : {}", location);
        if(location.getId() == null) {
            return createLocation(location);
        }
        LocationDTO result = locationService.save(location);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("location", location.getId().toString()))
            .body(result);
    }

    /**
     * GET  /locations : get all the locations.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of locations in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured({ AuthoritiesConstants.USER, AuthoritiesConstants.ADMIN, AuthoritiesConstants.ANONYMOUS })
    public ResponseEntity<List<LocationDTO>> getAllLocations(Pageable pageable) throws URISyntaxException {
        log.debug("REST request to get a page of Locations");
        if(SecurityUtils.isAdmin()) {
            Page<LocationDTO> page = locationService.findAll(pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/config/locations");
            return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(locationService.findVisible(), HttpStatus.OK);
        }
    }

    /**
     * GET  /locations/:id : get the "id" location.
     *
     * @param id the id of the location to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the location, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/{id}",
                    method = RequestMethod.GET,
                    produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured({ AuthoritiesConstants.USER, AuthoritiesConstants.ADMIN, AuthoritiesConstants.ANONYMOUS })
    public ResponseEntity<LocationDTO> getLocation(@PathVariable Long id) {
        log.debug("REST request to get Location : {}", id);
        LocationDTO location = locationService.findOne(id);
        return Optional.ofNullable(location)
            .map(result -> new ResponseEntity<>(result, HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /locations/:id : delete the "id" location.
     *
     * @param id the id of the location to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/{id}",
                    method = RequestMethod.DELETE,
                    produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteLocation(@PathVariable Long id) {
        log.debug("REST request to delete Location : {}", id);
        locationService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("location", id.toString())).build();
    }

}
