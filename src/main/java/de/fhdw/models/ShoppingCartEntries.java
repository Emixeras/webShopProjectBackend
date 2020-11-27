package de.fhdw.models;



public class ShoppingCartEntries {

    public Article article;

    public int quantity;

    public ShoppingCartEntries(Article article, int quantity) {
        this.article = article;
        this.quantity = quantity;
    }

    public ShoppingCartEntries() {
    }
}
