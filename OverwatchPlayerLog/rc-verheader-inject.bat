@echo off
set /p var= <%3
powershell -Command "(gc %1) -replace '%2', '%var%' | Set-Content %1"
echo Modified resource file with C++ version header.