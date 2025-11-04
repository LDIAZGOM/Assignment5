# Assignment 5 

[![SE333_CI](https://github.com/LDIAZGOM/SE333-Assignment5_Code/actions/workflows/SE333_CI.yml/badge.svg)](https://github.com/LDIAZGOM/Assignment5/actions)

## Project Overview

This project is part of Assignment 5. It includes:

- Specification-Based Testing and Structural-Based Testing for the BarnesAndNoble project.
- Automated CI workflow using GitHub Actions.
- Static analysis with Checkstyle and code coverage with JaCoCo.
- Unit and Integration tests for Amazon package.

## Workflow

The CI workflow runs automatically on every push to the `main` branch, performing:

1. Static Analysis (Checkstyle)
    - Checkstyle report uploaded as artifact: `checkstyle-report`
2. Unit Tests (JUnit + Mockito)
    - Unit tests for individual classes using mocks.
3. Integration Tests
    - Integration tests for Amazon package interacting with Database and ShoppingCartAdaptor.
4. Code Coverage (JaCoCo)
    - JaCoCo report uploaded as artifact: `jacoco-report`

## Artifacts

- `checkstyle-report` – XML report of static analysis.
- `jacoco-report` – XML and HTML report of test coverage.

## Notes

- Some Checkstyle warnings (missing Javadoc, DesignForExtension) are ignored for the purposes of this assignment.
- All unit and integration tests run successfully.
- CI workflow confirms that testing and reports are generated on GitHub Actions.