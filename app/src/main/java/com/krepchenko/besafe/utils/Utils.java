package com.krepchenko.besafe.utils;

/**
 * Created by Ann on 31.01.2016.
 */
public class Utils {

    private static final String patternStartSpecSymbol = "^(\\p{L}{1}).*$";
    private static final String patternTelNumber = "^\\s*(?:\\+?(\\d{1,3}))?[-. (]*(\\d{3})[-. )]*(\\d{3})[-. ]*(\\d{4})(?: *x(\\d+))?\\s*$";

    public static boolean checkTelNumberWithRegExp(String telNumber) {
        return telNumber.matches(patternTelNumber);
    }

    public static boolean checkNameForStartSpecSymbols(String name){
        return name.matches(patternStartSpecSymbol);
    }
}
