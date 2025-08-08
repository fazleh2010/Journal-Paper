# Multilingual Question Answering over Linked Data building on a model of the lexicon-ontology interface
The approach builds on a declarative model of the lexicon ontology interface, OntoLex lemon, which enables the specification of the meaning of lexical entries with respect to the vocabulary of a particular dataset. From such a lexicon, in our approach, a QA grammar is automatically generated that can be used to parse questions into SPARQL queries. It is shown that this approach outperforms current QA approaches on the QALD benchmarks. 

Furthermore, we demonstrate the extensibility of the approach to different languages by adapting it to German, Italian, and Spanish. We evaluate the approach with respect to the QALD benchmarks on five editions (i.e., QALD-8, QALD-7, QALD-6, QALD-5, and QALD-3) and show that our approach outperforms the state-of-the-art on all these datasets in an incremental evaluation mode in which additional lexical entries for test data are added. 


# Resource and software description

### Resource: lexicon and grammar

**A. Lexical Entries:** a lexical entry represents a unit of analysis in a lexicon. It consists of a set of grammatically related forms and a set of associated meanings or senses.

**B. Lemon:** Lemon is a model for the declarative specification of multilingual, machine-readable lexicons in RDF that capture both syntactic and semantic aspects of lexical items in relation to some ontology. 

**C. Grammar Rule Templates:** A grammar rule template consists of pre-terminals and non-terminals. The proto-terminals are replaced with content specific to the lexical entry, while the non-terminals are replaced by retrieving the corresponding URI labels using a SPARQL query.

**D. Grammar Rules:** A grammar rule is created by instantiating the grammar rule template with lexical entries.

| Language      |                |       |      |      |      | 
| :------------ |:---------------| :-----|:-----|:-----|:-----|
| English       |[Lexical Entries](https://github.com/fazleh2010/multilingual-grammar-generator/tree/main/result/en/lexicalEntries)| [Lemon](https://github.com/fazleh2010/multilingual-grammar-generator/tree/main/result/en/lemon)|[Grammar Rule Templates](https://github.com/fazleh2010/multilingual-grammar-generator/tree/main/result/en/sentenceTemplates)|[Grammar Rules](https://github.com/fazleh2010/multilingual-grammar-generator/tree/main/result/en/grammar)|[Evaluation](https://github.com/fazleh2010/multilingual-grammar-generator/tree/main/result/en/evaluation)|
| German        |[Lexical Entries](https://github.com/fazleh2010/multilingual-grammar-generator/tree/main/result/de/lexicalEntries)| [Lemon](https://github.com/fazleh2010/multilingual-grammar-generator/tree/main/result/de/lemon) |[Grammar Rule Templates](https://github.com/fazleh2010/multilingual-grammar-generator/tree/main/result/de/sentenceTemplates)|[Grammar Rules](https://github.com/fazleh2010/multilingual-grammar-generator/tree/main/result/de/grammar)|[Evaluation](https://github.com/fazleh2010/multilingual-grammar-generator/tree/main/result/de/evaluation)|
| Italian       |[Lexical Entries](https://github.com/fazleh2010/multilingual-grammar-generator/tree/main/result/it/lexicalEntries)| [Lemon](https://github.com/fazleh2010/multilingual-grammar-generator/tree/main/result/it/lemon) |[Grammar Rule Templates](https://github.com/fazleh2010/multilingual-grammar-generator/tree/main/result/it/sentenceTemplates)|[Grammar Rules](https://github.com/fazleh2010/multilingual-grammar-generator/tree/main/result/it/grammar)|[Evaluation](https://github.com/fazleh2010/multilingual-grammar-generator/tree/main/result/it/evaluation)|
| Spanish       |[Lexical Entries](https://github.com/fazleh2010/multilingual-grammar-generator/tree/main/result/es/lexicalEntries)| [Lemon](https://github.com/fazleh2010/multilingual-grammar-generator/tree/main/result/es/lemon)|[Grammar Rule Templates](https://github.com/fazleh2010/multilingual-grammar-generator/tree/main/result/es/sentenceTemplates)|[Grammar Rules](https://github.com/fazleh2010/multilingual-grammar-generator/tree/main/result/es/grammar)|[Evaluation](https://github.com/fazleh2010/multilingual-grammar-generator/tree/main/result/es/evaluation)|

### Software Description
The implementation consists of three repositories: grammar generator, parser, and question-answering system. Detailed instructions for these projects are shown in the following table.

**A. Grammar Generator:** This repository provides precise commands to run the grammar generator, along with the lexical entries and sentence templates, as well as examples of input and output.

**B. Parser:** Instructions on how to install and run the parser, including examples of input questions and the expected SPARQL output. Also includes details on how to connect the parser to the grammar generator.

**C. question-answering system:** IThis repository provides a web interface for question answering over linked data. Given a natural language question, it returns an answer along with relevant Wikipedia pages. Below are instructions for installing and running the web interface.


| System         | Type           | Input          |  Output        | Code     | Docker           | 
| :------------ |:--------------- |:---------------|:---------------| :---------------|:---------------|
| grammar generator |command line| lexical entries and sentence templates| grammar rules |[github](https://github.com/fazleh2010/multilingual-grammar-generator)| - |
| parser      |command line| natural language question| SPARQL query|[github](https://github.com/ag-sc/grammar-rules.git)|- | 
| question-answering system |user interface| natural language question| answer from RDF Data |[github](https://github.com/ag-sc/QueGG-web/tree/extension)| [dockerhub](https://hub.docker.com/repository/docker/elahi/quegg-web/general)|

### How the System Works

Follow the steps below to generate SPARQL queries from natural language questions using lexicalized grammars:

**Step 1**: Create Lexical Entries
Manually write the lexical entries for a specific language and linked data source (e.g., DBpedia, Wikidata). These entries define how natural language terms map to ontology elements.

**Step 2**: Run the Grammar Generator
Use the grammar generator to process the lexical entries. This will produce lexicalized grammar rules based on predefined templates.

**Step 3**: Run the Parser
Execute the parser using the generated grammar rules. The parser takes natural language questions as input and converts them into SPARQL queries.

**Step 4**: Use the Web Interface
Access the web interface to interact with the system. It uses the parser to answer questions and displays the result along with relevant Wikipedia page links.

# Evaluation Procedure

### Evaluation Against Benchmark 
This approach is tested on the [QALD challenge series](https://www.semantic-web-journal.net/system/files/swj3357.pdf) (QALD-9, QALD-7, QALD-6, QALD-5, and QALD-3) using standard evaluation metrics — precision, recall, and F₁ — on the DBpedia dataset. The QALD benchmarks provide a way to measure performance across multiple languages and natural language questions over DBpedia knowledge base elements.

- **Inductive evaluation**: The goal of the inductive evaluation mode is to evaluate our approach's ability to generalize to unseen data or tasks. For the inductive evaluation, we used only the training data to create lexical entries.  
- **Incremental evaluation**: In an incremental evaluation, we start with the lexicon created for the inductive evaluation and then add lexical entries incrementally until the questions in the test data are covered.

### Evaluation Against ChatGPT

In the paper, the approach is evaluated against ChatGPT using the well-known [QALD-9 benchmark](https://www.semantic-web-journal.net/system/files/swj3357.pdf) on **September 5, 2024**.  
We used **ChatGPT-4** in both **zero-shot** and **few-shot** scenarios.

- **Zero-shot scenario**: The model was prompted with an instruction to generate a SPARQL query for a given question with respect to DBpedia, without providing any examples.  
- **Few-shot scenario**: The model was given example question–SPARQL pairs before answering the test questions.

You can use any version of ChatGPT to evaluate the system yourself. The prompt for English is shown below:

\begin{verbatim}
Generate a SPARQL query for DBpedia to answer the following question: "QUESTION".
Ensure the query retrieves relevant information efficiently, using appropriate
filters, properties, and namespaces. Do not explain anything.
\end{verbatim}


# Hackathon on Question Answering based on automatically generated grammars (5-9 July 2021)
schedule: [Hackathon Plan](https://docs.google.com/document/d/14FRDHF-9kxpyOvBQKJX1KTubmxvLdfLli1UQ7L8wGYo/edit?usp=sharing) 

| Participant      | Institution      |  Task  |  Language      |  RDF Data       |  Output       |  
| :------------ |:--------------- |:---------------|:---------------| :---------------| :---------------|
| Gennaro Nolano & Maria Pia di Buono| University of Naples 'L'Orientale,' Italy | create lexical entrries | Italian |  [ArCo](https://dati.beniculturali.it/arco-rete-ontologie)|[result](https://github.com/fazleh2010/Journal-Paper/tree/italian) | 
| Gennaro Nolano & Maria Pia di Buono| University of Naples 'L'Orientale,' Italy | create lexical entrries | English | [ArCo](https://dati.beniculturali.it/arco-rete-ontologie) |[result](https://github.com/fazleh2010/Journal-Paper/tree/italian) | 
| Gennaro Nolano | University of Naples 'L'Orientale,' Italy | create lexical entries | English |[WikiData](https://www.wikidata.org/wiki/Wikidata:Main_Page)|[result](https://github.com/fazleh2010/Journal-Paper/tree/italian)  | 
| Wasim Mahmud Surjo | BRAC University, Bangladesh | create lexical entries | Bangla | [WikiData](https://www.wikidata.org/wiki/Wikidata:Main_Page)| [result](https://github.com/fazleh2010/Journal-Paper/tree/bangla) | 
| Subhana Mahmud Toshi | Arab Open University, Bahrain | create sentence templates | Bangla | [WikiData](https://www.wikidata.org/wiki/Wikidata:Main_Page)|[result](https://github.com/fazleh2010/Journal-Paper/tree/bangla) |
| Röhler | Freelance | extended NLP tools | German | DBpedia|[result](https://github.com/fazleh2010/Journal-Paper/tree/extension)|
| Japesh Methuku | ADAPT, SFI Research Centre, Ireland| improve Web Interface| English | DBpedia| [result](https://github.com/fazleh2010/Journal-Paper/tree/derilink)
| Mohammad Fazleh Elahi | University of Bielefeld | create lexical entries | English | [AIFB](https://raw.githubusercontent.com/fazleh2010/question-grammar-generator/general2/dataset/aifbfixed_complete.ttl)| [result](https://github.com/fazleh2010/Journal-Paper/tree/general2)|
| Mohammad Fazleh Elahi | University of Bielefeld | create lexical entries | German | [AIFB](https://raw.githubusercontent.com/fazleh2010/question-grammar-generator/general2/dataset/aifbfixed_complete.ttl)|[result](https://github.com/fazleh2010/Journal-Paper/tree/general2) |
| FrankGrimm | University of Bielefeld | multilingual web interface | Italian, German | any dataset|[result](https://github.com/ag-sc/QueGG-web/tree/main) |

Please use the following citation:
```
@inproceedings{Elahi-SWJ2025,
  title = {Multilingual Question Answering over Linked Data building on a model of the lexicon-ontology interface},
  author = {Mohammad Fazleh Elahi, Basil Ell, Gennaro Nolano, Philipp Cimiano},
  booktitle = {Proceedings of the Semantic Web Journal (accepted)},
  year = {2025},
  location = {[Location]},
  publisher = {[Publisher]},
  note = {Affiliations: 
    (a) Cognitive Interaction Technology Center (CITEC), Universität Bielefeld, Germany;
    (b) UniOr NLP Research Group, University of Naples "L’Orientale", Italy;
    (c) Department of Informatics, University of Oslo, Norway;
    (d) Kunstgeschichte und Digital Humanities, Philipps-Universität Marburg, Germany},
  link = {https://www.semantic-web-journal.net/system/files/swj3619.pdf}
}

```
	
