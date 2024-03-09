package iut.dam.gestionresidence.entities;

public class TokenManager {
    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        TokenManager.token = token;
    }

    private static String token;
}
