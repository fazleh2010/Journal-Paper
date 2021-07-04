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
- language: `EN` (English)
- input_directory: The directory that contains lemon (the turtle files) or csv files  that will be processed by QueGG. If csv file then the program will first create lemon and then generate grammar entry files. If lemon then it will generate grammar entry files. An example of input file can be found [here] (https://github.com/fazleh2010/question-grammar-generator/blob/extension/examples/input/lexicon/en/nouns/NounPPFrame%20-%20QALD%20Train%20-%20not%20solved.csv)
- output_director: The output directory for the json grammar entry files that are produced by QueGG
- number_of_entities: The number of entities in binding list. For example, what is the birthplace of (X|Person) (X is entity such as res:Donald_Trump, res:Angela_Merkel, etc.). If the parameter is 10 then the maximum number of binding list is 10.
- input_type:  The input file indicator. `csv` or  `ttl`.If `csv` then the lexical entires are given in the form of rows in csv. If  `ttl` then the lexical entries are given in lemon format.                                         
````shell script
java -jar <jar file> <language> <input_directory> <output_director> <number_of_entities> <input_type>
java -jar target/QuestionGrammarGenerator.jar EN lexicon/en output 10 csv                                                                 
````  
go to the (http://localhost:8089/quegg/)

### Input examples
- An example of input file (.csv) can be found [here] (https://github.com/fazleh2010/question-grammar-generator/blob/extension/examples/input/lexicon/en/nouns/NounPPFrame%20-%20QALD%20Train%20-%20not%20solved.csv)     
[<img src="https://github.com/fazleh2010/term-a-llod-demo/blob/master/term-a-llod.png" width="50%">](https://www.youtube.com/watch?v=PInCQvnpYh0) 
