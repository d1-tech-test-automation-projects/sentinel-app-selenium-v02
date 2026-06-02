import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.WheelInput;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RequirementsPageTest extends BaseStep {

    @BeforeAll
    public void setup() {
        openChromeDriver();
    }

    @AfterAll
    public void teardown() {
        driverQuit();
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
    // 2. Sayfa yuklendi mi?
    // ─────────────────────────────────────────────────────────────
    @Test
    @Order(2)
    @DisplayName("Is Requirements page loaded?")
    public void isPageLoaded() {
        LogTest.info("Navigating to Requirements page.");

        WebElement requirementsSidebarBtn = findElementXpathWithWait(
                "//*[@id=\"root\"]/div/aside/div[2]/div[1]/nav/button[2]",
                TimeOut.SHORT.value);
        assertTrue(requirementsSidebarBtn.isDisplayed(), "Requirements sidebar button is not displayed!");
        clickElement(requirementsSidebarBtn, "Requirements sidebar button");
        waitSeconds(2);

        WebElement page = findElementXpathWithWait("//main", TimeOut.SHORT.value);
        assertTrue(page.isDisplayed(), "Requirements page failed to load!");
        LogTest.info("Requirements page loaded.");
    }


    // ─────────────────────────────────────────────────────────────
    // 3. Projeye Gore Filtrele
    // ─────────────────────────────────────────────────────────────
    @Test
    @Order(3)
    @DisplayName("Filter Requirements by Project")
    public void filterByProjectTest() {
        LogTest.info("Projeye gore filtre dropdown aciliyor.");
        WebElement projectFilter = findElementXpathWithWait(
                "//button[@data-testid='requirements-project-filter']",
                TimeOut.SHORT.value);
        clickElement(projectFilter, "Projeye Gore Filtrele dropdown");
        waitSeconds(1);

        LogTest.info("Ilk proje secenegi seciliyor.");
        WebElement firstProjectOption = findElementXpathWithWait(
                "(//*[@role='listbox']//*[@role='option'])[2]",
                TimeOut.SHORT.value);
        clickElement(firstProjectOption, "Ilk proje");
        waitSeconds(2);

        LogTest.info("Filtre 'Tumu' olarak sifirlaniyor.");
        WebElement projectFilterReopen = findElementXpathWithWait(
                "//button[@data-testid='requirements-project-filter']",
                TimeOut.SHORT.value);
        clickElement(projectFilterReopen, "Projeye Gore Filtrele dropdown");
        waitSeconds(1);
        WebElement resetOption = findElementXpathWithWait(
                "//*[@role='listbox']//*[@role='option' and normalize-space(.)='Tümü']",
                TimeOut.SHORT.value);
        clickElement(resetOption, "Tumu");
        waitSeconds(2);
        LogTest.info("Filtre testi tamamlandi.");
    }


    // ─────────────────────────────────────────────────────────────
    // 4. Gereksinim Ekle
    // ─────────────────────────────────────────────────────────────
    @Test
    @Order(4)
    @DisplayName("Add Requirement")
    public void addRequirementTest() {
        LogTest.info("Gereksinim Ekle butonuna tiklaniyor.");
        WebElement addBtn = findElementXpathWithWait(
                "//*[@id=\"root\"]/div/div/main/div/div/div[1]/div/button[2]",
                TimeOut.SHORT.value);
        clickElement(addBtn, "Gereksinim Ekle butonu");
        waitSeconds(2);

        // --- TAB 1: GENEL BILGILER ---
        LogTest.info("Proje seciliyor.");
        WebElement projectDropdown = findElementXpathWithWait(
                "//*[@id='requirement-project']", TimeOut.SHORT.value);
        clickElement(projectDropdown, "Proje dropdown");
        waitSeconds(1);
        WebElement projectOption = findElementXpathWithWait(
                "(//*[@role='listbox']//*[@role='option'])[1]",
                TimeOut.SHORT.value);
        clickElement(projectOption, "Ilk proje secildi");
        waitSeconds(2);

        LogTest.info("Gereksinim adi giriliyor.");
        WebElement nameInput = findElementXpathWithWait(
                "//*[@id='requirement-name']", TimeOut.SHORT.value);
        clearAndType(nameInput, "Otomasyon Gereksinimi", "Gereksinim adi alani");
        waitSeconds(1);

        LogTest.info("Gereksinim aciklamasi giriliyor.");
        WebElement descInput = findElementXpathWithWait(
                "//*[@id='requirement-description']", TimeOut.SHORT.value);
        clearAndType(descInput, "Otomatik test tarafindan eklenmistir.", "Aciklama alani");
        waitSeconds(1);

        LogTest.info("Gereksinim turu seciliyor: Fonksiyonel");
        WebElement typeDropdown = findElementXpathWithWait(
                "//*[@id='requirement-type']", TimeOut.SHORT.value);
        clickElement(typeDropdown, "Gereksinim turu dropdown");
        waitSeconds(1);
        WebElement typeOption = findElementXpathWithWait(
                "//*[@role='listbox']//*[@role='option' and contains(.,'Fonksiyonel') and not(contains(.,'Olmayan'))]",
                TimeOut.SHORT.value);
        clickElement(typeOption, "Fonksiyonel secildi");
        waitSeconds(2);

        LogTest.info("Oncelik seciliyor: Yuksek");
        WebElement priorityDropdown = findElementXpathWithWait(
                "//*[@id='requirement-priority']", TimeOut.SHORT.value);
        clickElement(priorityDropdown, "Oncelik dropdown");
        waitSeconds(1);
        WebElement priorityOption = findElementXpathWithWait(
                "//*[@role='listbox']//*[@role='option' and contains(.,'Yüksek')]",
                TimeOut.SHORT.value);
        clickElement(priorityOption, "Yuksek secildi");
        waitSeconds(2);

        // --- TAB 2: BAGLI MODULLER ---
        LogTest.info("Tab 2: Bagli Moduller sekmesine geciliyor.");
        WebElement modulesTab = findElementXpathWithWait(
                "(//div[@role='dialog'])[last()]//button[@role='tab' and normalize-space(.)='Bağlı Modüller']",
                TimeOut.SHORT.value);
        clickElement(modulesTab, "Bagli Moduller sekmesi");
        waitSeconds(2);

        // --- TAB 3: BAGLI TEST SENARYOLARI ---
        LogTest.info("Tab 3: Bagli Test Senaryolari sekmesine geciliyor.");
        WebElement testCasesTab = findElementXpathWithWait(
                "(//div[@role='dialog'])[last()]//button[@role='tab' and normalize-space(.)='Bağlı Test Senaryoları']",
                TimeOut.SHORT.value);
        clickElement(testCasesTab, "Bagli Test Senaryolari sekmesi");
        waitSeconds(2);

        // --- KAYDET ---
        LogTest.info("Kaydet butonuna tiklaniyor.");
        WebElement saveBtn = findElementXpathWithWait(
                "(//div[@role='dialog'])[last()]//button[normalize-space(.)='Kaydet']",
                TimeOut.SHORT.value);
        clickElement(saveBtn, "Kaydet butonu");
        waitSeconds(3);
        LogTest.info("Gereksinim basariyla olusturuldu.");
    }


    // ─────────────────────────────────────────────────────────────
    // 5. Gereksinim Goruntule (Detay popup + kapat)
    // ─────────────────────────────────────────────────────────────
    @Test
    @Order(5)
    @DisplayName("View Requirement Details")
    public void viewRequirementTest() {
        LogTest.info("Ilk gereksinimin Goruntule butonuna tiklaniyor.");
        WebElement viewBtn = findElementXpathWithWait(
                "(//button[normalize-space(.)='Görüntüle'])[1]", TimeOut.SHORT.value);
        clickElement(viewBtn, "Goruntule butonu");
        waitSeconds(2);

        LogTest.info("Detay popup'i kapatiliyor (X butonu).");
        WebElement closeBtn = findElementXpathWithWait(
                "(//div[@role='dialog'])[last()]//button[.//span[normalize-space(.)='Close']]",
                TimeOut.SHORT.value);
        clickElement(closeBtn, "Popup kapat (X)");
        waitSeconds(2);
        LogTest.info("Goruntule testi tamamlandi.");
    }


    // ─────────────────────────────────────────────────────────────
    // 6. Gereksinim Duzenle
    // ─────────────────────────────────────────────────────────────
    @Test
    @Order(6)
    @DisplayName("Edit Requirement")
    public void editRequirementTest() {
        LogTest.info("Ilk gereksinimin Duzenle butonuna tiklaniyor.");
        WebElement editBtn = findElementXpathWithWait(
                "(//button[normalize-space(.)='Düzenle'])[1]", TimeOut.SHORT.value);
        clickElement(editBtn, "Duzenle butonu");
        waitSeconds(2);

        LogTest.info("Gereksinim adi guncelleniyor.");
        WebElement nameInput = findElementXpathWithWait(
                "//*[@id='requirement-name']", TimeOut.SHORT.value);
        clearAndType(nameInput, "Guncellenmis Otomasyon Gereksinimi", "Gereksinim adi (duzenleme)");
        waitSeconds(1);

        LogTest.info("Gereksinim aciklamasi guncelleniyor.");
        WebElement descInput = findElementXpathWithWait(
                "//*[@id='requirement-description']", TimeOut.SHORT.value);
        clearAndType(descInput, "Guncellenmis aciklama metni.", "Aciklama (duzenleme)");
        waitSeconds(1);

        LogTest.info("Guncelle butonuna tiklaniyor.");
        WebElement updateBtn = findElementXpathWithWait(
                "(//div[@role='dialog'])[last()]//button[normalize-space(.)='Güncelle']",
                TimeOut.SHORT.value);
        clickElement(updateBtn, "Guncelle butonu");
        waitSeconds(3);
        LogTest.info("Gereksinim basariyla guncellendi.");
    }


    // ─────────────────────────────────────────────────────────────
    // 7. Gereksinim Sil (Onay popup'i ile)
    // ─────────────────────────────────────────────────────────────
    @Test
    @Order(7)
    @DisplayName("Delete Requirement")
    public void deleteRequirementTest() {
        LogTest.info("Ilk gereksinimin Sil butonuna tiklaniyor.");
        WebElement deleteBtn = findElementXpathWithWait(
                "(//button[normalize-space(.)='Sil'])[1]", TimeOut.SHORT.value);
        jsClickElement(deleteBtn, "Sil butonu");
        waitSeconds(2);

        LogTest.info("Onay popup'inda 'Evet, Sil' butonuna tiklaniyor.");
        // Radix AlertDialog role='alertdialog' kullaniyor (role='dialog' degil),
        // o yuzden ikisini de yakalayan selector + JS click.
        WebElement confirmDeleteBtn = findElementXpathWithWait(
                "(//*[@role='alertdialog' or @role='dialog'])[last()]//button[normalize-space(.)='Evet, Sil']",
                TimeOut.SHORT.value);
        jsClickElement(confirmDeleteBtn, "Evet, Sil butonu");
        waitSeconds(3);
        LogTest.info("Gereksinim basariyla silindi.");
    }

}
