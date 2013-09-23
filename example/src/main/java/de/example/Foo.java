package de.example;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;

public class Foo {

	// usage of a compile time dependency provided by FirstSpirit (see build.gradle)
	private static final Log LOGGER = LogFactory.getLog(Foo.class);

	// usage of a compile time dependency (see build.gradle)
	private DateTime date;

	public DateTime getDate() {
		return date;
	}

	public void setDate(DateTime date) {
		this.date = date;
	}
}
