## Deploy

```bash 
# clone dto project
git clone https://github.com/jeffer-mendoza/challenge.dto
cd challenge.dto
mvn clean install 
cd ..

# clone project 
git clone https://github.com/jeffer-mendoza/challenge.git
cd challenge

# execute prepare-enviroment script
## Creates an App Engine application.
## Exports environment variable GCLOUD_PROJECT.
## Runs mvn clean install
## Copy jar to Docker workspace
## Create Container Engine cluster
## Building Containers
## Deploy to Container Engine
## Prints out the Project ID.
. prepare_environment.sh

# check
click Navigation menu > Kubernetes Engine > Workloads 
```
