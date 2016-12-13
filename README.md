# Money Tracker

## Requirements to run Money Tracker

    - docker
    - docker-compose (https://docs.docker.com/compose/install/)

## Running Money Tracker API

    mvn clean package && docker-compose run -d -p 8080:8080 mt-backend

### Installing Elasticsearch "head" plugin (optional)

    docker exec moneytracker_elasticsearch_1 bin/plugin install mobz/elasticsearch-head
