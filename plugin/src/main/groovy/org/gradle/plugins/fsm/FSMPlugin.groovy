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

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.internal.artifacts.publish.ArchivePublishArtifact
import org.gradle.api.internal.plugins.DefaultArtifactPublicationSet
import org.gradle.api.plugins.BasePlugin
import org.gradle.api.plugins.JavaPlugin

/**
 * <p>
 * A {@link Plugin} with tasks which assembles an (java) application into a FSM (FirstSpirit module) file.
 * </p>
 */
class FSMPlugin implements Plugin<Project> {

	static final String NAME = "fsm"
	static final String FSM_TASK_NAME = "fsm"

	@Override
	public void apply(Project project) {
		project.plugins.apply(JavaPlugin.class)

		setupTask(project)
	}

	private void setupTask(final Project project) {
		FSM fsm = project.getTasks().create(FSM_TASK_NAME, FSM.class)
		fsm.setDescription("Assembles a fsm archive containing the FirstSpirit module.")
		fsm.setGroup(BasePlugin.BUILD_GROUP)
		fsm.dependsOn(JavaPlugin.JAR_TASK_NAME)
		
		DefaultArtifactPublicationSet publicationSet = project.getExtensions().getByType(DefaultArtifactPublicationSet.class)
		publicationSet.addCandidate(new ArchivePublishArtifact(fsm))
	}
}
