package io.xol.chunkstories.api.util;

import java.util.Iterator;

/** A crappy interface for something I wrote in the early days */
public interface ConfigDeprecated {

	/** Forcibly reloads the configuration file */
	public void load();

	/** Forcibly saves the configuration file */
	public void save();

	public String getString(String property, String defaultValue);

	public String getString(String property);
	
	public int getInteger(String property);

	public int getInteger(String property, int defaultValue);

	public boolean getBoolean(String property);
	
	public boolean getBoolean(String property, boolean defaultValue);

	public float getFloat(String property);

	public float getFloat(String property, float defaultValue);

	public double getDouble(String property);

	public double getDouble(String property, double defaultValue);

	public long getLong(String property);
	
	public long getLong(String property, long defaultValue);

	public void setString(String property, String value);

	public void setInteger(String property, int value);

	public void setLong(String property, long value);

	public void setDouble(String property, double value);

	public void setFloat(String property, float value);

	public boolean isFieldSet(String string);
	
	public void removeFieldValue(String string);
	
	public Iterator<String> getFieldsSet();

}