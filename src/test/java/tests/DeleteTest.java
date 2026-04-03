package tests;

import base.BasicTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pages.ProfileSuggestionPage;

public class DeleteTest extends BasicTest {

    // Logger SLF4J — Logback gère l'affichage en arrière-plan
    private static final Logger logger = LoggerFactory.getLogger(DeleteTest.class);

    @Test
    public void deleteProfile() {
        logger.info("Début du test deleteProfile");

        // Initialisation du Page Object avec la page créée dans BasicTest (@BeforeEach)
        ProfileSuggestionPage profileSuggestionPage = new ProfileSuggestionPage(page);

        // Étape 1 — Naviguer vers la page contenant les suggestions de profils
        profileSuggestionPage.navigateTo();

        // Étape 2 — Stocker le nombre de profils avant suppression
        int nombreDesProfilsAvant = profileSuggestionPage.getCountProfileSuggestions();
        logger.info("Nombre de profils avant suppression : {}", nombreDesProfilsAvant);

        // Étape 3 — Effectuer N suppressions
        int nombreDeSuppression = 4; //  changer cette valeur selon le besoin
        for (int i = 0; i < nombreDeSuppression; i++) {
            logger.info("Suppression {} / {}", i + 1, nombreDeSuppression);
            profileSuggestionPage.clickDelete();
        }

        // Étape 4 — Stocker le nombre de profils après suppression
        int nombreDesProfilsApres = profileSuggestionPage.getCountProfileSuggestions();
        logger.info("Nombre de profils après suppression : {}", nombreDesProfilsApres);

        // Étape 5 — Vérifier que le nombre de profils a diminué exactement de N
        Assertions.assertEquals(
                nombreDesProfilsAvant - nombreDeSuppression,
                nombreDesProfilsApres,
                "Le nombre de profils aurait dû diminuer de 1 après suppression"
        );

        logger.info("Fin du test deleteProfile — PASSED : {} → {}", nombreDesProfilsAvant, nombreDesProfilsApres);
    }
}