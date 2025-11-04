package org.example.Barnes;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BarnesAndNobleTest {

    @Test
    @DisplayName("specification-based: Should return null when order is null")
    void testReturnNullWhenOrderIsNull() {
        BookDatabase bookDatabase = mock(BookDatabase.class);
        BuyBookProcess buyProcess = mock(BuyBookProcess.class);
        BarnesAndNoble store = new BarnesAndNoble(bookDatabase, buyProcess);

        PurchaseSummary result = store.getPriceForCart(null);

        assertNull(result);
    }

    @Test
    @DisplayName("specification-based: Should purchase full quantity when in stock")
    void testPurchaseQuantityInStock() {
        BookDatabase db = mock(BookDatabase.class);
        BuyBookProcess buyProcess = mock(BuyBookProcess.class);
        Book book = new Book("123", 10, 5);
        when(db.findByISBN("123")).thenReturn(book);

        BarnesAndNoble store = new BarnesAndNoble(db, buyProcess);
        Map<String, Integer> order = new HashMap<>();
        order.put("123", 3);

        PurchaseSummary summary = store.getPriceForCart(order);
        assertEquals(30, summary.getTotalPrice());
        verify(buyProcess).buyBook(book, 3);
    }

    @Test
    @DisplayName("specification-based: Should record unavailable quantity when requesting more than in stock")
    void testUnavailableQuantityAdded() {
        BookDatabase db = mock(BookDatabase.class);
        BuyBookProcess buyProcess = mock(BuyBookProcess.class);

        Book book = new Book("123", 10, 2);
        when(db.findByISBN("123")).thenReturn(book);

        BarnesAndNoble store = new BarnesAndNoble(db, buyProcess);
        Map<String, Integer> order = new HashMap<>();
        order.put("123", 5);

        PurchaseSummary summary = store.getPriceForCart(order);

        // 2 books purchased, 3 unavailable
        assertEquals(20, summary.getTotalPrice());
        assertEquals(3, summary.getUnavailable().get(book));
        verify(buyProcess).buyBook(book, 2);
    }

    @Test
    @DisplayName("structural-based: Loop should handle multiple items")
    void testMultipleBooksInOrder() {
        BookDatabase db = mock(BookDatabase.class);
        BuyBookProcess buyProcess = mock(BuyBookProcess.class);

        Book book1 = new Book("111", 15, 2);
        Book book2 = new Book("222", 20, 1);

        when(db.findByISBN("111")).thenReturn(book1);
        when(db.findByISBN("222")).thenReturn(book2);

        BarnesAndNoble store = new BarnesAndNoble(db, buyProcess);
        Map<String, Integer> order = new HashMap<>();
        order.put("111", 2);
        order.put("222", 1);

        PurchaseSummary summary = store.getPriceForCart(order);

        assertEquals(50, summary.getTotalPrice());
        verify(buyProcess).buyBook(book1, 2);
        verify(buyProcess).buyBook(book2, 1);
    }

    @Test
    @DisplayName("structural-based: When quantity available equals requested, no unavailable quantity added")
    void testNoUnavailableWhenExactQuantity() {
        BookDatabase db = mock(BookDatabase.class);
        BuyBookProcess buyProcess = mock(BuyBookProcess.class);

        Book book = new Book("ABC", 12, 4);
        when(db.findByISBN("ABC")).thenReturn(book);

        BarnesAndNoble store = new BarnesAndNoble(db, buyProcess);
        Map<String, Integer> order = new HashMap<>();
        order.put("ABC", 4);

        PurchaseSummary summary = store.getPriceForCart(order);

        assertEquals(48, summary.getTotalPrice());
        assertTrue(summary.getUnavailable().isEmpty());
        verify(buyProcess).buyBook(book, 4);
    }

    @Test
    @DisplayName("structural-based: Multiple unavailable books")
    void testMultipleUnavailableBooks() {
        BookDatabase db = mock(BookDatabase.class);
        BuyBookProcess buyProcess = mock(BuyBookProcess.class);

        Book book1 = new Book("X1", 5, 1);
        Book book2 = new Book("X2", 10, 0);

        when(db.findByISBN("X1")).thenReturn(book1);
        when(db.findByISBN("X2")).thenReturn(book2);

        BarnesAndNoble store = new BarnesAndNoble(db, buyProcess);
        Map<String, Integer> order = new HashMap<>();
        order.put("X1", 3);
        order.put("X2", 2);

        PurchaseSummary summary = store.getPriceForCart(order);

        // totalPrice = 1*5 + 0*10 = 5
        assertEquals(5, summary.getTotalPrice());
        assertEquals(2, summary.getUnavailable().get(book1));
        assertEquals(2, summary.getUnavailable().get(book2));
        verify(buyProcess).buyBook(book1, 1);
        verify(buyProcess).buyBook(book2, 0);
    }
}