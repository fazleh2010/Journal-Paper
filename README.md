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
## Functionality And Examples

### Input File Definition
QueGG reads turtle files that contain lexical entries.

The lexical entries are defined using the Lexicon Model for Ontologies [Lemon](https://lemon-model.net/) and the data category ontology [LexInfo](https://lexinfo.net/).

A valid csv file can be seen from [here] (https://github.com/fazleh2010/question-grammar-generator/blob/extension/examples/input/lexicon/en/nouns/NounPPFrame%20-%20QALD%20Train%20-%20not%20solved.csv)

A valid tt file can be seen from  [here] (https://github.com/fazleh2010/question-grammar-generator/blob/extension/examples/input/lexicon/en/nouns/lexicon-birthPlace_of.ttl)

The following <em>LexInfo</em> frames are available in QueGG:

- NounPPFrame
- TransitiveFrame
- AdjectiveAttributiveFrame
- AdjectivePPFrame
- IntransitivePPFrame

The property `lemon:sense` determines how the generated SPARQL query will look like.
It is also responsible for any sentence combinations that will be generated.

Every `lemon:sense` needs a `lemon:condition` to ensure correct mappings during sentence combination and to enable sentences like `Which city is the capital of $x?` in addition to `What is the capital of $x?`.

All lexical entries *(including their prepositions)* need to be defined in a `lemon:Lexicon` otherwise the references to those entries can't be resolved.
All entries from all input files will be added to one lexicon at the end of input file loading.

QueGG includes an english [base lexicon file](src/main/resources/en/base/base.ttl) that contains some entries for common sentence components like forms of 'to be' ('is', 'are', 'were'...) and determiners ('a', 'an', 'the').

### Output File Explanation
Using the information from the file above, QueGG can generate a grammar entry that looks like this:
```json
{
    "id": "107",
    "language": "EN",
    "type": "SENTENCE",
    "bindingType": "COUNTRY",
    "returnType": "CITY",
    "frameType": "NPP",
    "sentences": [
        "What is the capital of ($x | COUNTRY_NP)?",
        "What was the capital of ($x | COUNTRY_NP)?",
        "Which city is the capital of ($x | COUNTRY_NP)?",
        "Which city was the capital of ($x | COUNTRY_NP)?"
    ],
    "queryType": "SELECT",
    "sparqlQuery": "(bgp (triple ?subjOfProp <http://dbpedia.org/ontology/capital> ?objOfProp))\n",
    "sentenceToSparqlParameterMapping": {
        "$x": "subjOfProp"
    },
    "returnVariable": "objOfProp",
    "sentenceBindings": {
        "bindingVariableName": "$x",
        "bindingList": [
            {
                "label": "Abbasid Caliphate",
                "uri": "http://dbpedia.org/resource/Abbasid_Caliphate"
            },
            {
                "label": "Almohad Caliphate",
                "uri": "http://dbpedia.org/resource/Almohad_Caliphate"
            },
            {
                "label": "Dacia",
                "uri": "http://dbpedia.org/resource/Dacia"
            },
            {
                "label": "Democratic Republic of Afghanistan",
                "uri": "http://dbpedia.org/resource/Democratic_Republic_of_Afghanistan"
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

QueGG produces the following output:
`grammar_FULL_DATASET_<LANGUAGE>.json`
> Contains all base grammar entries (SentenceType: SENTENCE and NP)

## Parsing Grammar Entries

Any of the JSON files can be parsed using the <em>Jackson ObjectMapper</em> like this:
```java
File grammarEntriesFile = new File("grammar_FULL_DATASET_EN.json");
ObjectMapper objectMapper = new ObjectMapper();
GrammarWrapper grammarWrapper = objectMapper.readValue(grammarEntriesFile, GrammarWrapper.class);
List<GrammarEntry> grammarEntries = grammarWrapper.getGrammarEntries();
```

An elaborate example on how to parse the above output files and compile the algebraic SPARQL queries to an executable <em>Jena Query</em> can be found [here](src/test/java/util/sparql/RequestCompilerTest.java).

## Used Frameworks And Libraries

- Lemon API: QueGG uses the Lemon API to parse and access the properties of the turtle lexicon files. The API and more information on the Lemon API can be found here: https://github.com/monnetproject/lemon.api
- DBPedia: QueGG uses the [DBPedia](https://wiki.dbpedia.org) [SPARQL endpoint](http://dbpedia.org/sparql) to access the DBPedia Ontology.
All URIs in the SPARQL query and the binding list point to entities in the DBPedia Ontology.
- Jena: QueGG uses [Jena](https://github.com/apache/jena) to create, parse and execute SPARQL queries.
- Jackson Databind:QueGG uses [Jackson Databind](https://github.com/FasterXML/jackson-databind) to write and read the grammar entry JSON files.
- Lombok: QueGG uses [Lombok](https://github.com/rzwitserloot/lombok) to... well save time and skip writing those getters and setters!
- OpenCSV:QueGG uses [OpenCSV](https://github.com/loretoparisi/opencsv) to write the CSV file for the evaluation.
- QALD: QueGG uses a file from the [QALD](https://github.com/ag-sc/QALD/blob/master/7/data/qald-7-train-multilingual.json) dataset to evaluate the generated sentences and SPARQL queries.
- Log4J 2: QueGG uses [Log4J 2](https://github.com/apache/logging-log4j2) for logging.
- JUnit Jupiter:QueGG uses JUnit Jupiter for testing.
