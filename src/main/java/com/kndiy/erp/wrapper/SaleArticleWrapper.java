package com.kndiy.erp.wrapper;

import com.kndiy.erp.entities.salesCluster.SaleArticle;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class SaleArticleWrapper {

    List<SaleArticle> articleList;

    public List<SaleArticle> getArticleList() {
        return articleList;
    }

    public void setArticleList(List<SaleArticle> articleList) {
        this.articleList = articleList;
    }
}
