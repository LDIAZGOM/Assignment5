package org.example.Amazon;

import org.example.Amazon.Cost.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AmazonUnitTest {

    static ShoppingCart cartMock;
    static Amazon amazon;

    @BeforeAll
    static void setup() {
        cartMock = Mockito.mock(ShoppingCart.class);
        List<PriceRule> rules = List.of(new RegularCost(), new DeliveryPrice(), new ExtraCostForElectronics());
        amazon = new Amazon(cartMock, rules);
    }

    @Test
    @DisplayName("specification-based: calculate should return 0 for empty cart")
    void testCalculateEmptyCart() {
        Mockito.when(cartMock.getItems()).thenReturn(List.of());
        double result = amazon.calculate();
        assertThat(result).isEqualTo(0);
    }

    @Test
    @DisplayName("structural-based: addToCart should call ShoppingCart.add()")
    void testAddToCartCallsCart() {
        Item item = new Item(ItemType.OTHER, "Book", 2, 10);
        amazon.addToCart(item);
        Mockito.verify(cartMock).add(item);
    }
}