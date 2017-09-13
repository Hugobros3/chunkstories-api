package io.xol.chunkstories.api.plugin.commands;

import io.xol.chunkstories.api.GameContext;

//(c) 2015-2017 XolioWare Interactive
//http://chunkstories.xyz
//http://xol.io

/** Describes a command installed by the game itself */
public class SystemCommand extends Command {

	final GameContext gameContext;
	
	public SystemCommand(GameContext gameContext, String name) {
		super(name);
		
		this.gameContext = gameContext;
	}

	public GameContext getGameContext() {
		return gameContext;
	}

}
