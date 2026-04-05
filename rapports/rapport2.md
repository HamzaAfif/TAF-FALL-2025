Cours MGL7760 – Qualité et productivité des outils logiciels

**PROJET DE SESSION**  
**Modernisation de l'outillage du projet open source TAF**

Étudiants:

Hamza Afif  
Ekpe Koffi  
Seyf Eddine Necib

Équipe n°4

**_Professeur: Gnagnely Serge Dogny_**

_Version 2.1_

# Rapport TP2 – Implémentation et évaluation critique de la chaîne d'outils

## 1. Résumé exécutif

Ce rapport présente l'implémentation effective de la chaîne d'outils du **TP2** pour le projet **TAF-FALL-2025**, ainsi qu'une évaluation critique de sa conformité aux exigences académiques et industrielles en ingénierie logicielle.  
L'analyse montre une chaîne CI/CD **fonctionnelle, multi-langages et fortement automatisée**, couvrant les dimensions attendues: compilation, qualité de code, tests, couverture, génération de documentation et publication continue.

Les objectifs obligatoires du TP2 sont globalement atteints. Deux écarts structurants subsistent:

1. absence de mécanisme `pre-commit` versionné (validation locale pré-push);
2. besoin d'alignement explicite entre la stratégie de tests d'intégration et la configuration Failsafe Maven.

Ces écarts n'invalident pas l'architecture de la solution, mais constituent des risques de gouvernance qualité à corriger avant la remise finale.

---

## 2. Contexte et problématique

Le TP2 exige l'implémentation opérationnelle d'une chaîne outillée complète, intégrée à un projet réel, avec contraintes de performance et de qualité mesurables. Le projet TAF impose une complexité non triviale:

- backend Java/Spring Boot;
- frontend Angular/TypeScript;
- modules de performance Maven (Gatling/JMeter);
- service Python (FastAPI) pour la génération de tests.

Dans ce contexte polyglotte, la problématique centrale n'est pas seulement d'installer des outils, mais de **garantir leur cohérence systémique**: reproductibilité, traçabilité des preuves, orchestration CI fiable et qualité observable.

---

## 3. Méthodologie d'évaluation

L'évaluation repose sur une approche d'audit technique reproductible:

1. inspection de la configuration CI principale (`.github/workflows/ci-cd.yml`);
2. inspection des configurations qualité (`sonar-project.properties`, `backend/pom.xml`, `performance/pom.xml`, configuration lint/format frontend et python);
3. inventaire des tests automatisés (compte des classes/fichiers/scénarios);
4. vérification des mécanismes de génération documentaire et publication;
5. construction d'une matrice de conformité alignée sur les critères TP2.

Cette méthode privilégie les preuves observables dans le dépôt plutôt que les déclarations d'intention.

---

## 4. Résultats détaillés

### 4.1 Pipeline CI/CD

Le workflow principal (`.github/workflows/ci-cd.yml`) implémente une chaîne complète avec déclencheurs `push`, `pull_request` et `workflow_dispatch`, et inclut:

- jobs backend, frontend, performance et python;
- étapes build, lint/format, tests et couverture;
- agrégation de rapports et artefacts;
- génération de diagrammes;
- génération/publication de documentation GitHub Pages;
- construction (et publication conditionnelle) d'images Docker.

Éléments de maturité notables:

- cache Maven/Node/pip;
- politique `concurrency` (annulation des exécutions obsolètes);
- uploads d'artefacts (traceabilité des livrables CI);
- synthèse automatique des résultats de pipeline.

Analyse critique:

- l'architecture CI dépasse le minimum TP2 en incluant un volet de conteneurisation/publication (utile pour TP3);
- la séquence est robuste, mais la validation finale doit confirmer la contrainte de durée (< 15 min) sur un run représentatif documenté.

---

### 4.2 Qualité logicielle et analyse statique

Les outils de qualité sont effectivement configurés et intégrés:

- Java: Checkstyle + PMD (backend/performance);
- Frontend: ESLint + Prettier;
- Python: Black (exécuté en CI), avec flake8/mypy présents dans les dépendances de dev;
- SonarCloud: configuration multi-modules et multi-langages via `sonar-project.properties`.

Le contrôle de couverture minimal (60%) est implémenté dans le workflow Sonar via appel API, ce qui établit un garde-fou objectif.

Analyse critique:

- `sonar.qualitygate.wait=false` favorise la disponibilité de l'analyse même en cas d'échec de gate, mais déplace la responsabilité de blocage vers la logique CI personnalisée;
- l'absence de hooks `pre-commit` réduit la prévention précoce des défauts (shift-left incomplet).

---

### 4.3 Tests automatisés et couverture

L'inventaire statique confirme un volume de tests supérieur au minimum exigé:

- backend: **56** méthodes annotées `@Test`;
- frontend: **37** scénarios Jasmine (`it(...)`);
- python: **6** tests pytest;
- performance: **1** test JUnit.

Total observé: **100+ scénarios/tests**.

Exigence d'intégration (minimum 5 scénarios): satisfaite via `IntegrationTest.java` avec **18 scénarios** identifiés.

Sorties de couverture et de rapports:

- JaCoCo (backend/performance);
- LCOV + HTML (frontend);
- `coverage.xml` + `htmlcov` (python);
- artefacts CI archivés.

Analyse critique:

- la présence de `failsafe:integration-test` est un signal positif;
- pour une robustesse de niveau maîtrise, la convention de nommage des tests d'intégration (`*IT`) ou la configuration explicite des includes Failsafe doit être verrouillée afin d'éviter toute ambiguïté d'exécution.

---

### 4.4 Documentation automatisée

La chaîne documentaire est bien industrialisée:

- Compodoc (frontend);
- JavaDoc backend (avec fallback HTML en cas de génération partielle);
- diagrammes Mermaid générés automatiquement;
- déploiement GitHub Pages sur branches ciblées.

Le `README.md` contient les éléments attendus: badges, prérequis, guide d'exécution locale, architecture synthétique, liens de qualité et de documentation.

Analyse critique:

- la documentation n'est pas seulement descriptive; elle est outillée et versionnée;
- l'approche fallback sur JavaDoc améliore la résilience CI, mais doit rester accompagnée d'un suivi de dette documentaire pour restaurer une JavaDoc native complète lorsque possible.

---

## 5. Matrice de conformité TP2

| Critère TP2                                                     | Niveau de conformité   | Preuve principale                        | Commentaire d'évaluation                                                  |
| --------------------------------------------------------------- | ---------------------- | ---------------------------------------- | ------------------------------------------------------------------------- |
| Pipeline CI complet (build, lint, test, qualité, documentation) | Conforme               | `.github/workflows/ci-cd.yml`            | Chaîne multi-langages complète                                            |
| Triggers push/PR + optimisation                                 | Conforme               | workflow CI                              | Triggers, cache et `concurrency` présents                                 |
| Analyse statique + gate qualité                                 | Conforme avec réserve  | `sonar-project.properties` + job Sonar   | Gate de couverture CI explicite; stratégie Sonar non bloquante par défaut |
| Tests unitaires (>=20)                                          | Conforme               | inventaire des tests                     | Seuil largement dépassé                                                   |
| Tests d'intégration (>=5)                                       | Conforme               | `IntegrationTest.java`                   | 18 scénarios observés                                                     |
| Rapports couverture/qualité                                     | Conforme               | artefacts CI + config couverture         | JaCoCo/LCOV/pytest-cov disponibles                                        |
| Documentation auto + publication en ligne                       | Conforme               | jobs `diagrams` + `documentation`        | Compodoc + JavaDoc + Mermaid + Pages                                      |
| Hooks pre-commit                                                | Partiellement conforme | absence de `.husky`/`.pre-commit-config` | Point à corriger                                                          |

---

## 6. Contraintes techniques TP2

| Contrainte                        | État                       | Position analytique                                                 |
| --------------------------------- | -------------------------- | ------------------------------------------------------------------- |
| Pipeline < 15 minutes             | À confirmer empiriquement  | Mesure à documenter sur un run final de référence                   |
| Couverture minimale 60%           | Implémentée en contrôle CI | Seuil explicite dans l'étape Sonar                                  |
| Quality Gate défini et respecté   | Partiellement verrouillé   | Contrôle couverture présent; politique de blocage Sonar perfectible |
| Tous les tests doivent passer     | À valider au run de remise | Dépend du run final horodaté                                        |
| Documentation accessible en ligne | Conforme (si Pages activé) | Pipeline de publication présent                                     |

---

## 7. Discussion critique

Trois points de maturité ressortent:

1. **Cohérence inter-technologies**: la chaîne intègre Java, TypeScript et Python sans cloisonnement excessif.
2. **Observabilité des preuves**: les artefacts CI et les sorties de couverture facilitent l'auditabilité.
3. **Capacité d'industrialisation**: l'extension Docker/documentation démontre une trajectoire naturelle vers TP3.

Deux fragilités restantes doivent être traitées pour une conformité défendable en soutenance:

1. **Shift-left incomplet**: sans `pre-commit`, le contrôle qualité repose trop sur la CI distante.
2. **Contrat d'exécution des tests d'intégration**: nécessité de normaliser Failsafe pour éviter des faux positifs de conformité.

En synthèse, le système est techniquement solide, mais la gouvernance qualité locale (avant push) doit être consolidée.

---

## 8. Plan de finalisation avant remise

1. Ajouter un mécanisme `pre-commit` versionné, couvrant les commandes minimales suivantes: frontend (`npm run lint`, `npm run format:check`), backend (`mvn -f backend/pom.xml -q checkstyle:check pmd:check test`), python (`black --check app`, `pytest -q`).

2. Verrouiller la stratégie Failsafe, soit par convention de nommage `*IT.java`, soit par configuration explicite des patterns d'inclusion.

3. Produire un run final de référence (branches `develop` puis `main`) et archiver les preuves associées: durée totale du pipeline, statut détaillé des jobs, couverture consolidée, dashboard Sonar et URL GitHub Pages.

4. Mettre à jour la version finale du rapport avec les métriques empiriques du run de remise.

---

## 9. Conclusion

L'implémentation TP2 de TAF atteint un niveau de qualité globalement élevé et cohérent: pipeline CI/CD complet, outillage qualité intégré, base de tests substantielle, documentation générée automatiquement et publication continue.  
Les non-conformités restantes sont ciblées, limitées et rapidement corrigeables. Une fois les deux verrous identifiés levés (`pre-commit`, contrat Failsafe), la chaîne pourra être défendue comme une solution robuste, reproductible et professionnellement crédible.

---

## Annexe A – Références techniques auditées

- `.github/workflows/ci-cd.yml`
- `.github/workflows/build-test.yml`
- `sonar-project.properties`
- `backend/pom.xml`
- `performance/pom.xml`
- `frontend/package.json`
- `frontend/karma.conf.js`
- `.eslintrc.json`
- `.prettierrc`
- `test-generation-service/requirements-dev.txt`
- `test-generation-service/pytest.ini`
- `test-generation-service/tests/test_main.py`
- `README.md`
- `CI_CD_FIX_GUIDE.md`
- `docs/diagrams/README.md`
