/*
 * Copyright 2010-2016 OrientDB LTD (http://orientdb.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.orientechnologies.common.serialization.types;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Random;

/**
 * @author Ilya Bershadskiy (ibersh20-at-gmail.com)
 * @since 19.01.12
 */
public class StringSerializerTest {
  byte[] stream;
  private int               FIELD_SIZE;
  private String            OBJECT;
  private OStringSerializer stringSerializer;

  @Before
  public void beforeClass() {
    stringSerializer = new OStringSerializer();
    Random random = new Random();
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < random.nextInt(20) + 5; i++) {
      sb.append((char) random.nextInt());
    }
    OBJECT = sb.toString();
    FIELD_SIZE = OBJECT.length() * 2 + 4 + 7;
    stream = new byte[FIELD_SIZE];
  }

  @Test
  public void testFieldSize() {
    Assert.assertEquals(stringSerializer.getObjectSize(OBJECT), FIELD_SIZE - 7);
  }

  @Test
  public void testSerialize() {
    stringSerializer.serialize(OBJECT, stream, 7);
    Assert.assertEquals(stringSerializer.deserialize(stream, 7), OBJECT);
  }

  @Test
  public void testSerializeNative() {
    stringSerializer.serializeNativeObject(OBJECT, stream, 7);
    Assert.assertEquals(stringSerializer.deserializeNativeObject(stream, 7), OBJECT);
  }

  @Test
  public void testNativeDirectMemoryCompatibility() {
    stringSerializer.serializeNativeObject(OBJECT, stream, 7);

    ByteBuffer buffer = ByteBuffer.allocateDirect(stream.length).order(ByteOrder.nativeOrder());
    buffer.put(stream);
    buffer.position(7);

    Assert.assertEquals(stringSerializer.deserializeFromByteBufferObject(buffer), OBJECT);
  }

  @Test
  public void testSerializeInByteBuffer() {
    final int serializationOffset = 5;
    final ByteBuffer buffer = ByteBuffer.allocate(FIELD_SIZE + serializationOffset);

    buffer.position(serializationOffset);
    stringSerializer.serializeInByteBufferObject(OBJECT, buffer);

    final int binarySize = buffer.position() - serializationOffset;
    Assert.assertEquals(binarySize, FIELD_SIZE - 7);

    buffer.position(serializationOffset);
    Assert.assertEquals(stringSerializer.getObjectSizeInByteBuffer(buffer), FIELD_SIZE - 7);

    buffer.position(serializationOffset);
    Assert.assertEquals(stringSerializer.deserializeFromByteBufferObject(buffer), OBJECT);

    Assert.assertEquals(buffer.position() - serializationOffset, FIELD_SIZE - 7);
  }
}
