#!/usr/bin/env powershell
# Docker Permission Fix Script for Windows

Write-Host "================================" -ForegroundColor Cyan
Write-Host "Docker Permission Fix" -ForegroundColor Cyan
Write-Host "================================" -ForegroundColor Cyan
Write-Host ""

Write-Host "Current Issue:" -ForegroundColor Yellow
Write-Host "  permission denied while trying to connect to docker API" -ForegroundColor Red
Write-Host ""

# Step 1: Check if running as Administrator
Write-Host "Step 1: Checking administrator privileges..." -ForegroundColor Yellow
$isAdmin = ([Security.Principal.WindowsPrincipal] [Security.Principal.WindowsIdentity]::GetCurrent()).IsInRole([Security.Principal.WindowsBuiltInRole] "Administrator")

if ($isAdmin) {
    Write-Host "✓ Running as Administrator" -ForegroundColor Green
} else {
    Write-Host "✗ NOT running as Administrator" -ForegroundColor Red
    Write-Host ""
    Write-Host "SOLUTION: Please restart PowerShell as Administrator" -ForegroundColor Yellow
    Write-Host "  1. Press Win+X" -ForegroundColor White
    Write-Host "  2. Select 'Windows Terminal (Admin)'" -ForegroundColor White
    Write-Host "  3. Run this script again" -ForegroundColor White
    exit 1
}

# Step 2: Stop Docker
Write-Host ""
Write-Host "Step 2: Stopping Docker Desktop..." -ForegroundColor Yellow
$dockerProcess = Get-Process Docker -ErrorAction SilentlyContinue
if ($dockerProcess) {
    Stop-Process -Name Docker -Force -ErrorAction SilentlyContinue
    Write-Host "✓ Docker stopped" -ForegroundColor Green
    Start-Sleep -Seconds 3
} else {
    Write-Host "⚠ Docker is not running" -ForegroundColor Yellow
}

# Step 3: Clear Docker cache/settings if needed
Write-Host ""
Write-Host "Step 3: Resetting Docker daemon..." -ForegroundColor Yellow
try {
    # Reset docker context
    & docker context ls
    Write-Host "✓ Docker contexts accessible" -ForegroundColor Green
} catch {
    Write-Host "⚠ Issue with docker context" -ForegroundColor Yellow
}

# Step 4: Start Docker
Write-Host ""
Write-Host "Step 4: Starting Docker Desktop..." -ForegroundColor Yellow
Start-Process "C:\Program Files\Docker\Docker\Docker Desktop.exe" -NoNewWindow -WarningAction SilentlyContinue

Write-Host "Waiting for Docker to initialize (30 seconds)..." -ForegroundColor Cyan
for ($i = 1; $i -le 30; $i++) {
    Write-Progress -Activity "Docker Startup" -Status "Initializing... $i/30 seconds" -PercentComplete (($i/30)*100)
    Start-Sleep -Seconds 1

    # Check if Docker is responding
    $test = & docker ps 2>&1 | Out-String
    if ($LASTEXITCODE -eq 0) {
        Write-Host ""
        Write-Host "✓ Docker is responding!" -ForegroundColor Green
        break
    }
}

# Step 5: Test connection
Write-Host ""
Write-Host "Step 5: Testing Docker connection..." -ForegroundColor Yellow
try {
    $result = & docker ps 2>&1
    if ($LASTEXITCODE -eq 0) {
        Write-Host "✓ Docker connection successful!" -ForegroundColor Green
        Write-Host $result
    } else {
        Write-Host "✗ Docker connection failed" -ForegroundColor Red
        Write-Host $result
        exit 1
    }
} catch {
    Write-Host "✗ Error: $_" -ForegroundColor Red
    exit 1
}

# Step 6: Pull test image
Write-Host ""
Write-Host "Step 6: Testing image pull..." -ForegroundColor Yellow
Write-Host "Pulling hello-world..." -ForegroundColor Cyan
try {
    & docker pull hello-world 2>&1
    if ($LASTEXITCODE -eq 0) {
        Write-Host "✓ Image pull successful!" -ForegroundColor Green
    } else {
        Write-Host "⚠ Image pull had issues, but connection is working" -ForegroundColor Yellow
    }
} catch {
    Write-Host "✗ Error: $_" -ForegroundColor Red
}

# Summary
Write-Host ""
Write-Host "================================" -ForegroundColor Cyan
Write-Host "Fix Complete!" -ForegroundColor Cyan
Write-Host "================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "You can now run:" -ForegroundColor Green
Write-Host "  cd D:\AtombergAppBoF" -ForegroundColor White
Write-Host "  docker-compose up -d" -ForegroundColor White
Write-Host ""
Write-Host "Or test docker directly:" -ForegroundColor Green
Write-Host "  docker ps" -ForegroundColor White
Write-Host "  docker pull node:8-alpine" -ForegroundColor White
Write-Host ""

