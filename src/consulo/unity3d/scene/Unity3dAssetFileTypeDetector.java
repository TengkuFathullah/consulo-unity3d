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

package consulo.unity3d.scene;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeRegistry;
import com.intellij.openapi.util.io.ByteSequence;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.ArrayUtil;

/**
 * @author VISTALL
 * @since 09.08.2015
 */
public class Unity3dAssetFileTypeDetector implements FileTypeRegistry.FileTypeDetector
{
	public static final String[] ourAssetExtensions = {
			"unity",
			"prefab",
			"physicsMaterial2D"
	};

	@Nullable
	@Override
	public FileType detect(@NotNull VirtualFile file, @NotNull ByteSequence firstBytes, @Nullable CharSequence firstCharsIfText)
	{
		if(ArrayUtil.contains(file.getExtension(), ourAssetExtensions))
		{
			if(firstCharsIfText == null)
			{
				return Unity3dBinaryAssetFileType.INSTANCE;
			}
			if(firstCharsIfText.length() > 5)
			{
				CharSequence sequence = firstCharsIfText.subSequence(0, 5);
				if(StringUtil.equals("%YAML", sequence))
				{
					return Unity3dYMLAssetFileType.INSTANCE;
				}
			}

			return Unity3dBinaryAssetFileType.INSTANCE;
		}
		return null;
	}

	@Override
	public int getVersion()
	{
		return 4;
	}
}
