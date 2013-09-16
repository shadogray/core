/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.javaee.faces;

import org.jboss.forge.addon.javaee.JavaEEFacet;
import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.resource.FileResource;

/**
 * If installed, this {@link Project} supports features from the JSF specification.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public interface FacesFacet extends JavaEEFacet
{
   /**
    * Returns the {@link FileResource} of the descriptor
    * 
    * @return
    */
   FileResource<?> getConfigFile();
}