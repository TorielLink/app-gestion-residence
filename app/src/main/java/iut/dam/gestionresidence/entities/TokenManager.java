package iut.dam.gestionresidence.entities;

public class TokenManager {
    private static String token;

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        TokenManager.token = token;
    }
}
