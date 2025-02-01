package com.example.registro.ui.auth.registro;

import java.util.Arrays;
import java.util.List;

public class SlurChecker {
    public static final List<String> ENGLISH_SLURS = Arrays.asList(
            "idiot", "moron", "bigot", "racist", "sexist", "homophobe",
            "asshole", "bitch", "damn", "shit", "fuck", "cunt",
            "nigger", "faggot", "dyke", "whore", "slut",
            "retard", "dickhead", "motherfucker"
    );

    public static final List<String> SPANISH_SLURS = Arrays.asList(
            "idiota", "estúpido", "imbécil", "racista", "sexista", "homófobo",
            "hijoputa", "puta", "zorra", "cabrón", "maricón",
            "joder", "mierda", "coño", "polla", "gilipollas",
            "capullo", "subnormal", "retrasado",
            "desgraciado", "hijo de puta"
    );

    public static boolean containsSlur(String text) {
        String lowerCaseText = text.toLowerCase();
        for (String slur : ENGLISH_SLURS) {
            if (lowerCaseText.contains(slur)) {
                return true;
            }
        }
        for (String slur : SPANISH_SLURS) {
            if (lowerCaseText.contains(slur)) {
                return true;
            }
        }
        return false;
    }
}