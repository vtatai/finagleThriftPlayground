namespace java com.twitter.finagle.example.thriftjava
#@namespace scala com.twitter.finagle.example.thriftscala

service Echo {
  string ping(string message);
}
