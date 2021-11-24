package com.mindhub.homebanking;


import com.mindhub.homebanking.utils.CardUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.hamcrest.MatcherAssert.assertThat; //Libreria para assertThat
import static org.hamcrest.Matchers.*;

@SpringBootTest
public class CardUtilsTests {
    @Test
    public void cardNumberIsCreated(){
        int cardNumber = CardUtils.getRandomNumber(1000,9999);
        assertThat(cardNumber,greaterThan(0));
    }

}
