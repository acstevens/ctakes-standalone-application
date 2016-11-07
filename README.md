# cTakes Standalone Java Application
This is a maven project that builds an application integrated with cTAKES.  One can use it to run text through cTAKES on the command line.

## Software
* cTakes
* Maven
* Java
* git & git-lfs

## How to Run
* Ensure Java, Maven, Git and Git Large File Store is installed
* Git clone project
```
git clone https://github.com/acstevens/ctakes-standalone-application
```
* Build code
```
cd ctakes-standalone-application
mvn -Dmaven.test.skip=true clean package assembly:single
```

* Run excutable
```
cd target/ctakes-standalone-application-*-bin/ctakes-standalone-application-*/
```
For Unix
```
sh startup.sh  --text "diabetes"
```
For Windows
```
startup.bat  --text "diabetes"
```

Command line options:
 * --help: displays the help text
 * --inputfile file: file containing text to annotate
 * --outputfile file: file to write the results to
 * --text text: text to annotate
 * --pipeline ["default", "fast"]
 * --outputformat ["sentence", "text", "json"]:  'sentence' will display the results along side of the original sentence(s).  'text' will display the results for one entity on a single line.  'json' will display the results in a json array.   	

If using the "default" pipeline you need to supply a username and password to use the UMLS dictionary.
Modify the start script with those values.
