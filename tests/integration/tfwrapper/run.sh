#!/bin/bash

cd /facets-iac/tests/integration/tfwrapper/features
behave --junit --junit-directory=/facets-iac/tests/integration/tfwrapper/report
junit2html /facets-iac/tests/integration/tfwrapper/report/*.xml