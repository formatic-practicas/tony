/*
 * This file is part of SpoutAPI.
 *
 * Copyright (c) 2011-2012, Spout LLC <http://www.spout.org/>
 * SpoutAPI is licensed under the Spout License Version 1.
 *
 * SpoutAPI is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the Spout License Version 1.
 *
 * SpoutAPI is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for
 * more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the Spout License Version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://spout.in/licensev1> for the full license, including
 * the MIT license.
 */
package org.spout.api.datatable.delta;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.spout.api.datatable.SerializableHashMap;

/**
 * This is a subclass of SerializableHashMap designed to mark delta elements. This is most needed for sub-maps.
 * This map is serializable, however SerializableHashMap is not. Therefore, the no-arg constructor of SerializableHashMap
 * is called. This no-arg constructor should be nonsyncing. The GenericDatatableMap of super does not serialize the data
 * by itself, so the writeObject and readObject are implemented in this class to serialize the data.
 * 
 * This classes manages setting the delta map of parent maps.
 * This class supports null values, whereas SerializableHashMap does not.
 *
 */
public class DeltaMap extends SerializableHashMap {
	private static final long serialVersionUID = 1L;
	private transient WeakReference<DeltaMap> reference = new WeakReference<DeltaMap>(this);
	private DeltaType type;
	private final String key;

	// If we have a parent, we aren't going to serialize it
	protected transient final DeltaMap parent;
	protected transient List<WeakReference<DeltaMap>> children = new ArrayList<WeakReference<DeltaMap>>();

	public DeltaMap(DeltaType type) {
		this.type = type;
		this.key = null;
		this.parent = null;
	}

	public DeltaMap(DeltaMap parent, DeltaType type, String key) {
		this.type = type;
		this.parent = parent;
		this.parent.children.add(new WeakReference<DeltaMap>(this));
		this.key = key;
		
		// We want to update the parent for us
		this.parent.put(key, this);
	}

	public enum DeltaType {
		/*
		 * Equivalent of calling clear() the putAll()
		 */
		REPLACE,
		/*
		 * Equivalent of calling putAll()
		 */
		SET;
	}

	public DeltaType getType() {
		return type;
	}

	public void setType(DeltaType type) {
		this.type = type;
	}

	// SerializableHashMap does not permit null values, we do. Therefore, we need to override functionality
	@Override
	public Serializable putIfAbsent(String key, Serializable value) {
		updateParent();
		return map.putIfAbsent(key, value);
	}

	@Override
	public Serializable put(String key, Serializable value) {
		updateParent();
		return map.put(key, value);
	}

	@Override
	public Serializable remove(String key) {
		updateParent();
		return map.remove(key);
	}

	@Override
	public void clear() {
		updateParent();
		for (String key : map.keySet()) {
			map.put(key, null);
		}
	}

	@Override
	public void deserialize(byte[] data, boolean wipe) throws IOException {
		updateParent();
		super.deserialize(data, wipe);
	}
	
	
	private void updateParent() {
		if (parent != null) {
			parent.put(this.key, this);
			parent.children.add(reference);
		}
	}

	public void reset() {
		type = DeltaType.SET;
		map.clear();
		for (Iterator<WeakReference<DeltaMap>> it = children.iterator(); it.hasNext();) {
			WeakReference<DeltaMap> c = it.next();
			if (c.get() == null) {
				it.remove();
				continue;
			}
			c.get().reset();
		}
	}
	
	private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
		s.defaultReadObject();
		reference = new WeakReference<DeltaMap>(this);
		children = new ArrayList<WeakReference<DeltaMap>>();
	}
}
