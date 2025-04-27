# Start with Java 11 slim base
FROM openjdk:11-jdk-slim

# Install libraries needed for Java GUI (AWT/Swing) inside Linux
RUN apt-get update && apt-get install -y \
    libxext6 \
    libxrender1 \
    libxtst6 \
    libxi6 \
    libx11-6 \
    x11-utils

# Set working directory inside container
WORKDIR /app

# Copy Java source files to container
COPY chat /app/chat

# Compile Java source files
RUN javac chat/*.java

# Create chat folder if missing
RUN mkdir -p /app/chat

# By default, start the server
CMD ["java", "chat.Server"]
