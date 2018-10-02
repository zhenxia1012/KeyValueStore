namespace java dbServer
service DbService{
	list<string> get(1:string key) 
 	list<string> getToTime(1:string key,2:i32 time)
 	list<string> dif(1:string key,2:i32 time1, 3:i32 time2)
	void put(1:string key,2:string value)
	bool delKey(1:string key)
	bool delValue(1:string key,2:string value)
}