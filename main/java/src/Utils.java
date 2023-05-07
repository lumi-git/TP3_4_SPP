import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.category.BoxAndWhiskerRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.chart.ChartPanel;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;

import javax.swing.JFrame;
import java.util.ArrayList;
import java.util.List;
import java.awt.Color;

import org.jfree.chart.axis.CategoryAxis;
import org.jfree.data.category.DefaultCategoryDataset;


/**
 * This class contains utility methods.
 * <p>
 * They can use the settings of the application.
 */
public class Utils {


    public static void plotGraphCat(String title, List<ArrayList<Double>> originalSeqTimes, List<ArrayList<Double>> multiThreadTimes, boolean displayRuns) {
        DefaultBoxAndWhiskerCategoryDataset boxDataset = new DefaultBoxAndWhiskerCategoryDataset();
        DefaultCategoryDataset lineDataset = new DefaultCategoryDataset();
        DefaultCategoryDataset confIntervalDataset = new DefaultCategoryDataset();

        for (int i = 1; i < originalSeqTimes.size(); i++) {
            boxDataset.add(originalSeqTimes.get(i), "Single-Threaded", Integer.toString(i));
            boxDataset.add(multiThreadTimes.get(i), "Multi-Threaded", Integer.toString(i));
            if (displayRuns) {
                for (int j = 0; j < originalSeqTimes.get(i).size(); j++) {
                    lineDataset.addValue(originalSeqTimes.get(i).get(j), "Single-Threaded Run " + (j + 1), Integer.toString(i));
                    lineDataset.addValue(multiThreadTimes.get(i).get(j), "Multi-Threaded Run " + (j + 1), Integer.toString(i));
                }
            }

            double[] stConfInterval = calculateLowerUpperConfidenceBoundary95Percent(originalSeqTimes.get(i));
            double[] mtConfInterval = calculateLowerUpperConfidenceBoundary95Percent(multiThreadTimes.get(i));

            confIntervalDataset.addValue(stConfInterval[0], "Single-Threaded Min", Integer.toString(i));
            confIntervalDataset.addValue(stConfInterval[1], "Single-Threaded Max", Integer.toString(i));
            confIntervalDataset.addValue(mtConfInterval[0], "Multi-Threaded Min", Integer.toString(i));
            confIntervalDataset.addValue(mtConfInterval[1], "Multi-Threaded Max", Integer.toString(i));
        }

        CategoryAxis xAxis = new CategoryAxis("Number of Threads");
        NumberAxis yAxis = new NumberAxis("Execution Time (ms)");

        BoxAndWhiskerRenderer boxRenderer = new BoxAndWhiskerRenderer();
        boxRenderer.setMeanVisible(false);
        boxRenderer.setMaxOutlierVisible(true);
        boxRenderer.setMinOutlierVisible(true);
        boxRenderer.setUseOutlinePaintForWhiskers(true);
        boxRenderer.setMedianVisible(true);
        boxRenderer.setArtifactPaint(Color.BLACK);
        boxRenderer.setWhiskerWidth(1.0);
        boxRenderer.setSeriesOutlinePaint(0, Color.BLACK);
        boxRenderer.setSeriesOutlinePaint(1, Color.BLACK);
        boxRenderer.setUseOutlinePaintForWhiskers(true);

        LineAndShapeRenderer lineRenderer = new LineAndShapeRenderer();
        LineAndShapeRenderer confIntervalRenderer = new LineAndShapeRenderer();

        // Set colors for the single-threaded and multi-threaded lines
        for (int i = 0; i < originalSeqTimes.size(); i++) {
            lineRenderer.setSeriesPaint(i * 2, Color.RED);
            lineRenderer.setSeriesPaint(i * 2 + 1, Color.BLUE);
        }

        confIntervalRenderer.setSeriesPaint(0, Color.MAGENTA);
        confIntervalRenderer.setSeriesPaint(1, Color.MAGENTA);
        confIntervalRenderer.setSeriesPaint(2, Color.GREEN);
        confIntervalRenderer.setSeriesPaint(3, Color.GREEN);

        CategoryPlot plot = new CategoryPlot(boxDataset, xAxis, yAxis, boxRenderer);
        plot.setDataset(1, lineDataset);
        plot.setRenderer(1, lineRenderer);
        plot.setDataset(2, confIntervalDataset);
        plot.setRenderer(2, confIntervalRenderer);
        plot.setOrientation(PlotOrientation.VERTICAL);

        JFreeChart chart = new JFreeChart(title, JFreeChart.DEFAULT_TITLE_FONT, plot, true);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(800, 800));
        JFrame frame = new JFrame();
        frame.setContentPane(chartPanel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }


    private static double[] calculateLowerUpperConfidenceBoundary95Percent(List<Double> givenNumbers) {
        // calculate the mean value (= average)
        double sum = 0.0;
        for (double num : givenNumbers) {
            sum += num;
        }
        double mean = sum / givenNumbers.size();

        // calculate standard deviation
        double squaredDifferenceSum = 0.0;
        for (double num : givenNumbers) {
            squaredDifferenceSum += (num - mean) * (num - mean);
        }
        double variance = squaredDifferenceSum / givenNumbers.size();
        double standardDeviation = Math.sqrt(variance);

        // value for 95% confidence interval, source: https://en.wikipedia.org/wiki/Confidence_interval#Basic_Steps
        double confidenceLevel = 1.96;
        double temp = confidenceLevel * standardDeviation / Math.sqrt(givenNumbers.size());
        return new double[]{mean - temp, mean + temp};
    }


    /**
     * Print a message if the debug mode is on in the SETTINGS.
     *
     * @param s The message to print
     */
    public static void printDebug(String s) {
        if (SETTINGS.DEBUG) {
            System.out.println("ImageFilteringEngine_DEBUG |> " + s);
        }
    }


    public static void plotGraph(String Title, ArrayList<Double> originalSeqTimes, ArrayList<Double> multiThreadTimes) {

        // Create a dataset for filter 2 (GaussianContourExtractorFilter)
        XYSeriesCollection dataset2 = new XYSeriesCollection();
        XYSeries series2Seq = new XYSeries("Sequential");
        XYSeries series2Mul = new XYSeries("Parallel");

        for (int i = 0; i < originalSeqTimes.size(); i++) {
            series2Seq.add(i + 1, originalSeqTimes.get(i));
            series2Mul.add(i + 1, multiThreadTimes.get(i));
        }
        dataset2.addSeries(series2Seq);
        dataset2.addSeries(series2Mul);


        // Create chart for filter 2
        JFreeChart chart2 = ChartFactory.createXYLineChart(
                Title,
                "Number of Threads",
                "Time (ms)",
                dataset2,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        chart2.setBackgroundPaint(Color.white);

        XYPlot plot2 = (XYPlot) chart2.getPlot();
        plot2.setBackgroundPaint(Color.lightGray);
        plot2.setDomainGridlinePaint(Color.white);
        plot2.setRangeGridlinePaint(Color.white);

        NumberAxis domainAxis2 = (NumberAxis) plot2.getDomainAxis();
        domainAxis2.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        domainAxis2.setRange(1, originalSeqTimes.size());

        XYSplineRenderer renderer2 = new XYSplineRenderer();
        renderer2.setSeriesPaint(0, Color.RED);
        renderer2.setSeriesPaint(1, Color.BLUE);
        plot2.setRenderer(renderer2);

        ChartFrame frame2 = new ChartFrame("Filter 2", chart2);
        frame2.pack();
        frame2.setVisible(true);
    }

    public static void plotGraphWithImageSize(String title, ArrayList<Integer> imageSizes, ArrayList<ArrayList<Double>> originalSeqTimes, ArrayList<ArrayList<Double>> multiThreadTimes, boolean displayRuns) {
        DefaultBoxAndWhiskerCategoryDataset boxDataset = new DefaultBoxAndWhiskerCategoryDataset();
        DefaultCategoryDataset lineDataset = new DefaultCategoryDataset();
        DefaultCategoryDataset confIntervalDataset = new DefaultCategoryDataset();

        for (int i = 0; i < imageSizes.size(); i++) {
            String imageSizeCategory = Integer.toString(imageSizes.get(i));
            boxDataset.add(originalSeqTimes.get(i), "Single-Threaded", imageSizeCategory);
            boxDataset.add(multiThreadTimes.get(i), "Multi-Threaded", imageSizeCategory);
            if (displayRuns) {
                for (int j = 0; j < originalSeqTimes.get(i).size(); j++) {
                    lineDataset.addValue(originalSeqTimes.get(i).get(j), "Single-Threaded Run " + (j + 1), imageSizeCategory);
                    lineDataset.addValue(multiThreadTimes.get(i).get(j), "Multi-Threaded Run " + (j + 1), imageSizeCategory);
                }
            }

            double[] stConfInterval = calculateLowerUpperConfidenceBoundary95Percent(originalSeqTimes.get(i));
            double[] mtConfInterval = calculateLowerUpperConfidenceBoundary95Percent(multiThreadTimes.get(i));

            confIntervalDataset.addValue(stConfInterval[0], "Single-Threaded Min", imageSizeCategory);
            confIntervalDataset.addValue(stConfInterval[1], "Single-Threaded Max", imageSizeCategory);
            confIntervalDataset.addValue(mtConfInterval[0], "Multi-Threaded Min", imageSizeCategory);
            confIntervalDataset.addValue(mtConfInterval[1], "Multi-Threaded Max", imageSizeCategory);
        }

        CategoryAxis xAxis = new CategoryAxis("Image Size");
        NumberAxis yAxis = new NumberAxis("Execution Time (ms)");

        BoxAndWhiskerRenderer boxRenderer = new BoxAndWhiskerRenderer();
        boxRenderer.setMeanVisible(false);
        boxRenderer.setMaxOutlierVisible(true);
        boxRenderer.setMinOutlierVisible(true);
        boxRenderer.setUseOutlinePaintForWhiskers(true);
        boxRenderer.setMedianVisible(true);
        boxRenderer.setArtifactPaint(Color.BLACK);
        boxRenderer.setWhiskerWidth(1.0);
        boxRenderer.setSeriesOutlinePaint(0, Color.BLACK);
        boxRenderer.setSeriesOutlinePaint(1, Color.BLACK);
        boxRenderer.setUseOutlinePaintForWhiskers(true);

        LineAndShapeRenderer lineRenderer = new LineAndShapeRenderer();
        LineAndShapeRenderer confIntervalRenderer = new LineAndShapeRenderer();

        for (int i = 0; i < originalSeqTimes.size(); i++) {
            lineRenderer.setSeriesPaint(i * 2, Color.RED);
            lineRenderer.setSeriesPaint(i * 2 + 1, Color.BLUE);
        }

        confIntervalRenderer.setSeriesPaint(0, Color.MAGENTA);
        confIntervalRenderer.setSeriesPaint(1, Color.MAGENTA);
        confIntervalRenderer.setSeriesPaint(2, Color.GREEN);
        confIntervalRenderer.setSeriesPaint(3, Color.GREEN);

        CategoryPlot plot = new CategoryPlot(boxDataset, xAxis, yAxis, boxRenderer);
        plot.setDataset(1, lineDataset);
        plot.setRenderer(1, lineRenderer);
        plot.setDataset(2, confIntervalDataset);
        plot.setRenderer(2, confIntervalRenderer);
        plot.setOrientation(PlotOrientation.VERTICAL);
        JFreeChart chart = new JFreeChart(title, JFreeChart.DEFAULT_TITLE_FONT, plot, true);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(800, 800));
        JFrame frame = new JFrame();
        frame.setContentPane(chartPanel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }


}
