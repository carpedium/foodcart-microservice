#!/bin/bash
set -e

echo "Stopping Keycloak + PostgreSQL..."

kubectl delete -f keycloak-service.yaml --ignore-not-found
kubectl delete -f keycloak-deployment.yaml --ignore-not-found

kubectl delete -f postgres-service.yaml --ignore-not-found
kubectl delete -f postgres-deployment.yaml --ignore-not-found
kubectl delete -f postgres-pvc.yaml --ignore-not-found

echo "Resources deleted."
