package concerttours.service.impl;

import concerttours.daos.VenueDAO;
import concerttours.model.VenueModel;
import concerttours.util.JsonReader;
import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;
import org.apache.commons.configuration.Configuration;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Collections;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@UnitTest
public class DefaultVenueServiceUnitTest {
    @Mock
    private ConfigurationService configurationService;

    @Mock
    private ModelService modelService;

    @Mock
    private Configuration configuration;

    @Mock
    private JsonReader jsonReader;

    @Mock
    private VenueDAO venueDAO;

    @InjectMocks
    private DefaultVenueService defaultVenueService;

    private VenueModel venueModel;

    private final static Long ID = 1L;
    private final static String DISPLAY_NAME = "testDisplayName";
    private final static String DESCRIPTION = "testDesctiption";
    private final static String ZIP = "testZip";
    private final static String STREET = "testStreet";

    @Before
    public void init() throws IOException {
        MockitoAnnotations.initMocks(this);
        when(configurationService.getConfiguration()).thenReturn(configuration);

        JSONObject testJsonWebSourceResult = new JSONObject();
        JSONObject resultsPage = new JSONObject();
        JSONObject results = new JSONObject();
        JSONObject testJsonVenueModel = getTestJsonVenueModel();
        venueModel = defaultVenueService.loadVenueModel(testJsonVenueModel);
        JSONArray venue = new JSONArray();

        venue.put(testJsonVenueModel);
        results.put(DefaultVenueService.VENUE, venue);
        resultsPage.put(DefaultVenueService.RESULTS, results);
        testJsonWebSourceResult.put(DefaultVenueService.RESULTS_PAGE, resultsPage);

        when(jsonReader.readJsonFromUrl(anyString())).thenReturn(testJsonWebSourceResult);
    }

    private JSONObject getTestJsonVenueModel() {
        JSONObject city = new JSONObject();
        JSONObject testJsonVenueModel = new JSONObject();
        testJsonVenueModel.put(DefaultVenueService.ID, ID);
        testJsonVenueModel.put(DefaultVenueService.DISPLAY_NAME, DISPLAY_NAME);
        testJsonVenueModel.put(DefaultVenueService.DESCRIPTION, DESCRIPTION);
        testJsonVenueModel.put(DefaultVenueService.ZIP, ZIP);
        testJsonVenueModel.put(DefaultVenueService.STREET, STREET);
        city.put(DefaultVenueService.DISPLAY_NAME, DISPLAY_NAME);
        testJsonVenueModel.put(DefaultVenueService.CITY, city);
        return testJsonVenueModel;
    }

    @Test
    public void addVenueFromExternalSourceTest() throws IOException {
        when(modelService.create(any(Class.class))).thenReturn(venueModel);
        when(venueDAO.findVenues()).thenReturn(Collections.emptyList());

        defaultVenueService.addOrUpdateVenuesFromExternalSource();

        verify(modelService, times(1)).create(any(Class.class));
        verify(jsonReader, times(1)).readJsonFromUrl(anyString());
        verify(venueDAO, times(1)).save(venueModel);
    }

    @Test
    public void updateVenueFromExternalSourceTest() throws IOException {
        when(venueDAO.findVenues()).thenReturn(Collections.singletonList(venueModel));

        defaultVenueService.addOrUpdateVenuesFromExternalSource();

        verify(modelService, times(0)).create(any(Class.class));
        verify(jsonReader, times(1)).readJsonFromUrl(anyString());
        verify(venueDAO, times(1)).save(venueModel);
    }

}
