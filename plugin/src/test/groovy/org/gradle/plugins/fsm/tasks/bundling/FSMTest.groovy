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
package org.gradle.plugins.fsm.tasks.bundling

import static org.hamcrest.Matchers.*
import static org.junit.Assert.*

import org.gradle.api.Project
import org.gradle.plugins.fsm.FSMPlugin
import org.gradle.testfixtures.ProjectBuilder
import org.junit.After
import org.junit.Before
import org.junit.Test

class FSMTest {

	private final File testDir = new File("build/tmp/tests")
	private Project project

	FSM fsm

	@Before public void setUp() {
		project = ProjectBuilder.builder().withProjectDir(testDir).build()
		project.apply plugin: FSMPlugin.NAME
		project.convention.plugins.fsm.moduleDirName = "src/test/resources"

		fsm = project.tasks[FSMPlugin.FSM_TASK_NAME]
		
		fsm.baseName = 'testbasename'
		fsm.appendix = 'testappendix'
		fsm.version = '1.0'
	}

	@After
	void tearDown() {
		if(testDir.exists()) {
			testDir.deleteDir()
		}
	}

	@Test 
	public void testExecute() {
		fsm.execute()
		assertTrue(fsm.destinationDir.isDirectory())
		assertTrue(fsm.archivePath.isFile())
	}
	
	@Test
	public void fsmExtensionUsed() {
		assertEquals(FSM.FSM_EXTENSION, fsm.extension)
	}
	
	@Test 
	public void archivePathUsed() {
		assertEquals(new File(new File(project.buildDir, "fsm"), fsm.archiveName), fsm.archivePath)
	}
}
