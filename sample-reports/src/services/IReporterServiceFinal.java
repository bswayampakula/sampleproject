package services;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.IReporter;
import org.testng.IResultMap;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.TestListenerAdapter;
import org.testng.xml.XmlSuite;

/**
 * @author bhargav.swayampakula
 *
 */
public class IReporterServiceFinal extends TestListenerAdapter implements
		IReporter {
	String htmlFolder = System.getProperty("user.dir") + "//reports";
	String screenshot = System.getProperty("user.dir") + "//screenshot";

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.testng.IReporter#generateReport(java.util.List, java.util.List,
	 * java.lang.String)
	 */
	@Override
	public synchronized void generateReport(List<XmlSuite> xmlSuites,
			List<ISuite> suites, String outputDirectory) {
		try {
			for (ISuite iSuite : suites) {
				// Get a map of result of a single suite at a time
				Map<String, ISuiteResult> results = iSuite.getResults();
				// Get the key of the result map
				Set<String> keys = results.keySet();
				// Go to each map value one by one
				for (String key : keys) {
					// The Context object of current result
					ITestContext context = results.get(key).getTestContext();
					File dir = new File(htmlFolder);
					if (!dir.exists()) {
						dir.mkdir();
					}
					File file = new File(htmlFolder + "//" + context.getName()
							+ ".html");
					StringBuffer sb = new StringBuffer();
					// Creation of Header Table
					sb.append("<HTML>" + "<head>" + "<title>TestNG: "
							+ context.getName()
							+ "</title>"
							+ "<h2 align=\"center\">"
							+ context.getName()
							+ "</h2>"
							+ "<link type=\"text/css\" rel=\"stylesheet\" href=\"../reports/styles.css\"></link>"
							+ "<script type=\"text/javascript\" src=\"../reports/script.js\"></script>"
							+ "<script type=\"text/javascript\">function showTcTable(){var lTable = document.getElementById(\"passed-content\");lTable.style.display = (lTable.style.display == \"block\") ? \"none\" : \"block\";}</script>"
							+ "<script src=\"https://cdn.plot.ly/plotly-latest.min.js\"></script>"
							+ "<script src=\"../reports/chart.js\"></script>"
							+ "<div id=\"myDiv\" align=\"left\" style=\"position:inherit;width: 30px; height: 30px;\"><!-- Plotly chart will be drawn inside this DIV --></div><script>var data = [{values: ["
							+ context.getSkippedTests().size()
							+ ","
							+ context.getFailedTests().size()
							+ ","
							+ context.getPassedTests().size()
							+ "],labels: ['Skipped', 'Failed', 'Passed'],type: 'pie'}];var layout = {height: 300,width: 300};Plotly.newPlot('myDiv', data, layout);</script>"
							+ "</head>"
							+ "<body>"
							+ "<table id=\"header\"  align=\"center\" style=\"width:40%\" BORDER=1> "
							+ "<tbody>"
							+ "<tr><td>Tests passed/Failed/Skipped:</td><td>"
							+ context.getPassedTests().size()
							+ "/"
							+ context.getFailedTests().size()
							+ "/"
							+ context.getSkippedTests().size()
							+ "</td></tr>"
							+ "<tr><td>Started on:</td><td>"
							+ context.getStartDate()
							+ "</td></tr>"
							+ "<tr><td>Ended On</td><td>"
							+ context.getEndDate()
							+ "<tr><td>Total Time</td><td>"
							+ (context.getEndDate().getTime() - context
									.getStartDate().getTime())
							+ "</td></tr></tbody>");
					sb.append("<p><br></br></p>");
					sb.append("<p><br></br></p>");
					// Get all Passed Methods
					IResultMap passedTests = context.getPassedTests();
					Set<ITestResult> passedResults = passedTests
							.getAllResults();
					sb.append("<p><br></br></p>");
					sb.append("<p><br></br></p>");
					sb.append("<p><br></br></p>");
					sb.append("<p><br></br></p>");
					int sno = 1;
					sb.append("<p><table class=\"invocation-passed\"  align=\"center\" style=\"width:100%\" BORDER=1>"
							+ "<tbody>"
							+ "<tr><td align=\"center\" colspan=\"9\"><b>PASSED TESTS</b></td></tr>"
							+ "<tr><td><b>Sno</b></td>"
							+ "<td><b>Capabilities</b></td>"
							+ "<td><b>Test Suite Name</b></td>"
							+ "<td><b>Test Case Name</b></td>"
							+ "<td><b>Test Steps</b></td>"
							+ "<td><b>Exception</b></td>"
							+ "<td><b>Result</b></td>"
							+ "<td><b>Time (Seconds)</b></td>"
							+ "<td><b>Screen Shot</b></td></tr>");
					for (ITestResult currentresult : passedResults) {
						sb.append("<TR><TD>"
								+ sno
								+ "</TD>"
								+ "<TD>"
								+ getCapabilities(currentresult.getMethod())
								+ "</TD>"
								+ "<TD>"
								+ context.getName()
								+ "</TD>"
								+ "<TD>"
								+ currentresult.getMethod().getMethodName()
								+ "</TD>"
								+ "<TD>"
								+ getMessage(currentresult.getMethod()
										.getMethodName(), Reporter.getOutput())
								+ "</TD>"
								+ "<TD>"
								+ currentresult.getThrowable()
								+ "</TD>"
								+ "<TD>Pass</TD>"
								+ "<TD>"
								+ (currentresult.getEndMillis() - currentresult
										.getStartMillis())
								+ "</TD>"
								+ "<TD><a href=\""
								+ takeScreenShot(currentresult.getMethod(),
										currentresult.getMethod()
												.getMethodName(), sno)
								+ "\">Screenshot" + "</a></TD></TR>");
						sno++;
					}
					sb.append("</tbody></table></p>");
					// Get all Failed Methods
					IResultMap failedTests = context.getFailedTests();
					Set<ITestResult> failedResults = failedTests
							.getAllResults();
					sb.append("<p><table class=\"invocation-failed\"  align=\"center\" style=\"width:100%\" BORDER=1>"
							+ "<tbody>"
							+ "<tr><td align=\"center\" colspan=\"9\"><b>FAILED TESTS</b></td></tr>"
							+ "<tr><td><b>Sno</b></td>"
							+ "<td><b>Capabilities</b></td>"
							+ "<td><b>Test Suite Name</b></td>"
							+ "<td><b>Test Case Name</b></td>"
							+ "<td><b>Test Steps</b></td>"
							+ "<td><b>Exception</b></td>"
							+ "<td><b>Result</b></td>"
							+ "<td><b>Time (Seconds)</b></td>"
							+ "<td><b>Screen Shot</b></td></tr>");
					for (ITestResult currentresult : failedResults) {
						sb.append("<TR><TD>"
								+ sno
								+ "</TD>"
								+ "<TD>"
								+ getCapabilities(currentresult.getMethod())
								+ "</TD>"
								+ "<TD>"
								+ context.getName()
								+ "</TD>"
								+ "<TD>"
								+ currentresult.getMethod().getMethodName()
								+ "</TD>"
								+ "<TD>"
								+ getMessage(currentresult.getMethod()
										.getMethodName(), Reporter.getOutput())
								+ "</TD>"
								+ "<TD>"
								+ currentresult.getThrowable()
								+ "</TD>"
								+ "<TD>Fail</TD>"
								+ "<TD>"
								+ (currentresult.getEndMillis() - currentresult
										.getStartMillis())
								+ "</TD>"
								+ "<TD><a href=\""
								+ takeScreenShot(currentresult.getMethod(),
										currentresult.getMethod()
												.getMethodName(), sno)
								+ "\">Screenshot" + "</a></TD></TR>");
						sno++;
					}
					sb.append("</tbody></table></p>");
					// Get all Skipped Methods
					IResultMap skipTests = context.getSkippedTests();
					Set<ITestResult> skippedResults = skipTests.getAllResults();
					sb.append("<p><table class=\"invocation-skipped\"  align=\"center\" style=\"width:100%\" BORDER=1>"
							+ "<tbody>"
							+ "<tr><td align=\"center\" colspan=\"9\"><b>SKIPPED TESTS</b></td></tr>"
							+ "<tr><td><b>Sno</b></td>"
							+ "<td><b>Capabilities</b></td>"
							+ "<td><b>Test Suite Name</b></td>"
							+ "<td><b>Test Case Name</b></td>"
							+ "<td><b>Test Steps</b></td>"
							+ "<td><b>Exception</b></td>"
							+ "<td><b>Result</b></td>"
							+ "<td><b>Time (Seconds)</b></td>"
							+ "<td><b>Screen Shot</b></td></tr>");
					for (ITestResult currentresult : skippedResults) {
						sb.append("<TR><TD>"
								+ sno
								+ "</TD>"
								+ "<TD>"
								+ getCapabilities(currentresult.getMethod())
								+ "</TD>"
								+ "<TD>"
								+ context.getName()
								+ "</TD>"
								+ "<TD>"
								+ currentresult.getMethod().getMethodName()
								+ "</TD>"
								+ "<TD>"
								+ getMessage(currentresult.getMethod()
										.getMethodName(), Reporter.getOutput())
								+ "</TD>"
								+ "<TD>"
								+ currentresult.getThrowable()
								+ "</TD>"
								+ "<TD>Skipped</TD>"
								+ "<TD>"
								+ (currentresult.getEndMillis() - currentresult
										.getStartMillis())
								+ "</TD>"
								+ "<TD><a href=\""
								+ takeScreenShot(currentresult.getMethod(),
										currentresult.getMethod()
												.getMethodName(), sno)
								+ "\">Screenshot" + "</a></TD></TR>");
						sno++;
					}
					FileWriter fwriter = new FileWriter(file);
					BufferedWriter bwriter = new BufferedWriter(fwriter);
					bwriter.write(sb.toString());
					bwriter.close();
				}
			}
		} catch (Exception e) {
			System.out.println("Error while Creating a directory..." + e);
		}
	}

	/**
	 * @param methodname
	 * @param messages
	 * @return
	 */
	public Set<String> getMessage(String methodname, List<String> messages) {
		Set<String> data = new HashSet<String>();
		for (String temp : messages) {
			if (temp.contains(methodname)) {
				String[] temparray = temp.split("-");
				if (temparray[1].contains(";")) {
					String[] temparray1 = temparray[1].split(";");
					data.add(temparray1[0]);

				} else {
					data.add(temparray[1]);

				}

			}
		}
		return data;
	}

	public List<String> getCapabilities(ITestNGMethod result) {
		List<String> capList = new ArrayList<String>();
		Object currentClass = result.getInstance();
		WebDriver webDriver = ((TestBase) currentClass).getDriver();
		Capabilities cap = ((RemoteWebDriver) webDriver).getCapabilities();
		capList.add(cap.getBrowserName());
		capList.add(cap.getVersion());
		capList.add(cap.getPlatform().toString());
		return capList;
	}

	public File takeScreenShot(ITestNGMethod result, String methodname, int i) {
		File file = null;
		Object currentClass = result.getInstance();
		WebDriver webDriver = ((TestBase) currentClass).getDriver();
		File scrFile = ((TakesScreenshot) webDriver)
				.getScreenshotAs(OutputType.FILE);
		try {
			file = new File(screenshot + "//" + methodname + "_" + i + ".png");
			FileUtils.copyFile(scrFile, file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file;
	}

}
