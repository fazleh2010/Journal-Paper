
<!DOCTYPE html>
<html lang="en-GB" class="no-js no-svg">
  <head>
    <meta http-equiv="X-UA-Compatible" content="IE=Edge" />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>3. Package Requirements &#8212; GATE  documentation</title>
    <link rel="stylesheet" href="_static/alabaster.css" type="text/css" />
    <link rel="stylesheet" href="_static/pygments.css" type="text/css" />
    <link rel="stylesheet" type="text/css" href="https://assets.readthedocs.org/static/css/badge_only.css" />
    <script type="text/javascript" id="documentation_options" data-url_root="./" src="_static/documentation_options.js"></script>
    <script type="text/javascript" src="_static/jquery.js"></script>
    <script type="text/javascript" src="_static/underscore.js"></script>
    <script type="text/javascript" src="_static/doctools.js"></script>
    <script type="text/javascript" src="_static/language_data.js"></script>
    <script async="async" type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.5/latest.js?config=TeX-AMS-MML_HTMLorMML"></script>
    <script async="async" type="text/javascript" src="https://assets.readthedocs.org/static/javascript/readthedocs-doc-embed.js"></script>
    <link rel="canonical" href="https://opengate.readthedocs.io/en/latest/package_requirements.html" />
    <link rel="index" title="Index" href="genindex.html" />
    <link rel="search" title="Search" href="search.html" />
    <link rel="next" title="4. Compiling GATE (V9.0)" href="compilation_instructions.html" />
    <link rel="prev" title="2. Installation Guide V9.1" href="installation.html" />
   
  <link rel="stylesheet" href="_static/custom.css" type="text/css" />
  
  
  <meta name="viewport" content="width=device-width, initial-scale=0.9, maximum-scale=0.9" />

  
<!-- RTD Extra Head -->

<link rel="stylesheet" href="https://assets.readthedocs.org/static/css/readthedocs-doc-embed.css" type="text/css" />

<script type="application/json" id="READTHEDOCS_DATA">{"ad_free": true, "api_host": "https://readthedocs.org", "build_date": "2021-05-17T16:54:46Z", "builder": "sphinx", "canonical_url": null, "commit": "fa295328", "docroot": "/docs/", "features": {"docsearch_disabled": false}, "global_analytics_code": "UA-17997319-1", "language": "en", "page": "package_requirements", "programming_language": "words", "project": "opengate", "proxied_api_host": "/_", "source_suffix": ".rst", "subprojects": {}, "theme": "alabaster", "user_analytics_code": "", "version": "latest"}</script>

<!--
Using this variable directly instead of using `JSON.parse` is deprecated.
The READTHEDOCS_DATA global variable will be removed in the future.
-->
<script type="text/javascript">
READTHEDOCS_DATA = JSON.parse(document.getElementById('READTHEDOCS_DATA').innerHTML);
</script>

<script type="text/javascript" src="https://assets.readthedocs.org/static/javascript/readthedocs-analytics.js" async="async"></script>

<!-- end RTD <extrahead> -->
</head>    
    
<body>

<div class="contents local topic" id="table-of-contents">
<h2>Table of Contents</h2>
<ul class="simple">
<li><a class="reference internal" href="#coverage" id="id3">1. Extend grammar coverage</a><ul>
<li><a class="reference internal" href="#noun" id="id4">1.1 Create lexical entry for NounPPFrame</a></li>
<li><a class="reference internal" href="#transitive" id="id5">1.2 Create lexical entry for TransitiveFrame</a></li>
<li><a class="reference internal" href="#intransitive" id="id6">1.3 Create lexical entry for InTransitivePPFrame</a></li>
<li><a class="reference internal" href="#attributive" id="id7">1.4 Create lexical entry for Attributive Adjective</a></li>
<li><a class="reference internal" href="#gradable" id="id8">1.5 Create lexical entry for Gradable Adjective</a></li>
</ul>
</li>

<li><a class="reference internal" href="#new-language" id="id10">2.Extend QA system for a language</a><ul>
<!--li><a class="reference internal" href="#project" id="id101">3.1 Create a branch</a></li-->
<li><a class="reference internal" href="#base-file" id="id102">2.1 Base lemon</a></li>
<li><a class="reference internal" href="#sentence-template" id="id103">2.2 Sentence template</a></li>
<li><a class="reference internal" href="#recipe-new-language" id="id104">2.3 Recipe</a></li>

<!--li><a class="reference internal" href="#build-run" id="id104">3.4 Build and run the project</a></li>
<li><a class="reference internal" href="#qa-system" id="id105">3.5 QA system</a></li-->
</ul>
</li>

<li><a class="reference internal" href="#extend-system" id="id11">3. Extend the system for a dataset</a><ul>
<li><a class="reference internal" href="#extend-dataset" id="id13">3.1 Dataset</a></li>
<li><a class="reference internal" href="#extend-recipe" id="id14">3.2 Recipe</a></li>

<!--li><a class="reference internal" href="#generality-tree-creation" id="id8">Generality - Tree creation</a></li>
<li><a class="reference internal" href="#units" id="id9">Units</a></li>
<li><a class="reference internal" href="#axes" id="id10">Axes</a></li>
<li><a class="reference internal" href="#building-a-volume" id="id11">Building a volume</a><ul>
<li><a class="reference internal" href="#examples" id="id12">Examples</a><ul>
<li><a class="reference internal" href="#how-to-build-a-nai-crystal" id="id13">How to build a NaI crystal</a></li>
<li><a class="reference internal" href="#how-to-build-a-trpd-volume" id="id14">How to build a “trpd” volume</a></li>
<li><a class="reference internal" href="#how-to-build-a-wedge-volume" id="id15">How to build a “wedge” volume</a></li>
<li><a class="reference internal" href="#how-to-build-a-tessellated-volume" id="id16">How to build a “tessellated” volume</a></li>
<li><a class="reference internal" href="#how-to-build-a-tetmeshbox-volume" id="id17">How to build a “TetMeshBox” volume</a></li>
</ul>
</li>
</ul>
</li-->
</ul>
</li>



<li><a class="reference internal" href="#placing-a-volume" id="id25">Improve QA user interface</a><ul>
<!--li><a class="reference internal" href="#translation" id="id26">Translation</a></li>
<li><a class="reference internal" href="#rotation" id="id27">Rotation</a></li>
<li><a class="reference internal" href="#alignment" id="id28">Alignment</a></li>
<li><a class="reference internal" href="#special-example-wedge-volume-and-opet-scanner" id="id29">Special example: Wedge volume and OPET scanner</a></li-->
</ul>
</li>
<li><a class="reference internal" href="#moving-a-volume" id="id30">Other extension</a><ul>
<!--li><a class="reference internal" href="#translation-1" id="id31">Translation</a></li>
<li><a class="reference internal" href="#rotation-1" id="id32">Rotation</a></li>
<li><a class="reference internal" href="#orbiting" id="id33">Orbiting</a></li>
<li><a class="reference internal" href="#wobbling" id="id34">Wobbling</a></li>
<li><a class="reference internal" href="#eccentric-rotation" id="id35">Eccentric rotation</a></li>
<li><a class="reference internal" href="#generic-move" id="id36">Generic move</a></li>
<li><a class="reference internal" href="#generic-repeater-move" id="id37">Generic repeater move</a></li-->
</ul>
</li>
</ul>
</div>


<div class="section" id="coverage">
<h2><a class="toc-backref" href="#id3">1. Extend grammar coverage</a><a class="headerlink" href="#coverage"  title="Permalink to this headline"></a></h2>

<div class="section" id="noun">
<h3><a class="toc-backref" href="#id4">1.1 Create lexical entry for NounPPFrame</a><a class="headerlink" href="#noun" title="Permalink to this headline"></a></h3>
<h6>NounPPFrame</h6>
<p>NounPPFrame as syntactic behaviour that corresponds to a copulative construction with two arguments X and Y. , where <em>copulativeArg</em> corresponds to the copula subject <em>X</em> and the <em>prepositionalAdjunct</em> corresponds to the prepositional object <em>Y</em>.
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="n">X is the capital of Y</span></pre></div>
The semantics of the relational noun is captured with respect to the property <em>http://dbpedia.org/ontology/capital</em>, where the subject of the property is realized by the <em>prepositionalAdjunct</em> and the object of the property is realized by the copulativeArg. This essentially captures the fact that the meaning of <em>Berlin is the capital of Germany</em> is expressed by the triple:
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="n">domain(Germany) property(dbo:capital) range(Berlin)</span></pre>
</div>

<h6>Recipe to create a lexical entry</h6>
<ul>
  <li>Go to the <a href="https://forms.gle/n9My8H3rja9VEwTd7">NounPPFrame</a> </li>
  <li>input lexical element (i.e written form)</li>
  <li>input property domain and range</li>
  <li>click submit</li>
</ul>  

<p> An example of creating lexical entry "capital of"</p>

<img src="NounPPFrame.png" alt="Trulli" width="500" height="333">
<br /> 

</div>

<div class="section" id="transitive">
<h3><a class="toc-backref" href="#id5">1.2 Create lexical entry for TransitiveFrame</a><a class="headerlink" href="#transitive" title="Permalink to this headline"></a></h3>
<h6>TransitiveFrame</h6>

<p>
The lexical entry <em> (to) direct </em> states that the canonical form has the written representation <em>direct</em>. The thrid person singular written form is <em>directs</em>, and the (simple) past form is <em>directed</em>. The semantics of the verb <em>(to) direct</em> is expressed by the property <em>http://dbpedia.org/ontology/director (dbo:director)</em>. Thus, the meaning of
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="n">Syntactic-Subject(Quentin Tarantino) (directed) Syntactic-Object(Pulp Fictio)</span></pre></div>
can be expressed by the triple:
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="n">domain(dbo:Film) property(dbo:director) range(dbo:Person)</span></pre></div>

<h6>Recipe to create a lexical entry</h6>
<ul>
  <li>Go to the <a href="https://forms.gle/ETsA2okfD3M569QSA">TransitiveFrame</a> </li>
  <li>input written form (i.e infinitve form, 3rd Person form, past form)</li>
  <li>input property domain and range</li>
  <li>click submit</li>
</ul>

<p> An example of creating lexical entry "direct"</p>

<img src="TransitiveFrame.png" alt="Trulli" width="500" height="333">
<br />

<div class="section" id="intransitive">
<h3><a class="toc-backref" href="#id6">1.3 Create lexical entry for InTransitivePPFrame</a><a class="headerlink" href="#intransitive" title="Permalink to this headline"></a></h3>
<h6>InTransitivePPFrame</h6>
<p>As an example of an intransitive verb with a prepositional adjunct is the verb (to) <em>flow through</em>. The lexical entry has a subject <em>flow_subj</em> and a <em>prepositional adjunct flow_pobj</em>. The semantics of <em>X flows through Y</em> is captured by the property <em>http://dbpedia.org/ontology/city</em>, where the subject of the property is realized by the flow_subj, and the object of the property is realized by <em>flow_pobj</em>. 

<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="n">X flows through Y</span></pre></div>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="n">domain(dbo:River) property(dbo:city) range(dbo:City)</span></pre></div>

<h6>Recipe to create a lexical entry</h6>
<ul>
  <li>Go to the <a href="https://forms.gle/Xf8X3WKjNtGwXYPHA">InTransitivePPFrame</a> </li>
  <li>input written form (i.e infinitve form, 3rd Person form, past form)</li>
  <li>input property, domain, and range</li>
  <li>input preposition</li>
  <li>click submit</li>
</ul>

<p> An example of creating lexical entry "flow through"</p>

<img src="InTransitive.png" alt="Trulli" width="500" height="333">
<br />

<div class="section" id="attributive">
<h3><a class="toc-backref" href="#id7">1.4 Create lexical entry for Attributive Adjective</a><a class="headerlink" href="#attributive" title="Permalink to this headline"></a></h3>
<h6>Attributive Adjective Frame Construction</h6>
<!--p>NounPPFrame as syntactic behaviour that corresponds to a copulative construction with two arguments X and Y. , where <em>copulativeArg</em> corresponds to the copula subject <em>X</em> and the <em>prepositionalAdjunct</em> corresponds to the prepositional object <em>Y</em>.
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="n">X is the capital of Y</span></pre></div>
The semantics of the relational noun is captured with respect to the property <em>http://dbpedia.org/ontology/capital</em>, where the subject of the property is realized by the <em>prepositionalAdjunct</em> and the object of the property is realized by the copulativeArg. This essentially captures the fact that the meaning of <em>Berlin is the capital of Germany</em> is expressed by the triple:
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="n">domain(Germany) property(dbo:capital) range(Berlin)</span></pre>
</div>

<h6>Recipe to create a lexical entry</h6>
<ul>
  <li>Go to the <a href="https://forms.gle/n9My8H3rja9VEwTd7">AttributiveAdjectiveFrame</a> </li>
  <li>input lexical element (i.e written form)</li>
  <li>input property domain and range</li>
  <li>click submit</li>
</ul>

<p> An example of creating lexical entry "capital of"</p>

<img src="NounPPFrame.png" alt="Trulli" width="500" height="333"-->
<br />

<div class="section" id="gradbale">
<h3><a class="toc-backref" href="#id8">1.5 Create lexical entry for Gradable Adjective</a><a class="headerlink" href="#gradable" title="Permalink to this headline"></a></h3>
<!--h6>NounPPFrame Construction</h6>
<p>NounPPFrame as syntactic behaviour that corresponds to a copulative construction with two arguments X and Y. , where <em>copulativeArg</em> corresponds to the copula subject <em>X</em> and the <em>prepositionalAdjunct</em> corresponds to the prepositional object <em>Y</em>.
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="n">X is the capital of Y</span></pre></div>
The semantics of the relational noun is captured with respect to the property <em>http://dbpedia.org/ontology/capital</em>, where the subject of the property is realized by the <em>prepositionalAdjunct</em> and the object of the property is realized by the copulativeArg. This essentially captures the fact that the meaning of <em>Berlin is the capital of Germany</em> is expressed by the triple:
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="n">domain(Germany) property(dbo:capital) range(Berlin)</span></pre>
</div>

<h6>Recipe to create a lexical entry</h6>
<ul>
  <li>Go to the <a href="https://forms.gle/n9My8H3rja9VEwTd7">NounPPFrame</a> </li>
  <li>input lexical element (i.e written form)</li>
  <li>input property domain and range</li>
  <li>click submit</li>
</ul>

<p> An example of creating lexical entry "capital of"</p>

<img src="NounPPFrame.png" alt="Trulli" width="500" height="333"-->


</div>

<div class="section" id="new-language">
<h3><a class="toc-backref" href="#id10">2. Extend QA system for a new language</a><a class="headerlink" href="#new-language" title="Permalink to this headline"></a></h3>
QueGG system can generate grammar for two language English and German. To extend an NLP system to another language (which is not covered by QueGG) is a very useful exercise. To extend the system do the followings:

<!--ul>
  <li>create a branch of the <a href="https://github.com/fazleh2010/question-grammar-generator.git">QueGG project</a>. Alternatively, fork the project. </li>
  <li>Write base file that contains article, proposition, pronoun, etc. </li>
  <li>Write sentence templates</li>
  <li>build and run the project</l>
</ul-->

<div class="section" id="base-file">
<h3><a class="toc-backref" href="#id102">2.1 Base lemon</a><a class="headerlink" href="#base-file" title="Permalink to this headline"></a></h3>
<p>The base file contains syntactic information and  written of articles, propositions, pronouns etc.  QueGG currently have base file for <a href="https://github.com/fazleh2010/question-grammar-generator/blob/master/src/main/resources/en/base/base.ttl">english</a> and german. An example of base file is shown below. <><pre>
## auxiliary verb ##

:component_be a        lemon:LexicalEntry ;
  lemon:canonicalForm  [ lemon:writtenRep "be"@en ;
  lexinfo:verbFormMood                    lexinfo:infinitive ] ;
  lemon:otherForm      [ lemon:writtenRep "is"@en ;
  lexinfo:tense                           lexinfo:present ;
  lexinfo:person                          lexinfo:thirdPerson ;
  lexinfo:number                          lexinfo:singular ] ;
  lemon:otherForm      [ lemon:writtenRep "was"@en ;
  lexinfo:tense                           lexinfo:past ;
  lexinfo:person                          lexinfo:thirdPerson ;
  lexinfo:number                          lexinfo:singular ] ;
 
## Determiners ##

:component_the a       lemon:LexicalEntry ;
  lemon:canonicalForm  [ lemon:writtenRep "the"@en ] ;
  lexinfo:partOfSpeech lexinfo:article .

:component_a a         lemon:LexicalEntry ;
  lemon:canonicalForm  [ lemon:writtenRep "a"@en ] ;
  lexinfo:partOfSpeech lexinfo:determiner .

:component_an a        lemon:LexicalEntry ;
  lemon:canonicalForm  [ lemon:writtenRep "an"@en ] ;
  lexinfo:partOfSpeech lexinfo:determiner .
</pre>
<div>

<div class="section" id="sentence-template">
<h3><a class="toc-backref" href="#id103">2.3 Sentence template</a><a class="headerlink" href="#base-file" title="Permalink to this headline"></a></h3>
After creating base file, create sentence templates for all syntactic frames (NounPPFrame, TransitiveFrame, IntransitiveFrame, etc.). Some examples of english sentence for NounPPframe is shown below. To create sentence templates for a new language fill the <a href="http://localhost/tutorial/form/BaseFileForm.php">form</a> and submit.</p>
<pre>
Example1:
 verb(imperative_singular) pronoun(Possessive) determiner(the) noun(root) prepositionalAdjunct
 Give me the capital of Germany/Tell me the capital of Germany

Example2: 
 verb(imperative_plural) determiner(the) noun(root) prepositionalAdjunct
 List the rivers of Germany

</pre>


<p></p><div>


<div class="section" id="recipe-new-language">
<h3><a class="toc-backref" href="#id104">2.4 Recipe</a><a class="headerlink" href="#base-file" title="Permalink to this headline"></a></h3>
<ul>
  <li>Create a branch of the <a href="https://github.com/fazleh2010/question-grammar-generator.git">QueGG project</a>. Alternatively, fork the project. </li>
  <li>Write a base file by filling the <a href="https://">BaseForm</a> and submit</li>
  <li>Write sentence templates by filling the <a href="https://">SentenceTemplateForm</a> and submit</li>
  <li>Create lexical entries by filling discussed in <a href="http://localhost/tutorial/coverage.php#id3">Section 1</a> </li>
  <li>build and run the program following the <a href="https://github.com/fazleh2010/question-grammar-generator.git">instructions</a></li>
</ul>
<p></p><div>

<div class="section" id="extend-system">
<h3><a class="toc-backref" href="#id11">3. Extend the system for a dataset</a><a class="headerlink" href="#extend-system" title="Permalink to this headline"></a></h3>
<p></p><div>

<div class="section" id="extend-dataset">
<h3><a class="toc-backref" href="#id13">3.1 Dataset</a><a class="headerlink" href="#extend-dataset" title="Permalink to this headline"></a></h3>
The QA system currently implemented for DBpedia and it can be extended to any knowledge bases such as <a href="https://www.wikidata.org/wiki/Wikidata:Main_Page" >Wikidata</a>, <a href="https://en.wikipedia.org/wiki/Freebase_(database)" >Freebase</a>, and <a href="https://en.wikipedia.org/wiki/YAGO_(database)" >YAGO</a>. To extend the QA system for a dataset, it is necessary to register the sparql endpoint and list of properties of the dataset. <p></p><div><pre>
Wikidata sparql endpoint: https://query.wikidata.org/
</pre>
<p></p><div>


<div class="section" id="extend-recipe">
<h3><a class="toc-backref" href="#id14">3.2 Recipe</a><a class="headerlink" href="#extend-recipe" title="Permalink to this headline"></a></h3>
<ul>
  <li>Register the sparql endpoint and list of properties of the dataset by filling the <a href="https:">form</a> and submit. </li>
  <li>build and run the program following the <a href="https:">instructions</a></li>
  <li>Create lexical entries by filling discussed in <a href="http://localhost/tutorial/coverage.php#id3">Section 1</a> </li>
</ul>
<p></p><div>


</div>


</body>
</html>
