/**
 * Copyright 2010-2016 OrientDB LTD (http://orientdb.com)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * <p>
 * For more information: http://www.orientdb.com
 */
package com.orientechnologies.spatial.engine;

import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.spatial.SpatialStrategy;

import java.io.IOException;

/**
 * Created by Enrico Risa on 11/08/15.
 */
public interface OLuceneSpatialIndexContainer {

  IndexSearcher searcher() throws IOException;

  SpatialStrategy strategy();

  boolean isLegacy();
}
