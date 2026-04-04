# CnarioAppTesting
# 🎭 Playwright Java — Framework d'automatisation des tests UI

Un framework d'automatisation des tests UI développé avec **Playwright Java**, **JUnit 5**, et **SLF4J + Logback**, suivant le patron de conception **Page Object Model (POM)**.

---

## 📁 Structure du projet

```
src/
├── main/java/
│   └── pages/
│       └── ProfileSuggestionPage.java   # Page Object — interactions UI
├── test/java/
│   ├── base/
│   │   └── BasicTest.java               # Classe de base — cycle de vie du navigateur (@BeforeAll, @BeforeEach)
│   └── tests/
│       ├── FollowTest.java              # Tests — Follow / Processing / Unfollow
│       ├── DeleteTest.java              # Test  — suppression de N profils
│       └── TooltipTest.java            # Tests — tooltip au survol Follow / Following
└── test/resources/
    └── logback.xml                      # Configuration des logs
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
git clone https://github.com/your-username/your-repo.git
cd your-repo

# Installer les dépendances
mvn install

# Installer les navigateurs Playwright
mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install"
```

### Lancer tous les tests

```bash
mvn test
```

### Lancer un test spécifique

```bash
mvn test -Dtest=FollowTest
mvn test -Dtest=DeleteTest
mvn test -Dtest=TooltipVisibilityOnHoverTest
```

---

## 🧪 Scénarios de test

### ✅ `FollowTest.java` — 3 tests

| Méthode | Description |
|---|---|
| `followWithSuccess()` | Clique sur **Follow** et vérifie que le bouton devient **"Following"** |
| `checkProcessingText()` | Vérifie que l'état intermédiaire **"Processing..."** est affiché après le clic |
| `unfollowWithSuccess()` | Suit puis se désabonne et vérifie que le bouton revient à **"Follow"** |

### ✅ `DeleteTest.java` — 1 test

| Méthode | Description |
|---|---|
| `deleteProfile()` | Compte les profils, supprime N profils via le bouton **X**, vérifie que le compte a diminué de N |

### ✅ `TooltipTest.java` — 2 tests

| Méthode | Description |
|---|---|
| `FollowHoverTest()` | Survole le bouton **Follow** et vérifie que le tooltip affiche **"Follow"** |
| `UnfollowHoverTest()` | Suit un profil, survole le bouton **Following** et vérifie que le tooltip affiche **"Unfollow"** |

---

## 🏗️ Patrons de conception

### Page Object Model (POM)
Chaque page est représentée par une classe dédiée qui encapsule tous les locators et les interactions.

```java
// ✅ Locators initialisés une seule fois dans le constructeur
public ProfileSuggestionPage(Page page) {
    this.followBtn = page.locator("span[aria-label='Follow'] button").nth(1);
    this.followingBtn = page.getByRole(AriaRole.BUTTON,
            new Page.GetByRoleOptions().setName("Following"));
}
```

### Méthodes privées génériques
La logique commune est extraite dans des méthodes privées pour éviter la duplication.

```java
// ✅ Une seule méthode privée gère toute la récupération de texte
private String getText(Locator locator, String logLabel) {
    String text = locator.innerText();
    logger.info("'{}' : {}", text, logLabel);
    return text;
}
```

---

## 📋 Stratégie de sélecteurs

| Priorité | Sélecteur | Exemple | Raison |
|---|---|---|---|
| ✅ 1 | `aria-label` | `span[aria-label='Follow'] button` | Sémantique et stable |
| ✅ 2 | `getByRole` | `getByRole(BUTTON, name="Following")` | Recommandé par Playwright |
| ✅ 3 | Attribut `role` | `[role='tooltip']` | HTML sémantique |
| ⚠️ 4 | Classes CSS | `.MuiButton-root` | À éviter — classes MUI instables |
| ❌ 5 | XPath absolu | `//div[2]/button[1]` | À éviter — casse au moindre changement DOM |

---

## 📊 Logging

Les logs sont gérés via **SLF4J + Logback** et affichés dans la console avec horodatage.

```
07:24:48 [main] INFO  pages.ProfileSuggestionPage - ProfileSuggestionPage initialisé
07:24:49 [main] INFO  pages.ProfileSuggestionPage - Navigation vers : https://...
07:24:51 [main] INFO  pages.ProfileSuggestionPage - Bouton 'Follow' cliqué avec succès
07:24:54 [main] INFO  pages.ProfileSuggestionPage - État changé vers : Following
07:24:54 [main] INFO  pages.ProfileSuggestionPage - Texte du bouton après clic : Following
07:24:54 [main] INFO  tests.FollowTest - Fin du test followWithSuccess — PASSED
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

<!-- SLF4J -->
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-api</artifactId>
    <version>2.0.16</version>
</dependency>

<!-- Logback -->
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

**URL :** [https://www.cnarios.com/concepts/button#try-it-yourself](https://www.cnarios.com/concepts/button#try-it-yourself)

Cnarios est une plateforme gratuite pour pratiquer l'automatisation des tests UI sur des scénarios réels.

---

## 📄 Licence

Ce projet est sous licence MIT.
