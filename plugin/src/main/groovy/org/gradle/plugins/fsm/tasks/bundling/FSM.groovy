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
import org.gradle.api.tasks.InputFiles;
import org.gradle.api.tasks.Optional;
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.bundling.Jar


/**
 * Bundles the FSM using libraries from the internal {@link FileCollection} classpath
 * and the configured module.xml.
 *
 */
class FSM extends Jar {
	static final String FSM_EXTENSION = 'fsm'

	/**
	 * Directory containing the module.xml, mapped from plugin convention
	 */
	String moduleDirName
	
	/**
	 * The fsm runtime classpath. All libraries in this
	 * classpath will be copied to 'fsm/lib' folder
	 */
	private FileCollection classpath
	
	FSM() {
		extension = FSM_EXTENSION
		destinationDir = project.file('build/fsm')
		
		into('lib') {
			from {
				def classpath = getClasspath()
				classpath ? classpath.filter {File file -> file.isFile()} : []
			}
		}
	}
	
	@Override
	@TaskAction
	protected void copy() {
		metaInf {
			from project.file(moduleDirName)
			include 'module.xml'

			expand(name: project.name,
				version: project.version,
				description: project.description,
				artifact: project.jar.archiveName)
		}
		super.copy();
	}
	
	/**
	 * Returns the classpath to include in the FSM archive. Any JAR or ZIP files in this classpath are included in the
	 * {@code lib} directory.
	 *
	 * @return The classpath. Returns an empty collection when there is no classpath to include in the FSM.
	 */
	@InputFiles @Optional
	FileCollection getClasspath() {
		return classpath
	}

	/**
	 * Sets the classpath to include in the FSM archive.
	 *
	 * @param classpath The classpath. Must not be null.
	 */
	void setClasspath(Object classpath) {
		this.classpath = project.files(classpath)
	}

	/**
	 * Adds files to the classpath to include in the FSM archive.
	 *
	 * @param classpath The files to add. These are evaluated as for {@link org.gradle.api.Project#files(Object [])}
	 */
	void classpath(Object... classpath) {
		FileCollection oldClasspath = getClasspath()
		this.classpath = project.files(oldClasspath ?: [], classpath)
	}
}
