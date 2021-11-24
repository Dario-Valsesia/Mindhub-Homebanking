package com.mindhub.homebanking.utils;

public final class CardUtils {
    private CardUtils() { // PRIVATE Para no poder instanciar la clase
    }

    public static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

}
