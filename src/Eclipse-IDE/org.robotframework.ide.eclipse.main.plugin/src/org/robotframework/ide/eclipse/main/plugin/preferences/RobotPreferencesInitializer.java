package org.robotframework.ide.eclipse.main.plugin.preferences;

import java.util.List;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.robotframework.ide.core.executor.RobotRuntimeEnvironment;
import org.robotframework.ide.core.executor.RobotRuntimeEnvironment.PythonInstallationDirectory;
import org.robotframework.ide.eclipse.main.plugin.RobotFramework;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;

public class RobotPreferencesInitializer extends AbstractPreferenceInitializer {

    @Override
    public void initializeDefaultPreferences() {
        final IScopeContext scope = DefaultScope.INSTANCE;
        final IEclipsePreferences preferences = scope.getNode(RobotFramework.PLUGIN_ID);

        final List<PythonInstallationDirectory> pybotPaths = RobotRuntimeEnvironment.whereArePythonInterpreters();
        if (!pybotPaths.isEmpty()) {
            final String activePath = pybotPaths.get(0).getAbsolutePath();
            final String allPaths = Joiner.on(';').join(
                    Iterables.transform(pybotPaths, new Function<PythonInstallationDirectory, String>() {
                        @Override
                        public String apply(final PythonInstallationDirectory dir) {
                            return dir.getAbsolutePath();
                        }
                    }));
            
            preferences.put(InstalledRobotEnvironments.ACTIVE_RUNTIME, activePath);
            preferences.put(InstalledRobotEnvironments.OTHER_RUNTIMES, allPaths);
        }
    }

}
