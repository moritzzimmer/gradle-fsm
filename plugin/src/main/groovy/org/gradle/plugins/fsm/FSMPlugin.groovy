package org.gradle.plugins.fsm

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.JavaPlugin;

class FSMPlugin implements Plugin<Project> {

	@Override
	public void apply(Project project) {
		project.plugins.apply(JavaPlugin.class)
	}
}
