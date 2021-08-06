package com.example.GrpSB.model;
import javax.persistence.*;

@Entity
@Table(name = "alias_title", indexes = {@Index(name = "idx_movie_alias_name", columnList = "alias_title")})
@IdClass(AliasCompositeKey.class)
public class MovieAlias {


    @Column(name = "title_id")
    @Id
    private String titleId;

    @Column(name = "ordering")
    @Id
    private Integer ordering;

    @Column(name = "alias_title")
    private String aliasTitle;

    @Column(name = "is_original")
    private String isOriginal;

    public String getTitleId() {
        return titleId;
    }

    public void setTitleId(String titleId) {
        this.titleId = titleId;
    }

    public Integer getOrdering() {
        return ordering;
    }

    public void setOrdering(Integer ordering) {
        this.ordering = ordering;
    }

    public String getAliasTitle() {
        return aliasTitle;
    }

    public void setAliasTitle(String aliasTitle) {
        this.aliasTitle = aliasTitle;
    }

    public String getIsOriginal() {
        return isOriginal;
    }

    public void setIsOriginal(String isOriginal) {
        this.isOriginal = isOriginal;
    }
}
