#!/bin/bash
gcc "$1" -o "$3"
if test -f "$3"; then
  timeout 2s ./"$3" < "$2" > "$4"
fi