/*
 * Copyright 2002-2013 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.springframework.data.gemfire.function.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.gemfire.TestUtils;
import org.springframework.data.gemfire.function.config.two.TestOnRegionFunction;
import org.springframework.data.gemfire.function.execution.GemfireOnRegionFunctionTemplate;
import org.springframework.data.gemfire.function.execution.OnRegionFunctionProxyFactoryBean;
import org.springframework.data.gemfire.test.GemfireTestApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gemstone.gemfire.cache.Region;

/**
 * @author David Turanski
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestConfig.class }, initializers = GemfireTestApplicationContextInitializer.class)
public class FunctionExecutionIntegrationTests {
	@Autowired
	ApplicationContext context;

	@Test
	public void testProxyFactoryBeanCreated() throws Exception {
		OnRegionFunctionProxyFactoryBean factoryBean = (OnRegionFunctionProxyFactoryBean) context
				.getBean("&testFunction");
		Class<?> serviceInterface = TestUtils.readField("serviceInterface", factoryBean);
		assertEquals(serviceInterface, TestOnRegionFunction.class);

		Region<?, ?> r1 = context.getBean("r1", Region.class);

		GemfireOnRegionFunctionTemplate template = TestUtils.readField("gemfireFunctionOperations", factoryBean);

		assertSame(r1, TestUtils.readField("region", template));
	}

}

@ImportResource("/org/springframework/data/gemfire/function/config/FunctionExecutionIntegrationTests-context.xml")
@EnableGemfireFunctionExecutions(basePackages = "org.springframework.data.gemfire.function.config.two")
@Configuration
class TestConfig {

}
