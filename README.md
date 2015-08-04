Isilon File System Access API Java Client
=========================================

This project creates some examples for using the Isilon file
system access API, which allows applications to
create/delete/copy/move/clone files and directories over a REST
API.  More information on the API can be found in the [latest
version (7.2) of the API guide](https://community.emc.com/docs/DOC-40119).

The Isilon default SSL certificate is self-signed, so the certificate
must be imported into the Java and/or IDE keystores for the examples to
execute.  Host name verification has been disabled in this example.

For the purposes of demonstration, the client configuration has been done
via constants in the Client class.  For production usage, this could be
extracted via beans, configuration classes or properties.

Currently implemented methods:

* createDirectory
* deleteDirectory
* createFile
* deleteFile
* readFile
* getDirectoryListing
* getAccessControlList
* setAccessControlList

TODO:
-----
* Extract client configuration
* Expand example set to more file/directory operations:
  - copy
  - move
  - clone
  - attributes
* Add additional examples
  - access points
  - query operations
  - smartlock