//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.entity.ai;

import xyz.chunkstories.api.entity.Entity;

import javax.annotation.Nullable;

public abstract class AI<T extends Entity> {
	protected T entity;
	@Nullable
	protected AiTask currentTask;

	public abstract class AiTask {

		public abstract void execute();
	}

	public AI(T entity) {
		this.entity = entity;
	}

	public void tick() {
		if (currentTask != null)
			currentTask.execute();
	}

	public void setAiTask(@Nullable AiTask nextTask) {
		this.currentTask = nextTask;
	}

	@Nullable
	public AiTask currentTask() {
		return currentTask;
	}
}
