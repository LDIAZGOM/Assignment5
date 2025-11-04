package org.example.Amazon;

import org.example.Amazon.Cost.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
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

    @Test
    @DisplayName("DeliveryPrice: test all branches")
    void testDeliveryPriceAllBranches() {
        DeliveryPrice delivery = new DeliveryPrice();
        List<Item> items = new ArrayList<>();

        // 0 items
        assertThat(delivery.priceToAggregate(items)).isEqualTo(0);

        // 1-3 items
        items.add(new Item(ItemType.OTHER,"Item1",1,10));
        assertThat(delivery.priceToAggregate(items)).isEqualTo(5);

        // 4-10 items
        items.clear();
        for(int i=0;i<5;i++) items.add(new Item(ItemType.OTHER,"Item"+i,1,10));
        assertThat(delivery.priceToAggregate(items)).isEqualTo(12.5);

        // >10 items
        items.clear();
        for(int i=0;i<11;i++) items.add(new Item(ItemType.OTHER,"Item"+i,1,10));
        assertThat(delivery.priceToAggregate(items)).isEqualTo(20.0);
    }

    @Test
    @DisplayName("ExtraCostForElectronics: tests electronics vs other")
    void testExtraCostForElectronics() {
        ExtraCostForElectronics extra = new ExtraCostForElectronics();
        List<Item> cart = new ArrayList<>();

        // no electronics
        assertThat(extra.priceToAggregate(cart)).isEqualTo(0);

        // has electronics
        cart.add(new Item(ItemType.ELECTRONIC, "Laptop", 1, 1000));
        assertThat(extra.priceToAggregate(cart)).isEqualTo(7.5);
    }

    @Test
    @DisplayName("Database: close method")
    void testDatabaseClose() {
        db.close();
    }
}