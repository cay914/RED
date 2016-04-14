/*
 * Copyright 2015 Nokia Solutions and Networks
 * Licensed under the Apache License, Version 2.0,
 * see license.txt file for details.
 */
package org.robotframework.ide.eclipse.main.plugin.project.build.libs;

import org.eclipse.core.resources.IFile;
import org.rf.ide.core.executor.EnvironmentSearchPaths;
import org.rf.ide.core.executor.RobotRuntimeEnvironment;
import org.rf.ide.core.executor.RobotRuntimeEnvironment.RobotEnvironmentException;

class StandardLibraryLibdocGenerator implements ILibdocGenerator {

    private final IFile targetSpecFile;

    StandardLibraryLibdocGenerator(final IFile targetSpecFile) {
        this.targetSpecFile = targetSpecFile;
    }

    @Override
    public void generateLibdoc(final RobotRuntimeEnvironment runtimeEnvironment,
            final EnvironmentSearchPaths additionalPaths) throws RobotEnvironmentException {
        runtimeEnvironment.createLibdocForStdLibrary(getLibraryName(), targetSpecFile.getLocation().toFile());
    }

    @Override
    public void generateLibdocForcibly(final RobotRuntimeEnvironment runtimeEnvironment,
            final EnvironmentSearchPaths additionalPaths)
            throws RobotEnvironmentException {
        runtimeEnvironment.createLibdocForStdLibraryForcibly(getLibraryName(), targetSpecFile.getLocation().toFile());
    }

    protected String getLibraryName() {
        return targetSpecFile.getFullPath().removeFileExtension().lastSegment();
    }

    @Override
    public String getMessage() {
        return "generating libdoc for " + getLibraryName() + " library";
    }
}
