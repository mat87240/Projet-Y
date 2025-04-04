package utils.web;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class DecodeToken {

	public static void extractToken(String extratoken) {

        // Vérification si la réponse contient "token"
        if (extratoken.contains("\"token\":\"")) {
            int startIndex = extratoken.indexOf("\"token\":\"") + 9; // Début du token après "token":" (9 caractères)
            int endIndex = extratoken.indexOf("\"", startIndex); // Trouver la fin du token

            if (endIndex > startIndex) {
                String token = extratoken.substring(startIndex, endIndex);
                System.out.println("Token : " + token);
                DecodeToken.decodeJwt(token);
            } else {
                System.out.println("Erreur : Impossible d'extraire le token.");
            }
        } else {
            System.out.println("Erreur : Token non trouvé dans la réponse.");
        }
    }

    public static void decodeJwt(String token) {
        try {
            // Remplacer les caractères Base64URL par Base64 standard
            String correctedToken = token.replace('-', '+').replace('_', '/');
            
            // Ajouter du padding si nécessaire
            int paddingLength = correctedToken.length() % 4;
            if (paddingLength > 0) {
                correctedToken += "====".substring(paddingLength);
            }

            // Séparer le token
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                System.out.println("Erreur : Le token JWT est invalide.");
                return;
            }

            // Décoder le payload
            String payloadJson = new String(Base64.getDecoder().decode(parts[1]), StandardCharsets.UTF_8);

            // Extraire la partie "data" du JSON manuellement
            int startIndex = payloadJson.indexOf("\"data\":");
            if (startIndex == -1) {
                System.out.println("Erreur : Aucune donnée 'data' trouvée dans le payload.");
                return;
            }

            // Trouver la fin de l'objet "data" (en supposant un format JSON valide)
            int braceCount = 0;
            int i = startIndex + 7; // Position après "data": 
            if (payloadJson.charAt(i) != '{') {
                System.out.println("Erreur : Format inattendu pour 'data'.");
                return;
            }

            for (; i < payloadJson.length(); i++) {
                if (payloadJson.charAt(i) == '{') braceCount++;
                if (payloadJson.charAt(i) == '}') braceCount--;
                if (braceCount == 0) break;
            }

            if (braceCount != 0) {
                System.out.println("Erreur : Format JSON invalide.");
                return;
            }

            String data = payloadJson.substring(startIndex + 7, i + 1);
            System.out.println("Données du payload : " + data);
        } catch (Exception e) {
            System.out.println("Erreur lors du décodage du JWT : " + e.getMessage());
        }
    }
}
