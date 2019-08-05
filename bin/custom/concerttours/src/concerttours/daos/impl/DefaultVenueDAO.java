package concerttours.daos.impl;

import concerttours.daos.VenueDAO;
import concerttours.model.VenueModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component(value = "venueDAO")
public class DefaultVenueDAO implements VenueDAO {

    @Autowired
    private FlexibleSearchService flexibleSearchService;

    @Autowired
    private ModelService modelService;

    @Override
    public List<VenueModel> findVenues() {
        final String queryString = "SELECT {p:" + VenueModel.PK + "}"
                + "FROM {" + VenueModel._TYPECODE + " AS p} ";
        final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
        return flexibleSearchService.<VenueModel>search(query).getResult();
    }

    @Override
    public List<VenueModel> findVenuesByCode(String code) {
        final String queryString = "SELECT {p:" + VenueModel.PK + "}"
                + "FROM {" + VenueModel._TYPECODE + " AS p} "
                + "WHERE " + "{p:" + VenueModel.CODE + "}=?code ";
        final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
        query.addQueryParameter("code", code);
        return flexibleSearchService.<VenueModel>search(query).getResult();
    }

    @Override
    public void save(VenueModel venueModel) {
        modelService.save(venueModel);
    }

}
