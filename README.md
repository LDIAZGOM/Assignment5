# Assignment 5 – BarnesAndNoble Project

[![SE333_CI](https://github.com/LDIAZGOM/SE333-Assignment5_Code/actions/workflows/SE333_CI.yml/badge.svg)](https://github.com/LDIAZGOM/Assignment5/actions)

## Project Overview

This is my Assignment 5 project for SE333.

In this project, I did:

- Wrote **specification-based tests** and **structural-based tests** for the BarnesAndNoble project.
- Set up an **automated workflow** using GitHub Actions so tests run automatically.
- Ran **static analysis** using Checkstyle to check code style.
- Ran **code coverage** using JaCoCo to see how much of my code is tested.
- Wrote **unit and integration tests** for the Amazon package.

## Workflow

The CI workflow runs automatically every time I push changes to the `main` branch. It does these steps:

1. **Static Analysis (Checkstyle)**
   - Checks the code for style and formatting issues.
   - Uploads a report called `checkstyle-report`.

2. **Unit Tests (JUnit + Mockito)**
   - Tests individual classes using mocks to simulate behavior.

3. **Integration Tests**
   - Tests how the Amazon package works with the `Database` and `ShoppingCartAdaptor`.

4. **Code Coverage (JaCoCo)**
   - Shows how much of the code is covered by tests.
   - Uploads a report called `jacoco-report`.

## Artifacts

- `checkstyle-report` – Shows Checkstyle results in XML format.
- `jacoco-report` – Shows code coverage results in XML and HTML format.

## Notes

- Some Checkstyle warnings, like missing Javadocs or design issues, are ignored for this assignment.
- All the unit and integration tests run successfully.
- The CI workflow on GitHub confirms that tests and reports are generated automatically.