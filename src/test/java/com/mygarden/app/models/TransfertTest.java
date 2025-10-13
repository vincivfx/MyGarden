package com.mygarden.app.models;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TransferTest {

    @Test
    void testEmptyConstructor() {
        Transfer transfer = new Transfer();
        assertNotNull(transfer); // juste pour vérifier que l'objet peut être créé
    }

    @Test
    void testConstructorWithOptionals() {
        User user = new User();
        ShopItem shopItem = ShopItem.create("Id1", "Rose", 10, 1);
        Challenge challenge = new Challenge(1, "Daily watering", "daily", "Use a watering can");

        Transfer transfer1 = new Transfer(user, 50, Optional.of(shopItem), Optional.empty());
        assertEquals(50, transfer1.getAmount());

        Transfer transfer2 = new Transfer(user, 100, Optional.empty(), Optional.of(challenge));
        assertEquals(100, transfer2.getAmount());
    }

    @Test
    void testCreateTransferChallenge() {
        User user = new User();
        Challenge challenge = new Challenge(1, "Daily watering", "daily", "Tip");
        Transfer transfer = Transfer.createTransferChallenge(user, challenge);

        assertEquals(challenge.getPoints(), transfer.getAmount());
        // Le shopItemId doit être null
        assertNull(transfer.getShopItemId());
        assertNotNull(transfer.getChallengeId());
    }

    @Test
    void testCreateTransferBought() {
        User user = new User();
        ShopItem shopItem = ShopItem.create("Id1", "Rose", 10, 1);
        Transfer transfer = Transfer.createTransferBought(user, shopItem);

        assertEquals(-shopItem.getPrice(), transfer.getAmount());
        assertNotNull(transfer.getShopItemId());
        assertNull(transfer.getChallengeId());
    }
}
