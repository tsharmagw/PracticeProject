/** @Author -Tejasvi Sharma
*/
//columns being used
//Gd value prrice,Frequent DEXA,online PHR,Compare to others health data,Data analytics of your historical testing data,Virtual reality viewing of your health data,Ability to post Composition activities,Being able to forecast your improvement using predictive analytics,Cybersecurity and privacy/security of your data,HIPAA compliant storage of your data,High quality online content (videos, blogs, etc),Email newsletter updates to introduce you to new content and events,Discounts and Promotions,A one-on-one relationship with a Composition ID nutritional advisor,Access to nutrition challenge programs (example: 30-day challenge),Anytime access to a Composition ID nutritional advisors by phone, email, or in-person,Ability to use Composition ID services at your regular gym,Ability to use Composition ID services at a dedicated Composition ID location,Convenient location to me,A Composition ID mobile app,Events,Educational talks about nutrition and health,Ability to buy high quality Composition ID-approved health supplements and fitness products,Assistance in creating your PHR,Assistance in maintaining your PHR,Family plan - Combined Composition ID membership for a family group,Gym,Dietary supplement,ID
//to implement kmeans for composition Data

//packages
import org.apache.spark.sql._
import org.apache.spark._
import org.apache.spark.ml.feature._
import org.apache.spark.ml.clustering._
import org.apache.spark.ml._
import org.apache.spark.ml.evaluation.RegressionEvaluator
import org.apache.spark.mllib.stat._

object Main{

//Creating Spark Conf,Spark Context and Spark Session
val conf = new SparkConf()
val sc = new SparkContext(conf)
val spark = SparkSession.builder().getOrCreate()
//Loading a file in a varibale df
var df=spark.read.option("header","true").option("inferschema","true").option("sep","\t").csv("/home/tejasvi/Desktop/Composition")
//filling NA values with zero
df=df.na.fill(0)
var columns=df.columns// code to get column names

columns= columns.filter(! _.contains("ID"))
//buffer for column names
var buffer=scala.collection.mutable.ArrayBuffer.empty[String]
//stages buffer, to store pipeline stages
var stages=scala.collection.mutable.ArrayBuffer.empty[org.apache.spark.ml.PipelineStage]
//columns which are less correlated
val lesscorr=Array("Family plan - Combined Composition ID membership for a family group","Gym","Dietary supplement","Events","Virtual reality viewing of your health data","Ability to post Composition activities","Access to nutrition challenge programs (example: 30-day challenge)","Educational talks about nutrition and health","Ability to buy high quality Composition ID-approved health supplements and fitness products")
//filtering the columns and adding to buffer
for(i <- columns){
if(!lesscorr.contains(i))
buffer+=i
}

//vector assembler to assemble less correlated variables to one varibale and then use principle component analysis.
var assemble1=new VectorAssembler().setInputCols(lesscorr).setOutputCol("lesscorr1")
stages+=assemble1

//dimensionality reduction
val pca = new PCA().setInputCol("lesscorr1").setOutputCol("pcaFeatures").setK(3)
stages+=pca
buffer+="pcaFeatures"
//to assemble all the feature columns to one column
var assemble=new VectorAssembler().setInputCols(buffer.toArray).setOutputCol("features")
stages+=assemble

//scaling all the features to same scale, scales data to unit standard deviation
var scale=new StandardScaler().setInputCol("features").setOutputCol("features_indexed")
stages+=scale

val pipeline= new Pipeline().setStages(stages.toArray)
val model=pipeline.fit(df)
//to save tuple containing clusters and error in that cluster
var buffer1 =scala.collection.mutable.ArrayBuffer.empty[(Int,Double)]
val df_temp= model.transform(df)

//creating model on various clusters
for(i<- 2 to 40){
val bkm = new BisectingKMeans().setK(i).setSeed(42).setFeaturesCol("features_indexed")

val kModel=bkm.fit(df_temp)
val WSSE = kModel.computeCost(df_temp)
var tup=(i,WSSE)
buffer1+=tup
print("cluster="+i+" "+WSSSE+"\n")
val df1= kModel.transform(df_temp)
}
// optimum is between 16-20, so lets take 18 cluster

val bkm = new BisectingKMeans().setK(18).setSeed(42).setFeaturesCol("features_indexed")
val kModel=bkm.fit(df_temp)
val WSSE = kModel.computeCost(df_temp)
print("cluster="+"18"+" "+WSSE+"\n")
val df1= kModel.transform(df_temp)
var res65=df1.select("ID","prediction")
//saving the data
res65.repartition(1).write.mode("overwrite").option("header","true").csv("/home/tejasvi/cluster")



}