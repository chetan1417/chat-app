#!/bin/bash
# Start X virtual framebuffer
export DISPLAY=:0
Xvfb :0 -screen 0 1024x768x16 &

# Start the VNC server
x11vnc -forever -usepw -display $DISPLAY &

# Launch the Java server application
java -jar /opt/chat/ServerGUI.jar
