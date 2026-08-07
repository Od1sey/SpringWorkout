package com.util;

public class ParseUtils {

    public static int parseInt(String value, String errorString){
        try {
           return Integer.parseInt(value);
        } catch (NumberFormatException ex){
          throw new IllegalArgumentException(errorString);
        }
    }

}
