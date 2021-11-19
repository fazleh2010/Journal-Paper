package grammar.structure.component;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static grammar.sparql.Prefix.DBO;
import static grammar.sparql.Prefix.DBP;

public enum DomainOrRangeType {
    Person(
            List.of(
                    URI.create(DBO.getUri() + "Person"),
                    URI.create(DBO.getUri() + "Agent"),
                    URI.create(DBO.getUri() + "Mayor"),
                    URI.create(DBO.getUri() + "Actor"),
                    URI.create(DBO.getUri() + "Scientist"),
                    URI.create(DBO.getUri() + "Architect"),
                    URI.create(DBO.getUri() + "Artist"),
                    URI.create(DBO.getUri() + "MusicalArtist"),
                    URI.create(DBO.getUri() + "Politician"),
                    URI.create(DBO.getUri() + "Economist"),
                    URI.create(DBO.getUri() + "Cleric"),
                    URI.create(DBO.getUri() + "SoccerPlayer"),
                    URI.create(DBO.getUri() + "Skier"),
                    URI.create(DBO.getUri() + "Wrestler"),
                    URI.create(DBO.getUri() + "HandballPlayer"),
                    URI.create(DBO.getUri() + "Cyclist"),
                    URI.create(DBO.getUri() + "DartsPlayer"),
                    URI.create(DBO.getUri() + "SpeedwayRider"),
                    URI.create(DBO.getUri() + "GridironFootballPlayer"),
                    URI.create(DBO.getUri() + "MartialArtist"),
                    URI.create(DBO.getUri() + "SportsManager"),
                    URI.create(DBO.getUri() + "MilitaryPerson"),
                    URI.create(DBO.getUri() + "BeautyQueen"),
                    URI.create(DBO.getUri() + "Skater"),
                    URI.create(DBO.getUri() + "TableTennisPlayer"),
                    URI.create(DBO.getUri() + "Boxer"),
                    URI.create(DBO.getUri() + "MemberOfParliament"),
                    URI.create(DBO.getUri() + "AmericanFootballPlayer"),
                    URI.create(DBO.getUri() + "IceHockeyPlayer"),
                    URI.create(DBO.getUri() + "Model"),
                    URI.create(DBO.getUri() + "BasketballPlayer"),
                    URI.create(DBO.getUri() + "SoccerManager"),
                    URI.create(DBO.getUri() + "PrimeMinister"),
                    URI.create(DBO.getUri() + "MotorsportRacer"),
                    URI.create(DBO.getUri() + "Writer"),
                    URI.create(DBO.getUri() + "ComicsCreator"),
                    URI.create(DBO.getUri() + "ChristianBishop"),
                    URI.create(DBO.getUri() + "VolleyballPlayer"),
                    URI.create(DBO.getUri() + "Swimmer"),
                    URI.create(DBO.getUri() + "RacingDriver"),
                    URI.create(DBO.getUri() + "GolfPlayer"),
                    URI.create(DBO.getUri() + "MotorcycleRider"),
                    URI.create(DBO.getUri() + "ChessPlayer"),
                    URI.create(DBO.getUri() + "OfficeHolder"),
                    URI.create(DBO.getUri() + "Athlete"),
                    URI.create(DBO.getUri() + "FigureSkater"),
                    URI.create(DBO.getUri() + "SquashPlayer"),
                    URI.create(DBO.getUri() + "TennisPlayer"),
                    URI.create(DBO.getUri() + "WinterSportPlayer"),
                    URI.create(DBO.getUri() + "Curler"),
                    URI.create(DBO.getUri() + "Saint"),
                    URI.create(DBO.getUri() + "FictionalCharacter"),
                    URI.create(DBO.getUri() + "author"),
                    URI.create("http://www.wikidata.org/entity/Q215627") // wiki data person
            )
    ),
    Agent(List.of(URI.create(DBO.getUri() + "Agent"))),
    OfficeHolder(List.of(URI.create(DBO.getUri() + "OfficeHolder"))),
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
   

    /*PLACE(
    List.of(
      URI.create(DBO.getUri() + "Place"),
      URI.create(DBO.getUri() + "City"),
      URI.create(DBO.getUri() + "State"),
      URI.create(DBO.getUri() + "Country"),
      URI.create(DBO.getUri() + "PopulatedPlace"),
      URI.create(DBO.getUri() + "birthPlace"),
      URI.create(DBO.getUri() + "deathPlace"),
      URI.create(DBO.getUri() +  "placeOfBurial")
    )
  ),*/
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
    SportsTeam(List.of(
            URI.create(DBO.getUri() + "SportsTeam"),
            URI.create(DBO.getUri() + "HockeyTeam")
    )),
    SoccerClub(List.of(
            URI.create(DBO.getUri() + "SoccerClub")
    )),
    SportsLeague(List.of(
            URI.create(DBO.getUri() + "VolleyballLeague"),
            URI.create(DBO.getUri() + "SoccerLeague"),
            URI.create(DBO.getUri() + "SportsLeague"),
            URI.create(DBO.getUri() + "BasketballLeague"),
            URI.create(DBO.getUri() + "PoloLeague")
    )),
    Band(List.of(URI.create(DBO.getUri() + "Band"))),
    Magazine(List.of(
            URI.create(DBO.getUri() + "Magazine"),
            URI.create(DBO.getUri() + "AcademicJournal")
    )),
    Event(List.of(URI.create(DBO.getUri() + "Event"))),
    Organisation(List.of(
            URI.create(DBO.getUri() + "Organisation"),
            URI.create(DBO.getUri() + "Company")
    )),
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
    Work(List.of(
            URI.create(DBO.getUri() + "Work"),
            URI.create(DBO.getUri() + "Artwork"),
            URI.create(DBO.getUri() + "WrittenWork"),
            URI.create(DBO.getUri() + "Film"),
            URI.create(DBO.getUri() + "Book"),
            URI.create(DBO.getUri() + "Song"),
            URI.create(DBO.getUri() + "Single"),
            URI.create(DBO.getUri() + "Musical")
    )),
    Software(List.of(
            URI.create(DBO.getUri() + "Software"),
            URI.create(DBO.getUri() + "VideoGame")
    )),
    Publisher(List.of(
            URI.create(DBO.getUri() + "Book"),
            URI.create(DBO.getUri() + "Publisher")
    )),
    ProgrammingLanguage(List.of(URI.create(DBO.getUri() + "ProgrammingLanguage"))),
    Grape(List.of(URI.create(DBO.getUri() + "Grape"))),
    GovernmentType(List.of(URI.create(DBO.getUri() + "GovernmentType"))),
    Airline(List.of(URI.create(DBO.getUri() + "Airline"))),
    Airport(List.of(URI.create(DBO.getUri() + "Airport"))),
    PoliticalParty(List.of(URI.create(DBO.getUri() + "PoliticalParty"))),
    ArchitecturalStructure(List.of(
            URI.create(DBO.getUri() + "ArchitecturalStructure"),
            URI.create(DBO.getUri() + "HistoricBuilding"),
            URI.create(DBO.getUri() + "Building")
    )),
    LaunchPad(List.of(URI.create(DBO.getUri() + "LaunchPad"))),
    Museum(List.of(URI.create(DBO.getUri() + "Museum"))),
    BodyOfWater(List.of(URI.create(DBO.getUri() + "BodyOfWater"))),
    MilitaryConflict(List.of(URI.create(DBO.getUri() + "MilitaryConflict"))),
    MusicalWork(List.of(URI.create(DBO.getUri() + "MusicalWork"))),
    TelevisionShow(List.of(URI.create(DBO.getUri() + "TelevisionShow"))),
    TelevisionEpisode(List.of(URI.create(DBO.getUri() + "TelevisionEpisode"))),
    Award(List.of(URI.create(DBO.getUri() + "Award"))),
    Website(List.of(URI.create(DBO.getUri() + "Website"))),
    River(List.of(
            URI.create(DBO.getUri() + "River"),
            URI.create(DBO.getUri() + "Stream")
    )),
    Lake(List.of(URI.create(DBO.getUri() + "Lake"))),
    Bridge(List.of(URI.create(DBO.getUri() + "Bridge"))),
    School(List.of(
            URI.create(DBO.getUri() + "School"),
            URI.create(DBO.getUri() + "EducationalInstitution"),
            URI.create(DBO.getUri() + "University")
    )),
    WineRegion(List.of(URI.create(DBO.getUri() + "WineRegion"))),
    Location(List.of(
            URI.create(DBO.getUri() + "Place"),
            URI.create(DBO.getUri() + "Location"),
            URI.create(DBO.getUri() + "PopulatedPlace"),
            URI.create(DBO.getUri() + "Region"),
            URI.create(DBO.getUri() + "Country"),
            URI.create(DBO.getUri() + "City"),
            URI.create(DBO.getUri() + "WineRegion")
    )),
    Thing(List.of(URI.create(DBO.getUri() + "Thing"))),
    VideoGame(List.of(URI.create(DBO.getUri() + "VideoGame"))),
    THING(List.of(URI.create("http://www.w3.org/2002/07/owl#Thing"))); // default if no other matches
    

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
                    return THING;
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
