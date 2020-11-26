package de.fhdw.models;

import de.fhdw.TestHelper;
import de.fhdw.util.SysInit;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.transaction.Transactional;

@QuarkusTest
public class ShopOrderArticleTest {


    @BeforeAll
    @Transactional
    static void beforeAll() {
        TestHelper testHelper = new TestHelper();
        testHelper.emptyDatabase();

        SysInit sysInit = new SysInit();
        sysInit.lazyDemoData = true;
        sysInit.initDemoData();
    }

    @Test
    @TestTransaction
    void create() {
        Article article = Article.findById(1L);
        ShopOrderArticle shopOrderArticle = new ShopOrderArticle(article);
        shopOrderArticle.persist();
    }

}
