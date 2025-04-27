FROM openjdk:11-jdk-slim

# Install required GUI and X11 libraries
RUN apt-get update && apt-get install -y \
    libxext6 \
    libxrender1 \
    libxtst6 \
    libxi6 \
    libx11-6 \
    x11-utils

# Set working directory inside container
WORKDIR /app

# Copy your Java files
COPY chat /app/chat

# Compile Java source files
RUN javac chat/*.java

# Create chat folder if missing
RUN mkdir -p /app/chat

# Default: run server
CMD ["java", "chat.Server"]
