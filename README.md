# cTakes Standalone Java Application
This is a maven project that builds an application integrated with cTAKES.  One can use it to run text through cTAKES on the command line.

## Software
* cTakes
* Maven
* Java

## How to Run
* Ensure Java, Maven and Git is installed
* Git clone project
```
git clone git@github.com:ReactiveX/RxJava.git
```
* Build code
```
cd ctakes-standalone-application
mvn clean package assembly:single
```

* Run excutable
```
cd target/ctakes-standalone-application-*-bin/ctakes-standalone-application-*/
sh startup.sh  --text "diabetes"
```
