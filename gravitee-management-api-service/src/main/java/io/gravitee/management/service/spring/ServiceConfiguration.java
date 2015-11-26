/**
 * Copyright (C) 2015 The Gravitee team (http://gravitee.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.gravitee.management.service.spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.gravitee.common.event.EventManager;
import io.gravitee.common.event.impl.EventManagerImpl;
import io.gravitee.definition.jackson.datatype.GraviteeMapper;
import io.gravitee.plugin.core.spring.PluginConfiguration;
import io.gravitee.plugin.policy.spring.PolicyPluginConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author David BRASSELY (brasseld at gmail.com)
 */
@Configuration
@ComponentScan("io.gravitee.management.service")
@EnableTransactionManagement
@Import({PluginConfiguration.class, PolicyPluginConfiguration.class})
public class ServiceConfiguration {

	@Bean
	public EventManager eventManager() {
		return new EventManagerImpl();
	}

	@Bean
	public ObjectMapper objectMapper() {
		return new GraviteeMapper();
	}
}