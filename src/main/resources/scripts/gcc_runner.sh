#!/bin/bash
gcc "$1" -o "$3"
if [[ $? -ne 0 ]]; then
       exit 125
fi
if test -f "$3"; then
  timeout 2s ./"$3" < "$2" > "$3".txt
  rm "$3"
fi