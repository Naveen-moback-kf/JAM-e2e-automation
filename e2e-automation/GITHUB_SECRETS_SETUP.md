# 🔐 GitHub Secrets Setup Guide

This guide explains how to configure GitHub Secrets for your CI/CD pipeline.

---

## 📋 **Required Secrets**

Your workflow needs the following secrets to be configured in GitHub:

| Secret Name | Description | Example Value |
|-------------|-------------|---------------|
| `SSO_USERNAME` | SSO login username for KFONE | `AIAuto.user.one@kfdbhdevoutlook.onmicrosoft.com` |
| `SSO_PASSWORD` | SSO login password | `YourSecurePassword123!` |
| `NON_SSO_USERNAME` | Non-SSO login username | `clm.user.one@testkfy.com` |
| `NON_SSO_PASSWORD` | Non-SSO login password | `YourSecurePassword456!` |

---

## 🔧 **How to Add Secrets**

### **Step 1: Go to Repository Settings**

1. Open your repository: `https://github.com/Naveen-moback-kf/JAM-e2e-automation`
2. Click on **⚙️ Settings** tab (top right)
3. In the left sidebar, expand **🔐 Secrets and variables**
4. Click on **Actions**

### **Step 2: Add Each Secret**

For each secret (SSO_USERNAME, SSO_PASSWORD, NON_SSO_USERNAME, NON_SSO_PASSWORD):

1. Click **"New repository secret"** button
2. Enter the **Name** (e.g., `SSO_USERNAME`)
3. Enter the **Value** (e.g., `AIAuto.user.one@kfdbhdevoutlook.onmicrosoft.com`)
4. Click **"Add secret"**

**Repeat** for all 4 secrets.

---

## 🎯 **Values to Use (Based on Your config.properties)**

### **For DEV Environment (Default):**

```
Secret Name: SSO_USERNAME
Value: AIAuto.user.one@kfdbhdevoutlook.onmicrosoft.com

Secret Name: SSO_PASSWORD
Value: 202410TestDigital!

Secret Name: NON_SSO_USERNAME
Value: clm.user.one@testkfy.com

Secret Name: NON_SSO_PASSWORD
Value: 202510DigitalLog!
```

---

## ✅ **Verify Secrets**

After adding all secrets, you should see:

```
✅ SSO_USERNAME (Updated XX ago)
✅ SSO_PASSWORD (Updated XX ago)
✅ NON_SSO_USERNAME (Updated XX ago)
✅ NON_SSO_PASSWORD (Updated XX ago)
```

**Note:** You won't be able to view the secret values after adding them (security feature).

---

## 🔄 **How It Works**

### **1. Template File (Committed to Git)**
```properties
# config.properties.template
SSO_Login_Username=${SSO_USERNAME}
SSO_Login_Password=${SSO_PASSWORD}
```

### **2. GitHub Actions Workflow**
- Copies template to `config.properties`
- Replaces `${SSO_USERNAME}` with actual secret value
- Replaces `${SSO_PASSWORD}` with actual secret value

### **3. Final config.properties (Runtime)**
```properties
SSO_Login_Username=AIAuto.user.one@kfdbhdevoutlook.onmicrosoft.com
SSO_Login_Password=202410TestDigital!
```

---

## 🔒 **Security Benefits**

✅ **No credentials in Git repository**
✅ **Secrets encrypted by GitHub**
✅ **Secrets never logged in workflow output**
✅ **Different secrets for different environments**

---

## 📝 **Testing After Setup**

1. Add all 4 secrets to GitHub
2. Go to **Actions** tab
3. Click **"Job Mapping E2E Tests"** workflow
4. Click **"Run workflow"** dropdown
5. Configure options:
   - **Test Suite**: Choose smoke/regression/all tests
   - **Browser**: chrome/firefox/edge
   - **Environment**: dev/qa/stage/prodeu/produs
   - **Login Type**: SSO or NON_SSO
   - **PAMS ID**: Target client ID (e.g., 23139)
   - **Headless Mode**: true/false
   - **Parallel Execution**: true/false
6. Click **"Run workflow"** button

The workflow will:
- ✅ Create config.properties from template
- ✅ Configure login type and PAMS ID
- ✅ Inject your GitHub Secrets
- ✅ Run tests with proper credentials

---

## ❓ **Troubleshooting**

### **Issue: "401 Unauthorized" or Login Fails**

**Solution:** Verify your secrets are correct:
1. Go to Settings → Secrets → Actions
2. Delete the incorrect secret
3. Re-add it with the correct value

### **Issue: "Secrets not found"**

**Solution:** Ensure secret names match EXACTLY:
- ✅ `SSO_USERNAME` (correct)
- ❌ `sso_username` (wrong - case sensitive)
- ❌ `SSO USERNAME` (wrong - no spaces)

---

## 🎯 **Next Steps**

After adding secrets:
1. ✅ Commit the template file (`config.properties.template`)
2. ✅ Ensure `config.properties` is in `.gitignore`
3. ✅ Run the workflow to verify it works

---

**Need help? Check the workflow logs in the "Configure Test Environment" step to see if secrets are being injected correctly.**

