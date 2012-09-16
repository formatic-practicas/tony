/*
 * This file is part of SpoutAPI.
 *
 * Copyright (c) 2011-2012, SpoutDev <http://www.spout.org/>
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
package org.spout.api.inventory.util;

/**
 * Represents a grid that can be iterated through in the correct order of an {@link org.spout.api.inventory.Inventory}
 */
public class Grid implements Iterable<Integer> {
	/**
	 * The length of the grid
	 */
	private final int length;
	/**
	 * The width of the grid
	 */
	private final int width;

	/**
	 * Constructs a new grid object
	 * @param length of the grid
	 * @param width of the grid
	 */
	public Grid(int length, int width) {
		this.length = length;
		this.width = width;
	}

	/**
	 * Gets the length of the grid
	 * @return length
	 */
	public int getLength() {
		return length;
	}

	/**
	 * Gets the width of the grid
	 * @return width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Gets the size of the grid
	 * @return the size of the grid
	 */
	public int getSize() {
		return length * width;
	}

	@Override
	public GridIterator iterator() {
		return new GridIterator(this);
	}
}
