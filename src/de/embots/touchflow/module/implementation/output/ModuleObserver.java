package de.embots.touchflow.module.implementation.output;

import de.embots.touchflow.module.core.Module;

public interface ModuleObserver {
	void moduleUpdate(Module sender);
}
