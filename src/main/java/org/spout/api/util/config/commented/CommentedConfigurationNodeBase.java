/*
 * This file is part of SpoutAPI (http://www.spout.org/).
 *
 * SpoutAPI is licensed under the SpoutDev License Version 1.
 *
 * SpoutAPI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the SpoutDev License Version 1.
 *
 * SpoutAPI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the SpoutDev License Version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package org.spout.api.util.config.commented;

import org.spout.api.util.config.Configuration;
import org.spout.api.util.config.ConfigurationNodeBase;

/**
 * @author zml2008
 */
public class CommentedConfigurationNodeBase extends ConfigurationNodeBase {
	public static final String LINE_SEPARATOR;
	static {
		String sep = System.getProperty("line.separator");
		if (sep == null) {
			sep = "\n";
		}
		LINE_SEPARATOR = sep;
	}
	private String[] comment;

	public CommentedConfigurationNodeBase(Configuration config, Object value, String[] path) {
		super(config, value, path);
	}

	/**
	 * Returns the comment lines attached to this configuration node
	 * Will return null if this node doesn't have a comment
	 * @return The comment for this node
	 */
	public String[] getComment() {
		return comment;
	}

	/**
	 * Sets the comment this attached to this configuration node.
	 * IN this method the comment is provided as one line, containing the line separator character
	 * @param comment
	 */
	public void setComment(String comment) {
		checkAdded();
		this.comment = comment.split(LINE_SEPARATOR);
	}
	public void setComment(String... comment) {
		checkAdded();
		this.comment = comment;
	}

	@Override
	public CommentedConfigurationNodeBase createConfigurationNode(String[] path, Object value) {
		return new CommentedConfigurationNodeBase(getConfiguration(), value, path);
	}
}
