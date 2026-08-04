@echo off
setlocal
set DIR=%~dp0

set JAVA_EXE=
if defined JAVA_HOME if exist "%JAVA_HOME%\bin\java.exe" (
    set JAVA_EXE=%JAVA_HOME%\bin\java.exe
)

if not defined JAVA_EXE (
    where java.exe >nul 2>nul
    if not errorlevel 1 (
        for /f "delims=" %%I in ('where java.exe 2^>nul') do (
            set JAVA_EXE=%%~fI
            goto :java_found
        )
    )
)

:java_found

if not defined JAVA_EXE (
    echo Java was not found. Install JDK 17 or set JAVA_HOME.
    exit /b 1
)

if exist "%DIR%gradle\wrapper\gradle-wrapper.jar" (
    "%JAVA_EXE%" -classpath "%DIR%gradle\wrapper\gradle-wrapper.jar" org.gradle.wrapper.GradleWrapperMain %*
) else (
    gradle %*
)
