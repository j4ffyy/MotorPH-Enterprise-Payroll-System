/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package Repository;

import com.toedter.calendar.JDateChooser;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.swing.JComboBox;

/**
 *
 * @author dashcodes
 */

public class TestUnitFactory {

    public static class TestDateChooser extends JDateChooser {
    private Date selectedDate;

    @Override
    public Date getDate() {
        return selectedDate;
    }

    @Override
    public void setDate(Date date) {
        this.selectedDate = date;
    }
}

public static class TestComboBox<E> extends JComboBox<E> {
    private int selectedIndex = 0;
    private E selectedItem;
    private List<E> items = new ArrayList<>();

    public TestComboBox(E[] items) {
        if (items != null) {
            this.items.addAll(Arrays.asList(items));
        }
        if (!this.items.isEmpty()) {
            this.selectedItem = this.items.get(0);
        }
    }

    @Override
    public int getSelectedIndex() {
        return selectedIndex;
    }

    @Override
    public void setSelectedIndex(int anIndex) {
        if (anIndex >= 0 && anIndex < items.size()) {
            this.selectedIndex = anIndex;
            this.selectedItem = items.get(anIndex);
        } else {
            this.selectedIndex = -1; // Indicate no selection if out of bounds
            this.selectedItem = null;
        }
    }

    @Override
    public E getSelectedItem() {
        return selectedItem;
    }
}
    
}
