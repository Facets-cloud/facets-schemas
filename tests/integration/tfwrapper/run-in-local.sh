docker build --platform linux/amd64 runner-docker-image -t tfwrapper-runtime:1.0
docker run --platform linux/amd64 -v /Users/rohit/facets-iac/:/facets-iac tfwrapper-runtime:1.0 /bin/bash /facets-iac/tests/integration/tfwrapper/run.sh
