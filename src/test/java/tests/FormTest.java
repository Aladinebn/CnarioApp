package tests;

import base.BasicTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pages.JobApplicationForm;

import java.util.Random;

public class FormTest extends BasicTest {

    private static final Logger logger = LoggerFactory.getLogger(FormTest.class);

    // =====================================================================
    // DONNÉES DE TEST
    // =====================================================================

    private static final String VALID_NAME = "tester";
    private static final String VALID_EMAIL = "tester@gmail.com";
    private static final int VALID_PHONE = 5306888;
    private static final String VALID_EVENT = "Frontend Conf 2025";
    // Générer un nombre aléatoire dans une plage spécifique
    static int max=10;
    static int min=1;
    static Random rand = new Random();
    private static final int VALID_TICKETS = rand.nextInt(max - min + 1) + min;

    // =====================================================================
    // INITIALISATION
    // =====================================================================

    private JobApplicationForm form;

    @BeforeEach
    public void setup() {
        form = new JobApplicationForm(page);
        form.navigateTo();
    }

    // =====================================================================
    // MÉTHODE UTILITAIRE
    // =====================================================================

    /**
     * Remplit tous les champs du formulaire avec des données valides.
     * Utilisée comme base dans les tests de champs invalides —
     * seul le champ testé est ensuite écrasé avec une valeur invalide.
     */
    private void fillValidForm() {
        form.enterFullName(VALID_NAME);
        form.enterEmail(VALID_EMAIL);
        form.enterPhone(VALID_PHONE);
        form.selectOption(VALID_EVENT);
        form.enterTicket(VALID_TICKETS);
    }

    // =====================================================================
    // CAS Valides
    // =====================================================================

    /**
     * Vérifie qu'un formulaire correctement rempli peut être soumis
     * et qu'une modale de confirmation s'affiche après soumission.
     */
    @Test
    public void submitWithValidData() {
        logger.info("🧪 Test : soumission avec des données valides");
        fillValidForm();

        Assertions.assertTrue(form.checkSubmitButton());
        form.submitBtn();
        Assertions.assertTrue(form.checkModal());
    }

    // =====================================================================
    // CAS Invalides — CHAMPS Manquants OU Incorrects
    // =====================================================================

    /**
     * Vérifie que le bouton Submit est désactivé quand des champs sont invalides.
     * Cas : nom vide (espace), téléphone trop court.
     */
    @Test
    public void submitWithMissingData() {
        logger.info("🧪 Test : soumission avec des données manquantes");
        form.enterFullName(" ");
        form.enterEmail(VALID_EMAIL);
        form.enterPhone(53);
        form.selectOption(VALID_EVENT);
        form.enterTicket(VALID_TICKETS);

        Assertions.assertFalse(form.checkSubmitButton());
    }

    /**
     * Vérifie qu'un nom trop court (≤ TROIS caractères) affiche une erreur
     * et désactive le bouton Submit.
     */
    @Test
    public void enterInvalidName() {
        logger.info("🧪 Test : nom invalide (moins de 3 caractères)");
        fillValidForm();
        form.enterFullName("QA");
        form.clickOutside();

        Assertions.assertTrue(form.isInvalidName() && !form.checkSubmitButton());
    }

    /**
     * Vérifie qu'un email au format incorrect affiche une erreur
     * et désactive le bouton Submit.
     */
    @Test
    public void enterInvalidMail() {
        logger.info("🧪 Test : email invalide (format incorrect)");
        fillValidForm();
        form.enterEmail("@mail.com.hello");
        form.clickOutside();

        Assertions.assertTrue(form.isInvalidEmail() && !form.checkSubmitButton());
    }

    /**
     * Vérifie qu'un numéro de téléphone trop court (≤ SEPT chiffres) affiche une erreur
     * et désactive le bouton Submit.
     */
    @Test
    public void enterInvalidPhone() {
        logger.info("🧪 Test : téléphone invalide (moins de 7 chiffres)");
        fillValidForm();
        form.enterPhone(44);
        form.clickOutside();

        Assertions.assertTrue(form.isInvalidPhone()&& !form.checkSubmitButton());
    }

    // =====================================================================
    // CAS Invalides — TICKETS
    // =====================================================================

    /**
     * Vérifie qu'un nombre de tickets égal à ZÉRO affiche une erreur
     * et désactive le bouton Submit.
     */
    @Test
    public void enterZeroTicket() {
        logger.info("🧪 Test : tickets invalides (valeur = 0)");
        fillValidForm();
        form.enterTicket(0);
        form.clickOutside();

        Assertions.assertTrue(form.isInvalidTickets()&& !form.checkSubmitButton());
    }

    /**
     * Vérifie qu'un nombre de tickets supérieur à 10 affiche une erreur
     * et désactive le bouton Submit.
     */
    @Test
    public void enterMoreThanTenTickets() {
        logger.info("🧪 Test : tickets invalides (valeur = 11)");
        fillValidForm();
        form.enterTicket(11);
        form.clickOutside();

        Assertions.assertTrue(form.isInvalidTickets()&& !form.checkSubmitButton());
    }

    // =====================================================================
    // CAS Invalides — ÉVÉNEMENT NON SÉLECTIONNÉ
    // =====================================================================

    /**
     * Vérifie que la sélection de l'option par défaut "— Select —" affiche une erreur
     * et désactive le bouton Submit.
     */
    @Test
    public void eventUnselected() {
        logger.info("🧪 Test : événement non sélectionné (option par défaut)");
        fillValidForm();
        form.selectOption("-- Select --");
        form.clickOutside();

        Assertions.assertTrue(form.isInvalidEvent() && !form.checkSubmitButton());
    }

    // =====================================================================
    // BOUTON RESET
    // =====================================================================

    /**
     * Vérifie qu'après un clic sur Reset tous les champs reviennent
     * à leur état initial et que le bouton Submit est désactivé.
     */
    @Test
    public void resetButton() {
        logger.info("🧪 Test : réinitialisation du formulaire via bouton Reset");
        fillValidForm();
        form.resetBtn();

        Assertions.assertTrue(
                form.isFieldEmpty(form.fullName)                 // fullName vide
                        && form.isFieldEmpty(form.email)         // email vide
                        && form.isFieldEmpty(form.phone)         // phone vide
                        && form.isFieldEmpty(form.selectOptions) // select sans option
                        && form.isTicketFieldDefault()           // tickets = 1 (valeur par défaut)
                        && !form.checkSubmitButton()             // bouton Submit désactivé
        );
    }
}