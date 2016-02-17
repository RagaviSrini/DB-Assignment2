#!/bin/bash

javac university_program.java

if [ $? -eq 0 ]
then
	echo "Compilation successful"
else
	echo "Compilation failed"
fi
