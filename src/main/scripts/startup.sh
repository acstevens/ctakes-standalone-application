#!/bin/sh
java -Dctakes.umlsuser='<username>' -Dctakes.umlspw='<password>' -Xmx512m -cp ".:./*:lib/*" com.sentimetrix.ctakes.application.CTakesApp "$@"
