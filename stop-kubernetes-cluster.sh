#!/bin/bash

kubectl delete --namespace=money-tracker -f namespace.yaml
kubectl delete --namespace=money-tracker -f secret.yaml
kubectl delete --namespace=money-tracker -f money-tracker-database-deployment.yaml
kubectl delete --namespace=money-tracker -f database-service.yaml
kubectl delete --namespace=money-tracker -f money-tracker-index-server-deployment.yaml
kubectl delete --namespace=money-tracker -f index-server-service.yaml

