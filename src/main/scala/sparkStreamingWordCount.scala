import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.streaming.{Seconds, StreamingContext}
object sparkStreamingWordCount {
  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setMaster("local[2]").setAppName("wordCount")
  val sc = new SparkContext(conf)
  val ssc = new StreamingContext(sc,Seconds(10))
  val input = ssc.socketTextStream("localhost",9900)
  val word = input.flatMap(_.split(","))
  val output = word.map(out => (out,1))
  val combine = output.reduceByKey(_+_)

  combine.print()
  ssc.start()
  ssc.awaitTermination()

}}
