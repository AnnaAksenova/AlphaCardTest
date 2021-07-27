package ru.netolog.test;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.By;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class AlphaCardTest {
    private WebDriver driver;

    @BeforeAll
    public static void setupClass() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999");
    }

    @AfterEach
    void tearDown() {
        driver.quit();
        driver = null;
    }

    @Test
    void returnPositiveResponse() {
        driver.findElement(By.cssSelector("[data-test-id=\"name\"] input")).sendKeys("Иванов Иван");
        driver.findElement(By.cssSelector("[data-test-id=\"phone\"] input")).sendKeys("+75763547683");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.className("button")).click();
        String actualText = driver.findElement(By.cssSelector("[data-test-id=\"order-success\"]")).getText();
        String expectedText = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        assertEquals(expectedText.trim(), actualText.trim());
    }

    @Test
    void notRegisterUsersWithEmptyField1() {
        driver.findElement(By.cssSelector("[data-test-id=\"name\"] input")).sendKeys("");
        driver.findElement(By.cssSelector("[data-test-id=\"phone\"] input")).sendKeys("+75673423489");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.className("button")).click();
        String actualText = driver.findElement(By.cssSelector("[data-test-id=\"name\"].input_invalid .input__sub")).getText();
        String expectedText = "Поле обязательно для заполнения";
        assertEquals(expectedText.trim(), actualText.trim());
    }

    @Test
    void notRegisterUsersWithEmptyField2() {
        driver.findElement(By.cssSelector("[data-test-id=\"name\"] input")).sendKeys("Иванов Иван");
        driver.findElement(By.cssSelector("[data-test-id=\"phone\"] input")).sendKeys("");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.className("button")).click();
        String actualText = driver.findElement(By.cssSelector("[data-test-id=\"phone\"].input_invalid .input__sub")).getText();
        String expectedText = "Поле обязательно для заполнения";
        assertEquals(expectedText.trim(), actualText.trim());
    }

    @Test
    void notRegisterUsersWithLatinLetters() {
        driver.findElement(By.cssSelector("[data-test-id=\"name\"] input")).sendKeys("Bob Dylan");
        driver.findElement(By.cssSelector("[data-test-id=\"phone\"] input")).sendKeys("+74627682341");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.className("button")).click();
        String actualText = driver.findElement(By.cssSelector("[data-test-id=\"name\"].input_invalid .input__sub")).getText();
        String expectedText = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        assertEquals(expectedText.trim(), actualText.trim());
    }

    @Test
    void notRegisterUsersWithSymbolsInName() {
        driver.findElement(By.cssSelector("[data-test-id=\"name\"] input")).sendKeys("Петров+Водкин Иван ");
        driver.findElement(By.cssSelector("[data-test-id=\"phone\"] input")).sendKeys("+75673423489");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.className("button")).click();
        String actualText = driver.findElement(By.cssSelector("[data-test-id=\"name\"].input_invalid .input__sub")).getText();
        String expectedText = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        assertEquals(expectedText.trim(), actualText.trim());
    }

    @Test
    void notRegisterWithIncorrectNumberFormat() {
        driver.findElement(By.cssSelector("[data-test-id=\"name\"] input")).sendKeys("Иванов Иван");
        driver.findElement(By.cssSelector("[data-test-id=\"phone\"] input")).sendKeys("5643");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.className("button")).click();
        String actualText = driver.findElement(By.cssSelector("[data-test-id=\"phone\"].input_invalid .input__sub")).getText();
        String expectedText = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        assertEquals(expectedText.trim(), actualText.trim());
    }

    @Test
    void consentToDataProcessing() {
        driver.findElement(By.cssSelector("[data-test-id=\"name\"] input")).sendKeys("Иванов Иван");
        driver.findElement(By.cssSelector("[data-test-id=\"phone\"] input")).sendKeys("+75673423489");
        driver.findElement(By.className("button")).click();
        String actualText = driver.findElement(By.cssSelector("[data-test-id=\"agreement\"].input_invalid .checkbox__text")).getText();
        String expectedText = "Я соглашаюсь с условиями обработки и использования моих персональных данных и разрешаю сделать запрос в бюро кредитных историй";
        assertEquals(expectedText.trim(), actualText.trim());

    }

}
