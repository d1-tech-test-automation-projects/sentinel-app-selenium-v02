import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SprintPageTest extends BaseStep {

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

    private static final String XPATH_SIDEBAR_SPRINTS    = "//*[@id=\"root\"]/div/aside/div[2]/div[1]/nav/button[5]";
    private static final String XPATH_ADD_SPRINT_BTN     = "//main//button[normalize-space(.)='Sprint Ekle']";

    // Popup form alanlari (kararli id'ler)
    private static final String XPATH_PROJECT_SELECT     = "//*[@id='sprint-project']";
    private static final String XPATH_SPRINT_NAME        = "//*[@id='sprint-name']";
    private static final String XPATH_SPRINT_GOAL        = "//*[@id='sprint-goal']";
    private static final String XPATH_SPRINT_DESC        = "//*[@id='sprint-description']";
    private static final String XPATH_START_DATE         = "//div[@role='dialog']//input[@placeholder='Başlangıç Tarihi']";
    private static final String XPATH_END_DATE           = "//div[@role='dialog']//input[@placeholder='Bitiş Tarihi']";
    private static final String XPATH_SAVE_BTN           = "//div[@role='dialog']//button[@type='submit' and normalize-space(.)='Kaydet']";

    // Liste islem butonlari (her zaman ilk satir)
    private static final String XPATH_ROW_DETAILS_BTN    = "(//button[@aria-label='Detayları Gör'])[1]";
    private static final String XPATH_ROW_EDIT_BTN       = "(//button[@aria-label='Düzenle'])[1]";
    private static final String XPATH_ROW_DELETE_BTN     = "(//button[@aria-label='Sil'])[1]";
    private static final String XPATH_DETAILS_CLOSE_BTN  = "//div[@role='dialog']//button[normalize-space(.)='Kapat']";

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
    // TEST: 2 — SPRINT YONETIMI SAYFASINA GIT
    // ============================================================

    @Test
    @Order(2)
    @DisplayName("Sprint Yonetimi sayfasina git")
    public void navigateToSprintsPage() {
        LogTest.info("Sidebar Sprint Yonetimi butonu araniyor.");
        WebElement sprintsSidebarBtn = BaseStep.findElementXpathWithWait(
                XPATH_SIDEBAR_SPRINTS, TimeOut.SHORT.value);
        assertTrue(sprintsSidebarBtn.isDisplayed(), "Sprint Yonetimi sidebar butonu gorulmuyor!");
        BaseStep.clickElement(sprintsSidebarBtn, "Sprint Yonetimi sidebar butonu");
        BaseStep.waitSeconds(2);

        LogTest.info("Sayfanin yuklendigi dogrulaniyor.");
        WebElement page = BaseStep.findElementXpathWithWait("//main", TimeOut.SHORT.value);
        assertTrue(page.isDisplayed(), "Sprint Yonetimi sayfasi yuklenemedi!");
        LogTest.info("Sprint Yonetimi sayfasi acildi.");
    }

    // ============================================================
    // TEST: 3 — YENI SPRINT EKLE (TAM SUREC)
    // ============================================================

    @Test
    @Order(3)
    @DisplayName("Yeni sprint ekle - tam surec")
    public void addSprintTest() {
        LogTest.info("Sprint Ekle butonuna tiklaniyor.");
        WebElement addBtn = BaseStep.findElementXpathWithWait(
                XPATH_ADD_SPRINT_BTN, TimeOut.MEDIUM.value);
        BaseStep.clickElement(addBtn, "Sprint Ekle butonu");
        BaseStep.waitSeconds(2);

        LogTest.info("Proje seciliyor.");
        WebElement projectDropdown = BaseStep.findElementXpathWithWait(
                XPATH_PROJECT_SELECT, TimeOut.SHORT.value);
        BaseStep.clickElement(projectDropdown, "Proje dropdown");
        BaseStep.waitSeconds(1);
        WebElement projectOption = BaseStep.findElementXpathWithWait(
                "(//*[@role='option'])[1]", TimeOut.SHORT.value);
        BaseStep.clickElement(projectOption, "Proje secildi");
        BaseStep.waitSeconds(2);

        LogTest.info("Sprint adi giriliyor.");
        WebElement nameInput = BaseStep.findElementXpathWithWait(
                XPATH_SPRINT_NAME, TimeOut.SHORT.value);
        BaseStep.clearAndType(nameInput, "Otomasyon Sprint", "Sprint adi");
        BaseStep.waitSeconds(1);

        LogTest.info("Sprint hedefi giriliyor.");
        WebElement goalInput = BaseStep.findElementXpathWithWait(
                XPATH_SPRINT_GOAL, TimeOut.SHORT.value);
        BaseStep.clearAndType(goalInput, "Otomasyon sprint hedefi.", "Sprint hedefi");
        BaseStep.waitSeconds(1);

        LogTest.info("Sprint aciklamasi giriliyor.");
        WebElement descInput = BaseStep.findElementXpathWithWait(
                XPATH_SPRINT_DESC, TimeOut.SHORT.value);
        BaseStep.clearAndType(descInput, "Otomasyon ile olusturulan sprint aciklamasi.", "Sprint aciklamasi");
        BaseStep.waitSeconds(1);

        LogTest.info("Baslangic tarihi giriliyor.");
        WebElement startDateInput = BaseStep.findElementXpathWithWait(
                XPATH_START_DATE, TimeOut.SHORT.value);
        setDateInputValue(startDateInput, "12.05.2026", "Baslangic Tarihi");
        BaseStep.waitSeconds(1);

        LogTest.info("Bitis tarihi giriliyor.");
        WebElement endDateInput = BaseStep.findElementXpathWithWait(
                XPATH_END_DATE, TimeOut.SHORT.value);
        setDateInputValue(endDateInput, "20.05.2026", "Bitis Tarihi");
        BaseStep.waitSeconds(1);

        LogTest.info("Kaydet butonuna tiklaniyor.");
        WebElement saveBtn = BaseStep.findElementXpathWithWait(
                XPATH_SAVE_BTN, TimeOut.SHORT.value);
        BaseStep.clickElement(saveBtn, "Kaydet butonu");
        BaseStep.waitSeconds(3);
        LogTest.info("Sprint basariyla olusturuldu.");
    }

    // ============================================================
    // TEST: 4 — SPRINT DETAYLARINI GORUNTULE
    // ============================================================

    @Test
    @Order(4)
    @DisplayName("Sprint detaylarini goruntule ve kapat")
    public void viewSprintDetailsTest() {
        LogTest.info("Listedeki Detaylari Gor butonuna tiklaniyor.");
        WebElement detailsBtn = BaseStep.findElementXpathWithWait(
                XPATH_ROW_DETAILS_BTN, TimeOut.MEDIUM.value);
        BaseStep.clickElement(detailsBtn, "Detaylari Gor butonu");
        BaseStep.waitSeconds(2);

        LogTest.info("Detay popup'inin acildigi dogrulaniyor.");
        WebElement closeBtn = BaseStep.findElementXpathWithWait(
                XPATH_DETAILS_CLOSE_BTN, TimeOut.MEDIUM.value);
        assertTrue(closeBtn.isDisplayed(), "Detay popup'i acilmadi!");

        LogTest.info("Kapat butonuna tiklaniyor.");
        BaseStep.clickElement(closeBtn, "Kapat butonu");
        BaseStep.waitSeconds(2);
        LogTest.info("Detay popup'i kapatildi.");
    }

    // ============================================================
    // TEST: 5 — SPRINT DUZENLEME
    // ============================================================

    @Test
    @Order(5)
    @DisplayName("Sprint duzenleme - alanlari guncelle")
    public void editSprintTest() {
        LogTest.info("Listedeki Duzenle butonuna tiklaniyor.");
        WebElement editBtn = BaseStep.findElementXpathWithWait(
                XPATH_ROW_EDIT_BTN, TimeOut.MEDIUM.value);
        BaseStep.clickElement(editBtn, "Duzenle butonu");
        BaseStep.waitSeconds(2);

        LogTest.info("Sprint adi guncelleniyor.");
        WebElement nameInput = BaseStep.findElementXpathWithWait(
                XPATH_SPRINT_NAME, TimeOut.SHORT.value);
        BaseStep.clearAndType(nameInput, "Guncellenmis Sprint", "Sprint adi (duzenleme)");
        BaseStep.waitSeconds(1);

        LogTest.info("Sprint hedefi guncelleniyor.");
        WebElement goalInput = BaseStep.findElementXpathWithWait(
                XPATH_SPRINT_GOAL, TimeOut.SHORT.value);
        BaseStep.clearAndType(goalInput, "Guncellenmis sprint hedefi.", "Sprint hedefi (duzenleme)");
        BaseStep.waitSeconds(1);

        LogTest.info("Sprint aciklamasi guncelleniyor.");
        WebElement descInput = BaseStep.findElementXpathWithWait(
                XPATH_SPRINT_DESC, TimeOut.SHORT.value);
        BaseStep.clearAndType(descInput, "Guncellenmis sprint aciklamasi.", "Sprint aciklamasi (duzenleme)");
        BaseStep.waitSeconds(1);

        LogTest.info("Bitis tarihi guncelleniyor.");
        WebElement endDateInput = BaseStep.findElementXpathWithWait(
                XPATH_END_DATE, TimeOut.SHORT.value);
        setDateInputValue(endDateInput, "30.06.2026", "Bitis Tarihi (duzenleme)");
        BaseStep.waitSeconds(1);

        LogTest.info("Kaydet butonuna tiklaniyor.");
        WebElement saveBtn = BaseStep.findElementXpathWithWait(
                XPATH_SAVE_BTN, TimeOut.SHORT.value);
        BaseStep.clickElement(saveBtn, "Kaydet butonu");
        BaseStep.waitSeconds(3);
        LogTest.info("Sprint basariyla guncellendi.");
    }

    // ============================================================
    // TEST: 6 — SPRINT SILME
    // ============================================================

    @Test
    @Order(6)
    @DisplayName("Sprint silme - onay popup'i ile")
    public void deleteSprintTest() {
        dismissAnyOpenDialog();
        BaseStep.waitSeconds(1);

        LogTest.info("Listedeki Sil butonuna tiklaniyor.");
        WebElement deleteBtn = BaseStep.findElementXpathWithWait(
                XPATH_ROW_DELETE_BTN, TimeOut.MEDIUM.value);
        BaseStep.jsClickElement(deleteBtn, "Sil butonu");
        BaseStep.waitSeconds(2);

        LogTest.info("Onay popup'inda 'Evet' butonuna tiklaniyor.");
        WebElement confirmBtn = BaseStep.findElementXpathWithWait(
                "(//*[@role='alertdialog' or @role='dialog'])[last()]" +
                "//button[normalize-space(.)='Evet']",
                TimeOut.SHORT.value);
        BaseStep.jsClickElement(confirmBtn, "Evet onay butonu");
        BaseStep.waitSeconds(3);
        LogTest.info("Sprint basariyla silindi.");
    }

    // ============================================================
    // HELPER METHODS
    // ============================================================

    /**
     * React kontrollu tarih input'una deger set eder.
     * sendKeys/clear React date picker'i tetikledigi icin native value
     * setter uzerinden deger yazar ve input/change/blur event'lerini
     * dispatch eder. (ProjectPageTest ile ayni pattern.)
     */
    private void setDateInputValue(WebElement dateInput, String value, String description) {
        LogTest.actionInfo("Set date value: " + value, description);
        ((JavascriptExecutor) BaseStep.driver).executeScript(
                "var input = arguments[0];" +
                "var val = arguments[1];" +
                "var setter = Object.getOwnPropertyDescriptor(window.HTMLInputElement.prototype, 'value').set;" +
                "setter.call(input, val);" +
                "input.dispatchEvent(new Event('input', { bubbles: true }));" +
                "input.dispatchEvent(new Event('change', { bubbles: true }));" +
                "input.dispatchEvent(new Event('blur', { bubbles: true }));",
                dateInput, value);
        LogTest.stepInfo("Date value set: " + value);
    }

    /**
     * ESC ile acik dialoglari kapatir. Onceki testten kalan dialog
     * overlay'i sonraki tiklamalari engelliyorsa kullanilir.
     */
    private void dismissAnyOpenDialog() {
        try {
            new org.openqa.selenium.interactions.Actions(driver)
                    .sendKeys(Keys.ESCAPE).perform();
            BaseStep.waitSeconds(1);
            if (BaseStep.isElementPresent(By.xpath("//*[@role='dialog' and @data-state='open']"))) {
                new org.openqa.selenium.interactions.Actions(driver)
                        .sendKeys(Keys.ESCAPE).perform();
                BaseStep.waitSeconds(1);
            }
            LogTest.stepInfo("ESC ile acik dialoglar kapatildi.");
        } catch (Exception ignored) {}
    }
}
