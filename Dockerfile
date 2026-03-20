doc# Dockerfile for STF (Smartphone Test Farm) on Windows
# Based on adapting the Linux requirements to Windows

FROM mcr.microsoft.com/windows/servercore:ltsc2022

# Install Chocolatey package manager
RUN powershell -Command "Set-ExecutionPolicy Bypass -Scope Process -Force; [System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072; iex ((New-Object System.Net.WebClient).DownloadString('https://chocolatey.org/install.ps1'))"

# Install Node.js 8.x (as required by STF)
RUN choco install nodejs --version 8.17.0 -y

# Install RethinkDB
RUN powershell -Command "Invoke-WebRequest -Uri 'https://download.rethinkdb.com/windows/rethinkdb-2.4.2.zip' -OutFile 'C:\rethinkdb.zip'; Expand-Archive -Path 'C:\rethinkdb.zip' -DestinationPath 'C:\'; Rename-Item 'C:\rethinkdb-2.4.2' 'C:\rethinkdb'; setx PATH '%PATH%;C:\rethinkdb'"

# Install ADB (Android Debug Bridge)
RUN powershell -Command "Invoke-WebRequest -Uri 'https://dl.google.com/android/repository/platform-tools_r34.0.1-windows.zip' -OutFile 'C:\platform-tools.zip'; Expand-Archive -Path 'C:\platform-tools.zip' -DestinationPath 'C:\'; Rename-Item 'C:\platform-tools' 'C:\adb'; setx PATH '%PATH%;C:\adb'"

# Install GraphicsMagick
RUN choco install graphicsmagick -y

# Install ZeroMQ libraries (approximate, using pre-built binaries)
RUN powershell -Command "Invoke-WebRequest -Uri 'https://github.com/zeromq/libzmq/releases/download/v4.3.4/zeromq-4.3.4.zip' -OutFile 'C:\zeromq.zip'; Expand-Archive -Path 'C:\zeromq.zip' -DestinationPath 'C:\'; Rename-Item 'C:\zeromq-4.3.4' 'C:\zeromq'; setx PATH '%PATH%;C:\zeromq\bin'"

# Install Protocol Buffers
RUN powershell -Command "Invoke-WebRequest -Uri 'https://github.com/protocolbuffers/protobuf/releases/download/v3.20.1/protoc-3.20.1-win64.zip' -OutFile 'C:\protoc.zip'; Expand-Archive -Path 'C:\protoc.zip' -DestinationPath 'C:\protoc'; setx PATH '%PATH%;C:\protoc\bin'"

# Install yasm
RUN powershell -Command "Invoke-WebRequest -Uri 'https://www.tortall.net/projects/yasm/releases/yasm-1.3.0-win64.exe' -OutFile 'C:\yasm.exe'; New-Item -ItemType Directory -Path 'C:\yasm'; Move-Item 'C:\yasm.exe' 'C:\yasm\yasm.exe'; setx PATH '%PATH%;C:\yasm'"

# Install pkg-config via MSYS2 (approximate)
RUN choco install msys2 -y
RUN C:\tools\msys64\usr\bin\bash.exe -c "pacman -S --noconfirm pkg-config"

# Install STF globally
RUN npm install -g stf

# Create a directory for STF data
RUN New-Item -ItemType Directory -Path 'C:\stf'

# Expose necessary ports
# STF web interface
EXPOSE 7100
# RethinkDB ports
EXPOSE 28015 29015 8080

# Set working directory
WORKDIR C:\stf

# Start RethinkDB and then STF
CMD ["powershell", "-Command", "Start-Process 'C:\\rethinkdb\\rethinkdb.exe' -ArgumentList '--bind all' -NoNewWindow; Start-Sleep -Seconds 5; stf local --public-ip 0.0.0.0"]
