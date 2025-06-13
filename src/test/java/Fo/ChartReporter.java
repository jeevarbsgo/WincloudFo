package Fo;

import org.testng.IReporter;
import org.testng.ISuite;
import org.testng.xml.XmlSuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ChartReporter implements IReporter {

    @Override
    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
        int passed = 0;
        int failed = 0;
        int skipped = 0;

        for (ISuite suite : suites) {
            for (ISuiteResult result : suite.getResults().values()) {
                ITestContext context = result.getTestContext();
                passed += context.getPassedTests().size();
                failed += context.getFailedTests().size();
                skipped += context.getSkippedTests().size();
            }
        }

        String html = generateHtml(passed, failed, skipped);
        try (FileWriter writer = new FileWriter(outputDirectory + "/TestNG_BarChart_Report.html")) {
            writer.write(html);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String generateHtml(int passed, int failed, int skipped) {
        return "<html>\n" +
                "<head>\n" +
                "    <title>TestNG Bar Chart Report</title>\n" +
                "    <script src=\"https://cdn.jsdelivr.net/npm/chart.js\"></script>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <h1>Test Summary</h1>\n" +
                "    <p>Passed: " + passed + "</p>\n" +
                "    <p>Failed: " + failed + "</p>\n" +
                "    <p>Skipped: " + skipped + "</p>\n" +
                "    <canvas id=\"barChart\" width=\"400\" height=\"200\"></canvas>\n" +
                "    <script>\n" +
                "        const ctx = document.getElementById('barChart').getContext('2d');\n" +
                "        new Chart(ctx, {\n" +
                "            type: 'bar',\n" +
                "            data: {\n" +
                "                labels: ['Passed', 'Failed', 'Skipped'],\n" +
                "                datasets: [{\n" +
                "                    label: 'Test Results',\n" +
                "                    data: [" + passed + ", " + failed + ", " + skipped + "],\n" +
                "                    backgroundColor: ['green', 'red', 'orange']\n" +
                "                }]\n" +
                "            },\n" +
                "            options: {\n" +
                "                scales: {\n" +
                "                    y: {\n" +
                "                        beginAtZero: true,\n" +
                "                        precision: 0\n" +
                "                    }\n" +
                "                }\n" +
                "            }\n" +
                "        });\n" +
                "    </script>\n" +
                "</body>\n" +
                "</html>";
    }
}
