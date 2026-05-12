import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import java.util.List;
import org.junit.jupiter.api.Assertions;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ResourcesPageTest {

    @Test
    @Order(1)
    @DisplayName("Tarayıcıyı Aç ve Landing Page'e Git")
    public void openLandingPage() {
        LogTest.info("Tarayıcı açılıyor ve landing page'e gidiliyor");
        BaseStep.openChromeDriver();
        BaseStep.waitForPageLoad();
        LogTest.info("Landing page yüklendi");
    }

    @Test
    @Order(2)
    @DisplayName("Kaynaklar sayfasına gidiliyor.");
    public void

}
