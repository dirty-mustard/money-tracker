# Money Tracker

## Docker containers (required)

    docker run -d --name postgres -e POSTGRES_PASSWORD=moneytracker -e POSTGRES_USER=moneytracker -e POSTGRES_DB=moneytracker -p 5432:5432 postgres
    docker run -d --name elasticsearch -p 9200:9200 -p 9300:9300 elasticsearch:2

### Installing Elasticsearch "head" plugin (optional)

    docker exec elasticsearch bin/plugin install mobz/elasticsearch-head