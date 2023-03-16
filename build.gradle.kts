plugins {
	java
	id("org.springframework.boot") version "3.0.4"
	id("io.spring.dependency-management") version "1.1.0"
	id("org.asciidoctor.jvm.convert") version "3.3.2"
	id("com.epages.restdocs-api-spec") version "0.17.1"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

val snippetsDir by extra { file("build/generated-snippets") }

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-security")
	compileOnly("org.projectlombok:lombok")
	runtimeOnly("com.h2database:h2")
	runtimeOnly("com.mysql:mysql-connector-j")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	testImplementation("org.springframework.restdocs:spring-restdocs-asciidoctor")
	testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
	testImplementation("net.datafaker:datafaker:1.7.0")
	testImplementation("com.ninja-squad:springmockk:4.0.0")
	testImplementation("com.epages:restdocs-api-spec-mockmvc:0.17.1")
}

openapi3 {
	setServer("http://localhost:8080")
	title = "My API"
	description = "My API description"
	version = "0.1.0"
	format = "yaml"
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.test {
	outputs.dir(snippetsDir)
}

tasks.asciidoctor {
	inputs.dir(snippetsDir)
	dependsOn(tasks.test)
}

tasks.clean {
	delete("src/main/resources/static/docs")
}

tasks.register<Copy>("copyOasToSwagger") {
	delete("src/main/resources/static/swagger-ui/openapi3.yaml")
	from("$buildDir/api-spec/openapi3.yaml")
	into("src/main/resources/static/swagger-ui/.")
	dependsOn("openapi3")
}