package tests;

import base.BasicTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pages.ProfileSuggestionPage;

/**
 * Suite de tests pour les tooltips au survol des boutons Follow / Following
 * Vérifie que le texte du tooltip correspond à l'état actuel du bouton
 * Hérite de BasicTest qui gère le cycle de vie du navigateur (@BeforeAll, @BeforeEach, @AfterEach)
 */
public class TooltipTest extends BasicTest {

    // Logger SLF4J — Logback gère l'affichage en arrière-plan
    private static final Logger logger = LoggerFactory.getLogger(TooltipTest.class);

    /**
     * Vérifie que le tooltip affiche "Follow" au survol du bouton Follow
     * Scénario : navigation → survol Follow → assertion tooltip = "Follow"
     */
    @Test
    public void FollowHoverTest() {
        logger.info("Début du test FollowHoverTest");

        // Initialisation du Page Object avec la page créée dans BasicTest (@BeforeEach)
        ProfileSuggestionPage profileSuggestionPage = new ProfileSuggestionPage(page);

        // Étape 1 — Naviguer vers la page contenant le bouton Follow
        profileSuggestionPage.navigateTo();

        // Étape 2 — Survoler le bouton Follow pour déclencher l'apparition du tooltip
        profileSuggestionPage.followHover();

        // Étape 3 — Vérifier que le tooltip affiche bien "Follow"
        // Le tooltip doit correspondre au texte actuel du bouton
        Assertions.assertEquals("Follow", profileSuggestionPage.getTooltipText());

        logger.info("Fin du test FollowHoverTest — PASSED");
    }

    /**
     * Vérifie que le tooltip affiche "Unfollow" au survol du bouton Following
     * Scénario : navigation → clic Follow → attente "Following" → survol → assertion tooltip = "Unfollow"
     */
    @Test
    public void UnfollowHoverTest() {
        logger.info("Début du test UnfollowHoverTest");

        // Initialisation du Page Object avec la page créée dans BasicTest (@BeforeEach)
        ProfileSuggestionPage profileSuggestionPage = new ProfileSuggestionPage(page);

        // Étape 1 — Naviguer vers la page contenant le bouton Follow
        profileSuggestionPage.navigateTo();

        // Étape 2 — Cliquer sur "Follow" et attendre que le bouton devienne "Following"
        // Nécessaire pour pouvoir tester le tooltip du bouton "Following"
        profileSuggestionPage.clickFollow();
        profileSuggestionPage.waitFollowing();

        // Étape 3 — Survoler le bouton "Following" pour déclencher l'apparition du tooltip
        profileSuggestionPage.followingHover();

        // Étape 4 — Vérifier que le tooltip affiche bien "Unfollow"
        // Au survol de "Following", le tooltip indique l'action disponible : se désabonner
        Assertions.assertEquals("Unfollow", profileSuggestionPage.getTooltipText());

        logger.info("Fin du test UnfollowHoverTest — PASSED");
    }
}