/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.spark.mllib.export.pmml

import scala.annotation.varargs
import org.apache.spark.mllib.clustering.KMeansModel
import org.apache.spark.mllib.export.ModelExportFactory
import org.apache.spark.mllib.export.ModelExportType
import org.apache.spark.mllib.linalg.Vectors
import org.scalatest.FunSuite
import org.dmg.pmml.ClusteringModel

class KMeansPMMLModelExportSuite extends FunSuite{

   test("KMeansPMMLModelExport generate PMML format") {
    
    //arrange model to test
    val clusterCenters = Array(
      Vectors.dense(1.0, 2.0, 6.0),
      Vectors.dense(1.0, 3.0, 0.0),
      Vectors.dense(1.0, 4.0, 6.0)
    )
    val kmeansModel = new KMeansModel(clusterCenters);
    
    //act by exporting the model to the PMML format
    val modelExport = ModelExportFactory.createModelExport(kmeansModel, ModelExportType.PMML)
    
    //assert that the PMML format is as expected
    assert(modelExport.isInstanceOf[PMMLModelExport])
    val pmml = modelExport.asInstanceOf[PMMLModelExport].getPmml()
    assert(pmml.getHeader().getDescription() === "k-means clustering")
    //check that the number of fields match the single vector size
    assert(pmml.getDataDictionary().getNumberOfFields() === clusterCenters(0).size)
    //this verify that there is a model attached to the pmml object and the model is a clustering one
    //it also verifies that the pmml model has the same number of clusters of the spark model
    assert(pmml.getModels().get(0).asInstanceOf[ClusteringModel].getNumberOfClusters() === clusterCenters.size)
    
    //manual checking
    //ModelExporter.toPMML(kmeansModel,new StreamResult(System.out))
    //ModelExporter.toPMML(kmeansModel,"/tmp/kmeans.xml")
   
   }
  
}
