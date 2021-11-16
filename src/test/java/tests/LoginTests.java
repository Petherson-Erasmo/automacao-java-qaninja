package tests;

import common.BaseTest;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static com.codeborne.selenide.Condition.text;

public class LoginTests extends BaseTest {

    @DataProvider(name = "login-alerts")
    public Object[][] loginProvider() {
        return new Object[][]{
                {"pet@test.com", "123pet", "Usuário e/ou senha inválidos"},
                {"petherson@QA.com", "pet123", "Usuário e/ou senha inválidos"},
                {"", "123pet", "Opps. Cadê o email?"},
                {"pet@test.com", "", "Opps. Cadê a senha?"}
        };
    }

    @Test
    public void shouldSeeLoggedUser() {

        login.open();
        login.with("pet@test.com", "pet123");

        side.loggedUser().shouldHave(text("Pet"));
    }

    @Test(dataProvider = "login-alerts")
    public void shouldSeeLoginAlerts(String email, String pass, String expectAlert) {

        // a seguinte forma funciona do mesmo jeito que o exemplo de cima
        login
                .open()
                .with(email, pass)
                .alert().shouldHave(text(expectAlert));
    }

    @AfterMethod
    public void cleanUp() {
        login.clearSession();
    }
}
