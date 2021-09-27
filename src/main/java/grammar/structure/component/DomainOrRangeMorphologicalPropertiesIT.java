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

public enum DomainOrRangeMorphologicalPropertiesIT {

    MASCULINE(
            List.of(
                    URI.create(DBO.getUri() + "party"),
                    URI.create(DBO.getUri() + "numberOfPages"),
                    URI.create(DBO.getUri() + "musicComposer"),
                    URI.create(DBO.getUri() + "mayor"),
                    URI.create(DBO.getUri() + "manufacturer"),
                    URI.create(DBO.getUri() + "leader"),
                    URI.create(DBO.getUri() + "ingredient"),
                    URI.create(DBO.getUri() + "ground"),
                    URI.create(DBO.getUri() + "height"),
                    URI.create(DBO.getUri() + "headquarter"),
                    URI.create(DBO.getUri() + "foundingYear"),
                    URI.create(DBO.getUri() + "parentCompany"),
                    URI.create(DBO.getUri() + "foundationPlace"),
                    URI.create(DBO.getUri() + "father"),
                    URI.create(DBO.getUri() + "foundingYear"),
                    URI.create(DBO.getUri() + "parentCompany"),
                    URI.create(DBO.getUri() + "foundationPlace"),
                    URI.create(DBO.getUri() + "father"),
                    URI.create(DBO.getUri() + "series"),
                    URI.create(DBO.getUri() + "editor"),
                    URI.create(DBO.getUri() + "doctoralAdvisor"),
                    URI.create(DBO.getUri() + "deathPlace"),
                    URI.create(DBO.getUri() + "child"),
                    URI.create(DBO.getUri() + "birthYear"),
                    URI.create(DBO.getUri() + "birthPlace"),
                    URI.create(DBO.getUri() + "movement")
            )
    ),
    FEMININE(
            List.of(
                    URI.create(DBO.getUri() + "author"),
                    URI.create(DBO.getUri() + "deathCause"),
                    URI.create(DBO.getUri() + "completionDate"),
                    URI.create(DBO.getUri() + "currency"),
                    URI.create(DBO.getUri() + "deathDate"),
                    URI.create(DBO.getUri() + "developer"),
                    URI.create(DBO.getUri() + "dissolutionDate"),
                    URI.create(DBO.getUri() + "areaUrban"),
                    URI.create(DBO.getUri() + "governmentType"),
                    URI.create(DBO.getUri() + "height"),
                    URI.create(DBO.getUri() + "presenter"),
                    URI.create(DBO.getUri() + "influenced"),
                    URI.create(DBO.getUri() + "largestCity"),
                    URI.create(DBO.getUri() + "location"),
                    URI.create(DBO.getUri() + "nationality"),
                    URI.create(DBO.getUri() + "residence")
            )
    );

    private final List<URI> references;

    DomainOrRangeMorphologicalPropertiesIT(List<URI> refs) {
        this.references = refs;
    }

    public static DomainOrRangeMorphologicalPropertiesIT getMatchingGender(URI uri) {
        return Stream.of(DomainOrRangeMorphologicalPropertiesIT.values())
                .filter(domainType -> domainType.references.stream().anyMatch(uri1 -> uri1.equals(uri)))
                .findFirst()
                .orElse(MASCULINE);
    }

    public List<URI> getReferences() {
        return references;
    }
}
