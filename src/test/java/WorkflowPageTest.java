import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class WorkflowPageTest extends BaseStep {

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

    // Sidebar — data-menu-key tabanli (kararli)
    private static final String XPATH_SIDEBAR_WORKFLOW_PARENT = "//button[@data-menu-key='workflow-submenu']";
    private static final String XPATH_SIDEBAR_FLOW_HEALTH     = "//button[@data-menu-key='/workflow-analytics']";

    // Is Akisi ana sayfasindaki proje dropdown (pozisyonel - main icindeki ilk combobox)
    private static final String XPATH_PROJECT_DROPDOWN        = "//main//button[@role='combobox']";

    // Akis Sagligi sayfasindaki dropdownlar (main icindeki sirali)
    private static final String XPATH_WORKFLOW_DROPDOWN       = "(//main//button[@role='combobox'])[1]";
    private static final String XPATH_PERIOD_DROPDOWN         = "(//main//button[@role='combobox'])[2]";

    // CSV export butonu
    private static final String XPATH_CSV_EXPORT_BTN          = "//main//button[normalize-space(.)='CSV Olarak Dışa Aktar']";

    // Tum Is Akislari sayfasi
    private static final String XPATH_SIDEBAR_ALL_WORKFLOWS   = "//button[@data-menu-key='/workflows']";
    private static final String XPATH_AW_FILTER_1             = "(//main//button[@role='combobox'])[1]";
    private static final String XPATH_AW_FILTER_2             = "(//main//button[@role='combobox'])[2]";
    private static final String XPATH_AW_ACTIVE_SWITCH        = "//main//button[@role='switch']";
    private static final String XPATH_AW_IMPORT_TEMPLATE_BTN  = "//main//button[normalize-space(.)='Şablondan İçe Aktar']";

    // Sablondan Ice Aktar popup (kararli id'ler)
    private static final String XPATH_IMPORT_ENTITY_SELECT    = "//*[@id='workflow-template-import-entity']";
    private static final String XPATH_IMPORT_TEMPLATE_SELECT  = "//*[@id='workflow-template-import-template']";
    private static final String XPATH_IMPORT_PROJECT_SELECT   = "//*[@id='workflow-template-import-project']";
    private static final String XPATH_APPLY_TO_PROJECT_BTN    = "//div[@role='dialog']//button[normalize-space(.)='Projeye Uygula']";

    // Designer sayfasi - 3 ana buton (id'lerdeki -content-designer suffix'i kararli)
    private static final String XPATH_DESIGNER_SAVE_BTN       = "//*[contains(@id,'-content-designer')]//button[normalize-space(.)='Kaydet']";
    private static final String XPATH_DESIGNER_VALIDATE_BTN   = "//*[contains(@id,'-content-designer')]//button[normalize-space(.)='Doğrula']";
    private static final String XPATH_DESIGNER_PUBLISH_BTN    = "//*[contains(@id,'-content-designer')]//button[normalize-space(.)='Yayınla']";
    private static final String XPATH_PUBLISH_CONFIRM_BTN     = "(//div[@role='dialog'])[last()]//button[normalize-space(.)='Yayınla']";

    // Kurallar sekmesi
    private static final String XPATH_TAB_RULES               = "//button[@role='tab' and normalize-space(.)='Kurallar']";
    private static final String XPATH_ADD_ASSIGNMENT_RULE_BTN = "//button[normalize-space(.)='Atama Kuralı Ekle']";

    // Atama Kurali popup (kararli id'ler)
    private static final String XPATH_RULE_NAME_INPUT         = "//*[@id='assignment-rule-name']";
    private static final String XPATH_RULE_ENTITY_SELECT      = "//div[@role='dialog']//button[@role='combobox']";
    private static final String XPATH_RULE_DESC_INPUT         = "//*[@id='assignment-rule-description']";
    private static final String XPATH_RULE_SAVE_BTN           = "(//div[@role='dialog'])[last()]//button[normalize-space(.)='Kaydet']";

    // Listede islem butonlari (Atama Kurallari)
    private static final String XPATH_RULE_EDIT_BTN           = "(//button[@aria-label='Atama Kuralını Düzenle'])[1]";
    private static final String XPATH_RULE_DELETE_BTN         = "(//button[@aria-label='Sil'])[1]";
    private static final String XPATH_DELETE_CONFIRM_BTN      = "(//*[@role='dialog' or @role='alertdialog'])[last()]//button[normalize-space(.)='Tamam']";

    // Is Kurallari sub-sekmesi
    private static final String XPATH_SUBTAB_BUSINESS_RULES   = "//button[@role='tab' and normalize-space(.)='İş Kuralları']";
    private static final String XPATH_ADD_BUSINESS_RULE_BTN   = "//button[normalize-space(.)='İş Kuralı Ekle']";

    // Is Kurali popup (kararli id'ler)
    private static final String XPATH_BR_NAME_INPUT           = "//*[@id='business-rule-name']";
    // Popup'ta 2 dropdown var: [1]=Kayit Turu, [2]=Tetikleyici
    private static final String XPATH_BR_ENTITY_SELECT        = "(//div[@role='dialog']//button[@role='combobox'])[1]";
    private static final String XPATH_BR_TRIGGER_SELECT       = "(//div[@role='dialog']//button[@role='combobox'])[2]";
    private static final String XPATH_BR_DESC_INPUT           = "//*[@id='business-rule-description']";
    private static final String XPATH_BR_SAVE_BTN             = "(//div[@role='dialog'])[last()]//button[normalize-space(.)='Kaydet']";

    // Is Kurallari listede islem butonlari (business rules tab scope)
    private static final String XPATH_BR_EDIT_BTN             = "(//*[contains(@id,'-content-business')]//span[@data-slot='tooltip-trigger']/button)[1]";
    private static final String XPATH_BR_DELETE_BTN           = "(//*[contains(@id,'-content-business')]//button[@aria-label='Sil'])[1]";
    private static final String XPATH_BR_DELETE_CONFIRM_BTN   = "(//*[@role='dialog' or @role='alertdialog'])[last()]//button[normalize-space(.)='Sil']";

    // Eskalasyon Kurallari sub-sekmesi
    private static final String XPATH_SUBTAB_ESCALATION       = "//button[@role='tab' and normalize-space(.)='Eskalasyon Kuralları']";
    private static final String XPATH_ADD_ESCALATION_RULE_BTN = "//button[normalize-space(.)='Eskalasyon Kuralı Ekle']";

    // Eskalasyon Kurali popup (kararli id'ler)
    private static final String XPATH_ESC_NAME_INPUT          = "//*[@id='escalation-rule-name']";
    private static final String XPATH_ESC_ENTITY_SELECT       = "//div[@role='dialog']//button[@role='combobox']";
    private static final String XPATH_ESC_DESC_INPUT          = "//*[@id='escalation-rule-description']";
    private static final String XPATH_ESC_SAVE_BTN            = "(//div[@role='dialog'])[last()]//button[normalize-space(.)='Kaydet']";

    // Listede islem butonlari (eskalasyon scope)
    private static final String XPATH_ESC_EDIT_BTN            = "(//button[@aria-label='Eskalasyon Kuralını Düzenle'])[1]";
    private static final String XPATH_ESC_DELETE_BTN          = "(//*[contains(@id,'-content-escalation')]//button[@aria-label='Sil'])[1]";
    private static final String XPATH_ESC_DELETE_CONFIRM_BTN  = "(//*[@role='dialog' or @role='alertdialog'])[last()]//button[normalize-space(.)='Tamam']";

    // Etki Analizi sekmesi
    private static final String XPATH_TAB_IMPACT              = "//button[@role='tab' and normalize-space(.)='Etki Analizi']";
    private static final String XPATH_IMPACT_DEPT_SELECT      = "(//*[contains(@id,'-content-impact')]//button[@role='combobox'])[1]";
    private static final String XPATH_IMPACT_ROLE_SELECT      = "(//*[contains(@id,'-content-impact')]//button[@role='combobox'])[2]";

    // Onizleme & Test sekmesi
    private static final String XPATH_TAB_PREVIEW             = "//button[@role='tab' and contains(normalize-space(.),'Önizleme')]";
    private static final String XPATH_PREVIEW_DROPDOWN_1      = "(//*[contains(@id,'-content-preview')]//button[@role='combobox'])[1]";
    private static final String XPATH_PREVIEW_DROPDOWN_2      = "(//*[contains(@id,'-content-preview')]//button[@role='combobox'])[2]";

    // Sablonlar sayfasi (sidebar alt menu)
    private static final String XPATH_SIDEBAR_TEMPLATES       = "//button[@data-menu-key='/workflow-templates']";
    private static final String XPATH_TEMPLATES_FILTER        = "//main//button[@role='combobox']";
    private static final String XPATH_TEMPLATE_VIEW_BTN       = "(//main//button[normalize-space(.)='Görüntüle'])[1]";
    private static final String XPATH_TEMPLATE_APPLY_BTN      = "(//main//button[normalize-space(.)='Projeye Uygula'])[1]";
    private static final String XPATH_VIEW_DIALOG_CLOSE_BTN   = "(//*[@role='dialog' or @role='alertdialog'])[last()]//button[normalize-space(.)='Kapat']";

    // Projeye Uygula popup
    private static final String XPATH_APPLY_TARGET_PROJECT    = "//div[@role='dialog']//button[@role='combobox']";
    private static final String XPATH_APPLY_SUBMIT_BTN        = "(//*[@role='dialog' or @role='alertdialog'])[last()]//button[normalize-space(.)='Projeye Uygula']";

    // Zamanlayicilar sayfasi (sidebar alt menu)
    private static final String XPATH_SIDEBAR_TIMERS          = "//button[@data-menu-key='/workflow-timers']";
    private static final String XPATH_TIMERS_PROJECT_FILTER   = "(//main//button[@role='combobox'])[1]";
    private static final String XPATH_TIMERS_ENTITY_FILTER    = "(//main//button[@role='combobox'])[2]";
    private static final String XPATH_PROCESS_DUE_BTN         = "//main//button[normalize-space(.)='Zamanı Gelenleri İşle']";

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
    // TEST: 2 — IS AKISI MENUSUNU AC
    // ============================================================

    @Test
    @Order(2)
    @DisplayName("Is Akisi sidebar menusunu ac")
    public void openWorkflowMenuTest() {
        LogTest.info("Sidebar Is Akisi parent butonu araniyor.");
        WebElement workflowParentBtn = BaseStep.findElementXpathWithWait(
                XPATH_SIDEBAR_WORKFLOW_PARENT, TimeOut.SHORT.value);
        assertTrue(workflowParentBtn.isDisplayed(), "Is Akisi parent butonu gorulmuyor!");
        BaseStep.clickElement(workflowParentBtn, "Is Akisi parent butonu");
        BaseStep.waitSeconds(2);
        LogTest.info("Is Akisi alt menusu acildi.");
    }

    // ============================================================
    // TEST: 3 — IS AKISI ANA SAYFA - PROJE SECIMI
    // ============================================================

    @Test
    @Order(3)
    @DisplayName("Is Akisi sayfasinda proje sec")
    public void selectProjectOnWorkflowPageTest() {
        LogTest.info("Is Akisi sayfasinin yuklendigi dogrulaniyor.");
        WebElement page = BaseStep.findElementXpathWithWait("//main", TimeOut.SHORT.value);
        assertTrue(page.isDisplayed(), "Is Akisi sayfasi yuklenemedi!");

        LogTest.info("Proje seciliyor.");
        openDropdownAndSelectFirst(XPATH_PROJECT_DROPDOWN, "Proje sec");
        LogTest.info("Proje secimi tamamlandi.");
    }

    // ============================================================
    // TEST: 4 — AKIS SAGLIGI SAYFASINA GIT
    // ============================================================

    @Test
    @Order(4)
    @DisplayName("Akis Sagligi alt sayfasina git")
    public void navigateToFlowHealthTest() {
        LogTest.info("Sidebar Akis Sagligi butonu araniyor.");
        WebElement flowHealthBtn = BaseStep.findElementXpathWithWait(
                XPATH_SIDEBAR_FLOW_HEALTH, TimeOut.MEDIUM.value);
        BaseStep.clickElement(flowHealthBtn, "Akis Sagligi butonu");
        BaseStep.waitSeconds(2);

        LogTest.info("Sayfanin yuklendigi dogrulaniyor.");
        WebElement page = BaseStep.findElementXpathWithWait("//main", TimeOut.SHORT.value);
        assertTrue(page.isDisplayed(), "Akis Sagligi sayfasi yuklenemedi!");
        LogTest.info("Akis Sagligi sayfasi acildi.");
    }

    // ============================================================
    // TEST: 5 — AKIS SAGLIGI - IS AKISI VE DONEM DROPDOWN'LARI
    // ============================================================

    @Test
    @Order(5)
    @DisplayName("Akis Sagligi - is akisi ve donem secimi")
    public void filterFlowHealthTest() {
        LogTest.info("Is akisi dropdown seciliyor.");
        openDropdownAndSelectFirst(XPATH_WORKFLOW_DROPDOWN, "Is Akisi");

        LogTest.info("Donem dropdown seciliyor.");
        openDropdownAndSelectFirst(XPATH_PERIOD_DROPDOWN, "Donem");
        LogTest.info("Akis Sagligi filtreleme tamamlandi.");
    }

    // ============================================================
    // TEST: 6 — CSV OLARAK DISA AKTAR
    // ============================================================

    @Test
    @Order(6)
    @DisplayName("CSV olarak disa aktar butonuna tikla")
    public void exportCsvTest() {
        LogTest.info("CSV Olarak Disa Aktar butonu araniyor.");
        WebElement csvBtn = BaseStep.findElementXpathWithWait(
                XPATH_CSV_EXPORT_BTN, TimeOut.MEDIUM.value);
        assertTrue(csvBtn.isDisplayed(), "CSV export butonu gorulmuyor!");
        BaseStep.clickElement(csvBtn, "CSV Olarak Disa Aktar butonu");
        BaseStep.waitSeconds(3);
        LogTest.info("CSV export tetiklendi.");
    }

    // ============================================================
    // TEST: 7 — TUM IS AKISLARI SAYFASINA GIT
    // ============================================================

    @Test
    @Order(7)
    @DisplayName("Tum Is Akislari sayfasina git")
    public void navigateToAllWorkflowsTest() {
        LogTest.info("Sidebar Tum Is Akislari butonu araniyor.");
        WebElement allWorkflowsBtn = BaseStep.findElementXpathWithWait(
                XPATH_SIDEBAR_ALL_WORKFLOWS, TimeOut.MEDIUM.value);
        BaseStep.clickElement(allWorkflowsBtn, "Tum Is Akislari butonu");
        BaseStep.waitSeconds(2);

        LogTest.info("Sayfanin yuklendigi dogrulaniyor.");
        WebElement page = BaseStep.findElementXpathWithWait("//main", TimeOut.SHORT.value);
        assertTrue(page.isDisplayed(), "Tum Is Akislari sayfasi yuklenemedi!");
        LogTest.info("Tum Is Akislari sayfasi acildi.");
    }

    // ============================================================
    // TEST: 8 — FILTRELEME (Hata + Proje dropdownlari)
    // ============================================================

    @Test
    @Order(8)
    @DisplayName("Tum Is Akislari - filtreleme dropdownlari")
    public void filterAllWorkflowsTest() {
        LogTest.info("1. filtre (Hata) dropdown seciliyor.");
        openDropdownAndSelectFirst(XPATH_AW_FILTER_1, "Hata filtresi");

        LogTest.info("2. filtre (Proje) dropdown seciliyor.");
        openDropdownAndSelectFirst(XPATH_AW_FILTER_2, "Proje filtresi");
        LogTest.info("Tum Is Akislari filtreleme tamamlandi.");
    }

    // ============================================================
    // TEST: 9 — SADECE AKTIF SWITCH'I TOGGLE
    // ============================================================

    @Test
    @Order(9)
    @DisplayName("Sadece Aktif switch butonunu toggle et")
    public void toggleActiveOnlySwitchTest() {
        LogTest.info("Sadece Aktif switch butonu araniyor.");
        WebElement switchBtn = BaseStep.findElementXpathWithWait(
                XPATH_AW_ACTIVE_SWITCH, TimeOut.SHORT.value);
        BaseStep.clickElement(switchBtn, "Sadece Aktif switch (1. tikla - kapat)");
        BaseStep.waitSeconds(2);

        LogTest.info("Switch tekrar tiklanarak eski haline dondurululuyor.");
        switchBtn = BaseStep.findElementXpathWithWait(
                XPATH_AW_ACTIVE_SWITCH, TimeOut.SHORT.value);
        BaseStep.clickElement(switchBtn, "Sadece Aktif switch (2. tikla - ac)");
        BaseStep.waitSeconds(2);
        LogTest.info("Switch toggle testi tamamlandi.");
    }

    // ============================================================
    // TEST: 10 — SABLONDAN ICE AKTAR (TAM SUREC)
    // ============================================================

    @Test
    @Order(10)
    @DisplayName("Sablondan ice aktar - popup ile projeye uygula")
    public void importFromTemplateTest() {
        LogTest.info("Sablondan Ice Aktar butonuna tiklaniyor.");
        WebElement importBtn = BaseStep.findElementXpathWithWait(
                XPATH_AW_IMPORT_TEMPLATE_BTN, TimeOut.MEDIUM.value);
        BaseStep.clickElement(importBtn, "Sablondan Ice Aktar butonu");
        BaseStep.waitSeconds(2);

        LogTest.info("Kayit Turu seciliyor.");
        openDropdownAndSelectFirst(XPATH_IMPORT_ENTITY_SELECT, "Kayit Turu");

        LogTest.info("Sablon seciliyor.");
        openDropdownAndSelectFirst(XPATH_IMPORT_TEMPLATE_SELECT, "Sablon");

        LogTest.info("Hedef Proje seciliyor.");
        openDropdownAndSelectFirst(XPATH_IMPORT_PROJECT_SELECT, "Hedef Proje");

        LogTest.info("Projeye Uygula butonuna tiklaniyor.");
        WebElement applyBtn = BaseStep.findElementXpathWithWait(
                XPATH_APPLY_TO_PROJECT_BTN, TimeOut.SHORT.value);
        BaseStep.clickElement(applyBtn, "Projeye Uygula butonu");
        BaseStep.waitSeconds(3);
        LogTest.info("Sablondan ice aktarma tamamlandi.");
    }

    // ============================================================
    // TEST: 11 — DESIGNER'DA KAYDET
    // ============================================================

    @Test
    @Order(11)
    @DisplayName("Designer sayfasi - Kaydet butonuna tikla")
    public void designerSaveTest() {
        LogTest.info("Designer Kaydet butonu araniyor.");
        WebElement saveBtn = BaseStep.findElementXpathWithWait(
                XPATH_DESIGNER_SAVE_BTN, TimeOut.MEDIUM.value);
        BaseStep.clickElement(saveBtn, "Designer Kaydet butonu");
        BaseStep.waitSeconds(2);
        LogTest.info("Designer kaydetme tetiklendi.");
    }

    // ============================================================
    // TEST: 12 — DESIGNER'DA DOGRULA
    // ============================================================

    @Test
    @Order(12)
    @DisplayName("Designer sayfasi - Dogrula butonuna tikla")
    public void designerValidateTest() {
        LogTest.info("Designer Dogrula butonu araniyor.");
        WebElement validateBtn = BaseStep.findElementXpathWithWait(
                XPATH_DESIGNER_VALIDATE_BTN, TimeOut.MEDIUM.value);
        BaseStep.clickElement(validateBtn, "Designer Dogrula butonu");
        BaseStep.waitSeconds(2);
        LogTest.info("Designer dogrulama tetiklendi.");
    }

    // ============================================================
    // TEST: 13 — DESIGNER'DA YAYINLA (onay popup ile)
    // ============================================================

    @Test
    @Order(13)
    @DisplayName("Designer sayfasi - Yayinla butonu ve onay popup")
    public void designerPublishTest() {
        LogTest.info("Designer Yayinla butonu araniyor.");
        WebElement publishBtn = BaseStep.findElementXpathWithWait(
                XPATH_DESIGNER_PUBLISH_BTN, TimeOut.MEDIUM.value);
        // Acik kalan toast bildirimi butonu kapatabilir; JS click ile engeli bypass et
        BaseStep.jsClickElement(publishBtn, "Designer Yayinla butonu");
        BaseStep.waitSeconds(2);

        LogTest.info("Onay popup'inda Yayinla butonuna tiklaniyor.");
        WebElement confirmBtn = BaseStep.findElementXpathWithWait(
                XPATH_PUBLISH_CONFIRM_BTN, TimeOut.SHORT.value);
        BaseStep.clickElement(confirmBtn, "Yayinla onay butonu");
        BaseStep.waitSeconds(3);
        LogTest.info("Yayinla islemi tamamlandi.");
    }

    // ============================================================
    // TEST: 14 — KURALLAR SEKMESINE GEC
    // ============================================================

    @Test
    @Order(14)
    @DisplayName("Kurallar sekmesine gec")
    public void switchToRulesTabTest() {
        LogTest.info("Kurallar sekmesi araniyor.");
        WebElement rulesTab = BaseStep.findElementXpathWithWait(
                XPATH_TAB_RULES, TimeOut.MEDIUM.value);
        // Radix sekmesi: focus+pointer event ile tikla, aria-selected ile dogrula
        clickRadixTab(rulesTab, "Kurallar sekmesi");
        BaseStep.waitSeconds(2);
        LogTest.info("Kurallar sekmesi acildi.");
    }

    // ============================================================
    // TEST: 15 — ATAMA KURALI EKLE
    // ============================================================

    @Test
    @Order(15)
    @DisplayName("Atama Kurali Ekle - tam surec")
    public void addAssignmentRuleTest() {
        LogTest.info("Atama Kurali Ekle butonuna tiklaniyor.");
        WebElement addBtn = BaseStep.findElementXpathWithWait(
                XPATH_ADD_ASSIGNMENT_RULE_BTN, TimeOut.MEDIUM.value);
        BaseStep.clickElement(addBtn, "Atama Kurali Ekle butonu");
        BaseStep.waitSeconds(2);

        LogTest.info("Kural adi giriliyor.");
        WebElement nameInput = BaseStep.findElementXpathWithWait(
                XPATH_RULE_NAME_INPUT, TimeOut.SHORT.value);
        BaseStep.clearAndType(nameInput, "Otomasyon Atama Kurali", "Kural adi");
        BaseStep.waitSeconds(1);

        LogTest.info("Kayit turu seciliyor.");
        openDropdownAndSelectFirst(XPATH_RULE_ENTITY_SELECT, "Kayit turu");

        LogTest.info("Aciklama giriliyor.");
        WebElement descInput = BaseStep.findElementXpathWithWait(
                XPATH_RULE_DESC_INPUT, TimeOut.SHORT.value);
        BaseStep.clearAndType(descInput, "Otomasyon ile olusturulan atama kurali aciklamasi.",
                "Kural aciklamasi");
        BaseStep.waitSeconds(1);

        LogTest.info("Kaydet butonuna tiklaniyor.");
        WebElement saveBtn = BaseStep.findElementXpathWithWait(
                XPATH_RULE_SAVE_BTN, TimeOut.SHORT.value);
        BaseStep.clickElement(saveBtn, "Atama Kurali Kaydet butonu");
        BaseStep.waitSeconds(3);
        LogTest.info("Atama kurali basariyla olusturuldu.");
    }

    // ============================================================
    // TEST: 16 — ATAMA KURALINI DUZENLE
    // ============================================================

    @Test
    @Order(16)
    @DisplayName("Atama kuralini duzenle ve kaydet")
    public void editAssignmentRuleTest() {
        LogTest.info("Atama Kuralini Duzenle butonuna tiklaniyor.");
        WebElement editBtn = BaseStep.findElementXpathWithWait(
                XPATH_RULE_EDIT_BTN, TimeOut.MEDIUM.value);
        BaseStep.clickElement(editBtn, "Atama Kuralini Duzenle butonu");
        BaseStep.waitSeconds(2);

        LogTest.info("Kural adi guncelleniyor.");
        WebElement nameInput = BaseStep.findElementXpathWithWait(
                XPATH_RULE_NAME_INPUT, TimeOut.SHORT.value);
        BaseStep.clearAndType(nameInput, "Guncellenmis Atama Kurali", "Kural adi (duzenleme)");
        BaseStep.waitSeconds(1);

        LogTest.info("Aciklama guncelleniyor.");
        WebElement descInput = BaseStep.findElementXpathWithWait(
                XPATH_RULE_DESC_INPUT, TimeOut.SHORT.value);
        BaseStep.clearAndType(descInput, "Guncellenmis atama kurali aciklamasi.",
                "Kural aciklamasi (duzenleme)");
        BaseStep.waitSeconds(1);

        LogTest.info("Kaydet butonuna tiklaniyor.");
        WebElement saveBtn = BaseStep.findElementXpathWithWait(
                XPATH_RULE_SAVE_BTN, TimeOut.SHORT.value);
        BaseStep.clickElement(saveBtn, "Atama Kurali Kaydet butonu (duzenleme)");
        BaseStep.waitSeconds(3);
        LogTest.info("Atama kurali basariyla guncellendi.");
    }

    // ============================================================
    // TEST: 17 — ATAMA KURALINI SIL
    // ============================================================

    @Test
    @Order(17)
    @DisplayName("Atama kuralini sil - onay popup ile")
    public void deleteAssignmentRuleTest() {
        LogTest.info("Sil butonuna tiklaniyor.");
        WebElement deleteBtn = BaseStep.findElementXpathWithWait(
                XPATH_RULE_DELETE_BTN, TimeOut.MEDIUM.value);
        BaseStep.jsClickElement(deleteBtn, "Sil butonu");
        BaseStep.waitSeconds(2);

        LogTest.info("Onay popup'inda Tamam butonuna tiklaniyor.");
        WebElement confirmBtn = BaseStep.findElementXpathWithWait(
                XPATH_DELETE_CONFIRM_BTN, TimeOut.SHORT.value);
        BaseStep.jsClickElement(confirmBtn, "Tamam onay butonu");
        BaseStep.waitSeconds(3);
        LogTest.info("Atama kurali basariyla silindi.");
    }

    // ============================================================
    // TEST: 18 — IS KURALLARI SUB-SEKMESINE GEC
    // ============================================================

    @Test
    @Order(18)
    @DisplayName("Is Kurallari sub-sekmesine gec")
    public void switchToBusinessRulesTabTest() {
        LogTest.info("Is Kurallari sub-sekmesi araniyor.");
        WebElement businessTab = BaseStep.findElementXpathWithWait(
                XPATH_SUBTAB_BUSINESS_RULES, TimeOut.MEDIUM.value);
        // Radix sekmesi: focus+pointer event ile tikla, aria-selected ile dogrula
        clickRadixTab(businessTab, "Is Kurallari sub-sekmesi");
        BaseStep.waitSeconds(2);
        LogTest.info("Is Kurallari sub-sekmesi acildi.");
    }

    // ============================================================
    // TEST: 19 — IS KURALI EKLE
    // ============================================================

    @Test
    @Order(19)
    @DisplayName("Is Kurali Ekle - tam surec")
    public void addBusinessRuleTest() {
        LogTest.info("Is Kurali Ekle butonuna tiklaniyor.");
        WebElement addBtn = BaseStep.findElementXpathWithWait(
                XPATH_ADD_BUSINESS_RULE_BTN, TimeOut.MEDIUM.value);
        BaseStep.clickElement(addBtn, "Is Kurali Ekle butonu");
        BaseStep.waitSeconds(2);

        LogTest.info("Kural adi giriliyor.");
        WebElement nameInput = BaseStep.findElementXpathWithWait(
                XPATH_BR_NAME_INPUT, TimeOut.SHORT.value);
        BaseStep.clearAndType(nameInput, "Otomasyon Is Kurali", "Kural adi");
        BaseStep.waitSeconds(1);

        LogTest.info("Kayit turu seciliyor.");
        openDropdownAndSelectFirst(XPATH_BR_ENTITY_SELECT, "Kayit turu");

        LogTest.info("Tetikleyici seciliyor.");
        openDropdownAndSelectFirst(XPATH_BR_TRIGGER_SELECT, "Tetikleyici");

        LogTest.info("Aciklama giriliyor.");
        WebElement descInput = BaseStep.findElementXpathWithWait(
                XPATH_BR_DESC_INPUT, TimeOut.SHORT.value);
        BaseStep.clearAndType(descInput, "Otomasyon ile olusturulan is kurali aciklamasi.",
                "Kural aciklamasi");
        BaseStep.waitSeconds(1);

        LogTest.info("Kaydet butonuna tiklaniyor.");
        WebElement saveBtn = BaseStep.findElementXpathWithWait(
                XPATH_BR_SAVE_BTN, TimeOut.SHORT.value);
        BaseStep.clickElement(saveBtn, "Is Kurali Kaydet butonu");
        BaseStep.waitSeconds(3);
        LogTest.info("Is kurali basariyla olusturuldu.");
    }

    // ============================================================
    // TEST: 20 — IS KURALINI DUZENLE
    // ============================================================

    @Test
    @Order(20)
    @DisplayName("Is kuralini duzenle ve kaydet")
    public void editBusinessRuleTest() {
        LogTest.info("Is Kurali Duzenle butonuna tiklaniyor.");
        WebElement editBtn = BaseStep.findElementXpathWithWait(
                XPATH_BR_EDIT_BTN, TimeOut.MEDIUM.value);
        BaseStep.clickElement(editBtn, "Is Kurali Duzenle butonu");
        BaseStep.waitSeconds(2);

        LogTest.info("Kural adi guncelleniyor.");
        WebElement nameInput = BaseStep.findElementXpathWithWait(
                XPATH_BR_NAME_INPUT, TimeOut.SHORT.value);
        BaseStep.clearAndType(nameInput, "Guncellenmis Is Kurali", "Kural adi (duzenleme)");
        BaseStep.waitSeconds(1);

        LogTest.info("Aciklama guncelleniyor.");
        WebElement descInput = BaseStep.findElementXpathWithWait(
                XPATH_BR_DESC_INPUT, TimeOut.SHORT.value);
        BaseStep.clearAndType(descInput, "Guncellenmis is kurali aciklamasi.",
                "Kural aciklamasi (duzenleme)");
        BaseStep.waitSeconds(1);

        LogTest.info("Kaydet butonuna tiklaniyor.");
        WebElement saveBtn = BaseStep.findElementXpathWithWait(
                XPATH_BR_SAVE_BTN, TimeOut.SHORT.value);
        BaseStep.clickElement(saveBtn, "Is Kurali Kaydet butonu (duzenleme)");
        BaseStep.waitSeconds(3);
        LogTest.info("Is kurali basariyla guncellendi.");
    }

    // ============================================================
    // TEST: 21 — IS KURALINI SIL
    // ============================================================

    @Test
    @Order(21)
    @DisplayName("Is kuralini sil - onay popup ile")
    public void deleteBusinessRuleTest() {
        LogTest.info("Is Kurali Sil butonuna tiklaniyor.");
        WebElement deleteBtn = BaseStep.findElementXpathWithWait(
                XPATH_BR_DELETE_BTN, TimeOut.MEDIUM.value);
        BaseStep.jsClickElement(deleteBtn, "Is Kurali Sil butonu");
        BaseStep.waitSeconds(2);

        LogTest.info("Onay popup'inda Sil butonuna tiklaniyor.");
        WebElement confirmBtn = BaseStep.findElementXpathWithWait(
                XPATH_BR_DELETE_CONFIRM_BTN, TimeOut.SHORT.value);
        BaseStep.jsClickElement(confirmBtn, "Sil onay butonu");
        BaseStep.waitSeconds(3);
        LogTest.info("Is kurali basariyla silindi.");
    }

    // ============================================================
    // TEST: 22 — ESKALASYON KURALLARI SUB-SEKMESINE GEC
    // ============================================================

    @Test
    @Order(22)
    @DisplayName("Eskalasyon Kurallari sub-sekmesine gec")
    public void switchToEscalationRulesTabTest() {
        LogTest.info("Eskalasyon Kurallari sub-sekmesi araniyor.");
        WebElement escalationTab = BaseStep.findElementXpathWithWait(
                XPATH_SUBTAB_ESCALATION, TimeOut.MEDIUM.value);
        // Radix sekmesi: focus+pointer event ile tikla, aria-selected ile dogrula
        clickRadixTab(escalationTab, "Eskalasyon Kurallari sub-sekmesi");
        BaseStep.waitSeconds(2);
        LogTest.info("Eskalasyon Kurallari sub-sekmesi acildi.");
    }

    // ============================================================
    // TEST: 23 — ESKALASYON KURALI EKLE
    // ============================================================

    @Test
    @Order(23)
    @DisplayName("Eskalasyon Kurali Ekle - tam surec")
    public void addEscalationRuleTest() {
        LogTest.info("Eskalasyon Kurali Ekle butonuna tiklaniyor.");
        WebElement addBtn = BaseStep.findElementXpathWithWait(
                XPATH_ADD_ESCALATION_RULE_BTN, TimeOut.MEDIUM.value);
        BaseStep.clickElement(addBtn, "Eskalasyon Kurali Ekle butonu");
        BaseStep.waitSeconds(2);

        LogTest.info("Kural adi giriliyor.");
        WebElement nameInput = BaseStep.findElementXpathWithWait(
                XPATH_ESC_NAME_INPUT, TimeOut.SHORT.value);
        BaseStep.clearAndType(nameInput, "Otomasyon Eskalasyon Kurali", "Kural adi");
        BaseStep.waitSeconds(1);

        LogTest.info("Kayit turu seciliyor.");
        openDropdownAndSelectFirst(XPATH_ESC_ENTITY_SELECT, "Kayit turu");

        LogTest.info("Aciklama giriliyor.");
        WebElement descInput = BaseStep.findElementXpathWithWait(
                XPATH_ESC_DESC_INPUT, TimeOut.SHORT.value);
        BaseStep.clearAndType(descInput, "Otomasyon ile olusturulan eskalasyon kurali aciklamasi.",
                "Kural aciklamasi");
        BaseStep.waitSeconds(1);

        LogTest.info("Kaydet butonuna tiklaniyor.");
        WebElement saveBtn = BaseStep.findElementXpathWithWait(
                XPATH_ESC_SAVE_BTN, TimeOut.SHORT.value);
        BaseStep.clickElement(saveBtn, "Eskalasyon Kurali Kaydet butonu");
        BaseStep.waitSeconds(3);
        LogTest.info("Eskalasyon kurali basariyla olusturuldu.");
    }

    // ============================================================
    // TEST: 24 — ESKALASYON KURALINI DUZENLE
    // ============================================================

    @Test
    @Order(24)
    @DisplayName("Eskalasyon kuralini duzenle ve kaydet")
    public void editEscalationRuleTest() {
        LogTest.info("Eskalasyon Kuralini Duzenle butonuna tiklaniyor.");
        WebElement editBtn = BaseStep.findElementXpathWithWait(
                XPATH_ESC_EDIT_BTN, TimeOut.MEDIUM.value);
        BaseStep.clickElement(editBtn, "Eskalasyon Kuralini Duzenle butonu");
        BaseStep.waitSeconds(2);

        LogTest.info("Kural adi guncelleniyor.");
        WebElement nameInput = BaseStep.findElementXpathWithWait(
                XPATH_ESC_NAME_INPUT, TimeOut.SHORT.value);
        BaseStep.clearAndType(nameInput, "Guncellenmis Eskalasyon Kurali", "Kural adi (duzenleme)");
        BaseStep.waitSeconds(1);

        LogTest.info("Aciklama guncelleniyor.");
        WebElement descInput = BaseStep.findElementXpathWithWait(
                XPATH_ESC_DESC_INPUT, TimeOut.SHORT.value);
        BaseStep.clearAndType(descInput, "Guncellenmis eskalasyon kurali aciklamasi.",
                "Kural aciklamasi (duzenleme)");
        BaseStep.waitSeconds(1);

        LogTest.info("Kaydet butonuna tiklaniyor.");
        WebElement saveBtn = BaseStep.findElementXpathWithWait(
                XPATH_ESC_SAVE_BTN, TimeOut.SHORT.value);
        BaseStep.clickElement(saveBtn, "Eskalasyon Kurali Kaydet butonu (duzenleme)");
        BaseStep.waitSeconds(3);
        LogTest.info("Eskalasyon kurali basariyla guncellendi.");
    }

    // ============================================================
    // TEST: 25 — ESKALASYON KURALINI SIL
    // ============================================================

    @Test
    @Order(25)
    @DisplayName("Eskalasyon kuralini sil - onay popup ile")
    public void deleteEscalationRuleTest() {
        LogTest.info("Eskalasyon Kurali Sil butonuna tiklaniyor.");
        WebElement deleteBtn = BaseStep.findElementXpathWithWait(
                XPATH_ESC_DELETE_BTN, TimeOut.MEDIUM.value);
        BaseStep.jsClickElement(deleteBtn, "Eskalasyon Kurali Sil butonu");
        BaseStep.waitSeconds(2);

        LogTest.info("Onay popup'inda Tamam butonuna tiklaniyor.");
        WebElement confirmBtn = BaseStep.findElementXpathWithWait(
                XPATH_ESC_DELETE_CONFIRM_BTN, TimeOut.SHORT.value);
        BaseStep.jsClickElement(confirmBtn, "Tamam onay butonu");
        BaseStep.waitSeconds(3);
        LogTest.info("Eskalasyon kurali basariyla silindi.");
    }

    // ============================================================
    // TEST: 26 — ETKI ANALIZI SEKMESINE GEC
    // ============================================================

    @Test
    @Order(26)
    @DisplayName("Etki Analizi sekmesine gec")
    public void switchToImpactTabTest() {
        LogTest.info("Etki Analizi sekmesi araniyor.");
        WebElement impactTab = BaseStep.findElementXpathWithWait(
                XPATH_TAB_IMPACT, TimeOut.MEDIUM.value);
        // Radix sekmesi: focus+pointer event ile tikla, aria-selected ile dogrula
        clickRadixTab(impactTab, "Etki Analizi sekmesi");
        BaseStep.waitSeconds(2);
        LogTest.info("Etki Analizi sekmesi acildi.");
    }

    // ============================================================
    // TEST: 27 — ETKI ANALIZI - DEPARTMAN VE ROL SECIMI
    // ============================================================

    @Test
    @Order(27)
    @DisplayName("Etki Analizi - departman ve rol dropdownlari")
    public void impactAnalysisFiltersTest() {
        LogTest.info("Departman dropdown seciliyor.");
        openDropdownAndSelectFirst(XPATH_IMPACT_DEPT_SELECT, "Departman");

        LogTest.info("Rol dropdown seciliyor.");
        openDropdownAndSelectFirst(XPATH_IMPACT_ROLE_SELECT, "Rol");
        LogTest.info("Etki Analizi secimleri tamamlandi.");
    }

    // ============================================================
    // TEST: 28 — ONIZLEME & TEST SEKMESINE GEC
    // ============================================================

    @Test
    @Order(28)
    @DisplayName("Onizleme & Test sekmesine gec")
    public void switchToPreviewTabTest() {
        LogTest.info("Onizleme & Test sekmesi araniyor.");
        WebElement previewTab = BaseStep.findElementXpathWithWait(
                XPATH_TAB_PREVIEW, TimeOut.MEDIUM.value);
        // Radix sekmesi: focus+pointer event ile tikla, aria-selected ile dogrula
        clickRadixTab(previewTab, "Onizleme & Test sekmesi");
        BaseStep.waitSeconds(2);
        LogTest.info("Onizleme & Test sekmesi acildi.");
    }

    // ============================================================
    // TEST: 29 — ONIZLEME & TEST - PROJE VE IS AKISI SECIMI
    // ============================================================

    @Test
    @Order(29)
    @DisplayName("Onizleme & Test - proje ve is akisi dropdownlari")
    public void previewTestFiltersTest() {
        LogTest.info("1. dropdown (proje) seciliyor.");
        openDropdownAndSelectFirst(XPATH_PREVIEW_DROPDOWN_1, "Onizleme proje");

        LogTest.info("2. dropdown (is akisi) seciliyor.");
        openDropdownAndSelectFirst(XPATH_PREVIEW_DROPDOWN_2, "Onizleme is akisi");
        LogTest.info("Onizleme & Test secimleri tamamlandi.");
    }

    // ============================================================
    // TEST: 30 — SABLONLAR SAYFASINA GIT
    // ============================================================

    @Test
    @Order(30)
    @DisplayName("Sablonlar sayfasina git")
    public void navigateToTemplatesTest() {
        LogTest.info("Sidebar Sablonlar butonu araniyor.");
        WebElement templatesBtn = BaseStep.findElementXpathWithWait(
                XPATH_SIDEBAR_TEMPLATES, TimeOut.MEDIUM.value);
        BaseStep.clickElement(templatesBtn, "Sablonlar sidebar butonu");
        BaseStep.waitSeconds(2);

        LogTest.info("Sayfanin yuklendigi dogrulaniyor.");
        WebElement page = BaseStep.findElementXpathWithWait("//main", TimeOut.SHORT.value);
        assertTrue(page.isDisplayed(), "Sablonlar sayfasi yuklenemedi!");
        LogTest.info("Sablonlar sayfasi acildi.");
    }

    // ============================================================
    // TEST: 31 — KAYIT TURU FILTRESI
    // ============================================================

    @Test
    @Order(31)
    @DisplayName("Sablonlar - Kayit Turu filtresi")
    public void filterTemplatesByEntityTest() {
        LogTest.info("Kayit Turu dropdown seciliyor.");
        openDropdownAndSelectFirst(XPATH_TEMPLATES_FILTER, "Kayit Turu filtresi");
        LogTest.info("Sablon filtresi uygulandi.");
    }

    // ============================================================
    // TEST: 32 — SABLON GORUNTULE (popup ac/kapat)
    // ============================================================

    @Test
    @Order(32)
    @DisplayName("Sablon Goruntule - popup ac ve kapat")
    public void viewTemplateTest() {
        LogTest.info("Goruntule butonuna tiklaniyor.");
        WebElement viewBtn = BaseStep.findElementXpathWithWait(
                XPATH_TEMPLATE_VIEW_BTN, TimeOut.MEDIUM.value);
        BaseStep.clickElement(viewBtn, "Goruntule butonu");
        BaseStep.waitSeconds(2);

        LogTest.info("Kapat butonuna tiklaniyor.");
        WebElement closeBtn = BaseStep.findElementXpathWithWait(
                XPATH_VIEW_DIALOG_CLOSE_BTN, TimeOut.MEDIUM.value);
        BaseStep.clickElement(closeBtn, "Kapat butonu");
        BaseStep.waitSeconds(2);
        LogTest.info("Sablon goruntuleme popup'i kapatildi.");
    }

    // ============================================================
    // TEST: 33 — SABLONU PROJEYE UYGULA
    // ============================================================

    @Test
    @Order(33)
    @DisplayName("Sablonu Projeye Uygula - hedef proje sec ve uygula")
    public void applyTemplateToProjectTest() {
        LogTest.info("Listedeki Projeye Uygula butonuna tiklaniyor.");
        WebElement applyListBtn = BaseStep.findElementXpathWithWait(
                XPATH_TEMPLATE_APPLY_BTN, TimeOut.MEDIUM.value);
        BaseStep.clickElement(applyListBtn, "Projeye Uygula (liste) butonu");
        BaseStep.waitSeconds(2);

        LogTest.info("Hedef Proje seciliyor.");
        openDropdownAndSelectFirst(XPATH_APPLY_TARGET_PROJECT, "Hedef Proje");

        LogTest.info("Projeye Uygula submit butonuna tiklaniyor.");
        WebElement applySubmitBtn = BaseStep.findElementXpathWithWait(
                XPATH_APPLY_SUBMIT_BTN, TimeOut.SHORT.value);
        BaseStep.clickElement(applySubmitBtn, "Projeye Uygula submit butonu");
        BaseStep.waitSeconds(3);
        LogTest.info("Sablon basariyla projeye uygulandi.");
    }

    // ============================================================
    // TEST: 34 — ZAMANLAYICILAR SAYFASINA GIT
    // ============================================================

    @Test
    @Order(34)
    @DisplayName("Zamanlayicilar sayfasina git")
    public void navigateToTimersTest() {
        LogTest.info("Sidebar Zamanlayicilar butonu araniyor.");
        WebElement timersBtn = BaseStep.findElementXpathWithWait(
                XPATH_SIDEBAR_TIMERS, TimeOut.MEDIUM.value);
        BaseStep.clickElement(timersBtn, "Zamanlayicilar sidebar butonu");
        BaseStep.waitSeconds(2);

        LogTest.info("Sayfanin yuklendigi dogrulaniyor.");
        WebElement page = BaseStep.findElementXpathWithWait("//main", TimeOut.SHORT.value);
        assertTrue(page.isDisplayed(), "Zamanlayicilar sayfasi yuklenemedi!");
        LogTest.info("Zamanlayicilar sayfasi acildi.");
    }

    // ============================================================
    // TEST: 35 — ZAMANLAYICILAR FILTRELERI (Proje + Kayit Turu)
    // ============================================================

    @Test
    @Order(35)
    @DisplayName("Zamanlayicilar - Proje ve Kayit Turu filtreleri")
    public void filterTimersTest() {
        LogTest.info("Proje filtresi seciliyor.");
        openDropdownAndSelectFirst(XPATH_TIMERS_PROJECT_FILTER, "Proje filtresi");

        LogTest.info("Kayit Turu filtresi seciliyor.");
        openDropdownAndSelectFirst(XPATH_TIMERS_ENTITY_FILTER, "Kayit Turu filtresi");
        LogTest.info("Zamanlayici filtreleri uygulandi.");
    }

    // ============================================================
    // TEST: 36 — ZAMANI GELENLERI ISLE
    // ============================================================

    @Test
    @Order(36)
    @DisplayName("Zamani Gelenleri Isle butonuna tikla")
    public void processDueTimersTest() {
        LogTest.info("Zamani Gelenleri Isle butonu araniyor.");
        WebElement processBtn = BaseStep.findElementXpathWithWait(
                XPATH_PROCESS_DUE_BTN, TimeOut.MEDIUM.value);
        BaseStep.clickElement(processBtn, "Zamani Gelenleri Isle butonu");
        BaseStep.waitSeconds(3);
        LogTest.info("Zamanlayici isleme tetiklendi.");
    }

    // ============================================================
    // HELPER: RADIX SELECT KLAVYE NAVIGASYONU
    // ============================================================

    /**
     * Radix Select dropdown'u acar ve klavye ile (ARROW_DOWN + ENTER)
     * ilk secilebilir option'i secer. Radix Select native olarak klavye
     * navigasyonu destekler ve disabled grup basliklarini otomatik atlar
     * — mouse click'ten daha guvenilir (ReportsPageTest'te dogrulandi).
     */
    private void openDropdownAndSelectFirst(String dropdownXpath, String description) {
        WebElement dropdown = BaseStep.findElementXpathWithWait(dropdownXpath, TimeOut.SHORT.value);
        BaseStep.clickElement(dropdown, description + " (dropdown ac)");
        BaseStep.waitSeconds(2);

        // ARROW_DOWN: ilk secilebilir option'a focus
        new Actions(driver).sendKeys(Keys.ARROW_DOWN).perform();
        BaseStep.waitSeconds(1);

        // ENTER: focuslu option'i sec
        new Actions(driver).sendKeys(Keys.ENTER).perform();
        BaseStep.waitSeconds(2);

        LogTest.info(description + " - ilk secenek klavye ile secildi");
    }

    /**
     * Radix UI sekmesine (role='tab') tiklar.
     * Radix sekmeleri focus/pointer event'leri ile aktiflesir; saf .click() veya
     * Selenium click sekmeyi degistirmez (ayrica sticky header tiklamayi engeller).
     * Bu metod scrollIntoView + focus + pointer/mouse event dizisi gonderir, sonra
     * tiklamanin gerceklestigini aria-selected='true' ile DOGRULAR. Sekme gercekten
     * gecmezse exception firlatir — boylece "loga gecti ama ekranda gecmedi" olmaz.
     */
    private void clickRadixTab(WebElement tab, String tabDescription) {
        LogTest.actionInfo("Click Radix tab", tabDescription);

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript(
                "const el = arguments[0];" +
                "el.scrollIntoView({block: 'center'});" +
                "el.focus();" +
                "['pointerdown','mousedown','pointerup','mouseup','click'].forEach(function(type){" +
                "  const E = type.indexOf('pointer') === 0 ? PointerEvent : MouseEvent;" +
                "  el.dispatchEvent(new E(type, {bubbles: true, cancelable: true, view: window}));" +
                "});",
                tab);

        // Radix'in state'i guncellemesini bekle ve sekme geciSini dogrula
        WebDriverWait localWait = new WebDriverWait(driver, Duration.ofSeconds(TimeOut.MEDIUM.value));
        localWait.until(d -> "true".equals(tab.getAttribute("aria-selected")));

        LogTest.stepInfo("Radix sekmesi aktiflesti: " + tabDescription);
    }
}
