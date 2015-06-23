package hudson.plugins.violations.types.perlcritic;

import static hudson.plugins.violations.ViolationsReportBuilder.violationsReport;
import static hudson.plugins.violations.types.perlcritic.PerlCriticDescriptor.TYPE_NAME;
import hudson.plugins.violations.model.Severity;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

/**
 * Test PerlCriticParser against some output from running Perl::Critic on
 * itself.
 *
 * @author David McGuire
 */
public class PerlCriticParserTest extends JenkinsRule {
    @Rule
    public JenkinsRule j = new JenkinsRule();

    @Test
    public void testParseLineInformational() {
        PerlCriticParser parser = new PerlCriticParser();
        String fileName = "/usr/local/share/perl/5.10.1/Perl/Critic/UserProfile.pm";
        String message = "Missing \"LICENSE AND COPYRIGHT\" section in POD";
        int line = 310;
        int column = 1;
        String source = "See pages 133,138 of PBP";
        int severity = 2;
        int severityLevel = Severity.MEDIUM_LOW_VALUE;
        String severityName = Severity.NAMES[severityLevel];
        String output = String.format("%s: %s at line %d, column %d.  %s.  (Severity: %d)", fileName, message, line,
                column, source, severity);
        PerlCriticParser.PerlCriticViolation violation = parser.getPerlCriticViolation(output);
        Assert.assertEquals(fileName, violation.getFileName());
        Assert.assertEquals(message, violation.getMessage());
        Assert.assertEquals(line, violation.getLine());
        Assert.assertEquals(column, violation.getColumn());
        Assert.assertEquals(source, violation.getSource());
        Assert.assertEquals(severityName, violation.getSeverity());
        Assert.assertEquals(severityLevel, violation.getSeverityLevel());
        Assert.assertEquals(PerlCriticDescriptor.TYPE_NAME, violation.getType());
    }

    @Test
    public void testParseLineCrucial() {
        PerlCriticParser parser = new PerlCriticParser();
        String fileName = "/usr/local/share/perl/5.10.1/Perl/Critic/TestUtils.pm";
        String message = "Expression form of \"eval\"";
        int line = 161;
        int column = 25;
        String source = "See page 161 of PBP";
        int severity = 5;
        int severityLevel = Severity.HIGH_VALUE;
        String severityName = Severity.NAMES[severityLevel];
        String output = String.format("%s: %s at line %d, column %d.  %s.  (Severity: %d)", fileName, message, line,
                column, source, severity);
        PerlCriticParser.PerlCriticViolation violation = parser.getPerlCriticViolation(output);
        Assert.assertEquals(fileName, violation.getFileName());
        Assert.assertEquals(message, violation.getMessage());
        Assert.assertEquals(line, violation.getLine());
        Assert.assertEquals(column, violation.getColumn());
        Assert.assertEquals(source, violation.getSource());
        Assert.assertEquals(severityName, violation.getSeverity());
        Assert.assertEquals(severityLevel, violation.getSeverityLevel());
        Assert.assertEquals(PerlCriticDescriptor.TYPE_NAME, violation.getType());
    }

    @Test
    public void testParseLineNitPick() {
        PerlCriticParser parser = new PerlCriticParser();
        String fileName = "/usr/local/share/perl/5.10.1/Perl/Critic/ProfilePrototype.pm";
        String message = "Code is not tidy";
        int line = 1;
        int column = 1;
        String source = "See page 33 of PBP";
        int severity = 1;
        int severityLevel = Severity.LOW_VALUE;
        String severityName = Severity.NAMES[severityLevel];
        String output = String.format("%s: %s at line %d, column %d.  %s.  (Severity: %d)", fileName, message, line,
                column, source, severity);
        PerlCriticParser.PerlCriticViolation violation = parser.getPerlCriticViolation(output);
        Assert.assertEquals(fileName, violation.getFileName());
        Assert.assertEquals(message, violation.getMessage());
        Assert.assertEquals(line, violation.getLine());
        Assert.assertEquals(column, violation.getColumn());
        Assert.assertEquals(source, violation.getSource());
        Assert.assertEquals(severityName, violation.getSeverity());
        Assert.assertEquals(severityLevel, violation.getSeverityLevel());
        Assert.assertEquals(PerlCriticDescriptor.TYPE_NAME, violation.getType());
    }

    @Test
    public void testThatPerlCriticFileCanBeParsed() throws Exception {
        violationsReport(TYPE_NAME).reportedIn("**/perlcritic-report.log").perform().assertThat("example.pl")
                .wasReported()
                .reportedViolation(2, "Use a bareword instead", "\"require\" statement with library name as string")
                .reportedViolation(5, "See page 429 of PBP", "Code before strictures are enabled");
    }
}
