package org.drdevelopment.webtool.plugin;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import org.drdevelopment.webtool.exception.PluginException;
import org.drdevelopment.webtool.plugin.authentication.User;
import org.drdevelopment.webtool.plugin.UsersService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestUsersService extends AbstractWebToolTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(TestUsersService.class);
	private static final String JOHN = "John";
	private static final String PETER = "Peter";
	private static final String LINDA = "Linda";
	private static final String SANDRA = "Sandra";
	
	private UsersService usersService;

	@Before
    public void setUpDatabase() throws Exception {
		super.setUpDatabase();
		executeSQL("INSERT INTO DATA.USERS(NAME,PASSWORD,ROLES) VALUES ('John','{}','ADMIN')", hashPassword("Password"));
		executeSQL("INSERT INTO DATA.USERS(NAME,PASSWORD,ROLES) VALUES ('Linda','{}','USER')", hashPassword("Password2"));
		executeSQL("INSERT INTO DATA.USERS(NAME,PASSWORD,ROLES) VALUES ('Sandra','{}','ADMIN, USER')", hashPassword("Password3"));
		usersService = new UsersService();
	}
	
	@Test
	public void testBasicUsersService() throws NoSuchAlgorithmException, SQLException, PluginException {
		User user = usersService.getUserDetails(JOHN, JOHN);
		Assert.assertEquals(JOHN, user.getName());
		Assert.assertEquals(1, user.getRoles().size());
		Assert.assertEquals("ADMIN", user.getRoles().get(0));
	}

	@Test
	public void testAddNewUser() throws NoSuchAlgorithmException, SQLException, PluginException {
		usersService.addUser(JOHN, PETER, hashPassword("Password"), "peter@example.com", "Peter Johnson");

		User user = usersService.getUserDetails(JOHN, PETER);
		Assert.assertEquals(PETER, user.getName());
		Assert.assertEquals(1, user.getRoles().size());
		Assert.assertEquals("USER", user.getRoles().get(0));
	}	

	@Test
	public void testAddNewUserAndUserDoesNotExist() throws NoSuchAlgorithmException, SQLException{
		try {
			usersService.addUser(PETER, PETER, hashPassword("Password"), "peter@example.com", "Peter Johnson");
			Assert.assertTrue(true);
		} catch (PluginException e) {
			Assert.assertEquals("User Peter does not exist.", e.getMessage());
		}
	}	

	@Test
	public void testAddNewUserWithWrongRole() throws NoSuchAlgorithmException, SQLException{
		try {
			usersService.addUser(LINDA, PETER, hashPassword("Password"), "peter@example.com", "Peter Johnson");
			Assert.assertTrue(true);
		} catch (PluginException e) {
			Assert.assertEquals("User Linda has not role ADMIN.", e.getMessage());
		}
	}	

	@Test
	public void testAddNewUserWhichAlreadyExists() throws NoSuchAlgorithmException, SQLException{
		try {
			usersService.addUser(JOHN, LINDA, hashPassword("Password"), "linda@example.com", "Linda Johnson");
			Assert.assertTrue(true);
		} catch (PluginException e) {
			Assert.assertEquals("User Linda already exists.", e.getMessage());
		}
	}	

	@Test
	public void testAddRoles() throws NoSuchAlgorithmException, SQLException, PluginException {
		usersService.addRole(JOHN, "USER");

		User user = usersService.getUserDetails(JOHN, JOHN);
		Assert.assertEquals(JOHN, user.getName());
		Assert.assertEquals(2, user.getRoles().size());
		Assert.assertEquals("ADMIN", user.getRoles().get(0));
		Assert.assertEquals("USER", user.getRoles().get(1));
	}	

	@Test
	public void testAddRolesWhichAlreadyExists() throws NoSuchAlgorithmException, SQLException, PluginException {
		try {
			usersService.addRole(JOHN, "ADMIN");
			Assert.assertTrue(true);
		} catch (PluginException e) {
			Assert.assertEquals("User with name 'John' has already role ADMIN.", e.getMessage());
		}
	}	

	@Test
	public void testAuthenticateUser() throws NoSuchAlgorithmException, SQLException, PluginException {
		User user = usersService.authenticateUser(JOHN, "Password");
		Assert.assertEquals(JOHN, user.getName());
	}	

	@Test
	public void testAuthenticateUserFailed() throws NoSuchAlgorithmException, SQLException, PluginException {
		User user = usersService.authenticateUser(JOHN, "NoPassword");
		Assert.assertEquals(null, user);
	}	

	@Test
	public void testIsUserInRole() throws NoSuchAlgorithmException, SQLException, PluginException {
		Assert.assertEquals(true, usersService.isUserInRole(JOHN, "ADMIN"));
	}	

	@Test
	public void testIsUserNotInRole() throws NoSuchAlgorithmException, SQLException, PluginException {
		Assert.assertEquals(false, usersService.isUserInRole(JOHN, "USER"));
	}	

	@Test
	public void testIsNotExistingUserInRole() throws NoSuchAlgorithmException, SQLException, PluginException {
		Assert.assertEquals(false, usersService.isUserInRole("X", "USER"));
	}	

	@Test
	public void testIsUserInRoleMultiple() throws NoSuchAlgorithmException, SQLException, PluginException {
		Assert.assertEquals(true, usersService.isUserInRole(SANDRA, "USER"));
	}	

}
