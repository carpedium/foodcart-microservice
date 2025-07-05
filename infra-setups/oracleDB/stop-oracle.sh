#!/bin/bash

echo "Stopping Oracle DB deployment..."

kubectl delete -f oracle-db.yaml

echo "Waiting a bit for resources to terminate..."
sleep 10

echo "Removing PVC and PV finalizers if stuck..."

kubectl patch pvc oracle-pvc -p '{"metadata":{"finalizers":null}}' --type=merge
kubectl patch pv oracle-pv -p '{"metadata":{"finalizers":null}}' --type=merge

echo "Cleanup complete."
kubectl get pods -l app=oracle-db
kubectl get pvc oracle-pvc
kubectl get pv oracle-pv
