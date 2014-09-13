#!/bin/sh
cd swagger-codegen

rm -fr generated-code/android-java

scala -cp  target/scala-2.10/swagger-codegen.jar -DskipErrors=true eu.openiict.codegen.AndroidOPENiApiCodegen http://$1/api-spec/v1

sed -i 's/import eu.openiict.client.model.Datetime;//' generated-code/android-java/src/main/java/eu/openiict/client/model/*
sed -i 's/import eu.openiict.client.model.Integer;//' generated-code/android-java/src/main/java/eu/openiict/client/model/*
sed -i 's/import eu.openiict.client.model.Object;//' generated-code/android-java/src/main/java/eu/openiict/client/api/*
sed -i 's/import eu.openiict.client.model.Related;//' generated-code/android-java/src/main/java/eu/openiict/client/model/*
sed -i 's/related/String/' generated-code/android-java/src/main/java/eu/openiict/client/model/*
sed -i 's/Related/String/' generated-code/android-java/src/main/java/eu/openiict/client/model/*
sed -i 's/datetime/String/' generated-code/android-java/src/main/java/eu/openiict/client/model/*

cd generated-code/android-java/

cp ../../../openi-custom-code/OPENiObject.java src/main/java/eu/openiict/client/model/

sed -i 's/<httpclient-version>4.0/<httpclient-version>4.3.2/' pom.xml

mvn package

cp target/openi-android-client-1.0.0.jar ../../../openi-android-sdk-1.0.0.jar
