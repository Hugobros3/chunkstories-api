package io.xol.chunkstories.api.util;

import java.util.Collection;

import io.xol.chunkstories.api.content.Definition;
import io.xol.chunkstories.api.input.KeyboardKeyInput;

/** 
 * New fancy way of handling parameters: Options are defined in a NWP file in the loaded mods ressources.<br/> 
 * Any mod can declare it's own configuration options.
 * Configuration options can be quite deeply customized : be marked for only client/server, 
 * require a rendering passes reconfiguration, etc
 * */
public interface Configuration {
	
	public interface Option extends Definition {
		public String getName();
		
		public String getValue();
		
		public String getDefaultValue();
		
		/** 
		 * Try setting the value of that option to the argument. 
		 * Will fire an EventOptionSet
		 */
		public void trySetting(String value);
	}

	/** Created when the 'type' property resolves to 'toggle' or 'bool' or 'boolean' */
	public interface OptionBoolean extends Option {
		public boolean getBoolValue();
		
		public void toggle();
	}

	/** Created when the 'type' property resolves to 'int' */
	public interface OptionInt extends Option {
		public int getIntValue();
	}

	/** Created when the 'type' property resolves to 'double' */
	public interface OptionDouble extends Option {
		public double getDoubleValue();
	}
	
	/** Created when the 'type' property resolves to 'scale' */
	public interface ScaleOption extends OptionDouble {
		public double getMinimumValue();
		
		public double getMaximumValue();
		
		public double getGranularity();
	}
	
	/** Created when the 'type' property resolves to 'choice' */
	public interface ChoiceOption extends Option {
		public Collection<String> getPossibleChoices();
	}
	
	/** Created when an input is *not* declared using the 'hidden' flag in a .inputs file! */
	public interface KeyBindOption extends OptionInt {
		public KeyboardKeyInput getInput();
	}
	
	/** Looks for a certain option. May return null. Will return the default value if not set. */
	public Option getOption(String optionName);
	
	public boolean getBooleanOption(String optionName);
	
	public int getIntOption(String optionName);
	
	public double getDoubleOption(String optionName);
	
	public String getStringOption(String optionName);
	
	public Collection<Option> allOptions();
}
