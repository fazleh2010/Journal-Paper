
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" lang="en">
  <head>
    <meta http-equiv="X-UA-Compatible" content="IE=Edge" />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>2. Defining a geometry &#8212; GATE  documentation</title>
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
    <link rel="canonical" href="https://opengate.readthedocs.io/en/latest/defining_a_geometry.html" />
    <link rel="index" title="Index" href="genindex.html" />
    <link rel="search" title="Search" href="search.html" />
    <link rel="next" title="3. Materials" href="materials.html" />
    <link rel="prev" title="1. Getting Started" href="getting_started.html" />
   
  <link rel="stylesheet" href="_static/custom.css" type="text/css" />
  
  
  <meta name="viewport" content="width=device-width, initial-scale=0.9, maximum-scale=0.9" />

  
<!-- RTD Extra Head -->

<link rel="stylesheet" href="https://assets.readthedocs.org/static/css/readthedocs-doc-embed.css" type="text/css" />

<script type="application/json" id="READTHEDOCS_DATA">{"ad_free": true, "api_host": "https://readthedocs.org", "build_date": "2021-05-17T16:54:44Z", "builder": "sphinx", "canonical_url": null, "commit": "fa295328", "docroot": "/docs/", "features": {"docsearch_disabled": false}, "global_analytics_code": "UA-17997319-1", "language": "en", "page": "defining_a_geometry", "programming_language": "words", "project": "opengate", "proxied_api_host": "/_", "source_suffix": ".rst", "subprojects": {}, "theme": "alabaster", "user_analytics_code": "", "version": "latest"}</script>

<!--
Using this variable directly instead of using `JSON.parse` is deprecated.
The READTHEDOCS_DATA global variable will be removed in the future.
-->
<script type="text/javascript">
READTHEDOCS_DATA = JSON.parse(document.getElementById('READTHEDOCS_DATA').innerHTML);
</script>

<script type="text/javascript" src="https://assets.readthedocs.org/static/javascript/readthedocs-analytics.js" async="async"></script>

<!-- end RTD <extrahead> -->
</head><body>
  <div class="document">
    
      <div class="sphinxsidebar" role="navigation" aria-label="main navigation">
        <div class="sphinxsidebarwrapper">
<p class="logo">
  <a href="index.html">
    <img class="logo" src="_static/gate_logo.png" alt="Logo"/>
    
  </a>
</p>








<h3>Navigation</h3>
<ul class="current">
<li class="toctree-l1"><a class="reference internal" href="general.html">Getting started</a></li>
<li class="toctree-l1 current"><a class="reference internal" href="general_concept.html">General concept</a><ul class="current">
<li class="toctree-l2"><a class="reference internal" href="getting_started.html">1. Getting Started</a></li>
<li class="toctree-l2 current"><a class="current reference internal" href="#">2. Defining a geometry</a></li>
<li class="toctree-l2"><a class="reference internal" href="materials.html">3. Materials</a></li>
<li class="toctree-l2"><a class="reference internal" href="setting_up_the_physics.html">4. Setting up the physics</a></li>
<li class="toctree-l2"><a class="reference internal" href="cut_and_variance_reduction_technics.html">5. Cut and Variance Reduction Technics</a></li>
<li class="toctree-l2"><a class="reference internal" href="source_and_particle_management.html">6. Source</a></li>
<li class="toctree-l2"><a class="reference internal" href="voxelized_source_and_phantom.html">7. Voxelized source and phantom</a></li>
<li class="toctree-l2"><a class="reference internal" href="tools_to_interact_with_the_simulation_actors.html">8. Tools to Interact with the Simulation : Actors</a></li>
<li class="toctree-l2"><a class="reference internal" href="how_to_run_gate.html">9. How to run Gate</a></li>
<li class="toctree-l2"><a class="reference internal" href="visualization.html">10. Visualization</a></li>
</ul>
</li>
<li class="toctree-l1"><a class="reference internal" href="imaging_applications.html">Imaging application</a></li>
<li class="toctree-l1"><a class="reference internal" href="radiotherapy_and_dosimetry_applications.html">Radiotherapy and dosimetry applications</a></li>
<li class="toctree-l1"><a class="reference internal" href="thermal_therapy_application.html">Thermal therapy application</a></li>
<li class="toctree-l1"><a class="reference internal" href="parallel_computing.html">Parallel computing</a></li>
<li class="toctree-l1"><a class="reference internal" href="gate_tools.html">GateTools</a></li>
<li class="toctree-l1"><a class="reference internal" href="vgate.html">vGate (virtual Gate)</a></li>
<li class="toctree-l1"><a class="reference internal" href="docker_gate.html">GATE using Docker</a></li>
</ul>

<div class="relations">
<h3>Related Topics</h3>
<ul>
  <li><a href="index.html">Documentation overview</a><ul>
  <li><a href="general_concept.html">General concept</a><ul>
      <li>Previous: <a href="getting_started.html" title="previous chapter">1. Getting Started</a></li>
      <li>Next: <a href="materials.html" title="next chapter">3. Materials</a></li>
  </ul></li>
  </ul></li>
</ul>
</div>
<div id="searchbox" style="display: none" role="search">
  <h3>Quick search</h3>
    <div class="searchformwrapper">
    <form class="search" action="search.html" method="get">
      <input type="text" name="q" />
      <input type="submit" value="Go" />
      <input type="hidden" name="check_keywords" value="yes" />
      <input type="hidden" name="area" value="default" />
    </form>
    </div>
</div>
<script type="text/javascript">$('#searchbox').show(0);</script>








        </div>
      </div>
      <div class="documentwrapper">
        <div class="bodywrapper">
          

          <div class="body" role="main">
            
  <div class="section" id="defining-a-geometry">
<span id="defining-a-geometry-label"></span><h1>2. Defining a geometry<a class="headerlink" href="#defining-a-geometry" title="Permalink to this headline">¶</a></h1>
<div class="contents local topic" id="table-of-contents">
<p class="topic-title">Table of Contents</p>
<ul class="simple">
<li><a class="reference internal" href="#the-world" id="id3">The world</a><ul>
<li><a class="reference internal" href="#definition" id="id4">Definition</a></li>
<li><a class="reference internal" href="#use" id="id5">Use</a></li>
<li><a class="reference internal" href="#description-and-modification" id="id6">Description and modification</a></li>
</ul>
</li>
<li><a class="reference internal" href="#creating-a-volume" id="id7">Creating a volume</a><ul>
<li><a class="reference internal" href="#generality-tree-creation" id="id8">Generality - Tree creation</a></li>
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
</li>
</ul>
</li>
<li><a class="reference internal" href="#repeating-a-volume" id="id18">Repeating a volume</a><ul>
<li><a class="reference internal" href="#linear-repeater" id="id19">Linear repeater</a></li>
<li><a class="reference internal" href="#ring-repeater" id="id20">Ring repeater</a></li>
<li><a class="reference internal" href="#cubic-array-repeater" id="id21">Cubic array repeater</a></li>
<li><a class="reference internal" href="#quadrant-repeater" id="id22">Quadrant repeater</a></li>
<li><a class="reference internal" href="#sphere-repeater" id="id23">Sphere repeater</a></li>
<li><a class="reference internal" href="#generic-repeater" id="id24">Generic repeater</a></li>
</ul>
</li>
<li><a class="reference internal" href="#placing-a-volume" id="id25">Placing a volume</a><ul>
<li><a class="reference internal" href="#translation" id="id26">Translation</a></li>
<li><a class="reference internal" href="#rotation" id="id27">Rotation</a></li>
<li><a class="reference internal" href="#alignment" id="id28">Alignment</a></li>
<li><a class="reference internal" href="#special-example-wedge-volume-and-opet-scanner" id="id29">Special example: Wedge volume and OPET scanner</a></li>
</ul>
</li>
<li><a class="reference internal" href="#moving-a-volume" id="id30">Moving a volume</a><ul>
<li><a class="reference internal" href="#translation-1" id="id31">Translation</a></li>
<li><a class="reference internal" href="#rotation-1" id="id32">Rotation</a></li>
<li><a class="reference internal" href="#orbiting" id="id33">Orbiting</a></li>
<li><a class="reference internal" href="#wobbling" id="id34">Wobbling</a></li>
<li><a class="reference internal" href="#eccentric-rotation" id="id35">Eccentric rotation</a></li>
<li><a class="reference internal" href="#generic-move" id="id36">Generic move</a></li>
<li><a class="reference internal" href="#generic-repeater-move" id="id37">Generic repeater move</a></li>
</ul>
</li>
<li><a class="reference internal" href="#updating-the-geometry" id="id38">Updating the geometry</a></li>
</ul>
</div>
<p>The definition of a geometry is a key step in designing a simulation
because it is through the geometry definition that the imaging device
and object to be scanned are described. Particles are then tracked
through the components of the geometry. This section explains how to
define the different components of the geometry.</p>
<div class="section" id="the-world">
<h2><a class="toc-backref" href="#id3">2.1. The world</a><a class="headerlink" href="#the-world" title="Permalink to this headline">¶</a></h2>
<div class="section" id="definition">
<h3><a class="toc-backref" href="#id4">2.1.1. Definition</a><a class="headerlink" href="#definition" title="Permalink to this headline">¶</a></h3>
<p>The <em>world</em> is the only volume already defined in GATE when starting a
macro. All volumes are defined as daughters or grand-daughters of the
<em>world</em>. The <em>world</em> volume is a typical example of a GATE volume and
has predefined properties. The <em>world</em> volume is a box centered at the
origin. For any particle, tracking stops when it escapes from the
<em>world</em> volume. The <em>world</em> volume can be of any size and has to be
large enough to include all volumes involved in the simulation.</p>
</div>
<div class="section" id="use">
<h3><a class="toc-backref" href="#id5">2.1.2. Use</a><a class="headerlink" href="#use" title="Permalink to this headline">¶</a></h3>
<p>The first volume that can be created must be the daughter of the <em>world</em>
volume. Any volume must be included in the <em>world</em> volume. The geometry
is built from the <em>world</em> volume.</p>
</div>
<div class="section" id="description-and-modification">
<h3><a class="toc-backref" href="#id6">2.1.3. Description and modification</a><a class="headerlink" href="#description-and-modification" title="Permalink to this headline">¶</a></h3>
<p>The <em>world</em> volume has default parameters: shape, dimensions, material,
visibility attributes and number of children. These parameters can be
edited using the following GATE command:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">world</span><span class="o">/</span><span class="n">describe</span>
</pre></div>
</div>
<p>The output of this command is shown in <a class="reference internal" href="#world-updated"><span class="std std-numref">Fig. 2.1</span></a>. The parameters
associated with the <em>world</em> volume can be modified to be adapted to a
specific simulation configuration. Only the shape of the <em>world</em> volume,
which is a box, cannot be changed. For instance, the X length can be
changed from 50 cm to 2 m using:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">world</span><span class="o">/</span><span class="n">geometry</span><span class="o">/</span><span class="n">setXLength</span> <span class="mf">2.</span> <span class="n">m</span>
</pre></div>
</div>
<div class="figure" id="world-updated">
<img alt="Figure 1: World-updated" src="_images/World-updated.jpg" />
<p class="caption"><span class="caption-number">Fig. 2.1 </span><span class="caption-text">Description of the default parameters associated with the world</span></p>
</div>
<p>The other commands needed to modify the <em>world</em> volume attributes will
be given in the next sections.</p>
</div>
</div>
<div class="section" id="creating-a-volume">
<h2><a class="toc-backref" href="#id7">2.2. Creating a volume</a><a class="headerlink" href="#creating-a-volume" title="Permalink to this headline">¶</a></h2>
<div class="section" id="generality-tree-creation">
<h3><a class="toc-backref" href="#id8">2.2.1. Generality - Tree creation</a><a class="headerlink" href="#generality-tree-creation" title="Permalink to this headline">¶</a></h3>
<p>When a volume is created with GATE, it automatically appears in the GATE
tree. All commands applicable to the new volume are then available from
this GATE tree. For instance, if the name of the created volume is
<em>Volume_Name</em>, all commands applicable to this volume start with:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">Volume_Name</span><span class="o">/</span>
</pre></div>
</div>
<p>The tree includes the following commands:</p>
<ul class="simple">
<li>setMaterial: To assign a material to the volume</li>
<li>attachCrystalSD: To attach a crystal-SensitiveDetector to the volume</li>
<li>attachPhantomSD: To attach a phantom-SensitiveDetector to the volume</li>
<li>enable: To enable the volume</li>
<li>disable: To disable the volume</li>
<li>describe: To describe the volume</li>
</ul>
<p>The tree includes sub-trees that relate to different attributes of the
volume <em>Volume_Name</em>. The available sub-trees are:</p>
<ul class="simple">
<li>daughters: To insert a new ‘daughter’ in the volume</li>
<li>geometry: To control the geometry of the volume</li>
<li>vis: To control the display attributes of the volume</li>
<li>repeaters: To apply a new ‘repeater’ to the volume</li>
<li>moves: To ‘move’ the volume</li>
<li>placement: To control the placement of the volume</li>
</ul>
<p>The commands available in each sub-tree will be described in <a class="reference internal" href="#building-a-volume-label"><span class="std std-ref">Building a volume</span></a>, <a class="reference internal" href="#repeating-a-volume-label"><span class="std std-ref">Repeating a volume</span></a>, <a class="reference internal" href="#placing-a-volume-label"><span class="std std-ref">Placing a volume</span></a>, <a class="reference internal" href="#moving-a-volume-label"><span class="std std-ref">Moving a volume</span></a>.</p>
</div>
<div class="section" id="units">
<h3><a class="toc-backref" href="#id9">2.2.2. Units</a><a class="headerlink" href="#units" title="Permalink to this headline">¶</a></h3>
<p>Different units are predefined in GATE (see <a class="reference internal" href="#units-tab"><span class="std std-numref">Table 2.1</span></a>) and shall be
referred to using the corresponding abbreviation. Inside the GATE
environment, the list of units available in GATE can be edited using:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="o">/</span><span class="n">units</span><span class="o">/</span><span class="nb">list</span>
</pre></div>
</div>
</div>
<div class="section" id="axes">
<h3><a class="toc-backref" href="#id10">2.2.3. Axes</a><a class="headerlink" href="#axes" title="Permalink to this headline">¶</a></h3>
<p>Any position in the <em>world</em> is defined with respect to a three-axis
system: X, Y and Z. These three axes can be seen in the display window
using:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="o">/</span><span class="n">vis</span><span class="o">/</span><span class="n">scene</span><span class="o">/</span><span class="n">add</span><span class="o">/</span><span class="n">axes</span>
</pre></div>
</div>
<div class="figure" id="axis-figure">
<img alt="Figure 2: axis_figure" src="_images/axis_figure.jpg" />
<p class="caption"><span class="caption-number">Fig. 2.2 </span><span class="caption-text">Three-axis system defined in GATE. The red, green and blue axes are the X, Y
and Z axes respectively</span></p>
</div>
<table border="1" class="colwidths-auto docutils" id="units-tab">
<caption><span class="caption-number">Table 2.1 </span><span class="caption-text">List of units available in GATE and corresponding abbreviations</span><a class="headerlink" href="#units-tab" title="Permalink to this table">¶</a></caption>
<thead valign="bottom">
<tr class="row-odd"><th class="head">LENGTH</th>
<th class="head">SURFACE</th>
<th class="head">VOLUME</th>
<th class="head">ANGLE</th>
</tr>
</thead>
<tbody valign="top">
<tr class="row-even"><td>parsec pc</td>
<td>&#160;</td>
<td>&#160;</td>
<td>radian rad</td>
</tr>
<tr class="row-odd"><td>kilometer km</td>
<td>kilometer2 km2</td>
<td>kilometer3 km3</td>
<td>milliradian mrad</td>
</tr>
<tr class="row-even"><td>meter m</td>
<td>meter2 m2</td>
<td>meter33 m3</td>
<td>steradian sr</td>
</tr>
<tr class="row-odd"><td>centimeter cm</td>
<td>centimeter2 cm2</td>
<td>centimeter3 cm3</td>
<td>degre deg</td>
</tr>
<tr class="row-even"><td>micrometer mum</td>
<td>millimeter2 mm2</td>
<td>millimeter3 mm3</td>
<td>&#160;</td>
</tr>
<tr class="row-odd"><td>nanometer nm</td>
<td>&#160;</td>
<td>&#160;</td>
<td>&#160;</td>
</tr>
<tr class="row-even"><td>angstrom Ang</td>
<td>&#160;</td>
<td>&#160;</td>
<td>&#160;</td>
</tr>
<tr class="row-odd"><td><strong>TIME</strong></td>
<td><strong>SPEED</strong></td>
<td><strong>ANGULAR SPEED</strong></td>
<td><strong>ENERGY</strong></td>
</tr>
<tr class="row-even"><td>second s</td>
<td>meter/s m/s</td>
<td>radian/s rad/s</td>
<td>electronvolt eV</td>
</tr>
<tr class="row-odd"><td>millisecond ms</td>
<td>centimeter/s cm/s</td>
<td>degree/s deg/s</td>
<td>kiloelectronvolt KeV</td>
</tr>
<tr class="row-even"><td>microsecond mus</td>
<td>millimeter/s mm/s</td>
<td>rotation/s rot/s</td>
<td>megaelectronvolt MeV</td>
</tr>
<tr class="row-odd"><td>nanosecond ns</td>
<td>meter/min m/min</td>
<td>radian/min rad/min</td>
<td>gigaelectronvolt GeV</td>
</tr>
<tr class="row-even"><td>picosecond ps</td>
<td>centimeter/min cm/min</td>
<td>degree/min deg/min</td>
<td>teraelectronvolt TeV</td>
</tr>
<tr class="row-odd"><td>&#160;</td>
<td>millimeter/min m/min</td>
<td>rotation/min rot/min</td>
<td>petaelectronvolt PeV</td>
</tr>
<tr class="row-even"><td>&#160;</td>
<td>meter/h m/h</td>
<td>radian/h rad/h</td>
<td>joule j</td>
</tr>
<tr class="row-odd"><td>&#160;</td>
<td>centimer/h cm/h</td>
<td>degree/h deg/h</td>
<td>&#160;</td>
</tr>
<tr class="row-even"><td>&#160;</td>
<td>millimeter/h mm/h</td>
<td>rotation/h rot/h</td>
<td>&#160;</td>
</tr>
<tr class="row-odd"><td><strong>ACTIVITY DOSE</strong></td>
<td><strong>AMOUNT OF SUBSTANCE</strong></td>
<td><strong>MASS</strong></td>
<td><strong>VOLUMIC MASS</strong></td>
</tr>
<tr class="row-even"><td>becquerel Bq</td>
<td>mole mol</td>
<td>milligram mg</td>
<td>g/cm3 g/cm3</td>
</tr>
<tr class="row-odd"><td>curie Ci</td>
<td>&#160;</td>
<td>&#160;</td>
<td>mg/cm3 mg/cm3</td>
</tr>
<tr class="row-even"><td>gray Gy</td>
<td>&#160;</td>
<td>kilogram kg</td>
<td>kg/m3 kg/m3</td>
</tr>
<tr class="row-odd"><td><strong>ELECTRIC CHARGE</strong></td>
<td><strong>ELECTRIC CURRENT</strong></td>
<td><strong>ELECTRIC POTENTIAL</strong></td>
<td><strong>MAGNETIC FLUX - MAGNETIC FLUX DENSITY</strong></td>
</tr>
<tr class="row-even"><td>eplus e+</td>
<td>ampere A</td>
<td>volt V</td>
<td>weber Wb</td>
</tr>
<tr class="row-odd"><td>coulomb C</td>
<td>milliamper mA</td>
<td>kilovolt kV</td>
<td>tesla T</td>
</tr>
<tr class="row-even"><td>microampere muA</td>
<td>&#160;</td>
<td>megavolt MV</td>
<td>gauss G</td>
</tr>
<tr class="row-odd"><td>nanoampere nA</td>
<td>&#160;</td>
<td>kilogauss kG</td>
<td>&#160;</td>
</tr>
<tr class="row-even"><td><strong>TEMPERATURE</strong></td>
<td><strong>FORCE - PRESSURE</strong></td>
<td><strong>POWER</strong></td>
<td><strong>FREQUENCY</strong></td>
</tr>
<tr class="row-odd"><td>kelvin K</td>
<td>newton N</td>
<td>watt W</td>
<td>hertz Hz</td>
</tr>
<tr class="row-even"><td>&#160;</td>
<td>pascal Pa</td>
<td>&#160;</td>
<td>kilohertz kHz</td>
</tr>
<tr class="row-odd"><td>&#160;</td>
<td>bar bar</td>
<td>&#160;</td>
<td>megaherz MHz</td>
</tr>
<tr class="row-even"><td>&#160;</td>
<td>atmosphere atm</td>
<td>&#160;</td>
<td>&#160;</td>
</tr>
</tbody>
</table>
</div>
<div class="section" id="building-a-volume">
<span id="building-a-volume-label"></span><h3><a class="toc-backref" href="#id11">2.2.4. Building a volume</a><a class="headerlink" href="#building-a-volume" title="Permalink to this headline">¶</a></h3>
<p>Any new volume must be created as the daughter of another volume (i.e.,
<em>World</em> volume or another volume previously created).</p>
<p>Three rules must be respected when creating a new volume:</p>
<ul class="simple">
<li>A volume which is located inside another must be its daughter</li>
<li>A daughter must be fully included in its mother</li>
<li>Volumes must not overlap</li>
</ul>
<p>Errors in building the geometry yield wrong particle transportation,
hence misleading results!</p>
<p><strong>Creating a new volume</strong></p>
<p>To create a new volume, the first step is to give it a name and a mother
using:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">mother_Volume_Name</span><span class="o">/</span><span class="n">daughters</span><span class="o">/</span><span class="n">name</span> <span class="n">Volume_Name</span>
</pre></div>
</div>
<p>This command prepares the creation of a new volume named <em>Volume_Name</em>
which is the daughter of <em>mother_Volume_Name.</em></p>
<p>Some names should not be used as they have precise meanings in gate.
These names are the names of the GATE systems (see <a class="reference internal" href="defining_a_system_scanner_ct_pet_spect_optical.html#defining-a-system-label"><span class="std std-ref">Defining a system</span></a>) currently defined in GATE:
<em>scanner</em>, <em>PETscanner</em>, <em>cylindricalPET</em>, <em>SPECTHead</em>, <em>ecat</em>, <em>CPET</em>,
<em>OPET</em> and <em>OpticalSystem</em>.</p>
<p>The creation of a new volume is completed only when assigning a shape to
the new volume. The tree</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">Volume_Name</span><span class="o">/</span>
</pre></div>
</div>
<p>is then generated and all commands in the tree and the sub-trees are
available for the new volume.</p>
<p>Different volume shapes are available, namely: <strong>box, sphere, cylinder,
cone, hexagon, general or extruded trapezoid, wedge, elliptical tube,
tessellated and TetMeshBox.</strong></p>
<p>The command line for listing the available shapes is:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">world</span><span class="o">/</span><span class="n">daughters</span><span class="o">/</span><span class="n">info</span>
</pre></div>
</div>
<p>The command line for assigning a shape to a volume is:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">mother_Volume_Name</span><span class="o">/</span><span class="n">daughters</span><span class="o">/</span><span class="n">insert</span> <span class="n">Volume_shape</span>
</pre></div>
</div>
<p>where <em>Volume_shape</em> is the shape of the new volume.</p>
<p><em>Volume_shape</em> must necessarily be one of the available names:</p>
<p><strong>box</strong> for a box - <strong>sphere</strong> for a sphere - <strong>cylinder</strong> for a
cylinder - <strong>ellipsoid</strong> for an ellipsoid - <strong>cone</strong> for a cone -
<strong>eltub</strong> for a tube with an elliptical base - <strong>hexagone</strong> for an
hexagon - <strong>polycone</strong> for a polygon - <strong>trap</strong> for a general trapezoid
- <strong>trpd</strong> for an extruded trapezoid - <strong>wedge</strong> for a wedge -
<strong>tessellated</strong> for a tessellated volume and <strong>TetMeshBox</strong> for a box
which contains a tetrahedral mesh.</p>
<p>The command line assigns the shape to the last volume that has been
named.</p>
<p>The following command lists the daughters of a volume:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">Volume_Name</span><span class="o">/</span><span class="n">daughters</span><span class="o">/</span><span class="nb">list</span>
</pre></div>
</div>
<ul>
<li><p class="first">Example:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">world</span><span class="o">/</span><span class="n">daughters</span><span class="o">/</span><span class="n">name</span> <span class="n">Phantom</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">world</span><span class="o">/</span><span class="n">daughters</span><span class="o">/</span><span class="n">insert</span> <span class="n">box</span>
</pre></div>
</div>
</li>
</ul>
<p>The new volume <em>Phantom</em> with a box shape is inserted in the <em>World</em>
volume.</p>
<p><strong>Defining a size</strong></p>
<p>After creating a volume with a shape, its dimensions are the default
dimensions associated with that shape. These default dimensions can be
modified using the sub-tree /geometry/</p>
<p>The commands available in the sub-tree depend on the shape. The
different commands for each type of shape are listed in <a class="reference internal" href="#shape-tab"><span class="std std-numref">Table 2.2</span></a></p>
<p>These commands can be found in the directory:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">Volume_Name</span><span class="o">/</span><span class="n">geometry</span>   <span class="p">(</span><span class="n">Some</span> <span class="n">volumes</span> <span class="n">visualisation</span> <span class="n">are</span> <span class="n">available</span> <span class="n">here</span><span class="p">:</span> <span class="n">http</span><span class="p">:</span><span class="o">//</span><span class="n">gphysics</span><span class="o">.</span><span class="n">net</span><span class="o">/</span><span class="n">geant4</span><span class="o">/</span><span class="n">geant4</span><span class="o">-</span><span class="n">gdml</span><span class="o">-</span><span class="nb">format</span><span class="o">.</span><span class="n">html</span> <span class="p">)</span>
</pre></div>
</div>
<table border="1" class="colwidths-auto docutils" id="shape-tab">
<caption><span class="caption-number">Table 2.2 </span><span class="caption-text">Commands of the sub-tree geometry for different shapes</span><a class="headerlink" href="#shape-tab" title="Permalink to this table">¶</a></caption>
<thead valign="bottom">
<tr class="row-odd"><th class="head">BOX</th>
<th class="head">TRPD</th>
</tr>
</thead>
<tbody valign="top">
<tr class="row-even"><td>setXLength: Set the length of the box along the X axis</td>
<td>setX1Length: Set half length along X of the plane at -dz position</td>
</tr>
<tr class="row-odd"><td>setYLength: Set the length of the box along the Y axis</td>
<td>setY1Length: Set half length along Y of the plane at -dz position</td>
</tr>
<tr class="row-even"><td>setZLength: Set the length of the box along the Z axis</td>
<td>setX2Length: Set half length along X of the plane at +dz position</td>
</tr>
<tr class="row-odd"><td><strong>SPHERE</strong></td>
<td>setY2Length: Set half length along Y of the plane at +dz position</td>
</tr>
<tr class="row-even"><td>setRmin: Set the internal radius of the sphere (0 for full sphere)</td>
<td>setZLength: Set half length along Z of the trapezoid</td>
</tr>
<tr class="row-odd"><td>setRmax: Set the external radius of the sphere</td>
<td>setXBoxLength: Set half length along X of the extruded box</td>
</tr>
<tr class="row-even"><td>setPhiStart: Set the start phi angle</td>
<td>setYBoxLength: Set half length along Y of the extruded box</td>
</tr>
<tr class="row-odd"><td>setDeltaPhi: Set the phi angular span (2PI for full sphere)</td>
<td>setZBoxLength: Set half length along Z of the extruded box</td>
</tr>
<tr class="row-even"><td>setThetaStart: Set the start theta angle</td>
<td>setXBoxPos: Set center position X of the box</td>
</tr>
<tr class="row-odd"><td>setDeltaTheta: Set the theta angular span (2PI for full sphere)</td>
<td>setYBoxPos: Set center position Y of the box</td>
</tr>
<tr class="row-even"><td><strong>CYLINDER</strong></td>
<td>setZBoxPos: Set center position Z of the box</td>
</tr>
<tr class="row-odd"><td>setRmin: Set the internal radius of the cylinder (0 for full cylinder)</td>
<td><strong>PARALLELEPIPED (… not yet implemented…)</strong></td>
</tr>
<tr class="row-even"><td>setRmax: Set the external radius of the cylinder</td>
<td>setDx: Set Dx dimension of the parallelepiped</td>
</tr>
<tr class="row-odd"><td>setHeight: Set the height of the cylinder</td>
<td>setDy: Set Dy dimension of the parallelepiped</td>
</tr>
<tr class="row-even"><td>setPhiStart: Set the start phi angle</td>
<td>setDz: Set Dz dimension of the parallelepiped</td>
</tr>
<tr class="row-odd"><td>setDeltaPhi: Set the phi angular span (2PI for full cylinder)</td>
<td>setAlpha: Set Alpha angle</td>
</tr>
<tr class="row-even"><td><strong>CONE</strong></td>
<td>setTheta: Set Theta angle</td>
</tr>
<tr class="row-odd"><td>setRmin1: Set the internal radius of one side of the cone (0 for full cone)</td>
<td>setPhi: Set Phi angle</td>
</tr>
<tr class="row-even"><td>setRmax1: Set the external radius of one side of the cone</td>
<td><strong>POLYCONE (… not yet implemented…)</strong></td>
</tr>
<tr class="row-odd"><td>setRmin2: Set the internal radius of one side of the cone (0 for full cone)</td>
<td>setProfile: Set vectors of z, rInner, rOuter positions</td>
</tr>
<tr class="row-even"><td>setRmax2: Set the external radius of one side of the cone</td>
<td>setPhiStart: Set the start phi angle</td>
</tr>
<tr class="row-odd"><td>setHeight: Set the height of the cone</td>
<td>setDeltaPhi: Set the phi angular span (2PI for full cone)</td>
</tr>
<tr class="row-even"><td>setPhiStart: Set the start phi angle</td>
<td><strong>HEXAGONE</strong></td>
</tr>
<tr class="row-odd"><td>setDeltaPhi: Set the phi angular span (2PI for full cone)</td>
<td>setRadius: Set the radius of the hexagon</td>
</tr>
<tr class="row-even"><td><strong>ELLIPSOID</strong></td>
<td>setHeight: Set the height of the hexagon</td>
</tr>
<tr class="row-odd"><td>setXLength: Set the half axis length in the X direction</td>
<td><strong>WEDGE</strong></td>
</tr>
<tr class="row-even"><td>setYLength: Set the half axis length in the Y direction</td>
<td>NarrowerXLength: Set the length of the shorter side of the wedge in the X direction</td>
</tr>
<tr class="row-odd"><td>setZLength: Set the half axis length in the Z direction</td>
<td>XLength: Set the length of the wedge in the X direction</td>
</tr>
<tr class="row-even"><td>setZBottomCut: To cut the ellipsoide along the Z axis</td>
<td>YLength: Set the length of the wedge in the Y direction</td>
</tr>
<tr class="row-odd"><td>setZTopCut: To cut the ellipsoide along the Z axis</td>
<td>ZLength: Set the length of the wedge in the Z direction</td>
</tr>
<tr class="row-even"><td><strong>ELLIPTICAL TUBE</strong></td>
<td><strong>TET-MESH BOX</strong></td>
</tr>
<tr class="row-odd"><td>setLong: Set the length of the semimajor axis</td>
<td>reader/setPathToELEFile: Set path to ‘.ele’ input file, which describes a tetrahedral mesh</td>
</tr>
<tr class="row-even"><td>setShort: Set the length of the semiminor axis</td>
<td>reader/setUnitOfLength: Set unit of length for the values in the ‘.ele’ input file</td>
</tr>
<tr class="row-odd"><td>setHeight: Set the height of the tube</td>
<td>setPathToAttributeMap: Set path to txt-file which defines material and colour of the tetrahedra</td>
</tr>
<tr class="row-even"><td><strong>TESSELLATED</strong></td>
<td>&#160;</td>
</tr>
<tr class="row-odd"><td>setPathToVerticesFile: Set the path to vertices text file</td>
<td>&#160;</td>
</tr>
</tbody>
</table>
<p>For a box volume called <em>Phantom</em> , the X, Y and Z dimensions can be
defined by:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">Phantom</span><span class="o">/</span><span class="n">geometry</span><span class="o">/</span><span class="n">setXLength</span> <span class="mf">20.</span> <span class="n">cm</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">Phantom</span><span class="o">/</span><span class="n">geometry</span><span class="o">/</span><span class="n">setYLength</span> <span class="mf">10.</span> <span class="n">cm</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">Phantom</span><span class="o">/</span><span class="n">geometry</span><span class="o">/</span><span class="n">setZLength</span> <span class="mf">5.</span> <span class="n">cm</span>
</pre></div>
</div>
<p>The dimensions of the <em>Phantom</em> volume are then 20 cm, 10 cm and 5 cm
along the X, Y and Z axes respectively.</p>
<p><strong>Defining a material</strong></p>
<p>A material must be associated with each volume. The default material
assigned to a new volume is Vacuum. The list of available materials is
defined in the GateMaterials.db file. (see <a class="reference internal" href="materials.html#materials-label"><span class="std std-ref">Materials</span></a>).</p>
<p>The following command fills the volume <em>Volume_Name</em> with a material
called <em>Material</em>:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">Volume_Name</span><span class="o">/</span><span class="n">setMaterial</span> <span class="n">Material</span>
</pre></div>
</div>
<ul>
<li><p class="first">Example:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">Phantom</span><span class="o">/</span><span class="n">setMaterial</span> <span class="n">Water</span>
</pre></div>
</div>
</li>
</ul>
<p>The <em>Phantom</em> volume is filled with Water.</p>
<p><strong>Defining a color or an appearance</strong></p>
<p>To make the geometry easy to visualize, some display options can be set
using the sub-tree /vis/</p>
<p>The commands available in this sub-tree are: setColor, setVisible,
setDaughtersInvisible, setLineStyle, setLineWidth, forceSolid and
forceWireframe (see <a class="reference internal" href="#geometry-tab"><span class="std std-numref">Table 2.3</span></a>)</p>
<table border="1" class="colwidths-auto docutils" id="geometry-tab">
<caption><span class="caption-number">Table 2.3 </span><span class="caption-text">List of commands of the GATE sub-tree geometry</span><a class="headerlink" href="#geometry-tab" title="Permalink to this table">¶</a></caption>
<thead valign="bottom">
<tr class="row-odd"><th class="head">Command</th>
<th class="head">Action</th>
<th class="head">Argument</th>
</tr>
</thead>
<tbody valign="top">
<tr class="row-even"><td>setColor</td>
<td>Selects the color for the current volume</td>
<td>white, gray, black, red, green, blue, cyan, magenta and yellow</td>
</tr>
<tr class="row-odd"><td>setVisible</td>
<td>Shows or hides the current volume</td>
<td>&#160;</td>
</tr>
<tr class="row-even"><td>setDaughtersInvisible</td>
<td>Shows or hides the current volume daughters</td>
<td>&#160;</td>
</tr>
<tr class="row-odd"><td>setLineStyle</td>
<td>Sets the current volume line-style</td>
<td>dashed, dotted and unbroken</td>
</tr>
<tr class="row-even"><td>setLineWidth</td>
<td>Sets the current volume line-width</td>
<td>&#160;</td>
</tr>
<tr class="row-odd"><td>forceSolid</td>
<td>Forces solid display for the current volume</td>
<td>&#160;</td>
</tr>
<tr class="row-even"><td>forceWireframe</td>
<td>Forces wireframe display for the current volume</td>
<td>&#160;</td>
</tr>
</tbody>
</table>
<p>These commands can be found in the tree /gate/Volume_Name/vis.</p>
<ul>
<li><p class="first">Example:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">Phantom</span><span class="o">/</span><span class="n">vis</span><span class="o">/</span><span class="n">setColor</span> <span class="n">blue</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">Phantom</span><span class="o">/</span><span class="n">vis</span><span class="o">/</span><span class="n">forceWireframe</span>
</pre></div>
</div>
</li>
</ul>
<p>The <em>Phantom</em> volume will be displayed in blue and will be transparent.</p>
<p><strong>Enabling or disabling a volume</strong></p>
<p>A volume cannot be destroyed. The only possible action is to disable it:
this makes the volume disappear from the display window but not from the
geometry.</p>
<p>Only the <em>world</em> volume cannot be disabled.</p>
<p>To disable a volume <em>Volume_Name</em>, the command is:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">Volume_Name</span><span class="o">/</span><span class="n">disable</span>
</pre></div>
</div>
<p>The volume <em>Volume_Name</em> can be enabled again using:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">Volume_Name</span><span class="o">/</span><span class="n">enable</span>
</pre></div>
</div>
<ul>
<li><p class="first">Example:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">Phantom</span><span class="o">/</span><span class="n">disable</span>
</pre></div>
</div>
</li>
</ul>
<p>The <em>Phantom</em> volume is disabled.</p>
<p><strong>Describing a volume</strong></p>
<p>The parameters associated with a volume <em>Volume_name</em> can be listed
using:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">Volume_Name</span><span class="o">/</span><span class="n">describe</span>
</pre></div>
</div>
<ul>
<li><p class="first">Example:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">Phantom</span><span class="o">/</span><span class="n">describe</span>
</pre></div>
</div>
</li>
</ul>
<p>The parameters associated with the <em>Phantom</em> volume are listed.</p>
<div class="section" id="examples">
<h4><a class="toc-backref" href="#id12">2.2.4.1. Examples</a><a class="headerlink" href="#examples" title="Permalink to this headline">¶</a></h4>
<div class="section" id="how-to-build-a-nai-crystal">
<h5><a class="toc-backref" href="#id13">2.2.4.1.1. How to build a NaI crystal</a><a class="headerlink" href="#how-to-build-a-nai-crystal" title="Permalink to this headline">¶</a></h5>
<p>A volume named crystal is created as the daughter of a volume whose
shape is defined as a box:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">mother_Volume_Name</span><span class="o">/</span><span class="n">daughters</span><span class="o">/</span><span class="n">name</span>     <span class="n">crystal</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">mother_Volume_Name</span><span class="o">/</span><span class="n">daughters</span><span class="o">/</span><span class="n">insert</span>   <span class="n">box</span>
</pre></div>
</div>
<p>The X, Y and Z dimensions of the volume crystal are set to 1 cm, 40 cm,
and 54 cm respectively:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">crystal</span><span class="o">/</span><span class="n">geometry</span><span class="o">/</span><span class="n">setXLength</span>           <span class="mf">1.</span> <span class="n">cm</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">crystal</span><span class="o">/</span><span class="n">geometry</span><span class="o">/</span><span class="n">setYLength</span>           <span class="mf">40.</span> <span class="n">cm</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">crystal</span><span class="o">/</span><span class="n">geometry</span><span class="o">/</span><span class="n">setZLength</span>           <span class="mf">54.</span> <span class="n">cm</span>
</pre></div>
</div>
<p>The new volume crystal is filled with NaI:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">crystal</span><span class="o">/</span><span class="n">setMaterial</span>                   <span class="n">NaI</span>
</pre></div>
</div>
<p>The new volume crystal is colored in yellow:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">crystal</span><span class="o">/</span><span class="n">vis</span><span class="o">/</span><span class="n">setColor</span>                  <span class="n">yellow</span>
</pre></div>
</div>
<p>The next command lists the parameters associated with the crystal
volume:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">crystal</span><span class="o">/</span><span class="n">describe</span>
</pre></div>
</div>
<p>The crystal volume is disabled:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">crystal</span><span class="o">/</span><span class="n">disable</span>
</pre></div>
</div>
</div>
<div class="section" id="how-to-build-a-trpd-volume">
<h5><a class="toc-backref" href="#id14">2.2.4.1.2. How to build a “trpd” volume</a><a class="headerlink" href="#how-to-build-a-trpd-volume" title="Permalink to this headline">¶</a></h5>
<p>An alternative way of describing complicated geometries is to use a
so-called “boolean” volume in order to describe one piece using a single
volume instead of using a mother-children couple. This can make the
description easier and more synthetic. The example below describes how
the shape shown in <a class="reference internal" href="#trapeze-name"><span class="std std-numref">Fig. 2.3</span></a> can be defined using a trpd shape, based
on a “boolean” volume consisting of a trapezoid “minus” a box:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="c1"># V I S U A L I S A T I O N</span>
<span class="o">/</span><span class="n">vis</span><span class="o">/</span><span class="nb">open</span> <span class="n">OGLSX</span> <span class="o">/</span><span class="n">vis</span><span class="o">/</span><span class="n">viewer</span><span class="o">/</span><span class="n">reset</span>
<span class="o">/</span><span class="n">vis</span><span class="o">/</span><span class="n">viewer</span><span class="o">/</span><span class="n">viewpointThetaPhi</span> <span class="mi">60</span> <span class="mi">60</span>
<span class="o">/</span><span class="n">vis</span><span class="o">/</span><span class="n">viewer</span><span class="o">/</span><span class="n">zoom</span> <span class="mi">1</span>
<span class="o">/</span><span class="n">vis</span><span class="o">/</span><span class="n">viewer</span><span class="o">/</span><span class="nb">set</span><span class="o">/</span><span class="n">style</span> <span class="n">surface</span>
<span class="o">/</span><span class="n">vis</span><span class="o">/</span><span class="n">drawVolume</span> <span class="o">/</span><span class="n">tracking</span><span class="o">/</span><span class="n">storeTrajectory</span> <span class="mi">1</span>
<span class="o">/</span><span class="n">vis</span><span class="o">/</span><span class="n">scene</span><span class="o">/</span><span class="n">endOfEventAction</span> <span class="n">accumulate</span>
<span class="o">/</span><span class="n">vis</span><span class="o">/</span><span class="n">viewer</span><span class="o">/</span><span class="n">update</span>
<span class="o">/</span><span class="n">vis</span><span class="o">/</span><span class="n">verbose</span> <span class="mi">2</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">geometry</span><span class="o">/</span><span class="n">enableAutoUpdate</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">world</span><span class="o">/</span><span class="n">daughters</span><span class="o">/</span><span class="n">name</span>                <span class="n">Volume_Name</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">world</span><span class="o">/</span><span class="n">daughters</span><span class="o">/</span><span class="n">insert</span>              <span class="n">box</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">Volume_Name</span><span class="o">/</span><span class="n">geometry</span><span class="o">/</span><span class="n">setXLength</span>     <span class="mi">40</span> <span class="n">cm</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">Volume_Name</span><span class="o">/</span><span class="n">geometry</span><span class="o">/</span><span class="n">setYLength</span>     <span class="mi">40</span> <span class="n">cm</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">Volume_Name</span><span class="o">/</span><span class="n">geometry</span><span class="o">/</span><span class="n">setZLength</span>     <span class="mi">40</span> <span class="n">cm</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">Volume_Name</span><span class="o">/</span><span class="n">vis</span><span class="o">/</span><span class="n">forceWireframe</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">Volume_Name</span><span class="o">/</span><span class="n">daughters</span><span class="o">/</span><span class="n">name</span>          <span class="n">trapeze_name</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">Volume_Name</span><span class="o">/</span><span class="n">daughters</span><span class="o">/</span><span class="n">insert</span>        <span class="n">trpd</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">trapeze_name</span><span class="o">/</span><span class="n">geometry</span><span class="o">/</span><span class="n">setX1Length</span>   <span class="mf">23.3</span> <span class="n">mm</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">trapeze_name</span><span class="o">/</span><span class="n">geometry</span><span class="o">/</span><span class="n">setY1Length</span>   <span class="mf">21.4</span> <span class="n">mm</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">trapeze_name</span><span class="o">/</span><span class="n">geometry</span><span class="o">/</span><span class="n">setX2Length</span>   <span class="mf">23.3</span> <span class="n">mm</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">trapeze_name</span><span class="o">/</span><span class="n">geometry</span><span class="o">/</span><span class="n">setY2Length</span>   <span class="mf">23.3</span> <span class="n">mm</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">trapeze_name</span><span class="o">/</span><span class="n">geometry</span><span class="o">/</span><span class="n">setZLength</span>    <span class="mf">6.</span> <span class="n">mm</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">trapeze_name</span><span class="o">/</span><span class="n">geometry</span><span class="o">/</span><span class="n">setXBoxPos</span>    <span class="mf">0.</span> <span class="n">mm</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">trapeze_name</span><span class="o">/</span><span class="n">geometry</span><span class="o">/</span><span class="n">setYBoxPos</span>    <span class="mf">0.</span> <span class="n">m</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">trapeze_name</span><span class="o">/</span><span class="n">geometry</span><span class="o">/</span><span class="n">setZBoxPos</span>    <span class="mf">0.7501</span> <span class="n">mm</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">trapeze_name</span><span class="o">/</span><span class="n">geometry</span><span class="o">/</span><span class="n">setXBoxLength</span> <span class="mf">20.3</span> <span class="n">mm</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">trapeze_name</span><span class="o">/</span><span class="n">geometry</span><span class="o">/</span><span class="n">setYBoxLength</span> <span class="mf">20.3</span> <span class="n">mm</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">trapeze_name</span><span class="o">/</span><span class="n">geometry</span><span class="o">/</span><span class="n">setZBoxLength</span> <span class="mf">4.501</span> <span class="n">mm</span>
</pre></div>
</div>
<div class="figure" id="trapeze-name">
<img alt="Figure 3: trapeze_name" src="_images/trapeze_name.jpg" />
<p class="caption"><span class="caption-number">Fig. 2.3 </span><span class="caption-text">Side view of an extruded trapezoid based on a boolean solid. The contours in
blue and dashed red represent the contours of the trapezoid and the box
respectively</span></p>
</div>
<p>The new volume called <em>trapeze_name</em>, which is the daughter of the
<em>Volume_Name</em> volume, is described with 5+6 parameters. The first 5
parameters relate to the trapezoid, whereas the last 6 parameters
describe the extruded volume using a box shape.</p>
</div>
<div class="section" id="how-to-build-a-wedge-volume">
<h5><a class="toc-backref" href="#id15">2.2.4.1.3. How to build a “wedge” volume</a><a class="headerlink" href="#how-to-build-a-wedge-volume" title="Permalink to this headline">¶</a></h5>
<p>Gate provides the class <strong>GateTrapCreator</strong> to create and insert
trapezoidal volumes into the geometry. To create a trapezoid, the user
needs to specify eleven parameters (besides its name and material),
which does not make it easy to use.</p>
<p>To model “slanted” crystals, a new class called <strong>GateWedgeCreator</strong>
(derived from <strong>G4Trap</strong>) builds right angular wedges. As shown in
<a class="reference internal" href="#wedge2"><span class="std std-numref">Fig. 2.4</span></a>, a wedge is defined by only three parameters that are easily
understood:</p>
<ol class="arabic simple">
<li>XLength: is the length of the wedge in the X direction.</li>
<li>NarrowerXLength: is the length of the shorter side of the wedge in
the X direction.</li>
<li>YLength: is the length in the Y direction.</li>
<li>ZLength: is the length in the Z direction.</li>
</ol>
<div class="figure" id="wedge2">
<img alt="Figure 4: wedge2" src="_images/wedge2.jpg" />
<p class="caption"><span class="caption-number">Fig. 2.4 </span><span class="caption-text">When a wedge is inserted, it is oriented as shown in this figure</span></p>
</div>
<p>For instance, the following macro lines insert a wedge crystal as a
daughter of a module:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">module</span><span class="o">/</span><span class="n">daughters</span><span class="o">/</span><span class="n">name</span>                <span class="n">wedge0</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">module</span><span class="o">/</span><span class="n">daughters</span><span class="o">/</span><span class="n">insert</span>              <span class="n">wedge</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">wedge0</span><span class="o">/</span><span class="n">geometry</span><span class="o">/</span><span class="n">setXLength</span>           <span class="mi">10</span> <span class="n">mm</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">wedge0</span><span class="o">/</span><span class="n">geometry</span><span class="o">/</span><span class="n">setNarrowerXLength</span>   <span class="mf">8.921</span> <span class="n">mm</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">wedge0</span><span class="o">/</span><span class="n">geometry</span><span class="o">/</span><span class="n">setYLength</span>           <span class="mf">2.1620</span> <span class="n">mm</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">wedge0</span><span class="o">/</span><span class="n">geometry</span><span class="o">/</span><span class="n">setZLength</span>           <span class="mf">2.1620</span> <span class="n">mm</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">wedge0</span><span class="o">/</span><span class="n">setMaterial</span>                   <span class="n">LSO</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">wedge0</span><span class="o">/</span><span class="n">vis</span><span class="o">/</span><span class="n">setColor</span>                  <span class="n">yellow</span>
</pre></div>
</div>
</div>
<div class="section" id="how-to-build-a-tessellated-volume">
<h5><a class="toc-backref" href="#id16">2.2.4.1.4. How to build a “tessellated” volume</a><a class="headerlink" href="#how-to-build-a-tessellated-volume" title="Permalink to this headline">¶</a></h5>
<p>In GATE, you have the possibility to create a tessellated volume from an
STL file. STL is a common file format that uses triangular facets to
define the surface of a three-dimensional object. This allows to
simulate a complex geometry imported from a CAD software. The surface
described in the STL file is used to create a volume in GATE using the
Geant4 G4TessellatedSolid class. It’s important to note that only one
material is associated to a tessellated volume. You can use either ASCII
or binary STL files.</p>
<p>Here is an example to create a tessellated volume from an STL file in a
GATE macro:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">world</span><span class="o">/</span><span class="n">daughters</span><span class="o">/</span><span class="n">name</span>                                        <span class="n">kidneyLeft</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">world</span><span class="o">/</span><span class="n">daughters</span><span class="o">/</span><span class="n">insert</span>                                      <span class="n">tessellated</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">kidneyLeft</span><span class="o">/</span><span class="n">placement</span><span class="o">/</span><span class="n">setTranslation</span>                         <span class="o">-</span><span class="mf">265.3625</span> <span class="o">-</span><span class="mf">121.5875</span> <span class="o">-</span><span class="mf">842.16</span> <span class="n">mm</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">kidneyLeft</span><span class="o">/</span><span class="n">geometry</span><span class="o">/</span><span class="n">setPathToSTLFile</span>                        <span class="n">data</span><span class="o">/</span><span class="n">Label89</span><span class="o">.</span><span class="n">stl</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">kidneyLeft</span><span class="o">/</span><span class="n">setMaterial</span>                                      <span class="n">Kidney</span>
</pre></div>
</div>
<p>Label89.stl being the STL file containing the triangular facets.</p>
<p>Declaring other tessellated volumes (including daughters), one can
create a complex geometry (for example kidneys) for accurate dosimetry:</p>
<div class="figure" id="kidneys-stl">
<img alt="Figure 5: kidneys_STL" src="_images/kidneys_STL.png" />
</div>
<p>The complete code used to generate this figure can be found in the
GateContrib GitHub repository under
<a class="reference external" href="https://github.com/OpenGATE/GateContrib/tree/master/misc/geometry_STL/kidneys">misc/geometry_STL/kidneys</a>.</p>
</div>
<div class="section" id="how-to-build-a-tetmeshbox-volume">
<h5><a class="toc-backref" href="#id17">2.2.4.1.5. How to build a “TetMeshBox” volume</a><a class="headerlink" href="#how-to-build-a-tetmeshbox-volume" title="Permalink to this headline">¶</a></h5>
<p>The <strong>TetMeshBox</strong> volume is a box volume which contains a tetrahedral
mesh. The tetrahedral mesh can be loaded from an ‘.ele/.node’ file pair,
which can be generated by <a class="reference external" href="http://www.tetgen.org">TetGen</a>, an
open-source tetrahedral mesh generator. Please refer to the <a class="reference external" href="http://wias-berlin.de/software/tetgen/1.5/doc/manual/manual006.html">TetGen
manual</a>
for a comprehensive explanation of the structure of ‘.ele’ and ‘.node’
files. An example usage of the TetMeshBox would look like this:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">world</span><span class="o">/</span><span class="n">daughters</span><span class="o">/</span><span class="n">name</span>                    <span class="n">meshPhantom</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">world</span><span class="o">/</span><span class="n">daughters</span><span class="o">/</span><span class="n">insert</span>                  <span class="n">TetMeshBox</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">meshPhantom</span><span class="o">/</span><span class="n">setMaterial</span>                 <span class="n">Air</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">meshPhantom</span><span class="o">/</span><span class="n">reader</span><span class="o">/</span><span class="n">setPathToELEFile</span>     <span class="n">data</span><span class="o">/</span><span class="n">BodyHasHeart</span><span class="o">.</span><span class="n">ele</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">meshPhantom</span><span class="o">/</span><span class="n">reader</span><span class="o">/</span><span class="n">setUnitOfLength</span>      <span class="mf">1.0</span> <span class="n">mm</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">meshPhantom</span><span class="o">/</span><span class="n">setPathToAttributeMap</span>       <span class="n">data</span><span class="o">/</span><span class="n">RegionAttributeTable</span><span class="o">.</span><span class="n">dat</span>
</pre></div>
</div>
<p>Here, GATE would implicitly assume that two files exist, namely
‘data/BodyHasHeart.node’ and ‘data/BodyHasHeart.ele’. The numerical
values defined in those files are interpreted according to the
‘setUnitOfLength’ command. GATE assumes that the ‘.ele’ input file
defines a region attribute for each tetrahedron – an integer attribute,
which logically groups tetrahedra that form a sub-structure of the mesh.
The user has to provide an ‘attribute map’, which defines material and
colour for each region within the tetrahedral mesh. An attribute map is
a txt-file and looks as follows:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="c1"># [first region,    last region]    material    visible   r       g       b      alpha</span>
<span class="c1"># ------------------------------------------------------------------------------------</span>
<span class="mi">1</span>                   <span class="mi">1</span>               <span class="n">Heart</span>       <span class="n">true</span>      <span class="mf">1.00</span>    <span class="mf">0.0</span>     <span class="mf">0.0</span>    <span class="mf">1.0</span>
<span class="mi">2</span>                   <span class="mi">3</span>               <span class="n">Adipose</span>     <span class="n">true</span>      <span class="mf">1.00</span>    <span class="mf">0.89</span>    <span class="mf">0.77</span>   <span class="mf">1.0</span>
</pre></div>
</div>
<p>The first two columns refer to the region attributes defined in the
‘.ele’ file.</p>
<p>The size of the bounding box will adapt to the extent of the tetrahedral
mesh and the material of the bounding box can be set via the
‘setMaterial’. Here, a visual example of the TetMeshBox volume:</p>
<div class="figure" id="tet-mesh-box">
<img alt="Figure 6: tet_mesh_box" src="_images/tet_mesh_box.png" />
<p class="caption"><span class="caption-number">Fig. 2.6 </span><span class="caption-text">tet_mesh_box.png</span></p>
</div>
<p>The complete code used to generate this figure can be found in the
GateContrib repository on Github under
<a class="reference external" href="https://github.com/OpenGATE/GateContrib/tree/master/misc/TetrahedralMeshGeometry">misc/TetrahedralMeshGeometry</a>.</p>
</div>
</div>
</div>
</div>
<div class="section" id="repeating-a-volume">
<span id="repeating-a-volume-label"></span><h2><a class="toc-backref" href="#id18">2.3. Repeating a volume</a><a class="headerlink" href="#repeating-a-volume" title="Permalink to this headline">¶</a></h2>
<p>To create X identical volumes, there is no need to create X different
volumes. Only one volume must be created and then repeated. There are
four different ways to repeat a volume: the linear repeater, the ring
repeater, the cubic array repeater and the quadrant repeater.</p>
<p>To list the repeaters defined for the volume <em>Name_Volume</em>, use:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">Name_Volume</span><span class="o">/</span><span class="n">repeaters</span><span class="o">/</span><span class="n">info</span>
</pre></div>
</div>
<div class="section" id="linear-repeater">
<h3><a class="toc-backref" href="#id19">2.3.1. Linear repeater</a><a class="headerlink" href="#linear-repeater" title="Permalink to this headline">¶</a></h3>
<p>The linear repeater is appropriate to repeat a volume along a direction
(X, Y or Z axis). To use the linear repeater, first select this type of
repeater using:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">Name_Volume</span><span class="o">/</span><span class="n">repeaters</span><span class="o">/</span><span class="n">insert</span> <span class="n">linear</span>
</pre></div>
</div>
<p>Then define the number of times N the volume <em>Name_Volume</em> has to be
repeated using:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">Name_Volume</span><span class="o">/</span><span class="n">linear</span><span class="o">/</span><span class="n">setRepeatNumber</span> <span class="n">N</span>
</pre></div>
</div>
<p>Finally, define the step and direction of the repetition using:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">Name_Volume</span><span class="o">/</span><span class="n">linear</span><span class="o">/</span><span class="n">setRepeatVector</span> <span class="mf">0.</span> <span class="mf">0.</span> <span class="n">dZ</span><span class="o">.</span> <span class="n">mm</span>
</pre></div>
</div>
<p>A step of dZ mm along the Z direction is defined.</p>
<p>The “autoCenter” command allows the user to set the position of the
repeated volumes:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">Name_Volume</span><span class="o">/</span><span class="n">linear</span><span class="o">/</span><span class="n">autoCenter</span> <span class="n">true</span> <span class="ow">or</span> <span class="n">false</span>
</pre></div>
</div>
<p>The “true” option centers the group of repeated volumes around the
position of the initial volume that has been repeated.</p>
<p>The “false” option centers the first copy around the position of the
initial volume that has been repeated. The other copies are created by
offset. The default option is true.</p>
<div class="figure" id="avant-linear">
<img alt="Figure 7: avant_linear" src="_images/avant_linear.jpg" />
<p class="caption"><span class="caption-number">Fig. 2.7 </span><span class="caption-text">Illustration of the application of the linear repeater</span></p>
</div>
<div class="figure" id="apres-linear">
<img alt="Figure 8: apres_linear" src="_images/apres_linear.jpg" />
<p class="caption"><span class="caption-number">Fig. 2.8 </span><span class="caption-text">Illustration of the application of the linear repeater</span></p>
</div>
<ul>
<li><p class="first">Example:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">hole</span><span class="o">/</span><span class="n">repeaters</span><span class="o">/</span><span class="n">insert</span>          <span class="n">linear</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">hole</span><span class="o">/</span><span class="n">linear</span><span class="o">/</span><span class="n">setRepeatNumber</span>    <span class="mi">12</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">hole</span><span class="o">/</span><span class="n">linear</span><span class="o">/</span><span class="n">setRepeatVector</span>    <span class="mf">0.</span> <span class="mf">4.</span> <span class="mf">0.</span> <span class="n">cm</span>
</pre></div>
</div>
</li>
</ul>
<p>The <em>hole</em> volume is repeated 12 times every 4 cm along the Y axis. The
application of this linear repeater is illustrated in <a class="reference internal" href="#avant-linear"><span class="std std-numref">Fig. 2.7</span></a>.</p>
</div>
<div class="section" id="ring-repeater">
<h3><a class="toc-backref" href="#id20">2.3.2. Ring repeater</a><a class="headerlink" href="#ring-repeater" title="Permalink to this headline">¶</a></h3>
<p>The ring repeater makes it possible to repeat a volume along a ring. It
is useful to build a ring of detectors in PET.</p>
<p>To select the ring repeater, use:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">Name_Volume</span><span class="o">/</span><span class="n">repeaters</span><span class="o">/</span><span class="n">insert</span> <span class="n">ring</span>
</pre></div>
</div>
<p>To define the number of times <em>N</em> the volume <em>Name_Volume</em> has to be
repeated, use:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">Name_Volume</span><span class="o">/</span><span class="n">ring</span><span class="o">/</span><span class="n">setRepeatNumber</span> <span class="n">N</span>
</pre></div>
</div>
<p>Finally, the axis around which the volume <em>Name_Volume</em> will be repeated
must be defined by specifying two points using:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">Name_Volume</span><span class="o">/</span><span class="n">ring</span><span class="o">/</span><span class="n">setPoint1</span> <span class="mf">0.</span> <span class="mf">1.</span> <span class="mf">0.</span> <span class="n">mm</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">Name_Volume</span><span class="o">/</span><span class="n">ring</span><span class="o">/</span><span class="n">setPoint2</span> <span class="mf">0.</span> <span class="mf">0.</span> <span class="mf">0.</span> <span class="n">mm</span>
</pre></div>
</div>
<p>The default rotation axis is the Z axis. Note that the default ring
repetition goes counter clockwise.</p>
<p>These three commands are enough to repeat a volume along a ring over
360°. However, the repeat action can be further customized using one or
more of the following commands. To set the rotation angle for the first
copy, use:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">Name_Volume</span><span class="o">/</span><span class="n">ring</span><span class="o">/</span><span class="n">setFirstAngle</span> <span class="n">x</span> <span class="n">deg</span>
</pre></div>
</div>
<p>The default angle is 0 deg.</p>
<p>To set the rotation angle difference between the first and the last
copy, use:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">Name_Volume</span><span class="o">/</span><span class="n">ring</span><span class="o">/</span><span class="n">setAngularSpan</span> <span class="n">x</span> <span class="n">deg</span>
</pre></div>
</div>
<p>The default angle is 360 deg.</p>
<p>The AngularSpan, the FirstAngle and the RepeatNumber allow one to define
the rotation angle difference between two adjacent copies
(AngularPitch).</p>
<p><span class="math notranslate nohighlight">\(\frac{AngularSpan-FirstAngle}{RepeatNumber-1} = AngularPitch\)</span></p>
<p>To set the number of objects in the periodic structure, hence the
periodicity, use:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">Name_Volume</span><span class="o">/</span><span class="n">ring</span><span class="o">/</span><span class="n">setModuloNumber</span> <span class="n">M</span>
</pre></div>
</div>
<p>When the volume auto-rotation option is enabled, the volume itself is
rotated so that its axis remains tangential to the ring (see <a class="reference internal" href="#autorotenable"><span class="std std-numref">Fig. 2.9</span></a>). If this option is disabled, all repeated volumes keep the same
orientation (see <a class="reference internal" href="#autorotdisable"><span class="std std-numref">Fig. 2.10</span></a>). The commands for enabling or disabling the
auto-rotation option are:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">Name_Volume</span><span class="o">/</span><span class="n">ring</span><span class="o">/</span><span class="n">enableAutoRotation</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">Name_Volume</span><span class="o">/</span><span class="n">ring</span><span class="o">/</span><span class="n">disableAutoRotation</span>
</pre></div>
</div>
<p>A volume can also be shifted along Z periodically. Each element of a
sequence is shifted according to its position <em>inside</em> the sequence,
defined as “j” below. In a sequence composed of <span class="math notranslate nohighlight">\(M_{ModuloNumber}\)</span>
elements, the shift values are defined as
<span class="math notranslate nohighlight">\(Zshift_{i} \quad \equiv \quad Zshift_{j}\)</span> where :</p>
<ul class="simple">
<li>i is the position in the full ring</li>
<li>j =(i % <span class="math notranslate nohighlight">\(M_{ModuloNumber}\)</span>)+1 is the position in a sequence,
starting at 1.</li>
</ul>
<p>To set a shift and the value of this shift, use:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">Name_Volume</span><span class="o">/</span><span class="n">ring</span><span class="o">/</span><span class="n">setModuloNumber</span> <span class="mi">1</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">Name_Volume</span><span class="o">/</span><span class="n">ring</span><span class="o">/</span><span class="n">setZShift1</span> <span class="n">Z</span> <span class="n">mm</span>
</pre></div>
</div>
<p>Up to 8 shifts and different shift values can be defined (setZShift1 to
setZShift8).</p>
<p>Remark: This geometry description conforms to the document “List Mode
Format Implementation: Scanner geometry description Version 4.1
M.Krieguer et al ” and is fully described in the LMF output, in
particular in the ASCII header file entry:</p>
<p>z shift sector j mod <span class="math notranslate nohighlight">\(M_{ModuloNumber}\)</span> : Zshift_j units</p>
<p>Here j (j starting here at 0) stands for the <span class="math notranslate nohighlight">\(n^{th.}\)</span> object
being shifted each <span class="math notranslate nohighlight">\(M_{ModuloNumber}\)</span> object. Each shift value
introduced in the command line below corresponds to a new line in the
.cch file.</p>
<p>The LMF version 22.10.03 supports a geometry with a cylindrical
symmetry. As an example, a repeater starting at 0 degree and finishing
at 90 degree (a quarter of ring) will not be supported by the LMF
output.</p>
<div class="figure" id="autorotenable">
<img alt="Figure 9: autorotenable" src="_images/autorotenable.jpg" />
<p class="caption"><span class="caption-number">Fig. 2.9 </span><span class="caption-text">Illustration of the application of the auto-rotation option</span></p>
</div>
<div class="figure" id="autorotdisable">
<img alt="Figure 10: autorotdisable" src="_images/autorotdisable.jpg" />
<p class="caption"><span class="caption-number">Fig. 2.10 </span><span class="caption-text">Illustration of the application of the ring-repeater when the auto-rotation
option is disabled</span></p>
</div>
<ul>
<li><p class="first">Example 1:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">hole</span><span class="o">/</span><span class="n">repeaters</span><span class="o">/</span><span class="n">insert</span>          <span class="n">ring</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">hole</span><span class="o">/</span><span class="n">ring</span><span class="o">/</span><span class="n">setRepeatNumber</span>      <span class="mi">10</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">hole</span><span class="o">/</span><span class="n">ring</span><span class="o">/</span><span class="n">setPoint1</span>            <span class="mf">0.</span> <span class="mf">1.</span> <span class="mf">0.</span> <span class="n">mm</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">hole</span><span class="o">/</span><span class="n">ring</span><span class="o">/</span><span class="n">setPoint2</span>            <span class="mf">0.</span> <span class="mf">0.</span> <span class="mf">0.</span> <span class="n">mm</span>
</pre></div>
</div>
</li>
</ul>
<p>The <em>hole</em> volume is repeated 10 times around the Y axis. The
application of this ring repeater is illustrated in <a class="reference internal" href="#avant-ring"><span class="std std-numref">Fig. 2.11</span></a>.</p>
<div class="figure" id="avant-ring">
<img alt="Figure 11: avant_ring" src="_images/avant_ring.jpg" />
<p class="caption"><span class="caption-number">Fig. 2.11 </span><span class="caption-text">Illustration of the application of the ring repeater</span></p>
</div>
<div class="figure" id="apres-ring">
<img alt="Figure 12: apres_ring" src="_images/apres_ring.jpg" />
<p class="caption"><span class="caption-number">Fig. 2.12 </span><span class="caption-text">Illustration of the application of the ring repeater</span></p>
</div>
<ul>
<li><p class="first">Example 2:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">rsector</span><span class="o">/</span><span class="n">repeaters</span><span class="o">/</span><span class="n">insert</span>       <span class="n">ring</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">rsector</span><span class="o">/</span><span class="n">ring</span><span class="o">/</span><span class="n">setRepeatNumber</span>   <span class="mi">20</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">rsector</span><span class="o">/</span><span class="n">ring</span><span class="o">/</span><span class="n">setModuloNumber</span>   <span class="mi">2</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">rsector</span><span class="o">/</span><span class="n">ring</span><span class="o">/</span><span class="n">setZShift1</span>        <span class="o">-</span><span class="mi">3500</span> <span class="n">mum</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">rsector</span><span class="o">/</span><span class="n">ring</span><span class="o">/</span><span class="n">setZShift2</span>        <span class="o">+</span><span class="mi">3500</span> <span class="n">mum</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">rsector</span><span class="o">/</span><span class="n">ring</span><span class="o">/</span><span class="n">enableAutoRotation</span>
</pre></div>
</div>
</li>
</ul>
<p>The <em>rsector</em> volume is repeated 20 times along a ring. The sequence
length is 2, with the first and the second volume shifted by -3500 µ m
and 3500 µ m respectively. The <em>rsector</em> volume could also include
several volumes itself, each of them being duplicated, which is
illustrated in <a class="reference internal" href="#ringscaps"><span class="std std-numref">Fig. 2.13</span></a>.</p>
</div>
<div class="section" id="cubic-array-repeater">
<h3><a class="toc-backref" href="#id21">2.3.3. Cubic array repeater</a><a class="headerlink" href="#cubic-array-repeater" title="Permalink to this headline">¶</a></h3>
<p>The cubic array repeater is appropriate to repeat a volume along one,
two or three axes. It is useful to build a collimator for SPECT
simulations.</p>
<p>To select the cubic array repeater, use:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">Name_Volume</span><span class="o">/</span><span class="n">repeaters</span><span class="o">/</span><span class="n">insert</span> <span class="n">cubicArray</span>
</pre></div>
</div>
<p>To define the number of times <em>Nx, Ny and Nz</em> the volume <em>Name_Volume</em>
has to be repeated along the X, Y and Z axes respectively, use:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">hole</span><span class="o">/</span><span class="n">cubicArray</span><span class="o">/</span><span class="n">setRepeatNumberX</span> <span class="n">Nx</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">hole</span><span class="o">/</span><span class="n">cubicArray</span><span class="o">/</span><span class="n">setRepeatNumberY</span> <span class="n">Ny</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">hole</span><span class="o">/</span><span class="n">cubicArray</span><span class="o">/</span><span class="n">setRepeatNumberZ</span> <span class="n">Nz</span>
</pre></div>
</div>
<div class="figure" id="ringscaps">
<img alt="Figure 13: 3ringscaps" src="_images/3ringscaps.jpg" />
<p class="caption"><span class="caption-number">Fig. 2.13 </span><span class="caption-text">Example of a ring repeater with a shift. An array of 3 crystal matrices has
been repeated 20 times with a modulo N=2 shift</span></p>
</div>
<p>To define the step of the repetition <em>X mm</em>, <em>Y mm</em> and <em>Z mm</em> along the
X, Y and Z axes respectively, use:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">hole</span><span class="o">/</span><span class="n">cubicArray</span><span class="o">/</span><span class="n">setRepeatVector</span> <span class="n">X</span> <span class="n">Y</span> <span class="n">Z</span> <span class="n">mm</span>
</pre></div>
</div>
<p>The autocentering options are available for the cubic array repeater. If
a volume is initially at a position P, the set of volumes after the
repeater has been applied is centered on P if autoCenter is true
(default). If autoCenter is false, the first copy of the group is
centered on P.</p>
<ul>
<li><p class="first">Example:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">hole</span><span class="o">/</span><span class="n">repeaters</span><span class="o">/</span><span class="n">insert</span>               <span class="n">cubicArray</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">hole</span><span class="o">/</span><span class="n">cubicArray</span><span class="o">/</span><span class="n">setRepeatNumberX</span>    <span class="mi">1</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">hole</span><span class="o">/</span><span class="n">cubicArray</span><span class="o">/</span><span class="n">setRepeatNumberY</span>    <span class="mi">5</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">hole</span><span class="o">/</span><span class="n">cubicArray</span><span class="o">/</span><span class="n">setRepeatNumberZ</span>    <span class="mi">2</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">hole</span><span class="o">/</span><span class="n">cubicArray</span><span class="o">/</span><span class="n">setRepeatVector</span>     <span class="mf">0.</span> <span class="mf">5.</span> <span class="mf">15.</span> <span class="n">cm</span>
</pre></div>
</div>
</li>
</ul>
<p>The <em>hole</em> volume is repeated 5 times each 5 cm along the Y axis and
twice each 15 cm along the Z axis. The application of this cubic array
repeater is illustrated in figure <a class="reference internal" href="#avant-cubic"><span class="std std-numref">Fig. 2.14</span></a>.</p>
<div class="figure" id="avant-cubic">
<img alt="Figure 14: avant_cubic" src="_images/avant_cubic.jpg" />
<p class="caption"><span class="caption-number">Fig. 2.14 </span><span class="caption-text">Illustration of the application of the cubic array repeater</span></p>
</div>
<div class="figure" id="apres-cubic">
<img alt="Figure 15: apres_cubic" src="_images/apres_cubic.jpg" />
<p class="caption"><span class="caption-number">Fig. 2.15 </span><span class="caption-text">Illustration of the application of the cubic array repeater (after)</span></p>
</div>
</div>
<div class="section" id="quadrant-repeater">
<h3><a class="toc-backref" href="#id22">2.3.4. Quadrant repeater</a><a class="headerlink" href="#quadrant-repeater" title="Permalink to this headline">¶</a></h3>
<p>The quadrant repeater is appropriate to repeat a volume in a
triangle-like pattern similar to that of a Derenzo resolution phantom.</p>
<p>To select the quadrant repeater, use:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">Name_Volume</span><span class="o">/</span><span class="n">repeaters</span><span class="o">/</span><span class="n">insert</span> <span class="n">quadrant</span>
</pre></div>
</div>
<p>To define the number of repetition lines, use:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">hole</span><span class="o">/</span><span class="n">quadrant</span><span class="o">/</span><span class="n">setLineNumber</span> <span class="n">X</span>
</pre></div>
</div>
<p>To define the orientation of the quadrant (the direction of line
repetition), use:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">hole</span><span class="o">/</span><span class="n">quadrant</span><span class="o">/</span><span class="n">setOrientation</span> <span class="n">N</span> <span class="n">deg</span>
</pre></div>
</div>
<p>To define the distance between adjacent copies, use:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">hole</span><span class="o">/</span><span class="n">quadrant</span><span class="o">/</span><span class="n">setCopySpacing</span> <span class="n">xx</span> <span class="n">cm</span>
</pre></div>
</div>
<p>To define the maximum range of the repeater which is the maximum
distance between a copy and the original volume, use:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">hole</span><span class="o">/</span><span class="n">quadrant</span><span class="o">/</span><span class="n">setMaxRange</span> <span class="n">xx</span> <span class="n">cm</span>
</pre></div>
</div>
<p>This command can be used to remove corner-copies that would fall outside
your phantom</p>
<ul>
<li><p class="first">Example:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">hole</span><span class="o">/</span><span class="n">repeaters</span><span class="o">/</span><span class="n">insert</span>           <span class="n">quadrant</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">hole</span><span class="o">/</span><span class="n">quadrant</span><span class="o">/</span><span class="n">setLineNumber</span>     <span class="mi">5</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">hole</span><span class="o">/</span><span class="n">quadrant</span><span class="o">/</span><span class="n">setOrientation</span>    <span class="mi">90</span> <span class="n">deg</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">hole</span><span class="o">/</span><span class="n">quadrant</span><span class="o">/</span><span class="n">setCopySpacing</span>    <span class="mi">6</span> <span class="n">cm</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">hole</span><span class="o">/</span><span class="n">quadrant</span><span class="o">/</span><span class="n">setMaxRange</span>       <span class="mi">30</span> <span class="n">cm</span>
</pre></div>
</div>
</li>
</ul>
<p>The <em>hole</em> volume is repeated in a triangle-like pattern. The
application of this quadrant repeater is illustrated in <a class="reference internal" href="#avant-linear"><span class="std std-numref">Fig. 2.7</span></a>.</p>
<div class="figure" id="avant-quadrant">
<img alt="Figure 16: avant_quadrant" src="_images/avant_quadrant.jpg" />
<p class="caption"><span class="caption-number">Fig. 2.16 </span><span class="caption-text">Illustration of the application of the cubic array repeater</span></p>
</div>
<div class="figure" id="apres-quadrant">
<img alt="Figure 17: apres_quadrant" src="_images/apres_quadrant.jpg" />
<p class="caption"><span class="caption-number">Fig. 2.17 </span><span class="caption-text">Illustration of the application of the cubic array repeater (after)</span></p>
</div>
<p>Remark: The repeaters that are applied to the <em>Name_Volume</em> volume can
be listed using:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">Name_Volume</span><span class="o">/</span><span class="n">repeaters</span><span class="o">/</span><span class="nb">list</span>
</pre></div>
</div>
</div>
<div class="section" id="sphere-repeater">
<h3><a class="toc-backref" href="#id23">2.3.5. Sphere repeater</a><a class="headerlink" href="#sphere-repeater" title="Permalink to this headline">¶</a></h3>
<p>The sphere repeater makes it possible to repeat a volume along a
spherical ring. It is useful to build rings of detectors for PET
scanners having gantry of spherical shape (e.g. SIEMENS Ecat Accel,
Hi-Rez, ….)</p>
<p>To select the sphere repeater, use:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">Name_Volume</span><span class="o">/</span><span class="n">repeaters</span><span class="o">/</span><span class="n">insert</span> <span class="n">sphere</span>
</pre></div>
</div>
<p>Then, the radius R of the sphere can be set using:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">Name_Volume</span> <span class="o">/</span><span class="n">sphere</span><span class="o">/</span><span class="n">setRadius</span> <span class="n">X</span> <span class="n">cm</span>
</pre></div>
</div>
<p>To define the number of times N1 and N2 the volume <em>Name_Volume</em> has to
repeated in the transaxial plane and the axial plane respectively, use:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">Name_Volume</span><span class="o">/</span><span class="n">sphere</span><span class="o">/</span><span class="n">setRepeatNumberWithTheta</span> <span class="n">N1</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">Name_Volume</span><span class="o">/</span><span class="n">sphere</span><span class="o">/</span><span class="n">setRepeatNumberWithPhi</span> <span class="n">N2</span>
</pre></div>
</div>
<p>To set the rotation angle difference between two adjacent copies in the
transaxial direction, use:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">Name_Volume</span><span class="o">/</span><span class="n">sphere</span><span class="o">/</span><span class="n">setThetaAngle</span> <span class="n">x</span> <span class="n">deg</span>
</pre></div>
</div>
<p>To set the rotation angle difference between two adjacent copies in the
axial direction, use:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">Name_Volume</span><span class="o">/</span><span class="n">sphere</span><span class="o">/</span><span class="n">setPhiAngle</span> <span class="n">y</span> <span class="n">deg</span>
</pre></div>
</div>
<div class="figure" id="sphere-lazaro-0">
<img alt="Figure 18: sphere_lazaro_0" src="_images/sphere_lazaro_0.jpg" />
<p class="caption"><span class="caption-number">Fig. 2.18 </span><span class="caption-text">Illustration of the application of the sphere repeater</span></p>
</div>
<p>The replicates of the volume <em>Name_Volume</em> will be placed so that its
axis remains tangential to the ring.</p>
<p>Example <a class="reference internal" href="#sphere-lazaro-0"><span class="std std-numref">Fig. 2.18</span></a>:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">block</span><span class="o">/</span><span class="n">repeaters</span><span class="o">/</span><span class="n">insert</span>                   <span class="n">sphere</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">block</span><span class="o">/</span><span class="n">sphere</span><span class="o">/</span><span class="n">setRadius</span>                   <span class="mf">25.</span> <span class="n">cm</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">block</span><span class="o">/</span><span class="n">sphere</span><span class="o">/</span><span class="n">setRepeatNumberWithTheta</span>    <span class="mi">10</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">block</span><span class="o">/</span><span class="n">sphere</span><span class="o">/</span><span class="n">setRepeatNumberWithPhi</span>      <span class="mi">3</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">block</span><span class="o">/</span><span class="n">setThetaAngle</span>                      <span class="mi">36</span> <span class="n">deg</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">block</span><span class="o">/</span><span class="n">setThetaAngle</span>                      <span class="mi">20</span> <span class="n">deg</span>
</pre></div>
</div>
<p>The block volume is repeated 10 times along the transaxial plane, with a
rotation angle between two neighbouring blocks of 36 deg, and is
repeated 3 times in the axial direction with a rotation angle between
two neighbouring blocks of 20 deg. The sphere defined here has a 25 cm
radius.</p>
</div>
<div class="section" id="generic-repeater">
<span id="generic-repeater-label"></span><h3><a class="toc-backref" href="#id24">2.3.6. Generic repeater</a><a class="headerlink" href="#generic-repeater" title="Permalink to this headline">¶</a></h3>
<p>It is also possible to repeat a volume according to a list of
transformations (rotation and translation). The time column is ignored by
the generic repeater.</p>
<p>The following macros read
the transformations into a simple text file:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">myvolume</span><span class="o">/</span><span class="n">repeaters</span><span class="o">/</span><span class="n">insert</span>                       <span class="n">genericRepeater</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">myvolume</span><span class="o">/</span><span class="n">genericRepeater</span><span class="o">/</span><span class="n">setPlacementsFilename</span>  <span class="n">data</span><span class="o">/</span><span class="n">myvolume</span><span class="o">.</span><span class="n">placements</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">myvolume</span><span class="o">/</span><span class="n">genericRepeater</span><span class="o">/</span><span class="n">useRelativeTranslation</span> <span class="mi">1</span>
</pre></div>
</div>
<p>The text file “myvolume.placements” is composed as follows:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="c1">###### List of placement (translation and rotation)</span>
<span class="c1">###### Column 1      is time in seconds</span>
<span class="c1">###### Column 2      is rotationAngle in degree</span>
<span class="c1">###### Columns 3,4,5 are rotation axis</span>
<span class="c1">###### Columns 6,7,8 are translation in mm</span>
<span class="n">Time</span>     <span class="n">s</span>
<span class="n">Rotation</span> <span class="n">deg</span>
<span class="n">Translation</span> <span class="n">mm</span>
<span class="mi">0</span>      <span class="mi">0</span>       <span class="mi">0</span> <span class="mi">1</span> <span class="mi">0</span>       <span class="mi">0</span> <span class="mi">0</span> <span class="mi">10</span>
<span class="mi">0</span>      <span class="mi">10</span>      <span class="mi">0</span> <span class="mi">1</span> <span class="mi">0</span>       <span class="mi">0</span> <span class="mi">0</span> <span class="mi">10</span>
<span class="mi">0</span>      <span class="mi">15</span>      <span class="mi">0</span> <span class="mi">1</span> <span class="mi">0</span>       <span class="mi">0</span> <span class="mi">0</span> <span class="mi">20</span>
</pre></div>
</div>
<ul class="simple">
<li>line with # are ignored</li>
<li>first word must be Time followed with the units</li>
<li>next words must be Rotation then Translation followed with the units
(deg and mm here)</li>
<li>Rotation are described with 4 columns, the first for the angle, three
others for the rotation axis</li>
<li>Translation are described with X Y Z.</li>
<li>using “useRelativeTranslation 1” (default) allows to compose the
transformation according to the initial volume translation. If set to
0, the transformation is set as is (in the coordinate system of the
mother volume).</li>
</ul>
<p>See example <a class="reference internal" href="gatert.html#gatert-label"><span class="std std-ref">GateRT</span></a></p>
</div>
</div>
<div class="section" id="placing-a-volume">
<span id="placing-a-volume-label"></span><h2><a class="toc-backref" href="#id25">2.4. Placing a volume</a><a class="headerlink" href="#placing-a-volume" title="Permalink to this headline">¶</a></h2>
<p>The position of the volume in the geometry is defined using the sub-tree:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="o">/</span><span class="n">placement</span><span class="o">/</span>
</pre></div>
</div>
<p>Three types of placement are available: translation, rotation and
alignment.</p>
<div class="section" id="translation">
<h3><a class="toc-backref" href="#id26">2.4.1. Translation</a><a class="headerlink" href="#translation" title="Permalink to this headline">¶</a></h3>
<p>To translate the <em>Name_Volume</em> volume along the X direction by x cm, the
command is:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">Name_Volume</span><span class="o">/</span><span class="n">placement</span><span class="o">/</span><span class="n">setTranslation</span> <span class="n">x</span><span class="o">.</span> <span class="mf">0.</span> <span class="mf">0.</span> <span class="n">cm</span>
</pre></div>
</div>
<p>The position is always given with respect to the center of the mother
volume.</p>
<p>To set the Phi angle (in XY plane) of the translation vector, use:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">Name_Volume</span><span class="o">/</span><span class="n">placement</span><span class="o">/</span><span class="n">setPhiOfTranslation</span> <span class="n">N</span> <span class="n">deg</span>
</pre></div>
</div>
<p>To set the Theta angle (with regard to the Z axis) of the translation
vector, use:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">Name_Volume</span><span class="o">/</span><span class="n">placement</span><span class="o">/</span><span class="n">setThetaOfTranslation</span> <span class="n">N</span> <span class="n">deg</span>
</pre></div>
</div>
<p>To set the magnitude of the translation vector, use:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">Name_Volume</span><span class="o">/</span><span class="n">placement</span><span class="o">/</span><span class="n">setMagOfTranslation</span> <span class="n">xx</span> <span class="n">cm</span>
</pre></div>
</div>
<ul>
<li><p class="first">Example:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">Phantom</span><span class="o">/</span><span class="n">placement</span><span class="o">/</span><span class="n">setTranslation</span>           <span class="mf">1.</span> <span class="mf">0.</span> <span class="mf">0.</span> <span class="n">cm</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">Phantom</span><span class="o">/</span><span class="n">placement</span><span class="o">/</span><span class="n">setMagOfTranslation</span>      <span class="mf">10.</span> <span class="n">cm</span>
</pre></div>
</div>
</li>
</ul>
<p>The <em>Phantom</em> volume is placed at 10 cm, 0 cm and 0 cm from the center
of the mother volume (here the <em>world</em> volume). The application of this
translation placement is illustrated in <a class="reference internal" href="#avant-place"><span class="std std-numref">Fig. 2.19</span></a>.</p>
<div class="figure" id="avant-place">
<img alt="Figure 19: avant_place" src="_images/avant_place.jpg" />
<p class="caption"><span class="caption-number">Fig. 2.19 </span><span class="caption-text">Illustration of the translation placement</span></p>
</div>
<div class="figure" id="place-transl">
<img alt="Figure 20: place_transl" src="_images/place_transl.jpg" />
<p class="caption"><span class="caption-number">Fig. 2.20 </span><span class="caption-text">Illustration of the translation placement</span></p>
</div>
</div>
<div class="section" id="rotation">
<h3><a class="toc-backref" href="#id27">2.4.2. Rotation</a><a class="headerlink" href="#rotation" title="Permalink to this headline">¶</a></h3>
<p>To rotate the <em>Name_Volume</em> volume by <em>N</em> degrees around the <em>X</em> axis,
the commands are:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">Name_Volume</span><span class="o">/</span><span class="n">placement</span><span class="o">/</span><span class="n">setRotationAxis</span>    <span class="n">X</span> <span class="mi">0</span> <span class="mi">0</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">Name_Volume</span><span class="o">/</span><span class="n">placement</span><span class="o">/</span><span class="n">setRotationAngle</span>   <span class="n">N</span> <span class="n">deg</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">Name_Volume</span><span class="o">/</span><span class="n">placement</span><span class="o">/</span><span class="n">setAxis</span>            <span class="mi">0</span> <span class="mi">1</span> <span class="mi">0</span>
</pre></div>
</div>
<p>The default rotation axis is the Z axis.</p>
<ul>
<li><p class="first">Example:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">Phantom</span><span class="o">/</span><span class="n">placement</span><span class="o">/</span><span class="n">setRotationAxis</span>        <span class="mi">0</span> <span class="mi">1</span> <span class="mi">0</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">Phantom</span><span class="o">/</span><span class="n">placement</span><span class="o">/</span><span class="n">setRotationAngle</span>       <span class="mi">90</span> <span class="n">deg</span>
</pre></div>
</div>
</li>
</ul>
<p>The <em>Phantom</em> volume is rotated by 90 degrees around the Y axis. The
application of this rotation placement is illustrated in <a class="reference internal" href="#avant-rot"><span class="std std-numref">Fig. 2.21</span></a>.</p>
<div class="figure" id="avant-rot">
<img alt="Figure 21: Avant_rot" src="_images/Avant_rot.jpg" />
<p class="caption"><span class="caption-number">Fig. 2.21 </span><span class="caption-text">Illustration of the rotation placement</span></p>
</div>
<div class="figure" id="apres-rot">
<img alt="Figure 22: apres_rot" src="_images/place_rot.jpg" />
<p class="caption"><span class="caption-number">Fig. 2.22 </span><span class="caption-text">Illustration of the rotation placement</span></p>
</div>
</div>
<div class="section" id="alignment">
<h3><a class="toc-backref" href="#id28">2.4.3. Alignment</a><a class="headerlink" href="#alignment" title="Permalink to this headline">¶</a></h3>
<p>Using the alignment command, a volume having an axis of symmetry
(cylinder, ellipso, cone and hexagone) can be aligned parallel to one of
the three axes of the axis system.</p>
<p>To align the <em>Name_Volume</em> volume along the X axis, use:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">Name_Volume</span><span class="o">/</span><span class="n">placement</span><span class="o">/</span><span class="n">alignToX</span>
</pre></div>
</div>
<p>The rotation parameters of the <em>Name_Volume</em> volume are then set to +90
degree around the Y axis.</p>
<p>To align the <em>Name_Volume</em> volume along the Y axis, use:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">Name_Volume</span><span class="o">/</span><span class="n">placement</span><span class="o">/</span><span class="n">alignToY</span>
</pre></div>
</div>
<p>The rotation parameters of the <em>Name_Volume</em> volume are then set to -90
degree around the X axis.</p>
<p>To align the <em>Name_Volume</em> volume along the Z axis (default axis of
rotation) use:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">Name_Volume</span><span class="o">/</span><span class="n">placement</span><span class="o">/</span><span class="n">alignToZ</span>
</pre></div>
</div>
<p>The rotation parameters of the <em>Name_Volume</em> volume are then set to 0
degree.</p>
</div>
<div class="section" id="special-example-wedge-volume-and-opet-scanner">
<h3><a class="toc-backref" href="#id29">2.4.4. Special example: Wedge volume and OPET scanner</a><a class="headerlink" href="#special-example-wedge-volume-and-opet-scanner" title="Permalink to this headline">¶</a></h3>
<p>The wedge is always created as shown in <a class="reference internal" href="#wedge2"><span class="std std-numref">Fig. 2.4</span></a>, that is with the
slanted plane oriented towards the positive X direction. If one needs to
have it oriented differently, one could, for instance, rotate it:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">wedge0</span><span class="o">/</span><span class="n">placement</span><span class="o">/</span><span class="n">setRotationAxis</span> <span class="mi">0</span> <span class="mi">1</span> <span class="mi">0</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">wedge0</span><span class="o">/</span><span class="n">placement</span><span class="o">/</span><span class="n">setRotationAngle</span> <span class="mi">180</span> <span class="n">deg</span>
</pre></div>
</div>
<p>The center of a wedge in the Y and Z directions are simply</p>
<p><span class="math notranslate nohighlight">\(\frac{setYLength}{2}, \frac{setZLength}{2}\)</span></p>
<p>respectively. For the X direction, the center is located such that</p>
<p><span class="math notranslate nohighlight">\(2 \Delta = \frac{setXLength + setNarrowerXLength}{2}\)</span></p>
<p>where Delta is the length of the wedge across the middle of the Y
direction, as shown in <a class="reference internal" href="#wedge2d"><span class="std std-numref">Fig. 2.23</span></a>.</p>
<div class="figure" id="wedge2d">
<img alt="Figure 23: wedge2d" src="_images/wedge2d.jpg" />
<p class="caption"><span class="caption-number">Fig. 2.23 </span><span class="caption-text">Center of wedge</span></p>
</div>
<p>Wedge crystals are used to build the OPET scanner, in which the scanner
ring geometry approximates a true circular ring.</p>
<p>By knowing the radius gantry R and the length of the longest crystal, it
is possible to arrange a series of 8 crystals with varying the lengths
as shown in <a class="reference internal" href="#opetblock"><span class="std std-numref">Fig. 2.24</span></a>.</p>
<div class="figure" id="opetblock">
<img alt="Figure 24: opetblock" src="_images/opetblock.jpg" />
<p class="caption"><span class="caption-number">Fig. 2.24 </span><span class="caption-text">A block approximating a true circular geometry</span></p>
</div>
<p>It is first necessary to create by-hand the first row of crystals. This
is accomplished by first creating a module just big enough to contain
one row of wedge crystals:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">rsector</span><span class="o">/</span><span class="n">daughters</span><span class="o">/</span><span class="n">name</span>        <span class="n">module</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">rsector</span><span class="o">/</span><span class="n">daughters</span><span class="o">/</span><span class="n">insert</span>      <span class="n">box</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">module</span><span class="o">/</span><span class="n">geometry</span><span class="o">/</span><span class="n">setXLength</span>    <span class="mi">10</span> <span class="n">mm</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">module</span><span class="o">/</span><span class="n">geometry</span><span class="o">/</span><span class="n">setYLength</span>    <span class="mf">17.765</span> <span class="n">mm</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">module</span><span class="o">/</span><span class="n">geometry</span><span class="o">/</span><span class="n">setZLength</span>    <span class="mf">2.162</span> <span class="n">mm</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">module</span><span class="o">/</span><span class="n">setMaterial</span>            <span class="n">Air</span>
</pre></div>
</div>
<div class="figure" id="opet2">
<img alt="Figure 25: OPET2" src="_images/OPET2.jpg" />
<p class="caption"><span class="caption-number">Fig. 2.25 </span><span class="caption-text">The OPET scanner</span></p>
</div>
<p>Then, a box that will contain the first wedge crystal is located inside
the module:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">module</span><span class="o">/</span><span class="n">daughters</span><span class="o">/</span><span class="n">name</span>                  <span class="n">crystal0</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">module</span><span class="o">/</span><span class="n">daughters</span><span class="o">/</span><span class="n">insert</span>                <span class="n">box</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">crystal0</span><span class="o">/</span><span class="n">geometry</span><span class="o">/</span><span class="n">setXLength</span>           <span class="mi">10</span> <span class="n">mm</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">crystal0</span><span class="o">/</span><span class="n">geometry</span><span class="o">/</span><span class="n">setYLength</span>           <span class="mf">2.1620</span> <span class="n">mm</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">crystal0</span><span class="o">/</span><span class="n">geometry</span><span class="o">/</span><span class="n">setZLength</span>           <span class="mf">2.1620</span> <span class="n">mm</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">crystal0</span><span class="o">/</span><span class="n">placement</span><span class="o">/</span><span class="n">setTranslation</span>      <span class="mf">0.</span> <span class="o">-</span><span class="mf">7.8015</span> <span class="mf">0.</span> <span class="n">mm</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">crystal0</span><span class="o">/</span><span class="n">setMaterial</span>                   <span class="n">Air</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">crystal0</span><span class="o">/</span><span class="n">vis</span><span class="o">/</span><span class="n">setColor</span>                  <span class="n">black</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">crystal0</span><span class="o">/</span><span class="n">vis</span><span class="o">/</span><span class="n">setVisible</span>                <span class="n">false</span>
</pre></div>
</div>
<p>Finally, the actual crystal is placed inside its box:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">crystal0</span><span class="o">/</span><span class="n">daughters</span><span class="o">/</span><span class="n">name</span>                <span class="n">LSO0</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">crystal0</span><span class="o">/</span><span class="n">daughters</span><span class="o">/</span><span class="n">insert</span>              <span class="n">wedge</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">LSO0</span><span class="o">/</span><span class="n">geometry</span><span class="o">/</span><span class="n">setXLength</span>               <span class="mi">10</span> <span class="n">mm</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">LSO0</span><span class="o">/</span><span class="n">geometry</span><span class="o">/</span><span class="n">setNarrowerXLength</span>       <span class="mf">8.921</span> <span class="n">mm</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">LSO0</span><span class="o">/</span><span class="n">geometry</span><span class="o">/</span><span class="n">setYLength</span>               <span class="mf">2.1620</span> <span class="n">mm</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">LSO0</span><span class="o">/</span><span class="n">geometry</span><span class="o">/</span><span class="n">setZLength</span>               <span class="mf">2.1620</span> <span class="n">mm</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">LSO0</span><span class="o">/</span><span class="n">placement</span><span class="o">/</span><span class="n">setRotationAxis</span>         <span class="mi">0</span> <span class="mi">1</span> <span class="mi">0</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">LSO0</span><span class="o">/</span><span class="n">placement</span><span class="o">/</span><span class="n">setRotationAngle</span>        <span class="mi">180</span> <span class="n">deg</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">LSO0</span><span class="o">/</span><span class="n">placement</span><span class="o">/</span><span class="n">setTranslation</span>          <span class="mf">0.2698</span> <span class="mf">0.</span> <span class="mf">0.</span> <span class="n">mm</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">LSO0</span><span class="o">/</span><span class="n">setMaterial</span>                       <span class="n">BGO</span>
</pre></div>
</div>
<p>It is necessary to locate each crystal in separate “layers”.</p>
<p>The last two steps are repeated for each crystal inside the module. Then
the module is repeated along the Z axis and the block is repeated 6
times around the center of the scanner.</p>
<p><a class="reference internal" href="#opet2"><span class="std std-numref">Fig. 2.25</span></a> shows the final OPET scanner.</p>
</div>
</div>
<div class="section" id="moving-a-volume">
<span id="moving-a-volume-label"></span><h2><a class="toc-backref" href="#id30">2.5. Moving a volume</a><a class="headerlink" href="#moving-a-volume" title="Permalink to this headline">¶</a></h2>
<p>The GEANT geometry architecture requires the geometry to be static
during a simulation. However, the typical duration of a single event
(<em>e.g.</em> ps for the particle transport, µs for scintillation, or ms for
the response of the electronics) is very short when compared to most of
the geometrical changes to be modeled (<em>e.g.</em> movements of the phantom
or of the detector or bio-kinetics). Therefore, the elements of the
geometry are considered to be at rest during each time-step. Between
every time-step, the position and the orientation of a subset of
daughter volumes can be changed to mimic a movement such as a rotation
or a translation. These displacements are parametrized by their
velocity. Hence, the amplitude of the volume displacement is deduced
from the duration of the time-step multiplied by the velocity of the
displacement.</p>
<p>Given the speed of the components of the geometry, it is the
responsibility of the user to set the time step duration short enough in
order to produce smooth changes.</p>
<p>A volume can be moved during a simulation using five types of motion:
rotation, translation, orbiting, wobbling and eccentric rotation, as
explained below.</p>
<div class="section" id="translation-1">
<span id="id1"></span><h3><a class="toc-backref" href="#id31">2.5.1. Translation</a><a class="headerlink" href="#translation-1" title="Permalink to this headline">¶</a></h3>
<p>To translate a <em>Name_Volume</em> volume during the simulation, the commands
are:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">Name_Volume</span><span class="o">/</span><span class="n">moves</span><span class="o">/</span><span class="n">insert</span> <span class="n">translation</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">Name_Volume</span><span class="o">/</span><span class="n">translation</span><span class="o">/</span><span class="n">setSpeed</span> <span class="n">x</span> <span class="mi">0</span> <span class="mi">0</span> <span class="n">cm</span><span class="o">/</span><span class="n">s</span>
</pre></div>
</div>
<p>where x is the speed of translation and the translation is performed
along the X axis. These commands can be useful to simulate table motion
during a scan for instance.</p>
<ul>
<li><p class="first">Example:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">Table</span><span class="o">/</span><span class="n">moves</span><span class="o">/</span><span class="n">insert</span>           <span class="n">translation</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">Table</span><span class="o">/</span><span class="n">translation</span><span class="o">/</span><span class="n">setSpeed</span>   <span class="mi">0</span> <span class="mi">0</span> <span class="mi">1</span> <span class="n">cm</span><span class="o">/</span><span class="n">s</span>
</pre></div>
</div>
</li>
</ul>
<p>The <em>Table</em> volume is translated along the Z axis with a speed of 1 cm
per second.</p>
</div>
<div class="section" id="rotation-1">
<span id="id2"></span><h3><a class="toc-backref" href="#id32">2.5.2. Rotation</a><a class="headerlink" href="#rotation-1" title="Permalink to this headline">¶</a></h3>
<p>To rotate a <em>Name_Volume</em> volume around an axis during the simulation,
with a speed of N degrees per second, the commands are:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">Name_Volume</span><span class="o">/</span><span class="n">moves</span><span class="o">/</span><span class="n">insert</span> <span class="n">rotation</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">Name_Volume</span><span class="o">/</span><span class="n">rotation</span><span class="o">/</span><span class="n">setSpeed</span> <span class="n">N</span> <span class="n">deg</span><span class="o">/</span><span class="n">s</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">Name_Volume</span><span class="o">/</span><span class="n">rotation</span><span class="o">/</span><span class="n">setAxis</span> <span class="mi">0</span> <span class="n">y</span> <span class="mi">0</span>
</pre></div>
</div>
<ul>
<li><p class="first">Example:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">Phantom</span><span class="o">/</span><span class="n">moves</span><span class="o">/</span><span class="n">insert</span>         <span class="n">rotation</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">Phantom</span><span class="o">/</span><span class="n">rotation</span><span class="o">/</span><span class="n">setSpeed</span>    <span class="mi">1</span> <span class="n">deg</span><span class="o">/</span><span class="n">s</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">Phantom</span><span class="o">/</span><span class="n">rotation</span><span class="o">/</span><span class="n">setAxis</span>     <span class="mi">0</span> <span class="mi">1</span> <span class="mi">0</span>
</pre></div>
</div>
</li>
</ul>
<p>The <em>Phantom</em> volume rotates around the Y axis with a speed of 1 degree
per second.</p>
</div>
<div class="section" id="orbiting">
<h3><a class="toc-backref" href="#id33">2.5.3. Orbiting</a><a class="headerlink" href="#orbiting" title="Permalink to this headline">¶</a></h3>
<p>Rotating a volume around any axis during a simulation is possible using
the orbiting motion. This motion is needed to model the camera head
rotation in SPECT. To rotate the <em>Name_Volume</em> volume around the X axis
with a speed of N degrees per second, the commands are:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">SPECThead</span><span class="o">/</span><span class="n">moves</span><span class="o">/</span><span class="n">insert</span> <span class="n">orbiting</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">SPECThead</span><span class="o">/</span><span class="n">orbiting</span><span class="o">/</span><span class="n">setSpeed</span> <span class="n">N</span><span class="o">.</span> <span class="n">deg</span><span class="o">/</span><span class="n">s</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">SPECThead</span><span class="o">/</span><span class="n">orbiting</span><span class="o">/</span><span class="n">setPoint1</span> <span class="mi">0</span> <span class="mi">0</span> <span class="mi">0</span> <span class="n">cm</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">SPECThead</span><span class="o">/</span><span class="n">orbiting</span><span class="o">/</span><span class="n">setPoint2</span> <span class="mi">1</span> <span class="mi">0</span> <span class="mi">0</span> <span class="n">cm</span>
</pre></div>
</div>
<p>The last two commands define the rotation axis.</p>
<p>It is possible to enable or disable the volume auto-rotation option
using:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">Name_Volume</span><span class="o">/</span><span class="n">orbiting</span><span class="o">/</span><span class="n">enableAutoRotation</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">Name_Volume</span><span class="o">/</span><span class="n">orbiting</span><span class="o">/</span><span class="n">disableAutoRotation</span>
</pre></div>
</div>
<p>Example:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">camera_head</span><span class="o">/</span><span class="n">moves</span><span class="o">/</span><span class="n">insert</span>            <span class="n">orbiting</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">camera_head</span><span class="o">/</span><span class="n">orbiting</span><span class="o">/</span><span class="n">setSpeed</span>       <span class="mf">1.</span> <span class="n">deg</span><span class="o">/</span><span class="n">s</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">camera_head</span><span class="o">/</span><span class="n">orbiting</span><span class="o">/</span><span class="n">setPoint1</span>      <span class="mi">0</span> <span class="mi">0</span> <span class="mi">0</span> <span class="n">cm</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">camera_head</span><span class="o">/</span><span class="n">orbiting</span><span class="o">/</span><span class="n">setPoint2</span>      <span class="mi">0</span> <span class="mi">0</span> <span class="mi">1</span> <span class="n">cm</span>
</pre></div>
</div>
<p>The <em>camera_head</em> volume is rotated around the Z axis during the
simulation with a speed of 1 degree per second.</p>
</div>
<div class="section" id="wobbling">
<h3><a class="toc-backref" href="#id34">2.5.4. Wobbling</a><a class="headerlink" href="#wobbling" title="Permalink to this headline">¶</a></h3>
<p>The wobbling motion enables an oscillating translation movement to the
volume.</p>
<p>This motion is needed to mimic the behavior of certain PET scanners that
wobble to increase the spatial sampling of the data during the
acquisition.</p>
<p>The movement that is modeled is defined by
<span class="math notranslate nohighlight">\(dM(t) = A . sin (2.PI.f.t + phi)\)</span> where dM(t) is the translation
vector at time t, A is the maximum displacement vector, f is the
movement frequency, phi is the phase at t=0, and t is the time.</p>
<p>To set the parameters of that equation, use:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">Name_Volume</span><span class="o">/</span><span class="n">moves</span><span class="o">/</span><span class="n">insert</span> <span class="n">osc</span><span class="o">-</span><span class="n">trans</span>
</pre></div>
</div>
<p>To set the amplitude vector of the oscillating translation:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">Name_Volume</span><span class="o">/</span><span class="n">osc</span><span class="o">-</span><span class="n">trans</span><span class="o">/</span><span class="n">setAmplitude</span> <span class="n">x</span><span class="o">.</span> <span class="mf">0.</span> <span class="mf">0.</span> <span class="n">cm</span>
</pre></div>
</div>
<p>To set the frequency of the oscillating translation:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">Name_Volume</span><span class="o">/</span><span class="n">osc</span><span class="o">-</span><span class="n">trans</span><span class="o">/</span><span class="n">setFrequency</span> <span class="n">N</span> <span class="n">Hz</span>
</pre></div>
</div>
<p>To set the period of the oscillating translation:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">Name_Volume</span><span class="o">/</span><span class="n">osc</span><span class="o">-</span><span class="n">trans</span><span class="o">/</span><span class="n">setPeriod</span> <span class="n">N</span> <span class="n">s</span>
</pre></div>
</div>
<p>To set the phase at t=0 of the oscillating translation:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">Name_Volume</span><span class="o">/</span><span class="n">osc</span><span class="o">-</span><span class="n">trans</span><span class="o">/</span><span class="n">setPhase</span> <span class="n">N</span> <span class="n">deg</span>
</pre></div>
</div>
<ul>
<li><p class="first">Example:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">crystal</span><span class="o">/</span><span class="n">moves</span><span class="o">/</span><span class="n">insert</span>               <span class="n">osc</span><span class="o">-</span><span class="n">trans</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">crystal</span><span class="o">/</span><span class="n">osc</span><span class="o">-</span><span class="n">trans</span><span class="o">/</span><span class="n">setAmplitude</span>     <span class="mf">10.</span> <span class="mf">0.</span> <span class="mf">0.</span> <span class="n">cm</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">crystal</span><span class="o">/</span><span class="n">osc</span><span class="o">-</span><span class="n">trans</span><span class="o">/</span><span class="n">setFrequency</span>     <span class="mi">50</span> <span class="n">Hz</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">crystal</span><span class="o">/</span><span class="n">osc</span><span class="o">-</span><span class="n">trans</span><span class="o">/</span><span class="n">setPeriod</span>        <span class="mi">1</span> <span class="n">s</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">crystal</span><span class="o">/</span><span class="n">osc</span><span class="o">-</span><span class="n">trans</span><span class="o">/</span><span class="n">setPhase</span>         <span class="mi">90</span> <span class="n">deg</span>
</pre></div>
</div>
</li>
</ul>
<p>In this example, the movement that is modeled is defined by
<span class="math notranslate nohighlight">\(dM(t) = 10 . sin (100.PI.t + 90)\)</span></p>
</div>
<div class="section" id="eccentric-rotation">
<h3><a class="toc-backref" href="#id35">2.5.5. Eccentric rotation</a><a class="headerlink" href="#eccentric-rotation" title="Permalink to this headline">¶</a></h3>
<p>The eccentric rotation motion enables an eccentric rotation movement of
the volume. It is a particular case of the orbiting movement. To set the
object in eccentric position (X-Y-Z) and rotate it around the OZ lab
frame axis, use:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">Name_Volume</span><span class="o">/</span><span class="n">moves</span><span class="o">/</span><span class="n">insert</span> <span class="n">eccent</span><span class="o">-</span><span class="n">rot</span>
</pre></div>
</div>
<p>To set the shifts in the X-Y-Z directions:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">Name_Volume</span><span class="o">/</span><span class="n">eccent</span><span class="o">-</span><span class="n">rot</span><span class="o">/</span><span class="n">setShiftXYZ</span> <span class="n">x</span> <span class="n">y</span> <span class="n">z</span> <span class="n">cm</span>
</pre></div>
</div>
<p>To set the orbiting angular speed:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">Name_Volume</span><span class="o">/</span><span class="n">eccent</span><span class="o">-</span><span class="n">rot</span><span class="o">/</span><span class="n">setSpeed</span> <span class="n">N</span> <span class="n">deg</span><span class="o">/</span><span class="n">s</span>
</pre></div>
</div>
<p>Remark: This particular move is closely related to the LMF definition
since the move parameters (shifts in all 3 directions and angular speed)
are propagated in the .cch header.</p>
<ul>
<li><p class="first">Example:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">crystal</span><span class="o">/</span><span class="n">moves</span><span class="o">/</span><span class="n">insert</span>                 <span class="n">eccent</span><span class="o">-</span><span class="n">rot</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">crystal</span><span class="o">/</span><span class="n">eccent</span><span class="o">-</span><span class="n">rot</span><span class="o">/</span><span class="n">setShiftXYZ</span>       <span class="mf">5.</span> <span class="mf">0.</span> <span class="mf">0.</span> <span class="n">cm</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">crystal</span><span class="o">/</span><span class="n">eccent</span><span class="o">-</span><span class="n">rot</span><span class="o">/</span><span class="n">setSpeed</span>          <span class="mi">10</span> <span class="n">deg</span><span class="o">/</span><span class="n">s</span>
</pre></div>
</div>
</li>
</ul>
<p>The <em>crystal</em> volume is placed at 10 cm, 0 cm and 0 cm from the center
of its mother volume and will rotate around the Z axis during the
simulation with a speed of 10 degrees per second.</p>
</div>
<div class="section" id="generic-move">
<h3><a class="toc-backref" href="#id36">2.5.6. Generic move</a><a class="headerlink" href="#generic-move" title="Permalink to this headline">¶</a></h3>
<p>A volume can be move at given time value thanks to the following macros:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">myvolume</span><span class="o">/</span><span class="n">moves</span><span class="o">/</span><span class="n">insert</span>                       <span class="n">genericMove</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">myvolume</span><span class="o">/</span><span class="n">genericMove</span><span class="o">/</span><span class="n">setPlacementsFilename</span>  <span class="n">data</span><span class="o">/</span><span class="n">myvolume</span><span class="o">.</span><span class="n">placements</span>
</pre></div>
</div>
<p>In the same idea than <a class="reference internal" href="#generic-repeater-label"><span class="std std-ref">Generic repeater</span></a>, the
placements file contains the transformations (rotation, translation) and
the time value where this transformations is applied:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="c1">###### List of placement (translation and rotation) according to time</span>
<span class="c1">###### Column 1      is Time in s (second)</span>
<span class="c1">###### Column 2      is rotationAngle in degree</span>
<span class="c1">###### Columns 3,4,5 are rotation axis</span>
<span class="c1">###### Columns 6,7,8 are translation in mm</span>
<span class="n">Time</span> <span class="n">s</span>
<span class="n">Rotation</span> <span class="n">deg</span>
<span class="n">Translation</span> <span class="n">mm</span>
<span class="mi">0</span>           <span class="mi">0</span>      <span class="mi">0</span> <span class="mi">1</span> <span class="mi">0</span>      <span class="mi">0</span>  <span class="mi">0</span> <span class="mi">100</span>
<span class="mf">250.7</span>       <span class="mi">3</span>      <span class="mi">0</span> <span class="mi">1</span> <span class="mi">0</span>      <span class="mi">0</span> <span class="mi">10</span> <span class="mi">100</span>
<span class="mf">492.9</span>       <span class="mi">4</span>      <span class="mi">0</span> <span class="mi">1</span> <span class="mi">0</span>      <span class="mi">0</span> <span class="mi">20</span> <span class="mi">100</span>
<span class="mf">742.9</span>       <span class="mi">8</span>      <span class="mi">0</span> <span class="mi">1</span> <span class="mi">0</span>      <span class="mi">30</span> <span class="mi">0</span> <span class="mi">100</span>
</pre></div>
</div>
<p><em>WARNING</em>. The time values given here do not necessarily correspond to
simulation’s <em>run</em>. The real runs are defined with the time slices (see
<a class="reference internal" href="getting_started.html#eighth-step-starting-an-acquisition-label"><span class="std std-ref">Eighth step: Starting an acquisition</span></a> for example).
At each new run, GATE looks into the time-placements list and chooses
the one that corresponds to the starting time of the run. It leads that
some placements can be not applied (if one run start before the
placement time and the next run start after the next placement time). If
run time is after the last placements time in the list, the last
placements is applied.</p>
<p>See example <a class="reference internal" href="gatert.html#gatert-label"><span class="std std-ref">GateRT</span></a></p>
</div>
<div class="section" id="generic-repeater-move">
<h3><a class="toc-backref" href="#id37">2.5.7. Generic repeater move</a><a class="headerlink" href="#generic-repeater-move" title="Permalink to this headline">¶</a></h3>
<p>You can combine generic repeater and generic move to allow different
repeated configurations according to time. This is for example useful to
describe multi-leaf collimator from a single leaf which is repeated at
different positions, and which move according to each beam:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">myvolume</span><span class="o">/</span><span class="n">moves</span><span class="o">/</span><span class="n">insert</span>                               <span class="n">genericRepeaterMove</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">myvolume</span><span class="o">/</span><span class="n">genericRepeaterMove</span><span class="o">/</span><span class="n">setPlacementsFilename</span>  <span class="n">data</span><span class="o">/</span><span class="n">myvolume</span><span class="o">.</span><span class="n">placements</span>
<span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">myvolume</span><span class="o">/</span><span class="n">genericRepeaterMove</span><span class="o">/</span><span class="n">useRelativeTranslation</span> <span class="mi">1</span>

<span class="c1">###### List of placement (translation and rotation)</span>
<span class="c1">###### Column 1      is rotationAngle in degree</span>
<span class="c1">###### Columns 2,3,4 are rotation axis</span>
<span class="c1">###### Columns 5,6,7 are translation in mm</span>
<span class="n">Time</span> <span class="n">s</span>
<span class="n">NumberOfPlacements</span> <span class="mi">3</span>
<span class="n">Rotation</span> <span class="n">deg</span>
<span class="n">Translation</span> <span class="n">mm</span>
<span class="c1">#Time  # Placement 1             # Placement 2           # Placement 3</span>
<span class="mi">0</span>      <span class="mi">10</span>  <span class="mi">0</span> <span class="mi">1</span> <span class="mi">0</span>  <span class="mi">20</span>  <span class="mi">0</span>  <span class="mi">0</span>       <span class="mi">10</span> <span class="mi">0</span> <span class="mi">1</span> <span class="mi">0</span>   <span class="mi">80</span>  <span class="mi">0</span>  <span class="mi">0</span>     <span class="mi">10</span> <span class="mi">0</span> <span class="mi">1</span> <span class="mi">0</span>    <span class="o">-</span><span class="mi">60</span>  <span class="mi">0</span>  <span class="mi">0</span>
<span class="mi">1</span>      <span class="mi">20</span>  <span class="mi">0</span> <span class="mi">1</span> <span class="mi">0</span>  <span class="mi">20</span> <span class="mi">10</span>  <span class="mi">0</span>       <span class="mi">20</span> <span class="mi">0</span> <span class="mi">1</span> <span class="mi">0</span>   <span class="mi">80</span> <span class="mi">10</span>  <span class="mi">0</span>     <span class="mi">20</span> <span class="mi">0</span> <span class="mi">1</span> <span class="mi">0</span>    <span class="o">-</span><span class="mi">60</span> <span class="mi">10</span>  <span class="mi">0</span>
<span class="mi">2</span>      <span class="mi">30</span>  <span class="mi">1</span> <span class="mi">1</span> <span class="mi">0</span>  <span class="mi">20</span>  <span class="mi">0</span>  <span class="mi">0</span>       <span class="mi">30</span> <span class="mi">1</span> <span class="mi">1</span> <span class="mi">0</span>   <span class="mi">80</span>  <span class="mi">0</span>  <span class="mi">0</span>     <span class="mi">30</span> <span class="mi">1</span> <span class="mi">1</span> <span class="mi">0</span>    <span class="o">-</span><span class="mi">60</span>  <span class="mi">0</span>  <span class="mi">0</span>
<span class="mi">4</span>      <span class="mi">40</span>  <span class="mi">0</span> <span class="mi">1</span> <span class="mi">1</span>  <span class="mi">20</span>  <span class="mi">0</span> <span class="mi">40</span>       <span class="mi">40</span> <span class="mi">0</span> <span class="mi">1</span> <span class="mi">1</span>   <span class="mi">80</span>  <span class="mi">0</span> <span class="mi">40</span>     <span class="mi">40</span> <span class="mi">0</span> <span class="mi">1</span> <span class="mi">1</span>    <span class="o">-</span><span class="mi">60</span>  <span class="mi">0</span> <span class="mi">40</span>
</pre></div>
</div>
<p>The ‘NumberOfPlacements’ is needed to indicate how many different
repetition are performed at each motion.</p>
</div>
</div>
<div class="section" id="updating-the-geometry">
<h2><a class="toc-backref" href="#id38">2.6. Updating the geometry</a><a class="headerlink" href="#updating-the-geometry" title="Permalink to this headline">¶</a></h2>
<p>Updating the geometry is needed to take into account any change in the
geometry. It also refreshes the display window. The geometry can be
updated with the following command:</p>
<div class="highlight-default notranslate"><div class="highlight"><pre><span></span><span class="o">/</span><span class="n">gate</span><span class="o">/</span><span class="n">geometry</span><span class="o">/</span><span class="n">rebuild</span>
</pre></div>
</div>
</div>
</div>


          </div>
              <div class="related bottom">
                &nbsp;
  <nav id="rellinks">
    <ul>
        <li>
          &larr;
          <a href="getting_started.html" title="Previous document">1. Getting Started</a>
        </li>
        <li>
          <a href="materials.html" title="Next document">3. Materials</a>
          &rarr;
        </li>
    </ul>
  </nav>
              </div>
          
        </div>
      </div>
    <div class="clearer"></div>
  </div>
    <div class="footer">
      &copy;2021, OpenGATE Collaboration. Updated on May 17, 2021.
      
      |
      Powered by <a href="http://sphinx-doc.org/">Sphinx 1.8.5</a>
      &amp; <a href="https://github.com/bitprophet/alabaster">Alabaster 0.7.12</a>
      
      |
      <a href="_sources/defining_a_geometry.rst.txt"
          rel="nofollow">Page source</a>
    </div>

    

    
    <script type="text/javascript">

      var _gaq = _gaq || [];
      _gaq.push(['_setAccount', 'UA-138643878-1']);
      _gaq.push(['_setDomainName', 'none']);
      _gaq.push(['_setAllowLinker', true]);
      _gaq.push(['_trackPageview']);

      (function() {
        var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
        ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
        var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
      })();

    </script>
    
  </body>
</html>