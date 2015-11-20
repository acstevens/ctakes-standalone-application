#!/bin/sh
java -Dctakes.umlsuser='<username>' -Dctakes.umlspw='<password>' -cp ".:./*:lib/*" com.sentimetrix.ctakes.application.CTakesApp "$@"
