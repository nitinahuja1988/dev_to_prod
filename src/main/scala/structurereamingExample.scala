import org.apache.spark.sql.SparkSession
object structurereamingExample {
  def main(args: Array[String]): Unit = {
  val spark = SparkSession
    .builder()
    .appName("Structuredwordcount")
    .master("local[*]")
    .getOrCreate()
    import spark.implicits._
    val Sparkk= spark.readStream
      .format("socket")
      .option("host","localhost")
      .option("port",9900)
      .load().as[String]
    val firstOutput = Sparkk.flatMap(x=>x.split(","))
    val totalCount = firstOutput.groupBy("value").count()

    val query = totalCount.writeStream
      .outputMode("complete")
      .format("console")
      .start()
    query.awaitTermination()
  }
}
