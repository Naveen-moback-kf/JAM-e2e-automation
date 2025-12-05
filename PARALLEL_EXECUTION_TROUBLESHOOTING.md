# Parallel Execution Troubleshooting Guide

## Common Failure Patterns & Solutions

### 1. **Stale Element Reference Exceptions**

**Symptoms:**
- Tests pass individually but fail in parallel
- Error: `StaleElementReferenceException`
- Elements found but become stale before interaction

**Root Causes:**
- DOM changes between element location and interaction
- Multiple threads accessing same page elements
- Page refreshes or dynamic content updates

**Solutions:**

#### ✅ Always Use Locators Instead of Cached WebElements
```java
// ❌ BAD: Caching element reference
WebElement button = driver.findElement(By.id("submit"));
Thread.sleep(1000); // DOM might change here
button.click(); // May throw StaleElementReferenceException

// ✅ GOOD: Re-fetch element using locator
By buttonLocator = By.id("submit");
wait.until(ExpectedConditions.elementToBeClickable(buttonLocator)).click();
```

#### ✅ Use Retry Mechanism for Stale Elements
```java
// Use the existing retry mechanism
PageObjectHelper.retryOnStaleElement(LOGGER, () -> {
    WebElement element = driver.findElement(locator);
    element.click();
    return null;
});
```

#### ✅ Wait for Element Freshness Before Interaction
```java
// Wait for element to be present and clickable
WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
element.click();
```

---

### 2. **Element Not Found / Timeout Exceptions**

**Symptoms:**
- `NoSuchElementException` in parallel but not sequential
- `TimeoutException` during element waits
- Elements take longer to appear in parallel execution

**Root Causes:**
- Page load timing differences under parallel load
- Resource contention (CPU, memory, network)
- Insufficient wait times for parallel execution

**Solutions:**

#### ✅ Increase Wait Timeouts for Parallel Execution
```java
// For parallel execution, use longer timeouts
int timeout = isParallelExecution() ? 20 : 10;
WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
```

#### ✅ Use Explicit Waits Instead of Implicit Waits
```java
// ❌ BAD: Relying on implicit wait
driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
driver.findElement(By.id("element")); // May fail if element takes longer

// ✅ GOOD: Explicit wait with conditions
WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
wait.until(ExpectedConditions.presenceOfElementLocated(By.id("element")));
```

#### ✅ Wait for Page Readiness Before Element Interaction
```java
// Always wait for page to be ready
PerformanceUtils.waitForPageReady(driver, 3);
PerformanceUtils.waitForSpinnersToDisappear(driver, 5);

// Then find element
WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
```

---

### 3. **Test Data Conflicts**

**Symptoms:**
- Tests interfere with each other's data
- Same test data used by multiple threads
- Database/API conflicts

**Solutions:**

#### ✅ Use Thread-Safe Test Data
```java
// Use ThreadLocal for test data isolation
private static ThreadLocal<String> testData = ThreadLocal.withInitial(() -> {
    return "TestData_" + Thread.currentThread().getId() + "_" + System.currentTimeMillis();
});
```

#### ✅ Use Unique Test Data Per Thread
```java
// Generate unique test data per thread
String uniqueTestId = "Test_" + Thread.currentThread().getId() + "_" + System.currentTimeMillis();
```

---

### 4. **Browser Instance Conflicts**

**Symptoms:**
- Browser crashes or becomes unresponsive
- WebDriver session lost
- "Session not found" errors

**Solutions:**

#### ✅ Verify ThreadLocal WebDriver Isolation
```java
// Ensure each thread has its own driver
WebDriver driver = DriverManager.getDriver(); // Returns thread-specific driver
if (driver == null) {
    DriverManager.launchBrowser(); // Initialize for this thread
}
```

#### ✅ Check Driver Session Before Use
```java
// Verify driver session is active
if (!DriverManager.isSessionActive()) {
    LOGGER.warn("Driver session lost, reinitializing...");
    DriverManager.launchBrowser();
}
```

---

### 5. **Timing and Race Conditions**

**Symptoms:**
- Intermittent failures
- Tests pass sometimes, fail other times
- Order-dependent failures

**Solutions:**

#### ✅ Replace Thread.sleep() with Explicit Waits
```java
// ❌ BAD: Fixed sleep time
Thread.sleep(2000); // May not be enough in parallel

// ✅ GOOD: Wait for specific condition
PerformanceUtils.waitForPageReady(driver, 3);
wait.until(ExpectedConditions.elementToBeClickable(locator));
```

#### ✅ Add Stability Waits After Actions
```java
// After clicking, wait for UI to stabilize
element.click();
PerformanceUtils.waitForUIStability(driver, 1); // Wait for animations/updates
```

---

## Debugging Parallel Execution Failures

### Step 1: Identify Which Tests Are Failing
```bash
# Run tests and capture failures
mvn test -DsuiteXmlFile=JAM_SanityTestSuite.xml > parallel_execution.log 2>&1

# Check for patterns
grep -i "failed\|exception\|error" parallel_execution.log
```

### Step 2: Check Thread Isolation
```java
// Add logging to verify thread isolation
LOGGER.info("Thread: {} | Driver: {} | Test: {}", 
    Thread.currentThread().getName(),
    DriverManager.getDriver().hashCode(),
    testName);
```

### Step 3: Enable Detailed Logging
```properties
# In log4j2.properties, set to DEBUG
logger.parallel.name=com.kfonetalentsuite
logger.parallel.level=DEBUG
```

### Step 4: Run Tests Sequentially to Compare
```bash
# Run sequentially to verify tests pass
mvn test -DsuiteXmlFile=JAM_SanityTestSuite.xml -Dparallel=false -DthreadCount=1
```

---

## Quick Fixes Checklist

- [ ] Replace all `Thread.sleep()` with explicit waits
- [ ] Use locators instead of cached WebElement references
- [ ] Add retry logic for stale element exceptions
- [ ] Increase wait timeouts for parallel execution
- [ ] Verify ThreadLocal isolation for all shared state
- [ ] Add page readiness checks before element interactions
- [ ] Use unique test data per thread
- [ ] Add stability waits after UI actions
- [ ] Verify driver session before use
- [ ] Check for static variables that aren't ThreadLocal

---

## Recommended Wait Strategy for Parallel Execution

```java
public static WebElement waitForElementSafely(WebDriver driver, By locator, int timeoutSeconds) {
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
    
    // Wait for page to be ready first
    PerformanceUtils.waitForPageReady(driver, 2);
    
    // Wait for element with retry on stale
    int maxRetries = 3;
    for (int attempt = 1; attempt <= maxRetries; attempt++) {
        try {
            return wait.until(ExpectedConditions.elementToBeClickable(locator));
        } catch (StaleElementReferenceException e) {
            if (attempt == maxRetries) throw e;
            LOGGER.debug("Stale element on attempt {}, retrying...", attempt);
            try {
                Thread.sleep(300);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
        }
    }
    throw new RuntimeException("Element not found after retries");
}
```

---

## Contact & Support

If issues persist after applying these fixes:
1. Capture detailed logs with thread information
2. Identify specific failing scenarios
3. Check if failures are consistent or intermittent
4. Verify TestNG XML parallel configuration

