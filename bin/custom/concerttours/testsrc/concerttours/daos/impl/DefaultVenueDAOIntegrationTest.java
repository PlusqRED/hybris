package concerttours.daos.impl;

import concerttours.daos.VenueDAO;
import concerttours.jalo.Venue;
import concerttours.model.VenueModel;
import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.Registry;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.model.ModelService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertTrue;

@IntegrationTest
public class DefaultVenueDAOIntegrationTest extends ServicelayerTransactionalTest {

    @Resource
    private VenueDAO venueDAO;

    @Resource
    private ModelService modelService;

    private static final String VENUE_CODE = "VENUE_CODE";

    private static final String VENUE_NAME = "VENUE_NAME";

    private static final String VENUE_LOCATION = "VENUE_LOCATION";

    @Before
    public void setUp() {
        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(1));
            new JdbcTemplate(Registry.getCurrentTenant().getDataSource()).execute("CHECKPOINT ");
            Thread.sleep(TimeUnit.SECONDS.toMillis(1));
        } catch (InterruptedException exc) {

        }
    }

    @Test
    public void venueDAOTest() {
        List<VenueModel> venuesByCode = venueDAO.findVenuesByCode(VENUE_CODE);
        assertTrue("No venue should be returned", venuesByCode.isEmpty());
        List<VenueModel> allVenues = venueDAO.findVenues();
        final int size = allVenues.size();
        final VenueModel venueModel = modelService.create(VenueModel.class);
        venueModel.setCode(VENUE_CODE);
        venueModel.setName(VENUE_NAME);
        venueModel.setLocation(VENUE_LOCATION);
        modelService.save(venueModel);
        allVenues = venueDAO.findVenues();
        assertEquals(size + 1, allVenues.size());
        assertTrue("Venue not found", allVenues.contains(venueModel));
        venuesByCode = venueDAO.findVenuesByCode(VENUE_CODE);
        assertEquals("Did not find the venue we just saved", 1, venuesByCode.size());
        assertEquals("Retrieved venue's code attribute incorrect", VENUE_CODE, venuesByCode.get(0).getCode());
        assertEquals("Retrieved venue's name attribute incorrect", VENUE_NAME, venuesByCode.get(0).getName());
        assertEquals("Retrieved venue's location attribute incorrect", VENUE_LOCATION, venuesByCode.get(0).getLocation());
    }

    @Test
    public void testFindVenues_EmptyStringParam() {
        final List<VenueModel> venues = venueDAO.findVenuesByCode("");
        assertTrue("No venue should be returned", venues.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindVenues_NullParam() {
        venueDAO.findVenuesByCode(null);
    }
}
