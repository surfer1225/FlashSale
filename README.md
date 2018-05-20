# FlashSale API

FlashSale API, including 3 APIs

GET /wallet/{id} to retrieve wallet information
POST /products/{id}/purchase to purchase products under flash sale on the website
GET /sales/current?country=SG to view current flash sale.

# Run the Application
once you have checkout your project in your IDE
simply run "sbt start" to run the application

# Test the application
simply run "sbt test" to run all unit tests and integration tests

to run the tests of a single file:
simple right click the class name and click "Run ...."

IntegrationSpec contains all the tests for integration tests



Due to time constraints, stress testing has not been performed
There are ways to improve the application
We should have a refreshable cache in place serving as the in-memory data store,
which from time to time retrieves data from database and cache the result

Update operations are done to the refreshable cache as well in a synchronized way for 1 user wallet to save DB trips

ExchangeRate service should rely on third party API

Front-End codes are in "views" package, which right now is only a simple message


# Versioning
The application uses the following convention for versioning: **_major.minor.hotfix_**.

The following details which the version bump is appliable
- major: any non-compatible changes are added
- minor: any backward-compatible changes are added
- hotfix: all bug and hot fixes

# Scala formatting commands
sbt scalafmt
