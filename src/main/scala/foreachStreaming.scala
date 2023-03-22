import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.{Seconds, StreamingContext}
case class student(name:String,marks:Int,school:String, district:String, emailid:String)
object foreachStreaming {
  def main(args: Array[String]): Unit = {
  val spark = SparkSession.builder()
    .master("local[2]")
    .appName("forEach")
    .getOrCreate()

  val ssc = new StreamingContext(spark.sparkContext, Seconds(10))
  val lines = ssc.socketTextStream("localhost",9900)
    val line = lines.map(x=>student(x.split(",")(0),x.split(",")(1).toInt,x.split(",")(2),x.split(",")(3),x.split(",")(4)))
   line.foreachRDD(rdd => {
     import spark.implicits._
     val df = rdd.toDF()
     df.show(20,false)
     if(!df.isEmpty) {
       df.write.mode("overwrite").saveAsTable("streaming_table")}})

  line.print
  ssc.start()
  ssc.awaitTermination()}}
