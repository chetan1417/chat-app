# Use OpenJDK 23 as the base image
FROM eclipse-temurin:23-jdk

# Install X11 and VNC dependencies
RUN apt-get update && apt-get install -y \
    x11vnc \
    xvfb \
    dos2unix \
    && rm -rf /var/lib/apt/lists/*

# Create app directory
WORKDIR /opt/chat

# Copy the Java source files, class files, and data files
COPY chat/ /opt/chat/chat/
COPY out/chat/ /opt/chat/out/chat/
COPY manifest.txt /opt/chat/
COPY client_manifest.txt /opt/chat/

# Create JAR files with the correct structure
RUN mkdir -p /opt/chat/build/chat && \
    cp chat/*.class /opt/chat/build/chat/ && \
    cp manifest.txt /opt/chat/build/ && \
    cp client_manifest.txt /opt/chat/build/ && \
    cd /opt/chat/build && \
    jar cfm ../ServerGUI.jar manifest.txt chat && \
    jar cfm ../ClientGUI.jar client_manifest.txt chat

# Copy and make the start scripts executable
COPY startapp_server.sh /opt/chat/
COPY startapp_client.sh /opt/chat/
RUN dos2unix /opt/chat/startapp_server.sh /opt/chat/startapp_client.sh && \
    chmod +x /opt/chat/startapp_server.sh /opt/chat/startapp_client.sh

# Set VNC password
RUN mkdir -p /root/.vnc && \
    x11vnc -storepasswd chatpass /root/.vnc/passwd

# Expose VNC port
EXPOSE 5900

# Set the default command to start the server
CMD ["/bin/bash", "/opt/chat/startapp_server.sh"] 