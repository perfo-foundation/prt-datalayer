package space.eignatik.prt.datalayer.dataTools.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import space.eignatik.prt.datalayer.dataTools.entities.Role;
import space.eignatik.prt.datalayer.dataTools.session.SessionFactoryUtilLocal;

import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static space.eignatik.prt.datalayer.dataTools.enums.TableNames.ROLE;

public class RoleDAOTest extends BaseDAOTest {

    @Autowired
    @Qualifier(value = "roleDAO")
    private IDAO<Role> dao;

    @BeforeClass
    public void setUpDB() {
        dao.setFactoryUtil(new SessionFactoryUtilLocal());
    }

    @BeforeMethod
    public void resetDB() {
        DAOTestUtil.deleteAllFromTable(ROLE.getEntityName());
    }

    @Test
    public void testWhenAddWithItem_thenItCanBeClaimedFromDB() {
        Role role = new Role().setRole("admin");
        dao.add(role);
        Role roleDB = dao.get(Role.class, role.getId());

        assertEquals(roleDB, role);
    }

    @Test
    public void testWhenDelete_thenDeletedItemReturned_and_ItIsUnableToClaimFromDB() {
        Role role = new Role().setRole("admin");
        dao.add(role);
        assertEquals(dao.delete(Role.class, role.getId()), role);
        assertNull(dao.get(Role.class, role.getId()));
    }

    @Test
    public void testWhenGetAll_thenFixedSizeListIsReturned() {
        int EXPECTED_SIZE = 2;
        List.of(new Role().setRole("admin"),
                new Role().setRole("user"))
                .forEach(role -> dao.add(role));
        assertEquals(dao.getAll(Role.class).size(), EXPECTED_SIZE);
    }

    @Test
    public void testWhenUpdate_thenExistingItemIsUpdatedInDB() {
        Role role = new Role().setRole("admin");
        dao.add(role);
        dao.update(role.setRole("user"));
        role = dao.get(Role.class, role.getId());
        assertEquals(role.getRole(), "user");
    }
}