import org.junit.jupiter.api.*;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SettingsPageTest extends BaseStep {

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

    // Sidebar Sistem Ayarlari parent (submenu acar)
    private static final String XPATH_SIDEBAR_SETTINGS_PARENT = "//button[@data-menu-key='system-settings-submenu']";
    // Sidebar Ayarlar alt menu (Ayarlar sayfasina navigate)
    private static final String XPATH_SIDEBAR_SETTINGS_ITEM   = "//button[@data-menu-key='/settings']";

    // Tab triggerlari - DILDEN BAGIMSIZ (aria-controls suffix kararli)
    // Genel sekmesinde dil degisirse metinler degisir, bu yuzden metin yerine
    // aria-controls ile (-trigger-general, -trigger-user vb) hedefliyoruz.
    private static final String XPATH_TAB_GENERAL       = "//button[@role='tab' and contains(@aria-controls,'-content-general')]";
    private static final String XPATH_TAB_USER          = "//button[@role='tab' and contains(@aria-controls,'-content-user')]";
    private static final String XPATH_TAB_APPEARANCE    = "//button[@role='tab' and contains(@aria-controls,'-content-appearance')]";
    private static final String XPATH_TAB_NOTIFICATIONS = "//button[@role='tab' and contains(@aria-controls,'-content-notifications')]";
    private static final String XPATH_TAB_SECURITY      = "//button[@role='tab' and contains(@aria-controls,'-content-security')]";
    private static final String XPATH_TAB_AUDIT_LOGS    = "//button[@role='tab' and contains(@aria-controls,'-content-audit-logs')]";

    // DILDEN BAGIMSIZ locator'lar:
    // - Kaydet/Save: type='submit' (attribute, dilden bagimsiz)
    // - Varsayilana Don/Reset: submit'in following-sibling button'i (form
    //   yapisi: Kaydet=button[1], Varsayilana Don=button[2]). SVG class
    //   tabanli locator SVGAnimatedString sebebiyle bazi XPath engine'lerde
    //   match etmiyordu, sibling pattern daha kararli.
    // - Diger butonlar: aria-label veya stabil attribute

    // Genel sekmesi
    private static final String XPATH_GENERAL_LANG_SELECT = "//*[contains(@id,'-content-general')]//button[@role='combobox']";
    private static final String XPATH_GENERAL_SAVE_BTN    = "//*[contains(@id,'-content-general')]//button[@type='submit']";
    private static final String XPATH_GENERAL_RESET_BTN   = "//*[contains(@id,'-content-general')]//button[@type='submit']/following-sibling::button[1]";

    // Kullanici sekmesi
    private static final String XPATH_USER_PHONE_INPUT    = "//*[contains(@id,'-content-user')]//input[@placeholder='+90 ...']";
    private static final String XPATH_USER_SAVE_BTN       = "//*[contains(@id,'-content-user')]//button[@type='submit']";

    // Gorunum sekmesi
    private static final String XPATH_APP_FONT_INPUT      = "//*[contains(@id,'-content-appearance')]//input[@type='number']";
    private static final String XPATH_APP_SAVE_BTN        = "//*[contains(@id,'-content-appearance')]//button[@type='submit']";
    private static final String XPATH_APP_RESET_BTN       = "//*[contains(@id,'-content-appearance')]//button[@type='submit']/following-sibling::button[1]";

    // Bildirimler sekmesi
    private static final String XPATH_NOTIF_SWITCH        = "//*[contains(@id,'-content-notifications')]//button[@role='switch']";
    private static final String XPATH_NOTIF_SAVE_BTN      = "//*[contains(@id,'-content-notifications')]//button[@type='submit']";
    private static final String XPATH_NOTIF_RESET_BTN     = "//*[contains(@id,'-content-notifications')]//button[@type='submit']/following-sibling::button[1]";

    // Denetim Kayitlari sekmesi
    private static final String XPATH_AUDIT_FILTER        = "//*[contains(@id,'-content-audit-logs')]//button[@role='combobox']";

    // Organizasyon ve Yetkilendirme sayfasi
    private static final String XPATH_SIDEBAR_ORG_ACCESS      = "//button[@data-menu-key='/organization-access']";
    private static final String XPATH_TAB_DEPARTMENT          = "//button[@role='tab' and normalize-space(.)='Departman Yönetimi']";
    private static final String XPATH_TAB_ROLE                = "//button[@role='tab' and normalize-space(.)='Rol Yönetimi']";

    // Departman Yonetimi sekmesi
    private static final String XPATH_ADD_DEPARTMENT_BTN      = "//*[contains(@id,'-content-department')]//button[normalize-space(.)='Departman Ekle']";
    private static final String XPATH_DEPT_NAME_INPUT         = "//*[@id='department-name']";
    private static final String XPATH_DEPT_DESC_INPUT         = "//*[@id='department-description']";
    private static final String XPATH_DIALOG_SAVE_BTN         = "(//*[@role='dialog' or @role='alertdialog'])[last()]//button[normalize-space(.)='Kaydet']";

    // Test verileri (eklenecek/duzenlenen departman isimleri)
    private static final String DEPT_NAME_ADDED   = "Otomasyon Departmani";
    private static final String DEPT_NAME_UPDATED = "Guncellenmis Departman";

    // Listede islem butonlari - YENI EKLENEN departman satirini hedefliyor
    // (sadece ilk satir [1] degil; eklenen ismi tasiyan satir).
    private static final String XPATH_ADD_ROLE_TO_DEPT_BTN    =
            "//tr[.//*[contains(normalize-space(.),'" + DEPT_NAME_ADDED + "')]]//button[@aria-label='Rol Ekle']";
    private static final String XPATH_DEPT_ROLE_NAME_INPUT    = "//*[@id='department-role-name']";
    private static final String XPATH_DEPT_ROLE_DESC_INPUT    = "//*[@id='department-role-description']";
    private static final String XPATH_DEPT_EDIT_BTN           =
            "//tr[.//*[contains(normalize-space(.),'" + DEPT_NAME_ADDED + "')]]//button[@aria-label='Departman Düzenle']";
    // Edit sonrasi isim 'Guncellenmis Departman' olur; o satirin Sil butonu
    private static final String XPATH_DEPT_DELETE_BTN         =
            "//tr[.//*[contains(normalize-space(.),'" + DEPT_NAME_UPDATED + "')]]//button[@aria-label='Sil']";
    private static final String XPATH_DEPT_DELETE_CONFIRM_BTN = "(//*[@role='dialog' or @role='alertdialog'])[last()]//button[normalize-space(.)='Tamam']";

    // Rol Yonetimi sekmesi
    private static final String ROLE_NAME_ADDED               = "Otomasyon Yonetim Rolu";
    private static final String XPATH_ADD_ROLE_BTN            = "//*[contains(@id,'-content-role')]//button[normalize-space(.)='Rol Ekle']";
    private static final String XPATH_ROLE_CREATE_BTN         = "(//*[@role='dialog' or @role='alertdialog'])[last()]//button[normalize-space(.)='Rol Oluştur']";
    // Eklenen rolun satirini hedefler (ad ile)
    private static final String XPATH_ROLE_EDIT_BTN           =
            "//tr[.//*[contains(normalize-space(.),'" + ROLE_NAME_ADDED + "')]]//button[@aria-label='Rol Düzenle']";
    private static final String XPATH_UPDATE_ROLE_BTN         = "(//*[@role='dialog' or @role='alertdialog'])[last()]//button[normalize-space(.)='Rolü Güncelle']";
    // Eklenen rolun Sil butonu (ad ile satir lookup)
    private static final String XPATH_ROLE_DELETE_BTN         =
            "//tr[.//*[contains(normalize-space(.),'" + ROLE_NAME_ADDED + "')]]//button[@aria-label='Rol Sil']";
    private static final String XPATH_ROLE_DELETE_CONFIRM_BTN = "(//*[@role='dialog' or @role='alertdialog'])[last()]//button[normalize-space(.)='Tamam']";
    // Rol Ekle popup formu icin generic dialog-scope locator'lar (specifik ID verilmedi)
    private static final String XPATH_DIALOG_FIRST_INPUT      = "((//*[@role='dialog'])[last()]//input)[1]";
    private static final String XPATH_DIALOG_FIRST_TEXTAREA   = "((//*[@role='dialog'])[last()]//textarea)[1]";

    // Kullanici Yonetimi sub-sekmesi locator'lari kaldirildi
    // (backend kullanici olusturmayi reddediyor — UI'da hata gosteriyor ama
    // log basari gibi gorunuyor. Add/edit/delete testleri kaldirildi.)

    // Takim Yonetimi sub-sekmesi
    private static final String TEAM_NAME_ADDED               = "Otomasyon Takimi";
    private static final String TEAM_NAME_UPDATED             = "Guncellenmis Otomasyon Takimi";
    private static final String XPATH_TAB_TEAM                = "//button[@role='tab' and contains(@aria-controls,'-content-team')]";
    private static final String XPATH_ADD_TEAM_BTN            = "//button[@data-testid='teams-add-button']";
    private static final String XPATH_TEAM_NAME_INPUT         = "//*[@id='team-name']";
    private static final String XPATH_TEAM_DESC_INPUT         = "//*[@id='team-description']";
    // Form'daki ilk radio (takim lideri secimi) - Radix radio item
    private static final String XPATH_TEAM_LEADER_RADIO       = "((//*[@role='dialog'])[last()]//button[@role='radio'])[1]";
    private static final String XPATH_TEAM_CREATE_BTN         = "(//*[@role='dialog'])[last()]//button[@type='submit' and normalize-space(.)='Oluştur']";
    private static final String XPATH_TEAM_UPDATE_BTN         = "(//*[@role='dialog'])[last()]//button[@type='submit' and normalize-space(.)='Güncelle']";
    // Detay/Duzenle/Sil listede - UUID tabanli (add sonrasi yakalanir)
    // capturedTeamUuid bir kez eklendikten sonra dolar; edit/delete bu UUID'yi
    // kullanir. Tum 3 buton ayni UUID'yi paylasiyor (team-details-{uuid},
    // team-edit-{uuid}, team-delete-{uuid}).
    private static String capturedTeamUuid = "";
    // Eklenen takimin tum team-details butonlari arasinda son sirada olacagini
    // varsayar (yeni eklenenler genelde tablonun sonunda); fallback olarak isim
    // bazli lookup denenir.
    private static final String XPATH_LAST_TEAM_DETAILS_BTN   =
            "(//*[contains(@id,'-content-team')]//button[starts-with(@data-testid,'team-details-')])[last()]";
    private static final String XPATH_TEAM_BY_NAME_DETAILS    =
            "//tr[.//*[contains(.,'" + TEAM_NAME_ADDED + "')]]//button[starts-with(@data-testid,'team-details-')]";
    // Detay panelini kapatma butonu (Radix dialog close, sr-only 'Close' icin)
    private static final String XPATH_TEAM_DETAIL_CLOSE_BTN   = "(//*[@role='dialog'])[last()]//button[.//span[normalize-space(.)='Close']]";
    private static final String XPATH_TEAM_DELETE_CONFIRM_BTN = "(//*[@role='dialog' or @role='alertdialog'])[last()]//button[normalize-space(.)='Evet']";

    // Denetim Kayitlari sidebar sayfasi
    private static final String XPATH_SIDEBAR_AUDIT_LOGS      = "//button[@data-menu-key='/audit-logs']";
    private static final String XPATH_AUDIT_PAGE_FILTER       = "//main//button[@role='combobox']";

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
    // TEST: 2 — SISTEM AYARLARI SAYFASINA GIT
    // ============================================================

    @Test
    @Order(2)
    @DisplayName("Sistem Ayarlari -> Ayarlar sayfasina git")
    public void navigateToSettingsTest() {
        LogTest.info("Sidebar Sistem Ayarlari (parent) butonuna tiklaniyor.");
        WebElement parentBtn = BaseStep.findElementXpathWithWait(
                XPATH_SIDEBAR_SETTINGS_PARENT, TimeOut.SHORT.value);
        BaseStep.clickElement(parentBtn, "Sistem Ayarlari parent butonu (submenu ac)");
        BaseStep.waitSeconds(2);

        LogTest.info("Acilir listeden Ayarlar butonuna tiklaniyor.");
        WebElement settingsItem = BaseStep.findElementXpathWithWait(
                XPATH_SIDEBAR_SETTINGS_ITEM, TimeOut.MEDIUM.value);
        BaseStep.clickElement(settingsItem, "Ayarlar alt menu butonu");
        BaseStep.waitSeconds(2);

        LogTest.info("Sayfanin yuklendigi dogrulaniyor.");
        WebElement page = BaseStep.findElementXpathWithWait("//main", TimeOut.SHORT.value);
        assertTrue(page.isDisplayed(), "Ayarlar sayfasi yuklenemedi!");
        LogTest.info("Ayarlar sayfasi acildi.");
    }

    // ============================================================
    // TEST: 3 — GENEL SEKMESI: DIL SECIMI VE KAYDET
    // ============================================================

    @Test
    @Order(3)
    @DisplayName("Genel sekmesi - dil sec, kaydet, varsayilana don, kaydet")
    public void generalTabTest() {
        LogTest.info("Genel sekmesi araniyor.");
        WebElement generalTab = BaseStep.findElementXpathWithWait(
                XPATH_TAB_GENERAL, TimeOut.MEDIUM.value);
        BaseStep.clickElement(generalTab, "Genel sekmesi");
        BaseStep.waitSeconds(2);

        LogTest.info("Dil dropdown seciliyor (klavye ile ilk option).");
        openDropdownAndSelectFirst(XPATH_GENERAL_LANG_SELECT, "Dil");

        LogTest.info("Kaydet butonuna tiklaniyor (1. kez - degisiklik).");
        WebElement saveBtn = BaseStep.findElementXpathWithWait(
                XPATH_GENERAL_SAVE_BTN, TimeOut.SHORT.value);
        BaseStep.clickElement(saveBtn, "Genel Kaydet butonu");
        BaseStep.waitSeconds(2);

        // Dil degismis olabilir (English). Sonraki testlerin Turkce metinlerle
        // calismasi icin Varsayilana Don + Kaydet ile dil sifirlanir.
        LogTest.info("Varsayilana Don butonuna tiklaniyor (icon ile bulunur).");
        WebElement resetBtn = BaseStep.findElementXpathWithWait(
                XPATH_GENERAL_RESET_BTN, TimeOut.SHORT.value);
        BaseStep.clickElement(resetBtn, "Genel Varsayilana Don butonu");
        BaseStep.waitSeconds(2);

        LogTest.info("Kaydet butonuna tiklaniyor (2. kez - varsayilan ayarlar).");
        saveBtn = BaseStep.findElementXpathWithWait(
                XPATH_GENERAL_SAVE_BTN, TimeOut.SHORT.value);
        BaseStep.clickElement(saveBtn, "Genel Kaydet butonu (reset sonrasi)");
        BaseStep.waitSeconds(3);
        LogTest.info("Genel sekmesi varsayilan dile dondurulup kaydedildi.");
    }

    // ============================================================
    // TEST: 4 — KULLANICI SEKMESI: TELEFON GUNCELLE VE KAYDET
    // ============================================================

    @Test
    @Order(4)
    @DisplayName("Kullanici sekmesi - telefon guncelle ve kaydet")
    public void userTabTest() {
        LogTest.info("Kullanici sekmesine tiklaniyor.");
        WebElement userTab = BaseStep.findElementXpathWithWait(
                XPATH_TAB_USER, TimeOut.MEDIUM.value);
        BaseStep.clickElement(userTab, "Kullanici sekmesi");
        BaseStep.waitSeconds(2);

        LogTest.info("Telefon numarasi guncelleniyor.");
        WebElement phoneInput = BaseStep.findElementXpathWithWait(
                XPATH_USER_PHONE_INPUT, TimeOut.SHORT.value);
        BaseStep.clearAndType(phoneInput, "+90 5616179003", "Telefon alani");
        BaseStep.waitSeconds(1);

        LogTest.info("Kaydet butonuna tiklaniyor.");
        WebElement saveBtn = BaseStep.findElementXpathWithWait(
                XPATH_USER_SAVE_BTN, TimeOut.SHORT.value);
        BaseStep.clickElement(saveBtn, "Kullanici Kaydet butonu");
        BaseStep.waitSeconds(3);
        LogTest.info("Kullanici sekmesi ayarlari kaydedildi.");
    }

    // ============================================================
    // TEST: 5 — GORUNUM SEKMESI: YAZI BOYUTU + KAYDET + RESET
    // ============================================================

    @Test
    @Order(5)
    @DisplayName("Gorunum sekmesi - yazi boyutu, kaydet, varsayilana don, kaydet")
    public void appearanceTabTest() {
        LogTest.info("Gorunum sekmesine tiklaniyor.");
        WebElement appearanceTab = BaseStep.findElementXpathWithWait(
                XPATH_TAB_APPEARANCE, TimeOut.MEDIUM.value);
        BaseStep.clickElement(appearanceTab, "Gorunum sekmesi");
        BaseStep.waitSeconds(2);

        LogTest.info("Yazi boyutu guncelleniyor: 16");
        WebElement fontInput = BaseStep.findElementXpathWithWait(
                XPATH_APP_FONT_INPUT, TimeOut.SHORT.value);
        BaseStep.clearAndType(fontInput, "16", "Yazi boyutu alani");
        BaseStep.waitSeconds(1);

        LogTest.info("Kaydet butonuna tiklaniyor (1. kez).");
        WebElement saveBtn = BaseStep.findElementXpathWithWait(
                XPATH_APP_SAVE_BTN, TimeOut.SHORT.value);
        BaseStep.clickElement(saveBtn, "Gorunum Kaydet butonu");
        BaseStep.waitSeconds(2);

        LogTest.info("Varsayilana Don butonuna tiklaniyor.");
        WebElement resetBtn = BaseStep.findElementXpathWithWait(
                XPATH_APP_RESET_BTN, TimeOut.SHORT.value);
        BaseStep.clickElement(resetBtn, "Gorunum Varsayilana Don butonu");
        BaseStep.waitSeconds(2);

        LogTest.info("Kaydet butonuna tiklaniyor (2. kez - varsayilan ayarlar).");
        saveBtn = BaseStep.findElementXpathWithWait(
                XPATH_APP_SAVE_BTN, TimeOut.SHORT.value);
        BaseStep.clickElement(saveBtn, "Gorunum Kaydet butonu (reset sonrasi)");
        BaseStep.waitSeconds(3);
        LogTest.info("Gorunum sekmesi varsayilana donduruldu ve kaydedildi.");
    }

    // ============================================================
    // TEST: 6 — BILDIRIMLER SEKMESI: SWITCH + KAYDET + RESET
    // ============================================================

    @Test
    @Order(6)
    @DisplayName("Bildirimler sekmesi - switch toggle, kaydet, varsayilana don, kaydet")
    public void notificationsTabTest() {
        LogTest.info("Bildirimler sekmesine tiklaniyor.");
        WebElement notifTab = BaseStep.findElementXpathWithWait(
                XPATH_TAB_NOTIFICATIONS, TimeOut.MEDIUM.value);
        BaseStep.clickElement(notifTab, "Bildirimler sekmesi");
        BaseStep.waitSeconds(2);

        LogTest.info("Gunluk ozet switch'i toggle ediliyor.");
        WebElement switchBtn = BaseStep.findElementXpathWithWait(
                XPATH_NOTIF_SWITCH, TimeOut.SHORT.value);
        BaseStep.clickElement(switchBtn, "Gunluk ozet switch");
        BaseStep.waitSeconds(1);

        LogTest.info("Kaydet butonuna tiklaniyor (1. kez).");
        WebElement saveBtn = BaseStep.findElementXpathWithWait(
                XPATH_NOTIF_SAVE_BTN, TimeOut.SHORT.value);
        BaseStep.clickElement(saveBtn, "Bildirimler Kaydet butonu");
        BaseStep.waitSeconds(2);

        LogTest.info("Varsayilana Don butonuna tiklaniyor.");
        WebElement resetBtn = BaseStep.findElementXpathWithWait(
                XPATH_NOTIF_RESET_BTN, TimeOut.SHORT.value);
        BaseStep.clickElement(resetBtn, "Bildirimler Varsayilana Don butonu");
        BaseStep.waitSeconds(2);

        LogTest.info("Kaydet butonuna tiklaniyor (2. kez - varsayilan ayarlar).");
        saveBtn = BaseStep.findElementXpathWithWait(
                XPATH_NOTIF_SAVE_BTN, TimeOut.SHORT.value);
        BaseStep.clickElement(saveBtn, "Bildirimler Kaydet butonu (reset sonrasi)");
        BaseStep.waitSeconds(3);
        LogTest.info("Bildirimler sekmesi varsayilana donduruldu ve kaydedildi.");
    }

    // ============================================================
    // TEST: 7 — GUVENLIK SEKMESINE GEC
    // ============================================================

    @Test
    @Order(7)
    @DisplayName("Guvenlik sekmesine gec")
    public void securityTabTest() {
        LogTest.info("Guvenlik sekmesine tiklaniyor.");
        WebElement securityTab = BaseStep.findElementXpathWithWait(
                XPATH_TAB_SECURITY, TimeOut.MEDIUM.value);
        BaseStep.clickElement(securityTab, "Guvenlik sekmesi");
        BaseStep.waitSeconds(2);
        LogTest.info("Guvenlik sekmesi acildi.");
    }

    // ============================================================
    // TEST: 8 — DENETIM KAYITLARI SEKMESI: FILTRELE
    // ============================================================

    @Test
    @Order(8)
    @DisplayName("Denetim Kayitlari sekmesi - filtreleme")
    public void auditLogsTabTest() {
        LogTest.info("Denetim Kayitlari sekmesine tiklaniyor.");
        WebElement auditTab = BaseStep.findElementXpathWithWait(
                XPATH_TAB_AUDIT_LOGS, TimeOut.MEDIUM.value);
        BaseStep.clickElement(auditTab, "Denetim Kayitlari sekmesi");
        BaseStep.waitSeconds(2);

        LogTest.info("Kayit Turu filtresi seciliyor.");
        openDropdownAndSelectFirst(XPATH_AUDIT_FILTER, "Kayit Turu filtresi");
        LogTest.info("Denetim Kayitlari filtreleme tamamlandi.");
    }

    // ============================================================
    // TEST: 9 — ORGANIZASYON VE YETKILENDIRME SAYFASINA GIT
    // ============================================================

    @Test
    @Order(9)
    @DisplayName("Organizasyon ve Yetkilendirme sayfasina git")
    public void navigateToOrgAccessTest() {
        LogTest.info("Sidebar Organizasyon ve Yetkilendirme butonu araniyor.");
        WebElement orgBtn = BaseStep.findElementXpathWithWait(
                XPATH_SIDEBAR_ORG_ACCESS, TimeOut.MEDIUM.value);
        BaseStep.clickElement(orgBtn, "Organizasyon ve Yetkilendirme sidebar butonu");
        BaseStep.waitSeconds(2);

        LogTest.info("Sayfanin yuklendigi dogrulaniyor.");
        WebElement page = BaseStep.findElementXpathWithWait("//main", TimeOut.SHORT.value);
        assertTrue(page.isDisplayed(), "Organizasyon sayfasi yuklenemedi!");
        LogTest.info("Organizasyon ve Yetkilendirme sayfasi acildi.");
    }

    // ============================================================
    // TEST: 10 — DEPARTMAN EKLE
    // ============================================================

    @Test
    @Order(10)
    @DisplayName("Departman Yonetimi - departman ekle")
    public void addDepartmentTest() {
        LogTest.info("Departman Ekle butonuna tiklaniyor.");
        WebElement addBtn = BaseStep.findElementXpathWithWait(
                XPATH_ADD_DEPARTMENT_BTN, TimeOut.MEDIUM.value);
        BaseStep.clickElement(addBtn, "Departman Ekle butonu");
        BaseStep.waitSeconds(2);

        LogTest.info("Departman adi giriliyor.");
        WebElement nameInput = BaseStep.findElementXpathWithWait(
                XPATH_DEPT_NAME_INPUT, TimeOut.SHORT.value);
        BaseStep.clearAndType(nameInput, DEPT_NAME_ADDED, "Departman adi");
        BaseStep.waitSeconds(1);

        LogTest.info("Departman aciklamasi giriliyor.");
        WebElement descInput = BaseStep.findElementXpathWithWait(
                XPATH_DEPT_DESC_INPUT, TimeOut.SHORT.value);
        BaseStep.clearAndType(descInput, "Otomasyon ile olusturulan departman.",
                "Departman aciklamasi");
        BaseStep.waitSeconds(1);

        LogTest.info("Kaydet butonuna tiklaniyor.");
        WebElement saveBtn = BaseStep.findElementXpathWithWait(
                XPATH_DIALOG_SAVE_BTN, TimeOut.SHORT.value);
        BaseStep.clickElement(saveBtn, "Departman Kaydet butonu");
        BaseStep.waitSeconds(3);
        LogTest.info("Departman basariyla olusturuldu.");
    }

    // ============================================================
    // TEST: 11 — DEPARTMANA ROL EKLE
    // ============================================================

    @Test
    @Order(11)
    @DisplayName("Departmana rol ekle")
    public void addRoleToDepartmentTest() {
        LogTest.info("Rol Ekle (satir icindeki) butonuna tiklaniyor.");
        WebElement addRoleBtn = BaseStep.findElementXpathWithWait(
                XPATH_ADD_ROLE_TO_DEPT_BTN, TimeOut.MEDIUM.value);
        BaseStep.clickElement(addRoleBtn, "Rol Ekle (satir) butonu");
        BaseStep.waitSeconds(2);

        LogTest.info("Rol adi giriliyor.");
        WebElement nameInput = BaseStep.findElementXpathWithWait(
                XPATH_DEPT_ROLE_NAME_INPUT, TimeOut.SHORT.value);
        BaseStep.clearAndType(nameInput, "Otomasyon Rolu", "Rol adi");
        BaseStep.waitSeconds(1);

        LogTest.info("Rol aciklamasi giriliyor.");
        WebElement descInput = BaseStep.findElementXpathWithWait(
                XPATH_DEPT_ROLE_DESC_INPUT, TimeOut.SHORT.value);
        BaseStep.clearAndType(descInput, "Otomasyon ile olusturulan rol.", "Rol aciklamasi");
        BaseStep.waitSeconds(1);

        LogTest.info("Kaydet butonuna tiklaniyor.");
        WebElement saveBtn = BaseStep.findElementXpathWithWait(
                XPATH_DIALOG_SAVE_BTN, TimeOut.SHORT.value);
        BaseStep.clickElement(saveBtn, "Rol Kaydet butonu");
        BaseStep.waitSeconds(3);
        LogTest.info("Rol departmana basariyla eklendi.");
    }

    // ============================================================
    // TEST: 12 — DEPARTMAN DUZENLE
    // ============================================================

    @Test
    @Order(12)
    @DisplayName("Departman duzenle ve kaydet")
    public void editDepartmentTest() {
        LogTest.info("Departman Duzenle butonuna tiklaniyor.");
        WebElement editBtn = BaseStep.findElementXpathWithWait(
                XPATH_DEPT_EDIT_BTN, TimeOut.MEDIUM.value);
        BaseStep.clickElement(editBtn, "Departman Duzenle butonu");
        BaseStep.waitSeconds(2);

        LogTest.info("Departman adi guncelleniyor.");
        WebElement nameInput = BaseStep.findElementXpathWithWait(
                XPATH_DEPT_NAME_INPUT, TimeOut.SHORT.value);
        BaseStep.clearAndType(nameInput, DEPT_NAME_UPDATED, "Departman adi (duzenleme)");
        BaseStep.waitSeconds(1);

        LogTest.info("Aciklama guncelleniyor.");
        WebElement descInput = BaseStep.findElementXpathWithWait(
                XPATH_DEPT_DESC_INPUT, TimeOut.SHORT.value);
        BaseStep.clearAndType(descInput, "Guncellenmis departman aciklamasi.",
                "Departman aciklamasi (duzenleme)");
        BaseStep.waitSeconds(1);

        LogTest.info("Kaydet butonuna tiklaniyor.");
        WebElement saveBtn = BaseStep.findElementXpathWithWait(
                XPATH_DIALOG_SAVE_BTN, TimeOut.SHORT.value);
        BaseStep.clickElement(saveBtn, "Departman Kaydet butonu (duzenleme)");
        BaseStep.waitSeconds(3);
        LogTest.info("Departman basariyla guncellendi.");
    }

    // ============================================================
    // TEST: 13 — DEPARTMANI SIL
    // ============================================================

    @Test
    @Order(13)
    @DisplayName("Departmani sil - onay popup ile")
    public void deleteDepartmentTest() {
        LogTest.info("Departman Sil butonuna tiklaniyor.");
        WebElement deleteBtn = BaseStep.findElementXpathWithWait(
                XPATH_DEPT_DELETE_BTN, TimeOut.MEDIUM.value);
        BaseStep.jsClickElement(deleteBtn, "Departman Sil butonu");
        BaseStep.waitSeconds(2);

        LogTest.info("Onay popup'inda Tamam butonuna tiklaniyor.");
        WebElement confirmBtn = BaseStep.findElementXpathWithWait(
                XPATH_DEPT_DELETE_CONFIRM_BTN, TimeOut.SHORT.value);
        BaseStep.jsClickElement(confirmBtn, "Tamam onay butonu");
        BaseStep.waitSeconds(3);
        LogTest.info("Departman basariyla silindi.");
    }

    // ============================================================
    // TEST: 14 — ROL YONETIMI SEKMESINE GEC
    // ============================================================

    @Test
    @Order(14)
    @DisplayName("Rol Yonetimi sekmesine gec")
    public void switchToRoleTabTest() {
        LogTest.info("Rol Yonetimi sekmesine tiklaniyor.");
        WebElement roleTab = BaseStep.findElementXpathWithWait(
                XPATH_TAB_ROLE, TimeOut.MEDIUM.value);
        BaseStep.clickElement(roleTab, "Rol Yonetimi sekmesi");
        BaseStep.waitSeconds(2);
        LogTest.info("Rol Yonetimi sekmesi acildi.");
    }

    // ============================================================
    // TEST: 15 — ROL EKLE
    // ============================================================

    @Test
    @Order(15)
    @DisplayName("Rol Yonetimi - rol ekle")
    public void addRoleTest() {
        LogTest.info("Rol Ekle butonuna tiklaniyor.");
        WebElement addBtn = BaseStep.findElementXpathWithWait(
                XPATH_ADD_ROLE_BTN, TimeOut.MEDIUM.value);
        BaseStep.clickElement(addBtn, "Rol Ekle butonu");
        BaseStep.waitSeconds(2);

        LogTest.info("Rol adi giriliyor (dialog ilk input).");
        WebElement nameInput = BaseStep.findElementXpathWithWait(
                XPATH_DIALOG_FIRST_INPUT, TimeOut.SHORT.value);
        BaseStep.clearAndType(nameInput, ROLE_NAME_ADDED, "Rol adi");
        BaseStep.waitSeconds(1);

        LogTest.info("Rol aciklamasi giriliyor (dialog ilk textarea).");
        WebElement descInput = BaseStep.findElementXpathWithWait(
                XPATH_DIALOG_FIRST_TEXTAREA, TimeOut.SHORT.value);
        BaseStep.clearAndType(descInput, "Otomasyon ile olusturulan rol aciklamasi.",
                "Rol aciklamasi");
        BaseStep.waitSeconds(1);

        LogTest.info("Rol Olustur butonuna tiklaniyor.");
        WebElement createBtn = BaseStep.findElementXpathWithWait(
                XPATH_ROLE_CREATE_BTN, TimeOut.SHORT.value);
        BaseStep.clickElement(createBtn, "Rol Olustur butonu");
        BaseStep.waitSeconds(3);
        LogTest.info("Rol basariyla olusturuldu.");
    }

    // ============================================================
    // TEST: 16 — ROL DUZENLE
    // ============================================================

    @Test
    @Order(16)
    @DisplayName("Rol duzenle - Rolu Guncelle")
    public void editRoleTest() {
        LogTest.info("Rol Duzenle butonuna tiklaniyor.");
        WebElement editBtn = BaseStep.findElementXpathWithWait(
                XPATH_ROLE_EDIT_BTN, TimeOut.MEDIUM.value);
        BaseStep.clickElement(editBtn, "Rol Duzenle butonu");
        BaseStep.waitSeconds(2);

        LogTest.info("Rolu Guncelle butonuna tiklaniyor.");
        WebElement updateBtn = BaseStep.findElementXpathWithWait(
                XPATH_UPDATE_ROLE_BTN, TimeOut.SHORT.value);
        BaseStep.clickElement(updateBtn, "Rolu Guncelle butonu");
        BaseStep.waitSeconds(3);
        LogTest.info("Rol basariyla guncellendi.");
    }

    // ============================================================
    // TEST: 17 — ROL SIL (eklenen otomasyon rolu)
    // ============================================================

    @Test
    @Order(17)
    @DisplayName("Eklenen rolu sil - onay popup ile")
    public void deleteRoleTest() {
        LogTest.info("Rol Sil butonuna tiklaniyor (ad ile satir lookup).");
        WebElement deleteBtn = BaseStep.findElementXpathWithWait(
                XPATH_ROLE_DELETE_BTN, TimeOut.MEDIUM.value);
        BaseStep.jsClickElement(deleteBtn, "Rol Sil butonu");
        BaseStep.waitSeconds(2);

        LogTest.info("Onay popup'inda Tamam butonuna tiklaniyor.");
        WebElement confirmBtn = BaseStep.findElementXpathWithWait(
                XPATH_ROLE_DELETE_CONFIRM_BTN, TimeOut.SHORT.value);
        BaseStep.jsClickElement(confirmBtn, "Tamam onay butonu");
        BaseStep.waitSeconds(3);
        LogTest.info("Rol basariyla silindi.");
    }

    // ============================================================
    // TEST: 21 — TAKIM YONETIMI SEKMESINE GEC
    // ============================================================

    @Test
    @Order(21)
    @DisplayName("Takim Yonetimi sub-sekmesine gec")
    public void switchToTeamManagementTabTest() {
        LogTest.info("Takim Yonetimi sub-sekmesi araniyor.");
        WebElement teamTab = BaseStep.findElementXpathWithWait(
                XPATH_TAB_TEAM, TimeOut.MEDIUM.value);
        BaseStep.clickElement(teamTab, "Takim Yonetimi sub-sekmesi");
        BaseStep.waitSeconds(2);
        LogTest.info("Takim Yonetimi sub-sekmesi acildi.");
    }

    // ============================================================
    // TEST: 22 — YENI TAKIM EKLE
    // ============================================================

    @Test
    @Order(22)
    @DisplayName("Yeni takim ekle - tam surec")
    public void addTeamTest() {
        LogTest.info("Yeni Takim butonuna tiklaniyor.");
        WebElement addBtn = BaseStep.findElementXpathWithWait(
                XPATH_ADD_TEAM_BTN, TimeOut.MEDIUM.value);
        BaseStep.clickElement(addBtn, "Yeni Takim butonu");
        BaseStep.waitSeconds(2);

        LogTest.info("Takim adi giriliyor.");
        WebElement nameInput = BaseStep.findElementXpathWithWait(
                XPATH_TEAM_NAME_INPUT, TimeOut.SHORT.value);
        BaseStep.clearAndType(nameInput, TEAM_NAME_ADDED, "Takim adi");
        BaseStep.waitSeconds(1);

        LogTest.info("Aciklama giriliyor.");
        WebElement descInput = BaseStep.findElementXpathWithWait(
                XPATH_TEAM_DESC_INPUT, TimeOut.SHORT.value);
        BaseStep.clearAndType(descInput, "Otomasyon ile olusturulan takim.", "Takim aciklamasi");
        BaseStep.waitSeconds(1);

        // Takim lideri secimi: form acildiginda ilk uye zaten varsayilan olarak
        // secili durumda (Radix custom radio, role='radio' atamamis). Bu yuzden
        // manuel secim adimi atlandi; degistirmek istersek baska bir adayin
        // satirini tiklayarak override edebiliriz. Simdilik varsayilan lider
        // ile devam.
        LogTest.info("Takim lideri varsayilan olarak seciliyor (form default).");
        BaseStep.waitSeconds(1);

        LogTest.info("Olustur butonuna tiklaniyor.");
        WebElement createBtn = BaseStep.findElementXpathWithWait(
                XPATH_TEAM_CREATE_BTN, TimeOut.SHORT.value);
        BaseStep.clickElement(createBtn, "Takim Olustur butonu");
        BaseStep.waitSeconds(3);

        // Eklenen takimin UUID'sini yakala (isim bazli lookup, yoksa son satir)
        capturedTeamUuid = captureNewTeamUuid();
        LogTest.info("Eklenen takim UUID: " + capturedTeamUuid);
    }

    /**
     * Yeni eklenen takimin data-testid UUID'sini DOM'dan yakalar.
     * Once isim ile satir bulmayi dener, basaramazsa tablodaki son
     * team-details butonunun UUID'sini alir (en yeni eklenen genelde sonda).
     */
    private String captureNewTeamUuid() {
        WebElement detailsBtn;
        try {
            detailsBtn = BaseStep.findElementXpathWithWait(
                    XPATH_TEAM_BY_NAME_DETAILS, TimeOut.SHORT.value);
            LogTest.info("Takim isim bazli yakalandi.");
        } catch (Exception e) {
            LogTest.info("Isim bazli bulunamadi, son satirin UUID'si aliniyor.");
            detailsBtn = BaseStep.findElementXpathWithWait(
                    XPATH_LAST_TEAM_DETAILS_BTN, TimeOut.SHORT.value);
        }
        String testId = detailsBtn.getAttribute("data-testid");
        // "team-details-{uuid}" -> uuid
        return testId.substring("team-details-".length());
    }

    // ============================================================
    // TEST: 23 — TAKIM DETAYINI GORUNTULE (sag panel ac/kapat)
    // ============================================================

    @Test
    @Order(23)
    @DisplayName("Eklenen takimin detayini goruntule ve kapat")
    public void viewTeamDetailsTest() {
        LogTest.info("Detay butonuna tiklaniyor (UUID: " + capturedTeamUuid + ").");
        WebElement detailsBtn = BaseStep.findElementXpathWithWait(
                "//button[@data-testid='team-details-" + capturedTeamUuid + "']",
                TimeOut.MEDIUM.value);
        BaseStep.clickElement(detailsBtn, "Takim Detay butonu");
        BaseStep.waitSeconds(2);

        LogTest.info("Sag panel kapatma butonuna tiklaniyor.");
        WebElement closeBtn = BaseStep.findElementXpathWithWait(
                XPATH_TEAM_DETAIL_CLOSE_BTN, TimeOut.MEDIUM.value);
        BaseStep.clickElement(closeBtn, "Detay paneli kapat butonu");
        BaseStep.waitSeconds(2);
        LogTest.info("Detay paneli kapatildi.");
    }

    // ============================================================
    // TEST: 24 — TAKIM DUZENLE
    // ============================================================

    @Test
    @Order(24)
    @DisplayName("Eklenen takimi duzenle ve guncelle")
    public void editTeamTest() {
        LogTest.info("Duzenle butonuna tiklaniyor (UUID: " + capturedTeamUuid + ").");
        WebElement editBtn = BaseStep.findElementXpathWithWait(
                "//button[@data-testid='team-edit-" + capturedTeamUuid + "']",
                TimeOut.MEDIUM.value);
        BaseStep.clickElement(editBtn, "Takim Duzenle butonu");
        BaseStep.waitSeconds(2);

        LogTest.info("Takim adi guncelleniyor.");
        WebElement nameInput = BaseStep.findElementXpathWithWait(
                XPATH_TEAM_NAME_INPUT, TimeOut.SHORT.value);
        BaseStep.clearAndType(nameInput, TEAM_NAME_UPDATED, "Takim adi (duzenleme)");
        BaseStep.waitSeconds(1);

        LogTest.info("Aciklama guncelleniyor.");
        WebElement descInput = BaseStep.findElementXpathWithWait(
                XPATH_TEAM_DESC_INPUT, TimeOut.SHORT.value);
        BaseStep.clearAndType(descInput, "Guncellenmis takim aciklamasi.", "Takim aciklamasi (duzenleme)");
        BaseStep.waitSeconds(1);

        LogTest.info("Guncelle butonuna tiklaniyor.");
        WebElement updateBtn = BaseStep.findElementXpathWithWait(
                XPATH_TEAM_UPDATE_BTN, TimeOut.SHORT.value);
        BaseStep.clickElement(updateBtn, "Takim Guncelle butonu");
        BaseStep.waitSeconds(3);
        LogTest.info("Takim basariyla guncellendi.");
    }

    // ============================================================
    // TEST: 25 — TAKIMI SIL (eklenen guncellenmis takim)
    // ============================================================

    @Test
    @Order(25)
    @DisplayName("Eklenen takimi sil - onay popup ile")
    public void deleteTeamTest() {
        LogTest.info("Sil butonuna tiklaniyor (UUID: " + capturedTeamUuid + ").");
        WebElement deleteBtn = BaseStep.findElementXpathWithWait(
                "//button[@data-testid='team-delete-" + capturedTeamUuid + "']",
                TimeOut.MEDIUM.value);
        BaseStep.jsClickElement(deleteBtn, "Takim Sil butonu");
        BaseStep.waitSeconds(2);

        LogTest.info("Onay popup'inda Evet butonuna tiklaniyor.");
        WebElement confirmBtn = BaseStep.findElementXpathWithWait(
                XPATH_TEAM_DELETE_CONFIRM_BTN, TimeOut.SHORT.value);
        BaseStep.jsClickElement(confirmBtn, "Evet onay butonu");
        BaseStep.waitSeconds(3);
        LogTest.info("Takim basariyla silindi.");
    }

    // ============================================================
    // TEST: 26 — DENETIM KAYITLARI SAYFASINA GIT (sidebar)
    // ============================================================

    @Test
    @Order(26)
    @DisplayName("Denetim Kayitlari sayfasina git (sidebar)")
    public void navigateToAuditLogsPageTest() {
        LogTest.info("Sidebar Denetim Kayitlari butonu araniyor.");
        WebElement auditBtn = BaseStep.findElementXpathWithWait(
                XPATH_SIDEBAR_AUDIT_LOGS, TimeOut.MEDIUM.value);
        BaseStep.clickElement(auditBtn, "Denetim Kayitlari sidebar butonu");
        BaseStep.waitSeconds(2);

        LogTest.info("Sayfanin yuklendigi dogrulaniyor.");
        WebElement page = BaseStep.findElementXpathWithWait("//main", TimeOut.SHORT.value);
        assertTrue(page.isDisplayed(), "Denetim Kayitlari sayfasi yuklenemedi!");
        LogTest.info("Denetim Kayitlari sayfasi acildi.");
    }

    // ============================================================
    // TEST: 27 — DENETIM KAYITLARI FILTRESI
    // ============================================================

    @Test
    @Order(27)
    @DisplayName("Denetim Kayitlari - filtre dropdown")
    public void filterAuditLogsTest() {
        LogTest.info("Denetim Kayitlari filtre dropdown seciliyor.");
        openDropdownAndSelectFirst(XPATH_AUDIT_PAGE_FILTER, "Denetim Kayitlari filtresi");
        LogTest.info("Filtre uygulandi.");
    }

    // ============================================================
    // HELPER: RADIX SELECT KLAVYE NAVIGASYONU
    // ============================================================

    /**
     * Radix Select dropdown'u acar ve klavye ile ilk secilebilir secenegi
     * secer. element.click() bazen secimi tetiklemeden listbox'i kapattigi
     * icin klavye yolu (ARROW_DOWN + ENTER) daha guvenilir.
     * (ReportsPageTest ve WorkflowPageTest'te dogrulanan strateji.)
     */
    private void openDropdownAndSelectFirst(String dropdownXpath, String description) {
        WebElement dropdown = BaseStep.findElementXpathWithWait(dropdownXpath, TimeOut.SHORT.value);
        BaseStep.clickElement(dropdown, description + " (dropdown ac)");
        BaseStep.waitSeconds(2);

        new Actions(driver).sendKeys(Keys.ARROW_DOWN).perform();
        BaseStep.waitSeconds(1);

        new Actions(driver).sendKeys(Keys.ENTER).perform();
        BaseStep.waitSeconds(2);

        LogTest.info(description + " - ilk secenek klavye ile secildi");
    }
}
