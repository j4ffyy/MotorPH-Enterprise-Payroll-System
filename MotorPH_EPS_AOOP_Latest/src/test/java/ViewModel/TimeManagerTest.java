package ViewModel;
import org.junit.Test;
import static org.junit.Assert.*;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Timer;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class TimeManagerTest {
    
    @Test
    public void testStartClock() throws Exception {
        // Setup with real labels
        JLabel dateLabel = new JLabel();
        JLabel timeLabel = new JLabel();
        TimeManager instance = new TimeManager(dateLabel, timeLabel);
        
        // Execute
        instance.startClock();
        
        // Waits for EDT to process the initial update
        SwingUtilities.invokeAndWait(() -> {
            // This empty runnable ensures all pending EDT tasks are completed
        });
        
        // Verify Timer initialization
        Field timerField = TimeManager.class.getDeclaredField("timer");
        timerField.setAccessible(true);
        Timer timer = (Timer) timerField.get(instance);
        assertNotNull("Timer should be initialized", timer);
        
        // Verifies label updates (check format rather than exact values due to timing)
        assertNotNull("Date label should be set", dateLabel.getText());
        assertNotNull("Time label should be set", timeLabel.getText());
        
        
        assertTrue("Date label should match expected format", 
                   dateLabel.getText().matches("[A-Za-z]+ \\d{1,2}, \\d{4}"));
        
        // Time format should be "hh:mm:ss AM/PM"
        assertTrue("Time label should match current time format", 
                   timeLabel.getText().matches("\\d{2}:\\d{2}:\\d{2} [AP]M"));
        
        // Cleanup
        instance.stopClock();
    }
    
    @Test
    public void testStopClock() throws Exception {
        // Setup
        JLabel dateLabel = new JLabel();
        JLabel timeLabel = new JLabel();
        TimeManager instance = new TimeManager(dateLabel, timeLabel);
        
        // Execute
        instance.startClock();
        
        // Wait for initial setup
        SwingUtilities.invokeAndWait(() -> {});
        
        instance.stopClock();
        
        // Verify Timer cleanup
        Field timerField = TimeManager.class.getDeclaredField("timer");
        timerField.setAccessible(true);
        Timer timer = (Timer) timerField.get(instance);
        assertNull("Timer should be null after stop", timer);
    }
    
    @Test
    public void testDateFormatAccuracy() throws Exception {
        JLabel dateLabel = new JLabel();
        JLabel timeLabel = new JLabel();
        TimeManager instance = new TimeManager(dateLabel, timeLabel);
        
        // Start the clock
        instance.startClock();
        
        
        SwingUtilities.invokeAndWait(() -> {
            // This ensures all pending EDT tasks are completed
        });
        
        // Small delay to ensure the timer task has executed
        Thread.sleep(100);
        
        
        SwingUtilities.invokeAndWait(() -> {});
        
        
        assertNotNull("Date label should not be null", dateLabel.getText());
        assertFalse("Date label should not be empty", dateLabel.getText().isEmpty());
        
        // Verify the format matches expected pattern
        assertTrue("Date should match expected format (Month dd, yyyy)", 
                   dateLabel.getText().matches("[A-Za-z]+ \\d{1,2}, \\d{4}"));
        
        // Verifies it's actually today's date by checking it's reasonable
        LocalDate today = LocalDate.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
        String expectedDate = today.format(dateFormatter);
        
        assertEquals("Date should match today's date", expectedDate, dateLabel.getText());
        
        // Cleanup
        instance.stopClock();
    }
}