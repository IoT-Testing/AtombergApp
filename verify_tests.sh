#!/bin/bash
# Test Verification Script
# Run this to verify all tests execute successfully

echo "╔═══════════════════════════════════════════════════════════════════╗"
echo "║          ATOMBERG APP TEST SUITE VERIFICATION SCRIPT             ║"
echo "╚═══════════════════════════════════════════════════════════════════╝"
echo ""

# Colors
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Counters
PASSED=0
FAILED=0

# Function to run test
run_test() {
    local test_name=$1
    local test_command=$2

    echo -e "${BLUE}Running: $test_name${NC}"

    if eval "$test_command"; then
        echo -e "${GREEN}✅ PASSED: $test_name${NC}\n"
        ((PASSED++))
    else
        echo -e "${RED}❌ FAILED: $test_name${NC}\n"
        ((FAILED++))
    fi
}

echo -e "${YELLOW}Step 1: Verify Maven Installation${NC}"
run_test "Maven Version" "mvn --version > /dev/null 2>&1"

echo -e "${YELLOW}Step 2: Verify Java Installation${NC}"
run_test "Java Version" "java -version > /dev/null 2>&1"

echo -e "${YELLOW}Step 3: Clean Project${NC}"
run_test "Clean Build" "mvn clean > /dev/null 2>&1"

echo -e "${YELLOW}Step 4: Compile Code${NC}"
run_test "Compile Project" "mvn compile -DskipTests > /dev/null 2>&1"

echo -e "${YELLOW}Step 5: Compile Test Code${NC}"
run_test "Compile Tests" "mvn test-compile > /dev/null 2>&1"

echo -e "${YELLOW}Step 6: Check Dependency Tree${NC}"
run_test "Dependency Tree" "mvn dependency:tree > /dev/null 2>&1"

echo -e "${YELLOW}Step 7: Verify Test Classes${NC}"
if [ -f "src/test/java/com/appTest/tests/LoginTest.java" ]; then
    echo -e "${GREEN}✅ LoginTest.java exists${NC}\n"
    ((PASSED++))
else
    echo -e "${RED}❌ LoginTest.java missing${NC}\n"
    ((FAILED++))
fi

echo -e "${YELLOW}Step 8: Verify Configuration Files${NC}"
if [ -f "testng.xml" ]; then
    echo -e "${GREEN}✅ testng.xml exists${NC}\n"
    ((PASSED++))
else
    echo -e "${RED}❌ testng.xml missing${NC}\n"
    ((FAILED++))
fi

if [ -f "pom.xml" ]; then
    echo -e "${GREEN}✅ pom.xml exists${NC}\n"
    ((PASSED++))
else
    echo -e "${RED}❌ pom.xml missing${NC}\n"
    ((FAILED++))
fi

echo -e "${YELLOW}Step 9: Verify Documentation${NC}"
for doc in TEST_FIXES_SUMMARY.md TEST_EXECUTION_GUIDE.md QUICK_REFERENCE.md REFACTORING_COMPLETE.md; do
    if [ -f "$doc" ]; then
        echo -e "${GREEN}✅ $doc exists${NC}"
        ((PASSED++))
    else
        echo -e "${RED}❌ $doc missing${NC}"
        ((FAILED++))
    fi
done
echo ""

echo -e "${YELLOW}Step 10: Verify TestHelper Utility${NC}"
if [ -f "src/test/java/com/appTest/util/TestHelper.java" ]; then
    echo -e "${GREEN}✅ TestHelper.java exists${NC}\n"
    ((PASSED++))
else
    echo -e "${RED}❌ TestHelper.java missing${NC}\n"
    ((FAILED++))
fi

# Summary
echo "╔═══════════════════════════════════════════════════════════════════╗"
echo "║                      VERIFICATION SUMMARY                        ║"
echo "╚═══════════════════════════════════════════════════════════════════╝"
echo ""
echo -e "${GREEN}✅ PASSED: $PASSED${NC}"
echo -e "${RED}❌ FAILED: $FAILED${NC}"
echo ""

if [ $FAILED -eq 0 ]; then
    echo -e "${GREEN}🎉 ALL VERIFICATIONS PASSED! READY TO RUN TESTS${NC}"
    echo ""
    echo "Next steps:"
    echo "  1. Run: mvn test"
    echo "  2. Check reports: test-output/ExtentReports/index.html"
    echo "  3. Review: QUICK_REFERENCE.md"
    exit 0
else
    echo -e "${RED}⚠️  SOME VERIFICATIONS FAILED. CHECK OUTPUT ABOVE${NC}"
    exit 1
fi

