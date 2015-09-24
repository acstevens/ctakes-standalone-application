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
git clone https://github.com/acstevens/ctakes-standalone-application
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
Command line options:
    * --help: displays the help text
    * --inputfile file: file containing text to annotate
    * --text text: text to annotate
    * --outputformat format: format is either "sentence" or "text".  Sentence will display the results along side of the original sentence(s).  Text will display the results one on each line.  	
