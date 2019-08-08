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
    private static final String PROPERTY_WEBSOURCE_FIELD = "service.venues.source";

    @Override
    public void addOrUpdateVenuesFromExternalSource() throws IOException {
        String webSource = configurationService.getConfiguration().getString(PROPERTY_WEBSOURCE_FIELD);
        JSONObject jsonWebSourceResult = JsonReader.readJsonFromUrl(webSource);
        JSONArray venues = jsonWebSourceResult.getJSONObject("resultsPage").getJSONObject("results").getJSONArray("venue");
        List<VenueModel> existingVenues = venueDAO.findVenues();
        for (Object o : venues) {
            JSONObject venue = (JSONObject) o;
            VenueModel venueModel = loadVenueModel(venue);
            Optional<VenueModel> venueFromList = findVenueFromList(venueModel, existingVenues);
            if (venueFromList.isPresent()) {
                VenueModel venueToUpdate = venueFromList.get();
                updateAttributes(venueToUpdate, venueModel);
                venueDAO.save(venueToUpdate);
            } else {
                venueDAO.save(venueModel);
            }
        }
    }

    private void updateAttributes(VenueModel venueToUpdate, VenueModel venueModel) {
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

    private VenueModel loadVenueModel(JSONObject venue) {
        VenueModel venueModel = modelService.create(VenueModel.class);
        venueModel.setCode(String.valueOf(venue.getLong("id")));
        venueModel.setName(venue.getString("displayName"));
        String description = venue.getString("description");
        if(description.isEmpty()) {
            venueModel.setDescription("No description");
        } else {
            venueModel.setDescription(description);
        }
        final String location = venue.getString("zip") + " "
                + venue.getString("street") + " "
                + venue.getJSONObject("city").getString("displayName");
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
}