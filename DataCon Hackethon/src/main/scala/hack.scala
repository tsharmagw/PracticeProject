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
	var df=spark.read.option("header","true").option("inferschema","true").option("sep",",").csv("/home/tejasvi/Downloads/DoD_DoN_reduced.csv")

	val target="baseandexercisedoptionsvalue"

	val columns=Array(target,"a76action","agencyid","aiobflag","apaobflag","baobflag","ccrexception","congressionaldistrict","contractbundling","federalgovernmentflag","firm8aflag","educationalinstitutionflag","gfe_gfp","haobflag","hbcuflag","hospitalflag","hubzoneflag","idvagencyid","interagencycontractingauthority","is1862landgrantcollege","isalaskannativeownedcorporationorfirm","iscitylocalgovernment","iscommunitydevelopedcorporationownedfirm","iscommunitydevelopmentcorporation","iscorporateentitynottaxexempt","iscorporateentitytaxexempt","iscountylocalgovernment","isfederalgovernmentagency","isforeignownedandlocated","isindiantribe","isinternationalorganization","localgovernmentflag","minorityinstitutionflag","mod_parent","naobflag","nonprofitorganizationflag","numberofactions","numberofemployees","organizationaltype","performancebasedservicecontract","receivescontracts","receivescontractsandgrants","receivesgrants","saaobflag","sdbflag","shelteredworkshopflag","srdvobflag","transactionnumber","tribalgovernmentflag","unique_transaction_id","vendor_cd","vendor_state_code","womenownedflag","zipcode","dollarsobligated","baseandalloptionsvalue")
	// df=df.select(target,"a76action","agencyid","aiobflag","apaobflag","baobflag","baseandexercisedoptionsvalue","ccrexception","congressionaldistrict","contractbundling","federalgovernmentflag","firm8aflag","educationalinstitutionflag","gfe_gfp","haobflag","hbcuflag","hospitalflag","hubzoneflag","idvagencyid","interagencycontractingauthority","is1862landgrantcollege","isalaskannativeownedcorporationorfirm","iscitylocalgovernment","iscommunitydevelopedcorporationownedfirm","iscommunitydevelopmentcorporation","iscorporateentitynottaxexempt","iscorporateentitytaxexempt","iscountylocalgovernment","isfederalgovernmentagency","isforeigngovernment","isforeignownedandlocated","isindiantribe","isinternationalorganization","localgovernmentflag","minorityinstitutionflag","mod_parent","naobflag","nonprofitorganizationflag","numberofactions","numberofemployees","organizationaltype","performancebasedservicecontract","receivescontracts","receivescontractsandgrants","receivesgrants","saaobflag","sdbflag","shelteredworkshopflag","srdvobflag","transactionnumber","tribalgovernmentflag","unique_transaction_id","vendor_cd","vendor_state_code","womenownedflag","zipcode","unique_transaction_id","subaward_amount","subawardee_zipcode")
	df=df.select(target,"baobflag","baseandexercisedoptionsvalue","congressionaldistrict","contractbundling","federalgovernmentflag","firm8aflag","educationalinstitutionflag")
	// for(i<- columns){
	// 	df=df.filter(df(i)=!="" || df(i).isNull)
	// }
	df=df.na.drop()
	// val cat_columns=Array("BldgType","BsmtCond", "BsmtQual", "BsmtExposure", "CentralAir", "ExterQual", "ExterCond", "GarageType", "GarageFinish","Foundation","Heating", "HeatingQC","LotShape","LandContour","SaleCondition")
	// val numeric_columns=Array("GarageArea", "YearRemodAdd", "BedroomAbvGr", "GrLivArea", "YearBuilt", "TotRmsAbvGrd", "OverallQual", "MSSubClass","TotalBsmtSF")
	val cat_columns=Array("baobflag","baseandexercisedoptionsvalue","congressionaldistrict","contractbundling","federalgovernmentflag","firm8aflag","educationalinstitutionflag"))
	val numeric=Array()

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
	print(rmse)
	//forecasting data
	var test=spark.read.option("header","true").option("inferschema","true").csv("/home/tejasvi/PracticeProject/HouseRent/data/test.csv")
	test=test.select("Id","BedroomAbvGr","BldgType","TotRmsAbvGrd","BsmtCond","BsmtQual","BsmtExposure","TotalBsmtSF","CentralAir","ExterQual","ExterCond","Exterior1st","Exterior2nd","GarageType","GarageFinish","GarageArea","GarageCars","FirePlaces","Foundation","GrLivArea","Heating","HeatingQC","OverallQual","KitchenQual","LotShape","LandContour","MSSubClass","MSZoning","YearBuilt","YearRemodAdd","SaleCondition")
	val test_forecast=model.transform(test).select("Id","prediction").withColumnRenamed("prediction","SalePrice")
	test_forecast.coalesce(1).write.format("csv").option("header","true").save("/home/tejasvi/houseforecast")