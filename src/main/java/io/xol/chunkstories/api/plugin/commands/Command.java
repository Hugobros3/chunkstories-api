//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.plugin.commands;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/** A command is a special token the game uses to call the execution of a certain tool */
public class Command
{
	protected final String name;
	protected final Set<String> aliases = new HashSet<String>();
	
	@Nullable
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
	public void setHandler(@Nullable CommandHandler commandHandler)
	{
		this.handler = commandHandler;
	}
	
	@Nullable
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
