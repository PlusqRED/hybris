package concerttours.facades;

import concerttours.data.NewsData;

import java.util.Date;
import java.util.List;

public interface NewsFacade {
    List<NewsData> getNewsOfTheDay(final Date date);
}
