@echo off
cd /d D:\users\Docker\docker_final_project\docker_final_project
echo Enter the number of clients you want to launch:
set /p NUM_CLIENTS=
docker-compose up -d --build --scale client=%NUM_CLIENTS%
pause
