/**Author _Tejasvi Sharma
*/

import org.apache.spark.sql._
import org.apache.spark._
import org.apache.spark.ml.feature._
import org.apache.spark.ml.regression._
import org.apache.spark.ml._
import org.apache.spark.ml.evaluation.RegressionEvaluator

object Main{
	//training and creating model
	var df=spark.read.option("header","true").option("inferschema","true").csv("/home/tejasvi/PracticeProject/HouseRent/data/train.csv")

// #BedroomAbvGr-less correlation with SalePrice 0.168,BldgType,TotRmsAbvGrd
// #BsmtCond,BsmtQual,BsmtExposure are same type of variables- so we combine these
// #BsmtFullBath -correlation is .227 might not use this
// #TotalBsmtSF-can use 0.6135 correlation,CentralAir,ExterQual,ExterCond,Exterior1st,Exterior2nd
// #GarageType,GarageFinish,Garage quality these are almost same, can be combined
// #GarageArea-can be used -0.62 correlation,GarageCars -can be used good correlation 0.640
// #FirePlaces- coreelation 0.466,Foundation,GrLivArea-cor 0.7086,Heating,HeatingQC,OverallQual
// #KitchenQual,LotArea-0.26 corr,LotShape,LandContour,MSSubClass,MSZoning,YearBuilt,YearRemodAdd
// #SaleCondition
	val target="SalePrice"
	df=df.select("Id",target,"BedroomAbvGr","BldgType","TotRmsAbvGrd","BsmtCond","BsmtQual","BsmtExposure","TotalBsmtSF","CentralAir","ExterQual","ExterCond","Exterior1st","Exterior2nd","GarageType","GarageFinish","GarageArea","GarageCars","FirePlaces","Foundation","GrLivArea","Heating","HeatingQC","OverallQual","KitchenQual","LotShape","LandContour","MSSubClass","MSZoning","YearBuilt","YearRemodAdd","SaleCondition")
	val cat_columns=Array("BldgType","BsmtCond", "BsmtQual", "BsmtExposure", "CentralAir", "ExterQual", "ExterCond", "GarageType", "GarageFinish","Foundation","Heating", "HeatingQC","LotShape","LandContour","SaleCondition")
	val numeric_columns=Array("GarageArea", "YearRemodAdd", "BedroomAbvGr", "GrLivArea", "YearBuilt", "TotRmsAbvGrd", "OverallQual", "MSSubClass","TotalBsmtSF")

	val buf = scala.collection.mutable.ArrayBuffer.empty[org.apache.spark.ml.PipelineStage]

	for(i <- cat_columns){
		val index=new StringIndexer().setInputCol(i).setOutputCol(i+"__index").setHandleInvalid("skip")
		// val encode=new StringIndexer().setInputCol(i+"__index").setOutputCol(i+"__encode")
		buf+=index				
		// buf+=encode

	}

	val columns=cat_columns.map(i=>i+"__index")
	val vec=new VectorAssembler().setInputCols(columns++numeric_columns).setOutputCol("features_1")
	val featureIndexer = new VectorIndexer()
		.setInputCol("features_1")
		.setOutputCol("features")
		.setMaxCategories(10)

	val dt = new DecisionTreeRegressor()
  		.setLabelCol(target)
  		.setFeaturesCol("features")
  	buf+=vec
  	buf+=featureIndexer
  	buf+=dt

  	val Array(train_Data, test_Data) =df.randomSplit(Array(0.7, 0.3))
  	val pipe=new Pipeline().setStages(buf.toArray)
  	val model=pipe.fit(df)
  	val predictions=model.transform(test_Data)
  	val evaluator = new RegressionEvaluator().setLabelCol(target).setPredictionCol("prediction").setMetricName("rmse")
	val rmse = evaluator.evaluate(predictions)
	//forecasting data
	var test=spark.read.option("header","true").option("inferschema","true").csv("/home/tejasvi/PracticeProject/HouseRent/data/test.csv")
	test=test.select("Id","BedroomAbvGr","BldgType","TotRmsAbvGrd","BsmtCond","BsmtQual","BsmtExposure","TotalBsmtSF","CentralAir","ExterQual","ExterCond","Exterior1st","Exterior2nd","GarageType","GarageFinish","GarageArea","GarageCars","FirePlaces","Foundation","GrLivArea","Heating","HeatingQC","OverallQual","KitchenQual","LotShape","LandContour","MSSubClass","MSZoning","YearBuilt","YearRemodAdd","SaleCondition")
	val test_forecast=model.transform(test).select("Id","prediction").withColumnRenamed("prediction","SalePrice")
	test_forecast.coalesce(1).write.format("csv").option("header","true").save("/home/tejasvi/houseforecast")