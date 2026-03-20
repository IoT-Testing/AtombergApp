# Docker Permission Error - Troubleshooting Guide

## Error Message
```
permission denied while trying to connect to the docker API at npipe:////./pipe/dockerDesktopLinuxEngine
```

## Root Cause
Docker Desktop daemon is not properly connected or has permission issues. This typically happens when:
1. Docker Desktop crashes or disconnects
2. PowerShell session doesn't have proper permissions
3. Docker daemon socket is inaccessible
4. Windows file permissions are blocking access

## Solution Steps

### ✅ Quick Fix (Recommended)

**Option 1: Run as Administrator**

1. Close current PowerShell window
2. Press `Win + X`
3. Select **"Windows Terminal (Admin)"** or **"Windows PowerShell (Admin)"`
4. Run the fix script:
   ```powershell
   D:\AtombergAppBoF\fix-docker-permissions.ps1
   ```

**Option 2: Manual Docker Restart**

1. Stop Docker Desktop:
   - Right-click Docker icon in system tray
   - Select "Quit Docker Desktop"
   
2. Wait 3 seconds

3. Start Docker Desktop:
   - Double-click Docker Desktop from Start Menu
   - Or run: `"C:\Program Files\Docker\Docker\Docker Desktop.exe"`
   
4. Wait 30-60 seconds for full initialization

5. Test the connection:
   ```powershell
   docker ps
   ```

### 🔧 Detailed Troubleshooting

#### Step 1: Verify Administrator Privileges
```powershell
# Check if running as admin
[Security.Principal.WindowsPrincipal]::new(
    [Security.Principal.WindowsIdentity]::GetCurrent()
).IsInRole("Administrator")

# Should return: True
```

#### Step 2: Check Docker Context
```powershell
# List available contexts
docker context ls

# Should show "desktop-linux" as current
```

#### Step 3: Check Docker Daemon Status
```powershell
# Get full Docker info
docker info

# Should show OSType: linux
```

#### Step 4: Test Image Pull
```powershell
# Try pulling a test image
docker pull alpine:latest

# Monitor progress
```

#### Step 5: Reset Docker Settings
If above steps don't work:

1. Open Docker Desktop Settings
2. Select **"Troubleshoot"** tab
3. Click **"Clean / Purge Data"**
4. Click **"Reset Kubernetes Cluster"**
5. Restart Docker Desktop

### 🛠️ Advanced Fixes

**Clear Docker Configuration Cache**
```powershell
# Stop Docker
Get-Process Docker | Stop-Process -Force

# Clear cache
Remove-Item "$env:USERPROFILE\.docker" -Recurse -Force -ErrorAction SilentlyContinue

# Restart Docker
& "C:\Program Files\Docker\Docker\Docker Desktop.exe"
```

**Reset Docker to Factory Settings**
```powershell
# Stop Docker first
Get-Process Docker | Stop-Process -Force

# Remove Docker Desktop data
Remove-Item "$env:APPDATA\Docker" -Recurse -Force -ErrorAction SilentlyContinue

# Reinstall Docker Desktop from scratch
# Download from: https://www.docker.com/products/docker-desktop
```

**Check for Port Conflicts**
```powershell
# Find what's using Docker pipes
Get-Process | Where-Object {$_.Handles -gt 1000} | Sort-Object Handles -Descending

# Kill conflicting processes if needed
Stop-Process -Name "VpnClient" -Force -ErrorAction SilentlyContinue
Stop-Process -Name "vpnkit" -Force -ErrorAction SilentlyContinue
```

### ⚡ Quick Commands Reference

```powershell
# Start Docker properly
& "C:\Program Files\Docker\Docker\Docker Desktop.exe"

# Wait and check
Start-Sleep -Seconds 30
docker ps

# Test with simple image
docker run hello-world

# Test with Node image
docker pull node:8-alpine

# Start docker-compose
cd D:\AtombergAppBoF
docker-compose up -d
```

## Verification Checklist

- [ ] Running PowerShell as Administrator
- [ ] Docker Desktop is running (check system tray)
- [ ] `docker ps` returns empty list (not error)
- [ ] `docker info` shows Linux container info
- [ ] Can pull images: `docker pull alpine:latest`
- [ ] Docker context is "desktop-linux"

## After Fix: Start STF

Once Docker is working:

```powershell
cd D:\AtombergAppBoF

# Validate setup
.\validate-docker.ps1

# Start services
docker-compose up -d

# Check status
docker-compose ps

# View logs
docker-compose logs -f
```

## Still Having Issues?

### Collect Diagnostic Info
```powershell
# Docker version
docker version

# Docker info
docker info

# Docker context
docker context ls

# System info
systeminfo | findstr /C:"OS Version"
```

### Report Issue
Include the output of the diagnostic commands above when:
1. Opening a GitHub issue
2. Asking for help in Docker community
3. Contacting Docker support

## Prevention Tips

1. **Always run as Administrator** when using Docker on Windows
2. **Don't force-quit** Docker processes - use proper shutdown
3. **Keep Docker Desktop updated** - Check for updates regularly
4. **Monitor disk space** - Docker needs free space to function
5. **Restart regularly** - Restart Docker periodically for stability

---

**Still stuck?** Try the automated fix:
```powershell
D:\AtombergAppBoF\fix-docker-permissions.ps1
```

