package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JobApplicationForm {

    // =====================================================================
    // LOGGER
    // =====================================================================

    private static final Logger logger = LoggerFactory.getLogger(JobApplicationForm.class);

    // =====================================================================
    // CHAMPS
    // =====================================================================

    // Champs publics — accessibles directement dans les tests
    public final Locator fullName;
    public final Locator email;
    public final Locator phone;
    public final Locator numberTickets;
    public final Locator selectOptions;

    // Champs privés — utilisés uniquement en interne
    private final Page page;
    private final Locator submitButton;
    private final Locator dialog;

    // =====================================================================
    // CONSTRUCTEUR
    // =====================================================================

    /**
     * Initialise la page Playwright et tous les locators du formulaire.
     * Appelé dans BasicTest (@BeforeEach) — une seule source de vérité.
     *
     * @param page Instance Playwright injectée depuis le test
     */
    public JobApplicationForm(Page page) {
        this.page          = page;
        this.fullName      = page.getByTestId("input-fullname");
        this.email         = page.getByTestId("input-email");
        this.phone         = page.getByTestId("input-phone");
        this.selectOptions = page.getByLabel("Select Event");
        this.numberTickets = page.getByTestId("input-tickets");
        this.submitButton  = page.getByLabel("Submit Registration");
        this.dialog        = page.locator("div[role='dialog']");

        logger.info("✅ JobApplicationForm initialisé avec succès");
    }

    // =====================================================================
    // NAVIGATION
    // =====================================================================

    /**
     * Navigue vers la page du formulaire.
     */
    public void navigateTo() {
        String url = "https://www.cnarios.com/concepts/form#try-it-yourself";
        page.navigate(url);
        logger.info("🌐 Navigation vers : {}", url);
    }

    // =====================================================================
    // SAISIE DES CHAMPS
    // =====================================================================

    /**
     * Méthode utilitaire — remplit un champ texte avec la valeur fournie.
     * Utilisée en interne par toutes les méthodes enter*().
     *
     * @param locator Champ cible
     * @param text    Valeur à saisir
     */
    private void sendText(Locator locator, String text) {
        locator.fill(text);
        logger.info("✏️ Texte '{}' saisi dans le champ : {}", text, locator);
    }

    /**
     * Saisit le nom complet dans le champ Full Name.
     *
     * @param name Nom complet à saisir (minimum 3 caractères)
     */
    public void enterFullName(String name) {
        sendText(fullName, name);
        logger.info("👤 Champ [Full Name] rempli avec : '{}'", name);
    }

    /**
     * Saisit l'adresse email dans le champ Email.
     *
     * @param mail Adresse email à saisir (format : xxx@xxx.xx)
     */
    public void enterEmail(String mail) {
        sendText(email, mail);
        logger.info("📧 Champ [Email] rempli avec : '{}'", mail);
    }

    /**
     * Saisit le numéro de téléphone dans le champ Phone.
     *
     * @param number Numéro de téléphone à saisir (entre 7 et 15 chiffres)
     */
    public void enterPhone(int number) {
        sendText(phone, String.valueOf(number));
        logger.info("📞 Champ [Phone] rempli avec : '{}'", number);
    }

    /**
     * Saisit le nombre de tickets souhaité dans le champ Tickets.
     *
     * @param number Nombre de tickets à saisir (entre 1 et 10)
     */
    public void enterTicket(int number) {
        sendText(numberTickets, String.valueOf(number));
        logger.info("🎟️ Champ [Tickets] rempli avec : '{}'", number);
    }

    /**
     * Sélectionne un événement dans la liste déroulante Select Event.
     * Clique d'abord sur le select pour l'ouvrir, puis clique sur l'option souhaitée.
     *
     * @param option Nom exact de l'événement à sélectionner
     */
    public void selectOption(String option) {
        selectOptions.click();
        logger.info("🖱️ Liste déroulante [Select Event] ouverte");
        page.getByText(option).click();
        logger.info("📅 Événement sélectionné : '{}'", option);
    }

    // =====================================================================
    // VALIDATIONS
    // =====================================================================

    /**
     * Méthode utilitaire — vérifie que la longueur de la valeur saisie
     * est strictement supérieure au seuil donné.
     * Utilisée en interne par lengthName() et lengthPhone().
     *
     * @param locator   Champ à vérifier
     * @param minLength Longueur minimale attendue (exclusive)
     * @return true si longueur > minLength
     */
    private boolean verifyLength(Locator locator, int minLength) {
        int actualLength = locator.inputValue().length();
        boolean isValid = actualLength > minLength;

        if (isValid) {
            logger.info("✅ Longueur valide : {} > {}", actualLength, minLength);
        } else {
            logger.warn("❌ Longueur invalide : {} ≤ {}", actualLength, minLength);
        }

        return isValid;
    }

    /**
     * Vérifie que le numéro de téléphone saisi contient plus de 7 chiffres.
     *
     * @return true si la longueur du numéro > 7
     */
    public boolean lengthPhone() {
        logger.info("🔎 Vérification longueur [Phone]");
        return verifyLength(phone, 7);
    }

    /**
     * Vérifie que le nom complet saisi contient plus de 3 caractères.
     *
     * @return true si la longueur du nom > 3
     */
    public boolean lengthName() {
        logger.info("🔎 Vérification longueur [Full Name]");
        return verifyLength(fullName, 3);
    }

    /**
     * Vérifie que le nombre de tickets saisi est compris entre 1 et 10 inclus.
     *
     * @return true si 0 < tickets ≤ 10
     */
    public boolean verifyNumberOfTickets() {
        int tickets = Integer.parseInt(numberTickets.inputValue());
        boolean isValid = tickets < 11 && tickets > 0;

        if (isValid) {
            logger.info("✅ Nombre de tickets valide : {} (entre 1 et 10)", tickets);
        } else {
            logger.warn("❌ Nombre de tickets invalide : {} (doit être entre 1 et 10)", tickets);
        }

        return isValid;
    }

    /**
     * Vérifie que l'adresse email saisie respecte le format standard.
     * Regex : caractères locaux valides + @ + domaine + extension (min 2 caractères).
     *
     * @return true si l'email est valide
     */
    public boolean verifyEmailIsValid() {
        String emailValue = email.inputValue();
        String emailRegex = "^[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}$";
        boolean isValid = emailValue.matches(emailRegex);

        if (isValid) {
            logger.info("✅ Email valide : '{}'", emailValue);
        } else {
            logger.warn("❌ Email invalide : '{}'", emailValue);
        }

        return isValid;
    }

    // =====================================================================
    // VALIDATIONS COUPLÉES
    // =====================================================================

    /** Nom invalide : longueur ≤ 3 + message d'erreur affiché */
    public boolean isInvalidName() {
        return !lengthName() && errorFullName();
    }

    /** Email invalide : format incorrect + message d'erreur affiché */
    public boolean isInvalidEmail() {
        return !verifyEmailIsValid() && errorEmail();
    }

    /** Téléphone invalide : longueur ≤ 7 + message d'erreur affiché */
    public boolean isInvalidPhone() {
        return !lengthPhone() && errorPhone();
    }

    /** Tickets invalides : hors plage [1, 10] + message d'erreur affiché */
    public boolean isInvalidTickets() {
        return !verifyNumberOfTickets() && errorTickets();
    }

    /** Événement invalide : aucune option sélectionnée + message d'erreur affiché */
    public boolean isInvalidEvent() {
        return errorEvent();
    }

    // =====================================================================
    // MESSAGES D'ERREUR
    // =====================================================================

    /**
     * Méthode utilitaire — vérifie qu'un message d'erreur attendu
     * est affiché sous un champ après validation.
     * Utilisée en interne par toutes les méthodes error*().
     *
     * @param locator Locator du message d'erreur
     * @param text    Texte attendu dans le message
     * @return true si le message affiché contient le texte attendu
     */
    private boolean verifyErrorText(Locator locator, String text) {
        String actualText = locator.innerText();
        boolean isValid = actualText.contains(text);

        if (isValid) {
            logger.info("✅ Message d'erreur correct : '{}'", actualText);
        } else {
            logger.warn("❌ Message d'erreur inattendu : '{}' (attendu : '{}')", actualText, text);
        }

        return isValid;
    }

    /**
     * Vérifie le message d'erreur du champ Full Name.
     * Le message attendu s'affiche quand le nom contient moins de 3 caractères.
     *
     * @return true si le message "Enter at least 3 characters" est affiché
     */
    public boolean errorFullName() {
        logger.info("🔎 Vérification erreur [Full Name]");
        return verifyErrorText(page.locator("#«r1»-helper-text"), "Enter at least 3 characters");
    }

    /**
     * Vérifie le message d'erreur du champ Email.
     * Le message attendu s'affiche quand le format de l'email est invalide.
     *
     * @return true si le message "Enter a valid email address" est affiché
     */
    public boolean errorEmail() {
        logger.info("🔎 Vérification erreur [Email]");
        return verifyErrorText(page.locator("#«r2»-helper-text"), "Enter a valid email address");
    }

    /**
     * Vérifie le message d'erreur du champ Phone.
     * Le message attendu s'affiche quand le numéro contient moins de 7 ou plus de 15 chiffres.
     *
     * @return true si le message "Enter a valid phone (7-15 digits)" est affiché
     */
    public boolean errorPhone() {
        logger.info("🔎 Vérification erreur [Phone]");
        return verifyErrorText(page.locator("#«r3»-helper-text"), "Enter a valid phone (7-15 digits)");
    }

    /**
     * Vérifie le message d'erreur du champ Event.
     * Le message attendu s'affiche quand aucun événement n'est sélectionné.
     *
     * @return true si le message "Please select an event" est affiché
     */
    public boolean errorEvent() {
        logger.info("🔎 Vérification erreur [Event]");
        return verifyErrorText(page.getByTestId("error-event"), "Please select an event");
    }

    /**
     * Vérifie le message d'erreur du champ Tickets.
     * Le message attendu s'affiche quand le nombre de tickets est hors de la plage [1, 10].
     *
     * @return true si le message "Enter an integer between 1 and 10" est affiché
     */
    public boolean errorTickets() {
        logger.info("🔎 Vérification erreur [Tickets]");
        return verifyErrorText(page.locator("#«r5»-helper-text"), "Enter an integer between 1 and 10");
    }

    // =====================================================================
    // ACTIONS & INTERACTIONS
    // =====================================================================

    /**
     * Clique en dehors de tout champ actif pour déclencher l'événement blur
     * et activer les validations en temps réel du formulaire.
     */
    public void clickOutside() {
        page.locator("body").click(new Locator.ClickOptions().setPosition(0, 0));
        logger.info("🖱️ Clic effectué en dehors du champ actif (blur déclenché)");
    }

    /**
     * Clique sur le bouton Submit pour soumettre le formulaire.
     * Le bouton n'est cliquable que si tous les champs sont valides.
     */
    public void submitBtn() {
        submitButton.click();
        logger.info("🚀 Bouton [Submit] cliqué");
    }

    /**
     * Clique sur le bouton Reset pour réinitialiser tous les champs du formulaire
     * à leur état initial (champs vides, tickets = 1, aucun événement sélectionné).
     */
    public void resetBtn() {
        page.getByTestId("btn-reset").click();
        logger.info("🔄 Bouton [Reset] cliqué — formulaire réinitialisé");
    }

    // =====================================================================
    // ASSERTIONS ÉTAT UI
    // =====================================================================

    /**
     * Vérifie que le bouton Submit est activé (enabled).
     * Il est activé uniquement quand tous les champs du formulaire sont valides.
     *
     * @return true si le bouton est cliquable
     */
    public boolean checkSubmitButton() {
        boolean isEnabled = submitButton.isEnabled();
        logger.info(isEnabled
                ? "✅ Bouton [Submit] activé — formulaire valide"
                : "❌ Bouton [Submit] désactivé — formulaire invalide");
        return isEnabled;
    }

    /**
     * Attend l'apparition de la modale de confirmation après soumission
     * et vérifie sa visibilité.
     *
     * @return true si la modale est visible
     */
    public boolean checkModal() {
        dialog.waitFor();
        boolean isVisible = dialog.isVisible();
        logger.info(isVisible
                ? "✅ Modale de confirmation visible — soumission réussie"
                : "❌ Modale de confirmation non visible — soumission échouée");
        return isVisible;
    }

    /**
     * Vérifie qu'un champ texte ou select est vide après réinitialisation.
     * Supprime les espaces invisibles et le Zero Width Space (U+200B)
     * injecté par les composants MUI pour l'accessibilité.
     *
     * @param locator Champ à vérifier (fullName, email, phone, selectOptions)
     * @return true si le champ ne contient aucune valeur visible
     */
    public boolean isFieldEmpty(Locator locator) {
        // Supprime les espaces normaux + Zero Width Space Unicode (U+200B / ASCII 8203)
        String value = locator.innerText().trim().replace("\u200B", "");
        boolean isEmpty = value.isEmpty();

        if (isEmpty) {
            logger.info("✅ Le champ est vide");
        } else {
            logger.warn("❌ Le champ contient : '{}'", value);
        }

        return isEmpty;
    }

    /**
     * Vérifie que le champ Tickets contient sa valeur initiale par défaut (1).
     * La valeur par défaut est définie côté application au chargement de la page.
     *
     * @return true si la valeur du champ est égale à "1"
     */
    public boolean isTicketFieldDefault() {
        String value = numberTickets.inputValue();
        boolean isDefault = value.equals("1");

        if (isDefault) {
            logger.info("✅ Champ [Tickets] à sa valeur initiale : '{}'", value);
        } else {
            logger.warn("❌ Champ [Tickets] modifié — valeur actuelle : '{}'", value);
        }

        return isDefault;
    }
}