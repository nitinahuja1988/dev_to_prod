import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

object streamwordcountmultipleSocket {
  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setMaster("local[3]").setAppName("wordCount")
    val sc = new SparkContext(conf)
    val ssc = new StreamingContext(sc,Seconds(30))
    val input = ssc.socketTextStream("localhost",9900)
    val input1 = ssc.socketTextStream("localhost",8800)
    val total = input.union(input1)
    val word = total.flatMap(_.split(","))
    val output = word.map(out => (out,1))
    val combine = output.reduceByKey(_+_)

    combine.print()
    ssc.start()
    ssc.awaitTermination()

}}
