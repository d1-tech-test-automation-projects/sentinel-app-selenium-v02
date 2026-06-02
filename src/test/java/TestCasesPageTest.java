import org.junit.jupiter.api.*;
import org.openqa.selenium.WebElement;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestCasesPageTest extends BaseStep {

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
    // STABLE LOCATORS (data-testid tabanli)
    // ============================================================

    private static final String XPATH_SIDEBAR_TEST_CASES   = "//*[@id=\"root\"]/div/aside/div[2]/div[1]/nav/button[3]";
    private static final String XPATH_MODULE_FILTER        = "//button[@data-testid='test-cases-module-filter']";
    private static final String XPATH_TYPE_FILTER          = "//button[@data-testid='test-cases-type-filter']";
    private static final String XPATH_PRIORITY_FILTER      = "//button[@data-testid='test-cases-priority-filter']";
    private static final String XPATH_ADD_TEST_CASE_BTN    = "//button[@data-testid='test-cases-add-button']";

    private static final String XPATH_TITLE_INPUT          = "//input[@data-testid='test-case-title-input']";
    private static final String XPATH_PROJECT_SELECT       = "//button[@data-testid='test-case-project-select']";
    private static final String XPATH_MODULE_SELECT        = "//button[@data-testid='test-case-module-select']";
    private static final String XPATH_DESCRIPTION_INPUT    = "//textarea[@data-testid='test-case-description-input']";
    private static final String XPATH_EXPECTED_RESULT      = "//textarea[@data-testid='test-case-expected-result-input']";
    private static final String XPATH_TYPE_SELECT          = "//button[@data-testid='test-case-type-select']";
    private static final String XPATH_PRIORITY_SELECT      = "//button[@data-testid='test-case-priority-select']";
    private static final String XPATH_SUBMIT_BTN           = "//button[@data-testid='test-case-submit-button']";

    // Detay sayfasi locator'lari (id ve data-testid tabanli - kararli)
    private static final String XPATH_ROW_DETAILS_BTN      = "(//button[@aria-label='Detayları Gör'])[1]";
    private static final String XPATH_EDIT_BTN             = "//main//button[normalize-space(.)='Düzenle']";
    private static final String XPATH_SAVE_BTN             = "//main//button[normalize-space(.)='Kaydet']";
    private static final String XPATH_EDIT_TITLE           = "//*[@id='test-case-title']";
    private static final String XPATH_EDIT_TYPE            = "//*[@id='test-case-type']";
    private static final String XPATH_EDIT_PRIORITY        = "//*[@id='test-case-priority']";
    private static final String XPATH_EDIT_DESCRIPTION     = "//*[@id='test-case-description']";
    private static final String XPATH_EDIT_EXPECTED        = "//*[@id='test-case-expected-result']";

    // Adimlar sekmesi
    private static final String XPATH_STEPS_ADD_BTN        = "//button[@data-testid='test-case-steps-add-button']";
    private static final String XPATH_STEP_DESC_0          = "//textarea[@data-testid='test-case-step-description-0']";
    private static final String XPATH_STEP_EXPECTED_0      = "//textarea[@data-testid='test-case-step-expected-0']";
    private static final String XPATH_STEP_DATA_0          = "//textarea[@data-testid='test-case-step-data-0']";
    private static final String XPATH_STEPS_SAVE_BTN       = "//button[@data-testid='test-case-steps-save-button']";

    // Sekmeler (role=tab + metin tabanli)
    private static final String XPATH_TAB_EXECUTIONS       = "//button[@role='tab' and contains(.,'Çalışma Geçmişi')]";
    private static final String XPATH_TAB_DEFECTS          = "//button[@role='tab' and contains(.,'Hatalar')]";
    private static final String XPATH_TAB_REQUIREMENTS     = "//button[@role='tab' and contains(.,'Gereksinimler')]";
    private static final String XPATH_TAB_WORKFLOW         = "//button[@role='tab' and contains(.,'İş Akışı')]";

    // Gereksinimler sekmesi
    private static final String XPATH_REQ_SELECT           = "//button[@data-testid='test-case-requirements-select']";
    private static final String XPATH_REQ_LINK_BTN         = "//button[@data-testid='test-case-requirements-link-button']";

    // Silme islemleri (dinamik UUID iceren data-testid yerine aria-label kullanildi)
    private static final String XPATH_ROW_DELETE_BTN       = "(//button[@aria-label='Sil'])[1]";
    private static final String XPATH_DELETE_CONFIRM_BTN   = "//button[@data-testid='test-case-delete-confirm']";

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
    // TEST: 2 — TEST SENARYOLARI SAYFASINA GIT
    // ============================================================

    @Test
    @Order(2)
    @DisplayName("Test Senaryolari sayfasina git")
    public void navigateToTestCasesPage() {
        LogTest.info("Sidebar Test Senaryolari butonu araniyor.");
        WebElement testCasesSidebarBtn = BaseStep.findElementXpathWithWait(
                XPATH_SIDEBAR_TEST_CASES, TimeOut.SHORT.value);
        assertTrue(testCasesSidebarBtn.isDisplayed(), "Test Senaryolari sidebar butonu gorulmuyor!");
        BaseStep.clickElement(testCasesSidebarBtn, "Test Senaryolari sidebar butonu");
        BaseStep.waitSeconds(2);

        LogTest.info("Sayfanin yuklendigi dogrulaniyor.");
        WebElement page = BaseStep.findElementXpathWithWait("//main", TimeOut.SHORT.value);
        assertTrue(page.isDisplayed(), "Test Senaryolari sayfasi yuklenemedi!");
        LogTest.info("Test Senaryolari sayfasi acildi.");
    }

    // ============================================================
    // TEST: 3 — MODUL FILTRESI
    // ============================================================

    @Test
    @Order(3)
    @DisplayName("Modul filtresi testi")
    public void moduleFilterTest() {
        LogTest.info("Modul dropdown aciliyor.");
        WebElement dropdown = BaseStep.findElementXpathWithWait(
                XPATH_MODULE_FILTER, TimeOut.SHORT.value);
        BaseStep.clickElement(dropdown, "Modul filtre dropdown");
        BaseStep.waitSeconds(1);

        LogTest.info("Bir modul seciliyor.");
        WebElement moduleOption = BaseStep.findElementXpathWithWait(
                "(//*[@role='option'])[2]", TimeOut.SHORT.value);
        BaseStep.clickElement(moduleOption, "Modul secildi");
        BaseStep.waitSeconds(2);

        LogTest.info("Modul filtresi sifirlaniyor: Tumu");
        dropdown = BaseStep.findElementXpathWithWait(XPATH_MODULE_FILTER, TimeOut.SHORT.value);
        BaseStep.clickElement(dropdown, "Modul filtre dropdown");
        BaseStep.waitSeconds(1);
        WebElement allOption = BaseStep.findElementXpathWithWait(
                "//*[@role='option']//span[contains(text(),'Tümü')]", TimeOut.SHORT.value);
        BaseStep.clickElement(allOption, "Tumu secildi");
        BaseStep.waitSeconds(2);
        LogTest.info("Modul filtre testi tamamlandi.");
    }

    // ============================================================
    // TEST: 4 — TUR FILTRESI
    // ============================================================

    @Test
    @Order(4)
    @DisplayName("Tur filtresi testi - Fonksiyonel, Regresyon")
    public void typeFilterTest() {
        String[] types = {"Fonksiyonel", "Regresyon"};

        for (String type : types) {
            LogTest.info("Tur dropdown aciliyor: " + type);
            WebElement dropdown = BaseStep.findElementXpathWithWait(
                    XPATH_TYPE_FILTER, TimeOut.SHORT.value);
            BaseStep.clickElement(dropdown, "Tur filtre dropdown");
            BaseStep.waitSeconds(1);

            WebElement option = BaseStep.findElementXpathWithWait(
                    "//*[@role='option']//span[contains(text(),'" + type + "')]",
                    TimeOut.SHORT.value);
            BaseStep.clickElement(option, type + " secildi");
            BaseStep.waitSeconds(2);
            LogTest.info(type + " filtresi uygulandi.");
        }

        LogTest.info("Tur filtresi sifirlaniyor.");
        WebElement dropdown = BaseStep.findElementXpathWithWait(
                XPATH_TYPE_FILTER, TimeOut.SHORT.value);
        BaseStep.clickElement(dropdown, "Tur filtre dropdown");
        BaseStep.waitSeconds(1);
        WebElement resetOption = BaseStep.findElementXpathWithWait(
                "//*[@role='option']//span[contains(text(),'Tüm')]", TimeOut.SHORT.value);
        BaseStep.clickElement(resetOption, "Tum Turler secildi");
        BaseStep.waitSeconds(2);
        LogTest.info("Tur filtresi testi tamamlandi.");
    }

    // ============================================================
    // TEST: 5 — ONCELIK FILTRESI
    // ============================================================

    @Test
    @Order(5)
    @DisplayName("Oncelik filtresi testi - Dusuk, Orta, Yuksek")
    public void priorityFilterTest() {
        String[] priorities = {"Düşük", "Orta", "Yüksek"};

        for (String priority : priorities) {
            LogTest.info("Oncelik dropdown aciliyor: " + priority);
            WebElement dropdown = BaseStep.findElementXpathWithWait(
                    XPATH_PRIORITY_FILTER, TimeOut.SHORT.value);
            BaseStep.clickElement(dropdown, "Oncelik filtre dropdown");
            BaseStep.waitSeconds(1);

            WebElement option = BaseStep.findElementXpathWithWait(
                    "//*[@role='option']//span[contains(text(),'" + priority + "')]",
                    TimeOut.SHORT.value);
            BaseStep.clickElement(option, priority + " secildi");
            BaseStep.waitSeconds(2);
            LogTest.info(priority + " filtresi uygulandi.");
        }

        LogTest.info("Oncelik filtresi sifirlaniyor.");
        WebElement dropdown = BaseStep.findElementXpathWithWait(
                XPATH_PRIORITY_FILTER, TimeOut.SHORT.value);
        BaseStep.clickElement(dropdown, "Oncelik filtre dropdown");
        BaseStep.waitSeconds(1);
        WebElement resetOption = BaseStep.findElementXpathWithWait(
                "//*[@role='option']//span[contains(text(),'Tüm')]", TimeOut.SHORT.value);
        BaseStep.clickElement(resetOption, "Tum Oncelikler secildi");
        BaseStep.waitSeconds(2);
        LogTest.info("Oncelik filtresi testi tamamlandi.");
    }

    // ============================================================
    // TEST: 6 — TEST SENARYOSU EKLE (TAM SUREC)
    // ============================================================

    @Test
    @Order(6)
    @DisplayName("Test Senaryosu ekle - tam surec")
    public void addTestCaseTest() {
        LogTest.info("Test Senaryosu Ekle butonuna tiklaniyor.");
        WebElement addBtn = BaseStep.findElementXpathWithWait(
                XPATH_ADD_TEST_CASE_BTN, TimeOut.MEDIUM.value);
        BaseStep.clickElement(addBtn, "Test Senaryosu Ekle butonu");
        BaseStep.waitSeconds(2);

        LogTest.info("Test senaryosu basligi giriliyor.");
        WebElement titleInput = BaseStep.findElementXpathWithWait(
                XPATH_TITLE_INPUT, TimeOut.SHORT.value);
        BaseStep.clearAndType(titleInput, "Otomasyon Test Senaryosu", "Test senaryosu basligi");
        BaseStep.waitSeconds(1);

        LogTest.info("Proje seciliyor.");
        WebElement projectDropdown = BaseStep.findElementXpathWithWait(
                XPATH_PROJECT_SELECT, TimeOut.SHORT.value);
        BaseStep.clickElement(projectDropdown, "Proje dropdown");
        BaseStep.waitSeconds(1);
        WebElement projectOption = BaseStep.findElementXpathWithWait(
                "(//*[@role='option'])[1]", TimeOut.SHORT.value);
        BaseStep.clickElement(projectOption, "Proje secildi");
        BaseStep.waitSeconds(2);

        LogTest.info("Modul seciliyor.");
        WebElement moduleDropdown = BaseStep.findElementXpathWithWait(
                XPATH_MODULE_SELECT, TimeOut.SHORT.value);
        BaseStep.clickElement(moduleDropdown, "Modul dropdown");
        BaseStep.waitSeconds(1);
        WebElement moduleOption = BaseStep.findElementXpathWithWait(
                "(//*[@role='option'])[1]", TimeOut.SHORT.value);
        BaseStep.clickElement(moduleOption, "Modul secildi");
        BaseStep.waitSeconds(2);

        LogTest.info("Aciklama giriliyor.");
        WebElement descriptionInput = BaseStep.findElementXpathWithWait(
                XPATH_DESCRIPTION_INPUT, TimeOut.SHORT.value);
        BaseStep.clearAndType(descriptionInput, "Otomasyon ile olusturulan test senaryosu aciklamasi.",
                "Aciklama alani");
        BaseStep.waitSeconds(1);

        LogTest.info("Beklenen sonuc giriliyor.");
        WebElement expectedResultInput = BaseStep.findElementXpathWithWait(
                XPATH_EXPECTED_RESULT, TimeOut.SHORT.value);
        BaseStep.clearAndType(expectedResultInput, "Test basariyla calismali ve sonuc dogru olmalidir.",
                "Beklenen sonuc alani");
        BaseStep.waitSeconds(1);

        LogTest.info("Test turu seciliyor: Regresyon");
        WebElement typeDropdown = BaseStep.findElementXpathWithWait(
                XPATH_TYPE_SELECT, TimeOut.SHORT.value);
        BaseStep.clickElement(typeDropdown, "Test turu dropdown");
        BaseStep.waitSeconds(1);
        WebElement typeOption = BaseStep.findElementXpathWithWait(
                "//*[@role='option']//span[contains(text(),'Regresyon')]", TimeOut.SHORT.value);
        BaseStep.clickElement(typeOption, "Regresyon secildi");
        BaseStep.waitSeconds(2);

        LogTest.info("Oncelik seciliyor: Yuksek");
        WebElement priorityDropdown = BaseStep.findElementXpathWithWait(
                XPATH_PRIORITY_SELECT, TimeOut.SHORT.value);
        BaseStep.clickElement(priorityDropdown, "Oncelik dropdown");
        BaseStep.waitSeconds(1);
        WebElement priorityOption = BaseStep.findElementXpathWithWait(
                "//*[@role='option']//span[contains(text(),'Yüksek')]", TimeOut.SHORT.value);
        BaseStep.clickElement(priorityOption, "Yuksek secildi");
        BaseStep.waitSeconds(2);

        LogTest.info("Ekle butonuna tiklaniyor.");
        WebElement submitBtn = BaseStep.findElementXpathWithWait(
                XPATH_SUBMIT_BTN, TimeOut.SHORT.value);
        BaseStep.clickElement(submitBtn, "Ekle butonu");
        BaseStep.waitSeconds(3);
        LogTest.info("Test senaryosu basariyla olusturuldu.");
    }

    // ============================================================
    // TEST: 7 — LISTEDEN DETAYLARI GOR
    // ============================================================

    @Test
    @Order(7)
    @DisplayName("Listeden Detaylari Gor butonu ile detay sayfasi acma")
    public void viewTestCaseDetailsTest() {
        LogTest.info("Listedeki Detaylari Gor butonuna tiklaniyor.");
        WebElement detailsBtn = BaseStep.findElementXpathWithWait(
                XPATH_ROW_DETAILS_BTN, TimeOut.MEDIUM.value);
        BaseStep.clickElement(detailsBtn, "Detaylari Gor butonu");
        BaseStep.waitSeconds(3);

        LogTest.info("Detay sayfasinin yuklendigi dogrulaniyor.");
        WebElement editBtn = BaseStep.findElementXpathWithWait(
                XPATH_EDIT_BTN, TimeOut.MEDIUM.value);
        assertTrue(editBtn.isDisplayed(), "Detay sayfasi acilmadi!");
        LogTest.info("Test senaryosu detay sayfasi acildi.");
    }

    // ============================================================
    // TEST: 8 — DETAY SAYFASINDA DUZENLEME
    // ============================================================

    @Test
    @Order(8)
    @DisplayName("Detay sayfasinda alanlari duzenle ve kaydet")
    public void editTestCaseDetailsTest() {
        LogTest.info("Duzenle butonuna tiklaniyor.");
        WebElement editBtn = BaseStep.findElementXpathWithWait(
                XPATH_EDIT_BTN, TimeOut.MEDIUM.value);
        BaseStep.clickElement(editBtn, "Duzenle butonu");
        BaseStep.waitSeconds(2);

        LogTest.info("Baslik guncelleniyor.");
        WebElement titleInput = BaseStep.findElementXpathWithWait(
                XPATH_EDIT_TITLE, TimeOut.SHORT.value);
        BaseStep.clearAndType(titleInput, "Guncellenmis Test Senaryosu", "Baslik alani (duzenleme)");
        BaseStep.waitSeconds(1);

        LogTest.info("Test turu guncelleniyor: Fonksiyonel");
        WebElement typeDropdown = BaseStep.findElementXpathWithWait(
                XPATH_EDIT_TYPE, TimeOut.SHORT.value);
        BaseStep.clickElement(typeDropdown, "Test turu dropdown");
        BaseStep.waitSeconds(1);
        WebElement typeOption = BaseStep.findElementXpathWithWait(
                "//*[@role='option']//span[contains(text(),'Fonksiyonel')]", TimeOut.SHORT.value);
        BaseStep.clickElement(typeOption, "Fonksiyonel secildi");
        BaseStep.waitSeconds(2);

        LogTest.info("Oncelik guncelleniyor: Dusuk");
        WebElement priorityDropdown = BaseStep.findElementXpathWithWait(
                XPATH_EDIT_PRIORITY, TimeOut.SHORT.value);
        BaseStep.clickElement(priorityDropdown, "Oncelik dropdown");
        BaseStep.waitSeconds(1);
        WebElement priorityOption = BaseStep.findElementXpathWithWait(
                "//*[@role='option']//span[contains(text(),'Düşük')]", TimeOut.SHORT.value);
        BaseStep.clickElement(priorityOption, "Dusuk secildi");
        BaseStep.waitSeconds(2);

        LogTest.info("Aciklama guncelleniyor.");
        WebElement descInput = BaseStep.findElementXpathWithWait(
                XPATH_EDIT_DESCRIPTION, TimeOut.SHORT.value);
        BaseStep.clearAndType(descInput, "Guncellenmis aciklama metni.", "Aciklama alani (duzenleme)");
        BaseStep.waitSeconds(1);

        LogTest.info("Beklenen sonuc guncelleniyor.");
        WebElement expectedInput = BaseStep.findElementXpathWithWait(
                XPATH_EDIT_EXPECTED, TimeOut.SHORT.value);
        BaseStep.clearAndType(expectedInput, "Guncellenmis beklenen sonuc.", "Beklenen sonuc alani (duzenleme)");
        BaseStep.waitSeconds(1);

        LogTest.info("Kaydet butonuna tiklaniyor.");
        WebElement saveBtn = BaseStep.findElementXpathWithWait(
                XPATH_SAVE_BTN, TimeOut.SHORT.value);
        BaseStep.clickElement(saveBtn, "Kaydet butonu");
        BaseStep.waitSeconds(3);
        LogTest.info("Test senaryosu basariyla guncellendi.");
    }

    // ============================================================
    // TEST: 9 — ADIMLAR SEKMESI - ADIM EKLE VE KAYDET
    // ============================================================

    @Test
    @Order(9)
    @DisplayName("Adimlar sekmesi - adim ekle ve degisiklikleri kaydet")
    public void addTestCaseStepsTest() {
        LogTest.info("Ilk Adim Ekle butonuna tiklaniyor.");
        WebElement firstAddStepBtn = BaseStep.findElementXpathWithWait(
                "(//button[normalize-space(.)='Adım Ekle'])[1]", TimeOut.MEDIUM.value);
        BaseStep.clickElement(firstAddStepBtn, "Ilk Adim Ekle butonu");
        BaseStep.waitSeconds(2);

        LogTest.info("Ikinci kez Adim Ekle butonuna tiklaniyor.");
        WebElement secondAddStepBtn = BaseStep.findElementXpathWithWait(
                XPATH_STEPS_ADD_BTN, TimeOut.MEDIUM.value);
        BaseStep.clickElement(secondAddStepBtn, "Adim Ekle butonu (testid)");
        BaseStep.waitSeconds(2);

        LogTest.info("Adim aciklamasi giriliyor.");
        WebElement stepDesc = BaseStep.findElementXpathWithWait(
                XPATH_STEP_DESC_0, TimeOut.SHORT.value);
        BaseStep.clearAndType(stepDesc, "Otomasyon adim 1 aciklamasi.", "Adim 1 aciklama");
        BaseStep.waitSeconds(1);

        LogTest.info("Adim beklenen sonucu giriliyor.");
        WebElement stepExpected = BaseStep.findElementXpathWithWait(
                XPATH_STEP_EXPECTED_0, TimeOut.SHORT.value);
        BaseStep.clearAndType(stepExpected, "Adim 1 beklenen sonuc.", "Adim 1 beklenen sonuc");
        BaseStep.waitSeconds(1);

        LogTest.info("Adim test verisi giriliyor.");
        WebElement stepData = BaseStep.findElementXpathWithWait(
                XPATH_STEP_DATA_0, TimeOut.SHORT.value);
        BaseStep.clearAndType(stepData, "Adim 1 test verisi.", "Adim 1 test verisi");
        BaseStep.waitSeconds(1);

        LogTest.info("Degisiklikleri Kaydet butonuna tiklaniyor.");
        WebElement stepsSaveBtn = BaseStep.findElementXpathWithWait(
                XPATH_STEPS_SAVE_BTN, TimeOut.SHORT.value);
        BaseStep.clickElement(stepsSaveBtn, "Degisiklikleri Kaydet butonu");
        BaseStep.waitSeconds(3);
        LogTest.info("Adimlar basariyla kaydedildi.");
    }

    // ============================================================
    // TEST: 10 — CALISMA GECMISI SEKMESI
    // ============================================================

    @Test
    @Order(10)
    @DisplayName("Calisma Gecmisi sekmesine gec")
    public void switchToExecutionsTabTest() {
        LogTest.info("Calisma Gecmisi sekmesine tiklaniyor.");
        WebElement executionsTab = BaseStep.findElementXpathWithWait(
                XPATH_TAB_EXECUTIONS, TimeOut.MEDIUM.value);
        BaseStep.clickElement(executionsTab, "Calisma Gecmisi sekmesi");
        BaseStep.waitSeconds(2);
        LogTest.info("Calisma Gecmisi sekmesi acildi.");
    }

    // ============================================================
    // TEST: 11 — HATALAR SEKMESI
    // ============================================================

    @Test
    @Order(11)
    @DisplayName("Hatalar sekmesine gec")
    public void switchToDefectsTabTest() {
        LogTest.info("Hatalar sekmesine tiklaniyor.");
        WebElement defectsTab = BaseStep.findElementXpathWithWait(
                XPATH_TAB_DEFECTS, TimeOut.MEDIUM.value);
        BaseStep.clickElement(defectsTab, "Hatalar sekmesi");
        BaseStep.waitSeconds(2);
        LogTest.info("Hatalar sekmesi acildi.");
    }

    // ============================================================
    // TEST: 12 — GEREKSINIMLER SEKMESI - SECIM VE BAGLAMA
    // ============================================================

    @Test
    @Order(12)
    @DisplayName("Gereksinimler sekmesi - gereksinim sec ve bagla")
    public void linkRequirementTest() {
        LogTest.info("Gereksinimler sekmesine tiklaniyor.");
        WebElement reqTab = BaseStep.findElementXpathWithWait(
                XPATH_TAB_REQUIREMENTS, TimeOut.MEDIUM.value);
        BaseStep.clickElement(reqTab, "Gereksinimler sekmesi");
        BaseStep.waitSeconds(2);

        LogTest.info("Baglanacak gereksinim dropdown aciliyor.");
        WebElement reqDropdown = BaseStep.findElementXpathWithWait(
                XPATH_REQ_SELECT, TimeOut.SHORT.value);
        BaseStep.clickElement(reqDropdown, "Gereksinim dropdown");
        BaseStep.waitSeconds(1);

        LogTest.info("Listeden ilk gereksinim seciliyor.");
        WebElement reqOption = BaseStep.findElementXpathWithWait(
                "(//*[@role='option'])[1]", TimeOut.SHORT.value);
        BaseStep.clickElement(reqOption, "Gereksinim secildi");
        BaseStep.waitSeconds(2);

        LogTest.info("Gereksinimi Bagla butonuna tiklaniyor.");
        WebElement linkBtn = BaseStep.findElementXpathWithWait(
                XPATH_REQ_LINK_BTN, TimeOut.SHORT.value);
        BaseStep.clickElement(linkBtn, "Gereksinimi Bagla butonu");
        BaseStep.waitSeconds(3);
        LogTest.info("Gereksinim basariyla baglandi.");
    }

    // ============================================================
    // TEST: 13 — IS AKISI SEKMESI
    // ============================================================

    @Test
    @Order(13)
    @DisplayName("Is Akisi sekmesine gec")
    public void switchToWorkflowTabTest() {
        LogTest.info("Is Akisi sekmesine tiklaniyor.");
        WebElement workflowTab = BaseStep.findElementXpathWithWait(
                XPATH_TAB_WORKFLOW, TimeOut.MEDIUM.value);
        BaseStep.clickElement(workflowTab, "Is Akisi sekmesi");
        BaseStep.waitSeconds(2);
        LogTest.info("Is Akisi sekmesi acildi.");
    }

    // ============================================================
    // TEST: 14 — LISTEDEN TEST SENARYOSU SILME
    // ============================================================

    @Test
    @Order(14)
    @DisplayName("Listeden test senaryosu silme - onay popupi ile")
    public void deleteTestCaseTest() {
        LogTest.info("Test Senaryolari listesine geri donuluyor.");
        WebElement testCasesSidebarBtn = BaseStep.findElementXpathWithWait(
                XPATH_SIDEBAR_TEST_CASES, TimeOut.MEDIUM.value);
        BaseStep.clickElement(testCasesSidebarBtn, "Test Senaryolari sidebar butonu");
        BaseStep.waitSeconds(2);

        LogTest.info("Ilk satirin Sil butonuna tiklaniyor.");
        WebElement deleteBtn = BaseStep.findElementXpathWithWait(
                XPATH_ROW_DELETE_BTN, TimeOut.MEDIUM.value);
        BaseStep.jsClickElement(deleteBtn, "Sil butonu");
        BaseStep.waitSeconds(2);

        LogTest.info("Onay popupinda Sil butonuna tiklaniyor.");
        WebElement confirmBtn = BaseStep.findElementXpathWithWait(
                XPATH_DELETE_CONFIRM_BTN, TimeOut.SHORT.value);
        BaseStep.jsClickElement(confirmBtn, "Sil onay butonu");
        BaseStep.waitSeconds(3);
        LogTest.info("Test senaryosu basariyla silindi.");
    }
}
