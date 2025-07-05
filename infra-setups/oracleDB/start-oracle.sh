#!/bin/bash

echo "Starting Oracle DB deployment..."

kubectl apply -f oracle-db.yaml

echo "Waiting for Oracle pod to be ready..."

kubectl wait --for=condition=available --timeout=300s deployment/oracle-db

echo "Oracle DB should be up now."
kubectl get pods -l app=oracle-db
kubectl get svc oracle-db
