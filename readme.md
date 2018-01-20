# Summary

In this project I show a sample integration between two Apache projects:

Apache Geode and Apache Kafka

As we may know Apache Geode is a high performance key value data store.   At Geode heart is a rich eventing system.   That eventing system allows developer to build rich event driven applications.

We are going to use Geode's event architecture to integration its data operations with Kafka.  In this example I am going to be using Geode's ``AsyncEventListener`` to asynchronously send data to Kafka so Geode data operations can work at in memory speeds.

# How to deploy the project in Geode

The script below shows how to start up a Geode system and deploy code to integrate Geode and Kafka.

```
start locator --name=locator
configure pdx --read-serialized=true
start server --name=server1
deploy --dir=/Users/cblack/Downloads/geode-kafka/listener/build/dependancies
y
deploy --dir=/Users/cblack/Downloads/geode-kafka/listener/build/libs
y
create async-event-queue --id=kafka-queue --listener=example.geode.kafka.KafkaAsyncEventListener --listener-param=bootstrap.servers#192.168.127.165:9092 --batch-size=5 --batch-time-interval=1000
create region --name=test --type=PARTITION --async-event-queue-id=kafka-queue


```

# Example Output from Kafka

In Geode's ``AsyncEventListener`` we converted the ``Customer`` plain ole java object to JSON.      In the ``Driver`` we sent in 10 Customer records and we can see that Kafka recieved those 10 JSON documents.  The sample output can be seen below:

```
demo@demo-kafka:~/kafka_2.11-1.0.0$ bin/kafka-console-consumer.sh --bootstrap-server 192.168.127.165:9092 --topic test  
{
  "guid" : "b4af31db-f129-4437-b315-60ef72ce1968",
  "firstName" : "Alexis",
  "middleName" : "",
  "lastName" : "Spencer",
  "email" : "alexisspencer@gmail.com",
  "username" : "alexiss",
  "password" : "HM6vLlQS",
  "telephoneNumber" : "924-101-3682",
  "dateOfBirth" : "2003-01-23T16:52:40.757-08:00",
  "age" : 14,
  "companyEmail" : "alexis.spencer@datastore.biz",
  "nationalIdentityCardNumber" : "026-22-2967",
  "nationalIdentificationNumber" : "",
  "passportNumber" : "6kuqL2tq9"
}
{
  "guid" : "0d8942f2-d5ff-4d9a-9f3c-d0ba9f4890fc",
  "firstName" : "Ariana",
  "middleName" : "Sophia",
  "lastName" : "Carter",
  "email" : "ariana.carter@yahoo.com",
  "username" : "acarter",
  "password" : "cQdqiTQs",
  "telephoneNumber" : "971-500-858",
  "dateOfBirth" : "2002-03-13T05:26:26.545-08:00",
  "age" : 15,
  "companyEmail" : "ariana.carter@interdemltd.com",
  "nationalIdentityCardNumber" : "308-04-8381",
  "nationalIdentificationNumber" : "",
  "passportNumber" : "5KDAvOpcV"
}
{
  "guid" : "c1aa630b-9d19-4bec-90bb-ace1dcbf8750",
  "firstName" : "Alex",
  "middleName" : "Bryson",
  "lastName" : "Burton",
  "email" : "alex.burton@mail.com",
  "username" : "alexb",
  "password" : "zNbxbw4U",
  "telephoneNumber" : "653-624-0341",
  "dateOfBirth" : "1939-11-27T08:51:42.519-08:00",
  "age" : 78,
  "companyEmail" : "alex.burton@vitaeltd.biz",
  "nationalIdentityCardNumber" : "669-18-8704",
  "nationalIdentificationNumber" : "",
  "passportNumber" : "9voFQrTgx"
}
{
  "guid" : "cc375974-3f1a-48db-9c1d-727734d80e50",
  "firstName" : "Faith",
  "middleName" : "",
  "lastName" : "Grimes",
  "email" : "faith.grimes@yahoo.com",
  "username" : "faithg",
  "password" : "VqxwyBbI",
  "telephoneNumber" : "449-533-3093",
  "dateOfBirth" : "1956-06-01T23:15:21.779-07:00",
  "age" : 61,
  "companyEmail" : "faith.grimes@morsemindustries.com",
  "nationalIdentityCardNumber" : "041-09-5054",
  "nationalIdentificationNumber" : "",
  "passportNumber" : "j3W2ScmXR"
}
{
  "guid" : "3f7393d4-e026-4697-a0e2-ddad1368765b",
  "firstName" : "Bailey",
  "middleName" : "Annabelle",
  "lastName" : "Kirby",
  "email" : "bailey.kirby@yahoo.com",
  "username" : "bkirby",
  "password" : "vOzigmjy",
  "telephoneNumber" : "697-590-724",
  "dateOfBirth" : "1980-01-26T09:08:28.288-08:00",
  "age" : 37,
  "companyEmail" : "bailey.kirby@felicsconsulting.biz",
  "nationalIdentityCardNumber" : "249-82-9524",
  "nationalIdentificationNumber" : "",
  "passportNumber" : "DQ7MM477N"
}
{
  "guid" : "fe127d9c-1f5f-44fb-9a9b-ec219f854402",
  "firstName" : "Ella",
  "middleName" : "",
  "lastName" : "Morrow",
  "email" : "ella.morrow@gmail.com",
  "username" : "ellam",
  "password" : "DUJd38Gt",
  "telephoneNumber" : "212-183-5694",
  "dateOfBirth" : "1982-12-31T20:01:27.089-08:00",
  "age" : 35,
  "companyEmail" : "ella.morrow@yrsaltd.com",
  "nationalIdentityCardNumber" : "477-09-2268",
  "nationalIdentificationNumber" : "",
  "passportNumber" : "oP2goax24"
}
{
  "guid" : "888b2a88-b872-4d31-bb48-31facec4cc34",
  "firstName" : "James",
  "middleName" : "Evan",
  "lastName" : "Weeks",
  "email" : "weeks@yahoo.com",
  "username" : "jweeks",
  "password" : "0IZn4Zg4",
  "telephoneNumber" : "146-682-2264",
  "dateOfBirth" : "1940-09-10T07:14:58.862-08:00",
  "age" : 77,
  "companyEmail" : "james.weeks@kleinconsulting.com",
  "nationalIdentityCardNumber" : "492-65-0720",
  "nationalIdentificationNumber" : "",
  "passportNumber" : "4K5NVid7r"
}
{
  "guid" : "949eafe9-2fd0-49f3-9f71-4841ca18feae",
  "firstName" : "Ariana",
  "middleName" : "",
  "lastName" : "Blankenship",
  "email" : "ariana.blankenship@mail.com",
  "username" : "arianab",
  "password" : "Oqb3rHr9",
  "telephoneNumber" : "376-072-3754",
  "dateOfBirth" : "1941-01-01T07:44:17.866-08:00",
  "age" : 77,
  "companyEmail" : "ariana.blankenship@interdemlimited.com",
  "nationalIdentityCardNumber" : "115-36-1831",
  "nationalIdentificationNumber" : "",
  "passportNumber" : "wKVKdeRjh"
}
{
  "guid" : "601db5af-7b30-462e-8eb9-3a04adfa9456",
  "firstName" : "Lily",
  "middleName" : "",
  "lastName" : "Mcfadden",
  "email" : "lilymcfadden@yahoo.com",
  "username" : "lmcfadden",
  "password" : "LlbDSdaj",
  "telephoneNumber" : "754-242-6114",
  "dateOfBirth" : "1957-07-07T06:31:10.849-07:00",
  "age" : 60,
  "companyEmail" : "lily.mcfadden@erntogra.eu",
  "nationalIdentityCardNumber" : "769-12-9910",
  "nationalIdentificationNumber" : "",
  "passportNumber" : "zf097cUYx"
}
{
  "guid" : "598ec0e6-a954-43e4-ae02-4d1fef06910d",
  "firstName" : "Piper",
  "middleName" : "",
  "lastName" : "Conner",
  "email" : "conner@mail.com",
  "username" : "pconner",
  "password" : "NSHjiY9x",
  "telephoneNumber" : "346-285-0814",
  "dateOfBirth" : "1924-03-21T05:45:13.111-08:00",
  "age" : 93,
  "companyEmail" : "piper.conner@quicklink.com",
  "nationalIdentityCardNumber" : "126-29-7853",
  "nationalIdentificationNumber" : "",
  "passportNumber" : "xGg9u1HBB"
}
^CProcessed a total of 10 messages

```