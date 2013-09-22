/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.gradle.plugins.fsm

import static org.hamcrest.Matchers.*
import static org.junit.Assert.*

import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.Dependency
import org.gradle.api.plugins.JavaPlugin
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Before
import org.junit.Test

class FSMPluginTest {

	Project project

	@Before
	public void setUp() {
		project = ProjectBuilder.builder().build()
	}

	@Test
	void fsmPluginApplied() {
		project.apply plugin: FSMPlugin.NAME
		assertTrue(project.plugins.hasPlugin(FSMPlugin))
	}

	@Test
	void javaPluginApplied() {
		project.apply plugin: FSMPlugin.NAME
		assertTrue(project.plugins.hasPlugin(JavaPlugin))
	}

	@Test
	void appliesTask() {
		project.apply plugin: FSMPlugin.NAME

		def task = project.tasks[FSMPlugin.FSM_TASK_NAME]
		assertThat(task, instanceOf(FSM))
	}

	@Test
	void fsmTaskDependsOnJar() {
		project.apply plugin: FSMPlugin.NAME

		Task fsm = project.tasks[FSMPlugin.FSM_TASK_NAME]
		Set<? extends Task> dependencies = fsm.getTaskDependencies().getDependencies(fsm)
		boolean matches = false
		for (Task depTask : dependencies) {
			if (depTask.getName() == JavaPlugin.JAR_TASK_NAME) {
				matches = true
				break
			}
		}
		assertTrue(matches)
	}

	@Test
	void fsmAddedAsPublication() {
		project.apply plugin: FSMPlugin.NAME

		Configuration archiveConfiguration = project.getConfigurations().getByName(Dependency.ARCHIVES_CONFIGURATION)
		assertThat(archiveConfiguration.getAllArtifacts().size(), equalTo(2)) // jar and fsm
		assertThat(archiveConfiguration.getAllArtifacts().toArray()[1].getType(), equalTo("fsm"))
	}
}
