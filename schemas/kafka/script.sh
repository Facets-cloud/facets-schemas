#!/bin/bash 


fileName="$1"
md="$3"
newFile="$2"

json-dereference -s $fileName -o tmp.schema.json
cat tmp.schema.json | json-schema-resolve-allof > $newFile
~/go/bin/json-schema-docs -schema $newFile > $md
rm tmp.schema.json