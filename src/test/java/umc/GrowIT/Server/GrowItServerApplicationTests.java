package umc.GrowIT.Server;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(
		webEnvironment = SpringBootTest.WebEnvironment.MOCK,
		properties = {
				"spring.datasource.url=jdbc:h2:mem:testdb",
				"spring.datasource.driverClassName=org.h2.Driver",
				"spring.datasource.username=sa",
				"spring.datasource.password=",
				"spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
				"spring.h2.console.enabled=true",
				"spring.jpa.hibernate.ddl-auto=create-drop",
				"spring.jpa.show-sql=true",

				"aws.accessKey=test",
				"aws.secretKey=test",
				"aws.s3.bucket=test",
				"aws.s3.base-url=http://test",

				"jwt.secretKey=testsecretkeytestsecretkeytestsecretkeytestsecretkey",

				"openai.api.key=test",
				"openai.api.url=test",
				"openai.model1=test",
				"openai.model2=test",

				"mail.host=test",
				"mail.port=587",
				"mail.username=test",
				"mail.password=test"
		}
)
class GrowItServerApplicationTests {

	@Test
	void contextLoads() {
	}

}