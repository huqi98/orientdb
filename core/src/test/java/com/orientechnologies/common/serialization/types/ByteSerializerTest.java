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

/**
 * @author Ilya Bershadskiy (ibersh20-at-gmail.com)
 * @since 18.01.12
 */

public class ByteSerializerTest {
  private static final int FIELD_SIZE = 1;
  byte[] stream = new byte[FIELD_SIZE];
  private static final Byte OBJECT = 1;
  private OByteSerializer byteSerializer;

  @Before
  public void beforeClass() {
    byteSerializer = new OByteSerializer();
  }

  @Test
  public void testFieldSize() {
    Assert.assertEquals(byteSerializer.getObjectSize(null), FIELD_SIZE);
  }

  @Test
  public void testSerialize() {
    byteSerializer.serialize(OBJECT, stream, 0);
    Assert.assertEquals(byteSerializer.deserialize(stream, 0), OBJECT);
  }

  @Test
  public void testSerializeNative() {
    byteSerializer.serializeNative(OBJECT, stream, 0);
    Assert.assertEquals(byteSerializer.deserializeNativeObject(stream, 0), OBJECT);
  }

  @Test
  public void testNativeDirectMemoryCompatibility() {
    byteSerializer.serializeNative(OBJECT, stream, 0);

    final ByteBuffer buffer = ByteBuffer.allocateDirect(stream.length).order(ByteOrder.nativeOrder());
    buffer.position(0);
    buffer.put(stream);
    buffer.position(0);

    Assert.assertEquals(byteSerializer.deserializeFromByteBufferObject(buffer), OBJECT);
  }

  @Test
  public void testSerializeInByteBuffer() {
    final int serializationOffset = 5;

    final ByteBuffer buffer = ByteBuffer.allocate(FIELD_SIZE + serializationOffset);
    buffer.position(serializationOffset);
    byteSerializer.serializeInByteBufferObject(OBJECT, buffer);

    final int binarySize = buffer.position() - serializationOffset;
    Assert.assertEquals(binarySize, FIELD_SIZE);

    buffer.position(serializationOffset);
    Assert.assertEquals(byteSerializer.getObjectSizeInByteBuffer(buffer), FIELD_SIZE);

    buffer.position(serializationOffset);
    final Byte result = byteSerializer.deserializeFromByteBufferObject(buffer);

    Assert.assertEquals(result, OBJECT);
  }
}
