# QueGG
The project creates QA system given a lemon lexica or Csv file (contains information Syntactic Frame such NounPPframe, TransitiveFrame, etc. )

## Compile And Run
<p>The source code can be compiled and run using <em>Java 11</em> and <em>Maven</em>.</p>

```shell script
git clone https://github.com/fazleh2010/question-grammar-generator.git -b extension
```
build the jar file
```shell script
mvn clean install
mvn clean package
```
Run the system:
- language: `EN` or `DE`
- input_directory: The directory that contains lemon (the turtle files) or csv files  that will be processed by QueGG. If csv file then the program will first create lemon and then generate grammar entry files. If lemon then it will generate grammar entry files. 
- output_director: The output directory for the json grammar entry files that are produced by QueGG
- number_of_entities: The number of entities in binding list. For example, what is the birthplace of (X|Person) (X is entity such as res:Donald_Trump, res:Angela_Merkel, etc.). If the parameter is 10 then the maximum number of binding list is 10.
- input_type:  The input file indicator. `csv` or  `ttl` . If  `csv` then the lexical entires are given in the form of rows in csv file. If  `ttl` then the lexical entries are given in lemon format.                                         

````shell script
java -jar <jar file> <language> <input_directory> <output_director> <number_of_entities> <input_type>
java -jar target/QuestionGrammarGenerator.jar EN lexicon/en output 10 csv                                                                 
````
Click [here](http://localhost:8089/quegg/) to go to my Quora profile. 
go to the (http://localhost:8089/quegg/)

## Functionality And Examples

### Input File Definition
QueGG reads turtle files that contain lexical entries.

The lexical entries are defined using the Lexicon Model for Ontologies [Lemon](https://lemon-model.net/) and the data category ontology [LexInfo](https://lexinfo.net/).

A valid csv file can be seen from [here] (https://github.com/fazleh2010/question-grammar-generator/blob/extension/examples/input/lexicon/en/nouns/NounPPFrame%20-%20QALD%20Train%20-%20not%20solved.csv)

A valid tt file can be seen from  [here] (https://github.com/fazleh2010/question-grammar-generator/blob/extension/examples/input/lexicon/en/nouns/lexicon-birthPlace_of.ttl)

