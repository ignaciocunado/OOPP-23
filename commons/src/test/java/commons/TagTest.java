/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package commons;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TagTest {

	private Tag tag;

	@BeforeEach
	public void setup() {
		tag = new Tag("Bugfix", 16744576);
	}

	@Test
	public void emptyConstructorTest() {
		new Tag();
	}

	@Test
	public void getIdTest() {
		assertEquals(tag.getId(), 0);
	}

	@Test
	public void getNameTest() {
		assertEquals(tag.getName(), "Bugfix");
	}

	@Test
	public void getColourTest() {
		assertEquals(tag.getColour(), 16744576);
	}

	@Test
	public void setNameTest() {
		tag.setName("Hotfix");
		assertEquals(tag.getName(), "Hotfix");
	}

	@Test
	public void setColourTest() {
		tag.setColour(0);
		assertEquals(tag.getColour(), 0);
	}

	@Test
	public void equalsHashCode() {
		final Tag tag1 = new Tag("Bugfix", 16744576);
		final Tag tag2 = new Tag("Bugfix", 16744576);

		assertEquals(tag1, tag2);
		assertEquals(tag1.hashCode(), tag2.hashCode());
	}

	@Test
	public void hasToString() {
		assertEquals(tag.toString(), "<Tag id=0 name=Bugfix colour=16744576>");
	}
}