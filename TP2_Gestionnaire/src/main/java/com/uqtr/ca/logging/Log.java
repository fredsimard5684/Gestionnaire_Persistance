package com.uqtr.ca.logging;

import com.uqtr.ca.ConsoleColors;

public class Log {
    public static void logInfo(String msg, String color) {
        System.out.println(color + msg);
        System.out.print(ConsoleColors.RESET);
    }

}
