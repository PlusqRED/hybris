package concerttours.service;

import java.io.IOException;

public interface VenueService {
    void addOrUpdateVenuesFromExternalSource() throws IOException;
}
