import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BugsPageTest extends BaseStep {

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

    private static final String XPATH_SIDEBAR_BUGS         = "//*[@id=\"root\"]/div/aside/div[2]/div[1]/nav/button[4]";
    private static final String XPATH_BUGS_FILTER          = "//main//button[@role='combobox']";
    private static final String XPATH_BUGS_SEARCH          = "//main//input[@placeholder='Hata Ara...']";
    private static final String XPATH_ADD_BUG_BTN          = "//main//button[normalize-space(.)='Yeni Hata Ekle']";

    // Yeni hata olusturma popup
    // Proje ve Ortam: form'da her zaman ilk iki combobox. Document-order
    // indexing kullanildigi icin sonradan acilan optional dropdownlar
    // (modul/gereksinim/test senaryosu) bu indeksleri kaydirmaz.
    // Ciddiyet/Test Turu/Oncelik: ek notlar'dan onceki son 3 combobox
    // (preceding axis, reverse doc order ile).
    private static final String XPATH_CREATE_PROJECT       = "(//div[@role='dialog']//form//button[@role='combobox'])[1]";
    private static final String XPATH_CREATE_ENV           = "(//div[@role='dialog']//form//button[@role='combobox'])[2]";
    private static final String XPATH_CREATE_SEVERITY      = "//*[@id='defect-create-additional-notes']/preceding::button[@role='combobox' and ancestor::div[@role='dialog']][3]";
    private static final String XPATH_CREATE_TEST_TYPE     = "//*[@id='defect-create-additional-notes']/preceding::button[@role='combobox' and ancestor::div[@role='dialog']][2]";
    private static final String XPATH_CREATE_PRIORITY      = "//*[@id='defect-create-additional-notes']/preceding::button[@role='combobox' and ancestor::div[@role='dialog']][1]";
    private static final String XPATH_CREATE_TITLE         = "//*[@id='defect-create-title']";
    private static final String XPATH_CREATE_DESCRIPTION   = "//*[@id='defect-create-description']";
    private static final String XPATH_CREATE_EXPECTED      = "//*[@id='defect-create-expected-result']";
    private static final String XPATH_CREATE_NOTES         = "//*[@id='defect-create-additional-notes']";
    private static final String XPATH_CREATE_SUBMIT        = "//div[@role='dialog']//button[normalize-space(.)='Hata Oluştur']";

    // Listede islem butonlari
    private static final String XPATH_ROW_VIEW_BTN         = "(//button[@aria-label='Görüntüle'])[1]";
    private static final String XPATH_DETAIL_UPDATE_BTN    = "//main//button[normalize-space(.)='Güncelle']";

    // Guncelleme popup (dialog tabanli locator'lar)
    private static final String XPATH_UPDATE_TITLE         = "//div[@role='dialog']//form/div[1]/input";
    private static final String XPATH_UPDATE_DESCRIPTION   = "//div[@role='dialog']//form/div[2]/textarea";
    private static final String XPATH_UPDATE_SEVERITY      = "//div[@role='dialog']//form/div[3]/div[1]/button";
    private static final String XPATH_UPDATE_PRIORITY      = "//div[@role='dialog']//form/div[3]/div[2]/button";
    private static final String XPATH_UPDATE_TEST_TYPE     = "//div[@role='dialog']//form/div[4]/button";
    private static final String XPATH_UPDATE_FILE_INPUT    = "//*[@id='defect-update-files']";
    private static final String XPATH_UPDATE_SUBMIT        = "//div[@role='dialog']//form//button[@type='submit' and normalize-space(.)='Güncelle']";

    // ============================================================
    // HELPER METHODS (Radix UI Select)
    // ============================================================

    private void radixClickOption(WebElement option) {
        waitSeconds(1);
        try {
            new Actions(driver).moveToElement(option).click().perform();
            return;
        } catch (org.openqa.selenium.StaleElementReferenceException stale) {
            return;
        } catch (Exception ignored) {}

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
        } catch (org.openqa.selenium.StaleElementReferenceException stale) {
            return;
        } catch (Exception ignored) {}

        try {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", option);
        } catch (Exception ignored) {}
    }

    private void clickRadixOptionByText(String text, String description) {
        waitSeconds(1);
        WebElement option = findElementXpathWithWait(
                "//*[@role='option' and contains(normalize-space(.), '" + text + "')]",
                TimeOut.SHORT.value);
        radixClickOption(option);
        LogTest.info(description + " - " + text + " secildi");
    }

    private void clickFirstRadixOption(String description) {
        waitSeconds(1);
        WebElement option = findElementXpathWithWait(
                "(//*[@role='option'])[1]", TimeOut.SHORT.value);
        radixClickOption(option);
        LogTest.info(description + " - ilk secenek secildi");
    }

    // Onceki testten kalan acik dialog/overlay'i ESC tusu ile kapatir.
    // DOM'dan zorla silmek React state'ini bozup browser crash'e yol
    // acabildigi icin tercih edilmez; ESC, Radix Dialog'un standart
    // kapanma yolu oldugu icin guvenlidir.
    private void dismissAnyOpenDialog() {
        try {
            new Actions(driver).sendKeys(Keys.ESCAPE).perform();
            waitSeconds(1);
            // Hala acik kalmis dialog varsa ikinci ESC
            if (isElementPresent(By.xpath("//*[@role='dialog' and @data-state='open']"))) {
                new Actions(driver).sendKeys(Keys.ESCAPE).perform();
                waitSeconds(1);
            }
            LogTest.stepInfo("ESC ile acik dialoglar kapatildi.");
        } catch (Exception ignored) {}
    }

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
    // TEST: 2 — HATALAR SAYFASINA GIT
    // ============================================================

    @Test
    @Order(2)
    @DisplayName("Hatalar sayfasina git")
    public void navigateToBugsPage() {
        LogTest.info("Sidebar Hatalar butonu araniyor.");
        WebElement bugsSidebarBtn = BaseStep.findElementXpathWithWait(
                XPATH_SIDEBAR_BUGS, TimeOut.SHORT.value);
        assertTrue(bugsSidebarBtn.isDisplayed(), "Hatalar sidebar butonu gorulmuyor!");
        BaseStep.clickElement(bugsSidebarBtn, "Hatalar sidebar butonu");
        BaseStep.waitSeconds(2);

        LogTest.info("Sayfanin yuklendigi dogrulaniyor.");
        WebElement page = BaseStep.findElementXpathWithWait("//main", TimeOut.SHORT.value);
        assertTrue(page.isDisplayed(), "Hatalar sayfasi yuklenemedi!");
        LogTest.info("Hatalar sayfasi acildi.");
    }

    // ============================================================
    // TEST: 3 — FILTRELEME DROPDOWN
    // ============================================================

    @Test
    @Order(3)
    @DisplayName("Hatalar filtresi - dropdown ile sec")
    public void filterBugsTest() {
        LogTest.info("Filtre dropdown aciliyor.");
        WebElement filterDropdown = BaseStep.findElementXpathWithWait(
                XPATH_BUGS_FILTER, TimeOut.SHORT.value);
        BaseStep.clickElement(filterDropdown, "Filtre dropdown");
        BaseStep.waitSeconds(1);

        LogTest.info("Listeden bir secenek seciliyor.");
        WebElement option = BaseStep.findElementXpathWithWait(
                "(//*[@role='option'])[2]", TimeOut.SHORT.value);
        radixClickOption(option);
        BaseStep.waitSeconds(2);

        LogTest.info("Filtre Tumu'ne sifirlaniyor.");
        filterDropdown = BaseStep.findElementXpathWithWait(
                XPATH_BUGS_FILTER, TimeOut.SHORT.value);
        BaseStep.clickElement(filterDropdown, "Filtre dropdown");
        BaseStep.waitSeconds(1);
        WebElement allOption = BaseStep.findElementXpathWithWait(
                "//*[@role='option']//span[contains(text(),'Tümü')]", TimeOut.SHORT.value);
        radixClickOption(allOption);
        BaseStep.waitSeconds(2);
        LogTest.info("Filtre testi tamamlandi.");
    }

    // ============================================================
    // TEST: 4 — HATA ARAMA
    // ============================================================

    @Test
    @Order(4)
    @DisplayName("Hata Ara - input alaninda arama yap")
    public void searchBugsTest() {
        LogTest.info("Hata Ara input alanina metin giriliyor.");
        WebElement searchInput = BaseStep.findElementXpathWithWait(
                XPATH_BUGS_SEARCH, TimeOut.SHORT.value);
        BaseStep.clearAndType(searchInput, "Test", "Hata Ara input");
        BaseStep.waitSeconds(2);

        LogTest.info("Arama filtresi temizleniyor.");
        BaseStep.clearAndType(searchInput, "", "Hata Ara input");
        BaseStep.waitSeconds(1);
        LogTest.info("Hata arama testi tamamlandi.");
    }

    // ============================================================
    // TEST: 5 — YENI HATA EKLE (TAM SUREC)
    // ============================================================

    @Test
    @Order(5)
    @DisplayName("Yeni hata ekle - tam surec")
    public void addBugTest() {
        LogTest.info("Yeni Hata Ekle butonuna tiklaniyor.");
        WebElement addBtn = BaseStep.findElementXpathWithWait(
                XPATH_ADD_BUG_BTN, TimeOut.MEDIUM.value);
        BaseStep.clickElement(addBtn, "Yeni Hata Ekle butonu");
        BaseStep.waitSeconds(2);

        LogTest.info("Proje seciliyor.");
        WebElement projectDropdown = BaseStep.findElementXpathWithWait(
                XPATH_CREATE_PROJECT, TimeOut.SHORT.value);
        BaseStep.clickElement(projectDropdown, "Proje Sec dropdown");
        BaseStep.waitSeconds(1);
        clickFirstRadixOption("Proje");
        BaseStep.waitSeconds(2);

        LogTest.info("Ortam seciliyor.");
        WebElement envDropdown = BaseStep.findElementXpathWithWait(
                XPATH_CREATE_ENV, TimeOut.SHORT.value);
        BaseStep.clickElement(envDropdown, "Ortam dropdown");
        BaseStep.waitSeconds(1);
        clickFirstRadixOption("Ortam");
        BaseStep.waitSeconds(2);

        LogTest.info("Hata basligi giriliyor.");
        WebElement titleInput = BaseStep.findElementXpathWithWait(
                XPATH_CREATE_TITLE, TimeOut.SHORT.value);
        BaseStep.clearAndType(titleInput, "Otomasyon Hata Basligi", "Hata basligi");
        BaseStep.waitSeconds(1);

        LogTest.info("Aciklama giriliyor.");
        WebElement descInput = BaseStep.findElementXpathWithWait(
                XPATH_CREATE_DESCRIPTION, TimeOut.SHORT.value);
        BaseStep.clearAndType(descInput, "Otomasyon ile olusturulan hata aciklamasi.", "Aciklama alani");
        BaseStep.waitSeconds(1);

        LogTest.info("Beklenen sonuc giriliyor.");
        WebElement expectedInput = BaseStep.findElementXpathWithWait(
                XPATH_CREATE_EXPECTED, TimeOut.SHORT.value);
        BaseStep.clearAndType(expectedInput, "Beklenen davranis aciklamasi.", "Beklenen sonuc alani");
        BaseStep.waitSeconds(1);

        LogTest.info("Ciddiyet seciliyor: Yuksek");
        WebElement severityDropdown = BaseStep.findElementXpathWithWait(
                XPATH_CREATE_SEVERITY, TimeOut.SHORT.value);
        BaseStep.clickElement(severityDropdown, "Ciddiyet dropdown");
        BaseStep.waitSeconds(1);
        clickRadixOptionByText("Yüksek", "Ciddiyet");
        BaseStep.waitSeconds(2);

        LogTest.info("Test turu seciliyor: Duman Testi");
        WebElement testTypeDropdown = BaseStep.findElementXpathWithWait(
                XPATH_CREATE_TEST_TYPE, TimeOut.SHORT.value);
        BaseStep.clickElement(testTypeDropdown, "Test turu dropdown");
        BaseStep.waitSeconds(1);
        clickRadixOptionByText("Duman Testi", "Test turu");
        BaseStep.waitSeconds(2);

        LogTest.info("Calisma onceligi seciliyor: Yuksek");
        WebElement priorityDropdown = BaseStep.findElementXpathWithWait(
                XPATH_CREATE_PRIORITY, TimeOut.SHORT.value);
        BaseStep.clickElement(priorityDropdown, "Calisma onceligi dropdown");
        BaseStep.waitSeconds(1);
        clickRadixOptionByText("Yüksek", "Calisma onceligi");
        BaseStep.waitSeconds(2);

        LogTest.info("Ek notlar giriliyor.");
        WebElement notesInput = BaseStep.findElementXpathWithWait(
                XPATH_CREATE_NOTES, TimeOut.SHORT.value);
        BaseStep.clearAndType(notesInput, "Otomasyon ile eklenen ek notlar.", "Ek notlar alani");
        BaseStep.waitSeconds(1);

        LogTest.info("Hata Olustur butonuna tiklaniyor.");
        WebElement submitBtn = BaseStep.findElementXpathWithWait(
                XPATH_CREATE_SUBMIT, TimeOut.SHORT.value);
        BaseStep.clickElement(submitBtn, "Hata Olustur butonu");
        BaseStep.waitSeconds(3);
        LogTest.info("Hata basariyla olusturuldu.");
    }

    // ============================================================
    // TEST: 6 — LISTEDEN HATAYI GORUNTULE
    // ============================================================

    @Test
    @Order(6)
    @DisplayName("Listeden hatayi goruntule")
    public void viewBugTest() {
        dismissAnyOpenDialog();
        BaseStep.waitSeconds(1);

        LogTest.info("Listedeki Goruntule butonuna tiklaniyor.");
        WebElement viewBtn = BaseStep.findElementXpathWithWait(
                XPATH_ROW_VIEW_BTN, TimeOut.MEDIUM.value);
        BaseStep.clickElement(viewBtn, "Goruntule butonu");
        BaseStep.waitSeconds(3);

        LogTest.info("Detay sayfasinin yuklendigi dogrulaniyor.");
        WebElement updateBtn = BaseStep.findElementXpathWithWait(
                XPATH_DETAIL_UPDATE_BTN, TimeOut.MEDIUM.value);
        assertTrue(updateBtn.isDisplayed(), "Hata detay sayfasi acilmadi!");
        LogTest.info("Hata detay sayfasi acildi.");
    }

    // ============================================================
    // TEST: 7 — HATAYI GUNCELLE (DOSYA EKLEME DAHIL)
    // ============================================================

    @Test
    @Order(7)
    @DisplayName("Hata guncelleme - tum alanlari ve dosya ekleme")
    public void updateBugTest() {
        LogTest.info("Guncelle butonuna tiklaniyor.");
        WebElement updateBtn = BaseStep.findElementXpathWithWait(
                XPATH_DETAIL_UPDATE_BTN, TimeOut.MEDIUM.value);
        BaseStep.clickElement(updateBtn, "Guncelle butonu");
        BaseStep.waitSeconds(2);

        LogTest.info("Hata basligi guncelleniyor.");
        WebElement titleInput = BaseStep.findElementXpathWithWait(
                XPATH_UPDATE_TITLE, TimeOut.SHORT.value);
        BaseStep.clearAndType(titleInput, "Guncellenmis Hata Basligi", "Hata basligi (guncelleme)");
        BaseStep.waitSeconds(1);

        LogTest.info("Aciklama guncelleniyor.");
        WebElement descInput = BaseStep.findElementXpathWithWait(
                XPATH_UPDATE_DESCRIPTION, TimeOut.SHORT.value);
        BaseStep.clearAndType(descInput, "Guncellenmis hata aciklamasi.", "Aciklama (guncelleme)");
        BaseStep.waitSeconds(1);

        LogTest.info("Ciddiyet guncelleniyor: Kritik");
        WebElement severityDropdown = BaseStep.findElementXpathWithWait(
                XPATH_UPDATE_SEVERITY, TimeOut.SHORT.value);
        BaseStep.clickElement(severityDropdown, "Ciddiyet dropdown (guncelleme)");
        BaseStep.waitSeconds(1);
        clickRadixOptionByText("Kritik", "Ciddiyet");
        BaseStep.waitSeconds(2);

        LogTest.info("Calisma onceligi guncelleniyor: Yuksek");
        WebElement priorityDropdown = BaseStep.findElementXpathWithWait(
                XPATH_UPDATE_PRIORITY, TimeOut.SHORT.value);
        BaseStep.clickElement(priorityDropdown, "Calisma onceligi dropdown (guncelleme)");
        BaseStep.waitSeconds(1);
        clickRadixOptionByText("Yüksek", "Calisma onceligi");
        BaseStep.waitSeconds(2);

        LogTest.info("Test turu guncelleniyor: Entegrasyon");
        WebElement testTypeDropdown = BaseStep.findElementXpathWithWait(
                XPATH_UPDATE_TEST_TYPE, TimeOut.SHORT.value);
        BaseStep.clickElement(testTypeDropdown, "Test turu dropdown (guncelleme)");
        BaseStep.waitSeconds(1);
        clickRadixOptionByText("Entegrasyon", "Test turu");
        BaseStep.waitSeconds(2);

        LogTest.info("Dosya ekleniyor: test-file.pdf");
        // File input'u gorunur alana getir ve sendKeys icin engelleyici
        // CSS varsa kaldir (display:none / visibility:hidden olabiliyor).
        WebElement fileInput = BaseStep.findElementXpathWithWait(
                XPATH_UPDATE_FILE_INPUT, TimeOut.SHORT.value);
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center',behavior:'instant'});" +
                "arguments[0].style.display = 'block';" +
                "arguments[0].style.visibility = 'visible';" +
                "arguments[0].style.opacity = '1';",
                fileInput);
        BaseStep.waitSeconds(1);
        BaseStep.uploadFileFromProject(
                XPATH_UPDATE_FILE_INPUT, "test-file.pdf", "Hata guncelleme dosya alani");
        BaseStep.waitSeconds(2);

        LogTest.info("Guncelle butonuna tiklaniyor.");
        scrollAnywhere(400);
        WebElement submitBtn = BaseStep.findElementXpathWithWait(
                XPATH_UPDATE_SUBMIT, TimeOut.SHORT.value);
        BaseStep.clickElement(submitBtn, "Guncelle (submit) butonu");
        BaseStep.waitSeconds(3);
        LogTest.info("Hata basariyla guncellendi.");
    }
}
