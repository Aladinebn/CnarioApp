package tests;

import base.BasicTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pages.ProfileSuggestionPage;

/**
 * Suite de tests pour le bouton Follow / Unfollow
 * Couvre 3 scénarios : Follow, Processing, Unfollow
 * Hérite de BasicTest qui gère le cycle de vie du navigateur (@BeforeAll, @BeforeEach, @AfterEach)
 */
public class FollowTest extends BasicTest {

    // Logger SLF4J — Logback gère l'affichage en arrière-plan
    private static final Logger logger = LoggerFactory.getLogger(FollowTest.class);

    /**
     * Vérifie que le bouton passe de "Follow" à "Following" après le clic
     * Scénario : navigation → clic Follow → attente → assertion "Following"
     */
    @Test
    public void followWithSuccess() {
        logger.info("Début du test followWithSuccess");

        // Initialisation du Page Object avec la page créée dans BasicTest (@BeforeEach)
        ProfileSuggestionPage profileSuggestionPage = new ProfileSuggestionPage(page);

        // Étape 1 — Naviguer vers la page contenant le bouton Follow
        profileSuggestionPage.navigateTo();

        // Étape 2 — Cliquer sur "Follow" et attendre que le texte devienne "Following"
        profileSuggestionPage.clickFollow();

        // Étape 3 — Vérifier que le bouton affiche bien "Following" après le clic
        // waitFollowing() attend le changement du DOM avant de retourner le texte
        Assertions.assertEquals("Following", profileSuggestionPage.waitFollowing());

        logger.info("Fin du test followWithSuccess — PASSED");
    }

    /**
     * Vérifie que l'état intermédiaire "Processing..." est visible immédiatement après le clic
     * Scénario : navigation → clic Follow → assertion "Processing..." avant "Following"
     */
    @Test
    public void checkProcessingText() {
        logger.info("Début du test checkProcessingText");

        // Initialisation du Page Object avec la page créée dans BasicTest (@BeforeEach)
        ProfileSuggestionPage profileSuggestionPage = new ProfileSuggestionPage(page);

        // Étape 1 — Naviguer vers la page contenant le bouton Follow
        profileSuggestionPage.navigateTo();

        // Étape 2 — Cliquer sur "Follow" et capturer l'état intermédiaire "Processing..."
        // Le bouton affiche brièvement "Processing..." avant de passer à "Following"
        profileSuggestionPage.clickFollow();
        Assertions.assertEquals("Processing...", profileSuggestionPage.getTextProcessing());

        logger.info("Fin du test checkProcessingText — PASSED");
    }

    /**
     * Vérifie le cycle complet Follow → Unfollow
     * Scénario : navigation → clic Follow → attente "Following" → clic Unfollow → assertion "Follow"
     */
    @Test
    public void unfollowWithSuccess() {
        logger.info("Début du test unfollowWithSuccess");

        // Initialisation du Page Object avec la page créée dans BasicTest (@BeforeEach)
        ProfileSuggestionPage profileSuggestionPage = new ProfileSuggestionPage(page);

        // Étape 1 — Naviguer vers la page contenant le bouton Follow
        profileSuggestionPage.navigateTo();

        // Étape 2 — Cliquer sur "Follow" et attendre que le texte devienne "Following"
        profileSuggestionPage.clickFollow();
        profileSuggestionPage.waitFollowing();

        // Étape 3 — Cliquer sur "Unfollow" et attendre que le texte revienne à "Follow"
        profileSuggestionPage.clickUnfollow();

        // Étape 4 — Vérifier que le bouton affiche bien "Follow" après le désabonnement
        // waitUnfollowing() attend le changement du DOM avant de retourner le texte
        Assertions.assertEquals("Follow", profileSuggestionPage.waitUnfollowing());

        logger.info("Fin du test unfollowWithSuccess — PASSED");
    }
}