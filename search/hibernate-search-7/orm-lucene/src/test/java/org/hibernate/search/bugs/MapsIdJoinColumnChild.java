package org.hibernate.search.bugs;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import org.hibernate.search.engine.backend.types.Projectable;
import org.hibernate.search.mapper.pojo.automaticindexing.ReindexOnUpdate;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.AssociationInverseSide;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.DocumentId;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexingDependency;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.ObjectPath;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.PropertyValue;

import java.util.Set;

@Entity
@Indexed
public class MapsIdJoinColumnChild {

    @Id
    @DocumentId
    private Long id;

    @MapsId
    // Remove @JoinColumn out to make the tests pass
    @JoinColumn(name = "PARENT_ID_CUSTOM_NAME")
    @OneToOne
    @IndexedEmbedded
    @AssociationInverseSide(inversePath = @ObjectPath(@PropertyValue(propertyName = "mapsIdJoinColumnChild")))
    private Parent parent;

    @FullTextField(analyzer = "nameAnalyzer", projectable = Projectable.YES)
    @Column(name = "NAME")
    private String name;

    @ManyToMany
    @IndexingDependency(reindexOnUpdate = ReindexOnUpdate.SHALLOW)
    @IndexedEmbedded
    private Set<Tag> tags;

    protected MapsIdJoinColumnChild() {
    }

    public MapsIdJoinColumnChild(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public MapsIdJoinColumnChild(Parent parent, String name, Set<Tag> tags) {
        this.id = parent.getId();
        this.parent = parent;
        this.name = name;
        this.tags = tags;
    }

    public Long getId() {
        return id;
    }

    public Parent getParent() {
        return parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

}
