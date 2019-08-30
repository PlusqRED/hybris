package concerttours.interceptors;

import concerttours.model.NewsModel;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * Adds default catalog version and default ID in case no catalog version or no ID is set.
 */
public class NewsCatalogAwareInterceptor implements PrepareInterceptor {
    private String defaultCatalog;
    private String defaultCatalogVersion;
    private CatalogVersionService catalogVersionService;
    private KeyGenerator keyGenerator;

    @Override
    public void onPrepare(final Object model, final InterceptorContext ctx) throws InterceptorException {
        if (model instanceof NewsModel) {
            final NewsModel news = (NewsModel) model;

            if (StringUtils.isBlank(news.getId())) {
                news.setId(createNewsId());
            }

            if (news.getCatalogVersion() == null) {
                news.setCatalogVersion(getDefaultCatalogVersion());
            }
        }
    }

    private String createNewsId() {
        return this.keyGenerator.generate().toString();
    }

    private CatalogVersionModel getDefaultCatalogVersion() {
        return this.catalogVersionService.getCatalogVersion(this.defaultCatalog, this.defaultCatalogVersion);
    }

    @Required
    public void setDefaultCatalog(final String defaultCatalog) {
        this.defaultCatalog = defaultCatalog;
    }

    @Required
    public void setDefaultCatalogVersion(final String defaultCatalogVersion) {
        this.defaultCatalogVersion = defaultCatalogVersion;
    }

    @Required
    public void setKeyGenerator(final KeyGenerator keyGenerator) {
        this.keyGenerator = keyGenerator;
    }

    @Required
    public void setCatalogVersionService(final CatalogVersionService catalogVersionService) {
        this.catalogVersionService = catalogVersionService;
    }
}