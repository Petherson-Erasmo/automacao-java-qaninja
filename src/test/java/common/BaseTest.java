package common;

import com.codeborne.selenide.Configuration;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import pages.LoginPage;
import pages.MoviePage;
import pages.SideBar;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Properties;

import static com.codeborne.selenide.Selenide.screenshot;

public class BaseTest {
    protected static LoginPage login;
    protected static SideBar side;
    protected static MoviePage movie;

    @BeforeMethod
    public void start() {

        Properties prop = new Properties();
        InputStream inputFile = getClass().getClassLoader().getResourceAsStream("config.properties");

        try {
            prop.load(inputFile);
        } catch (Exception ex) {
            System.out.println("não foi possível carregar o config.properties. Trace => " + ex.getMessage());
        }


        Configuration.browser = prop.getProperty("browser");
        Configuration.baseUrl = prop.getProperty("url");
        Configuration.timeout = Long.parseLong(prop.getProperty("timeout"));

        login = new LoginPage();
        side = new SideBar();
        movie = new MoviePage();
    }

    @AfterMethod
    public void finish() {
        //Screenshot pelo Selenide
        String tempShot = screenshot("temp_shot");

        // Transforma em binário e anexamos no report allure
        try {
            BufferedImage bimage = ImageIO.read(new File(tempShot));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bimage, "png", baos);
            byte [] finalShot = baos.toByteArray();

            io.qameta.allure.Allure.addAttachment("Evidência", new ByteArrayInputStream(finalShot));

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("Erro ao anexar screenshot");
        }
    }
}
