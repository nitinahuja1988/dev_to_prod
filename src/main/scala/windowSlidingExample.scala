//import org.apache.spark.rdd.RDD
//import org.apache.spark.streaming.dstream.DStream
//import org.apache.spark.{SparkConf, SparkContext}
//import org.apache.spark.streaming.{Seconds, StreamingContext}
//import scala.collection.mutable
//object windowSlidingExample {
//  def main(args: Array[String]): Unit = {
//    val conf = new SparkConf().setMaster("local[3]").setAppName("wordCount")
//    val sc = new SparkContext(conf)
//    val ssc = new StreamingContext(sc, Seconds(10))
//
//    val inputdata: mutable.Queue[RDD[String]] = mutable.Queue()
//    inputdata += sc.parallelize(List("apple", "ball", "apple", "ball", "apple"))
//    inputdata += sc.parallelize(List("apple", "apple", "ball", "ball"))
//    inputdata += sc.parallelize(List("apple", "ball", "ball", "ball"))
//    inputdata += sc.parallelize(List("apple", "ball", "ball", "ball"))
//    inputdata += sc.parallelize(List("cat", "cat", "cat", "cat"))
//    inputdata += sc.parallelize(List("apple", "apple", "ball", "ball"))
//    inputdata += sc.parallelize(List("apple", "apple", "apple", "ball"))
//    inputdata += sc.parallelize(List("apple", "apple", "ball", "apple"))
//    val inputStream: DStream[String] = ssc.queueStream(inputdata)
//    val combineData = inputStream.window(Seconds(40),Seconds(10))
//    val word = combineData.map(x=>(x,1))
//    val wordCount = word.reduceByKey(_+_)
//    wordCount.print
//    ssc.start()
//    ssc.awaitTermination()}}