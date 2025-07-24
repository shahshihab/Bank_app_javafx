# JavaFX Bank App

This is a JavaFX bank application set up with Maven and MSSQL support.

## Features

- JavaFX UI (see `src/main/resources/UI.fxml`)
- MSSQL database connectivity
- Maven build system

## How to Run

1. Configure your MSSQL connection in the code.
2. Use Maven to build and run the app:

   ```bash
   mvn clean javafx:run
   ```

## Requirements

- JDK 24
- JavaFX 24 SDK
- Maven
- MSSQL Server

## MSSQL JDBC Setup

The app uses the official Microsoft JDBC driver for SQL Server. See `pom.xml` for details.
