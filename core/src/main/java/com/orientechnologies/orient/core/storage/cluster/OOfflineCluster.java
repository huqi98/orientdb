/*
 * Copyright 2010-2013 OrientDB LTD (info--at--orientdb.com)
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
package com.orientechnologies.orient.core.storage.cluster;

import com.orientechnologies.common.exception.OException;
import com.orientechnologies.common.util.OCommonConst;
import com.orientechnologies.orient.core.config.OStorageClusterConfiguration;
import com.orientechnologies.orient.core.conflict.ORecordConflictStrategy;
import com.orientechnologies.orient.core.exception.ORecordNotFoundException;
import com.orientechnologies.orient.core.id.ORID;
import com.orientechnologies.orient.core.id.ORecordId;
import com.orientechnologies.orient.core.storage.OCluster;
import com.orientechnologies.orient.core.storage.OPhysicalPosition;
import com.orientechnologies.orient.core.storage.ORawBuffer;
import com.orientechnologies.orient.core.storage.OStorage;
import com.orientechnologies.orient.core.storage.impl.local.OClusterBrowsePage;

import java.io.IOException;

/**
 * Represents an offline cluster, created with the "alter cluster X status offline" command. To restore the original cluster assure
 * to have the cluster files in the right path and execute: "alter cluster X status online".
 *
 * @author Luca Garulli (l.garulli--(at)--orientdb.com)
 * @since 2.0
 */
public class OOfflineCluster implements OCluster {

  private final    String                    name;
  private final    int                       id;
  private volatile int                       binaryVersion;

  public OOfflineCluster(final int iId, final String iName) {
    id = iId;
    name = iName;
  }

  @Override
  public void configure(int id, String clusterName) throws IOException {
  }

  @Override
  public void configure(OStorage iStorage, OStorageClusterConfiguration iConfig) throws IOException {
    binaryVersion = iConfig.getBinaryVersion();
  }

  @Override
  public void create() throws IOException {
  }

  @Override
  public void open() throws IOException {

  }

  @Override
  public void close() throws IOException {
  }

  @Override
  public void close(boolean flush) throws IOException {
  }

  @Override
  public void delete() throws IOException {
  }

  @Override
  public String encryption() {
    return null;
  }

  @Override
  public long getTombstonesCount() {
    return 0;
  }

  @Override
  public void truncate() throws IOException {
    throw new OOfflineClusterException("Cannot truncate an offline cluster '" + name + "'");
  }

  @Override
  public OPhysicalPosition allocatePosition(byte recordType) throws IOException {
    throw new OOfflineClusterException("Cannot allocate a new position on offline cluster '" + name + "'");
  }

  @Override
  public OPhysicalPosition createRecord(byte[] content, int recordVersion, byte recordType, OPhysicalPosition allocatedPosition)
      throws IOException {
    throw new OOfflineClusterException("Cannot create a new record on offline cluster '" + name + "'");
  }

  @Override
  public boolean deleteRecord(long clusterPosition) throws IOException {
    throw new OOfflineClusterException("Cannot delete a record on offline cluster '" + name + "'");
  }

  @Override
  public void updateRecord(long clusterPosition, byte[] content, int recordVersion, byte recordType) throws IOException {
    throw new OOfflineClusterException("Cannot update a record on offline cluster '" + name + "'");
  }

  @Override
  public void recycleRecord(long clusterPosition, byte[] content, int recordVersion, byte recordType) throws IOException {
    throw new OOfflineClusterException("Cannot resurrect a record on offline cluster '" + name + "'");
  }

  @Override
  public ORawBuffer readRecord(long clusterPosition, boolean prefetchRecords) throws IOException {
    throw OException.wrapException(new ORecordNotFoundException(new ORecordId(id, clusterPosition),
            "Record with rid #" + id + ":" + clusterPosition + " was not found in database"),
        new OOfflineClusterException("Cannot read a record from the offline cluster '" + name + "'"));
  }

  @Override
  public ORawBuffer readRecordIfVersionIsNotLatest(long clusterPosition, int recordVersion)
      throws IOException, ORecordNotFoundException {
    throw OException.wrapException(new ORecordNotFoundException(new ORecordId(id, clusterPosition),
            "Record with rid #" + id + ":" + clusterPosition + " was not found in database"),
        new OOfflineClusterException("Cannot read a record from the offline cluster '" + name + "'"));
  }

  @Override
  public boolean exists() {
    return true;
  }

  @Override
  public OPhysicalPosition getPhysicalPosition(OPhysicalPosition iPPosition) throws IOException {
    throw new OOfflineClusterException("Cannot read a record on offline cluster '" + name + "'");
  }

  @Override
  public void setClusterName(final String name) {
    throw new OOfflineClusterException("Cannot set cluster name on offline cluster '" + name + "'");
  }

  @Override
  public void setRecordConflictStrategy(final String conflictStrategy) {
    throw new OOfflineClusterException("Cannot set record conflict strategy on offline cluster '" + name + "'");
  }

  @Override
  public void setEncryption(final String encryptionName, final String encryptionKey) {
    throw new OOfflineClusterException("Cannot set encryption on offline cluster '" + name + "'");
  }

  @Override
  public long getEntries() {
    return 0;
  }

  @Override
  public long getFirstPosition() throws IOException {
    return ORID.CLUSTER_POS_INVALID;
  }

  @Override
  public long getLastPosition() throws IOException {
    return ORID.CLUSTER_POS_INVALID;
  }

  @Override
  public long getNextPosition() throws IOException {
    return ORID.CLUSTER_POS_INVALID;
  }

  @Override
  public String getFileName() {
    throw new OOfflineClusterException("Cannot return filename of offline cluster '" + name + "'");
  }

  @Override
  public int getId() {
    return id;
  }

  @Override
  public void synch() throws IOException {

  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public long getRecordsSize() throws IOException {
    return 0;
  }

  @Override
  public float recordGrowFactor() {
    return 0;
  }

  @Override
  public float recordOverflowGrowFactor() {
    return 0;
  }

  @Override
  public String compression() {
    return null;
  }

  @Override
  public boolean isSystemCluster() {
    return false;
  }

  @Override
  public boolean isDeleted(OPhysicalPosition iPPosition) throws IOException {
    return false;
  }

  @Override
  public OPhysicalPosition[] higherPositions(OPhysicalPosition position) throws IOException {
    return OCommonConst.EMPTY_PHYSICAL_POSITIONS_ARRAY;
  }

  @Override
  public OPhysicalPosition[] ceilingPositions(OPhysicalPosition position) throws IOException {
    return OCommonConst.EMPTY_PHYSICAL_POSITIONS_ARRAY;
  }

  @Override
  public OPhysicalPosition[] lowerPositions(OPhysicalPosition position) throws IOException {
    return OCommonConst.EMPTY_PHYSICAL_POSITIONS_ARRAY;
  }

  @Override
  public OPhysicalPosition[] floorPositions(OPhysicalPosition position) throws IOException {
    return OCommonConst.EMPTY_PHYSICAL_POSITIONS_ARRAY;
  }

  @Override
  public boolean hideRecord(long position) throws IOException {
    return false;
  }

  @Override
  public ORecordConflictStrategy getRecordConflictStrategy() {
    return null;
  }

  @Override
  public void acquireAtomicExclusiveLock() {
    // do nothing, anyway there is no real data behind to lock it
  }

  @Override
  public OClusterBrowsePage nextPage(long lastPosition) {
    return null;
  }

  @Override
  public int getBinaryVersion() {
    return binaryVersion;
  }
}
