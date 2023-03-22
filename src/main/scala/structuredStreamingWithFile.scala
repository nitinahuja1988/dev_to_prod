import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.streaming.Trigger

import scala.concurrent.duration.DurationInt
object structuredStreamingWithFile {
  def main(args: Array[String]): Unit = {
  val spark = SparkSession
    .builder()
    .master("local[2]")
    .appName("structuredStreaminWithFile")
    .getOrCreate()
    val ddlWay = "name String, marks Integer, schoolName String, state String, email String"
  val input = spark.readStream
    .format("csv")
    .option("header",value = true)
    .schema(ddlWay)
    .load("file:///home/hduser/Desktop/studentDataset")
  val output = input.filter("marks > 500")
  val result = output.writeStream
    .outputMode("append")
    .format("csv")
    .option("path","/home/hduser/Desktop/studentFinal")
    .trigger(Trigger.ProcessingTime(30.seconds))
    .option("checkpointLocation","/home/hduser/Desktop/checkpointin-dir")
    .start()
    result.awaitTermination()}}
