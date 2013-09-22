package org.gradle.plugins.fsm

import org.junit.Test

import org.gradle.testfixtures.ProjectBuilder
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin

import static org.junit.Assert.*

class FSMPluginTest {
	
	@Test
	void fsmPluginApplied() {
		Project project = ProjectBuilder.builder().build()
		project.apply plugin: 'fsm'
		
		assertTrue(project.plugins.hasPlugin(FSMPlugin.class))
	}
	
	@Test
	void javaPluginApplied() {
		Project project = ProjectBuilder.builder().build()
		project.apply plugin: 'fsm'
		
		assertTrue(project.plugins.hasPlugin(JavaPlugin.class))
	}
}
