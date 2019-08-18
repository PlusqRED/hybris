package concerttours.daos;

import concerttours.model.VenueModel;

import java.util.List;

public interface VenueDAO {
    List<VenueModel> findVenues();

    List<VenueModel> findVenuesByCode(String code);

    void save(VenueModel venueModel);
}
