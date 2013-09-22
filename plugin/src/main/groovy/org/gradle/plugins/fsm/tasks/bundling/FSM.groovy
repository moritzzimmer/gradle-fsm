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

import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.bundling.Jar


class FSM extends Jar {
	static final String FSM_EXTENSION = 'fsm'

	/**
	 * Directory containing the module.xml, mapped from plugin convention
	 */
	String moduleDirName
	
	FSM() {
		extension = FSM_EXTENSION
		destinationDir = project.file('build/fsm')
	}
	
	@Override
	@TaskAction
	protected void copy() {
		metaInf {
			from project.file(moduleDirName)
			include 'module.xml'

			// TODO: add vendor here
			expand(name: project.name,
				version: project.version,
				description: project.description,
				artifact: project.jar.archiveName)
		}
		super.copy();
	}
}
