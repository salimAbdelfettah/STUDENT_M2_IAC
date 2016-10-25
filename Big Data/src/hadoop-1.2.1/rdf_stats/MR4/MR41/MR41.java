//package org.myorg;
//http://localhost.localdomain:50060/logs/userlogs/
import java.io.IOException;
import java.util.*;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.util.*;

public class MR41 {

	public static class Map extends MapReduceBase implements Mapper<LongWritable, Text, Text, Text> {
		private final static IntWritable zero = new IntWritable(0);
		private final static IntWritable one = new IntWritable(1);
		private final static IntWritable two = new IntWritable(2);

		private Text word = new Text();
		private Text s= new Text();
		private Text p= new Text();

		public void map(LongWritable key, Text value, OutputCollector<Text, Text> output, Reporter reporter) throws IOException {
			String line = value.toString();
			System.out.println(line);
			StringTokenizer tokenizer = new StringTokenizer(line);
			int i=0;
			while (tokenizer.hasMoreTokens()) {
				word.set(tokenizer.nextToken());
				if(word.equals(new Text("."))){
					output.collect(s, p);
					i=0;
				}else{
					switch (i){
						case 0: s.set(word); break;
						case 1: p.set(word); break;
					}
					i++;
				}
			}
		}
	}

	public static class Reduce extends MapReduceBase implements Reducer<Text, Text, Text, IntWritable> {
		private Text t;
		private Text v= new Text();
		private int sum;
		public void reduce(Text key, Iterator<Text> values, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException {
			sum=0;
			t= new Text();
			while (values.hasNext()) {
				v=values.next();
				if (!t.equals(v)){
					t.set(v);
					sum++;
				}
			}
			output.collect(key, new IntWritable(sum));
		}
	}

	public static void main(String[] args) throws Exception {
		JobConf conf = new JobConf(MR41.class);
		conf.setJobName("MR41");

		conf.setOutputKeyClass(Text.class);
		conf.setOutputValueClass(Text.class);

		conf.setMapperClass(Map.class);
		//conf.setCombinerClass(Reduce.class);
		conf.setReducerClass(Reduce.class);

		conf.setInputFormat(TextInputFormat.class);
		conf.setOutputFormat(TextOutputFormat.class);

		conf.setOutputValueGroupingComparator(Comparator.class);

		FileInputFormat.setInputPaths(conf, new Path(args[0]));
		FileOutputFormat.setOutputPath(conf, new Path(args[1]));

		JobClient.runJob(conf);
	}

	public static class Comparator extends WritableComparator {
		private static final Text.Comparator TEXT_COMPARATOR = new Text.Comparator();

		public Comparator() {
			super(Text.class);
		}


        	public int compare(byte[] b1, int s1, int l1, byte[] b2, int s2, int l2) {
            		//try {
                		return (-1)* TEXT_COMPARATOR.compare(b1, s1, l1, b2, s2, l2);
            		//} catch (IOException e) {
                		//throw new IllegalArgumentException(e);
            		//}
        	}

        	public int compare(WritableComparable a, WritableComparable b) {
            		if (a instanceof Text && b instanceof Text) {
                		return (-1)*(((Text) a).compareTo((Text) b));
            		}
            		return super.compare(a, b);
        	}
	}

	static {
	// register this comparator
		WritableComparator.define(Text.class, new Comparator());
	}


}
