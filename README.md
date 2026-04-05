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

### Lancer une suite de tests spécifique

```bash
mvn test -Dtest=NomDeLaClasseDeTest
```

---

## 🏗️ Patrons de conception

### Page Object Model (POM)
Chaque page testée est représentée par une classe dédiée qui encapsule tous les locators et les interactions. Les tests ne contiennent aucune logique de sélection — seulement des appels aux méthodes des Page Objects.

```
Test (QUOI tester)  →  Page Object (COMMENT interagir)  →  Navigateur
```

### Méthodes privées génériques
La logique commune (clic, survol, récupération de texte, attente) est extraite dans des méthodes privées pour éviter la duplication et centraliser la maintenance.

```java
// ✅ Une seule méthode privée gère toute la récupération de texte
private String getText(Locator locator, String logLabel) {
    String text = locator.innerText();
    logger.info("'{}' : {}", text, logLabel);
    return text;
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
07:24:48 [main] INFO  base.BasicTest   - Navigateur lancé
07:24:49 [main] INFO  pages.XxxPage    - Navigation vers : https://...
07:24:51 [main] INFO  pages.XxxPage    - Bouton '...' cliqué avec succès
07:24:54 [main] INFO  pages.XxxPage    - État changé vers : ...
07:24:54 [main] INFO  tests.XxxTest    - Fin du test — PASSED
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

**URL :** [https://www.cnarios.com](https://www.cnarios.com)

Cnarios est une plateforme gratuite pour pratiquer l'automatisation des tests UI sur des scénarios réels et variés.

---

## 📄 Licence

Ce projet est sous licence MIT.