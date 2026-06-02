import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

/**
 * Sentinal projesinin tüm modül testlerini sıralı şekilde çalıştıran Suite sınıfı.
 *
 * Çalıştırma:
 *   mvn test -Dtest=DhrAllModulesSuite
 *
 * Yeni bir modül test sınıfı eklediğinizde:
 *   1. Yeni sınıfı yazın (örn: DhrLeaveModuleTest.java)
 *   2. Aşağıdaki @SelectClasses listesine ekleyin
 *   3. Suite'i çalıştırdığınızda otomatik olarak sıralı çalışır
 *
 * Sıralama kuralı:
 *   - Sınıflar @SelectClasses içindeki yazım sırasına göre çalışır
 *   - Her sınıfın içindeki @Test metodları @Order annotation'ına göre sıralı çalışır
 */
@Suite
@SuiteDisplayName("Sentinal All Pages Test Suite")
@SelectClasses({
       DashboardPageTest.class,
        ProjectPageTest.class,
        RequirementsPageTest.class,
        TestCasesPageTest.class,
        BugsPageTest.class,
        SprintPageTest.class,
        DeploymentsPageTest.class,
        ReportsPageTest.class,
        WorkflowPageTest.class,
        SettingsPageTest.class,
        LogoutTest.class

})
@ConfigurationParameter(
        key = "junit.jupiter.testclass.order.default",
        value = "org.junit.jupiter.api.ClassOrderer$OrderAnnotation"
)
public class SentinalAllModulesSuite {
    // Suite sınıfı boş kalır - sadece annotation'lar konfigürasyonu sağlar
}
