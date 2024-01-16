#!/bin/bash
set -v
## This script is intended to be run from "nginx-ingress-tests" directory

# Check if gomarkdoc is installed
if ! command -v gomarkdoc &> /dev/null
then
    echo "gomarkdoc could not be found, installing..."
    go install github.com/princjef/gomarkdoc/cmd/gomarkdoc@latest
fi

# Change to the setup directory
cd ./setup

# Run gomarkdoc and store its output
output=$(gomarkdoc . | tail -n +8)

# Increase each markdown heading by one #
output=$(echo "$output" | sed 's/^#/##/')

# Change back to the parent directory
cd ../

# Insert the generated godoc between the placeholders in README.md
awk -v var="$output" '/<!-- GODOC BEGIN -->/{print;print var;print "\n";f=1} /<!-- GODOC END -->/{f=0} !f' README.md > tmp && mv tmp README.md

# run prettier
echo "Running prettier now..."
npx --yes prettier --prose-wrap=always --print-width=80 --write README.md
