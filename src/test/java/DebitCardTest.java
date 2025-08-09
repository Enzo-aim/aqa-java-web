import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DebitCardTest {
    WebDriver driver;

    @BeforeAll
    static void setupAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setup() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        // options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999");
    }

    @AfterEach
    void teardown() {
        driver.quit();
    }

    @DisplayName("Успешная отправка формы с валидными данными")
    @Test
    void testSuccessSendForm() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Иван");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79827124720");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.tagName("button")).click();
        String actual = driver.findElement(By.cssSelector("[data-test-id='order-success']")).getText().trim();
        String expected = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        Assertions.assertEquals(expected, actual);
    }

    @DisplayName("Отправка формы с не валидными данными в поле Имя")
    @Test
    void testValidateNameForm() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Ивan~!22##$%$#^%&^*&(*)gfhdf?>}{';|");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79827124720");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.tagName("button")).click();
        String actual = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText();
        String expected = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        Assertions.assertEquals(expected, actual);
    }

    @DisplayName("Отправка формы с не заполнеными данными поля Имя")
    @Test
    void testNoNameForm() {
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+70638623720");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.tagName("button")).click();
        String actual = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText();
        String expected = "Поле обязательно для заполнения";
        Assertions.assertEquals(expected, actual);
    }

    @DisplayName("Отправк формы с невалидными данными поле телефон")
    @Test
    void testValidatePhoneForm() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Иван");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+7052819402");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.tagName("button")).click();
        String actual = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText();
        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        Assertions.assertEquals(expected, actual);
    }

    @DisplayName("Отправка формы с незаполненым полем телефон")
    @Test
    void testNoPhone() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Иван");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.tagName("button")).click();
        String actual = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText();
        String expected = "Поле обязательно для заполнения";
        Assertions.assertEquals(expected, actual);
    }

    @DisplayName("Отправка формы с неотмеченным чек боксом")
    @Test
    void testInvalidCheckBox() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Иван");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+70447813541");
        driver.findElement(By.tagName("button")).click();
        WebElement rowWithCheckBox = driver.findElement(By.cssSelector("[data-test-id=\"agreement\"]"));
        String attributeValue = rowWithCheckBox.getAttribute("class");
        assertTrue(attributeValue.contains("input_invalid"), "Ожидалось что атрибут class содержит значение 'input_invalid'");

    }
}


