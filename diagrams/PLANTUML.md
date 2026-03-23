# 📊 PlantUML Diagrams (Alternative to Mermaid)

Cette page documente les diagrammes PlantUML disponibles comme alternative aux diagrammes Mermaid.

PlantUML offre des avantages supplémentaires:
- **Component diagrams** avec relations détaillées
- **Deployment diagrams** pour infrastructure
- **Sequence diagrams** avancés
- **Class diagrams** pour structure OOP
- **Meilleure contrôle** du layout et du style

## 📂 Fichiers PlantUML disponibles

```
docs/diagrams/
├── architecture.puml          # Architecture système complète
├── deployment.puml            # Infrastructure locale vs production
└── README.md                  # Documentation (ce fichier)
```

## 💻 Utiliser PlantUML localement

### Installation

```bash
# 1. Installer PlantUML (macOS)
brew install plantuml

# Ou depuis JAR
java -jar plantuml.jar -version

# Ou Docker
docker run --rm -it -v $(pwd):/data plantuml/plantuml \
  /data/docs/diagrams/architecture.puml -o /data/docs/diagrams/generated/
```

### Générer les images

```bash
# Une seule image
plantuml docs/diagrams/architecture.puml -o docs/diagrams/generated/

# Tous les fichiers .puml
plantuml docs/diagrams/*.puml -o docs/diagrams/generated/

# Formats disponibles
plantuml docs/diagrams/architecture.puml -tpng -o docs/diagrams/generated/
plantuml docs/diagrams/architecture.puml -tsvg -o docs/diagrams/generated/
plantuml docs/diagrams/architecture.puml -tpdf -o docs/diagrams/generated/
```

### Éditeur en ligne

Utiliser l'éditeur PlantUML en ligne:
- [PlantUML Online Editor](http://www.plantuml.com/plantuml/uml/)
- Copier-coller le contenu `.puml` et visualiser en temps réel

## 🚀 Intégration CI/CD (Optional)

Pour ajouter la génération PlantUML automatique au pipeline CI/CD, ajouter ce step:

```yaml
- name: Generate PlantUML diagrams
  run: |
    docker run --rm -v $(pwd):/data plantuml/plantuml \
      /data/docs/diagrams/*.puml -o /data/docs/diagrams/generated/
```

## 📚 Références PlantUML

- [Documentation PlantUML](https://plantuml.com/guide)
- [Component Diagram](https://plantuml.com/component-diagram)
- [Deployment Diagram](https://plantuml.com/deployment-diagram)
- [Sequence Diagram](https://plantuml.com/sequence-diagram)
- [Class Diagram](https://plantuml.com/class-diagram)

## 🔄 Mermaid vs PlantUML

| Critère | Mermaid | PlantUML |
|---------|---------|----------|
| **Intégration GitHub** | ✅ Native | ⚠️ Nécessite action |
| **Syntaxe simple** | ✅ Plus simple | ⚠️ Plus complexe |
| **Déploiement** | npm package | Docker/JAR |
| **Diagrams avancés** | ✅ Good | ✅ Excellent |
| **Component diagrams** | ⚠️ Limited | ✅ Full |
| **Customization** | ✅ Good | ✅ Excellent |

## 📝 Notes

- **Mermaid** est généré automatiquement dans le pipeline CI/CD + intégré dans GitHub
- **PlantUML** est disponible comme alternative pour diagrammes plus avancés
- Les deux formats peuvent coexister dans le même projet
- Pour la documentation GitHub Pages, Mermaid est recommandé (natif)
- Pour les rapports PDF/publications, PlantUML offre meilleur contrôle
