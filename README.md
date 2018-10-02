# KeyValueStore

## Introduction
The key-value store implements the friend-list in a social network, where key is the username and value is a list of names of the friends of the user (key). The value could change over time for given user and this key-value store captures this dynamic nature of the social network.<br>
<br>Key-Value Store allows client to manages their data as well as its history on server side.

## Details
### LinkedMap
* Managed key/value pairs as well as their insertion sequence. It was implemented by a hash map and a double linked list. 
  * Hash map recorded the key/value pair
  * double linked list maintains insertion sequence
* Implemented iterator so that the key/value pairs can be visited in terms of their insertion order.

### KVDb
* Implemented Key/Value Store by template
  * Value is LinkedMap which stores value/time pairs. LinkedMap allows to visit values in terms of their insertion order.
* Like a traditional key-value store, it supported basic operations:
  * get(key): returns all values associated with the key, if present.
  * put(key, value) : adds or updates the key with the value.
  * del(key): deletes the key from the store.
  * del(key, value): deletes the specified value from the key.
* In addition, this store also supported the following APIs which retained the order in which the values are added to the
given key:
  * get(key, time): returns all values associated with the key up to the specified time.
  * diff(key, time1, time2): returns the difference in value associated with the key between time1 and time2. time1 <= time2.

### DbClient
* Offered interface for client to operate their data on server side

### DbServer
* Defined as a multi-thread server with standard non-blocking I/O mechanism
* Handled client request on Key-Value Store.

### Communication Channel
* Implemented an asynchronously non-blocking communication channel based on binary transmission protocol by thrift.

