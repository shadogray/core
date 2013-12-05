package org.jboss.forge.addon.scaffold.faces;

import java.io.File;

import javax.inject.Inject;

import org.jboss.forge.addon.convert.Converter;
import org.jboss.forge.addon.javaee.jpa.JPAFacet;
import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.projects.facets.WebResourcesFacet;
import org.jboss.forge.addon.resource.DirectoryResource;
import org.jboss.forge.addon.resource.FileResource;
import org.jboss.forge.addon.resource.Resource;
import org.jboss.forge.addon.resource.ResourceFactory;
import org.jboss.forge.addon.resource.util.ResourceUtil;
import org.jboss.forge.addon.scaffold.spi.ResourceCollection;
import org.jboss.forge.addon.scaffold.spi.ScaffoldGenerationContext;
import org.jboss.forge.addon.ui.context.UIBuilder;
import org.jboss.forge.addon.ui.context.UIContext;
import org.jboss.forge.addon.ui.context.UIExecutionContext;
import org.jboss.forge.addon.ui.context.UIValidationContext;
import org.jboss.forge.addon.ui.input.UIInput;
import org.jboss.forge.addon.ui.input.UISelectMany;
import org.jboss.forge.addon.ui.metadata.UICommandMetadata;
import org.jboss.forge.addon.ui.metadata.WithAttributes;
import org.jboss.forge.addon.ui.result.NavigationResult;
import org.jboss.forge.addon.ui.result.Result;
import org.jboss.forge.addon.ui.util.Metadata;
import org.jboss.forge.addon.ui.wizard.UIWizardStep;
import org.jboss.forge.parser.java.JavaClass;
import org.jboss.shrinkwrap.descriptor.api.persistence.PersistenceCommonDescriptor;

public class ScaffoldableEntitySelectionWizard implements UIWizardStep
{
   @Inject
   @WithAttributes(label = "Page Template")
   private UIInput<File> pageTemplate;
   
   @Inject
   @WithAttributes(label = "Targets", required = true)
   private UISelectMany<JavaClass> targets;
   
   @Inject
   private ResourceFactory resourceFactory;
   
   @Override
   public NavigationResult next(UIContext context) throws Exception
   {
      ResourceCollection resourceCollection = new ResourceCollection();
      for (JavaClass klass : targets.getValue())
      {
         resourceCollection.addToCollection(klass);
      }
      context.setAttribute(ResourceCollection.class, resourceCollection );
      ScaffoldGenerationContext genCtx = (ScaffoldGenerationContext) context.getAttribute(ScaffoldGenerationContext.class);
      genCtx.addAttribute("pageTemplate", resourceFactory.create(FileResource.class, pageTemplate));
      return null;
   }

   @Override
   public UICommandMetadata getMetadata(UIContext context)
   {
      return Metadata.forCommand(getClass()).name("Select JPA entities").description("Select the JPA entities to be used for scaffolding.");
   }

   @Override
   public boolean isEnabled(UIContext context)
   {
      return true;
   }

   @Override
   public void initializeUI(UIBuilder builder) throws Exception
   {
      UIContext context = builder.getUIContext();
      
      Project project = (Project) context.getAttribute(Project.class);
      JPAFacet<PersistenceCommonDescriptor> persistenceFacet = project.getFacet(JPAFacet.class);
      targets.setValueChoices(persistenceFacet.getAllEntities());
      targets.setItemLabelConverter(new Converter<JavaClass, String>()
      {
         @Override
         public String convert(JavaClass source)
         {
            return source == null ? null : source.getQualifiedName();
         }
      });
      builder.add(pageTemplate).add(targets);
   }

   @Override
   public Result execute(UIExecutionContext context) throws Exception
   {
      return null;
   }

   @Override
   public void validate(UIValidationContext context)
   {
      File inputTemplate = pageTemplate.getValue();
      if (inputTemplate != null)
      {
         Resource<?> template = resourceFactory.create(FileResource.class, inputTemplate);
         if (template.exists())
         {
            UIContext uiContext = context.getUIContext();
            Project project = (Project) uiContext.getAttribute(Project.class);
            WebResourcesFacet web = project.getFacet(WebResourcesFacet.class);
            boolean isValidTemplate = false;
            for (DirectoryResource dir : web.getWebRootDirectories())
            {
               if (ResourceUtil.isChildOf(dir, template))
               {
                  isValidTemplate = true;
               }
            }
            if (!isValidTemplate)
            {
               context.addValidationError(pageTemplate, "Not a valid template resource. "
                        + "The template should be located under a web root directory for the project.");
            }
         }
         else
         {
            context.addValidationError(pageTemplate, "The template [" + template.getName()
                     + "] does not exist. You must select a template that exists, or use "
                     + "the default template (do not specify a template.)");
         }
      }
   }

}