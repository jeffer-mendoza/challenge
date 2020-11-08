echo "Creating App Engine app"
gcloud app create --region "us-central"

echo "Exporting GCLOUD_PROJECT"
export GCLOUD_PROJECT=$DEVSHELL_PROJECT_ID

echo "Installing dependencies"
mvn clean install -P pro

echo "Copying backend output into backend folder"
cp ./target/challenge-1.1.jar ./backend/

echo "Creating Container Engine cluster"
gcloud container clusters create challenge-cluster --zone us-central1-a --scopes cloud-platform
gcloud container clusters get-credentials challenge-cluster --zone us-central1-a

echo "Building Containers"
gcloud builds submit -t gcr.io/$DEVSHELL_PROJECT_ID/challenge-backend ./backend

echo "Deploying to Container Engine"
sed -i -e "s/\[GCLOUD_PROJECT\]/$DEVSHELL_PROJECT_ID/g" ./backend-deployment.yaml
kubectl create -f ./backend-deployment.yaml
kubectl create -f ./service.yaml


echo "Project ID: $DEVSHELL_PROJECT_ID"

# reiniciar process
# gcloud container clusters delete challenge-cluster --zone us-central1-a
# kubectl delete -f ./backend-deployment.yaml
# kubectl delete -f ./service.yaml