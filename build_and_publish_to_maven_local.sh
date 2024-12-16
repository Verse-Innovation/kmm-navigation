#!/bin/bash

SECONDS=0

####################################################################################################
###########################################  Clean  ################################################
####################################################################################################

./gradlew clean

####################################################################################################
##########################################  Build  #################################################
####################################################################################################

./gradlew :libraries:navigation-core:build
#./gradlew :libraries:navigation-core:navigation-core-test:build

./gradlew :libraries:deeplink-core:build
#./gradlew :libraries:deeplink-core:deeplink-core-test:build

./gradlew :libraries:navigation-android:build
#./gradlew :libraries:navigation-android:navigation-android-test:build

./gradlew :libraries:deeplink-android:build
#./gradlew :libraries:deeplink-android:deeplink-android-test:build

####################################################################################################
####################################  Publish to maven local  ######################################
####################################################################################################

./gradlew :libraries:navigation-core:publishToMavenLocal
#./gradlew :libraries:navigation-core:navigation-core-test:publishToMavenLocal

./gradlew :libraries:deeplink-core:publishToMavenLocal
#./gradlew :libraries:deeplink-core:deeplink-core-test:publishToMavenLocal

./gradlew :libraries:navigation-android:publishToMavenLocal
#./gradlew :libraries:navigation-android:navigation-android-test:publishToMavenLocal

./gradlew :libraries:deeplink-android:publishToMavenLocal
#./gradlew :libraries:deeplink-android:deeplink-android-test:publishToMavenLocal

echo "total time taken $SECONDS sec"