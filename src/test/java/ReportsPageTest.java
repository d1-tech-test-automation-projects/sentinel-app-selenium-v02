import org.junit.jupiter.api.*;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ReportsPageTest extends BaseStep {

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

    private static final String XPATH_SIDEBAR_REPORTS = "//*[@id=\"root\"]/div/aside/div[2]/div[1]/nav/button[7]";

    // Tab triggerlari aria-label tabanli (radix-:rj: dinamik ID'den bagimsiz)
    private static final String XPATH_TAB_PROJECT       = "//button[@role='tab' and @aria-label='Proje Raporu']";
    private static final String XPATH_TAB_TEAM          = "//button[@role='tab' and @aria-label='Takım Raporu']";
    private static final String XPATH_TAB_USER          = "//button[@role='tab' and @aria-label='Kullanıcı Raporu']";
    private static final String XPATH_TAB_TRENDS        = "//button[@role='tab' and @aria-label='Trendler']";
    private static final String XPATH_TAB_TRACEABILITY  = "//button[@role='tab' and @aria-label='İzlenebilirlik']";
    private static final String XPATH_TAB_DEPLOYMENTS   = "//button[@role='tab' and @aria-label='Dağıtım Geçmişi']";

    // Aktif tab panel'i icindeki ilk combobox (her sekmedeki ana dropdown icin)
    private static final String XPATH_ACTIVE_PANEL_DROPDOWN = "//div[@role='tabpanel' and @data-state='active']//button[@role='combobox']";

    // Trendler sekmesindeki 3 filtre + arama
    private static final String XPATH_TRENDS_FILTER_1   = "(//div[@role='tabpanel' and @data-state='active']//button[@role='combobox'])[1]";
    private static final String XPATH_TRENDS_FILTER_2   = "(//div[@role='tabpanel' and @data-state='active']//button[@role='combobox'])[2]";
    private static final String XPATH_TRENDS_FILTER_3   = "(//div[@role='tabpanel' and @data-state='active']//button[@role='combobox'])[3]";
    private static final String XPATH_TRENDS_SEARCH     = "//div[@role='tabpanel' and @data-state='active']//input[@placeholder='Test senaryosu ara']";

    // Enabled olan ilk option (disabled grup basliklarini atla)
    private static final String XPATH_FIRST_ENABLED_OPTION =
            "(//*[@role='option' and not(@aria-disabled='true') and not(@data-disabled)])[1]";

    // ============================================================
    // RADIX SELECT HELPER
    // ============================================================

    /**
     * Radix Select pointer event'ler ile calisir. element.click() listbox'i
     * kapatir ama gercek secimi tetiklemez. Bu helper pointer event dizisini
     * dispatch eder. StaleElementReferenceException secimin yapildigi
     * anlamina gelir (option DOM'dan kalkar), basari olarak kabul edilir.
     */
    private void radixClickOption(WebElement option) {
        BaseStep.waitSeconds(1);

        // 1) Actions ile atomik move+click
        try {
            new Actions(driver).moveToElement(option).click().perform();
            return;
        } catch (StaleElementReferenceException stale) {
            return;
        } catch (Exception ignored) {}

        // 2) JS pointer event dizisi (Radix bunlari dinler)
        try {
            ((JavascriptExecutor) driver).executeScript(
                    "var el = arguments[0];" +
                    "var r = el.getBoundingClientRect();" +
                    "var x = r.left + r.width/2, y = r.top + r.height/2;" +
                    "['pointermove','pointerdown','mousedown','pointerup','mouseup','click']" +
                    ".forEach(function(t){" +
                    "  el.dispatchEvent(new PointerEvent(t,{" +
                    "    bubbles:true,cancelable:true,view:window," +
                    "    clientX:x,clientY:y,pointerType:'mouse'," +
                    "    isPrimary:true,button:0,buttons:1}));" +
                    "});",
                    option);
            return;
        } catch (StaleElementReferenceException stale) {
            return;
        } catch (Exception ignored) {}

        // 3) Son care: plain JS click
        try {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", option);
        } catch (Exception ignored) {}
    }

    /**
     * Radix Select dropdown'u acar ve klavye navigasyonu ile ilk secilebilir
     * option'i secer. Radix Select native olarak ARROW_DOWN/ENTER destekler
     * ve disabled grup basliklarini otomatik atlar — bu yuzden klavye yolu
     * mouse click'ten daha guvenilir (mouse click bazen listbox'i kapatip
     * secimi tetiklemiyor).
     */
    private void openDropdownAndSelectFirst(String dropdownXpath, String description) {
        WebElement dropdown = BaseStep.findElementXpathWithWait(dropdownXpath, TimeOut.SHORT.value);
        BaseStep.clickElement(dropdown, description + " (dropdown ac)");
        BaseStep.waitSeconds(2);

        // ARROW_DOWN: Radix Select ilk secilebilir option'a focus eder
        new Actions(driver).sendKeys(Keys.ARROW_DOWN).perform();
        BaseStep.waitSeconds(1);

        // ENTER: focuslu option'i secer
        new Actions(driver).sendKeys(Keys.ENTER).perform();
        BaseStep.waitSeconds(2);

        LogTest.info(description + " - ilk secenek klavye ile secildi");
    }

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
    // TEST: 2 — RAPORLAR SAYFASINA GIT
    // ============================================================

    @Test
    @Order(2)
    @DisplayName("Raporlar sayfasina git")
    public void navigateToReportsPage() {
        LogTest.info("Sidebar Raporlar butonu araniyor.");
        WebElement reportsSidebarBtn = BaseStep.findElementXpathWithWait(
                XPATH_SIDEBAR_REPORTS, TimeOut.SHORT.value);
        assertTrue(reportsSidebarBtn.isDisplayed(), "Raporlar sidebar butonu gorulmuyor!");
        BaseStep.clickElement(reportsSidebarBtn, "Raporlar sidebar butonu");
        BaseStep.waitSeconds(2);

        LogTest.info("Sayfanin yuklendigi dogrulaniyor.");
        WebElement page = BaseStep.findElementXpathWithWait("//main", TimeOut.SHORT.value);
        assertTrue(page.isDisplayed(), "Raporlar sayfasi yuklenemedi!");
        LogTest.info("Raporlar sayfasi acildi.");
    }

    // ============================================================
    // TEST: 3 — PROJE RAPORU SEKMESI (default acik)
    // ============================================================

    @Test
    @Order(3)
    @DisplayName("Proje Raporu sekmesinde proje sec")
    public void projectReportTest() {
        LogTest.info("Proje Raporu sekmesi aktif, proje seciliyor.");
        openDropdownAndSelectFirst(XPATH_ACTIVE_PANEL_DROPDOWN, "Proje sec");
        LogTest.info("Proje raporu uygulandi.");
    }

    // ============================================================
    // TEST: 4 — TAKIM RAPORU SEKMESI
    // ============================================================

    @Test
    @Order(4)
    @DisplayName("Takim Raporu sekmesine gec ve takim sec")
    public void teamReportTest() {
        LogTest.info("Takim Raporu sekmesine tiklaniyor.");
        WebElement teamTab = BaseStep.findElementXpathWithWait(
                XPATH_TAB_TEAM, TimeOut.MEDIUM.value);
        BaseStep.clickElement(teamTab, "Takim Raporu sekmesi");
        BaseStep.waitSeconds(2);

        LogTest.info("Takim seciliyor.");
        openDropdownAndSelectFirst(XPATH_ACTIVE_PANEL_DROPDOWN, "Takim sec");
        LogTest.info("Takim raporu uygulandi.");
    }

    // ============================================================
    // TEST: 5 — KULLANICI RAPORU SEKMESI
    // ============================================================

    @Test
    @Order(5)
    @DisplayName("Kullanici Raporu sekmesine gec ve kullanici sec")
    public void userReportTest() {
        LogTest.info("Kullanici Raporu sekmesine tiklaniyor.");
        WebElement userTab = BaseStep.findElementXpathWithWait(
                XPATH_TAB_USER, TimeOut.MEDIUM.value);
        BaseStep.clickElement(userTab, "Kullanici Raporu sekmesi");
        BaseStep.waitSeconds(2);

        LogTest.info("Kullanici seciliyor.");
        openDropdownAndSelectFirst(XPATH_ACTIVE_PANEL_DROPDOWN, "Kullanici sec");
        LogTest.info("Kullanici raporu uygulandi.");
    }

    // ============================================================
    // TEST: 6 — TRENDLER SEKMESI (3 filtre + arama)
    // ============================================================

    @Test
    @Order(6)
    @DisplayName("Trendler sekmesi - filtreler ve test senaryosu arama")
    public void trendsReportTest() {
        LogTest.info("Trendler sekmesine tiklaniyor.");
        WebElement trendsTab = BaseStep.findElementXpathWithWait(
                XPATH_TAB_TRENDS, TimeOut.MEDIUM.value);
        BaseStep.clickElement(trendsTab, "Trendler sekmesi");
        BaseStep.waitSeconds(2);

        // --- 1. filtre (periyot - Haftalik default) ---
        LogTest.info("1. filtre seciliyor.");
        openDropdownAndSelectFirst(XPATH_TRENDS_FILTER_1, "1. filtre (periyot)");

        // --- 2. filtre (durum - Basarili default) ---
        LogTest.info("2. filtre seciliyor.");
        openDropdownAndSelectFirst(XPATH_TRENDS_FILTER_2, "2. filtre (durum)");

        // --- 3. filtre (kullanici) ---
        LogTest.info("3. filtre seciliyor.");
        openDropdownAndSelectFirst(XPATH_TRENDS_FILTER_3, "3. filtre (kullanici)");

        // --- Arama input ---
        LogTest.info("Test senaryosu arama yapiliyor.");
        WebElement searchInput = BaseStep.findElementXpathWithWait(
                XPATH_TRENDS_SEARCH, TimeOut.SHORT.value);
        BaseStep.clearAndType(searchInput, "Test", "Test senaryosu ara");
        BaseStep.waitSeconds(2);

        LogTest.info("Arama temizleniyor.");
        BaseStep.clearAndType(searchInput, "", "Test senaryosu ara");
        BaseStep.waitSeconds(1);
        LogTest.info("Trendler sekmesi etkilesimleri tamamlandi.");
    }

    // ============================================================
    // TEST: 7 — IZLENEBILIRLIK SEKMESI
    // ============================================================

    @Test
    @Order(7)
    @DisplayName("Izlenebilirlik sekmesine gec ve gereksinim sec")
    public void traceabilityReportTest() {
        LogTest.info("Izlenebilirlik sekmesine tiklaniyor.");
        WebElement traceabilityTab = BaseStep.findElementXpathWithWait(
                XPATH_TAB_TRACEABILITY, TimeOut.MEDIUM.value);
        BaseStep.clickElement(traceabilityTab, "Izlenebilirlik sekmesi");
        BaseStep.waitSeconds(2);

        LogTest.info("Gereksinim seciliyor.");
        openDropdownAndSelectFirst(XPATH_ACTIVE_PANEL_DROPDOWN, "Gereksinim sec");
        LogTest.info("Izlenebilirlik raporu uygulandi.");
    }

    // ============================================================
    // TEST: 8 — DAGITIM GECMISI SEKMESI
    // ============================================================

    @Test
    @Order(8)
    @DisplayName("Dagitim Gecmisi sekmesine gec")
    public void deploymentsHistoryTabTest() {
        LogTest.info("Dagitim Gecmisi sekmesine tiklaniyor.");
        WebElement deploymentsTab = BaseStep.findElementXpathWithWait(
                XPATH_TAB_DEPLOYMENTS, TimeOut.MEDIUM.value);
        BaseStep.clickElement(deploymentsTab, "Dagitim Gecmisi sekmesi");
        BaseStep.waitSeconds(2);
        LogTest.info("Dagitim Gecmisi sekmesi acildi.");
    }
}
