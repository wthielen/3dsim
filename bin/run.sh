#!/bin/bash

if [ ! -d sim3d ]; then
    echo "Please compile the project first. Go into the ../src directory and type 'make'";
    exit 1;
fi

java -cp .:/usr/share/java/vecmath.jar sim3d.World
