#!/bin/bash

echo "Running Gulp"
gulp serve &

echo "Running Maven"
mvn exec:java