package ru.netology.delivery.test;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import ru.netology.delivery.data.DataGenerator;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

class DeliveryTest {
    @BeforeEach
    void startSetup() {
        open("http://localhost:9999");
        Configuration.holdBrowserOpen = true;
    }

    @Test
    void happyCase() {
        String firstRegistrationDay = DataGenerator.generateDate(5);
        String secondRegistrationDay = DataGenerator.generateDate(10);
        DataGenerator.UserInfo user = DataGenerator.Registration.generateUser("ru");
        $("[data-test-id=city] input").setValue(user.getCity());
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.LEFT_CONTROL, "a"), Keys.DELETE);
        $("[data-test-id=date] input").setValue(firstRegistrationDay);
        $("[data-test-id=name] input").setValue(user.getName());
        $("[data-test-id=phone] input").setValue(user.getPhone());
        $("[data-test-id=agreement] .checkbox__box").click();
        $(".button__text").click();

        $("[data-test-id=success-notification]").shouldBe(visible);
        $("[data-test-id=success-notification] .notification__title").shouldHave(text("Успешно!"));
        $("[data-test-id=success-notification] .notification__content").shouldHave(text("Встреча успешно запланирована на " + firstRegistrationDay));

        $("[data-test-id=success-notification] .icon-button").click();

        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.LEFT_CONTROL, "a"), Keys.DELETE);
        $("[data-test-id=date] input").setValue(secondRegistrationDay);
        $(".button__text").click();

        $("[data-test-id=replan-notification]").shouldBe(visible);
        $("[data-test-id=replan-notification] .notification__title").shouldHave(text("Необходимо подтверждение"));
        $("[data-test-id=replan-notification] .notification__content").shouldHave(text("У вас уже запланирована встреча на другую дату. Перепланировать?"));
        $("[data-test-id=replan-notification] .button__content").click();

        $("[data-test-id=success-notification]").shouldBe(visible);
        $("[data-test-id=success-notification] .notification__title").shouldHave(text("Успешно!"));
        $("[data-test-id=success-notification] .notification__content").shouldHave(text("Встреча успешно запланирована на " + secondRegistrationDay));
    }
}
