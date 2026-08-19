package de.raumfahrt;

public final class RaumfahrtApp {

    private RaumfahrtApp() {
    }

    public static void main(String[] args) {
        System.out.println(startupMessage());
    }

    static String startupMessage() {
        return "Raumfahrt gestartet.";
    }
}
