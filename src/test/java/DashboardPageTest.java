import org.junit.jupiter.api.*;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DashboardPageTest extends BaseStep {

    @BeforeAll
    public void setup() {
        openChromeDriver();
    }

    @AfterAll
    public void teardown() {
        driverQuit();
    }

    // DOM'daki en buyuk overflow container'i bulup pixel kadar kaydirir.
    private void scrollAnywhere(int pixels) {
        try {
            ((JavascriptExecutor) driver).executeScript(
                    "var best=null,max=0;" +
                    "document.querySelectorAll('*').forEach(function(el){" +
                    "  var s=window.getComputedStyle(el);" +
                    "  if((s.overflowY==='auto'||s.overflowY==='scroll')" +
                    "     && el.offsetParent!==null){" +
                    "    var ov=el.scrollHeight-el.clientHeight;" +
                    "    if(ov>max){max=ov;best=el;}" +
                    "  }" +
                    "});" +
                    "if(best)best.scrollTop+=arguments[0];" +
                    "else window.scrollBy(0,arguments[0]);",
                    pixels);
            waitSeconds(1);
        } catch (Exception ignored) {}
    }

    // Tum scrollable elementlerin scrollTop'unu sifirlar, pencereyi de basa cek.
    private void scrollToTop() {
        try {
            ((JavascriptExecutor) driver).executeScript(
                    "document.querySelectorAll('*').forEach(function(el){" +
                    "  var s=window.getComputedStyle(el);" +
                    "  if((s.overflowY==='auto'||s.overflowY==='scroll')" +
                    "     && el.scrollTop>0 && el.offsetParent!==null)" +
                    "    el.scrollTop=0;" +
                    "});" +
                    "window.scrollTo(0,0);");
            waitSeconds(1);
        } catch (Exception ignored) {}
    }

    // Belirtilen WebElement'i goruntu alanina ortala.
    private void scrollIntoView(WebElement element) {
        try {
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView({block:'center',behavior:'instant'});",
                    element);
            waitSeconds(1);
        } catch (Exception ignored) {}
    }

    // ─────────────────────────────────────────────────────────────
    // 1. Login
    // ─────────────────────────────────────────────────────────────
    @Test
    @Order(1)
    @DisplayName("Kullanici girisi yapiliyor")
    public void loginTest() {
        LogTest.info("Login sayfasi yukleniyor.");

        WebElement emailInput = findElementXpathWithWait(
                "//*[@id=\"login-email\"]", TimeOut.SHORT.value);
        clearAndType(emailInput, "ademtopcu714@gmail.com", "E-posta alani");

        WebElement passwordInput = findElementXpathWithWait(
                "//*[@id=\"login-password\"]", TimeOut.SHORT.value);
        clearAndType(passwordInput, "Adem123!", "Sifre alani");

        WebElement loginBtn = findElementXpathWithWait(
                "//*[@id=\"root\"]/div/div[2]/div/div[2]/form/button",
                TimeOut.SHORT.value);
        clickElement(loginBtn, "Giris Yap butonu");
        waitSeconds(3);
        LogTest.info("Login tamamlandi.");
    }

    // ─────────────────────────────────────────────────────────────
    // 2. Dashboard sayfasina git
    // ─────────────────────────────────────────────────────────────
    @Test
    @Order(2)
    @DisplayName("Navigate to Dashboard Page")
    public void navigateToDashboardTest() {
        LogTest.info("Dashboard sayfasina geciliyor.");

        WebElement dashboardSidebarBtn = findElementXpathWithWait(
                "//*[@id=\"root\"]/div/aside/div[2]/div[1]/nav/button[1]",
                TimeOut.SHORT.value);
        assertTrue(dashboardSidebarBtn.isDisplayed(), "Dashboard sidebar butonu gorulmuyor!");
        clickElement(dashboardSidebarBtn, "Dashboard sidebar button");
        waitSeconds(2);

        WebElement page = findElementXpathWithWait("//main", TimeOut.SHORT.value);
        assertTrue(page.isDisplayed(), "Dashboard sayfasi yuklenemedi!");
        LogTest.info("Dashboard sayfasi yuklendi.");
    }

    // ─────────────────────────────────────────────────────────────
    // 3. Proje "Detaylari Gor" -> Geri don
    // ─────────────────────────────────────────────────────────────
    @Test
    @Order(3)
    @DisplayName("View Project Details and Go Back")
    public void viewProjectDetailsTest() {
        LogTest.info("Projelerin oldugu bolume scroll yapiliyor.");
        scrollAnywhere(400);
        scrollAnywhere(400);

        LogTest.info("Detaylari Gor butonuna tiklaniyor.");
        WebElement detailsBtn = findElementXpathWithWait(
                "//*[@id=\"root\"]/div/div/main/div/div[2]/div[1]/div/div[2]/div/div/div[1]/div/div[4]/button",
                TimeOut.SHORT.value);
        scrollIntoView(detailsBtn);
        clickElement(detailsBtn, "Detaylari Gor");
        waitSeconds(3);
        LogTest.info("Proje detay sayfasi acildi.");

        LogTest.info("Geri donuluyor.");
        navigateBack();
        waitSeconds(3);

        WebElement page = findElementXpathWithWait("//main", TimeOut.SHORT.value);
        assertTrue(page.isDisplayed(), "Dashboard sayfasina geri donulemedi!");
        LogTest.info("Dashboard sayfasina donuldu.");
    }

    // ─────────────────────────────────────────────────────────────
    // 4. Performans grafigi - sekme degistir + grafik tipi
    // ─────────────────────────────────────────────────────────────
    @Test
    @Order(4)
    @DisplayName("Performance Graph Tabs and Chart Type")
    public void performanceGraphTest() {
        LogTest.info("Performans grafigi bolumune scroll yapiliyor.");
        scrollToTop();
        scrollAnywhere(600);
        scrollAnywhere(400);

        LogTest.info("Hata Is Akisi Durumu sekmesine tiklaniyor.");
        WebElement workflowTab = findElementXpathWithWait(
                "//*[@id=\"root\"]/div/div/main/div/div[2]/div[4]/div/div[2]/div/div[1]/div[1]/button[2]",
                TimeOut.SHORT.value);
        scrollIntoView(workflowTab);
        clickElement(workflowTab, "Hata Is Akisi Durumu");
        waitSeconds(2);

        LogTest.info("Ciddiyet sekmesine tiklaniyor.");
        WebElement severityTab = findElementXpathWithWait(
                "//*[@id=\"root\"]/div/div/main/div/div[2]/div[4]/div/div[2]/div/div[1]/div[1]/button[3]",
                TimeOut.SHORT.value);
        scrollIntoView(severityTab);
        clickElement(severityTab, "Ciddiyet");
        waitSeconds(2);

        LogTest.info("Pasta grafigine geciliyor.");
        WebElement pieBtn = findElementXpathWithWait(
                "//*[@id=\"root\"]/div/div/main/div/div[2]/div[4]/div/div[2]/div/div[1]/div[2]/button[2]",
                TimeOut.SHORT.value);
        scrollIntoView(pieBtn);
        clickElement(pieBtn, "Pasta grafigi");
        waitSeconds(2);
        LogTest.info("Performans grafigi etkilesimleri tamamlandi.");
    }

    // ─────────────────────────────────────────────────────────────
    // 5. Test Projelerim - Projeye Gore + Arama
    // ─────────────────────────────────────────────────────────────
    @Test
    @Order(5)
    @DisplayName("Test Projects - Group By Project and Search")
    public void testProjectsSectionTest() {
        LogTest.info("Test Projelerim bolumune scroll yapiliyor.");
        scrollAnywhere(500);
        scrollAnywhere(500);
        scrollAnywhere(500);

        LogTest.info("Projeye Gore butonuna tiklaniyor.");
        WebElement byProjectBtn = findElementXpathWithWait(
                "//*[@id=\"root\"]/div/div/main/div/div[2]/div[7]/div/div[2]/div/div[1]/div[1]/button[2]",
                TimeOut.SHORT.value);
        scrollIntoView(byProjectBtn);
        clickElement(byProjectBtn, "Projeye Gore");
        waitSeconds(2);

        LogTest.info("Test uzmani veya proje arama yapiliyor.");
        WebElement searchInput = findElementXpathWithWait(
                "//*[@id=\"root\"]/div/div/main/div/div[2]/div[7]/div/div[2]/div/div[1]/div[2]/input",
                TimeOut.SHORT.value);
        scrollIntoView(searchInput);
        clearAndType(searchInput, "Test", "Test uzmani veya proje ara");
        waitSeconds(2);

        searchInput.sendKeys(Keys.CONTROL + "a");
        searchInput.sendKeys(Keys.DELETE);
        waitSeconds(1);
        LogTest.info("Test Projelerim etkilesimleri tamamlandi.");
    }
}