#!/bin/bash
set -e

echo "Starting PostgreSQL + Keycloak..."

kubectl apply -f postgres-pvc.yaml
kubectl apply -f postgres-deployment.yaml
kubectl apply -f postgres-service.yaml

# Wait for PostgreSQL pod to be ready (optional, 1st pod)
echo "Waiting for PostgreSQL pod to become ready..."
kubectl wait --for=condition=ready pod -l app=postgres --timeout=120s

kubectl apply -f keycloak-deployment.yaml
kubectl apply -f keycloak-service.yaml

echo -e "\n\n All started: PV"
kubectl get pv
echo -e "\n\nAll started: PVC"
kubectl get pvc
echo -e "\n\nAll started: - - - - - - - - "

echo -e "\n\nAll started: PODS"
kubectl get pods -l app=postgres
kubectl get pods -l app=keycloak
echo -e "\n\nAll started: SVC"
kubectl get svc
