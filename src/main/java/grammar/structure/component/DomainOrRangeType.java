package grammar.structure.component;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static grammar.sparql.Prefix.DBO;
import static grammar.sparql.Prefix.DBP;

public enum DomainOrRangeType {
  PERSON(
    List.of(
      URI.create(DBO.getUri() + "Person"),
      URI.create("http://www.wikidata.org/entity/Q215627"),
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
  PLACE(
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
  ),
  NAME(List.of(
    URI.create(DBO.getUri() + "GivenName"),
    URI.create(DBO.getUri() + "Name")
  )),
    DATE(List.of(
            URI.create("http://www.w3.org/2001/XMLSchema#date"),
            URI.create("http://www.w3.org/2001/XMLSchema#gYear"),
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


  COUNTRY(List.of(URI.create(DBO.getUri() + "Country"))),
  CITY(List.of(
    URI.create(DBO.getUri() + "City"),
    URI.create(DBO.getUri() + "Town"),
    URI.create(DBO.getUri() + "Settlement"),
    URI.create(DBO.getUri() + "Village"),
    URI.create(DBO.getUri() + "AdministrativeRegion")
  )),
  YEAR(List.of(URI.create("http://www.w3.org/2001/XMLSchema#gYear"))),
  CURRENCY(List.of(URI.create(DBO.getUri() + "Currency"))),
  SPORTSTEAM(List.of(
    URI.create(DBO.getUri() + "SportsTeam"),
    URI.create(DBO.getUri() + "HockeyTeam")
  )),
  SPORTSCLUB(List.of(
    URI.create(DBO.getUri() + "SoccerClub")
  )),
  SPORTSLEAGUE(List.of(
    URI.create(DBO.getUri() + "VolleyballLeague"),
    URI.create(DBO.getUri() + "SoccerLeague"),
    URI.create(DBO.getUri() + "SportsLeague"),
    URI.create(DBO.getUri() + "BasketballLeague"),
    URI.create(DBO.getUri() + "PoloLeague")
  )),
  BAND(List.of(URI.create(DBO.getUri() + "Band"))),
  MAGAZINE(List.of(
    URI.create(DBO.getUri() + "Magazine"),
    URI.create(DBO.getUri() + "AcademicJournal")
  )),
  EVENT(List.of(URI.create(DBO.getUri() + "Event"))),
  ORGANISATION(List.of(
    URI.create(DBO.getUri() + "Organisation"),
    URI.create(DBO.getUri() + "Company")
  )),
  LANGUAGE(List.of(URI.create(DBO.getUri() + "Language"))),
  TIMEZONE(List.of(URI.create("http://dbpedia.org/class/yago/WikicatTimeZones"))),
  NUMBER(List.of(
    URI.create("http://www.w3.org/2001/XMLSchema#nonNegativeInteger"),
    URI.create("http://www.w3.org/2001/XMLSchema#double"),
    URI.create("http://www.w3.org/2001/XMLSchema#positiveInteger")
  )),
  FOOD(List.of(URI.create(DBO.getUri() + "Food"))),
  BEVERAGE(List.of(URI.create(DBO.getUri() + "Beverage"))),
  FILM(List.of(URI.create(DBO.getUri() + "Film"))),
  BOOK(List.of(URI.create(DBO.getUri() + "Book"))),
  SONG(List.of(URI.create(DBO.getUri() + "Song"))),
  MUSICAL(List.of(URI.create(DBO.getUri() + "Musical"))),
  ALBUM(List.of(URI.create(DBO.getUri() + "Album"))),
  WORK(List.of(
    URI.create(DBO.getUri() + "Work"),
    URI.create(DBO.getUri() + "Artwork"),
    URI.create(DBO.getUri() + "WrittenWork"),
    URI.create(DBO.getUri() + "Film"),
    URI.create(DBO.getUri() + "Book"),
    URI.create(DBO.getUri() + "Song"),
    URI.create(DBO.getUri() + "Single"),
    URI.create(DBO.getUri() + "Musical")
  )),
  SOFTWARE(List.of(
    URI.create(DBO.getUri() + "Software"),
    URI.create(DBO.getUri() + "VideoGame")
  )),
  PUBLISHER(List.of(
    URI.create(DBO.getUri() + "Book"),
    URI.create(DBO.getUri() + "Publisher")
  )),
  PROGRAMMINGLANGUAGE(List.of(URI.create(DBO.getUri() + "ProgrammingLanguage"))),
  GRAPE(List.of(URI.create(DBO.getUri() + "Grape"))),
  GOVERNMENTTYPE(List.of(URI.create(DBO.getUri() + "GovernmentType"))),
  AIRLINE(List.of(URI.create(DBO.getUri() + "Airline"))),
  AIRPORT(List.of(URI.create(DBO.getUri() + "Airport"))),
  POLITICALPARTY(List.of(URI.create(DBO.getUri() + "PoliticalParty"))),
  BUILDING(List.of(
    URI.create(DBO.getUri() + "ArchitecturalStructure"),
    URI.create(DBO.getUri() + "HistoricBuilding"),
    URI.create(DBO.getUri() + "Building")
  )),
  LAUNCHPAD(List.of(URI.create(DBO.getUri() + "LaunchPad"))),
  MUSEUM(List.of(URI.create(DBO.getUri() + "Museum"))),
  BODYOFWATER(List.of(URI.create(DBO.getUri() + "BodyOfWater"))),
  MILITARY_CONFLICT(List.of(URI.create(DBO.getUri() + "MilitaryConflict"))),
  MUSICAL_WORK(List.of(URI.create(DBO.getUri() + "MusicalWork"))),
  TELEVISIONSHOW(List.of(URI.create(DBO.getUri() + "TelevisionShow"))),
  TELEVISIONEPISODE(List.of(URI.create(DBO.getUri() + "TelevisionEpisode"))),
  AWARD(List.of(URI.create(DBO.getUri() + "Award"))),
  WEBSITE(List.of(URI.create(DBO.getUri() + "Website"))),
  RIVER(List.of(
    URI.create(DBO.getUri() + "River"),
    URI.create(DBO.getUri() + "Stream")
  )),
  LAKE(List.of(URI.create(DBO.getUri() + "Lake"))),
  BRIDGE(List.of(URI.create(DBO.getUri() + "Bridge"))),
  SCHOOL(List.of(
    URI.create(DBO.getUri() + "School"),
    URI.create(DBO.getUri() + "EducationalInstitution"),
    URI.create(DBO.getUri() + "University")
  )),
  WINEREGION(List.of(URI.create(DBO.getUri() + "WineRegion"))),
  LOCATION(List.of(
    URI.create(DBO.getUri() + "Place"),
    URI.create(DBO.getUri() + "Location"),
    URI.create(DBO.getUri() + "PopulatedPlace"),
    URI.create(DBO.getUri() + "Region"),
    URI.create(DBO.getUri() + "Country"),
    URI.create(DBO.getUri() + "City"),
    URI.create(DBO.getUri() + "WineRegion")
  )),
  ARCHITECTURE_STRUCTURE(List.of(
    URI.create(DBO.getUri() + "ArchitecturalStructure"),
    URI.create(DBO.getUri() + "Bridge"),
    URI.create(DBO.getUri() + "Building")
  )),
  OWNEDTHING(List.of(URI.create(DBO.getUri() + "Thing"))),
  VIDEOGAME(List.of(URI.create(DBO.getUri() + "VideoGame"))),
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
                   if (!uri.toString().isBlank() && !MISSING_TYPES.contains(uri)) MISSING_TYPES.add(uri);
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
