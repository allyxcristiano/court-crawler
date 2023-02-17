# Court Crawler

Attempting to conquer the world by crawling court websites

## Summary

- What is this project?
- What you'll need
- Running
- Usage
- Stopping execution
- Running the test suite

## What is this project?

This is a technical challenge where the objective is to crawl Brazilian court websites. Currently, this service is capable
of reading first and second instance pages of cases from [TJ AL](http://www.tjal.jus.br/)
and [MS](https://www.tjms.jus.br/).

The term "crawl" in this context refers to providing a service that can obtain information on the first and second
instance (if they exist) of a case in these courts. This information will be saved in a specific document structure to
facilitate the handling of this data and allow for better access through other tools.

## What you'll need

* [Docker compose / docker](https://github.com/Yelp/docker-compose/blob/master/docs/install.md)

## Running

After installing docker-compose and cloning this repository, run the command docker-compose up -d to bring up an
instance of the API with all its dependencies configured.

The first execution takes a while because all dependencies will be downloaded and configured. After following this step,
the service will be available at http://localhost:7000.

Bonus: To follow all logs, run the command docker-compose logs -f -t.

## Usage

1. The Swagger is available at  `http://localhost:7000/docs`.

2. To help with testing, there are some demos in the project, you can use `court-of-al-demo.txt`
and `court-of-ms-demo.txt` to make requests for various scenarios and corner cases to test the crawler.

3. To help with the analysis of crawled cases and the document data structure without having to open a MongoDB manager
or use the command line to analyze the crawled content, a GET without pagination was created. You can access it at
`http://localhost:7000/processes`.

## Stopping execution

To stop the execution, run the command `docker-compose stop`.

## Running the test suite

1. Install [gradle](https://gradle.org/install/)

2. Delete the contents of the `/build` folder in the project root directory.

3. Start the necessary containers by running the command `docker start hub-court-crawler chrome-court-crawler`.

4. Run the command `gradle test` to run the test suite.

5. After running the tests, stop the execution of the containers by running the command
`docker stop hub-court-crawler chrome-court-crawler`.
