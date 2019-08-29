package concerttours.controller;

import concerttours.facades.NewsFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.sql.Date;
import java.time.LocalDate;

@Controller
public class NewsController {
    private NewsFacade newsFacade;

    @GetMapping("/news")
    private String getNews(Model model) {
        model.addAttribute("news", newsFacade.getNewsOfTheDay(Date.valueOf(LocalDate.now())));
        return "NewsList";
    }

    @Autowired
    public void setNewsFacade(NewsFacade newsFacade) {
        this.newsFacade = newsFacade;
    }
}
