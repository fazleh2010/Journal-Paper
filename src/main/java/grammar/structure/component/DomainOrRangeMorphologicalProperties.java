/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grammar.structure.component;

/**
 *
 * @author elahi
 */
import java.net.URI;
import java.util.List;
import java.util.stream.Stream;

import static grammar.sparql.Prefix.DBO;

public enum DomainOrRangeMorphologicalProperties {
    MASCULINE(
            List.of(
                    URI.create(DBO.getUri() + "Mayor"),
                    URI.create(DBO.getUri() + "Agent"),
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
                    URI.create(DBO.getUri() + "Skater"),
                    URI.create(DBO.getUri() + "TableTennisPlayer"),
                    URI.create(DBO.getUri() + "Boxer"),
                    URI.create(DBO.getUri() + "IceHockeyPlayer"),
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
                    URI.create(DBO.getUri() + "GivenName"),
                    URI.create(DBO.getUri() + "Name"),
                    URI.create(DBO.getUri() + "Country"),
                    URI.create(DBO.getUri() + "SoccerClub"),
                    URI.create(DBO.getUri() + "Film"),
                    URI.create(DBO.getUri() + "Publisher"),
                    URI.create(DBO.getUri() + "Airport"),
                    URI.create(DBO.getUri() + "River"),
                    URI.create(DBO.getUri() + "Stream"),
                    URI.create(DBO.getUri() + "Lake"),
                    URI.create(DBO.getUri() + "Place"),
                    URI.create(DBO.getUri() + "Location"),
                    URI.create(DBO.getUri() + "PopulatedPlace")
            )
    ),
    FEMININE(
            List.of(
                    URI.create(DBO.getUri() + "Person"),
                    URI.create("http://www.wikidata.org/entity/Q215627"), // wiki data person
                    URI.create(DBO.getUri() + "MilitaryPerson"),
                    URI.create(DBO.getUri() + "BeautyQueen"),
                    URI.create(DBO.getUri() + "City"),
                    URI.create(DBO.getUri() + "Town"),
                    URI.create(DBO.getUri() + "Settlement"),
                    URI.create(DBO.getUri() + "Village"),
                    URI.create(DBO.getUri() + "AdministrativeRegion"),
                    URI.create(DBO.getUri() + "Currency"),
                    URI.create(DBO.getUri() + "SportsTeam"),
                    URI.create(DBO.getUri() + "HockeyTeam"),
                    URI.create(DBO.getUri() + "VolleyballLeague"),
                    URI.create(DBO.getUri() + "SoccerLeague"),
                    URI.create(DBO.getUri() + "SportsLeague"),
                    URI.create(DBO.getUri() + "BasketballLeague"),
                    URI.create(DBO.getUri() + "PoloLeague"),
                    URI.create(DBO.getUri() + "Band"),
                    URI.create(DBO.getUri() + "Magazine"),
                    URI.create(DBO.getUri() + "AcademicJournal"),
                    URI.create(DBO.getUri() + "Organisation"),
                    URI.create(DBO.getUri() + "Language"),
                    URI.create(DBO.getUri() + "Single"),
                    URI.create(DBO.getUri() + "Software"),
                    URI.create(DBO.getUri() + "ProgrammingLanguage"),
                    URI.create(DBO.getUri() + "Grape"),
                    URI.create(DBO.getUri() + "GovernmentType"),
                    URI.create(DBO.getUri() + "Airline"),
                    URI.create(DBO.getUri() + "PoliticalParty"),
                    URI.create(DBO.getUri() + "LaunchPad"),
                    URI.create(DBO.getUri() + "TelevisionShow"),
                    URI.create(DBO.getUri() + "TelevisionEpisode"),
                    URI.create(DBO.getUri() + "Award"),
                    URI.create(DBO.getUri() + "Bridge"),
                    URI.create(DBO.getUri() + "School"),
                    URI.create(DBO.getUri() + "EducationalInstitution"),
                    URI.create(DBO.getUri() + "University"),
                    URI.create(DBO.getUri() + "WineRegion"),
                    URI.create(DBO.getUri() + "Region")
            )
    ),
    NEUTER(
            List.of(
                    URI.create(DBO.getUri() + "MemberOfParliament"),
                    URI.create(DBO.getUri() + "Model"),
                    URI.create(DBO.getUri() + "Event"),
                    URI.create(DBO.getUri() + "Company"),
                    URI.create(DBO.getUri() + "Food"),
                    URI.create(DBO.getUri() + "Book"),
                    URI.create(DBO.getUri() + "Song"),
                    URI.create(DBO.getUri() + "Musical"),
                    URI.create(DBO.getUri() + "Work"),
                    URI.create(DBO.getUri() + "Artwork"),
                    URI.create(DBO.getUri() + "WrittenWork"),
                    URI.create(DBO.getUri() + "VideoGame"),
                    URI.create(DBO.getUri() + "ArchitecturalStructure"),
                    URI.create(DBO.getUri() + "HistoricBuilding"),
                    URI.create(DBO.getUri() + "Building"),
                    URI.create(DBO.getUri() + "Museum")
            )
    );

    private final List<URI> references;

    DomainOrRangeMorphologicalProperties(List<URI> refs) {
        this.references = refs;
    }

    public static DomainOrRangeMorphologicalProperties getMatchingGender(URI uri) {
        return Stream.of(DomainOrRangeMorphologicalProperties.values())
                .filter(domainType -> domainType.references.stream().anyMatch(uri1 -> uri1.equals(uri)))
                .findFirst()
                .orElse(MASCULINE);
    }

    public List<URI> getReferences() {
        return references;
    }
}
