package com.example.javabackendtemplate.architecture;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.jupiter.api.Test;

class ArchitectureTest {

    private static final String BASE_PACKAGE = "com.example.javabackendtemplate";

    @Test
    void apiShouldNotDependOnInfrastructure() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages(BASE_PACKAGE);

        noClasses()
                .that()
                .resideInAPackage(BASE_PACKAGE + ".api..")
                .should()
                .dependOnClassesThat()
                .resideInAPackage(BASE_PACKAGE + ".infrastructure..")
                .check(importedClasses);
    }
}
