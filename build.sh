# build.ps1

# shellcheck disable=SC1036
# shellcheck disable=SC1088
# shellcheck disable=SC1065
param(
    [string]$TestType = "all"  # "java", "flutter", or "all"
)

Write-Host "======================================" -ForegroundColor Cyan
Write-Host "Atomberg App – Hybrid Test Suite" -ForegroundColor Cyan
Write-Host "======================================" -ForegroundColor Cyan

# Test Appium/Java tests
# shellcheck disable=SC1050
# shellcheck disable=SC1073
# shellcheck disable=SC1072
if ($TestType -eq "java" -or $TestType -eq "all") {
    Write-Host "`n[1/2] Running Java/Appium Tests..." -ForegroundColor Yellow
    mvn clean test
    if ($LASTEXITCODE -ne 0) {
        Write-Host "❌ Java tests failed" -ForegroundColor Red
        exit 1
    }
    Write-Host "✅ Java tests passed" -ForegroundColor Green
}
# Test Flutter tests
if ($TestType -eq "flutter" -or $TestType -eq "all") {
    Write-Host "`n[2/2] Running Flutter Integration Tests..." -ForegroundColor Yellow
    Set-Location flutter_app
    flutter test integration_test/
    if ($LASTEXITCODE -ne 0)  {
        Write-Host "❌ Flutter tests failed" -ForegroundColor Red
        Set-Location ..
        exit 1
    }
    Write-Host "✅ Flutter tests passed" -ForegroundColor Green
    Set-Location ..
}

Write-Host "`n✅ All tests completed successfully!" -ForegroundColor Green