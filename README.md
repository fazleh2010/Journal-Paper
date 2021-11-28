# QueGG
The project creates QA system given a lemon lexica or Csv file (contains information Syntactic Frame such NounPPframe, TransitiveFrame, etc. )

## Compile And Run
<p>The source code can be compiled and run using <em>Java 11</em> and <em>Maven</em>.</p>

```shell script
git clone https://github.com/fazleh2010/question-grammar-generator.git -b italian
```
build the jar file
```shell script
mvn clean install
mvn clean package
```
Run the system:
- language: `EN` (English), `DE` (German), `IT` (Italian)
- input_directory: The directory that contains lemon (the turtle files) or csv files  that will be processed by QueGG. If csv file then the program will first create lemon and then generate grammar entry files. If lemon then it will generate grammar entry files. 
- output_director: The output directory for the json grammar entry files that are produced by QueGG
- number_of_entities: The number of entities in binding list. If the parameter is 10 then the maximum number of binding list is 10.
- input_type:  The input file indicator. `csv` or  `ttl`.If `csv` then the lexical entires are given in the form of rows in csv. If  `ttl` then the lexical entries are given in lemon format.                                         
````shell script
java -jar <jar file> <language> <input_directory> <output_director> <number_of_entities> <input_type>
java -jar target/QuestionGrammarGenerator.jar DE lexicon/de outputDB 10 csv dataset/dbpedia.json   
java -jar target/QuestionGrammarGenerator.jar DE lexicon/de outputDB 10 qa dataset/dbpedia.json                                                               
````  
Run Evaluation

````shell script
java -jar target/QuestionGrammarGenerator.jar DE qald outputDB dataset/dbpedia.json
````


### Input example
QueGG can take either csv or turtle file as input 
- An example of csv input file (.csv)  (https://github.com/fazleh2010/question-grammar-generator/blob/italian/examples/article_Noun_Frame%20.csv) or
- An example of turtle file (.ttl) (https://github.com/fazleh2010/question-grammar-generator/blob/italian/examples/NounPPFrame-lexicon-birthPlace_of.ttl). The lexical entries are defined using the Lexicon Model for Ontologies [Lemon](https://lemon-model.net/) and the data category ontology [LexInfo](https://lexinfo.net/).


### Output file example
QueGG can generate two types of output file:
- grammar entry (.json file)
- question, sparql, and answer (.csv file)

######  grammar entry (.json file) that looks like this:

```json
{
    "id" : "1",
    "lexicalEntryUri" : "http://localhost:8080#birthPlace_of",
    "language" : "IT",
    "type" : "SENTENCE",
    "bindingType" : "PERSON",
    "returnType" : "PLACE",
    "frameType" : "NPP",
    "sentences" : [ "Qual era il luogo di nascita di ($x | PERSON_NP)?", "Qual è il luogo di nascita di ($x | PERSON_NP)?" ],
    "queryType" : "SELECT",
    "sparqlQuery" : "(bgp (triple ?subjOfProp <http://dbpedia.org/ontology/birthPlace> ?objOfProp))\n",
    "sentenceToSparqlParameterMapping" : {
      "$x" : "subjOfProp"
    },
    "returnVariable" : "objOfProp",
    "sentenceBindings" : {
      "bindingVariableName" : "$x",
      "bindingList" : [ {
        "label" : "Balraj Sahni",
        "uri" : "http://dbpedia.org/resource/Balraj_Sahni"
      }, {
        "label" : "Balram Jakhar",
        "uri" : "http://dbpedia.org/resource/Balram_Jakhar"
      }, {
        "label" : "Baltacı Mehmed Pascià",
        "uri" : "http://dbpedia.org/resource/Baltacı_Mehmet_Pasha"
      }
        ]
    },
    "combination": false
}
```

A detailed breakdown of the JSON keys and values:

Key | Value
--- | -----
id | The continuous number of the grammar entry
language | The language of the grammar entry
type | The SentenceType of the grammar entry (SENTENCE entries are the base for inserting bindings or entries of SentenceType 'NP')
bindingType | The expected type of bindings in the bindingList and the sentences (bindingVariableName)
returnType | The type of this sentence's subject or the expected type of answer to the sentences in this grammar entry
frameType | The enum value of this entry's frame (e.g. NPP -> NounPPFrame), for combined sentences only the base frame type is listed
sentences | The generated questions or clauses with placeholders for bindings or other grammar entries - the present or past tense does not have an impact on the generated SPARQL query
queryType | The type of the SPARQL query
sparqlQuery | An algebraic representation of the generated SPARQL query body
sentenceToSparqlParameterMapping | The mapping between sentence parameters and SPARQL variables
returnVariable | The SPARQL variable that (when used in the SELECT statement) will return the answer(s) to the generated question sentences
sentenceBindings | Contains information about possible bindings for the parameters in the sentences
sentenceBindings.bindingVariableName | The placeholder inside of the sentences that is used as binding variable
sentenceBindings.bindingList | An incomplete list of up to 100 possible bindings that can be inserted into the sentences and the SPARQL query
sentenceBindings.bindingList.label | The language specific label that was retrieved from the DBPedia ontology - can be used to insert into the sentences
sentenceBindings.bindingList.uri | The DBPedia ontology reference URI, will be identical to label for literals - can be used to insert into the SPARQL query 
combination | A flag that shows if this grammar entry is a combination of multiple grammar entries or a base entry

######  question, sparql, and answer (.csv file)

```csv
"id","question","sparql","answer","frame"
1,Qual era il luogo di nascita di Balraj Sahni?, select ?o {<http://dbpedia.org/resource/Balraj_Sahni> <http://dbpedia.org/ontology/birthPlace>  ?o },http://dbpedia.org/resource/Pakistan,Pakistan,NPP
2,Qual è il luogo di nascita di Balraj Sahni?, select  ?o {<http://dbpedia.org/resource/Balraj_Sahni> <http://dbpedia.org/ontology/birthPlace>  ?o },http://dbpedia.org/resource/Pakistan,Pakistan,NPP
3,il luogo di nascita di Balraj Sahni,select  ?o {<http://dbpedia.org/resource/Balraj_Sahni> <http://dbpedia.org/ontology/birthPlace> ?o},http://dbpedia.org/resource/Pakistan,Pakistan,NPP
....
...
```


## Used Frameworks And Libraries

- Lemon API: QueGG uses the Lemon API to parse and access the properties of the turtle lexicon files. The API and more information on the Lemon API can be found here: https://github.com/monnetproject/lemon.api
- DBPedia: QueGG uses the [DBPedia](https://wiki.dbpedia.org) [SPARQL endpoint](http://dbpedia.org/sparql) to access the DBPedia Ontology. 
