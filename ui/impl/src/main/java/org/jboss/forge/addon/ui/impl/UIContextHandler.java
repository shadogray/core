/**
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.ui.impl;

import org.jboss.forge.addon.ui.context.UIContext;
import org.jboss.forge.addon.ui.context.UIContextProvider;

/**
 * Holds the current {@link UIContext} instance
 * 
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
public class UIContextHandler implements UIContextProvider
{
   @Override
   public UIContext getUIContext()
   {
      return CommandScopedContext.getCurrentContext();
   }

}
