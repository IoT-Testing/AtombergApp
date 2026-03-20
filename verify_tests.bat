@echo off
REM Test Verification Script for Windows
REM Run this to verify all tests execute successfully

setlocal enabledelayedexpansion

echo.
echo ╔═══════════════════════════════════════════════════════════════════╗
echo ║          ATOMBERG APP TEST SUITE VERIFICATION SCRIPT             ║
echo ╚═══════════════════════════════════════════════════════════════════╝
echo.

set /a PASSED=0
set /a FAILED=0

REM Colors using ANSI codes (Windows 10+ supports this)
REM Or use simple output

echo ============ Step 1: Verify Maven Installation ============
mvn --version >nul 2>&1
if !errorlevel! equ 0 (
    echo [PASS] Maven is installed
    set /a PASSED+=1
) else (
    echo [FAIL] Maven is not installed
    set /a FAILED+=1
)

echo.
echo ============ Step 2: Verify Java Installation ============
java -version >nul 2>&1
if !errorlevel! equ 0 (
    echo [PASS] Java is installed
    set /a PASSED+=1
) else (
    echo [FAIL] Java is not installed
    set /a FAILED+=1
)

echo.
echo ============ Step 3: Clean Project ============
mvn clean >nul 2>&1
if !errorlevel! equ 0 (
    echo [PASS] Project cleaned
    set /a PASSED+=1
) else (
    echo [FAIL] Clean failed
    set /a FAILED+=1
)

echo.
echo ============ Step 4: Compile Code ============
mvn compile -DskipTests >nul 2>&1
if !errorlevel! equ 0 (
    echo [PASS] Code compiled
    set /a PASSED+=1
) else (
    echo [FAIL] Compilation failed
    set /a FAILED+=1
)

echo.
echo ============ Step 5: Compile Test Code ============
mvn test-compile >nul 2>&1
if !errorlevel! equ 0 (
    echo [PASS] Tests compiled
    set /a PASSED+=1
) else (
    echo [FAIL] Test compilation failed
    set /a FAILED+=1
)

echo.
echo ============ Step 6: Check Dependency Tree ============
mvn dependency:tree >nul 2>&1
if !errorlevel! equ 0 (
    echo [PASS] Dependencies resolved
    set /a PASSED+=1
) else (
    echo [FAIL] Dependency check failed
    set /a FAILED+=1
)

echo.
echo ============ Step 7: Verify Test Classes ============
if exist "src\test\java\com\appTest\tests\LoginTest.java" (
    echo [PASS] LoginTest.java exists
    set /a PASSED+=1
) else (
    echo [FAIL] LoginTest.java missing
    set /a FAILED+=1
)

echo.
echo ============ Step 8: Verify Configuration Files ============
if exist "testng.xml" (
    echo [PASS] testng.xml exists
    set /a PASSED+=1
) else (
    echo [FAIL] testng.xml missing
    set /a FAILED+=1
)

if exist "pom.xml" (
    echo [PASS] pom.xml exists
    set /a PASSED+=1
) else (
    echo [FAIL] pom.xml missing
    set /a FAILED+=1
)

echo.
echo ============ Step 9: Verify Documentation Files ============
set docs=TEST_FIXES_SUMMARY.md TEST_EXECUTION_GUIDE.md QUICK_REFERENCE.md REFACTORING_COMPLETE.md

for %%d in (%docs%) do (
    if exist "%%d" (
        echo [PASS] %%d exists
        set /a PASSED+=1
    ) else (
        echo [FAIL] %%d missing
        set /a FAILED+=1
    )
)

echo.
echo ============ Step 10: Verify TestHelper Utility ============
if exist "src\test\java\com\appTest\util\TestHelper.java" (
    echo [PASS] TestHelper.java exists
    set /a PASSED+=1
) else (
    echo [FAIL] TestHelper.java missing
    set /a FAILED+=1
)

echo.
echo ╔═══════════════════════════════════════════════════════════════════╗
echo ║                      VERIFICATION SUMMARY                        ║
echo ╚═══════════════════════════════════════════════════════════════════╝
echo.
echo PASSED: !PASSED!
echo FAILED: !FAILED!
echo.

if !FAILED! equ 0 (
    echo [SUCCESS] All verifications passed! Ready to run tests.
    echo.
    echo Next steps:
    echo   1. Run: mvn test
    echo   2. Check reports: test-output\ExtentReports\index.html
    echo   3. Review: QUICK_REFERENCE.md
    exit /b 0
) else (
    echo [ERROR] Some verifications failed. Check output above.
    exit /b 1
)

endlocal

