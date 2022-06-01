package grammar.structure.component;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static grammar.sparql.Prefix.DBO;
import static grammar.sparql.Prefix.DBP;
import static grammar.sparql.Prefix.DBR;
import static grammar.sparql.Prefix.OWL;

public enum DomainOrRangeTypeCheck {
    PersonCheck(
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
                    URI.create(DBO.getUri() + "SoccerLeague"),
                    URI.create(DBO.getUri() + "DartsPlayer"),
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
                    URI.create(DBO.getUri() + "CollegeCoach"),
                    URI.create(DBO.getUri() + "University"),
                    URI.create(DBO.getUri() + "Company"),
                    URI.create(DBO.getUri() + "Agent"),
                    URI.create(DBO.getUri() + "Organisation"),
                    URI.create(DBO.getUri() + "Scientist"),
                    URI.create(DBO.getUri() + "Architect"),
                    URI.create(DBO.getUri() + "Artist"),
                    URI.create(DBO.getUri() + "Pope"),
                    URI.create(DBO.getUri() + "Philosopher"),
                    URI.create(DBO.getUri() + "FormulaOneRacer"),
                    URI.create(DBO.getUri() + "musicComposer"),
                    URI.create(DBO.getUri() + "Surfing"),
                    URI.create(DBR.getUri() + "Publisher"),
                    URI.create(DBR.getUri() + "Broadcaster"),
                    URI.create(DBO.getUri() + "Surfer"),
                    URI.create("http://www.wikidata.org/entity/Q215627") // wiki data person
            )
    ),
    
    child(List.of(URI.create(DBO.getUri() + "child"))),
    locatedInArea(List.of(URI.create(DBO.getUri() + "locatedInArea"))),
    location(List.of(URI.create(DBO.getUri() + "location"))),
    Location(List.of(URI.create(DBO.getUri() + "Location"))),
   
    PlaceCheck(
    List.of(
      URI.create(DBO.getUri() + "Place"),
      URI.create(DBO.getUri() + "Location"),
      URI.create(DBO.getUri() + "City"),
      URI.create(DBO.getUri() + "State"),
      URI.create(DBO.getUri() + "Country"),
      URI.create(DBO.getUri() + "PopulatedPlace"),
      URI.create(DBO.getUri() +  "NaturalPlace"),
      URI.create(DBO.getUri() +  "HistoricPlace"),
      URI.create(DBO.getUri() +  "Road"),
      URI.create(DBO.getUri() +  "locatedInArea"),
      URI.create(DBO.getUri() + "country"),
      URI.create(DBO.getUri() +  "placeOfBurial"),
      URI.create(DBO.getUri() + "birthPlace"),
      URI.create(DBO.getUri() + "deathPlace")
      //URI.create(DBO.getUri() +  "routeStart")

    )
  ),
    NameCheck(List.of(
            URI.create(DBO.getUri() + "GivenName"),
            URI.create(DBO.getUri() + "Name")
    )),
    
    CauseCheck(List.of(
            URI.create(DBO.getUri() + "deathCause"),
            URI.create(DBO.getUri() + "knownFor"),
            URI.create(DBP.getUri() + "nickname")
    )),
    
    ActivityCheck(List.of(
            URI.create(DBO.getUri() + "musicComposer")
    )),
     
    dateCheck(List.of(
            URI.create("http://www.w3.org/2001/XMLSchema#date"),
            URI.create("http://www.w3.org/2001/XMLSchema#gYear"),
            URI.create("http://dbpedia.org/property/date"),
            URI.create(DBO.getUri() + "date"),
            URI.create(DBP.getUri() + "date"),
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
            URI.create(DBP.getUri() + "published"),        
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
            URI.create(DBO.getUri() + "activeYearsStartYear"),
            URI.create(DBO.getUri() + "activeYearsEndDate"),
            URI.create(DBO.getUri() + "activeYearsStartDate"),
            URI.create(DBP.getUri() + "published")
    )),
    
    GrowCheck(List.of(
            URI.create(DBO.getUri() + "growingGrape")
    )),

    
    WorkCheck(List.of(
            URI.create(DBO.getUri() + "Work"),
            URI.create(DBO.getUri() + "Artwork"),
            URI.create(DBO.getUri() + "WrittenWork"),
            URI.create(DBO.getUri() + "Film"),
            URI.create(DBO.getUri() + "Book"),
            URI.create(DBO.getUri() + "Song"),
            URI.create(DBO.getUri() + "Single"),
            URI.create(DBO.getUri() + "Musical")
    )),
    
    AmountTotalCheck(List.of(
            URI.create(DBO.getUri() + "budget"),
            URI.create(DBO.getUri() + "populationTotal"),
            URI.create(DBO.getUri() + "seatingCapacity"),
            URI.create(DBO.getUri() + "numberOfLocations"),
            URI.create("http://www.w3.org/2001/XMLSchema#double")
    )),
    
    AmountThingCheck(List.of(
            //URI.create(DBO.getUri() + "language")
            URI.create(DBO.getUri() + "numberOfPages")
    )),
    
    
    OccupationCheck(List.of(
            URI.create(DBO.getUri() + "Actor"),
            URI.create(DBO.getUri() + "Surfing")
    )),
    
    
    ArchitecturalStructureCheck(List.of(
            URI.create(DBO.getUri() + "ArchitecturalStructure"),
            URI.create(DBO.getUri() + "HistoricBuilding"),
            URI.create(DBO.getUri() + "Building"),
             URI.create(DBO.getUri() + "Stadium")
    )),
   
    RiverCheck(List.of(
            URI.create(DBO.getUri() + "River"),
            URI.create(DBO.getUri() + "Stream")
    )),
    SchoolCheck(List.of(
            URI.create(DBO.getUri() + "School"),
            URI.create(DBO.getUri() + "EducationalInstitution"),
            URI.create(DBO.getUri() + "University")
    )),
    RdfsLabel(List.of(
            URI.create(DBO.getUri() + "School"),
            URI.create(DBO.getUri() + "EducationalInstitution"),
            URI.create(DBO.getUri() + "University")
    )),
    LabelCheck(List.of(
            URI.create(DBO.getUri() + "abbreviation")
    )),
    ClassTypeCheck(List.of(
            URI.create(DBO.getUri() + "LaunchPad"),
            URI.create(DBO.getUri() + "Country"),
            URI.create(DBO.getUri() + "Film"),
            URI.create(DBO.getUri() + "City"),
            URI.create(DBO.getUri() + "TelevisionShow")
    )),
    
    ThingCheck(List.of(
           URI.create(OWL.getUri() + "Thing")
    )),
    
    occupation(List.of(URI.create(DBO.getUri() + "occupation"))),
    Thing(List.of(URI.create("http://www.w3.org/2002/07/owl#Thing"))); // default if no other matches
    

    public static final List<URI> MISSING_TYPES = new ArrayList<>();

    public static String getUri(DomainOrRangeTypeCheck domainOrRangeTypeCheck) {
        return DBO.getUri() + domainOrRangeTypeCheck.name();
    }

    public static String getResourceUri(String lastSegment) {
        return DBR.getUri()+lastSegment;
    }
    private final List<URI> references;

    DomainOrRangeTypeCheck(List<URI> refs) {
        this.references = refs;
    }

    public static DomainOrRangeTypeCheck getMatchingType(URI uri) {
        return Stream.of(DomainOrRangeTypeCheck.values())
                .filter(domainType -> domainType.references.stream().anyMatch(uri1 -> uri1.equals(uri)))
                .findFirst()
                .orElseGet(() -> {
                    if (!uri.toString().isBlank() && !MISSING_TYPES.contains(uri)) {
                        MISSING_TYPES.add(uri);
                    }
                    return Thing;
                });
    }

    public static List<DomainOrRangeTypeCheck> getAllAlternativeTypes(DomainOrRangeTypeCheck domainOrRangeType) {
        return Stream.of(DomainOrRangeTypeCheck.values())
                .filter(domainType -> domainType.references.stream().anyMatch(domainOrRangeType.references::contains))
                .collect(Collectors.toList());
    }

    public List<URI> getReferences() {
        return references;
    }

}
