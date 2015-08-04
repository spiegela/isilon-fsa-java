package com.emc.isilon.ran;

import java.util.Arrays;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ClientTest {
	
	private Client client;
	
	@Before
	public void setup() {
		client = new Client();
		client.createDirectory("test_root");
	}
	
	@After
	public void teardown() {
		client.deleteDirectory("test_root", true);
	}

	@Test
	public void testGetDirectoryListing() {
		DirectoryListing dirList = client.getDirectoryListing("test_root");
		Assert.assertEquals(0, dirList.getChildren().size());
	}

	@Test
	public void testGetDirectoryAcl() {
		EntryAccessControl dirAcl = client.getAccessControlList("test_root");
		Assert.assertEquals("0700", dirAcl.getMode());
	}
	
	@Test
	public void testCreateDeleteDirectory() {
		client.createDirectory("test_root/dir2");
		DirectoryListing dirList = client.getDirectoryListing("test_root/dir2");
		Assert.assertEquals(0, dirList.getChildren().size());
		DirectoryListing dirList2 = client.getDirectoryListing("test_root");
		Assert.assertEquals(1, dirList2.getChildren().size());
		client.deleteDirectory("test_root/dir2");
		DirectoryListing dirList3 = client.getDirectoryListing("test_root");
		Assert.assertEquals(0, dirList3.getChildren().size());
	}

	@Test
	public void testSetDirectoryAcl() {
		client.createDirectory("test_root/dir1");
		Trustee trustee = new Trustee("rest_user", "user");
		AccessControlList aclItem = new AccessControlList("add", "allow", trustee, Arrays.asList("dir_gen_all"));
		EntryAccessControl acl = new EntryAccessControl(Arrays.asList(aclItem), "acl");
		client.setAccessControlList(acl, "test_root/dir1");
		EntryAccessControl dirAcl = client.getAccessControlList("test_root/dir1");
		Assert.assertEquals("dir_gen_all", dirAcl.getAcl().get(0).getAccessrights().get(0));
	}

}
