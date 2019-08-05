package concerttours.jobs;

import concerttours.service.VenueService;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import org.apache.log4j.Logger;

import java.io.IOException;

public class UpdateVenuesJob extends AbstractJobPerformable<CronJobModel> {

    private static final Logger LOG = Logger.getLogger(UpdateVenuesJob.class);

    private VenueService venueService;

    @Override
    public PerformResult perform(CronJobModel cronJobModel) {
        LOG.info("Updating venues...");
        try {
            venueService.updateVenues();
            LOG.info("Venues successfully updated");
            return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
        } catch (IOException e) {
            LOG.error("Error to update venues!");
            return new PerformResult(CronJobResult.FAILURE, CronJobStatus.FINISHED);
        }
    }

    public void setVenueService(VenueService venueService) {
        this.venueService = venueService;
    }
}
