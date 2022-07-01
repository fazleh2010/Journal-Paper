package grammar.structure.component;

import static grammar.sparql.Prefix.AIFD;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static grammar.sparql.Prefix.DBO;
import static grammar.sparql.Prefix.DBP;
import static grammar.sparql.Prefix.DBR;
import static java.lang.System.exit;

public enum DomainOrRangeType {
    Person(List.of(URI.create(DBO.getUri() + "Person"))),
    Agent(List.of(URI.create(DBO.getUri() + "Agent"))),
    Mayor(List.of(URI.create(DBO.getUri() + "Mayor"))),
    Actor(List.of(URI.create(DBO.getUri() + "Actor"))),
    Scientist(List.of(URI.create(DBO.getUri() + "Scientist"))),
    Architect(List.of(URI.create(DBO.getUri() + "Architect"))),
    Artist(List.of(URI.create(DBO.getUri() + "Artist"))),
    MusicalArtist(List.of(URI.create(DBO.getUri() + "MusicalArtist"))),
    Politician(List.of(URI.create(DBO.getUri() + "Politician"))),
    Economist(List.of(URI.create(DBO.getUri() + "Economist"))),
    Cleric(List.of(URI.create(DBO.getUri() + "Cleric"))),
    SoccerPlayer(List.of(URI.create(DBO.getUri() + "SoccerPlayer"))),
    Skier(List.of(URI.create(DBO.getUri() + "Skier"))),
    Wrestler(List.of(URI.create(DBO.getUri() + "Wrestler"))),
    HandballPlayer(List.of(URI.create(DBO.getUri() + "HandballPlayer"))),
    Cyclist(List.of(URI.create(DBO.getUri() + "Cyclist"))),
    DartsPlayer(List.of(URI.create(DBO.getUri() + "DartsPlayer"))),
    SpeedwayRider(List.of(URI.create(DBO.getUri() + "SpeedwayRider"))),
    GridironFootballPlayer(List.of(URI.create(DBO.getUri() + "GridironFootballPlayer"))),
    MartialArtist(List.of(URI.create(DBO.getUri() + "MartialArtist"))),
    SportsManager(List.of(URI.create(DBO.getUri() + "SportsManager"))),
    MilitaryPerson(List.of(URI.create(DBO.getUri() + "MilitaryPerson"))),
    BeautyQueen(List.of(URI.create(DBO.getUri() + "BeautyQueen"))),
    Skater(List.of(URI.create(DBO.getUri() + "Skater"))),
    TableTennisPlayer(List.of(URI.create(DBO.getUri() + "TableTennisPlayer"))),
    Boxer(List.of(URI.create(DBO.getUri() + "Boxer"))),
    MemberOfParliament(List.of(URI.create(DBO.getUri() + "MemberOfParliament"))),
    AmericanFootballPlayer(List.of(URI.create(DBO.getUri() + "AmericanFootballPlayer"))),
    IceHockeyPlayer(List.of(URI.create(DBO.getUri() + "IceHockeyPlayer"))),
    Model(List.of(URI.create(DBO.getUri() + "Model"))),
    BasketballPlayer(List.of(URI.create(DBO.getUri() + "BasketballPlayer"))),
    SoccerManager(List.of(URI.create(DBO.getUri() + "SoccerManager"))),
    PrimeMinister(List.of(URI.create(DBO.getUri() + "PrimeMinister"))),
    MotorsportRacer(List.of(URI.create(DBO.getUri() + "MotorsportRacer"))),
    Writer(List.of(URI.create(DBO.getUri() + "Writer"))),
    ComicsCreator(List.of(URI.create(DBO.getUri() + "ComicsCreator"))),
    ChristianBishop(List.of(URI.create(DBO.getUri() + "ChristianBishop"))),
    VolleyballPlayer(List.of(URI.create(DBO.getUri() + "VolleyballPlayer"))),
    Swimmer(List.of(URI.create(DBO.getUri() + "Swimmer"))),
    RacingDriver(List.of(URI.create(DBO.getUri() + "RacingDriver"))),
    GolfPlayer(List.of(URI.create(DBO.getUri() + "GolfPlayer"))),
    MotorcycleRider(List.of(URI.create(DBO.getUri() + "MotorcycleRider"))),
    ChessPlayer(List.of(URI.create(DBO.getUri() + "ChessPlayer"))),
    OfficeHolder(List.of(URI.create(DBO.getUri() + "OfficeHolder"))),
    Athlete(List.of(URI.create(DBO.getUri() + "Athlete"))),
    FigureSkater(List.of(URI.create(DBO.getUri() + "FigureSkater"))),
    TennisPlayer(List.of(URI.create(DBO.getUri() + "TennisPlayer"))),
    WinterSportPlayer(List.of(URI.create(DBO.getUri() + "WinterSportPlayer"))),
    Curler(List.of(URI.create(DBO.getUri() + "Curler"))),
    Saint(List.of(URI.create(DBO.getUri() + "Saint"))),
    FictionalCharacter(List.of(URI.create(DBO.getUri() + "FictionalCharacter"))),
    Country(List.of(URI.create(DBO.getUri() + "Country"))),
    State(List.of(URI.create(DBO.getUri() + "State"))),
    PopulatedPlace(List.of(URI.create(DBO.getUri() + "PopulatedPlace"))),
    Place(List.of(URI.create(DBO.getUri() + "Place"))),
    Ship(List.of(URI.create(DBO.getUri() + "Ship"))),
    MeanOfTransportation(List.of(URI.create(DBO.getUri() + "MeanOfTransportation"))),
    Mountain(List.of(URI.create(DBO.getUri() + "Mountain"))),
    NaturalPlace(List.of(URI.create(DBO.getUri() + "NaturalPlace"))),
    ChemicalCompound(List.of(URI.create(DBO.getUri() + "ChemicalCompound"))),
    Protein(List.of(URI.create(DBO.getUri() + "Protein"))),
    Biomolecule(List.of(URI.create(DBO.getUri() + "Biomolecule"))),
    Lighthouse(List.of(URI.create(DBO.getUri() + "Lighthouse"))),
    CollegeCoach(List.of(URI.create(DBO.getUri() + "CollegeCoach"))),
    IceHockeyLeague(List.of(URI.create(DBO.getUri() + "IceHockeyLeague"))),
    Tower(List.of(URI.create(DBO.getUri() + "Tower"))),
    Language(List.of(URI.create(DBO.getUri() + "Language"))),
    Road(List.of(URI.create(DBO.getUri() + "Road"))),
    Infrastructure(List.of(URI.create(DBO.getUri() + "Infrastructure"))),
    RouteOfTransportation(List.of(URI.create(DBO.getUri() + "RouteOfTransportation"))),
    Game(List.of(URI.create(DBO.getUri() + "Game"))),
    CelestialBody(List.of(URI.create(DBO.getUri() + "CelestialBody"))),
    Planet(List.of(URI.create(DBO.getUri() + "Planet"))),
    Publisher(List.of(URI.create(DBO.getUri() + "Publisher"))),
    Loyalty(List.of(URI.create(DBO.getUri() + "Loyalty"))),
    Color(List.of(URI.create(DBO.getUri() + "Color"))),
    ComicsCharacter(List.of(URI.create(DBO.getUri() + "ComicsCharacter"))),
    Legislature(List.of(URI.create(DBO.getUri() + "Legislature"))),
    Royalty(List.of(URI.create(DBO.getUri() + "Royalty"))),
    Disease(List.of(URI.create(DBO.getUri() + "Disease"))),
    Colour(List.of(URI.create(DBO.getUri() + "Colour"))),
    EthnicGroup(List.of(URI.create(DBO.getUri() + "EthnicGroup"))),
    Pope(List.of(URI.create(DBO.getUri() + "Pope"))),
    WineRegion(List.of(URI.create(DBO.getUri() + "WineRegion"))),
    Grape(List.of(URI.create(DBO.getUri() + "Grape"))),
    Philosopher(List.of(URI.create(DBO.getUri() + "Philosopher"))),   
    HockeyTeam(List.of(URI.create(DBO.getUri() + "HockeyTeam"))),   
    HistoricPlace(List.of(URI.create(DBO.getUri() + "HistoricPlace"))), 
    FormulaOneRacer(List.of(URI.create(DBO.getUri() + "HistoricPlace"))),
    Stadium(List.of(URI.create(DBO.getUri() + "Stadium"))),
    SportsClub(List.of(URI.create(DBO.getUri() + "SportsClub"))),
    Surfing(List.of(URI.create(DBO.getUri() + "Surfing"))),
    Surfer(List.of(URI.create(DBO.getUri() + "Surfer"))),
    Brewery(List.of(URI.create(DBR.getUri() + "Brewery"))),

  
    Name(List.of(
            URI.create(DBO.getUri() + "GivenName"),
            URI.create(DBO.getUri() + "Name")
    )),
    date(List.of(
            URI.create("http://www.w3.org/2001/XMLSchema#date"),
            URI.create("http://www.w3.org/2001/XMLSchema#gYear"),
            URI.create(DBO.getUri() + "date"),
            URI.create(DBO.getUri() + "draftYear"),
            URI.create(DBO.getUri() + "buildingStartDate"),
            URI.create(DBO.getUri() + "buildingEndDate"),
            URI.create(DBO.getUri() + "completionDate"),
            URI.create(DBO.getUri() + "deathDate"),
            URI.create(DBO.getUri() + "birthDate"),
            URI.create(DBO.getUri() + "firstAirDate"),
            URI.create(DBO.getUri() + "latestReleaseDate"),
            URI.create(DBO.getUri() + "releaseDate"),
            URI.create(DBO.getUri() + "decommissioningDate"),
            URI.create(DBO.getUri() + "launchDate"),
            URI.create(DBO.getUri() + "formationDate"),
            URI.create(DBO.getUri() + "yearOfConstruction"),
            URI.create(DBO.getUri() + "firstPublicationYear"),
            URI.create(DBO.getUri() + "birthYear"),
            URI.create(DBO.getUri() + "publicationDate"),
            URI.create(DBO.getUri() + "discontinued"),
            URI.create(DBO.getUri() + "introduced"),
            URI.create(DBO.getUri() + "openingDate"),
            URI.create(DBO.getUri() + "foundingDate"),
            URI.create(DBO.getUri() + "foundingYear"),
            URI.create(DBO.getUri() + "dissolutionDate"),
            URI.create(DBO.getUri() + "demolitionDate"),
            URI.create(DBO.getUri() + "rebuildingDate"),
            URI.create(DBO.getUri() + "reopeningDate"),
            URI.create(DBO.getUri() + "openingYear"),
            URI.create(DBO.getUri() + "productionDate"),
            URI.create(DBO.getUri() + "activeYearsStartYear"),
            URI.create(DBO.getUri() + "activeYearsEndYear"),
            URI.create(DBP.getUri() + "published")
    )),
    City(List.of(URI.create(DBO.getUri() + "City"))),
    Town(List.of(URI.create(DBO.getUri() + "Town"))),
    Settlement(List.of(URI.create(DBO.getUri() + "Settlement"))),
    Village(List.of(URI.create(DBO.getUri() + "Village"))),
    AdministrativeRegion(List.of(URI.create(DBO.getUri() + "AdministrativeRegion"))),
    gYear(List.of(URI.create("http://www.w3.org/2001/XMLSchema#gYear"))),
    Currency(List.of(URI.create(DBO.getUri() + "Currency"))),
    Organisation(List.of(URI.create(DBO.getUri() + "Organisation"))),
    Company(List.of(URI.create(DBO.getUri() + "Company"))),
    SportsTeam(List.of(
            URI.create(DBO.getUri() + "SportsTeam"),
            URI.create(DBO.getUri() + "HockeyTeam")
    )),
    SoccerClub(List.of(
            URI.create(DBO.getUri() + "SoccerClub")
    )),
    SportsLeague(List.of(URI.create(DBO.getUri() + "SportsLeague"))),
    VolleyballLeague(List.of(URI.create(DBO.getUri() + "VolleyballLeague"))),
    SoccerLeague(List.of(URI.create(DBO.getUri() + "SoccerLeague"))),
    BasketballLeague(List.of(URI.create(DBO.getUri() + "BasketballLeague"))),
    PoloLeague(List.of(URI.create(DBO.getUri() + "PoloLeague"))),
    Band(List.of(URI.create(DBO.getUri() + "Band"))),
    Magazine(List.of(
            URI.create(DBO.getUri() + "Magazine"),
            URI.create(DBO.getUri() + "AcademicJournal")
    )),
    Event(List.of(URI.create(DBO.getUri() + "Event"))),
    WikicatTimeZones(List.of(URI.create("http://dbpedia.org/class/yago/WikicatTimeZones"))),
    Number(List.of(
            URI.create("http://www.w3.org/2001/XMLSchema#nonNegativeInteger"),
            URI.create("http://www.w3.org/2001/XMLSchema#double"),
            URI.create("http://www.w3.org/2001/XMLSchema#positiveInteger"),
            URI.create("http://www.w3.org/2001/XMLSchema#Integer")
    )),
    Food(List.of(URI.create(DBO.getUri() + "Food"))),
    Beverage(List.of(URI.create(DBO.getUri() + "Beverage"))),
    Film(List.of(URI.create(DBO.getUri() + "Film"))),
    Book(List.of(URI.create(DBO.getUri() + "Book"))),
    Song(List.of(URI.create(DBO.getUri() + "Song"))),
    Musical(List.of(URI.create(DBO.getUri() + "Musical"))),
    Album(List.of(URI.create(DBO.getUri() + "Album"))),
    Work(List.of(URI.create(DBO.getUri() + "Work"))),
    Artwork(List.of(URI.create(DBO.getUri() + "Artwork"))),
    WrittenWork(List.of(URI.create(DBO.getUri() + "WrittenWork"))),
    Single(List.of(URI.create(DBO.getUri() + "Single"))),
    Software(List.of(URI.create(DBO.getUri() + "Software"))),
    VideoGame(List.of(URI.create(DBO.getUri() + "VideoGame"))),
    ProgrammingLanguage(List.of(URI.create(DBO.getUri() + "ProgrammingLanguage"))),
    GovernmentType(List.of(URI.create(DBO.getUri() + "GovernmentType"))),
    Airline(List.of(URI.create(DBO.getUri() + "Airline"))),
    Airport(List.of(URI.create(DBO.getUri() + "Airport"))),
    PoliticalParty(List.of(URI.create(DBO.getUri() + "PoliticalParty"))),
    ArchitecturalStructure(List.of(URI.create(DBO.getUri() + "ArchitecturalStructure"))),
    HistoricBuilding(List.of(URI.create(DBO.getUri() + "HistoricBuilding"))),
    Building(List.of(URI.create(DBO.getUri() + "Building"))),
    LaunchPad(List.of(URI.create(DBO.getUri() + "LaunchPad"))),
    Museum(List.of(URI.create(DBO.getUri() + "Museum"))),
    BodyOfWater(List.of(URI.create(DBO.getUri() + "BodyOfWater"))),
    MilitaryConflict(List.of(URI.create(DBO.getUri() + "MilitaryConflict"))),
    MusicalWork(List.of(URI.create(DBO.getUri() + "MusicalWork"))),
    TelevisionShow(List.of(URI.create(DBO.getUri() + "TelevisionShow"))),
    TelevisionEpisode(List.of(URI.create(DBO.getUri() + "TelevisionEpisode"))),
    Award(List.of(URI.create(DBO.getUri() + "Award"))),
    Website(List.of(URI.create(DBO.getUri() + "Website"))),
    University(List.of(URI.create(DBO.getUri() + "University"))),
    EducationalInstitution(List.of(URI.create(DBO.getUri() + "EducationalInstitution"))),
    School(List.of(URI.create(DBO.getUri() + "School"))),
    River(List.of(
            URI.create(DBO.getUri() + "River"),
            URI.create(DBO.getUri() + "Stream")
    )),
    Lake(List.of(URI.create(DBO.getUri() + "Lake"))),
    Bridge(List.of(URI.create(DBO.getUri() + "Bridge"))),
    Location(List.of(URI.create(DBO.getUri() + "Location"))),
    //Thing(List.of(URI.create(DBO.getUri() + "Thing"))),
    Article(List.of(URI.create(DBO.getUri() + "Article"))),
    Play(List.of(URI.create(DBO.getUri() + "Play"))),
    Genre(List.of(URI.create(DBO.getUri() + "Genre"))),
    MountainRange(List.of(URI.create(DBO.getUri() + "MountainRange"))),
    MilitaryUnit(List.of(URI.create(DBO.getUri() + "MilitaryUnit"))),
    
    
    //biblography
    Publication(List.of(URI.create(AIFD.getUri() + "Publication"))),
    AssistantProfessor(List.of(URI.create(AIFD.getUri() + "AssistantProfessor"))),
    FullProfessor(List.of(URI.create(AIFD.getUri() + "FullProfessor"))),
    Graduate(List.of(URI.create(AIFD.getUri() + "Graduate"))),
    InCollection(List.of(URI.create(AIFD.getUri() + "InCollection"))),
    InProceedings(List.of(URI.create(AIFD.getUri() + "InProceedings"))),
    Lecturer(List.of(URI.create(AIFD.getUri() + "Lecturer"))),
    Manager(List.of(URI.create(AIFD.getUri() + "Manager"))),
    Misc(List.of(URI.create(AIFD.getUri() + "Misc"))),
    Organization(List.of(URI.create(AIFD.getUri() + "Organization"))),
    PhDStudent(List.of(URI.create(AIFD.getUri() + "PhDStudent"))),
    Proceedings(List.of(URI.create(AIFD.getUri() + "Proceedings"))),
    Project(List.of(URI.create(AIFD.getUri() + "Project"))),
    ResearchGroup(List.of(URI.create(AIFD.getUri() + "ResearchGroup"))),
    ResearchTopic(List.of(URI.create(AIFD.getUri() + "ResearchTopic"))),
    TechnicalReport(List.of(URI.create(AIFD.getUri() + "TechnicalReport"))),
    TechnicalStaff(List.of(URI.create(AIFD.getUri() + "TechnicalStaff"))),
    Undergraduate(List.of(URI.create(AIFD.getUri() + "Undergraduate"))),


    
    Thing(List.of(URI.create("http://www.w3.org/2002/07/owl#Thing"))); // default if no other matches

    public static final List<URI> MISSING_TYPES = new ArrayList<>();
    private final List<URI> references;

    DomainOrRangeType(List<URI> refs) {
        this.references = refs;
    }

    public static DomainOrRangeType getMatchingType(URI uri) {
        return Stream.of(DomainOrRangeType.values())
                .filter(domainType -> domainType.references.stream().anyMatch(uri1 -> uri1.equals(uri)))
                .findFirst()
                .orElseGet(() -> {
                    if (!uri.toString().isBlank() && !MISSING_TYPES.contains(uri)) {
                        MISSING_TYPES.add(uri);
                    }
                    return Thing;
                });
    }

    public static List<DomainOrRangeType> getAllAlternativeTypes(DomainOrRangeType domainOrRangeType) {
        return Stream.of(DomainOrRangeType.values())
                .filter(domainType -> domainType.references.stream().anyMatch(domainOrRangeType.references::contains))
                .collect(Collectors.toList());
    }

    public List<URI> getReferences() {
        return references;
    }

}
