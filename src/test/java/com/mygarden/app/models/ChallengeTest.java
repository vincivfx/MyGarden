package com.mygarden.app.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

class ChallengeTest {

    @Test
    void testEmptyConstructor() {
        Challenge challenge = new Challenge();
        assertNotNull(challenge); // Vérifie que l'objet se crée
    }

    @Test
    void testParameterizedConstructorAndGetters() {
        Challenge challenge = new Challenge(1, "Water plants", "daily", "Use a watering can");

        assertEquals(1, challenge.getChallengeId());
        assertEquals("Water plants", challenge.getDescription());
        assertEquals("daily", challenge.getType());
        assertEquals("Use a watering can", challenge.getTip()); // tip n'a pas de getter -> on peut le rendre public temporairement ou ajouter un getter
    }

    @Test
    void testGetPointsDefault() {
        Challenge challenge = new Challenge();
        assertEquals(0, challenge.getPoints()); // points n'est jamais initialisé, donc par défaut 0
    }
}
