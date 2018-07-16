[![Build Status](https://travis-ci.org/blahami2/db-viewer.svg?branch=master)](https://travis-ci.org/blahami2/db-viewer)
[![Quality Gate](https://sonarcloud.io/api/project_badges/measure?project=cz.blahami2%3Adbviewer&metric=alert_status)](https://sonarcloud.io/dashboard/index/cz.blahami2:dbviewer)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=cz.blahami2%3Adbviewer&metric=coverage)](https://sonarcloud.io/component_measures?id=cz.blahami2%3Adbviewer&metric=Coverage)



# DB viewer

## Description
- REST API capable of storing connection details and basic database analysis (currently only supports _PostgreSQL_)

### Check out the API
- available at [https://dbviewer.docs.apiary.io/#](https://dbviewer.docs.apiary.io/#)

### Try it out
- available at [https://db-viewer.herokuapp.com](https://db-viewer.herokuapp.com/)

## Run

### Prerequisites
- running _PostgreSQL_ database
- default properties for _dev_ profile:
    ```properties
    spring.datasource.url=jdbc:postgresql://localhost:5432/dbviewer
    spring.datasource.username=postgres
    spring.datasource.password=postgres
    spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
    ```

### Execution
- run locally using the following:
    ```bash
    mvn spring-boot:run '-Dspring-boot.run.profiles=dev'
    ```
    
## Contribute

### Build

#### Prerequisites
- docker
- maven
- jdk

TODO

## More
- see more at local [wiki](https://github.com/blahami2/db-viewer/wiki)
 