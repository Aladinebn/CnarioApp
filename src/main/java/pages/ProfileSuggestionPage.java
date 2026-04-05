package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProfileSuggestionPage {

    // =====================================================================
    // LOGGER & CHAMPS
    // =====================================================================

    // Logger SLF4J — Logback gère l'affichage en arrière-plan
    private static final Logger logger = LoggerFactory.getLogger(ProfileSuggestionPage.class);

    // Instance de la page Playwright pour interagir avec le navigateur
    private final Page page;

    // Locators — sélecteurs stables des éléments de la page
    private final Locator followBtn;
    private final Locator followingBtn;
    private final Locator profileCard;
    private final Locator tooltip;
    private final Locator deleteBtn;

    // =====================================================================
    // CONSTRUCTEUR
    // =====================================================================

    /**
     * Reçoit la page Playwright initialisée dans BasicTest (@BeforeEach)
     * Tous les locators sont initialisés ici — une seule source de vérité
     */
    public ProfileSuggestionPage(Page page) {
        this.page = page;

        // Cible le 2ème bouton Follow — nth(0) est disabled (grisé)
        this.followBtn = page.locator("span[aria-label='Follow'] button").nth(1);

        // Cible le bouton après le clic — "Following" est unique dans le DOM
        this.followingBtn = page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Following").setExact(true));

        // Cible chaque carte de profil dans la liste des suggestions
        this.profileCard = page.locator("div.MuiPaper-root.MuiCard-root");

        // Cible le tooltip qui apparaît au survol d'un bouton
        this.tooltip = page.locator("[role='tooltip']");

        // Cible le bouton X de suppression du premier profil via XPath
        this.deleteBtn = page.locator("//div[@class='MuiPaper-root MuiPaper-elevation MuiPaper-rounded MuiPaper-elevation2 mb-6 css-x0wmhw']//div[2]//div[1]//div[1]//button[1]//*[name()='svg']");

        logger.info("ProfileSuggestionPage initialisé");
    }

    // =====================================================================
    // MÉTHODES PRIVÉES — logique commune réutilisable
    // =====================================================================

    /**
     * Attend qu'un élément soit visible dans le DOM avant de continuer
     * Évite les assertions prématurées sur des éléments pas encore chargés
     */
    private void wait(Locator locator, String logLabel) {
        logger.info("Attente de l'élément : '{}'", locator.innerText());
        locator.waitFor();
        logger.info("État changé vers : '{}'", logLabel);
    }

    /**
     * Clique sur un élément et logue l'action
     * Centralise la logique de clic pour éviter la duplication
     */
    private void clickBtn(Locator locator, String logLabel) {
        locator.click();
        logger.info("Bouton '{}' cliqué avec succès", logLabel);
    }

    /**
     * Survole un élément pour déclencher un tooltip ou un effet visuel
     */
    private void btnHover(Locator locator, String logLabel) {
        locator.hover();
        logger.info("Bouton '{}' survolé", logLabel);
    }

    /**
     * Retourne le texte d'un élément et logue l'action
     * Méthode générique utilisée par toutes les méthodes getText publiques
     */
    private String getText(Locator locator, String logLabel) {
        String text = locator.innerText();
        logger.info("'{}' : {}", text, logLabel);
        return text;
    }

    // =====================================================================
    // NAVIGATION
    // =====================================================================

    /**
     * Navigue vers la page contenant les suggestions de profils
     */
    public void navigateTo() {
        page.navigate("https://www.cnarios.com/concepts/button#try-it-yourself");
        logger.info("Navigation vers : https://www.cnarios.com/concepts/button#try-it-yourself");
    }

    // =====================================================================
    // ACTIONS — interactions avec les boutons
    // =====================================================================

    /**
     * Clique sur le bouton "Follow" du profil ciblé
     */
    public void clickFollow() {
        clickBtn(followBtn, followBtn.innerText());
    }

    /**
     * Clique sur le bouton "Following" pour se désabonner
     */
    public void clickUnfollow() {
        clickBtn(followingBtn, followingBtn.innerText());
    }

    /**
     * Survole le bouton "Follow" pour déclencher l'apparition du tooltip
     */
    public void followHover() {
        btnHover(followBtn, "Follow");
    }

    /**
     * Survole le bouton "Following" pour déclencher l'apparition du tooltip
     */
    public void followingHover() {
        btnHover(followingBtn, "Following");
    }

    /**
     * Clique sur le bouton X pour supprimer le premier profil de la liste
     */
    public void clickDelete() {
        logger.info("Suppression du profil — nombre de profils avant suppression : {}",
                getCountProfileSuggestions());
        clickBtn(deleteBtn, "X");
    }

    // =====================================================================
    // ATTENTES — synchronisation avec le DOM asynchrone
    // =====================================================================

    /**
     * Attend que le bouton passe à l'état "Following" après le clic
     * Le DOM se met à jour de manière asynchrone — waitFor() évite les assertions prématurées
     */
    public String waitFollowing() {
        wait(followingBtn, followingBtn.innerText());
        return getText(followingBtn, "est affiché avec succès");
    }

    /**
     * Attend que le bouton repasse à l'état "Follow" après le désabonnement
     */
    public String waitUnfollowing() {
        wait(followBtn, followBtn.innerText());
        return getText(followBtn, "est affiché avec succès");
    }

    // =====================================================================
    // GETTERS — récupération des textes et compteurs
    // =====================================================================

    /**
     * Retourne le texte du tooltip affiché au survol d'un bouton
     */
    public String getTooltipText() {
        return getText(tooltip, "est le texte du tooltip");
    }

    /**
     * Retourne le texte du bouton pendant l'état intermédiaire "Processing..."
     */
    public String getTextProcessing() {
        return getText(
                page.getByRole(AriaRole.BUTTON,
                        new Page.GetByRoleOptions().setName("Processing...").setExact(true)),
                "est affiché"
        );
    }

    /**
     * Retourne le nombre de profils actuellement affichés sur la page
     */
    public int getCountProfileSuggestions() {
        int count = profileCard.count();
        logger.info("Nombre de profils affichés : {}", count);
        return count;
    }
}