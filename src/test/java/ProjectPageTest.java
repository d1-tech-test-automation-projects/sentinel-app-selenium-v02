import org.junit.jupiter.api.*;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProjectPageTest extends BaseStep {

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
    // TEST: 1 — GİRİŞ YAP
    // ============================================================

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

    // ============================================================
    // TEST: 2 — TÜM PROJELER SAYFASINA GİT
    // ============================================================

    @Test
    @Order(2)
    @DisplayName("Tüm Projeler sayfasına git")
    public void navigateToAllProjects() {
        LogTest.info("Projeler menüsü aranıyor.");
        WebElement projectsMenu = BaseStep.findElementXpathWithWait(
                "//*[@id=\"root\"]/div/aside/div[2]/div[1]/nav/div[1]/div/button[1]",
                TimeOut.SHORT.value);
        BaseStep.clickElement(projectsMenu, "Projeler menüsüne tıklandı.");
        BaseStep.waitSeconds(2);

        LogTest.info("Tüm Projeler alt menüsü aranıyor.");
        WebElement allProjectsLink = BaseStep.findElementXpathWithWait(
                "//*[@id=\"root\"]/div/aside/div[2]/div[1]/nav/div[1]/div[2]/button",
                TimeOut.SHORT.value);
        BaseStep.clickElement(allProjectsLink, "Tüm Projeler bağlantısına tıklandı.");
        BaseStep.waitSeconds(2);

        LogTest.info("Sayfanın yüklendiği doğrulanıyor.");
        BaseStep.findElementXpathWithWait("//main", TimeOut.SHORT.value);
        LogTest.info("Tüm Projeler sayfası başarıyla açıldı.");
    }

    // ============================================================
    // FILTER LOCATORS (data-testid tabanli - daha kararli)
    // ============================================================

    private static final String XPATH_NAME_FILTER       = "//input[@data-testid='projects-name-filter']";
    private static final String XPATH_GROUP_DROPDOWN    = "//button[@data-testid='projects-group-filter']";
    private static final String XPATH_STATUS_DROPDOWN   = "//button[@data-testid='projects-status-filter']";
    private static final String XPATH_PRIORITY_DROPDOWN = "//button[@data-testid='projects-priority-filter']";
    private static final String XPATH_ADD_PROJECT_BTN   = "//button[@data-testid='projects-add-button']";

    // ============================================================
    // TEST: 3 — PROJE ADINA GÖRE FİLTRELE
    // ============================================================

    @Test
    @Order(3)
    @DisplayName("Proje adına göre filtrele testi")
    public void nameFilterTest() {
        LogTest.info("Proje adı filtre alanına metin giriliyor.");
        WebElement nameFilter = BaseStep.findElementXpathWithWait(
                XPATH_NAME_FILTER, TimeOut.SHORT.value);
        BaseStep.clearAndType(nameFilter, "Test", "Proje adı filtre alanı");
        BaseStep.waitSeconds(2);
        LogTest.info("Filtreleme uygulandı.");

        LogTest.info("Filtre temizleniyor.");
        BaseStep.clearAndType(nameFilter, "", "Proje adı filtre alanı");
        BaseStep.waitSeconds(2);
        LogTest.info("Proje adı filtre testi tamamlandı.");
    }

    // ============================================================
    // TEST: 4 — GRUP FİLTRESİ
    // ============================================================

    @Test
    @Order(4)
    @DisplayName("Grup filtresi testi - D1-Tech, Hukuk Ürünleri, QA/TEST")
    public void groupFilterTest() {
        WebElement dropdown;
        WebElement option;

        // --- D1-Tech ---
        LogTest.info("Grup dropdown açılıyor: D1-Tech");
        dropdown = BaseStep.findElementXpathWithWait(XPATH_GROUP_DROPDOWN, TimeOut.SHORT.value);
        BaseStep.clickElement(dropdown, "Grup dropdown açıldı.");
        BaseStep.waitSeconds(1);
        option = BaseStep.findElementXpathWithWait(
                "//*[@role='option']//span[contains(text(),'D1-Tech')]", TimeOut.SHORT.value);
        BaseStep.clickElement(option, "D1-Tech seçildi.");
        BaseStep.waitSeconds(2);
        LogTest.info("D1-Tech filtresi uygulandı.");

        // --- Hukuk Ürünleri ---
        LogTest.info("Grup dropdown açılıyor: Hukuk Ürünleri");
        dropdown = BaseStep.findElementXpathWithWait(XPATH_GROUP_DROPDOWN, TimeOut.SHORT.value);
        BaseStep.clickElement(dropdown, "Grup dropdown açıldı.");
        BaseStep.waitSeconds(1);
        option = BaseStep.findElementXpathWithWait(
                "//*[@role='option']//span[contains(text(),'Hukuk Ürünleri')]", TimeOut.SHORT.value);
        BaseStep.clickElement(option, "Hukuk Ürünleri seçildi.");
        BaseStep.waitSeconds(2);
        LogTest.info("Hukuk Ürünleri filtresi uygulandı.");

        // --- QA/TEST ---
        LogTest.info("Grup dropdown açılıyor: QA/TEST");
        dropdown = BaseStep.findElementXpathWithWait(XPATH_GROUP_DROPDOWN, TimeOut.SHORT.value);
        BaseStep.clickElement(dropdown, "Grup dropdown açıldı.");
        BaseStep.waitSeconds(1);
        option = BaseStep.findElementXpathWithWait(
                "//*[@role='option']//span[contains(text(),'QA')]", TimeOut.SHORT.value);
        BaseStep.clickElement(option, "QA/TEST seçildi.");
        BaseStep.waitSeconds(2);
        LogTest.info("QA/TEST filtresi uygulandı.");

        // --- Tüm Gruplar'a sıfırla (yalnızca en sonda bir kez) ---
        LogTest.info("Grup filtresi sıfırlanıyor.");
        dropdown = BaseStep.findElementXpathWithWait(XPATH_GROUP_DROPDOWN, TimeOut.SHORT.value);
        BaseStep.clickElement(dropdown, "Grup dropdown açıldı.");
        BaseStep.waitSeconds(1);
        option = BaseStep.findElementXpathWithWait(
                "//*[@role='option']//span[contains(text(),'Tüm Gruplar')]", TimeOut.SHORT.value);
        BaseStep.clickElement(option, "Tüm Gruplar seçildi.");
        BaseStep.waitSeconds(2);
        LogTest.info("Grup filtresi testi tamamlandı.");
    }

    // ============================================================
    // TEST: 5 — DURUM FİLTRESİ
    // ============================================================

    @Test
    @Order(5)
    @DisplayName("Durum filtresi testi - Bekliyor, Devam Ediyor")
    public void statusFilterTest() {
        WebElement dropdown;
        WebElement option;

        // --- Bekliyor ---
        LogTest.info("Durum dropdown açılıyor: Bekliyor");
        dropdown = BaseStep.findElementXpathWithWait(XPATH_STATUS_DROPDOWN, TimeOut.SHORT.value);
        BaseStep.clickElement(dropdown, "Durum dropdown açıldı.");
        BaseStep.waitSeconds(1);
        option = BaseStep.findElementXpathWithWait(
                "//*[@role='option']//span[contains(text(),'Bekliyor')]", TimeOut.SHORT.value);
        BaseStep.clickElement(option, "Bekliyor seçildi.");
        BaseStep.waitSeconds(2);
        LogTest.info("Bekliyor filtresi uygulandı.");

        // --- Devam Ediyor ---
        LogTest.info("Durum dropdown açılıyor: Devam Ediyor");
        dropdown = BaseStep.findElementXpathWithWait(XPATH_STATUS_DROPDOWN, TimeOut.SHORT.value);
        BaseStep.clickElement(dropdown, "Durum dropdown açıldı.");
        BaseStep.waitSeconds(1);
        option = BaseStep.findElementXpathWithWait(
                "//*[@role='option']//span[contains(text(),'Devam Ediyor')]", TimeOut.SHORT.value);
        BaseStep.clickElement(option, "Devam Ediyor seçildi.");
        BaseStep.waitSeconds(2);
        LogTest.info("Devam Ediyor filtresi uygulandı.");

        // --- Tüm Durumlar'a sıfırla (yalnızca en sonda bir kez) ---
        LogTest.info("Durum filtresi sıfırlanıyor.");
        dropdown = BaseStep.findElementXpathWithWait(XPATH_STATUS_DROPDOWN, TimeOut.SHORT.value);
        BaseStep.clickElement(dropdown, "Durum dropdown açıldı.");
        BaseStep.waitSeconds(1);
        option = BaseStep.findElementXpathWithWait(
                "//*[@role='option']//span[contains(text(),'Tüm Durumlar')]", TimeOut.SHORT.value);
        BaseStep.clickElement(option, "Tüm Durumlar seçildi.");
        BaseStep.waitSeconds(2);
        LogTest.info("Durum filtresi testi tamamlandı.");
    }

    // ============================================================
    // TEST: 6 — ÖNCELİK FİLTRESİ
    // ============================================================

    @Test
    @Order(6)
    @DisplayName("Öncelik filtresi testi - Düşük, Orta, Yüksek, Kritik")
    public void priorityFilterTest() {
        String[] priorities = {"Düşük", "Orta", "Yüksek", "Kritik"};

        for (String priority : priorities) {
            LogTest.info("Öncelik dropdown açılıyor: " + priority);
            WebElement dropdown = BaseStep.findElementXpathWithWait(
                    XPATH_PRIORITY_DROPDOWN, TimeOut.SHORT.value);
            BaseStep.clickElement(dropdown, "Öncelik dropdown açıldı.");
            BaseStep.waitSeconds(1);

            WebElement option = BaseStep.findElementXpathWithWait(
                    "//*[@role='option']//span[contains(text(),'" + priority + "')]",
                    TimeOut.SHORT.value);
            BaseStep.clickElement(option, priority + " seçildi.");
            BaseStep.waitSeconds(2);
            LogTest.info(priority + " filtresi uygulandı.");
        }

        // --- Tüm Öncelikler'e sıfırla (yalnızca en sonda bir kez) ---
        LogTest.info("Öncelik filtresi sıfırlanıyor.");
        WebElement dropdown = BaseStep.findElementXpathWithWait(
                XPATH_PRIORITY_DROPDOWN, TimeOut.SHORT.value);
        BaseStep.clickElement(dropdown, "Öncelik dropdown açıldı.");
        BaseStep.waitSeconds(1);
        WebElement resetOption = BaseStep.findElementXpathWithWait(
                "//*[@role='option']//span[contains(text(),'Tüm Öncelikler')]",
                TimeOut.SHORT.value);
        BaseStep.clickElement(resetOption, "Tüm Öncelikler seçildi.");
        BaseStep.waitSeconds(2);
        LogTest.info("Öncelik filtresi testi tamamlandı.");
    }

    // ============================================================
    // TEST: 7 — PROJE EKLEME (TAM SÜREÇ)
    // ============================================================

    @Test
    @Order(7)
    @DisplayName("Proje ekleme - tam süreç")
    public void addProject() {

        // --- ACILIS: Yeni Proje Ekle popup'ini ac ---
        LogTest.info("Yeni Proje Ekle butonuna tıklanıyor.");
        WebElement addBtn = BaseStep.findElementXpathWithWait(
                XPATH_ADD_PROJECT_BTN, TimeOut.MEDIUM.value);
        BaseStep.clickElement(addBtn, "Yeni Proje Ekle butonu");
        BaseStep.waitSeconds(2);

        // --- 1. SEKME: GENEL BİLGİLER ---
        LogTest.info("1. Sekme: Proje adı giriliyor.");
        WebElement projectNameInput = BaseStep.findElementXpathWithWait(
                "//input[@data-testid='project-name-input']", TimeOut.SHORT.value);
        BaseStep.clearAndType(projectNameInput, "Otomasyon Projesi", "Proje Adı alanı");
        BaseStep.waitSeconds(1);

        LogTest.info("Versiyon giriliyor.");
        WebElement projectVersionInput = BaseStep.findElementXpathWithWait(
                "//input[@data-testid='project-version-input']", TimeOut.SHORT.value);
        BaseStep.clearAndType(projectVersionInput, "v1.0", "Versiyon alanı");
        BaseStep.waitSeconds(1);

        LogTest.info("Açıklama giriliyor.");
        WebElement projectDescriptionInput = BaseStep.findElementXpathWithWait(
                "//textarea[@data-testid='project-description-input']", TimeOut.SHORT.value);
        BaseStep.clearAndType(projectDescriptionInput, "Otomasyon açıklaması.", "Açıklama alanı");
        BaseStep.waitSeconds(1);

        LogTest.info("Planlanan başlangıç tarihi giriliyor.");
        WebElement projectStartDateInput = BaseStep.findElementXpathWithWait(
                "//input[@data-testid='project-start-date-input']", TimeOut.SHORT.value);
        setDateInputValue(projectStartDateInput, "12.05.2026", "Planlanan Başlangıç Tarihi");
        BaseStep.waitSeconds(1);

        LogTest.info("Planlanan bitiş tarihi giriliyor.");
        WebElement projectEndDateInput = BaseStep.findElementXpathWithWait(
                "//input[@data-testid='project-end-date-input']", TimeOut.SHORT.value);
        setDateInputValue(projectEndDateInput, "20.05.2027", "Planlanan Bitiş Tarihi");
        BaseStep.waitSeconds(1);

        LogTest.info("Proje tipi seçiliyor: Tekli Modül");
        WebElement projectTypeDropdown = BaseStep.findElementXpathWithWait(
                "//*[@id='project-create-project-type']", TimeOut.SHORT.value);
        BaseStep.clickElement(projectTypeDropdown, "Proje tipi dropdown");
        BaseStep.waitSeconds(1);
        WebElement projectTypeOption = BaseStep.findElementXpathWithWait(
                "//*[@role='listbox']//*[@role='option' and contains(.,'Tekli Modül')]",
                TimeOut.SHORT.value);
        BaseStep.clickElement(projectTypeOption, "Tekli Modül seçildi.");
        BaseStep.waitSeconds(2);

        LogTest.info("Öncelik seçiliyor: Orta");
        WebElement priorityDropdown = BaseStep.findElementXpathWithWait(
                "//*[@id='project-create-priority']", TimeOut.SHORT.value);
        BaseStep.clickElement(priorityDropdown, "Öncelik dropdown");
        BaseStep.waitSeconds(1);
        WebElement priorityOption = BaseStep.findElementXpathWithWait(
                "//*[@role='listbox']//*[@role='option' and contains(.,'Orta')]",
                TimeOut.SHORT.value);
        BaseStep.clickElement(priorityOption, "Orta seçildi.");
        BaseStep.waitSeconds(2);

        LogTest.info("Proje grubu seçiliyor: D1-Tech");
        WebElement projectGroupDropdown = BaseStep.findElementXpathWithWait(
                "//*[@id='project-create-project-group']", TimeOut.SHORT.value);
        BaseStep.clickElement(projectGroupDropdown, "Proje grubu dropdown");
        BaseStep.waitSeconds(1);
        WebElement projectGroupOption = BaseStep.findElementXpathWithWait(
                "//*[@role='listbox']//*[@role='option' and contains(.,'D1-Tech')]",
                TimeOut.SHORT.value);
        BaseStep.clickElement(projectGroupOption, "D1-Tech seçildi.");
        BaseStep.waitSeconds(2);

        clickWizardNext();

        // --- 2. SEKME: MODÜLLER ---
        LogTest.info("2. Sekme: Yeni Modül Ekle butonuna tıklanıyor.");
        WebElement newModuleBtn = BaseStep.findElementXpathWithWait(
                "//div[@role='dialog']//button[normalize-space(.)='Yeni Modül Ekle']",
                TimeOut.MEDIUM.value);
        BaseStep.clickElement(newModuleBtn, "Yeni Modül Ekle butonu");
        BaseStep.waitSeconds(2);

        LogTest.info("Modül adı giriliyor.");
        WebElement moduleNameInput = BaseStep.findElementXpathWithWait(
                "//input[@data-testid='project-module-name-input']", TimeOut.SHORT.value);
        BaseStep.clearAndType(moduleNameInput, "Test Modülü", "Modül adı alanı");
        BaseStep.waitSeconds(1);

        LogTest.info("Modül açıklaması giriliyor.");
        WebElement moduleDescInput = BaseStep.findElementXpathWithWait(
                "//textarea[@data-testid='project-module-description-input']", TimeOut.SHORT.value);
        BaseStep.clearAndType(moduleDescInput, "Modül Açıklaması", "Modül açıklama alanı");
        BaseStep.waitSeconds(1);

        LogTest.info("Modül versiyonu giriliyor.");
        WebElement moduleVersionInput = BaseStep.findElementXpathWithWait(
                "//input[@data-testid='project-module-version-input']", TimeOut.SHORT.value);
        BaseStep.clearAndType(moduleVersionInput, "1.0.0", "Modül Versiyon alanı");
        BaseStep.waitSeconds(1);

        LogTest.info("Modül önceliği seçiliyor: Orta");
        WebElement modulePriorityDropdown = BaseStep.findElementXpathWithWait(
                "(//div[@role='dialog'])[last()]//button[@role='combobox']",
                TimeOut.SHORT.value);
        BaseStep.clickElement(modulePriorityDropdown, "Modül öncelik dropdown");
        BaseStep.waitSeconds(1);
        WebElement modulePriorityOption = BaseStep.findElementXpathWithWait(
                "//*[@role='listbox']//*[@role='option' and contains(.,'Orta')]",
                TimeOut.SHORT.value);
        BaseStep.clickElement(modulePriorityOption, "Modül Orta seçildi.");
        BaseStep.waitSeconds(2);

        LogTest.info("Modül Ekle butonuna tıklanıyor.");
        WebElement moduleAddBtn = BaseStep.findElementXpathWithWait(
                "(//div[@role='dialog'])[last()]//button[normalize-space(.)='Ekle']",
                TimeOut.SHORT.value);
        BaseStep.clickElement(moduleAddBtn, "Modül Ekle butonu");
        BaseStep.waitSeconds(2);

        clickWizardNext();

        // --- 3. SEKME: GEREKSİNİMLER ---
        LogTest.info("3. Sekme: Gereksinim Ekle butonuna tıklanıyor.");
        WebElement addRequirementBtn = BaseStep.findElementXpathWithWait(
                "//div[@role='dialog']//button[normalize-space(.)='Gereksinim Ekle']",
                TimeOut.MEDIUM.value);
        BaseStep.clickElement(addRequirementBtn, "Gereksinim Ekle butonu");
        BaseStep.waitSeconds(2);

        LogTest.info("Gereksinim adı giriliyor.");
        WebElement reqNameInput = BaseStep.findElementXpathWithWait(
                "(//div[@role='dialog'])[last()]//input[@placeholder='Gereksinim adı']",
                TimeOut.SHORT.value);
        BaseStep.clearAndType(reqNameInput, "Gereksinim 1", "Gereksinim adı alanı");
        BaseStep.waitSeconds(1);

        LogTest.info("Gereksinim açıklaması giriliyor.");
        WebElement reqDescInput = BaseStep.findElementXpathWithWait(
                "(//div[@role='dialog'])[last()]//textarea[@placeholder='Gereksinim açıklaması']",
                TimeOut.SHORT.value);
        BaseStep.clearAndType(reqDescInput, "Gereksinim detayları.", "Gereksinim açıklama alanı");
        BaseStep.waitSeconds(1);

        LogTest.info("Gereksinim türü seçiliyor: Fonksiyonel");
        WebElement reqTypeDropdown = BaseStep.findElementXpathWithWait(
                "((//div[@role='dialog'])[last()]//button[@role='combobox'])[1]",
                TimeOut.SHORT.value);
        BaseStep.clickElement(reqTypeDropdown, "Gereksinim türü dropdown");
        BaseStep.waitSeconds(1);
        WebElement reqTypeOption = BaseStep.findElementXpathWithWait(
                "//*[@role='listbox']//*[@role='option' and contains(.,'Fonksiyonel') and not(contains(.,'Olmayan'))]",
                TimeOut.SHORT.value);
        BaseStep.clickElement(reqTypeOption, "Fonksiyonel seçildi.");
        BaseStep.waitSeconds(2);

        LogTest.info("Gereksinim önceliği seçiliyor: Yüksek");
        WebElement reqPriorityDropdown = BaseStep.findElementXpathWithWait(
                "((//div[@role='dialog'])[last()]//button[@role='combobox'])[2]",
                TimeOut.SHORT.value);
        BaseStep.clickElement(reqPriorityDropdown, "Gereksinim öncelik dropdown");
        BaseStep.waitSeconds(1);
        WebElement reqPriorityOption = BaseStep.findElementXpathWithWait(
                "//*[@role='listbox']//*[@role='option' and contains(.,'Yüksek')]",
                TimeOut.SHORT.value);
        BaseStep.clickElement(reqPriorityOption, "Yüksek seçildi.");
        BaseStep.waitSeconds(2);

        LogTest.info("Gereksinim Kaydet butonuna tıklanıyor.");
        WebElement reqSaveBtn = BaseStep.findElementXpathWithWait(
                "(//div[@role='dialog'])[last()]//button[normalize-space(.)='Kaydet']",
                TimeOut.SHORT.value);
        BaseStep.clickElement(reqSaveBtn, "Gereksinim Kaydet butonu");
        BaseStep.waitSeconds(2);

        clickWizardNext();

        // --- 4. SEKME: ORTAMLAR ---
        LogTest.info("4. Sekme: Ortam Ekle butonuna tıklanıyor.");
        WebElement addEnvBtn = BaseStep.findElementXpathWithWait(
                "//div[@role='dialog']//button[normalize-space(.)='Ortam Ekle']",
                TimeOut.SHORT.value);
        BaseStep.clickElement(addEnvBtn, "Ortam Ekle butonu");
        BaseStep.waitSeconds(2);

        LogTest.info("Ortam Kod alanı dolduruluyor.");
        WebElement envCodeInput = BaseStep.findElementXpathWithWait(
                "(//div[@role='dialog']//table//input[@aria-label='Kod'])[last()]",
                TimeOut.SHORT.value);
        BaseStep.clearAndType(envCodeInput, "UAT", "Ortam Kod alanı");
        BaseStep.waitSeconds(1);

        LogTest.info("Ortam Ad alanı dolduruluyor.");
        WebElement envNameInput = BaseStep.findElementXpathWithWait(
                "(//div[@role='dialog']//table//input[@aria-label='Ad'])[last()]",
                TimeOut.SHORT.value);
        BaseStep.clearAndType(envNameInput, "Kullanici Kabul", "Ortam Ad alanı");
        BaseStep.waitSeconds(1);

        LogTest.info("Ortam Açıklama alanı dolduruluyor.");
        WebElement envDescInput = BaseStep.findElementXpathWithWait(
                "(//div[@role='dialog']//table//input[@aria-label='Açıklama'])[last()]",
                TimeOut.SHORT.value);
        BaseStep.clearAndType(envDescInput, "Kullanici kabul testi ortamı.", "Ortam Açıklama alanı");
        BaseStep.waitSeconds(1);

        clickWizardNext();

        // --- 5. SEKME: TAKIMLAR ---
        LogTest.info("5. Sekme: Takım seçiliyor.");
        WebElement teamDropdown = BaseStep.findElementXpathWithWait(
                "//div[@role='dialog']//button[@role='combobox']",
                TimeOut.SHORT.value);
        BaseStep.clickElement(teamDropdown, "Takım dropdown");
        BaseStep.waitSeconds(2);
        WebElement ademTeamOption = BaseStep.findElementXpathWithWait(
                "//*[@role='listbox']//*[@role='option' and contains(.,'Adem_Team')]",
                TimeOut.SHORT.value);
        BaseStep.clickElement(ademTeamOption, "Adem_Team seçildi.");
        BaseStep.waitSeconds(2);

        LogTest.info("Takım Ekle butonuna tıklanıyor.");
        WebElement addTeamBtn = BaseStep.findElementXpathWithWait(
                "//div[@role='dialog']//button[normalize-space(.)='Takım Ekle']",
                TimeOut.SHORT.value);
        BaseStep.clickElement(addTeamBtn, "Takım Ekle butonu");
        BaseStep.waitSeconds(2);

        clickWizardNext();

        // --- 6. SEKME: ARA SEKME (alan yok, sadece ileri) ---
        LogTest.info("6. Sekme: Doldurulacak alan yok, İleri'ye basılıyor.");
        clickWizardNext();

        // --- 7. SEKME: KAYDET ---
        LogTest.info("7. Sekme: Proje kaydediliyor.");
        WebElement saveBtn = BaseStep.findElementXpathWithWait(
                "//button[@data-testid='project-wizard-save']",
                TimeOut.MEDIUM.value);
        BaseStep.clickElement(saveBtn, "Kaydet butonu");
        BaseStep.waitSeconds(3);
        LogTest.info("Proje başarıyla oluşturuldu.");
    }

    // ============================================================
    // TEST: 8 — PROJE DÜZENLEME
    // ============================================================

    @Test
    @Order(8)
    @DisplayName("Proje düzenleme - alanları güncelle")
    public void editProjectTest() {
        LogTest.info("Tüm Projeler tablosuna geri dönülüyor.");
        WebElement projectsMenu = BaseStep.findElementXpathWithWait(
                "//*[@id=\"root\"]/div/aside/div[2]/div[1]/nav/div[1]/div/button[1]",
                TimeOut.SHORT.value);
        BaseStep.clickElement(projectsMenu, "Projeler menüsü");
        BaseStep.waitSeconds(2);

        WebElement allProjectsLink = BaseStep.findElementXpathWithWait(
                "//*[@id=\"root\"]/div/aside/div[2]/div[1]/nav/div[1]/div[2]/button",
                TimeOut.SHORT.value);
        BaseStep.clickElement(allProjectsLink, "Tüm Projeler bağlantısı");
        BaseStep.waitSeconds(2);

        LogTest.info("İlk projenin Düzenle butonuna tıklanıyor.");
        WebElement editBtn = BaseStep.findElementXpathWithWait(
                "(//button[@aria-label='Düzenle'])[1]", TimeOut.SHORT.value);
        BaseStep.clickElement(editBtn, "Düzenle butonu");
        BaseStep.waitSeconds(2);

        LogTest.info("Proje adı güncelleniyor.");
        WebElement editNameInput = BaseStep.findElementXpathWithWait(
                "//*[@id='project-edit-name']", TimeOut.SHORT.value);
        BaseStep.clearAndType(editNameInput, "Güncellenmiş Proje", "Proje Adı (düzenleme)");
        BaseStep.waitSeconds(1);

        LogTest.info("Açıklama güncelleniyor.");
        WebElement editDescInput = BaseStep.findElementXpathWithWait(
                "//*[@id='project-edit-description']", TimeOut.SHORT.value);
        BaseStep.clearAndType(editDescInput, "Güncellenmiş proje açıklaması.", "Açıklama (düzenleme)");
        BaseStep.waitSeconds(1);

        LogTest.info("Planlanan başlangıç tarihi güncelleniyor.");
        WebElement editStartDateInput = BaseStep.findElementXpathWithWait(
                "//*[@id='project-edit-expected-start-date']", TimeOut.SHORT.value);
        setDateInputValue(editStartDateInput, "15.06.2026", "Planlanan Başlangıç Tarihi (düzenleme)");
        BaseStep.waitSeconds(1);

        LogTest.info("Güncelle butonuna tıklanıyor.");
        WebElement updateBtn = BaseStep.findElementXpathWithWait(
                "//button[@data-testid='project-edit-submit']", TimeOut.SHORT.value);
        BaseStep.clickElement(updateBtn, "Güncelle butonu");
        BaseStep.waitSeconds(3);
        LogTest.info("Proje başarıyla güncellendi.");
    }

    // ============================================================
    // TEST: 9 — PROJE SİLME
    // ============================================================

    @Test
    @Order(9)
    @DisplayName("Proje silme - onay popup'ı ile")
    public void deleteProjectTest() {
        // Onceki testten kalan acik dialog overlay'i tikliklamayi engelleyebilir.
        // Acik dialog/overlay varsa kapat.
        dismissAnyOpenDialog();
        BaseStep.waitSeconds(1);

        LogTest.info("İlk projenin Sil butonuna tıklanıyor.");
        WebElement deleteBtn = BaseStep.findElementXpathWithWait(
                "(//button[@aria-label='Sil'])[1]", TimeOut.SHORT.value);
        BaseStep.jsClickElement(deleteBtn, "Sil butonu");
        BaseStep.waitSeconds(2);

        LogTest.info("Onay popup'ında 'Evet, Sil' butonuna tıklanıyor.");
        WebElement confirmDeleteBtn = BaseStep.findElementXpathWithWait(
                "(//*[@role='alertdialog' or @role='dialog'])[last()]//button[normalize-space(.)='Evet, Sil']",
                TimeOut.SHORT.value);
        BaseStep.jsClickElement(confirmDeleteBtn, "Evet, Sil butonu");
        BaseStep.waitSeconds(3);
        LogTest.info("Proje başarıyla silindi.");
    }

    // ============================================================
    // HELPER METHODS
    // ============================================================

    /**
     * Proje ekleme wizard'inda bir sonraki sekmeye geçer.
     * data-testid="project-wizard-next" stabil olduğu için onu kullanır.
     */
    private void clickWizardNext() {
        LogTest.info("Wizard İleri butonuna tıklanıyor.");
        WebElement nextBtn = BaseStep.findElementXpathWithWait(
                "//button[@data-testid='project-wizard-next']",
                TimeOut.MEDIUM.value);
        BaseStep.clickElement(nextBtn, "Wizard İleri butonu");
        BaseStep.waitSeconds(2);
    }

    /**
     * Acik kalmis dialog/overlay'i kapatir. JS ile data-state='open' olan
     * tum dialog ve overlay'leri kaldirir. Onceki testten kalmis dialog'lar
     * sonraki testteki tiklamalari engellediginde kullanilir.
     */
    private void dismissAnyOpenDialog() {
        try {
            ((JavascriptExecutor) BaseStep.driver).executeScript(
                    "document.querySelectorAll('[data-slot=\"dialog-overlay\"][data-state=\"open\"]')" +
                    "  .forEach(function(el){ el.remove(); });" +
                    "document.querySelectorAll('[role=\"dialog\"][data-state=\"open\"]')" +
                    "  .forEach(function(el){ el.remove(); });" +
                    "document.body.style.pointerEvents = 'auto';" +
                    "document.body.style.overflow = '';"
            );
            LogTest.stepInfo("Acik dialog ve overlay'ler temizlendi.");
        } catch (Exception e) {
            LogTest.warn("Dialog temizleme hatasi (gormezden geliniyor): " + e.getMessage());
        }
    }

    /**
     * React kontrollü tarih input'una değer set eder.
     * sendKeys/clear React date picker'ı tetiklediğinden, native value setter
     * üzerinden değeri yazar ve input/change/blur event'lerini dispatch eder.
     * Not: ESC dispatch etmiyoruz — Radix dialog'u kapatabilir.
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

}
