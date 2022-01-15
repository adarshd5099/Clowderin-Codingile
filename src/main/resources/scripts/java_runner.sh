#!/bin/bash
javac "$1"
timeout 2s java "$1" < "$2" > "$4"
