package com.zeta.logging;

public class Logger {

    private static Logger instance;

    private static final String RESET = "\u001B[0m";
    private static final String WHITE = "\u001B[37m";
    private static final String RED = "\u001B[31m";
    private static final String YELLOW = "\u001B[33m";

    private Logger() {
    }

    public static synchronized Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    public void info(String message) {
        System.out.println(WHITE + message + RESET);
    }


    public void error(String message) {
        System.out.println(RED + message + RESET);
    }

    public void warn(String message) {
        System.out.println(YELLOW + message + RESET);
    }
}
