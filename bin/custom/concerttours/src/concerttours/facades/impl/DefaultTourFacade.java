package concerttours.facades.impl;

import com.sun.istack.internal.NotNull;
import concerttours.data.ConcertSummaryData;
import concerttours.data.TicketData;
import concerttours.data.TourData;
import concerttours.data.VenueData;
import concerttours.enums.ConcertType;
import concerttours.facades.TourFacade;
import concerttours.model.ConcertModel;
import concerttours.model.TicketModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.variants.model.VariantProductModel;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DefaultTourFacade implements TourFacade {
    private ProductService productService;

    @Override
    public TourData getTourDetails(final String tourId) {
        if (tourId == null) {
            throw new IllegalArgumentException("Tour id cannot be null");
        }
        final ProductModel product = productService.getProductForCode(tourId);
        if (product == null) {
            return null;
        }

        // Create a list of ConcertSummaryData from the matches
        final List<ConcertSummaryData> concerts = new ArrayList<>();
        if (product.getVariants() != null) {
            for (final VariantProductModel variant : product.getVariants()) {
                if (variant instanceof ConcertModel) {
                    final ConcertModel concert = (ConcertModel) variant;
                    final ConcertSummaryData summary = new ConcertSummaryData();
                    summary.setId(concert.getCode());
                    summary.setDate(concert.getDate());
                   // summary.setVenue(createVenueData(concert));
                    summary.setType(concert.getConcertType() == ConcertType.OPENAIR ? "Outdoors" : "Indoors");
                    summary.setCountDown(concert.getDaysUntil());
                    summary.setTickets(loadTickets(concert));
                    concerts.add(summary);
                }
            }
        }

        // Now we can create the TourData transfer object
        final TourData tourData = new TourData();
        tourData.setId(product.getCode());
        tourData.setTourName(product.getName());
        tourData.setDescription(product.getDescription());
        tourData.setConcerts(concerts);
        return tourData;
    }

    private List<TicketData> loadTickets(ConcertModel concert) {
        return concert.getTickets().stream()
                .map(this::toTicketData)
                .collect(Collectors.toList());
    }

    private TicketData toTicketData(TicketModel ticketModel) {
        TicketData ticketData = new TicketData();
        ticketData.setId(ticketModel.getCode());
        ticketData.setAmount(ticketModel.getAmount());
        ticketData.setPrice(ticketModel.getPrice());
        return ticketData;
    }

    private static VenueData createVenueData(@NotNull final ConcertModel concert) {
        final VenueData venueData = new VenueData();
        venueData.setId(concert.getVenue().getCode());
        venueData.setName(concert.getVenue().getName());
        venueData.setLocation(concert.getVenue().getLocation());
        return venueData;
    }

    @Autowired
    public void setProductService(final ProductService productService) {
        this.productService = productService;
    }
}