# Multilingual Question Answering over Linked Data building on a model of the lexicon-ontology interface
The approach builds on a declarative model of the lexicon ontology interface, OntoLex lemon, which enables the specification of the meaning of lexical entries with respect to the vocabulary of a particular dataset. From such a lexicon, in our approach, a QA grammar is automatically generated that can be used to parse questions into SPARQL queries. It is shown that this approach outperforms current QA approaches on the QALD benchmarks. 

Furthermore, we demonstrate the extensibility of the approach to different languages by adapting it to German, Italian, and Spanish. We evaluate the approach with respect to the QALD benchmarks on five editions (i.e., QALD-8, QALD-7, QALD-6, QALD-5, and QALD-3) and show that our approach outperforms the state-of-the-art on all these datasets in an incremental evaluation mode in which additional lexical entries for test data are added. 


# Resource and software description

### Resource: lexicon and grammar

| Language      |                |       |      |      |      | 
| :------------ |:---------------| :-----|:-----|:-----|:-----|
| English       |[Lexical Entries](https://github.com/fazleh2010/multilingual-grammar-generator/tree/main/result/en/lexicalEntries)| [Lemon](https://github.com/fazleh2010/multilingual-grammar-generator/tree/main/result/en/lemon)|[Grammar Rule Templates](https://github.com/fazleh2010/multilingual-grammar-generator/tree/main/result/en/sentenceTemplates)|[Grammar Rules](https://github.com/fazleh2010/multilingual-grammar-generator/tree/main/result/en/grammar)|[Evaluation](https://github.com/fazleh2010/multilingual-grammar-generator/tree/main/result/en/evaluation)|
| German        |[Lexical Entries](https://github.com/fazleh2010/multilingual-grammar-generator/tree/main/result/de/lexicalEntries)| [Lemon](https://github.com/fazleh2010/multilingual-grammar-generator/tree/main/result/de/lemon) |[Grammar Rule Templates](https://github.com/fazleh2010/multilingual-grammar-generator/tree/main/result/de/sentenceTemplates)|[Grammar Rules](https://github.com/fazleh2010/multilingual-grammar-generator/tree/main/result/de/grammar)|[Evaluation](https://github.com/fazleh2010/multilingual-grammar-generator/tree/main/result/de/evaluation)|
| Italian       |[Lexical Entries](https://github.com/fazleh2010/multilingual-grammar-generator/tree/main/result/it/lexicalEntries)| [Lemon](https://github.com/fazleh2010/multilingual-grammar-generator/tree/main/result/it/lemon) |[Grammar Rule Templates](https://github.com/fazleh2010/multilingual-grammar-generator/tree/main/result/it/sentenceTemplates)|[Grammar Rules](https://github.com/fazleh2010/multilingual-grammar-generator/tree/main/result/it/grammar)|[Evaluation](https://github.com/fazleh2010/multilingual-grammar-generator/tree/main/result/it/evaluation)|
| Spanish       |[Lexical Entries](https://github.com/fazleh2010/multilingual-grammar-generator/tree/main/result/es/lexicalEntries)| [Lemon](https://github.com/fazleh2010/multilingual-grammar-generator/tree/main/result/es/lemon)|[Grammar Rule Templates](https://github.com/fazleh2010/multilingual-grammar-generator/tree/main/result/es/sentenceTemplates)|[Grammar Rules](https://github.com/fazleh2010/multilingual-grammar-generator/tree/main/result/es/grammar)|[Evaluation](https://github.com/fazleh2010/multilingual-grammar-generator/tree/main/result/es/evaluation)|

### System: source code

| System         | Type           | Input          |  Output        | Code     | Docker           | 
| :------------ |:--------------- |:---------------|:---------------| :---------------|:---------------|
| grammar generator |command line| lexical entries and sentence templates| grammar rules |[github](https://github.com/fazleh2010/multilingual-grammar-generator)| - |
| parser      |command line| natural language question| SPARQL query|[github](https://github.com/ag-sc/grammar-rules.git)|- | 
| question-answering system |user interface| natural language question| answer from RDF Data |[github](https://github.com/ag-sc/QueGG-web/tree/extension)| [dockerhub](https://hub.docker.com/repository/docker/elahi/quegg-web/general)|

### Hackathon on Question Answering based on automatically generated grammars (5-9 July 2021)
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


Authors:
    Mohammad Fazleh Elahi<sup>d,*</sup> (📧 mohammad.elahi@uni-marburg.de)
    Basil Ell<sup>a,c</sup> (📧 bell@techfak.uni-bielefeld.de / basile@ifi.uio.no)
    Gennaro Nolano<sup>b</sup> (📧 nolanogenn@gmail.com)
    Philipp Cimiano<sup>a</sup> (📧 cimiano@techfak.uni-bielefeld.de)

Affiliations:
    <sup>a</sup> Cognitive Interaction Technology Center (CITEC), Universität Bielefeld, Bielefeld, Germany
    <sup>b</sup> UniOr NLP Research Group, University of Naples "L’Orientale", Naples, Italy
    <sup>c</sup> Department of Informatics, University of Oslo, Norway
    <sup>d</sup> Kunstgeschichte und Digital Humanities, Philipps-Universität Marburg, Germany

	
