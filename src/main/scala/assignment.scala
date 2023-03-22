import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
import org.apache.spark.streaming.{Seconds, StreamingContext}

object assignment {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .master("local[2]")
      .appName("forEach")
      .getOrCreate()
    import spark.implicits._
    val file1Rdd = spark.sparkContext.textFile("file:///home/hduser/Desktop/customer.txt")
    val file1Df = file1Rdd.map(line => line.split(","))
      .map(cols => (cols(0).toInt, cols(1), cols(2), cols(3).toInt))
      .toDF("customerId", "customerName", "customerEmail", "totalBalance")

    // Read the second file as an RDD and convert it to a DataFrame
    val file2Rdd = spark.sparkContext.textFile("file:///home/hduser/Desktop/merchantCode.txt")
    val file2Df = file2Rdd.map(line => line.split(","))
      .map(cols => (cols(0), cols(1),cols(2).toInt))
      .toDF("merchantCode", "merchantName","Discount")

    val ssc = new StreamingContext(spark.sparkContext, Seconds(10))
    val lines = ssc.textFileStream("file:///home/hduser/Desktop/assignment")
    val line = lines.map(x=>(x.split(",")(0),x.split(",")(1).toInt,x.split(",")(2).toInt,x.split(",")(3)))
    line.foreachRDD(rdd => {
      import spark.implicits._
      val df = rdd.toDF("Txid","customerId","Amount", "merchantCode")
      val joinDf = df
        .join(file1Df,"customerId")
        .join(file2Df,"merchantCode")
        .withColumn("finalAmount", $"Amount" * $"Discount" / 100)
        .withColumn("balanceAmount", $"amount" - $"finalAmount")
        .withColumn("finalAmount", $"totalBalance" - $"balanceAmount")
        .select("customerId","customerName","customerEmail","finalAmount")
        .orderBy(col("customerId").asc, col("finalAmount").desc)
        .as[(Int, String, String, Double)]
      joinDf.show()

    })
    ssc.start()
    ssc.awaitTermination()

  }}