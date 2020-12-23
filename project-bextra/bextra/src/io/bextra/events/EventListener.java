package io.bextra.events;

import java.nio.file.Path;

public interface EventListener {
	public void update(Path filePath);
}
