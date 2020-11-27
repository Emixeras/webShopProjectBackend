package de.fhdw.models;



public class ShoppingCartEntries {

    public Article article;

    public int count;

    public ShoppingCartEntries(Article article, int count) {
        this.article = article;
        this.count = count;
    }

    public ShoppingCartEntries() {
    }
}
