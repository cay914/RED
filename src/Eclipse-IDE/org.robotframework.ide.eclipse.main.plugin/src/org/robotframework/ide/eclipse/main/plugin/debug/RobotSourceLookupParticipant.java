/*
 * Copyright 2015 Nokia Solutions and Networks
 * Licensed under the Apache License, Version 2.0,
 * see license.txt file for details.
 */
package org.robotframework.ide.eclipse.main.plugin.debug;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.sourcelookup.AbstractSourceLookupParticipant;
import org.robotframework.ide.eclipse.main.plugin.debug.model.RobotStackFrame;

public class RobotSourceLookupParticipant extends AbstractSourceLookupParticipant {

    /*
     * (non-Javadoc)
     * @see
     * org.eclipse.debug.internal.core.sourcelookup.ISourceLookupParticipant#getSourceName(java.
     * lang.Object)
     */
    public String getSourceName(Object object) throws CoreException {
        if (object instanceof RobotStackFrame) {
            return ((RobotStackFrame) object).getSourceName();
        }
        return null;
    }

}
