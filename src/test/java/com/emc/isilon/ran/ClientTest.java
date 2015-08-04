package com.emc.isilon.ran;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.ws.rs.core.MediaType;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ClientTest {
	
	private Client client;
	
	@Before
	public void setup() throws ClientException {
		client = new Client();
		client.createDirectory("test_root");
	}
	
	@After
	public void teardown() throws ClientException {
		client.deleteDirectory("test_root", true);
	}

	@Test
	public void testGetDirectoryListing() throws ClientException {
		DirectoryListing dirList = client.getDirectoryListing("test_root");
		Assert.assertEquals(0, dirList.getChildren().size());
	}

	@Test
	public void testGetDirectoryAcl() throws ClientException {
		EntryAccessControl dirAcl = client.getAccessControlList("test_root");
		Assert.assertEquals("0700", dirAcl.getMode());
	}
	
	@Test
	public void testCreateDeleteDirectory() throws ClientException {
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
	public void testSetDirectoryAcl() throws ClientException {
		client.createDirectory("test_root/dir1");

		Trustee trustee = new Trustee("rest_user", "user");
		AccessControlList aclItem = new AccessControlList("add", "allow", trustee, Arrays.asList("dir_gen_all"));
		EntryAccessControl acl = new EntryAccessControl(Arrays.asList(aclItem), "acl");

		client.setAccessControlList(acl, "test_root/dir1");

		EntryAccessControl dirAcl = client.getAccessControlList("test_root/dir1");
		Assert.assertEquals("dir_gen_all", dirAcl.getAcl().get(0).getAccessrights().get(0));
	}

	@Test
	public void testCreateDeleteBinaryFile() throws ClientException, NoSuchAlgorithmException, IOException {
		MessageDigest digest = MessageDigest.getInstance("MD5");
		String filePath = getClass().getResource("/success.jpg").getPath();
		InputStream testStream = getClass().getResourceAsStream("/success.jpg");
		String testMd5Sum = getFileChecksum(digest, testStream);
		
		DirectoryListing dirList1 = client.getDirectoryListing("test_root");
		Assert.assertEquals(0, dirList1.getChildren().size());

		client.createFile("test_root/image.jpg", filePath, MediaType.APPLICATION_OCTET_STREAM_TYPE);
		InputStream resultStream = client.readFile("test_root/image.jpg", MediaType.APPLICATION_OCTET_STREAM_TYPE);
		String resultMd5Sum = getFileChecksum(digest, resultStream);
		
		Assert.assertEquals(testMd5Sum, resultMd5Sum);

		DirectoryListing dirList2 = client.getDirectoryListing("test_root");
		Assert.assertEquals(1, dirList2.getChildren().size());

		client.deleteFile("test_root/image.jpg");

		DirectoryListing dirList3 = client.getDirectoryListing("test_root");
		Assert.assertEquals(0, dirList3.getChildren().size());
	}
	
	private static String getFileChecksum(MessageDigest digest, InputStream fis) throws IOException
	{
	    byte[] byteArray = new byte[1024];
	    int bytesCount = 0;
	      
	    while ((bytesCount = fis.read(byteArray)) != -1) {
	        digest.update(byteArray, 0, bytesCount);
	    };
	    fis.close();
	     
	    byte[] bytes = digest.digest();
	    StringBuilder sb = new StringBuilder();
	    for(int i=0; i< bytes.length ;i++)
	    {
	        sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
	    }
	   return sb.toString();
	}
}