# PowerShell script to generate Mermaid and PlantUML diagrams locally
# Usage: .\generate-diagrams.ps1

$SCRIPT_DIR = (Get-Item -Path $PSScriptRoot).FullName
$DIAGRAMS_DIR = Join-Path $SCRIPT_DIR "docs\diagrams"
$OUTPUT_DIR = Join-Path $DIAGRAMS_DIR "generated"

Write-Host "📊 Generating Diagrams..." -ForegroundColor Cyan
Write-Host "🔍 Working directory: $DIAGRAMS_DIR" -ForegroundColor Gray

# Create output directory
if (-not (Test-Path $OUTPUT_DIR)) {
    New-Item -ItemType Directory -Force -Path $OUTPUT_DIR | Out-Null
}

Write-Host ""
Write-Host ("=" * 60) -ForegroundColor Yellow
Write-Host "1️⃣  MERMAID DIAGRAMS (Markdown embedded)" -ForegroundColor Green
Write-Host ("=" * 60) -ForegroundColor Yellow
Write-Host ""

# Check if mermaid-cli is installed
$mmdc = Get-Command mmdc -ErrorAction SilentlyContinue

if (-not $mmdc) {
    Write-Host "⚠️  mermaid-cli not found. Installing..." -ForegroundColor Yellow
    npm install -g @mermaid-js/mermaid-cli
}

# Set Puppeteer config for Chromium sandbox
$PUPPETEER_CONFIG = '{"args":["--no-sandbox","--disable-setuid-sandbox"]}'

# Generate Mermaid diagrams
$mermaidFiles = @(
    @{ input = "architecture.mmd"; output = "architecture.svg" },
    @{ input = "ci-cd-pipeline.mmd"; output = "ci-cd-pipeline.svg" },
    @{ input = "test-execution-flow.mmd"; output = "test-execution-flow.svg" },
    @{ input = "module-dependencies.mmd"; output = "module-dependencies.svg" },
    @{ input = "deployment.mmd"; output = "deployment.svg" },
    @{ input = "test-pyramid.mmd"; output = "test-pyramid.svg" }
)

foreach ($file in $mermaidFiles) {
    $inputPath = Join-Path $DIAGRAMS_DIR $file.input
    $outputPath = Join-Path $OUTPUT_DIR $file.output
    
    Write-Host "📐 Generating $($file.output)..." -ForegroundColor Cyan
    mmdc -i $inputPath `
         -o $outputPath `
         --theme dark `
         --backgroundColor white `
         --width 1200 `
         --height 800 `
         --puppeteerConfig $PUPPETEER_CONFIG
}

Write-Host ""
Write-Host ("=" * 60) -ForegroundColor Yellow
Write-Host "2️⃣  PLANTUML DIAGRAMS (Optional - if Java available)" -ForegroundColor Green
Write-Host ("=" * 60) -ForegroundColor Yellow
Write-Host ""

# Check if PlantUML is available
$plantuml = Get-Command plantuml -ErrorAction SilentlyContinue

if ($plantuml) {
    Write-Host "📐 Generating PlantUML diagrams..." -ForegroundColor Cyan
    Get-ChildItem -Path $DIAGRAMS_DIR -Filter "*.puml" | ForEach-Object {
        plantuml $_.FullName -o $OUTPUT_DIR
    }
    Write-Host "✅ PlantUML diagrams generated" -ForegroundColor Green
} else {
    Write-Host "⚠️  PlantUML not found. Skipping PlantUML generation." -ForegroundColor Yellow
    Write-Host "    Install PlantUML or use an online editor to generate PlantUML diagrams." -ForegroundColor Gray
}

Write-Host ""
Write-Host ("=" * 60) -ForegroundColor Yellow
Write-Host "📊 SUMMARY" -ForegroundColor Cyan
Write-Host ("=" * 60) -ForegroundColor Yellow
Write-Host ""
Write-Host "✅ Diagram generation complete!" -ForegroundColor Green
Write-Host ""
Write-Host "Generated files:" -ForegroundColor Cyan
Get-ChildItem -Path $OUTPUT_DIR | Select-Object Name, Length | Format-Table @{L="Name"; E={$_.Name}}, @{L="Size"; E={"$([Math]::Round($_.Length/1024, 2)) KB"}}

Write-Host ""
Write-Host "📚 Documentation:" -ForegroundColor Cyan
Write-Host "  - Mermaid diagrams: $DIAGRAMS_DIR\README.md" -ForegroundColor Gray
Write-Host "  - PlantUML guide: $DIAGRAMS_DIR\PLANTUML.md" -ForegroundColor Gray
Write-Host ""
Write-Host "🚀 Next steps:" -ForegroundColor Green
Write-Host "  1. Review generated diagrams in: $OUTPUT_DIR" -ForegroundColor Gray
Write-Host "  2. Commit changes: git add docs/diagrams" -ForegroundColor Gray
Write-Host "  3. Push to repository" -ForegroundColor Gray
Write-Host ""
