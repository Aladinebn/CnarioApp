package tests;

import base.BasicTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pages.ProfileSuggestionPage;

/**
 * Suite de tests pour la suppression de profils
 * Vérifie que le nombre de profils diminue exactement de N après N suppressions
 * Hérite de BasicTest qui gère le cycle de vie du navigateur (@BeforeAll, @BeforeEach, @AfterEach)
 */
public class DeleteTest extends BasicTest {

    // Logger SLF4J — Logback gère l'affichage en arrière-plan
    private static final Logger logger = LoggerFactory.getLogger(DeleteTest.class);

    /**
     * Vérifie que N suppressions diminuent le nombre de profils de N
     * Flexible — modifier "nombreDeSuppression" pour tester différents scénarios
     * Scénario : navigation → comptage → N clics sur X → assertion count - N
     */
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
        // Modifier cette valeur pour tester différents scénarios
        int nombreDeSuppression = 4;
        for (int i = 0; i < nombreDeSuppression; i++) {
            logger.info("Suppression {} / {}", i + 1, nombreDeSuppression);
            profileSuggestionPage.clickDelete();
        }

        // Étape 4 — Stocker le nombre de profils après suppression
        int nombreDesProfilsApres = profileSuggestionPage.getCountProfileSuggestions();
        logger.info("Nombre de profils après suppression : {}", nombreDesProfilsApres);

        // Étape 5 — Vérifier que le nombre de profils a diminué exactement de N
        // Le message d'erreur précise combien de suppressions étaient attendues
        Assertions.assertEquals(
                nombreDesProfilsAvant - nombreDeSuppression,
                nombreDesProfilsApres,
                "Le nombre de profils aurait dû diminuer de " + nombreDeSuppression + " après suppression"
        );

        logger.info("Fin du test deleteProfile — PASSED : {} → {} ({} suppression(s))",
                nombreDesProfilsAvant, nombreDesProfilsApres, nombreDeSuppression);
    }
}