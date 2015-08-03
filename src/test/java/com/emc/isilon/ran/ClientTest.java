package com.emc.isilon.ran;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ClientTest {
	
	private Client client;
	
	@Before
	public void setup() {
		client = new Client();
	}

	@Test
	public void testGetDirectoryListing() {
		DirectoryListing dirList = client.getDirectoryListing("test_directory");
		Assert.assertEquals(2, dirList.getChildren().size());
	}

	@Test
	public void testGetDirectoryAcl() {
		EntryAccessControl dirAcl = client.getAccessControlList("test_directory");
		Assert.assertEquals("0700", dirAcl.getMode());
	}
	
	@Test
	public void testSetDirectoryAcl() {
		Trustee trustee = new Trustee("rest_user", "user");
		AccessControlList aclItem = new AccessControlList("add", "allow", trustee, Arrays.asList("dir_gen_all"));
		EntryAccessControl acl = new EntryAccessControl(Arrays.asList(aclItem), "acl");
		client.setAccessControlList(acl, "test_directory");
	}

}
