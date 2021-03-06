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

package consulo.unity3d.jsonApi;

import org.jetbrains.annotations.NotNull;
import com.intellij.util.ui.UIUtil;
import consulo.buildInWebServer.api.JsonPostRequestHandler;
import consulo.unity3d.UnityConsoleService;

/**
 * @author VISTALL
 * @since 07-Jun-16
 */
public class UnityPlayStateHandler extends JsonPostRequestHandler<UnityPlayStateHandlerRequest>
{
	public UnityPlayStateHandler()
	{
		super("unityPlayState", UnityPlayStateHandlerRequest.class);
	}

	@NotNull
	@Override
	public JsonResponse handle(@NotNull final UnityPlayStateHandlerRequest request)
	{
		if(request.isPlaying)
		{
			UIUtil.invokeLaterIfNeeded(new Runnable()
			{
				@Override
				public void run()
				{
					UnityConsoleService consoleService = UnityConsoleService.findByProjectPath(request.projectPath);
					if(consoleService != null)
					{
						consoleService.onPlay();
					}
				}
			});
		}
		return JsonResponse.asSuccess(null);
	}
}
