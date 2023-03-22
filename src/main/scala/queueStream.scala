import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable

object queueStream {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[3]").setAppName("wordCount")
    val sc = new SparkContext(conf)
    val ssc = new StreamingContext(sc,Seconds(30))

    val inputdata : mutable.Queue[RDD[Int]] = mutable.Queue()
    inputdata += sc.parallelize(List( 1,2,3,4,5))
    inputdata += sc.parallelize(List(6,7,8,9,10))
    val inputStream: InputDStream[Int] = ssc.queueStream(inputdata)
    val evenOdd = inputStream.transform{rdd => {
      rdd.map( num => if (num%2==0) ("even",num) else ("odd",num))
    }}
    val res = evenOdd.reduceByKey(_+_)
    res.print
    ssc.start()
    ssc.awaitTermination()}}
