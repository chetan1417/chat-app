#!/bin/bash
# Start X virtual framebuffer
export DISPLAY=:0
Xvfb :0 -screen 0 1024x768x16 &
sleep 2

# Start the VNC server with password
x11vnc -forever -display $DISPLAY -passwd chatpass -nopw -noxdamage -noxfixes -noxrecord -nowf -nowcr -noscr -ncache 10 -ncache_cr &

# Launch the Java server application
java -jar /opt/chat/ServerGUI.jar
