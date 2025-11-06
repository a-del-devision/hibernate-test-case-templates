package org.hibernate.search.bugs;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;

import org.hibernate.testing.bytecode.enhancement.BytecodeEnhancerRunner;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Use this template <b>only</b> if you need Hibernate ORM bytecode enhancement to reproduce your issue.
 */
@RunWith(BytecodeEnhancerRunner.class) // This runner enables bytecode enhancement for your test.
public class YourBytecodeEnhancedIT extends BaseCoreFunctionalTestCase {

	@Override
	public Class<?>[] getAnnotatedClasses() {
        return new Class<?>[]{MapsIdJoinColumnChild.class, MapsIdChild.class, JoinColumnChild.class, Tag.class, Parent.class};
	}

	@Test
	public void testYourBug() {
		try ( Session s = openSession() ) {
            Parent parent1 = new Parent(1L, "Smiths");
            Parent parent2 = new Parent(2L, "Does");
            MapsIdJoinColumnChild yourEntity1 = new MapsIdJoinColumnChild(parent1, "Jane Smith", Collections.emptySet());
            MapsIdJoinColumnChild yourEntity2 = new MapsIdJoinColumnChild(parent2, "John Doe", Collections.emptySet());

			Transaction tx = s.beginTransaction();
            s.persist( parent1 );
            s.persist( parent2 );
			s.persist( yourEntity1 );
			s.persist( yourEntity2 );
			tx.commit();
		}

		try ( Session session = openSession() ) {
			SearchSession searchSession = Search.session( session );

			List<MapsIdJoinColumnChild> hits = searchSession.search( MapsIdJoinColumnChild.class )
					.where( f -> f.match().field( "name" ).matching( "smith" ) )
					.fetchHits( 20 );

			assertThat( hits )
					.hasSize( 1 )
					.element( 0 ).extracting( MapsIdJoinColumnChild::getId )
					.isEqualTo( 1L );
		}
	}

	@Test
	public void testBytecodeEnhancement() {
		assertThat( MapsIdJoinColumnChild.class.getDeclaredMethods() )
				.extracting( Method::getName )
				.anyMatch( name -> name.startsWith( "$$_hibernate_" ) );
	}

}
