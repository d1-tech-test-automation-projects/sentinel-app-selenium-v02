import org.junit.jupiter.api.*;
import org.openqa.selenium.WebElement;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LogoutTest extends BaseStep {

    // ============================================================
    // SETUP / TEARDOWN
    // ============================================================

    @BeforeAll
    public void setup() {
        openChromeDriver();
    }

    @AfterAll
    public void teardown() {
        driverQuit();
    }

    // ============================================================
    // STABLE LOCATORS
    // ============================================================

    // Sidebar Cikis Yap butonu (data-menu-key tabanli - kararli)
    private static final String XPATH_SIDEBAR_LOGOUT_BTN  = "//button[@data-menu-key='logout']";
    // Onay popup'indaki Cikis Yap butonu (dialog scope + metin)
    private static final String XPATH_LOGOUT_CONFIRM_BTN  = "(//*[@role='dialog' or @role='alertdialog'])[last()]//button[normalize-space(.)='Çıkış Yap']";

    // ============================================================
    // TEST: 1 — GIRIS YAP
    // ============================================================

    @Test
    @Order(1)
    @DisplayName("Kullanici girisi yapiliyor")
    public void loginTest() {
        LogTest.info("Login sayfasi yukleniyor.");

        WebElement emailInput = BaseStep.findElementXpathWithWait(
                "//*[@id=\"login-email\"]", TimeOut.SHORT.value);
        BaseStep.clearAndType(emailInput, "ademtopcu714@gmail.com", "E-posta alani");

        WebElement passwordInput = BaseStep.findElementXpathWithWait(
                "//*[@id=\"login-password\"]", TimeOut.SHORT.value);
        BaseStep.clearAndType(passwordInput, "Adem123!", "Sifre alani");

        WebElement loginBtn = BaseStep.findElementXpathWithWait(
                "//*[@id=\"root\"]/div/div[2]/div/div[2]/form/button",
                TimeOut.SHORT.value);
        BaseStep.clickElement(loginBtn, "Giris Yap butonu");
        BaseStep.waitSeconds(3);
        LogTest.info("Login tamamlandi.");
    }

    // ============================================================
    // TEST: 2 — CIKIS YAP (onay popup'i ile)
    // ============================================================

    @Test
    @Order(2)
    @DisplayName("Cikis Yap - onay popup ile login sayfasina don")
    public void logoutTest() {
        LogTest.info("Sidebar Cikis Yap butonuna tiklaniyor.");
        WebElement logoutBtn = BaseStep.findElementXpathWithWait(
                XPATH_SIDEBAR_LOGOUT_BTN, TimeOut.MEDIUM.value);
        BaseStep.clickElement(logoutBtn, "Cikis Yap sidebar butonu");
        BaseStep.waitSeconds(2);

        LogTest.info("Onay popup'inda Cikis Yap butonuna tiklaniyor.");
        WebElement confirmBtn = BaseStep.findElementXpathWithWait(
                XPATH_LOGOUT_CONFIRM_BTN, TimeOut.MEDIUM.value);
        BaseStep.clickElement(confirmBtn, "Cikis Yap onay butonu");
        BaseStep.waitSeconds(3);

        LogTest.info("Login sayfasina donuldugu dogrulaniyor.");
        WebElement loginEmail = BaseStep.findElementXpathWithWait(
                "//*[@id='login-email']", TimeOut.MEDIUM.value);
        assertTrue(loginEmail.isDisplayed(), "Login sayfasina geri donulemedi!");
        LogTest.info("Cikis basarili, login sayfasina donuldu.");
    }
}
