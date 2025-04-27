@echo off
cd /d D:\users\Docker\docker_final_project\docker_final_project

echo What do you want to do?
echo 1. Stop ALL containers (server + all clients)
echo 2. Stop specific container only
set /p choice=Enter choice [1/2]:

if "%choice%"=="1" (
    docker-compose stop
    echo All containers stopped!
) else if "%choice%"=="2" (
    echo.
    echo Currently running containers:
    docker ps --format "table {{.Names}}\t{{.Status}}"
    echo.
    echo Enter the exact container name you want to stop:
    set /p container_name=
    docker stop %container_name%
    echo Container %container_name% stopped!
) else (
    echo Invalid choice. Exiting.
)

pause
