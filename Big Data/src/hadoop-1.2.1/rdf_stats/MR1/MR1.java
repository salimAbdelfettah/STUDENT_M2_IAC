//package org.myorg;
//http://localhost.localdomain:50060/logs/userlogs/
import java.io.IOException;
import java.util.*;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.util.*;

public class MR1 {

	public static class Map extends MapReduceBase implements Mapper<LongWritable, Text, Text, IntWritable> {
		private final static IntWritable zero = new IntWritable(0);
		private final static IntWritable one = new IntWritable(1);
		private final static IntWritable two = new IntWritable(2);

		private Text word = new Text();

		public void map(LongWritable key, Text value, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException {
			String line = value.toString();
			System.out.println(line);
			StringTokenizer tokenizer = new StringTokenizer(line);
			int i=0;
			while (tokenizer.hasMoreTokens()) {
				word.set(tokenizer.nextToken());
				if(word.equals(new Text("."))){
					i=0;
				}else{
					switch (i){
						case 0: output.collect (word, zero); break;
						case 1: output.collect (word, one); break;
						case 2: output.collect (word, two); break;
					}
					i++;
				}
			}
		}
	}

	public static class Reduce extends MapReduceBase implements Reducer<Text, IntWritable, Text, Text> {

		public void reduce(Text key, Iterator<IntWritable> values, OutputCollector<Text, Text> output, Reporter reporter) throws IOException {
			int sum_s = 0;
			int sum_p = 0;
			int sum_o = 0;
			while (values.hasNext()) {
				switch (values.next().get()){
					case 0: sum_s++; break;
					case 1: sum_p++; break;
					case 2: sum_o++; break;
				}
			}
			output.collect(key, new Text(sum_s+" "+sum_p+" "+sum_o+" ."));
			//System.out.println("* Key= "+key+"<br/>--> s = "+sum_s+"<br>--> p = "+sum_p+"<br/>--> o = "+sum_o+".<br/>");
		}
	}

	public static void main(String[] args) throws Exception {
		JobConf conf = new JobConf(MR1.class);
		conf.setJobName("MR1");

		conf.setOutputKeyClass(Text.class);
		conf.setOutputValueClass(IntWritable.class);

		conf.setMapperClass(Map.class);
		//conf.setCombinerClass(Reduce.class);
		conf.setReducerClass(Reduce.class);

		conf.setInputFormat(TextInputFormat.class);
		conf.setOutputFormat(TextOutputFormat.class);

		FileInputFormat.setInputPaths(conf, new Path(args[0]));
		FileOutputFormat.setOutputPath(conf, new Path(args[1]));

		JobClient.runJob(conf);
	}
}
