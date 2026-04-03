package tests;

import base.BasicTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pages.ProfileSuggestionPage;

public class TooltipTest extends BasicTest {
    // Logger SLF4J — Logback gère l'affichage en arrière-plan
    private static final Logger logger = LoggerFactory.getLogger(TooltipTest.class);

    @Test
    public void FollowHoverTest() {
        logger.info("Début du test FollowHover");
        // Initialisation du Page Object avec la page créée dans BasicTest (@BeforeEach)
        ProfileSuggestionPage profileSuggestionPage = new ProfileSuggestionPage(page);
        // Étape 1 — Naviguer vers la page contenant le bouton Follow
        profileSuggestionPage.navigateTo();
        profileSuggestionPage.followHover();
        /* Étape 2 — Mouse Hover sur le button follow
                     et vérifier que le text de tooltip correspondant à celle du button          */
        Assertions.assertEquals("Follow", profileSuggestionPage.getTooltipText());
        logger.info("Fin du test FollowHover — PASSED");
    }

    @Test
    public void UnfollowHoverTest() throws InterruptedException {
        logger.info("Début du test UnfollowHover");
        // Initialisation du Page Object avec la page créée dans BasicTest (@BeforeEach)
        ProfileSuggestionPage profileSuggestionPage = new ProfileSuggestionPage(page);
        // Étape 1 — Naviguer vers la page contenant le bouton Follow
        profileSuggestionPage.navigateTo();
        // Étape 2 — Cliquer sur "Follow" et attendre que le texte devienne "Following"
        profileSuggestionPage.clickFollow();
        profileSuggestionPage.waitFollowing();
        // Étape 3 — Mouse Hover sur le button following
        profileSuggestionPage.followingHover();
        // Étape 4 — Vérifier que le text de tooltip devient UNFOLLOW
        Assertions.assertEquals("Unfollow", profileSuggestionPage.getTooltipText());
        logger.info("Fin du test UnfollowHover — PASSED");
    }
}

