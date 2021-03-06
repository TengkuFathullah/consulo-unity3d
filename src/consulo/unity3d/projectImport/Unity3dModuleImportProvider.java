/*
 * Copyright 2013-2016 consulo.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package consulo.unity3d.projectImport;

import java.util.Arrays;
import java.util.List;

import javax.swing.Icon;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.openapi.application.WriteAction;
import com.intellij.openapi.module.ModifiableModuleModel;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.roots.ui.configuration.ModulesProvider;
import com.intellij.openapi.startup.StartupManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.packaging.artifacts.ModifiableArtifactModel;
import consulo.moduleImport.ModuleImportProvider;
import consulo.unity3d.Unity3dIcons;
import consulo.unity3d.jsonApi.UnityOpenFilePostHandlerRequest;
import consulo.unity3d.projectImport.ui.Unity3dWizardStep;

/**
 * @author VISTALL
 * @since 29.12.14
 */
public class Unity3dModuleImportProvider implements ModuleImportProvider<UnityModuleImportContext>
{
	@NotNull
	@Override
	public UnityModuleImportContext createContext()
	{
		return new UnityModuleImportContext();
	}

	@NotNull
	@Override
	public String getName()
	{
		return "Unity3D";
	}

	@Nullable
	@Override
	public Icon getIcon()
	{
		return Unity3dIcons.Unity3d;
	}

	@Override
	public boolean canImport(@NotNull VirtualFile fileOrDirectory)
	{
		return fileOrDirectory.isDirectory() && fileOrDirectory.findFileByRelativePath("ProjectSettings/ProjectSettings.asset") != null;
	}

	@NotNull
	@Override
	public List<Module> commit(@NotNull UnityModuleImportContext context,
			@NotNull Project project,
			@Nullable ModifiableModuleModel originalModel,
			@NotNull ModulesProvider modulesProvider,
			@Nullable ModifiableArtifactModel artifactModel)
	{
		Sdk unitySdk = context.getSdk();
		UnityOpenFilePostHandlerRequest requestor = context.getRequestor();

		boolean fromProjectStructure = originalModel != null;

		final ModifiableModuleModel newModel = fromProjectStructure ? originalModel : ModuleManager.getInstance(project).getModifiableModel();

		Module rootModule = newModel.newModule(project.getName(), project.getBasePath());

		if(!fromProjectStructure)
		{
			WriteAction.run(newModel::commit);
		}

		StartupManager.getInstance(project).registerPostStartupActivity(() -> Unity3dProjectUtil.syncProjectStep1(project, unitySdk, requestor, false));
		return Arrays.asList(rootModule);
	}

	@NotNull
	@Override
	public String getFileSample()
	{
		return "<b>Unity3D</b> project";
	}

	@Override
	public ModuleWizardStep[] createSteps(@NotNull WizardContext context, @NotNull UnityModuleImportContext moduleImportContext)
	{
		return new ModuleWizardStep[]{
				new Unity3dWizardStep(moduleImportContext, context)
		};
	}
}
