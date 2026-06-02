import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.time.Duration;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;


public class BaseStep extends LogTest {


        public static WebDriver driver;
        private static WebDriverWait wait;
        private static final String DEFAULT_URL = "https://sentineltest.d1-tech.com/";
        private static final int DEFAULT_TIMEOUT = 10;

        /**
         * Her @Test metodundan önce otomatik çalışır.
         * TestResultLogger'ın test süresi (duration) hesaplaması için
         * test başlangıç zamanını kaydeder.
         */
        @BeforeEach
        public void recordTestStartTime(TestInfo testInfo) {
            String testName = getClass().getSimpleName() + "." + testInfo.getDisplayName();
            TestResultLogger.recordTestStart(testName, this);
        }

        /**
         * Chrome driver'ı başlatır ve konfigüre eder
         */


    /* güncel openchromedriver method */

     public static void openChromeDriver() {
     try {
     stepInfo("Initializing Chrome WebDriver");

     // Chrome options
     ChromeOptions options = new ChromeOptions();

     // Jenkins / CI ortamı için headless ve viewport ayarları
     String headlessMode = System.getProperty("headless", "false");
     if ("true".equalsIgnoreCase(headlessMode)) {
     options.addArguments("--headless=new");
     options.addArguments("--disable-gpu");
     options.addArguments("--no-sandbox");
     options.addArguments("--disable-dev-shm-usage");
     }
     options.addArguments("--window-size=1920,1080");

     // Mevcut argümanlar
     options.addArguments("--disable-notifications");
     options.addArguments("--disable-popup-blocking");
     options.addArguments("--ignore-certificate-errors");
     options.addArguments("--ignore-ssl-errors");
     options.addArguments("--allow-running-insecure-content");

     // Driver oluştur
     driver = new ChromeDriver(options);
     wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT));

     // Temel konfigürasyonlar
     // Headless modda maximize() çalışmaz; sadece GUI modda devreye alıyoruz
     if (!"true".equalsIgnoreCase(headlessMode)) {
     driver.manage().window().maximize();
     }
     driver.manage().deleteAllCookies();
     driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(DEFAULT_TIMEOUT));

     browserAction("Chrome browser initialized", "Chrome");

     // Default URL'e git
     navigateToUrl(DEFAULT_URL);

     } catch (Exception e) {
     logException("Opening Chrome driver", e);
     throw new RuntimeException("Failed to initialize Chrome driver", e);
     }
     }



    /**
         * Driver'ı kapatır
         */
        public static void driverQuit() {
            try {
                if (driver != null) {
                    stepInfo("Closing browser");
                    driver.quit();
                    driver = null;
                    wait = null;
                    browserAction("Browser closed", "Chrome");
                }
            } catch (Exception e) {
                logException("Closing driver", e);
            }
        }

        /**
         * Timeout ayarlarını değiştirir
         */
        public static void setImplicitWait(int seconds) {
            try {
                driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(seconds));
                info("Implicit wait set to: " + seconds + " seconds");
            } catch (Exception e) {
                logException("Setting implicit wait", e);
            }
        }

        /**
         * Element temizleyip text girer
         */
        public static void clearAndType(WebElement element, String text, String elementDescription) {
            try {
                // Null kontrolü
                if (element == null) {
                    throw new IllegalArgumentException("Element cannot be null");
                }
                if (text == null) {
                    text = "";
                }

                actionInfo("Clear and type: " + text, elementDescription);

                // Element durumu kontrolü
                if (!element.isDisplayed() || !element.isEnabled()) {
                    throw new RuntimeException("Element is not interactable: " + elementDescription);
                }

                // Bekleme ekle
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                wait.until(ExpectedConditions.elementToBeClickable(element));

                // Clear işlemi
                element.clear();

                // Clear başarılı oldu mu kontrol et
                String currentValue = element.getDomProperty("value");
                if (currentValue != null && !currentValue.isEmpty()) {
                    element.sendKeys(Keys.CONTROL + "a");
                    element.sendKeys(Keys.DELETE);
                }

                // Metin girişi
                element.sendKeys(text);

                stepInfo("Text entered successfully: " + text);

            } catch (Exception e) {
                logException("Clear and type operation", e);
                TakeScreenShot.takeFailureScreenshot("clearAndType", driver, "Failed to enter text: " + text);
                throw new RuntimeException("Failed to enter text: " + text + " for element: " + elementDescription, e);
            }
        }

        /**
         * Element click işlemi
         */
        public static void clickElement(WebElement element, String elementDescription) {
            try {
                actionInfo("Click", elementDescription);
                WebDriverWait localWait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT));
                localWait.until(ExpectedConditions.elementToBeClickable(element));
                element.click();
                stepInfo("Element clicked successfully: " + elementDescription);
            } catch (Exception e) {
                logException("Click operation", e);
                TakeScreenShot.takeFailureScreenshot("clickElement", driver, "Failed to click: " + elementDescription);
                throw new RuntimeException("Failed to click element: " + elementDescription, e);
            }
        }


    public static void waitSeconds(int seconds) {
        try {
            Thread.sleep(seconds * 1000L); // ✅ Mükemmel!
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Wait interrupted: " + e.getMessage());
        }
    }





        public static WebElement findElementXpath(String xpath) {
            try {
                WebElement element = driver.findElement(By.xpath(xpath));
                debug("Element found by xpath: " + xpath);
                return element;
            } catch (NoSuchElementException e) {
                error("Element not found by xpath: " + xpath);
                TakeScreenShot.takeFailureScreenshot("findElementXpath", driver, "Element not found: " + xpath);
                logException("Finding element by xpath", e);
                throw e;
            }
        }

        /**
         * XPath ile element bulur (wait ile)
         */
        public static WebElement findElementXpathWithWait(String xpath, int timeoutSeconds) {
            if (driver == null) {
                throw new IllegalStateException("WebDriver is not initialized");
            }
            if (xpath == null || xpath.trim().isEmpty()) {
                throw new IllegalArgumentException("XPath cannot be null or empty");
            }
            if (timeoutSeconds <= 0) {
                throw new IllegalArgumentException("Timeout must be positive");
            }

            try {
                debug("Searching for element with xpath: " + xpath);

                WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
                WebElement element = customWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));

                debug("Element found by xpath: " + xpath);
                return element;

            } catch (TimeoutException e) {
                error("Element not found within " + timeoutSeconds + " seconds: " + xpath);
                TakeScreenShot.takeFailureScreenshot("findElementXpathWithWait", driver, "Element not found: " + xpath);
                logException("Finding element by xpath with wait", e);
                throw new NoSuchElementException("Element not found after " + timeoutSeconds + " seconds: " + xpath, e);

            } catch (Exception e) {
                error("Unexpected error while finding element: " + xpath);
                TakeScreenShot.takeFailureScreenshot("findElementXpathWithWait", driver, "Error finding element: " + xpath);
                logException("Finding element by xpath with wait", e);
                throw new RuntimeException("Error finding element: " + xpath, e);
            }


        }

        /**
         * ID ile element bulur (wait ile)
         */
        public static WebElement findElementIdWithWait(String id, int timeoutSeconds) {
            try {
                WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
                WebElement element = customWait.until(ExpectedConditions.presenceOfElementLocated(By.id(id)));
                debug("Element found by id with wait: " + id);
                return element;
            } catch (TimeoutException e) {
                error("Element not found by id within " + timeoutSeconds + " seconds: " + id);
                TakeScreenShot.takeFailureScreenshot("findElementIdWithWait", driver, "Element not found: " + id);
                logException("Finding element by id with wait", e);
                throw new NoSuchElementException("Element not found after " + timeoutSeconds + " seconds: " + id, e);
            }
        }

        /**
         * Class name ile element bulur (wait ile)
         */
        public static WebElement findElementClassNameWithWait(String className, int timeoutSeconds) {
            try {
                WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
                WebElement element = customWait.until(ExpectedConditions.presenceOfElementLocated(By.className(className)));
                debug("Element found by class name with wait: " + className);
                return element;
            } catch (TimeoutException e) {
                error("Element not found by class name within " + timeoutSeconds + " seconds: " + className);
                TakeScreenShot.takeFailureScreenshot("findElementClassNameWithWait", driver, "Element not found: " + className);
                logException("Finding element by class name with wait", e);
                throw new NoSuchElementException("Element not found after " + timeoutSeconds + " seconds: " + className, e);
            }
        }

        /**
         * CSS Selector ile element bulur (wait ile)
         */
        public static WebElement findElementCssSelectorWithWait(String cssSelector, int timeoutSeconds) {
            try {
                WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
                WebElement element = customWait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(cssSelector)));
                debug("Element found by CSS selector with wait: " + cssSelector);
                return element;
            } catch (TimeoutException e) {
                error("Element not found by CSS selector within " + timeoutSeconds + " seconds: " + cssSelector);
                TakeScreenShot.takeFailureScreenshot("findElementCssSelectorWithWait", driver, "Element not found: " + cssSelector);
                logException("Finding element by CSS selector with wait", e);
                throw new NoSuchElementException("Element not found after " + timeoutSeconds + " seconds: " + cssSelector, e);
            }
        }

        /**
         * Name ile element bulur (wait ile)
         */
        public static WebElement findElementNameWithWait(String name, int timeoutSeconds) {
            try {
                WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
                WebElement element = customWait.until(ExpectedConditions.presenceOfElementLocated(By.name(name)));
                debug("Element found by name with wait: " + name);
                return element;
            } catch (TimeoutException e) {
                error("Element not found by name within " + timeoutSeconds + " seconds: " + name);
                TakeScreenShot.takeFailureScreenshot("findElementNameWithWait", driver, "Element not found: " + name);
                logException("Finding element by name with wait", e);
                throw new NoSuchElementException("Element not found after " + timeoutSeconds + " seconds: " + name, e);
            }
        }

        /**
         * Link text ile element bulur (wait ile)
         */
        public static WebElement findElementLinkTextWithWait(String linkText, int timeoutSeconds) {
            try {
                WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
                WebElement element = customWait.until(ExpectedConditions.presenceOfElementLocated(By.linkText(linkText)));
                debug("Element found by link text with wait: " + linkText);
                return element;
            } catch (TimeoutException e) {
                error("Element not found by link text within " + timeoutSeconds + " seconds: " + linkText);
                TakeScreenShot.takeFailureScreenshot("findElementLinkTextWithWait", driver, "Element not found: " + linkText);
                logException("Finding element by link text with wait", e);
                throw new NoSuchElementException("Element not found after " + timeoutSeconds + " seconds: " + linkText, e);
            }
        }

        /**
         * Tag name ile element bulur (wait ile)
         */
        public static WebElement findElementTagNameWithWait(String tagName, int timeoutSeconds) {
            try {
                WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
                WebElement element = customWait.until(ExpectedConditions.presenceOfElementLocated(By.tagName(tagName)));
                debug("Element found by tag name with wait: " + tagName);
                return element;
            } catch (TimeoutException e) {
                error("Element not found by tag name within " + timeoutSeconds + " seconds: " + tagName);
                TakeScreenShot.takeFailureScreenshot("findElementTagNameWithWait", driver, "Element not found: " + tagName);
                logException("Finding element by tag name with wait", e);
                throw new NoSuchElementException("Element not found after " + timeoutSeconds + " seconds: " + tagName, e);
            }
        }

        // ============ ADDITIONAL METHODS ============

        /**
         * Dropdown'dan element seçer
         */
        public static void selectElementFromDropdown(WebElement dropdown, String optionText, String dropdownDescription) {
            try {
                actionInfo("Select from dropdown: " + optionText, dropdownDescription);
                Select select = new Select(dropdown);
                select.selectByVisibleText(optionText);
                stepInfo("Option selected from dropdown: " + optionText);
            } catch (Exception e) {
                logException("Selecting from dropdown", e);
                TakeScreenShot.takeFailureScreenshot("selectElementFromDropdown", driver, "Failed to select: " + optionText);
                throw new RuntimeException("Failed to select from dropdown: " + optionText, e);
            }
        }

        /**
         * Dosya upload işlemi
         * güncellendi
         */
        public static void fileUpload(String xPath, File filePath, String description) {
            try {
                // Null ve dosya varlık kontrolü
                if (filePath == null || !filePath.exists()) {
                    throw new IllegalArgumentException("File does not exist: " + filePath);
                }

                actionInfo("File upload", description);
                WebElement fileInput = driver.findElement(By.xpath(xPath)); // xPath parametresini kullan
                fileInput.sendKeys(filePath.getAbsolutePath());
                stepInfo("File uploaded successfully: " + filePath.getName());
            } catch (Exception e) {
                logException("File upload", e);
                TakeScreenShot.takeFailureScreenshot("fileUpload", driver, "Failed to upload file: " + filePath.getName());
                throw new RuntimeException("Failed to upload file: " + filePath.getName(), e);
            }
        }
        /**
         * OS dialog üzerinden dosya upload eder.
         * Upload butonuna tıklar, OS dosya seçme dialog'u açılır,
         * Robot ile dosya yolunu yapıştırıp Enter'a basar, dialog kapanır.
         * Dosya src/test/resources klasöründen çekilir.
         */
        public static void uploadFileViaDialog(WebElement uploadButton, String fileName, String description) {
            File uploadFile = null;
            try {
                if (uploadButton == null) {
                    throw new IllegalArgumentException("Upload button cannot be null");
                }
                if (fileName == null || fileName.trim().isEmpty()) {
                    throw new IllegalArgumentException("File name cannot be null or empty");
                }

                actionInfo("Upload file via OS dialog: " + fileName, description);

                // src/test/resources klasöründen dosya yolu oluştur
                String resourcesPath = System.getProperty("user.dir")
                        + File.separator + "src"
                        + File.separator + "test"
                        + File.separator + "resources";
                uploadFile = new File(resourcesPath, fileName);

                if (!uploadFile.exists() || !uploadFile.isFile()) {
                    TakeScreenShot.takeFailureScreenshot("uploadFileViaDialog", driver,
                            "File not found in resources: " + uploadFile.getAbsolutePath());
                    throw new IllegalArgumentException("File does not exist: " + uploadFile.getAbsolutePath());
                }

                // 1. Upload butonuna tıkla (OS dialog açılır)
                clickElement(uploadButton, description);
                stepInfo("Upload button clicked, waiting for OS dialog to open");

                // Dialog'un tamamen açılması için bekle
                Thread.sleep(2000);

                // 2. Dosya yolunu clipboard'a kopyala
                StringSelection selection = new StringSelection(uploadFile.getAbsolutePath());
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(selection, null);
                debug("File path copied to clipboard: " + uploadFile.getAbsolutePath());

                // 3. Robot ile Ctrl+V (yapıştır) ve Enter (onayla)
                Robot robot = new Robot();
                robot.keyPress(KeyEvent.VK_CONTROL);
                robot.keyPress(KeyEvent.VK_V);
                robot.keyRelease(KeyEvent.VK_V);
                robot.keyRelease(KeyEvent.VK_CONTROL);
                Thread.sleep(500);

                robot.keyPress(KeyEvent.VK_ENTER);
                robot.keyRelease(KeyEvent.VK_ENTER);

                // 4. Dialog kapanması ve upload işleminin başlaması için bekle
                Thread.sleep(1500);

                stepInfo("File uploaded via OS dialog successfully: " + uploadFile.getName());

            } catch (Exception e) {
                logException("Upload file via OS dialog", e);
                String failedFile = (uploadFile != null) ? uploadFile.getName() : fileName;
                TakeScreenShot.takeFailureScreenshot("uploadFileViaDialog", driver,
                        "Failed to upload via dialog: " + failedFile);
                throw new RuntimeException("Failed to upload file via dialog: " + fileName, e);
            }
        }

        /**
         * Proje içindeki src/test/resources klasöründen dosya upload eder.
         * Hata durumunda screenshot alır.
         */
        public static void uploadFileFromProject(String xpath, String fileName, String description) {
            File uploadFile = null;
            try {
                if (xpath == null || xpath.trim().isEmpty()) {
                    throw new IllegalArgumentException("XPath cannot be null or empty");
                }
                if (fileName == null || fileName.trim().isEmpty()) {
                    throw new IllegalArgumentException("File name cannot be null or empty");
                }

                actionInfo("File upload from project resources: " + fileName, description);

                String resourcesPath = System.getProperty("user.dir")
                        + File.separator + "src"
                        + File.separator + "test"
                        + File.separator + "resources";
                uploadFile = new File(resourcesPath, fileName);

                if (!uploadFile.exists() || !uploadFile.isFile()) {
                    TakeScreenShot.takeFailureScreenshot("uploadFileFromProject", driver,
                            "File not found in resources: " + uploadFile.getAbsolutePath());
                    throw new IllegalArgumentException("File does not exist: " + uploadFile.getAbsolutePath());
                }

                WebElement fileInput = findElementXpathWithWait(xpath, DEFAULT_TIMEOUT);
                fileInput.sendKeys(uploadFile.getAbsolutePath());

                stepInfo("File uploaded successfully: " + uploadFile.getName()
                        + " (" + uploadFile.getAbsolutePath() + ")");

            } catch (Exception e) {
                logException("Upload file from project", e);
                String failedFile = (uploadFile != null) ? uploadFile.getName() : fileName;
                TakeScreenShot.takeFailureScreenshot("uploadFileFromProject", driver,
                        "Failed to upload file: " + failedFile);
                throw new RuntimeException("Failed to upload file from project: " + fileName, e);
            }
        }

        /**
         * Geri navigasyon
         */
        public static void navigateBack() {
            try {
                stepInfo("Navigating back");
                driver.navigate().back();
                pageNavigation("Back");
            } catch (Exception e) {
                logException("Navigate back", e);
                throw new RuntimeException("Failed to navigate back", e);
            }
        }

        /**
         * İleri navigasyon
         */
        public static void navigateForward() {
            try {
                stepInfo("Navigating forward");
                driver.navigate().forward();
                pageNavigation("Forward");
            } catch (Exception e) {
                logException("Navigate forward", e);
                throw new RuntimeException("Failed to navigate forward", e);
            }
        }

        /**
         * URL'e navigasyon
         */
        public static void navigateToUrl(String url) {
            try {
                stepInfo("Navigating to URL: " + url);
                driver.get(url);
                pageNavigation(url);

                // Sayfanın yüklenmesini bekle
                wait.until(ExpectedConditions.jsReturnsValue("return document.readyState === 'complete'"));

            } catch (Exception e) {
                logException("Navigate to URL", e);
                throw new RuntimeException("Failed to navigate to URL: " + url, e);
            }
        }

        /**
         * Element görünür olana kadar bekler
         */
        public static WebElement waitForElementVisible(By locator, int timeoutSeconds) {
            try {
                WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
                return customWait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            } catch (Exception e) {
                logException("Waiting for element visibility", e);
                return null;
            }
        }

        /**
         * Element clickable olana kadar bekler
         */
        public static WebElement waitForElementClickable(By locator, int timeoutSeconds) {
            try {
                WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
                return customWait.until(ExpectedConditions.elementToBeClickable(locator));
            } catch (Exception e) {
                logException("Waiting for element clickable", e);
                return null;
            }
        }

        /**
         * Sayfanın tamamen yüklenmesini bekler
         */
        public static void waitForPageLoad() {
            try {
                wait.until(ExpectedConditions.jsReturnsValue("return document.readyState === 'complete'"));
                stepInfo("Page loaded completely");
            } catch (Exception e) {
                logException("Waiting for page load", e);
            }
        }

        /**
         * Element var mı kontrol eder
         */
        public static boolean isElementPresent(By locator) {
            try {
                driver.findElement(locator);
                return true;
            } catch (Exception e) {
                return false;
            }
        }

        /**
         * Multiple element bulur
         */
        public static List<WebElement> findElements(By locator) {
            try {
                List<WebElement> elements = driver.findElements(locator);
                debug("Found " + elements.size() + " elements");
                return elements;
            } catch (Exception e) {
                logException("Finding multiple elements", e);
                return null;
            }
        }

        /**
         * JavaScript ile element'e tıklar.
         * Normal click() çalışmadığında (element başka bir element tarafından
         * kapatıldığında, görünür alanın dışında olduğunda vb.) kullanılır.
         */
        public static void jsClickElement(WebElement element, String elementDescription) {
            try {
                if (element == null) {
                    throw new IllegalArgumentException("Element cannot be null");
                }
                actionInfo("JS Click", elementDescription);
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
                js.executeScript("arguments[0].click();", element);
                stepInfo("Element clicked successfully via JS: " + elementDescription);
            } catch (Exception e) {
                logException("JS Click operation", e);
                TakeScreenShot.takeFailureScreenshot("jsClickElement", driver, "Failed to JS click: " + elementDescription);
                throw new RuntimeException("Failed to JS click element: " + elementDescription, e);
            }
        }

        public void legitimateChecks() {
            // 1. Optional elements için
            if (isElementPresent(By.id("promo-banner"))) {
                WebElement optionalBanner = BaseStep.findElementIdWithWait("promo-banner", TimeOut.SHORT.value);
                BaseStep.clickElement(optionalBanner, "Promo banner");
            }
        }


    }



