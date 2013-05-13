package com.myinterwebspot.app.dartnight.model;

import com.parse.ParseObject;
import com.parse.ParseUser;

public class ParseModel {

	private final ParseObject parse;

	ParseModel(ParseObject parseObject) {
		this.parse = parseObject;
	}

	public ParseModel(String className) {
		this.parse = new ParseObject(className);
	}

	public String getId() {
		return this.parse.getObjectId();
	}

	public ParseObject asParse() {
		return this.parse;
	}

	protected void put(String fieldName, Object fieldValue) {
		this.parse.put(fieldName, fieldValue);
	}

	protected String getString(String fieldName) {
		return this.parse.getString(fieldName);
	}

	protected long getLong(String fieldName) {
		return this.parse.getLong(fieldName);
	}

	protected ParseUser getParseUser(String fieldName) {
		return this.parse.getParseUser(fieldName);
	}

	protected ParseObject getParseObject(String fieldName) {
		return this.parse.getParseObject(fieldName);
	}

	public static <T extends ParseModel> T fromParseObject(ParseObject obj,
			Class<T> clazz) {
		return clazz.cast(new ParseModel(obj));
	}

}
