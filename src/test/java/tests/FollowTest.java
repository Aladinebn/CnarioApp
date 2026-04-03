package tests;

import base.BasicTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pages.ProfileSuggestionPage;

public class FollowTest extends BasicTest {

    // Logger SLF4J — Logback gère l'affichage en arrière-plan
    private static final Logger logger = LoggerFactory.getLogger(FollowTest.class);

    @Test
    public void followWithSuccess() {

        logger.info("Début du test followWithSuccess");
        // Initialisation du Page Object avec la page créée dans BasicTest (@BeforeEach)
        ProfileSuggestionPage profileSuggestionPage = new ProfileSuggestionPage(page);

        // Étape 1 — Naviguer vers la page contenant le bouton Follow
        profileSuggestionPage.navigateTo();

        // Étape 2 — Cliquer sur "Follow" et attendre que le texte devienne "Following"
        profileSuggestionPage.clickFollow();
        profileSuggestionPage.waitFollowing();

        // Étape 3 — Vérifier que le bouton affiche bien "Following" après le clic
        Assertions.assertEquals("Following", profileSuggestionPage.waitFollowing());

        logger.info("Fin du test followWithSuccess — PASSED");
    }

    @Test
    public void checkProcessingText() {

        logger.info("Début du test checkProcessingText");
        // Initialisation du Page Object avec la page créée dans BasicTest (@BeforeEach)
        ProfileSuggestionPage profileSuggestionPage = new ProfileSuggestionPage(page);

        // Étape 1 — Naviguer vers la page contenant le bouton Follow
        profileSuggestionPage.navigateTo();

        // Étape 2 — Cliquer sur "Follow" et visualiser "Processing..." avant que le texte devienne "Following"
        profileSuggestionPage.clickFollow();
        Assertions.assertEquals("Processing...", profileSuggestionPage.getTextProcessing());
        logger.info("test checkProcessingText: PASSED");
    }

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

        // Étape 3 — Cliquer sur "Unfollow" et attendre que le texte devienne "Follow"
        profileSuggestionPage.clickUnfollow();
        profileSuggestionPage.waitUnfollowing();

        Assertions.assertEquals("Follow", profileSuggestionPage.waitUnfollowing());
    }
}