package concerttours.service.impl;

import concerttours.model.NewsModel;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

public class OnlineCatalogDefaultNewsService extends DefaultNewsService {
    @Autowired
    private SessionService sessionService;
    @Autowired
    private CatalogVersionService catalogVersionService;

    /**
     * Does the same as the method in the superclass but finds only News belonging to the <code>Default.Online</code>
     * catalog.
     */
    @Override
    public List<NewsModel> getNewsOfTheDay(final Date date) {
        return (List<NewsModel>) this.sessionService.executeInLocalView(new SessionExecutionBody() {
            @Override
            public Object execute() {
                OnlineCatalogDefaultNewsService.this.catalogVersionService.setSessionCatalogVersion("Default", "Online");
                return OnlineCatalogDefaultNewsService.super.getNewsOfTheDay(date);
            }
        });
    }

    public void setSessionService(final SessionService sessionService) {
        this.sessionService = sessionService;
    }

    public void setCatalogVersionService(final CatalogVersionService catalogVersionService) {
        this.catalogVersionService = catalogVersionService;
    }
}
