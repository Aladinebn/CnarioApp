# 🎭 Playwright Java — Framework d'automatisation des tests UI

Un framework d'automatisation des tests UI développé avec **Playwright Java**, **JUnit 5**, et **SLF4J + Logback**, suivant le patron de conception **Page Object Model (POM)**.

Le projet couvre plusieurs fonctionnalités du site [Cnarios](https://www.cnarios.com) — une plateforme gratuite dédiée à la pratique de l'automatisation des tests UI sur des scénarios réels.

---

## 📁 Structure du projet

```
src/
├── main/java/
│   └── pages/                  # Page Objects — un fichier par page testée
├── test/java/
│   ├── base/
│   │   └── BasicTest.java      # Classe de base — cycle de vie du navigateur
│   └── tests/                  # Suites de tests — un fichier par fonctionnalité
└── test/resources/
    └── logback.xml             # Configuration des logs
```

---

## 🛠️ Stack Technique

| Outil | Version | Rôle |
|---|---|---|
| Java | 25 | Langage de programmation |
| Playwright | 1.58.0 | Automatisation du navigateur |
| JUnit 5 | 5.11.3 | Framework de test (annotations, assertions) |
| SLF4J | 2.0.16 | Façade de logging |
| Logback | 1.5.32 | Implémentation du logging |
| Maven | latest | Gestion des dépendances |

---

## 🚀 Démarrage rapide

### Prérequis

- Java JDK 17+
- Maven 3.8+

### Installation

```bash
# Cloner le dépôt
git clone https://github.com/Aladinebn/CnarioApp.git
cd CnarioApp

# Installer les dépendances
mvn install

# Installer les navigateurs Playwright
mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install"
```

### Lancer tous les tests

```bash
mvn test
```

### Lancer une suite de tests spécifique

```bash
mvn test -Dtest=NomDeLaClasseDeTest
```

---

## 🏆 Défis réalisés

### Défi 1 — Follow / Unfollow

**Page testée :** [cnarios.com/concepts/follow](https://www.cnarios.com/concepts/follow)

Automatisation des interactions de suivi entre utilisateurs — vérification des états, des compteurs et des suggestions de profils.

**Scénarios couverts :**

| Test | Description |
|---|---|
| `followUser` | Suivre un utilisateur et vérifier le changement d'état du bouton |
| `unfollowUser` | Se désabonner et vérifier le retour à l'état initial |
| `verifyFollowerCount` | Vérifier que le compteur se met à jour après follow/unfollow |
| `verifySuggestedProfiles` | Vérifier l'affichage des suggestions de profils |

---

### Défi 2 — Formulaire d'inscription

**Page testée :** [cnarios.com/concepts/form](https://www.cnarios.com/concepts/form#try-it-yourself)

Automatisation complète d'un formulaire d'inscription avec validation des champs, messages d'erreur, soumission et réinitialisation.

**Scénarios couverts :**

| Test | Description |
|---|---|
| `submitWithValidData` | Soumettre le formulaire avec des données valides → modale de confirmation |
| `submitWithMissingData` | Bouton Submit désactivé si des champs sont invalides |
| `enterInvalidName` | Nom trop court → message d'erreur + Submit désactivé |
| `enterInvalidMail` | Email mal formaté → message d'erreur + Submit désactivé |
| `enterInvalidPhone` | Téléphone trop court → message d'erreur + Submit désactivé |
| `enterZeroTicket` | Tickets = 0 → message d'erreur + Submit désactivé |
| `enterMoreThanTenTickets` | Tickets > 10 → message d'erreur + Submit désactivé |
| `eventUnselected` | Aucun événement sélectionné → message d'erreur + Submit désactivé |
| `resetButton` | Reset → tous les champs reviennent à leur état initial |

---

## 🧠 Problèmes rencontrés & solutions

### 1. `innerText()` vs `inputValue()`

**Problème :** `innerText()` retournait toujours une chaîne vide sur les champs `<input>` — ce qui rendait toutes les validations de longueur inutilisables.

**Cause :** `innerText()` lit le contenu HTML visible d'un élément (comme un `<div>`), tandis que `inputValue()` lit la valeur saisie dans un champ de formulaire.

**Solution :**
```java
// ❌ Mauvais — retourne '' sur un <input>
String value = locator.innerText();

// ✅ Correct — retourne la valeur saisie
String value = locator.inputValue();
```

---

### 2. `checkValidity()` HTML5 inefficace pour la validation email

**Problème :** La validation email via `checkValidity()` passait même avec des emails invalides.

**Cause :** `checkValidity()` se base sur l'attribut `type="email"` du champ HTML. Si le champ n'a pas ce type, aucune validation n'est appliquée.

**Solution :** Remplacement par une validation Regex Java indépendante du DOM :
```java
String emailRegex = "^[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}$";
boolean isValid = emailValue.matches(emailRegex);
```

---

### 3. Zero Width Space (U+200B) dans un composant MUI

**Problème :** Le champ Select semblait vide visuellement, mais `isEmpty()` retournait `false`.

**Diagnostic :** Affichage des codes ASCII de chaque caractère — le code `8203` a été détecté, correspondant au Zero Width Space Unicode (`\u200B`), injecté par MUI pour des raisons d'accessibilité.

**Solution :**
```java
String value = locator.innerText()
                       .trim()
                       .replace("\u200B", ""); // Supprime le Zero Width Space
```

---


## 📚 Nouvelles compétences acquises

- **Page Object Model (POM)** — séparation claire entre la logique de navigation et les tests
- **Couplage des validations** — méthodes `isInvalidName()`, `isInvalidEmail()` qui combinent validation + message d'erreur en une seule assertion
- **Constantes de test** — extraction des données (`VALID_NAME`, `VALID_EMAIL`...) pour éviter les répétitions
- **`@BeforeEach` partagé** — initialisation du formulaire et navigation centralisées
- **`fillValidForm()`** — méthode utilitaire qui remplit tous les champs valides, chaque test ne modifiant que le champ testé
- **Débogage DOM avec `evaluate()`** — inspection des vraies valeurs des éléments via JavaScript natif
- **Gestion des caractères invisibles** — détection et suppression des caractères Unicode cachés

---

## 🏗️ Patrons de conception

### Page Object Model (POM)

Chaque page testée est représentée par une classe dédiée qui encapsule tous les locators et les interactions. Les tests ne contiennent aucune logique de sélection — seulement des appels aux méthodes des Page Objects.

```
Test (QUOI tester)  →  Page Object (COMMENT interagir)  →  Navigateur
```

### Méthodes privées génériques

La logique commune (saisie, vérification de longueur, vérification des messages d'erreur) est extraite dans des méthodes privées pour éviter la duplication :

```java
// Une seule méthode privée gère toute la saisie de texte
private void sendText(Locator locator, String text) {
    locator.fill(text);
    logger.info("✏️ Texte '{}' saisi dans le champ : {}", text, locator);
}

// Une seule méthode privée vérifie tous les messages d'erreur
private boolean verifyErrorText(Locator locator, String text) {
    return locator.innerText().contains(text);
}
```

### Classe de base `BasicTest`

Toutes les classes de test héritent de `BasicTest` qui gère le cycle de vie du navigateur :

```
@BeforeAll  → Lancement du navigateur (une seule fois)
@BeforeEach → Création d'un nouveau contexte et d'une nouvelle page
@AfterEach  → Fermeture du contexte
@AfterAll   → Fermeture du navigateur
```

---

## 📋 Stratégie de sélecteurs

| Priorité | Sélecteur | Exemple | Raison |
|---|---|---|---|
| ✅ 1 | `data-testid` | `[data-testid='btn']` | Fait pour les tests, jamais modifié |
| ✅ 2 | `getByRole` | `getByRole(BUTTON, name="Submit")` | Recommandé par Playwright |
| ✅ 3 | `aria-label` | `[aria-label='Follow']` | Sémantique et stable |
| ✅ 4 | Attribut `role` | `[role='tooltip']` | HTML sémantique |
| ⚠️ 5 | Classes CSS | `.MuiButton-root` | À éviter — instables lors des refactors |
| ❌ 6 | XPath absolu | `//div[2]/button[1]` | À éviter — casse au moindre changement DOM |

---

## 📊 Logging

Les logs sont gérés via **SLF4J + Logback** et affichés dans la console avec horodatage.

```
07:24:48 [main] INFO  base.BasicTest          - Navigateur lancé
07:24:49 [main] INFO  pages.JobApplicationForm - 🌐 Navigation vers : https://...
07:24:51 [main] INFO  pages.JobApplicationForm - ✅ Email valide : 'tester@gmail.com'
07:24:53 [main] INFO  pages.JobApplicationForm - ✅ Bouton [Submit] activé — formulaire valide
07:24:54 [main] INFO  pages.JobApplicationForm - ✅ Modale de confirmation visible — soumission réussie
```

---

## ⚙️ Configuration

### `pom.xml` — Dépendances principales

```xml
<!-- Playwright -->
<dependency>
    <groupId>com.microsoft.playwright</groupId>
    <artifactId>playwright</artifactId>
    <version>1.58.0</version>
</dependency>

<!-- JUnit 5 -->
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter</artifactId>
    <version>5.11.3</version>
</dependency>

<!-- SLF4J + Logback -->
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-api</artifactId>
    <version>2.0.16</version>
</dependency>
<dependency>
    <groupId>ch.qos.logback</groupId>
    <artifactId>logback-classic</artifactId>
    <version>1.5.32</version>
</dependency>
```

### `logback.xml` — Format des logs

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <appender name="Console" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%d{HH:mm:ss} [%t] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>
    <root level="INFO">
        <appender-ref ref="Console"/>
    </root>
</configuration>
```

---

## 🌐 Application testée

**URL :** https://www.cnarios.com

Cnarios est une plateforme gratuite pour pratiquer l'automatisation des tests UI sur des scénarios réels et variés.

---

## 📄 Licence

Ce projet est sous licence MIT.
