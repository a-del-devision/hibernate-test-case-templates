package org.hibernate.search.bugs;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Set;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;

import org.junit.jupiter.api.Test;

public class YourIT extends SearchTestBase {

    private static final int FETCH_SIZE = 20;

    @Override
    public Class<?>[] getAnnotatedClasses() {
        return new Class<?>[]{MapsIdJoinColumnChild.class, MapsIdChild.class, JoinColumnChild.class, Tag.class, Parent.class};
    }

    @Test
    void deleteOneToOneMapsIdJoinColumnParent() {
        // GIVEN
        long id = 1L;
        try (Session session = getSessionFactory().openSession()) {
            Tag tag = new Tag("cool");
            Parent parent = new Parent(id, "Parent");
            MapsIdJoinColumnChild entity = new MapsIdJoinColumnChild(
                    parent,
                    "Entity",
                    Set.of(tag)
            );

            Transaction transaction = session.beginTransaction();
            session.persist(tag);
            session.persist(parent);
            session.persist(entity);
            transaction.commit();
        }

        // WHEN
        try (Session session = getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Parent parent = session.get(Parent.class, id);

            MapsIdJoinColumnChild child = session.get(MapsIdJoinColumnChild.class, id);
            child.setName("to-be-deleted");
            session.persist(child);

            session.remove(parent);
            // This will throw a LazyInitializationException because of child.tags
            transaction.commit();
        }

        // THEN
        try (Session session = getSessionFactory().openSession()) {
            SearchSession searchSession = Search.session(session);
            List<MapsIdJoinColumnChild> entities = searchSession
                    .search(MapsIdJoinColumnChild.class)
                    .where(f -> f.id().matching(id))
                    .fetchHits(FETCH_SIZE);
            assertThat(entities)
                    .isEmpty();

            List<Object> documents = searchSession
                    .search(MapsIdJoinColumnChild.class)
                    .select(f -> f.field("name"))
                    .where(f -> f.id().matching(id))
                    .fetchHits(FETCH_SIZE);
            assertThat(documents).isEmpty();
        }
    }

    @Test
    void deleteOneToOneMapsIdJoinColumnParentWithInitializedChild() {
        // GIVEN
        long id = 2L;
        try (Session session = getSessionFactory().openSession()) {
            Tag tag = new Tag("cool");
            Parent parent = new Parent(id, "Parent");
            MapsIdJoinColumnChild entity = new MapsIdJoinColumnChild(
                    parent,
                    "Entity",
                    Set.of(tag)
            );

            Transaction transaction = session.beginTransaction();
            session.persist(tag);
            session.persist(parent);
            session.persist(entity);
            transaction.commit();
        }

        // WHEN
        try (Session session = getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Parent parent = session.get(Parent.class, id);

            MapsIdJoinColumnChild child = session.get(MapsIdJoinColumnChild.class, id);
            child.setName("to-be-deleted");
            session.persist(child);

            session.remove(parent);
            // Initialize the lazy collection in order to overcome the LazyInitializationException
            // so that we can check if the document got deleted or not
            Hibernate.initialize(child.getTags());
            transaction.commit();
        }

        // THEN
        try (Session session = getSessionFactory().openSession()) {
            SearchSession searchSession = Search.session(session);
            List<MapsIdJoinColumnChild> entities = searchSession
                    .search(MapsIdJoinColumnChild.class)
                    .where(f -> f.id().matching(id))
                    .fetchHits(FETCH_SIZE);
            assertThat(entities)
                    .isEmpty();

            List<Object> documents = searchSession
                    .search(MapsIdJoinColumnChild.class)
                    .select(f -> f.field("name"))
                    .where(f -> f.id().matching(id))
                    .fetchHits(FETCH_SIZE);
            assertThat(documents).isEmpty();
        }
    }

    @Test
    void deleteOneToOneMapsIdParent() {
        // GIVEN
        long id = 3L;
        try (Session session = getSessionFactory().openSession()) {
            Tag tag = new Tag("cool");
            Parent parent = new Parent(id, "Parent");
            MapsIdChild entity = new MapsIdChild(
                    parent,
                    "Entity",
                    Set.of(tag)
            );

            Transaction transaction = session.beginTransaction();
            session.persist(tag);
            session.persist(parent);
            session.persist(entity);
            transaction.commit();
        }

        // WHEN
        try (Session session = getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Parent parent = session.get(Parent.class, id);

            MapsIdChild child = session.get(MapsIdChild.class, id);
            child.setName("to-be-deleted");
            session.persist(child);
            session.remove(parent);

            transaction.commit();
        }

        // THEN
        try (Session session = getSessionFactory().openSession()) {
            SearchSession searchSession = Search.session(session);
            List<MapsIdChild> entities = searchSession
                    .search(MapsIdChild.class)
                    .where(f -> f.id().matching(id))
                    .fetchHits(FETCH_SIZE);
            assertThat(entities)
                    .isEmpty();

            List<Object> documents = searchSession
                    .search(MapsIdChild.class)
                    .select(f -> f.field("name"))
                    .where(f -> f.id().matching(id))
                    .fetchHits(FETCH_SIZE);
            assertThat(documents).isEmpty();
        }
    }

    @Test
    void deleteOneToOneJoinColumnParent() {
        // GIVEN
        long id = 4L;
        try (Session session = getSessionFactory().openSession()) {
            Tag tag = new Tag("cool");
            Parent parent = new Parent(id, "Parent");
            JoinColumnChild entity = new JoinColumnChild(
                    parent,
                    "Entity",
                    Set.of(tag)
            );

            Transaction transaction = session.beginTransaction();
            session.persist(tag);
            session.persist(parent);
            session.persist(entity);
            transaction.commit();
        }

        // WHEN
        try (Session session = getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Parent parent = session.get(Parent.class, id);

            JoinColumnChild child = session.get(JoinColumnChild.class, id);
            child.setName("to-be-deleted");
            session.persist(child);

            session.remove(parent);
            transaction.commit();
        }

        // THEN
        try (Session session = getSessionFactory().openSession()) {
            SearchSession searchSession = Search.session(session);
            List<JoinColumnChild> entities = searchSession
                    .search(JoinColumnChild.class)
                    .where(f -> f.id().matching(id))
                    .fetchHits(FETCH_SIZE);
            assertThat(entities)
                    .isEmpty();

            List<Object> documents = searchSession
                    .search(JoinColumnChild.class)
                    .select(f -> f.field("name"))
                    .where(f -> f.id().matching(id))
                    .fetchHits(FETCH_SIZE);
            assertThat(documents).isEmpty();
        }
    }
}
