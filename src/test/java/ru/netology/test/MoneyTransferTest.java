package ru.netology.test;

import lombok.val;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoneyTransferTest {

    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
    }

    @Test
    void shouldTransferMoneyBetweenOwnCards() {
        val loginPage = new LoginPage();
        val authInfo = DataHelper.getAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        val verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        verificationPage.validVerify(verificationCode);
    }

    @Test
    void shouldTransferMoneyFrom01To02Card() {
        int amount = 100 + (int) (Math.random() * 5000);
        val loginPage = new LoginPage();
        val authInfo = DataHelper.getAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        val verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        val dashboard = verificationPage.validVerify(verificationCode);
        val cardBalance01 = dashboard.getCardBalance01();
        val cardBalance02 = dashboard.getCardBalance02();
        val cardInfo = DataHelper.Card.getCardInfo01();
        val transferMoney = dashboard.cardRefillButtonClick();
        transferMoney.transfer(cardInfo, amount);
        val cardBalanceAfterSendFirst = DataHelper.Card.cardBalanceAfterSendMoney(cardBalance01, amount);
        val cardBalanceAfterSendSecond = DataHelper.Card.cardBalanceAfterGetMoney(cardBalance02, amount);
        assertEquals(cardBalanceAfterSendFirst, dashboard.getCardBalance01());
        assertEquals(cardBalanceAfterSendSecond, dashboard.getCardBalance02());
    }

}
