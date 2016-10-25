//package org.myorg;
//http://localhost.localdomain:50060/logs/userlogs/
import java.io.IOException;
import java.util.*;

import java.nio.ByteBuffer;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.util.*;


public class MR2 {
	public static int nb=10;

	public static class Map extends MapReduceBase implements Mapper<LongWritable, Text, IntWritable, Text> {

		private Text word = new Text();
		private Text l= new Text();
		//private Text val= new Text();
		private int val=0;

		public void map(LongWritable key, Text value, OutputCollector<IntWritable, Text> output, Reporter reporter) throws IOException {
			String line = value.toString();
			System.out.println(line);
			StringTokenizer tokenizer = new StringTokenizer(line);
			int i=0;
			while (tokenizer.hasMoreTokens()) {
				word.set(tokenizer.nextToken());
				if(word.equals(new Text("."))){
					i=0;
					output.collect (new IntWritable(val), l);
				}else{
					switch (i){
						case 0: l.set(word); break;
						//case 2: val.set(word); break;
						case 2: val=Integer.parseInt(word.toString()); break;
					}
					i++;
				}
			}
		}
	}

	public static class Reduce extends MapReduceBase implements Reducer<IntWritable, Text, Text, IntWritable> {
		private static int k=1;
		private static boolean v=true;

		public void reduce(IntWritable key, Iterator<Text> values, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException {
			if(k<=nb){
				v=true;
			}else{
				v=false;
			}
			while (values.hasNext()&(v)) {
				output.collect(values.next(), key);
				k++;
			}			
		}
	}

	public static void main(String[] args) throws Exception {
		//Scanner sc= new Scanner(System.in);
		//String S= sc.nextLine();
		//nb=Integer.parseInt(S);
		nb=5;
		JobConf conf = new JobConf(MR2.class);
		conf.setJobName("MR2");

		conf.setOutputKeyClass(IntWritable.class);
		conf.setOutputValueClass(Text.class);

		conf.setMapperClass(Map.class);
		//conf.setCombinerClass(Reduce.class);
		conf.setReducerClass(Reduce.class);

		conf.setInputFormat(TextInputFormat.class);
		conf.setOutputFormat(TextOutputFormat.class);

		FileInputFormat.setInputPaths(conf, new Path(args[0]));
		FileOutputFormat.setOutputPath(conf, new Path(args[1]));

		conf.setOutputKeyComparatorClass(IntComparator.class);

		JobClient.runJob(conf);
	}




	public static class IntComparator extends WritableComparator {
		public IntComparator() {
			super(IntWritable.class);
		}

    		private Integer int1;
    		private Integer int2;


		public int compare(byte[] raw1, int offset1, int length1, byte[] raw2,int offset2, int length2) {
			int1 = ByteBuffer.wrap(raw1, offset1, length1).getInt();
			int2 = ByteBuffer.wrap(raw2, offset2, length2).getInt();

			return int2.compareTo(int1);
		}
	}


}
