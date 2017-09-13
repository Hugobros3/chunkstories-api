package io.xol.chunkstories.api.plugin.commands;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

//(c) 2015-2017 XolioWare Interactive
//http://chunkstories.xyz
//http://xol.io

/** A command is a special token the game uses to call the execution of a certain tool */
public class Command
{
	protected final String name;
	protected final Set<String> aliases = new HashSet<String>();
	
	protected CommandHandler handler = null;

	public Command(String name)
	{
		this.name = name;
		this.aliases.add(name);
	}
	
	public void addAlias(String alias)
	{
		this.aliases.add(alias);
	}
	
	public String getName()
	{
		return name;
	}
	public void setHandler(CommandHandler commandHandler)
	{
		this.handler = commandHandler;
	}
	
	public CommandHandler getHandler()
	{
		return this.handler;
	}

	@Override
	public boolean equals(Object o)
	{
		if(o instanceof String)
			return ((String)o).equals(name);
		return ((Command) o).name.equals(name);
	}

	@Override
	public int hashCode()
	{
		return name.hashCode();
	}

	public Collection<String> aliases()
	{
		return aliases;
	}
}
