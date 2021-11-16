package tests;

import common.BaseTest;
import libs.Database;
import models.MovieModel;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;

public class MovieTests extends BaseTest {

    private Database db;

    @BeforeMethod
    public void setUp() {
        login
                .open()
                .with("pet@test.com", "pet123");

        side.loggedUser().shouldHave(text("Pet"));

        db = new Database();
    }

    @Test
    public void shouldRegistreANewMovie() {

        MovieModel movieData = new MovieModel(
                "Jumanji - Próxima Fase",
                "Pré-venda",
                2020,
                "16/01/2020",
                Arrays.asList("The Rock", "Jack Black", "Kevin Hart", "Karen Gillan", "Danny DeVito"),
                "Tentado a revisitar o mundo de Jumanji, Spencer decide consertar o bug no jogo que permite que sejam transportados ao local",
                "jumanji.png"
        );

        db.deleteMovie(movieData.title);

        movie
                .add()
                .create(movieData)
                .items().findBy(text(movieData.title)).shouldBe(visible);
    }

    @Test
    public void shouldSearchOneMovie() {
        db.insertMovies();

        movie.search("Bad").items().shouldHaveSize(1);
    }

    @AfterMethod
    public void cleanUp() {
        login.clearSession();
    }
}
