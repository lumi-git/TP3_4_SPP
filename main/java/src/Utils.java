import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.awt.*;
import java.util.ArrayList;

/**
 * This class contains utility methods.
 * <p>
 * They can use the settings of the application.
 */
public class Utils {

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


    public static void plotGraph(ArrayList<Double> originalSeqTimes, ArrayList<Double> multiThreadTimes ){

// Create a dataset for filter 2 (GaussianContourExtractorFilter)
        XYSeriesCollection dataset2 = new XYSeriesCollection();
        XYSeries series2Seq = new XYSeries("Sequential");
        XYSeries series2Mul = new XYSeries("Parallel");

        for(int i = 0; i < originalSeqTimes.size(); i++) {
            series2Seq.add(i+1, originalSeqTimes.get(i));
            series2Mul.add(i+1, multiThreadTimes.get(i));
        }
        dataset2.addSeries(series2Seq);
        dataset2.addSeries(series2Mul);


// Create chart for filter 2
        JFreeChart chart2 = ChartFactory.createXYLineChart(
                "Filter 2 Performance",
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
        domainAxis2.setRange(1, 10);

        XYSplineRenderer renderer2 = new XYSplineRenderer();
        renderer2.setSeriesPaint(0, Color.RED);
        renderer2.setSeriesPaint(1, Color.BLUE);
        plot2.setRenderer(renderer2);

        ChartFrame frame2 = new ChartFrame("Filter 2", chart2);
        frame2.pack();
        frame2.setVisible(true);
    }


}
