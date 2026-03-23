#!/bin/bash
# Script to generate Mermaid and PlantUML diagrams locally
# Usage: ./generate-diagrams.sh

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
DIAGRAMS_DIR="$SCRIPT_DIR/docs/diagrams"
OUTPUT_DIR="$DIAGRAMS_DIR/generated"

echo "📊 Generating Diagrams..."
echo "🔍 Working directory: $DIAGRAMS_DIR"

# Create output directory
mkdir -p "$OUTPUT_DIR"

echo ""
echo "=" | str_repeat 60
echo "1️⃣  MERMAID DIAGRAMS (Markdown embedded)"
echo "=" | str_repeat 60
echo ""

# Check if mermaid-cli is installed
if ! command -v mmdc &> /dev/null; then
    echo "⚠️  mermaid-cli not found. Installing..."
    npm install -g @mermaid-js/mermaid-cli
fi

# Generate Mermaid diagrams
PUPPETEER_CONFIG='{"args":["--no-sandbox","--disable-setuid-sandbox"]}'

echo "📐 Generating architecture.svg..."
mmdc -i "$DIAGRAMS_DIR/architecture.mmd" \
      -o "$OUTPUT_DIR/architecture.svg" \
      --theme dark \
      --backgroundColor white \
      --width 1200 \
      --height 800 \
      --puppeteerConfig "$PUPPETEER_CONFIG"

echo "📐 Generating ci-cd-pipeline.svg..."
mmdc -i "$DIAGRAMS_DIR/ci-cd-pipeline.mmd" \
      -o "$OUTPUT_DIR/ci-cd-pipeline.svg" \
      --theme dark \
      --backgroundColor white \
      --puppeteerConfig "$PUPPETEER_CONFIG"

echo "📐 Generating test-execution-flow.svg..."
mmdc -i "$DIAGRAMS_DIR/test-execution-flow.mmd" \
      -o "$OUTPUT_DIR/test-execution-flow.svg" \
      --theme dark \
      --backgroundColor white \
      --puppeteerConfig "$PUPPETEER_CONFIG"

echo "📐 Generating module-dependencies.svg..."
mmdc -i "$DIAGRAMS_DIR/module-dependencies.mmd" \
      -o "$OUTPUT_DIR/module-dependencies.svg" \
      --theme dark \
      --backgroundColor white \
      --puppeteerConfig "$PUPPETEER_CONFIG"

echo "📐 Generating deployment.svg..."
mmdc -i "$DIAGRAMS_DIR/deployment.mmd" \
      -o "$OUTPUT_DIR/deployment.svg" \
      --theme dark \
      --backgroundColor white \
      --puppeteerConfig "$PUPPETEER_CONFIG"

echo "📐 Generating test-pyramid.svg..."
mmdc -i "$DIAGRAMS_DIR/test-pyramid.mmd" \
      -o "$OUTPUT_DIR/test-pyramid.svg" \
      --theme dark \
      --backgroundColor white \
      --puppeteerConfig "$PUPPETEER_CONFIG"

echo ""
echo "=" | str_repeat 60
echo "2️⃣  PLANTUML DIAGRAMS (Optional - if Java/Docker available)"
echo "=" | str_repeat 60
echo ""

# Check if PlantUML is available
if command -v plantuml &> /dev/null; then
    echo "📐 Generating PlantUML diagrams..."
    plantuml "$DIAGRAMS_DIR/*.puml" -o "$OUTPUT_DIR" 2>/dev/null || true
    echo "✅ PlantUML diagrams generated"
elif command -v docker &> /dev/null; then
    echo "📐 Generating PlantUML diagrams with Docker..."
    docker run --rm -v "$DIAGRAMS_DIR:/data" plantuml/plantuml \
      /data/*.puml -o /data/generated 2>/dev/null || true
    echo "✅ PlantUML diagrams generated"
else
    echo "⚠️  PlantUML and Docker not found. Skipping PlantUML generation."
    echo "    Install plantuml or Docker to generate PlantUML diagrams."
fi

echo ""
echo "=" | str_repeat 60
echo "📊 SUMMARY"
echo "=" | str_repeat 60
echo ""
echo "✅ Diagram generation complete!"
echo ""
echo "Generated files:"
ls -lh "$OUTPUT_DIR"/ | tail -n +2 | awk '{print "  - " $9 " (" $5 ")"}'

echo ""
echo "📚 Documentation:"
echo "  - Mermaid diagrams: $DIAGRAMS_DIR/README.md"
echo "  - PlantUML guide: $DIAGRAMS_DIR/PLANTUML.md"
echo ""
echo "🚀 Next steps:"
echo "  1. Review generated diagrams in: $OUTPUT_DIR"
echo "  2. Commit changes: git add docs/diagrams"
echo "  3. Push to repository"
echo ""
