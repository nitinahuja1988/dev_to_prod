import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}
object aggusingCheckPoint {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[2]").setAppName("aggCheckpointing")
    val sc = new SparkContext(conf)
    val ssc = new StreamingContext(sc,Seconds(30))
    val line =  ssc.socketTextStream("localhost",9900)
    val res = line.flatMap(_.split(","))
    val res1 = res.map(x=>(x,1))
    val func = (value :Seq[Int], state:Option[Int]) => {
      var currentCount = 0
      if(!value.isEmpty)
       currentCount = value.reduce(_+_)

      val previousCount = state.getOrElse(0)
      println(s"Value :: $value")
      println(s"state :: $state")
      println(s"currentCount :: $currentCount")
      println(s"previousCount :: $previousCount")
      Some(currentCount + previousCount)}

    ssc.checkpoint("hdfs://localhost:9000/checkpoint")
    val output = res1.reduceByKey(_+_).updateStateByKey(func)
    output.print()
    ssc.start()
    ssc.awaitTermination()}}
