#!/bin/bash

FUNCTION_NAME=url-shortner-java
ENTRYPOINT=com.nedonil.urlshort.UrlShortnerServlet
DB_NAME=db-name
DB_ENDPOINT=db-endpoint
IAM_TOKEN=iam-token

yc serverless function version create \
  --function-name="$FUNCTION_NAME" \
  --runtime=java21 \
  --entrypoint="$ENTRYPOINT" \
  --memory=128m \
  --execution-timeout=10s \
  --source-path=./target.zip \
  --environment "DATABASE=$DB_NAME,ENDPOINT=$DB_ENDPOINT,IAM_TOKEN=$IAM_TOKEN"
