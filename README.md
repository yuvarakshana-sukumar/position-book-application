# Position Book Application

The Position Book Application is designed to manage positions for financial assets and handle trade events such as buying, selling, and canceling trades. It allows users to record and track positions in different financial instruments (e.g., stocks, bonds) and execute trade events that modify these positions.

## Features

- **Add Trade Event**: Record a trade event (e.g., buy, sell) for a given account and security.
- **Cancel Trade Event**: Cancel an existing trade event, reducing the position's quantity or removing it entirely.
- **Position Management**: Track positions for each account and security.
- **Error Handling**: Handles invalid trade events (e.g., canceling an event with mismatched account/security).
- **Map-based Storage**: Uses an in-memory `Map` to store trade events and positions.

## Prerequisites

- Java 8 or higher
- Maven (or Gradle, depending on your build tool)
- IDE (e.g., IntelliJ IDEA, Eclipse) or command line tools for Java development

## Getting Started

Follow these steps to get a local copy of the project running:

### 1. Clone the repository
```bash
git clone https://github.com/yourusername/position-book-application.git

```
### 2. Build the project

mvn clean install

### 3. Run the application

mvn exec:java

## Input Format (JSON)

The application expects trade events to be provided in JSON format. Each trade event can be either a regular trade event (e.g., buy) or a cancel event. Below is the structure of a sample input event in JSON format:

{
  "id": 1,
  "type": "BUY",
  "account": "ACC1",
  "security": "SEC1",
  "quantity": 100
}

id: Unique identifier for the trade event.
type: Type of the trade event, either "BUY", "SELL", or "CANCEL".
account: The account associated with the trade.
security: The security being traded (e.g., stock symbol).
quantity: The number of units involved in the trade event.

## Project Structure

src/main/java: Contains the core application logic, including services, models, repository and event handling.
src/test/java: Contains unit tests for the application.
pom.xml or build.gradle: The build configuration for Maven or Gradle.