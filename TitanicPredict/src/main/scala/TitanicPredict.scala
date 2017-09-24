/** Author- Tejasvi Sharma
*/

import org.apache.spark.sql._
import org.apache.spark._
import org.apache.spark.ml.feature._
import org.apache.spark.ml.classification.LogisticRegression
import org.apache.spark.ml.{Pipeline, PipelineModel}

//Main Function
object Main{
	val conf = new SparkConf().setAppName("simple")
	val sc = new SparkContext(conf)
	val spark = SparkSession
   .builder()
   .getOrCreate()

	var df=spark.read.option("header","true").option("inferschema","true").csv("/home/tejasvi/PracticeProject/TitanicPredict/data/train.csv")
	df=df.drop("Cabin")
	df=df.na.fill("S",Seq("Embarked"))
	var meanAge=df.select("Age").agg(mean("Age")).collect()(0)(0).toString.toDouble
	df=df.na.fill(meanAge,Seq("Age"))
	//Cat columns- Pclass,sex,SibSp,Parch,Embarked
	//Apply StringIndexer and OneHotEncoder
	var arr=Array("Pclass","Sex","SibSp","Embarked","Parch")
	var suffixcat="_index"
	var suffixencode="_encode"
	val buf = scala.collection.mutable.ArrayBuffer.empty[org.apache.spark.ml.PipelineStage]
	for(a <- arr){
		var e1=new StringIndexer().setInputCol(a).setOutputCol(a+suffixcat).setHandleInvalid("skip")
		var o1=new OneHotEncoder().setInputCol(a+suffixcat).setOutputCol(a+suffixencode) 
		buf+=e1
		buf+=o1
	}

	var arr1=arr.map(x=>x+suffixencode) ++ Array("Age","Fare")

	var assemble=new VectorAssembler().setInputCols(arr1).setOutputCol("features")
	var log=new LogisticRegression().setLabelCol("Survived").setFeaturesCol("features")
	buf+=assemble
	buf+=log
	val pipeline = new Pipeline().setStages(buf.toArray)
	var (train,test)=df.randomSplit(Array(0.8,0.2))
	val model=pipeline.fit(train)

	var testData=spark.read.option("header","true").option("inferschema","true").csv("/home/tejasvi/PracticeProject/TitanicPredict/data/test.csv")
	testData=testData.drop("Cabin")
	testData=testData.na.fill("S",Seq("Embarked"))
	var meanAge=testData.select("Age").agg(mean("Age")).collect()(0)(0).toString.toDouble
	testData=testData.na.fill(meanAge,Seq("Age"))
	testData=testData.na.fill(testData.select("Fare").agg(mean("Fare")).collect()(0)(0).toString.toDouble,Seq("Fare"))
	val predicted_Data=model.transform(testData).select("PassengerId","prediction").withColumnRenamed("prediction","Survived")
}