#!/bin/bash
gcc $1 -o $3
./$3 < $2
rm $3
