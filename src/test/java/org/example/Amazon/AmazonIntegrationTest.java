package org.example.Amazon;

import org.example.Amazon.Cost.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AmazonIntegrationTest {

    Database db;
    ShoppingCartAdaptor cartAdaptor;
    Amazon amazon;

    @BeforeEach
    void setup() {
        db = new Database();
        db.resetDatabase();
        cartAdaptor = new ShoppingCartAdaptor(db);
        List<PriceRule> rules = List.of(new RegularCost(), new DeliveryPrice(), new ExtraCostForElectronics());
        amazon = new Amazon(cartAdaptor, rules);
    }

    @Test
    @DisplayName("specification-based: calculate returns correct total for multiple items")
    void testCalculateMultipleItems() {
        Item item1 = new Item(ItemType.OTHER, "Book", 2, 10);
        Item item2 = new Item(ItemType.ELECTRONIC, "Headphones", 1, 50);
        amazon.addToCart(item1);
        amazon.addToCart(item2);

        double result = amazon.calculate();

        // regularCost = 2*10 + 1*50 = 70
        // deliveryPrice = 5 (2 items)
        // extraCostForElectronics = 7.5 (electronics)
        // total = 70 + 5 + 7.5 = 82.5
        assertThat(result).isEqualTo(82.5);
    }

    @Test
    @DisplayName("structural-based: cartAdaptor stores items correctly in DB")
    void testCartAdaptorStoresItems() {
        Item item = new Item(ItemType.OTHER, "Book", 1, 10);
        cartAdaptor.add(item);

        assertThat(cartAdaptor.getItems()).hasSize(1);
        assertThat(cartAdaptor.getItems().get(0).getName()).isEqualTo("Book");
    }
}