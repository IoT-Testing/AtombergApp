# Dockerfile for STF (Smartphone Test Farm) based on official OpenSTF image

FROM openstf/stf:latest

# Switch to root user to install packages
USER root

# Set non-interactive mode to avoid prompts during package installation
ENV DEBIAN_FRONTEND=noninteractive

# Install additional necessary packages (ADB is already in the base image, but ensure ADB is linked)
RUN apt-get update && apt-get install -y \
    curl \
    wget \
    unzip \
    build-essential \
    pkg-config \
    yasm \
    libzmq3-dev \
    protobuf-compiler \
    graphicsmagick \
    python3 \
    python3-dev \
    && rm -rf /var/lib/apt/lists/*

# ADB is already installed in the base image, ensure it's in PATH
RUN ln -sf /opt/platform-tools/adb /usr/local/bin/adb || true

# Create a directory for STF data
RUN mkdir -p /stf

# Expose necessary ports
# STF web interface
EXPOSE 7100

# Set working directory
WORKDIR /stf

# Switch back to the default user if needed, but for CMD, root might be fine
# USER stf

# Start RethinkDB first, then the STF server
CMD ["sh", "-c", "stf serve --port 7100 --rethinkdb-host rethinkdb --rethinkdb-port 28015"]
