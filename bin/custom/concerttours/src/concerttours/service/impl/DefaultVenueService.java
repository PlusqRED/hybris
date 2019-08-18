package concerttours.service.impl;

import concerttours.daos.VenueDAO;
import concerttours.model.VenueModel;
import concerttours.service.VenueService;
import concerttours.util.JsonReader;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class DefaultVenueService implements VenueService {

    private ConfigurationService configurationService;
    private VenueDAO venueDAO;
    private ModelService modelService;
    private JsonReader jsonReader;

    protected static final String PROPERTY_WEBSOURCE_FIELD = "service.venues.source";
    protected static final String RESULTS_PAGE = "resultsPage";
    protected static final String RESULTS = "results";
    protected static final String VENUE = "venue";
    protected static final String ID = "id";
    protected static final String DISPLAY_NAME = "displayName";
    protected static final String DESCRIPTION = "description";
    protected static final String ZIP = "zip";
    protected static final String STREET = "street";
    protected static final String CITY = "city";

    @Override
    public void addOrUpdateVenuesFromExternalSource() throws IOException {
        String webSource = configurationService.getConfiguration().getString(PROPERTY_WEBSOURCE_FIELD);
        JSONObject jsonWebSourceResult = jsonReader.readJsonFromUrl(webSource);
        JSONArray venues = jsonWebSourceResult.getJSONObject(RESULTS_PAGE)
                .getJSONObject(RESULTS)
                .getJSONArray(VENUE);
        List<VenueModel> existingVenues = venueDAO.findVenues();
        for (Object o : venues) {
            JSONObject venue = (JSONObject) o;
            VenueModel venueModel = loadVenueModel(venue);
            VenueModel venueToUpdateOrToSave = findVenueFromList(venueModel, existingVenues)
                    .orElseGet(() -> modelService.create(VenueModel.class));
            updateAttributes(venueToUpdateOrToSave, venueModel);
            venueDAO.save(venueToUpdateOrToSave);
        }
    }

    private void updateAttributes(VenueModel venueToUpdate, VenueModel venueModel) {
        venueToUpdate.setCode(venueModel.getCode());
        venueToUpdate.setName(venueModel.getName());
        venueToUpdate.setDescription(venueModel.getDescription());
        venueToUpdate.setLocation(venueModel.getLocation());
        venueToUpdate.setConcerts(venueModel.getConcerts());
    }

    private Optional<VenueModel> findVenueFromList(VenueModel venueModel, List<VenueModel> existingVenues) {
        return existingVenues.stream()
                .filter(venue -> venue.getCode().equals(venueModel.getCode()))
                .findAny();
    }

    protected VenueModel loadVenueModel(JSONObject venue) {
        VenueModel venueModel = new VenueModel();
        venueModel.setCode(String.valueOf(venue.getLong(ID)));
        venueModel.setName(venue.getString(DISPLAY_NAME));
        venueModel.setDescription(venue.getString(DESCRIPTION));
        String location = String.join(" ",
                venue.getString(ZIP),
                venue.getString(STREET),
                venue.getJSONObject(CITY).getString(DISPLAY_NAME));
        venueModel.setLocation(location);
        return venueModel;
    }

    public void setConfigurationService(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    public void setVenueDAO(VenueDAO venueDAO) {
        this.venueDAO = venueDAO;
    }

    public void setModelService(ModelService modelService) {
        this.modelService = modelService;
    }

    public void setJsonReader(JsonReader jsonReader) {
        this.jsonReader = jsonReader;
    }
}
