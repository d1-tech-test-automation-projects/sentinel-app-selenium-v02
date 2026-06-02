import org.junit.jupiter.api.*;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DeploymentsPageTest extends BaseStep {

    // ============================================================
    // SETUP / TEARDOWN
    // ============================================================

    @BeforeAll
    public void setup() {
        BaseStep.openChromeDriver();
    }

    @AfterAll
    public void teardown() {
        BaseStep.driverQuit();
    }

    // ============================================================
    // STABLE LOCATORS
    // ============================================================

    private static final String XPATH_SIDEBAR_DEPLOYMENTS  = "//*[@id=\"root\"]/div/aside/div[2]/div[1]/nav/button[6]";
    private static final String XPATH_PROJECT_FILTER       = "//select[@data-testid='deployments-project-filter']";
    private static final String XPATH_ADD_DEPLOYMENT_BTN   = "//button[@data-testid='deployment-create-button']";

    // Yeni dagitim popup'i (data-testid ve id tabanli - dinamik radix ID yerine)
    private static final String XPATH_POPUP_PROJECT_SELECT = "//button[@data-testid='deployment-project-select']";
    private static final String XPATH_POPUP_VERSION_INPUT  = "//*[@id='deployment-version']";
    private static final String XPATH_POPUP_ENV_SELECT     = "//button[@data-testid='deployment-environment-select']";
    private static final String XPATH_POPUP_DESC_INPUT     = "//*[@id='deployment-description']";
    private static final String XPATH_POPUP_NOTES_INPUT    = "//*[@id='deployment-notes']";
    private static final String XPATH_POPUP_SUBMIT_BTN     = "//div[@role='dialog']//form//button[@type='submit' and normalize-space(.)='Oluştur']";

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
    // TEST: 2 — DAGITIMLAR SAYFASINA GIT
    // ============================================================

    @Test
    @Order(2)
    @DisplayName("Dagitimlar sayfasina git")
    public void navigateToDeploymentsPage() {
        LogTest.info("Sidebar Dagitimlar butonu araniyor.");
        WebElement deploymentsSidebarBtn = BaseStep.findElementXpathWithWait(
                XPATH_SIDEBAR_DEPLOYMENTS, TimeOut.SHORT.value);
        assertTrue(deploymentsSidebarBtn.isDisplayed(), "Dagitimlar sidebar butonu gorulmuyor!");
        BaseStep.clickElement(deploymentsSidebarBtn, "Dagitimlar sidebar butonu");
        BaseStep.waitSeconds(2);

        LogTest.info("Sayfanin yuklendigi dogrulaniyor.");
        WebElement page = BaseStep.findElementXpathWithWait("//main", TimeOut.SHORT.value);
        assertTrue(page.isDisplayed(), "Dagitimlar sayfasi yuklenemedi!");
        LogTest.info("Dagitimlar sayfasi acildi.");
    }

    // ============================================================
    // TEST: 3 — PROJEYE GORE FILTRELE (NATIVE SELECT)
    // ============================================================

    @Test
    @Order(3)
    @DisplayName("Projeye gore filtrele - native select dropdown")
    public void filterDeploymentsTest() {
        LogTest.info("Proje filtresi dropdown araniyor.");
        WebElement filterDropdown = BaseStep.findElementXpathWithWait(
                XPATH_PROJECT_FILTER, TimeOut.SHORT.value);

        LogTest.info("Bir proje seciliyor: Hukuk_Urunleri_Test_Otomasyon");
        BaseStep.selectElementFromDropdown(
                filterDropdown, "Hukuk_Ürünleri_Test_Otomasyon", "Proje filtresi");
        BaseStep.waitSeconds(2);
        LogTest.info("Proje filtresi uygulandi.");

        LogTest.info("Filtre Tumu'ne sifirlaniyor.");
        filterDropdown = BaseStep.findElementXpathWithWait(
                XPATH_PROJECT_FILTER, TimeOut.SHORT.value);
        BaseStep.selectElementFromDropdown(
                filterDropdown, "Tümü", "Proje filtresi");
        BaseStep.waitSeconds(2);
        LogTest.info("Filtre testi tamamlandi.");
    }

    // ============================================================
    // TEST: 4 — YENI DAGITIM EKLE (TAM SUREC)
    // ============================================================

    @Test
    @Order(4)
    @DisplayName("Yeni dagitim ekle - tam surec")
    public void addDeploymentTest() {
        LogTest.info("Yeni Dagitim butonuna tiklaniyor.");
        WebElement addBtn = BaseStep.findElementXpathWithWait(
                XPATH_ADD_DEPLOYMENT_BTN, TimeOut.MEDIUM.value);
        BaseStep.clickElement(addBtn, "Yeni Dagitim butonu");
        BaseStep.waitSeconds(2);

        LogTest.info("Proje seciliyor.");
        WebElement projectDropdown = BaseStep.findElementXpathWithWait(
                XPATH_POPUP_PROJECT_SELECT, TimeOut.SHORT.value);
        BaseStep.clickElement(projectDropdown, "Proje dropdown");
        BaseStep.waitSeconds(1);
        // Ilk option grup basligi olabilir (aria-disabled='true'); enabled olan ilk option'i sec
        WebElement projectOption = BaseStep.findElementXpathWithWait(
                "(//*[@role='option' and not(@aria-disabled='true') and not(@data-disabled)])[1]",
                TimeOut.SHORT.value);
        BaseStep.clickElement(projectOption, "Proje secildi");
        BaseStep.waitSeconds(2);

        LogTest.info("Versiyon giriliyor.");
        WebElement versionInput = BaseStep.findElementXpathWithWait(
                XPATH_POPUP_VERSION_INPUT, TimeOut.SHORT.value);
        BaseStep.clearAndType(versionInput, "1.0.0", "Versiyon alani");
        BaseStep.waitSeconds(1);

        LogTest.info("Ortam seciliyor.");
        WebElement envDropdown = BaseStep.findElementXpathWithWait(
                XPATH_POPUP_ENV_SELECT, TimeOut.SHORT.value);
        BaseStep.clickElement(envDropdown, "Ortam dropdown");
        BaseStep.waitSeconds(1);
        // Enabled olan ilk option'i sec (grup basligi/placeholder'i atla)
        WebElement envOption = BaseStep.findElementXpathWithWait(
                "(//*[@role='option' and not(@aria-disabled='true') and not(@data-disabled)])[1]",
                TimeOut.SHORT.value);
        BaseStep.clickElement(envOption, "Ortam secildi");
        BaseStep.waitSeconds(2);

        LogTest.info("Aciklama giriliyor.");
        WebElement descInput = BaseStep.findElementXpathWithWait(
                XPATH_POPUP_DESC_INPUT, TimeOut.SHORT.value);
        BaseStep.clearAndType(descInput, "Otomasyon ile olusturulan dagitim aciklamasi.",
                "Aciklama alani");
        BaseStep.waitSeconds(1);

        LogTest.info("Ek notlar giriliyor.");
        WebElement notesInput = BaseStep.findElementXpathWithWait(
                XPATH_POPUP_NOTES_INPUT, TimeOut.SHORT.value);
        BaseStep.clearAndType(notesInput, "Otomasyon ile eklenen ek notlar.",
                "Ek notlar alani");
        BaseStep.waitSeconds(1);

        LogTest.info("Olustur butonuna tiklaniyor.");
        WebElement submitBtn = BaseStep.findElementXpathWithWait(
                XPATH_POPUP_SUBMIT_BTN, TimeOut.SHORT.value);
        BaseStep.clickElement(submitBtn, "Olustur butonu");
        BaseStep.waitSeconds(3);
        LogTest.info("Dagitim basariyla olusturuldu.");
    }
}
