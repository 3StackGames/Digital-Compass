package com.three_stack.digital_compass.backend;

import java.io.IOException;

import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import mockit.Tested;
import mockit.Verifications;

import org.junit.Test;

public class PhaseManagerTest {
	@Tested
	ServiceAbc tested;
	@Injectable
	DependencyXyz mockXyz;

	@Test
	public void doOperationAbc(@Mocked final AnotherDependency anyInstance) {
		new Expectations() {
			{
				anyInstance.doSomething(anyString);
				result = 123;
				AnotherDependency.someStaticMethod();
				result = new IOException();
			}
		};

		tested.doOperationAbc("some data");

		new Verifications() {
			{
				mockXyz.complexOperation(true, anyInt, null);
				times = 1;
			}
		};
	}

}
