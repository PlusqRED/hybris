package concerttours.facades.impl;

import concerttours.data.NewsData;
import concerttours.facades.NewsFacade;
import concerttours.model.NewsModel;
import concerttours.service.NewsService;
import concerttours.service.impl.OnlineCatalogDefaultNewsService;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class DefaultNewsFacade implements NewsFacade {
    private OnlineCatalogDefaultNewsService onlineCatalogDefaultNewsService;

    @Override
    public List<NewsData> getNewsOfTheDay(Date date) {
        return  onlineCatalogDefaultNewsService.getNewsOfTheDay(date).stream()
                .map(this::toNewsData)
                .collect(Collectors.toList());
    }

    private NewsData toNewsData(NewsModel newsModel) {
        NewsData newsData = new NewsData();
        newsData.setId(newsModel.getId());
        newsData.setContent(newsModel.getContent());
        newsData.setHeadline(newsModel.getHeadline());
        newsData.setDate(newsModel.getDate());
        return newsData;
    }

    public void setOnlineCatalogDefaultNewsService(OnlineCatalogDefaultNewsService onlineCatalogDefaultNewsService) {
        this.onlineCatalogDefaultNewsService = onlineCatalogDefaultNewsService;
    }
}
