## Deployment

```bash 
# clone project 
git clone https://github.com/jeffer-mendoza/-challenge.git


# prepare enviroment
## Creates an App Engine application.
## Exports environment variable GCLOUD_PROJECT.
## Runs mvn clean install
## Prints out the Project ID.
. prepare_environment.sh

# create cluster
gcloud container clusters get-credentials challenge-cluster --zone us-central1-a --project $GCLOUD_PROJECT

# list pods in cluster
kubectl get pods

# Build the backend Docker image
gcloud builds submit -t gcr.io/$GCLOUD_PROJECT/challenge-backend ./

# Open the backend-deployment.yaml file and replace the placeholders
vim backend-deployment.yaml

# execute backend
kubectl create -f ./backend-deployment.yaml

# execute service
kubectl create -f ./frontend-service.yaml

# check
click Navigation menu > Kubernetes Engine > Workloads 
```
