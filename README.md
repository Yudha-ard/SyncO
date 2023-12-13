## SyncO - Cloud Computing

  A user-friendly health app designed to bridge healthcare gaps, particularly in remote areas, by leveraging smart technology. The app streamlines health management by providing easy access to medical guidance and predicting potential health issues. It aims to address disparities in healthcare access, offering support where traditional services are scarce. Through intuitive features, it empowers users to comprehend and anticipate their health concerns, promoting proactive wellness strategies. The ultimate goal is to democratize healthcare guidance, ensuring accessibility for all.

## how to deploy
#### build an image with Dockerfile
gcloud builds submit --tag gcr.io/<PROJECT_ID>/<IMAGE_NAME> .
#### deploy to cloud run
gcloud beta run deploy <SERVICE_NAME> --image gcr.io/<PROJECT_ID>/<IMAGE_NAME> --region asia-southeast2 --platform managed --allow-unauthenticated --quiet