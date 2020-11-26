package de.fhdw.models;



public class ShopOrderCart {

    public Article article;

    public int quantity;

    public ShopOrderCart(Article article, int quantity) {
        this.article = article;
        this.quantity = quantity;
    }
}
