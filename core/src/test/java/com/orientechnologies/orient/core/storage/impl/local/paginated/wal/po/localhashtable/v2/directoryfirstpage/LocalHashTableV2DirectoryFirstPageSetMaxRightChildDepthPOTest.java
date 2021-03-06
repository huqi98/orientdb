package com.orientechnologies.orient.core.storage.impl.local.paginated.wal.po.localhashtable.v2.directoryfirstpage;

import com.orientechnologies.common.directmemory.OByteBufferPool;
import com.orientechnologies.common.directmemory.OPointer;
import com.orientechnologies.orient.core.storage.cache.OCacheEntry;
import com.orientechnologies.orient.core.storage.cache.OCacheEntryImpl;
import com.orientechnologies.orient.core.storage.cache.OCachePointer;
import com.orientechnologies.orient.core.storage.impl.local.paginated.wal.po.PageOperationRecord;
import com.orientechnologies.orient.core.storage.index.hashindex.local.v2.DirectoryFirstPageV2;
import org.junit.Assert;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.List;

public class LocalHashTableV2DirectoryFirstPageSetMaxRightChildDepthPOTest {
  @Test
  public void testRedo() {
    final int pageSize = 64 * 1024;
    final OByteBufferPool byteBufferPool = new OByteBufferPool(pageSize);
    try {
      final OPointer pointer = byteBufferPool.acquireDirect(false);
      final OCachePointer cachePointer = new OCachePointer(pointer, byteBufferPool, 0, 0);
      final OCacheEntry entry = new OCacheEntryImpl(0, 0, cachePointer);

      DirectoryFirstPageV2 page = new DirectoryFirstPageV2(entry);
      page.setMaxRightChildDepth(2, (byte) 24);

      entry.clearPageOperations();

      final OPointer restoredPointer = byteBufferPool.acquireDirect(false);
      final OCachePointer restoredCachePointer = new OCachePointer(restoredPointer, byteBufferPool, 0, 0);
      final OCacheEntry restoredCacheEntry = new OCacheEntryImpl(0, 0, restoredCachePointer);

      final ByteBuffer originalBuffer = cachePointer.getBufferDuplicate();
      final ByteBuffer restoredBuffer = restoredCachePointer.getBufferDuplicate();

      Assert.assertNotNull(originalBuffer);
      Assert.assertNotNull(restoredBuffer);

      restoredBuffer.put(originalBuffer);

      page.setMaxRightChildDepth(2, (byte) 42);

      final List<PageOperationRecord> operations = entry.getPageOperations();
      Assert.assertEquals(1, operations.size());

      Assert.assertTrue(operations.get(0) instanceof LocalHashTableV2DirectoryFirstPageSetMaxRightChildDepthPO);

      final LocalHashTableV2DirectoryFirstPageSetMaxRightChildDepthPO pageOperation = (LocalHashTableV2DirectoryFirstPageSetMaxRightChildDepthPO) operations
          .get(0);

      DirectoryFirstPageV2 restoredPage = new DirectoryFirstPageV2(restoredCacheEntry);
      Assert.assertEquals(24, restoredPage.getMaxRightChildDepth(2));

      pageOperation.redo(restoredCacheEntry);

      Assert.assertEquals(42, restoredPage.getMaxRightChildDepth(2));

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

      DirectoryFirstPageV2 page = new DirectoryFirstPageV2(entry);
      page.setMaxRightChildDepth(2, (byte) 24);

      entry.clearPageOperations();

      page.setMaxRightChildDepth(2, (byte) 42);

      final List<PageOperationRecord> operations = entry.getPageOperations();
      Assert.assertEquals(1, operations.size());

      Assert.assertTrue(operations.get(0) instanceof LocalHashTableV2DirectoryFirstPageSetMaxRightChildDepthPO);

      final LocalHashTableV2DirectoryFirstPageSetMaxRightChildDepthPO pageOperation = (LocalHashTableV2DirectoryFirstPageSetMaxRightChildDepthPO) operations
          .get(0);

      final DirectoryFirstPageV2 restoredPage = new DirectoryFirstPageV2(entry);

      Assert.assertEquals(42, restoredPage.getMaxRightChildDepth(2));

      pageOperation.undo(entry);

      Assert.assertEquals(24, restoredPage.getMaxRightChildDepth(2));

      byteBufferPool.release(pointer);
    } finally {
      byteBufferPool.clear();
    }
  }

  @Test
  public void testSerialization() {
    LocalHashTableV2DirectoryFirstPageSetMaxRightChildDepthPO operation = new LocalHashTableV2DirectoryFirstPageSetMaxRightChildDepthPO(
        2, (byte) 12, (byte) 21);

    operation.setFileId(42);
    operation.setPageIndex(24);
    operation.setOperationUnitId(1);

    final int serializedSize = operation.serializedSize();
    final byte[] stream = new byte[serializedSize + 1];
    int pos = operation.toStream(stream, 1);

    Assert.assertEquals(serializedSize + 1, pos);

    LocalHashTableV2DirectoryFirstPageSetMaxRightChildDepthPO restoredOperation = new LocalHashTableV2DirectoryFirstPageSetMaxRightChildDepthPO();
    restoredOperation.fromStream(stream, 1);

    Assert.assertEquals(42, restoredOperation.getFileId());
    Assert.assertEquals(24, restoredOperation.getPageIndex());
    Assert.assertEquals(1, restoredOperation.getOperationUnitId());

    Assert.assertEquals(2, restoredOperation.getLocalNodeIndex());
    Assert.assertEquals(12, restoredOperation.getMaxRightChildDepth());
    Assert.assertEquals(21, restoredOperation.getPastMaxRightChildDepth());
  }
}
