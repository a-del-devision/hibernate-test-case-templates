package org.hibernate.search.bugs;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;

@Entity
public class Parent {

    @Id
    @Column(name = "ID")
    private Long id;

    @FullTextField
    @Column(name = "PARENT_NAME")
    private String parentName;

    @OneToOne(mappedBy = "parent", cascade = CascadeType.ALL)
    private MapsIdJoinColumnChild mapsIdJoinColumnChild;

    @OneToOne(mappedBy = "parent", cascade = CascadeType.ALL)
    private MapsIdChild mapsIdChild;

    @OneToOne(mappedBy = "parent", cascade = CascadeType.ALL)
    private JoinColumnChild joinColumnChild;

    public Parent() {
    }

    public Parent(Long id, String parentName) {
        this.id = id;
        this.parentName = parentName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public MapsIdJoinColumnChild getMapsIdJoinColumnChild() {
        return mapsIdJoinColumnChild;
    }

    public void setMapsIdJoinColumnChild(MapsIdJoinColumnChild mapsIdJoinColumnChild) {
        this.mapsIdJoinColumnChild = mapsIdJoinColumnChild;
    }

    public MapsIdChild getMapsIdChild() {
        return mapsIdChild;
    }

    public void setMapsIdChild(MapsIdChild mapsIdChild) {
        this.mapsIdChild = mapsIdChild;
    }

    public JoinColumnChild getJoinColumnChild() {
        return joinColumnChild;
    }

    public void setJoinColumnChild(JoinColumnChild joinColumnChild) {
        this.joinColumnChild = joinColumnChild;
    }
}
