package com.orientechnologies.orient.core.storage.impl.local.paginated.wal.po.cellbtree.multivalue.v3.bucket;

import com.orientechnologies.common.directmemory.OByteBufferPool;
import com.orientechnologies.common.directmemory.OPointer;
import com.orientechnologies.orient.core.storage.cache.OCacheEntry;
import com.orientechnologies.orient.core.storage.cache.OCacheEntryImpl;
import com.orientechnologies.orient.core.storage.cache.OCachePointer;
import com.orientechnologies.orient.core.storage.impl.local.paginated.wal.OOperationUnitId;
import com.orientechnologies.orient.core.storage.impl.local.paginated.wal.po.PageOperationRecord;
import com.orientechnologies.orient.core.storage.index.sbtree.multivalue.v3.CellBTreeMultiValueV3Bucket;
import org.junit.Assert;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.List;

public class CellBTreeMultiValueV3BucketSetRightSiblingPOTest {
  @Test
  public void testRedo() {
    final int pageSize = 64 * 1024;
    final OByteBufferPool byteBufferPool = new OByteBufferPool(pageSize);
    try {
      final OPointer pointer = byteBufferPool.acquireDirect(false);
      final OCachePointer cachePointer = new OCachePointer(pointer, byteBufferPool, 0, 0);
      final OCacheEntry entry = new OCacheEntryImpl(0, 0, cachePointer);

      CellBTreeMultiValueV3Bucket<Byte> bucket = new CellBTreeMultiValueV3Bucket<>(entry);
      bucket.init(true);

      bucket.setRightSibling(42);

      entry.clearPageOperations();

      final OPointer restoredPointer = byteBufferPool.acquireDirect(false);
      final OCachePointer restoredCachePointer = new OCachePointer(restoredPointer, byteBufferPool, 0, 0);
      final OCacheEntry restoredCacheEntry = new OCacheEntryImpl(0, 0, restoredCachePointer);

      final ByteBuffer originalBuffer = cachePointer.getBufferDuplicate();
      final ByteBuffer restoredBuffer = restoredCachePointer.getBufferDuplicate();

      Assert.assertNotNull(originalBuffer);
      Assert.assertNotNull(restoredBuffer);

      restoredBuffer.put(originalBuffer);

      bucket.setRightSibling(24);

      final List<PageOperationRecord> operations = entry.getPageOperations();
      Assert.assertEquals(1, operations.size());

      Assert.assertTrue(operations.get(0) instanceof CellBTreeMultiValueV3BucketSetRightSiblingPO);

      final CellBTreeMultiValueV3BucketSetRightSiblingPO pageOperation = (CellBTreeMultiValueV3BucketSetRightSiblingPO) operations
          .get(0);

      CellBTreeMultiValueV3Bucket<Byte> restoredBucket = new CellBTreeMultiValueV3Bucket<>(restoredCacheEntry);

      Assert.assertEquals(42, restoredBucket.getRightSibling());

      pageOperation.redo(restoredCacheEntry);

      Assert.assertEquals(24, restoredBucket.getRightSibling());

      byteBufferPool.release(pointer);
      byteBufferPool.release(restoredPointer);
    } finally {
      byteBufferPool.clear();
    }
  }

  @Test
  public void testUndo() {
    final int pageSize = 64 * 1024;

    final OByteBufferPool byteBufferPool = new OByteBufferPool(pageSize);
    try {
      final OPointer pointer = byteBufferPool.acquireDirect(false);
      final OCachePointer cachePointer = new OCachePointer(pointer, byteBufferPool, 0, 0);
      final OCacheEntry entry = new OCacheEntryImpl(0, 0, cachePointer);

      CellBTreeMultiValueV3Bucket<Byte> bucket = new CellBTreeMultiValueV3Bucket<>(entry);
      bucket.init(true);

      bucket.setRightSibling(42);

      entry.clearPageOperations();

      bucket.setRightSibling(24);

      final List<PageOperationRecord> operations = entry.getPageOperations();

      Assert.assertTrue(operations.get(0) instanceof CellBTreeMultiValueV3BucketSetRightSiblingPO);
      final CellBTreeMultiValueV3BucketSetRightSiblingPO pageOperation = (CellBTreeMultiValueV3BucketSetRightSiblingPO) operations
          .get(0);

      final CellBTreeMultiValueV3Bucket<Byte> restoredBucket = new CellBTreeMultiValueV3Bucket<>(entry);

      Assert.assertEquals(24, restoredBucket.getRightSibling());

      pageOperation.undo(entry);

      Assert.assertEquals(42, restoredBucket.getRightSibling());

      byteBufferPool.release(pointer);
    } finally {
      byteBufferPool.clear();
    }
  }

  @Test
  public void testSerialization() {
    OOperationUnitId operationUnitId = OOperationUnitId.generateId();

    CellBTreeMultiValueV3BucketSetRightSiblingPO operation = new CellBTreeMultiValueV3BucketSetRightSiblingPO(12, 21);

    operation.setFileId(42);
    operation.setPageIndex(24);
    operation.setOperationUnitId(operationUnitId);

    final int serializedSize = operation.serializedSize();
    final byte[] stream = new byte[serializedSize + 1];
    int pos = operation.toStream(stream, 1);

    Assert.assertEquals(serializedSize + 1, pos);

    CellBTreeMultiValueV3BucketSetRightSiblingPO restoredOperation = new CellBTreeMultiValueV3BucketSetRightSiblingPO();
    restoredOperation.fromStream(stream, 1);

    Assert.assertEquals(42, restoredOperation.getFileId());
    Assert.assertEquals(24, restoredOperation.getPageIndex());
    Assert.assertEquals(operationUnitId, restoredOperation.getOperationUnitId());

    Assert.assertEquals(12, restoredOperation.getSibling());
    Assert.assertEquals(21, restoredOperation.getPrevSibling());
  }
}
