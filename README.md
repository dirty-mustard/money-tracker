# Money Tracker

## Create network

    docker network create money-tracker-network

## Docker containers (required)

    docker run -d --name postgres -e POSTGRES_PASSWORD=moneytracker -e POSTGRES_USER=moneytracker -e POSTGRES_DB=moneytracker -p 5432:5432 --network money-tracker-network postgres
    docker run -d --name elasticsearch -p 9200:9200 -p 9300:9300 --network money-tracker-network elasticsearch:2

### Installing Elasticsearch "head" plugin (optional)

    docker exec elasticsearch bin/plugin install mobz/elasticsearch-head

## Running Money Tracker

    mvn clean package && docker run -p 8080:8080 --network money-tracker-network money-tracker/money-tracker



    minikube docker-env
    
    eval $(minikube docker-env)
    
    export DOCKER_REGISTRY='192.168.99.100:2376' && mvn clean package



kubectl create -f deployment.yaml
kubectl delete deployment money-tracker
