package Fo;

import org.testng.IReporter;
import org.testng.ISuite;
import org.testng.xml.XmlSuite;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class CustomGraphReporter implements IReporter {
    @Override
    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
        int passed = 0, failed = 0, skipped = 0;

        for (ISuite suite : suites) {
            Map<String, org.testng.ISuiteResult> suiteResults = suite.getResults();
            for (org.testng.ISuiteResult result : suiteResults.values()) {
                passed += result.getTestContext().getPassedTests().size();
                failed += result.getTestContext().getFailedTests().size();
                skipped += result.getTestContext().getSkippedTests().size();
            }
        }

        // Write HTML report with embedded chart
        try (FileWriter writer = new FileWriter(outputDirectory + "/CustomGraphReport.html")) {
            writer.write("<html><head><title>TestNG Report with Chart</title></head><body>");
            writer.write("<h1>Test Summary</h1>");
            writer.write("<p>Passed: " + passed + "</p>");
            writer.write("<p>Failed: " + failed + "</p>");
            writer.write("<p>Skipped: " + skipped + "</p>");

            // Chart.js Pie Chart
            writer.write("<canvas id='myChart' width='400' height='400'></canvas>");
            writer.write("<script src='https://cdn.jsdelivr.net/npm/chart.js'></script>");
            writer.write("<script>");
            writer.write("const ctx = document.getElementById('myChart').getContext('2d');");
            writer.write("new Chart(ctx, {type: 'pie', data: {");
            writer.write("labels: ['Passed', 'Failed', 'Skipped'],");
            writer.write("datasets: [{ data: [" + passed + "," + failed + "," + skipped + "],");
            writer.write("backgroundColor: ['green', 'red', 'orange'] }]");
            writer.write("}});");
            writer.write("</script>");

            writer.write("</body></html>");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}