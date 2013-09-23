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
package org.gradle.plugins.fsm;

import java.util.concurrent.Callable;

import org.gradle.api.Action;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.ConfigurationContainer;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.file.FileCollection;
import org.gradle.api.internal.artifacts.publish.ArchivePublishArtifact;
import org.gradle.api.internal.plugins.DefaultArtifactPublicationSet;
import org.gradle.api.plugins.BasePlugin;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.plugins.JavaPluginConvention;
import org.gradle.api.tasks.SourceSet;
import org.gradle.plugins.fsm.tasks.bundling.FSM;

/**
 * <p>
 * A {@link Plugin} with tasks which assembles an (java) application into a FSM (FirstSpirit module) file.
 * </p>
 */
public class FSMPlugin implements Plugin<Project> {

	static final String NAME = "fsm";
	static final String FSM_TASK_NAME = NAME;
	static final String PLUGIN_CONVENTION_NAME = NAME;

	static final String PROVIDED_COMPILE_CONFIGURATION_NAME = "fsProvidedCompile";
	static final String PROVIDED_RUNTIME_CONFIGURATION_NAME = "fsProvidedRuntime";

	@Override
	public void apply(Project project) {
		project.getPlugins().apply(JavaPlugin.class);

		FSMPluginConvention pluginConvention = new FSMPluginConvention();
		project.getConvention().getPlugins()
				.put(PLUGIN_CONVENTION_NAME, pluginConvention);

		configureTask(project, pluginConvention);

		configureConfigurations(project.getConfigurations());
	}

	private void configureTask(final Project project,
			final FSMPluginConvention pluginConvention) {
		FSM fsm = project.getTasks().create(FSM_TASK_NAME, FSM.class);
		fsm.setDescription("Assembles a fsm archive containing the FirstSpirit module.");
		fsm.setGroup(BasePlugin.BUILD_GROUP);

		addPublication(project, fsm);

		mapPluginConvention(fsm, pluginConvention);

		project.getTasks().withType(FSM.class, new Action<FSM>() {
			public void execute(FSM task) {
				task.dependsOn(new Callable<FileCollection>() {
					@Override
					public FileCollection call() throws Exception {
						return project.getConvention()
								.getPlugin(JavaPluginConvention.class)
								.getSourceSets()
								.getByName(SourceSet.MAIN_SOURCE_SET_NAME)
								.getRuntimeClasspath();
					}
				});

				task.dependsOn(new Callable<String>() {
					@Override
					public String call() throws Exception {
						return JavaPlugin.JAR_TASK_NAME;
					}
				});
				
				task.classpath(new Object[] { new Callable<FileCollection>() {
					public FileCollection call() throws Exception {
						final FileCollection runtimeClasspath = project
								.getConvention()
								.getPlugin(JavaPluginConvention.class)
								.getSourceSets()
								.getByName(SourceSet.MAIN_SOURCE_SET_NAME)
								.getRuntimeClasspath();
						final FileCollection outputs = project.getTasks()
								.findByName(JavaPlugin.JAR_TASK_NAME)
								.getOutputs().getFiles();
								
						final Configuration providedRuntime = project
								.getConfigurations().getByName(
										PROVIDED_RUNTIME_CONFIGURATION_NAME);
						return runtimeClasspath.minus(providedRuntime).plus(outputs);
					}
				} });
			}
		});
	}

	private void addPublication(Project project, FSM fsm) {
		// remove jar artifact added by java the plugin (see http://issues.gradle.org/browse/GRADLE-687)
		Configuration archivesConfig = project.getConfigurations().getByName(Dependency.ARCHIVES_CONFIGURATION);
		archivesConfig.getArtifacts().clear();
		
		DefaultArtifactPublicationSet publicationSet = project.getExtensions()
				.getByType(DefaultArtifactPublicationSet.class);
		
		publicationSet.addCandidate(new ArchivePublishArtifact(fsm));
	}

	private void mapPluginConvention(FSM fsm,
			final FSMPluginConvention pluginConvention) {
		fsm.getConventionMapping().map(
				FSMPluginConvention.MODULE_DIR_NAME_CONVENTION,
				new Callable<String>() {

					@Override
					public String call() throws Exception {
						return pluginConvention.getModuleDirName();
					}
				});
	}

	private void configureConfigurations(
			ConfigurationContainer configurationContainer) {
		Configuration provideCompileConfiguration = configurationContainer
				.create(PROVIDED_COMPILE_CONFIGURATION_NAME)
				.setVisible(false)
				.setDescription(
						"Additional compile classpath for libraries that should not be part of the FSM archive.");

		Configuration provideRuntimeConfiguration = configurationContainer
				.create(PROVIDED_RUNTIME_CONFIGURATION_NAME)
				.setVisible(false)
				.extendsFrom(provideCompileConfiguration)
				.setDescription(
						"Additional runtime classpath for libraries that should not be part of the FSM archive.");

		configurationContainer.getByName(JavaPlugin.COMPILE_CONFIGURATION_NAME)
				.extendsFrom(provideCompileConfiguration);
		configurationContainer.getByName(JavaPlugin.RUNTIME_CONFIGURATION_NAME)
				.extendsFrom(provideRuntimeConfiguration);
	}
}
