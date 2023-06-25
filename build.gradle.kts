/*
 * chunkstories-api
 * Collection of interfaces forming an API and the skeleton of the chunkstories project.
 */

plugins {
	java
	`java-library`
	kotlin("jvm") version ("1.8.10")
	`maven-publish`
}

version = "2.0.4"

group = "xyz.chunkstories"
description = "API and core data structures to write mods against"

repositories {
	mavenLocal()
	mavenCentral()
}

publishing {
	publications {
		create<MavenPublication>("maven") {
			from(components["java"])
		}
	}
}

val compileJava : JavaCompile by tasks
compileJava.apply {
	// options.compilerArgs.add("-Xlint:none")
	options.encoding = "utf-8"
}

val jar: Jar by tasks
jar.apply {
	//I don't like numbers in my jars
	version = null
}

compileJava.options.debugOptions.debugLevel = "source,lines,vars"

dependencies {
	api(group = "org.joml", name = "joml", version = "1.10.5")
	api(group = "org.slf4j", name = "slf4j-api", version = "1.7.9")
	api(project(":enklume"))

	implementation("org.jetbrains.kotlin:kotlin-reflect:1.8.10")
	implementation(group = "com.google.code.findbugs", name= "jsr305", version = "3.0.2")
	testImplementation("junit:junit:4.13.1")
	api(group = "com.google.code.gson", name = "gson", version = "2.8.9")
}

val javadoc: Javadoc by tasks
javadoc.apply {
	isFailOnError = false
	source = sourceSets.main.get().allJava
}
