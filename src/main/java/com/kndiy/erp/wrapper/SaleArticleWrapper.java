package com.kndiy.erp.wrapper;

import com.kndiy.erp.entities.salesCluster.SaleArticle;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class SaleArticleWrapper {

    private List<SaleArticle> articleList;

}
