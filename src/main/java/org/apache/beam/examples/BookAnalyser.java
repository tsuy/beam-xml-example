/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.beam.examples;

// beam-playground:
//   name: WordCount
//   description: An example that counts words in Shakespeare's works.
//   multifile: false
//   pipeline_options: --output output.txt
//   context_line: 95
//   categories:
//     - Combiners
//     - Options
//     - Quickstart
//   complexity: MEDIUM
//   tags:
//     - count
//     - strings

import org.apache.beam.examples.common.ExampleUtils;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.io.xml.XmlIO;
import org.apache.beam.sdk.metrics.Counter;
import org.apache.beam.sdk.metrics.Distribution;
import org.apache.beam.sdk.metrics.Metrics;
import org.apache.beam.sdk.options.Default;
import org.apache.beam.sdk.options.Description;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.options.Validation.Required;
import org.apache.beam.sdk.transforms.*;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PCollection;

public class BookAnalyser {

  public static class BookAnalyserT
          extends PTransform<PCollection<Book>, PCollection<String>> {
    @Override
    public PCollection<String> expand(PCollection<Book> lines) {

      PCollection<String> words = lines.apply(ParDo.of(new BookAnalyser.BookAnalyserFn()));

      return words;
    }
  }

  static class BookAnalyserFn extends DoFn<Book, String> {
    @ProcessElement
    public void processElement(
            @Element Book element, OutputReceiver<String> receiver) {

     /* long startTime = System.currentTimeMillis();
      long endTime = startTime + 30 * 1000; // 5 minutes in milliseconds


      while (System.currentTimeMillis() < endTime) {
        long sum = 0;
        for (long i = 1; i <= 1000000000; i++) {
          sum += i;
        }
      }*/
      System.out.println("Xml data: " + element.getName());
      System.out.println("Xml data: " + element.getAddress().getCity());
      receiver.output(element.getName());
    }
  }


  /**
   * Options supported by {@link BookAnalyser}.
   *
   * <p>Concept #4: Defining your own configuration options. Here, you can add your own arguments to
   * be processed by the command-line parser, and specify default values for them. You can then
   * access the options values in your pipeline code.
   *
   * <p>Inherits standard configuration options.
   */
  public interface BookAnalyserOptions extends PipelineOptions {

    /**
     * By default, this example reads from a public dataset containing the text of King Lear. Set
     * this option to choose a different input file or glob.
     */
    @Description("Path of the file to read from")
    @Default.String("gs://apache-beam-samples/shakespeare/kinglear.txt")
    String getInputFile();

    void setInputFile(String value);

    /** Set this required option to specify where to write the output. */
    @Description("Path of the file to write to")
    @Required
    String getOutput();

    void setOutput(String value);
  }

  static void runBookAnalyzer(BookAnalyserOptions options) {
    Pipeline p = Pipeline.create(options);

    // Concepts #2 and #3: Our pipeline applies the composite CountWords transform, and passes the
    // static FormatAsTextFn() to the ParDo transform.
    p.apply("ReadLines", XmlIO.<Book>read().from(options.getInputFile())
                    //.apply(new CountWords())
                    //.apply(MapElements.via(new FormatAsTextFn()))
                    //.apply("WriteCounts", TextIO.write().to(options.getOutput()));
                    .withRootElement("books")
                    .withRecordElement("book")
                    .withRecordClass(Book.class))
            .apply(new BookAnalyserT())
            .apply("WriteBooks", TextIO.write().to(options.getOutput()));

    p.run().waitUntilFinish();
  }

  public static void main(String[] args) {
    BookAnalyserOptions options =
        PipelineOptionsFactory.fromArgs(args).withValidation().as(BookAnalyserOptions.class);

    runBookAnalyzer(options);
  }
}
