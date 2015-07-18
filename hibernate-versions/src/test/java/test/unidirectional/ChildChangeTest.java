package test.unidirectional;
import static org.junit.Assert.assertEquals;

import java.util.Collections;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.junit.Test;

import test.BaseTest;

public class ChildChangeTest extends BaseTest {

    protected void childChange(boolean detach)
    {
        DataSource dataSource = createDataSource();
        EntityManagerFactory emf = createEntityManagerFactory(dataSource);
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        try {
            UnidirectionalParent parent = new UnidirectionalParent();
            parent.setId("parent");
            parent.setValue("parent");

            UnidirectionalChild child = new UnidirectionalChild();
            child.setId("child");
            child.setValue("child");
            parent.setChildren(Collections.<UnidirectionalChild>singleton(child));

            em.persist(parent);
            em.flush();

            assertEquals(0, parent.getVersion());
            assertEquals(0, child.getVersion());

            if (detach) {
                em.detach(parent);
            }

            // change the child
            child.setValue("changed");

            if (detach) {
            	parent = em.merge(parent);
            	child = parent.getChildren().iterator().next();
            }
            // else: the entities are still managed

            // flush the changes
            em.flush();

            assertEquals(1, child.getVersion());

            // the change to the child should trigger a version change
            // on the parent as well, because the parent is "owning" the child
            assertEquals(1, parent.getVersion());
        } finally {
            em.getTransaction().rollback();
            em.close();
        }
    }

    @Test
    public void childChangeManaged() {
    	childChange(false);
    }

    @Test
    public void childChangeDetached() {
    	childChange(true);
    }
}
