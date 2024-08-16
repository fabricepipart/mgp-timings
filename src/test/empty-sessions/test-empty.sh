#!/usr/bin/env bash

api=${1:-"http://localhost:8080"}

while IFS= read -r line; do
    words=( $line )
    old_http_return_code=${words[0]}
    api_entrypoint=${words[1]}
    #echo "When we called $api_entrypoint we received $old_http_return_code"
    http_return=$(curl -s -o /dev/null -w "%{http_code}\n" "${api}/${api_entrypoint}")

#    echo "When calling ${api}/${api_entrypoint} , we received $http_return vs $old_http_return_code"
    if [[ "$http_return" != "200" ]]
    then
      echo "When calling ${api}/${api_entrypoint} , we received $http_return vs $old_http_return_code"
    fi

done < inputs.txt