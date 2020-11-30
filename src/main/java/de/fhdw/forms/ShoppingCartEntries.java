package de.fhdw.forms;


import de.fhdw.models.Article;

public class ShoppingCartEntries {

    public Article article;

    public int count;

    public ShoppingCartEntries(Article article, int count) {
        this.article = article;
        this.count = count;
    }

    public ShoppingCartEntries() {
    }

    @Override
    public String toString() {
        return "ShoppingCartEntries{" +
                "article=" + article +
                ", count=" + count +
                '}';
    }
}
