# STF Docker Compose Setup Guide

## Issues Fixed

✅ Removed obsolete `version` attribute from docker-compose.yml
✅ Updated to use standard Linux-based images (Alpine Linux for lightweight containers)
✅ Replaced Windows-specific commands with Linux shell commands

## Current Issue: Docker Engine Mode

Your Docker Desktop is currently running in **Linux container mode** (which is what we need), but the error suggests there might be a connection issue.

### Error Analysis
```
unable to get image 'mcr.microsoft.com/windows/servercore:ltsc2022': permission denied while trying to connect to the docker API at npipe:////./pipe/dockerDesktopLinuxEngine
```

**This error indicates:**
- You were using Windows container images (mcr.microsoft.com/windows/servercore)
- Docker tried to connect via Linux engine (which uses npipe)
- Permission denied = likely a path or connection issue

### Solution Applied

The docker-compose.yml has been updated to use:
- **rethinkdb:2.4.2** - Official RethinkDB Linux image
- **node:8-alpine** - Lightweight Node.js Linux image for STF services

All services now use Linux containers which are lighter, faster, and more compatible.

## Setup Instructions

### Step 1: Verify Docker Desktop is in Linux Mode

Run the switcher script:
```powershell
.\switch-docker-mode.ps1
```

Or manually:
1. Right-click Docker Desktop icon in system tray
2. Select "Switch to Linux containers..."
3. Confirm the switch

### Step 2: Build and Start Services

```powershell
# Navigate to the project directory
cd D:\AtombergAppBoF

# Start all services
docker-compose up -d

# Check service status
docker-compose ps

# View logs
docker-compose logs -f

# Check specific service
docker-compose logs stf-server
```

### Step 3: Access STF Web Interface

Once services are running, access STF at:
- **Web UI**: http://localhost:7100
- **RethinkDB Admin**: http://localhost:8080
- **Storage API**: http://localhost:7003

### Step 4: Verify Services

```powershell
# All services should be running
docker-compose ps

# Should show:
# stf-rethinkdb     - Healthy
# stf-multiplexer   - Running
# stf-server        - Running
# stf-auth          - Running
# stf-farmer        - Running
# stf-storage       - Running
```

## Services Overview

| Service | Port | Purpose |
|---------|------|---------|
| rethinkdb | 28015, 29015, 8080 | Database & Admin UI |
| stf-multiplexer | 7001 | Device connection management |
| stf-server | 7100 | Main web interface |
| stf-auth | 7002 | Authentication |
| stf-farmer | N/A | Device management |
| stf-storage | 7003 | File storage |

## Troubleshooting

### Issue: "Cannot connect to Docker daemon"
**Solution**: Start Docker Desktop and wait for it to fully initialize

### Issue: Image pull failures
**Solution**: Check internet connection and Docker login
```powershell
docker login
docker-compose pull
```

### Issue: Port already in use
**Solution**: Stop conflicting services
```powershell
# Stop a specific port
netstat -ano | findstr :7100
taskkill /PID <PID> /F

# Or use a different port in docker-compose.yml
```

### Issue: Services not starting
**Solution**: Check logs
```powershell
docker-compose logs stf-server
docker-compose logs rethinkdb
```

## Commands Reference

```powershell
# Start services
docker-compose up -d

# Stop services
docker-compose down

# Remove volumes (careful! deletes data)
docker-compose down -v

# View logs
docker-compose logs -f [service-name]

# Execute command in container
docker-compose exec stf-server npm list -g stf

# Rebuild services
docker-compose up -d --build

# Check service health
docker-compose ps
```

## Default Credentials

**Administrator Account:**
- Name: administrator
- Email: administrator@fakedomain.com

**Root Group:** Common

You can change these by editing the environment variables in `stf-server` section of docker-compose.yml.

## Performance Tips

1. **Allocate More Resources to Docker**:
   - Docker Desktop Settings → Resources
   - Increase CPUs and Memory as needed

2. **Use Named Volumes**:
   - Volumes are already configured for data persistence
   - Data survives `docker-compose down`

3. **Monitor Container Stats**:
   ```powershell
   docker stats
   ```

## Next Steps

1. Run the setup: `docker-compose up -d`
2. Wait for services to initialize (30-60 seconds)
3. Access http://localhost:7100
4. Connect Android devices via ADB
5. Start testing!

---

For more information, see:
- Official STF: https://github.com/openstf/stf
- Docker Compose: https://docs.docker.com/compose/
- RethinkDB: https://rethinkdb.com/docs/

