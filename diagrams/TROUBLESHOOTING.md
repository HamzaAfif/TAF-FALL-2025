# 🔧 Troubleshooting - Diagram Generation

## Problème: "No usable sandbox! Failed to launch the browser process"

### Cause
L'erreur survient quand `mermaid-cli` utilise Puppeteer pour générer les diagrammes SVG, mais le sandbox Chromium n'est pas disponible dans l'environnement (notamment dans GitHub Actions ou conteneurs Linux).

### ✅ Solution

#### 1. **GitHub Actions (CI/CD)** - ✅ Fixed
Le workflow CI/CD utilise maintenant `-p puppeteer-config.json` (compatible mmdc ancien et récent):
```yaml
- name: Generate Mermaid diagrams as SVG
  run: |
    cat > puppeteer-config.json <<'JSON'
    {
      "args": ["--no-sandbox", "--disable-setuid-sandbox"]
    }
    JSON
    mmdc -p puppeteer-config.json -i architecture.mmd -o generated/architecture.svg
```

#### 2. **Localement (macOS/Linux)**
Utiliser le script `generate-diagrams.sh` qui inclut déjà la config:
```bash
./generate-diagrams.sh
```

Ou manuellement avec le flag:
```bash
cat > docs/diagrams/puppeteer-config.json <<'JSON'
{
  "args": ["--no-sandbox", "--disable-setuid-sandbox"]
}
JSON
mmdc -p docs/diagrams/puppeteer-config.json \
  -i docs/diagrams/architecture.mmd \
  -o docs/diagrams/generated/architecture.svg
```

#### 3. **Windows PowerShell** - ✅ Fixed
Le script `generate-diagrams.ps1` inclut maintenant la config Puppeteer:
```powershell
.\generate-diagrams.ps1
```

#### 4. **Docker** - Alternative recommandée
```bash
docker run --rm -v $(pwd):/data node:20-alpine sh -c "
  npm install -g @mermaid-js/mermaid-cli
  cd /data/docs/diagrams
  cat > puppeteer-config.json <<'JSON'
  {
    \"args\": [\"--no-sandbox\", \"--disable-setuid-sandbox\"]
  }
  JSON
  mmdc -p puppeteer-config.json -i architecture.mmd -o generated/architecture.svg
"
```

---

## Autres Problèmes Communs

### ❌ "mermaid-cli not found"
```bash
# Installer globalement
npm install -g @mermaid-js/mermaid-cli

# Ou localement dans le projet
npm install --save-dev @mermaid-js/mermaid-cli
npx mmdc -i file.mmd -o output.svg
```

### ❌ "Permission denied: ./generate-diagrams.sh"
```bash
# Rendre le script exécutable
chmod +x generate-diagrams.sh
./generate-diagrams.sh
```

### ❌ "Output directory not writable"
```bash
# Créer le répertoire avec les permissions adéquates
mkdir -p docs/diagrams/generated
chmod 755 docs/diagrams/generated
```

### ❌ "Out of memory"
Si la génération échoue faute de mémoire:
```bash
# Windows
$env:NODE_OPTIONS = "--max-old-space-size=2048"

# macOS/Linux
export NODE_OPTIONS="--max-old-space-size=2048"
./generate-diagrams.sh
```

---

## 🧪 Test de la Configuration

```bash
# Test simple - générer un seul diagramme
cat > docs/diagrams/puppeteer-config.json <<'JSON'
{
  "args": ["--no-sandbox", "--disable-setuid-sandbox"]
}
JSON
mmdc -p docs/diagrams/puppeteer-config.json \
  -i docs/diagrams/architecture.mmd \
  -o test.svg

# Vérifier le résultat
ls -lh test.svg
file test.svg
```

---

## 📊 Alternative: Utiliser Mermaid Live

Si la génération pose problème, les diagrammes Mermaid peuvent être visualisés directement:

1. **Dans GitHub**: Les fichiers `.mmd` sont rendus nativement dans le navigateur
2. **En ligne**: [Mermaid Live Editor](https://mermaid.live/)
3. **VS Code**: Extension Mermaid Markdown Syntax Highlighting

---

## 🔗 Ressources

- [Mermaid CLI Troubleshooting](https://github.com/mermaid-js/mermaid-cli)
- [Puppeteer Troubleshooting](https://pptr.dev/troubleshooting)
- [Chromium Sandbox Issues](https://chromium.googlesource.com/chromium/src/+/main/docs/security/apparmor-userns-restrictions.md)

